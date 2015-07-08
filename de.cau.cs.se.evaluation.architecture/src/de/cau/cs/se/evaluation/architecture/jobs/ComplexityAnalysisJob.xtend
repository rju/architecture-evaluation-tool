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
import de.cau.cs.se.evaluation.architecture.views.NamedValue
import de.cau.cs.se.evaluation.architecture.views.ResultModelProvider
import de.cau.cs.se.evaluation.architecture.transformation.metric.TransformationHypergraphSize
import de.cau.cs.se.evaluation.architecture.transformation.metric.TransformationIntermoduleHyperedgesOnlyGraph
import de.cau.cs.se.evaluation.architecture.transformation.metric.TransformationMaximalInterconnectedGraph
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
	public new (IJavaProject project, List<AbstractTypeDeclaration> classes,
		List<String> dataTypePatterns, List<String> observedSystemPatterns
	) {
		super("Analysis Complexity")
		this.project = project
		this.classes = classes
		this.dataTypePatterns = dataTypePatterns
		this.observedSystemPatterns = observedSystemPatterns
	}
	
	protected override IStatus run(IProgressMonitor monitor) {
		val result = ResultModelProvider.INSTANCE
		
		result.values.add(new NamedValue(project.project.name, "size of observed system", this.classes.size))
		updateView(null)
				
		/** construct analysis. */				
		val javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(project, dataTypePatterns, observedSystemPatterns, monitor)
		
		javaToModularHypergraph.input = this.classes
		javaToModularHypergraph.transform
		
		result.values.add(new NamedValue(project.project.name, "number of modules", javaToModularHypergraph.result.modules.size))
		result.values.add(new NamedValue(project.project.name, "number of nodes", javaToModularHypergraph.result.nodes.size))
		result.values.add(new NamedValue(project.project.name, "number of edges", javaToModularHypergraph.result.edges.size))
		
		updateView(javaToModularHypergraph)
							
		/** Determine maximal interconnected modular graph MS^(n) */
		val maximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(monitor)
		maximalInterconnectedGraph.input = javaToModularHypergraph.result
		maximalInterconnectedGraph.transform
		
		/** Determine intermodule hyperedges only modular graph for MS^* */
		val intermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(monitor)
		intermoduleHyperedgesOnlyGraph.input = javaToModularHypergraph.result
		intermoduleHyperedgesOnlyGraph.transform

		/** Calculating system size based on input modular hyper graph */
		val hypergraphSize = new TransformationHypergraphSize(monitor)
		hypergraphSize.name = "Calculate system size"
		hypergraphSize.input = javaToModularHypergraph.result
		hypergraphSize.transform

		result.getValues.add(new NamedValue(project.project.name, "hypergraph size", hypergraphSize.result))
		updateView(javaToModularHypergraph)
				
		/** Calculate graph complexities */
		val calculateComplexity = new CalculateComplexity(monitor)

		/** Calculation for S -> S^#, S^#_i */
		val complexity = calculateComplexity.calculate(javaToModularHypergraph.result, "Calculate system's hypergraph complexity")
		
		result.getValues.add(new NamedValue(project.project.name, "hypergraph complexity", complexity))
		updateView(javaToModularHypergraph)
		
		/** Calculation for MS^(n) -> MS^(n)#, MS^(n)#_i */
		val complexityMaximalInterconnected = calculateComplexity.calculate(maximalInterconnectedGraph.result, 
			"Calculate maximal interconnected graph complexity"
		)
		/** Calculation for MS^* -> MS^*#, MS^*#_i */
		val complexityIntermodule = calculateComplexity.calculate(intermoduleHyperedgesOnlyGraph.result, "Calculate intermodule complexity")	
		
		/** display results */
		result.getValues.add(new NamedValue(project.project.name, "inter module cohesion", complexityIntermodule/complexityMaximalInterconnected))
		result.getValues.add(new NamedValue(project.project.name, "inter module coupling", complexityIntermodule))
				
		monitor.done()
		
		updateView(javaToModularHypergraph)
							
		return Status.OK_STATUS
	}
	
	/**
	 * Update the analysis view after updating its content.
	 */
	private def updateView(TransformationJavaMethodsToModularHypergraph javaToModularHypergraph) {
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
       		public override void run() {
	           try { 
					val part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AnalysisResultView.ID)
					if (javaToModularHypergraph != null) (part as AnalysisResultView).setGraph(javaToModularHypergraph.result)
					(part as AnalysisResultView).setProject(project)
					(part as AnalysisResultView).update()
	           } catch (PartInitException e) {
	                e.printStackTrace()
	           }
	    	}
     	})
	}
			
	
	
}