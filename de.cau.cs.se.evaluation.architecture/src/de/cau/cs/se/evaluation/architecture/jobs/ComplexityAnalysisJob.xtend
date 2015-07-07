/***************************************************************************
 * Copyright 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package de.cau.cs.se.evaluation.architecture.jobs

import de.cau.cs.se.evaluation.architecture.transformation.java.TransformationJavaMethodsToModularHypergraph
import de.cau.cs.se.evaluation.architecture.transformation.metrics.NamedValue
import de.cau.cs.se.evaluation.architecture.transformation.metrics.ResultModelProvider
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphSize
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationIntermoduleHyperedgesOnlyGraph
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationMaximalInterconnectedGraph
import de.cau.cs.se.evaluation.architecture.views.AnalysisResultView
import java.util.List
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.ui.PartInitException
import org.eclipse.ui.PlatformUI

class ComplexityAnalysisJob extends Job {

	val List<AbstractTypeDeclaration> classes 
					
	val List<String> dataTypePatterns
	
	val List<String> observedSystemPatterns
		
	val IJavaProject project
			
	/**
	 * The constructor scans the selection for files.
	 * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
	 */
	public new (IJavaProject project, List<AbstractTypeDeclaration> classes, List<String> dataTypePatterns, List<String> observedSystemPatterns
	) {
		super("Analysis Complexity")
		this.project = project
		this.classes = classes
		this.dataTypePatterns = dataTypePatterns
		this.observedSystemPatterns = observedSystemPatterns
	}
	
	protected override IStatus run(IProgressMonitor monitor) {
		// set total number of work units
		val result = ResultModelProvider.INSTANCE
		/** construct analysis. */
		monitor.beginTask("Processing project", 3)
				
		val javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(project, classes, dataTypePatterns, observedSystemPatterns, monitor)
		javaToModularHypergraph.transform
		
		monitor.worked(1)

		result.values.add(new NamedValue(project.project.name, "size of observed system", classes.size))
		result.values.add(new NamedValue(project.project.name, "number of modules", javaToModularHypergraph.modularSystem.modules.size))
		result.values.add(new NamedValue(project.project.name, "number of nodes", javaToModularHypergraph.modularSystem.nodes.size))
		result.values.add(new NamedValue(project.project.name, "number of edges", javaToModularHypergraph.modularSystem.edges.size))
		
		updateView(javaToModularHypergraph)
						
		/** Preparing different hypergraphs */
		
		// Transformation for MS^(n)
		val transformationMaximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(javaToModularHypergraph.modularSystem)
		// Transformation for MS^*
		val transformationIntermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(javaToModularHypergraph.modularSystem)
		
		monitor.subTask("Create maximal interconnected graph")
		transformationMaximalInterconnectedGraph.transform
		monitor.worked(1)
		monitor.subTask("Create intermodule hyperedges only graph")
		transformationIntermoduleHyperedgesOnlyGraph.transform
		monitor.worked(1)
		monitor.subTask("")
		
		/** calculating result metrics */
		// Calculation for S
		val transformationHypergraphSize = new TransformationHypergraphSize(monitor)
		transformationHypergraphSize.name = "Calculate system size"
		transformationHypergraphSize.system = javaToModularHypergraph.modularSystem
		val systemSize = transformationHypergraphSize.calculate
		
		/** Calculate graph complexities */
		val calculateComplexity = new CalculateComplexity(monitor)

		// Calculation for S -> S^#, S^#_i
		val complexity = calculateComplexity.calculate(javaToModularHypergraph.modularSystem, "Primary graph complexity")
		// Calculation for MS^(n) -> MS^(n)#, MS^(n)#_i
		val complexityMaximalInterconnected = calculateComplexity.calculate(transformationMaximalInterconnectedGraph.result, 
			"Maximal interconnected graph complexity"
		)
		// Calculation for MS^* -> MS^*#, MS^*#_i
		val complexityIntermodule = calculateComplexity.calculate(transformationIntermoduleHyperedgesOnlyGraph.result, "Intermodule complexity")	
		
		/** display results */
		result.getValues.add(new NamedValue(project.project.name, "hypergraph size", systemSize))
		result.getValues.add(new NamedValue(project.project.name, "hypergraph complexity", complexity))
		result.getValues.add(new NamedValue(project.project.name, "inter module cohesion", complexityIntermodule/complexityMaximalInterconnected))
		result.getValues.add(new NamedValue(project.project.name, "inter module coupling", complexityIntermodule))
				
		monitor.done()
		
		updateView(javaToModularHypergraph)
							
		return Status.OK_STATUS
	}
	
	private def updateView(TransformationJavaMethodsToModularHypergraph javaToModularHypergraph) {
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
       		public override void run() {
	           try { 
					val part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AnalysisResultView.ID)
					(part as AnalysisResultView).setGraph(javaToModularHypergraph.modularSystem)
					(part as AnalysisResultView).setProject(project)
					(part as AnalysisResultView).update()
	           } catch (PartInitException e) {
	                e.printStackTrace()
	           }
	    	}
     	})
	}
			
	
	
}