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

import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.transformation.java.TransformationJavaMethodsToModularHypergraph
import de.cau.cs.se.evaluation.architecture.transformation.metrics.NamedValue
import de.cau.cs.se.evaluation.architecture.transformation.metrics.ResultModelProvider
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphMetrics
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationHyperedgesOnlyGraph
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationIntermoduleHyperedgesOnlyGraph
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationMaximalInterconnectedGraph
import de.cau.cs.se.evaluation.architecture.views.AnalysisResultView
import java.util.ArrayList
import java.util.Iterator
import java.util.List
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.ui.PartInitException
import org.eclipse.ui.PlatformUI
import org.eclipse.emf.ecore.EObject

class ComplexityAnalysisJob extends Job {
	
	private static val PARALLEL_TASKS = 8 
	
	val List<AbstractTypeDeclaration> classes 
					
	val List<String> dataTypePatterns
	
	val List<String> observedSystemPatterns
	
	// Used in the parallelized version of this
	var volatile Iterator<Node> globalHyperEdgesOnlyGraphNodes
	var volatile List<Hypergraph> resultConnectedNodeGraphs
	
	var volatile double complexity
	
	var volatile List<Hypergraph> globalMetricsSubGraphs
	
	var volatile int globalMetricsSubGraphCounter
	
	var volatile int globalMetricsSubGraphTotal
	
	val IJavaProject project
	
	var TransformationJavaMethodsToModularHypergraph javaToModularHypergraph
	
	var IProgressMonitor monitor
	
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
		this.javaToModularHypergraph = null
	}
	
	protected override IStatus run(IProgressMonitor monitor) {
		this.monitor = monitor
		// set total number of work units
		val result = ResultModelProvider.INSTANCE
		/** construct analysis. */
		this.monitor.beginTask("Processing project", 3)
				
		this.javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(project, classes, dataTypePatterns, observedSystemPatterns, monitor)
		
		this.javaToModularHypergraph.transform
		this.monitor.worked(1)

		result.values.add(new NamedValue(project.project.name + " size of observed system", classes.size))
		result.values.add(new NamedValue(project.project.name + " number of modules", javaToModularHypergraph.modularSystem.modules.size))
		result.values.add(new NamedValue(project.project.name + " number of nodes",javaToModularHypergraph.modularSystem.nodes.size))
		result.values.add(new NamedValue(project.project.name + " number of edges",javaToModularHypergraph.modularSystem.edges.size))
		
		updateView()
						
		/** Preparing different hypergraphs */
		
		// Transformation for MS^(n)
		val transformationMaximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(javaToModularHypergraph.modularSystem)
		// Transformation for MS^*
		val transformationIntermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(javaToModularHypergraph.modularSystem)
		
		monitor.subTask("Create maximal interconnected graph")
		transformationMaximalInterconnectedGraph.transform
		this.monitor.worked(1)
		monitor.subTask("Create intermodule hyperedges only graph")
		transformationIntermoduleHyperedgesOnlyGraph.transform
		this.monitor.worked(1)
		
		/** calculating result metrics */
		// Calculation for S
		val metrics = new TransformationHypergraphMetrics(monitor, "Calculate system size")
		metrics.setSystem(javaToModularHypergraph.modularSystem)
		val systemSize = metrics.calculate

		// Calculation for S -> S^#, S^#_i
		val complexity = calculateComplexity(javaToModularHypergraph.modularSystem, monitor, "Complexity")
		// Calculation for MS^(n) -> MS^(n)#, MS^(n)#_i
		val complexityMaximalInterconnected = calculateComplexity(transformationMaximalInterconnectedGraph.result, 
			monitor, "Maximal interconnected graph complexity"
		)
		// Calculation for MS^* -> MS^*#, MS^*#_i
		val complexityIntermodule = calculateComplexity(transformationIntermoduleHyperedgesOnlyGraph.result, monitor, "Intermodule complexity")	
		
		/** display results */
		result.getValues.add(new NamedValue(project.project.name + " Size", systemSize))
		result.getValues.add(new NamedValue(project.project.name + " Complexity", complexity))
		result.getValues.add(new NamedValue(project.project.name + " Cohesion", complexityIntermodule/complexityMaximalInterconnected))
		result.getValues.add(new NamedValue(project.project.name + " Coupling", complexityIntermodule))
				
		monitor.done()
		
		updateView()
							
		return Status.OK_STATUS
	}
	
	private def updateView() {
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
			
	/**
	 * Calculate for a given modular hyper graph:
	 * - hyperedges only graph
	 * - hyperedges only graphs for each node in the graph which is connected to the i-th node
	 * - calculate the size of all graphs
	 * - calculate the complexity
	 * 
	 * @param input a modular system
	 */
	private def calculateComplexity(Hypergraph input, IProgressMonitor monitor, String message) {
		monitor.beginTask(message + " - S^#", input.nodes.size + 1)
		/** S^# (hyperedges only graph) */
		val transformationHyperedgesOnlyGraph = new TransformationHyperedgesOnlyGraph(input)
		transformationHyperedgesOnlyGraph.transform
		monitor.worked(1)
		
		/** S^#_i (hyperedges only graphs for each node graph) */
		//val transformationConnectedNodeHyperedgesOnlyGraph = 
		//	new TransformationConnectedNodeHyperedgesOnlyGraph(transformationHyperedgesOnlyGraph.result)
		
		//val resultConnectedNodeGraphs = new ArrayList<Hypergraph>() 
		
		//var int i = 0
		
		//for (Node node : transformationHyperedgesOnlyGraph.result.nodes) {
		//	monitor?.subTask(message + " - S^#_" + i)
			
		//	transformationConnectedNodeHyperedgesOnlyGraph.node = node
		//	transformationConnectedNodeHyperedgesOnlyGraph.transform
		//	resultConnectedNodeGraphs.add(transformationConnectedNodeHyperedgesOnlyGraph.result)
		//	i++			
		//}
	
		var List<Job> jobs = new ArrayList<Job>()
		
		resultConnectedNodeGraphs = new ArrayList<Hypergraph>() 
		globalHyperEdgesOnlyGraphNodes = transformationHyperedgesOnlyGraph.result.nodes.iterator
				
		for (var int j=0;j<PARALLEL_TASKS;j++) {
			val job = new CalculationSubJob("S^#_i " + j, this, transformationHyperedgesOnlyGraph.result)
			jobs.add(job)
			job.schedule
		}
		
		jobs.forEach[it.join]

		/** calculate size of S^# and S^#_i */
		val complexity = calculateComplexity(transformationHyperedgesOnlyGraph.result, resultConnectedNodeGraphs, monitor)
				
		return complexity
	}
	
	/**
	 * Used for the parallelization. Return the next task
	 */
	synchronized def getNextConnectedNodeTask() {
		if (globalHyperEdgesOnlyGraphNodes.hasNext)
			globalHyperEdgesOnlyGraphNodes.next
		else
			null
	}
	
	/**
	 * Used for the parallelization. Deliver the result
	 */
	synchronized def deliverResult(Hypergraph hypergraph) {
		this.monitor.worked(1)
		resultConnectedNodeGraphs.add(hypergraph)
	}
	
	
	/**
	 * Calculate complexity.
	 */
	private def double calculateComplexity(Hypergraph hypergraph, List<Hypergraph> subgraphs, IProgressMonitor monitor) {
		//val metrics = new TransformationHypergraphMetrics(monitor)
		//var double complexity = 0
		complexity = 0
		///** Can start at zero, as we ignore environment node by using system and not system graph. */
		//for (var int i=0;i < hypergraph.nodes.size;i++) {
		//	metrics.setSystem(subgraphs.get(i))					
		//	complexity += metrics.calculate
		//}
		
		globalMetricsSubGraphs = subgraphs
		globalMetricsSubGraphCounter = 0
		globalMetricsSubGraphTotal = hypergraph.nodes.size
		
		var List<Job> jobs = new ArrayList<Job>()
		for (var int j=0;j<PARALLEL_TASKS;j++) {
			val job = new MetricsSubJob("Metrics " + j, this)
			jobs.add(job)
			job.schedule
		}
		
		jobs.forEach[it.join]
		
		val metrics = new TransformationHypergraphMetrics(monitor, "S^#")
		/** the rest stays in both versions */
		metrics.setSystem(hypergraph)
		complexity -= metrics.calculate
					
		return complexity
	}
		
	synchronized def getNextSubgraph() {
		if (globalMetricsSubGraphCounter<globalMetricsSubGraphTotal) {
			val result = globalMetricsSubGraphs.get(globalMetricsSubGraphCounter)
			globalMetricsSubGraphCounter++
			return result
		} else
			return null
	}
	
	synchronized def deliverMetricsResult(double d) {
		complexity += d
	}	
	
}