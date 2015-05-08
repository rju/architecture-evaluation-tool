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

class ComplexityAnalysisJob extends Job {
	
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
		monitor.beginTask("Processing project", 0)
				
		val javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(project, classes, dataTypePatterns, observedSystemPatterns, monitor)
		
		javaToModularHypergraph.transform

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
		monitor.subTask("Create intermodule hyperedges only graph")
		transformationIntermoduleHyperedgesOnlyGraph.transform
		
		/** calculating result metrics */
		monitor.beginTask("Calculating metrics", 1+3*3)
		// Calculation for S
		val metrics = new TransformationHypergraphMetrics(monitor)
		metrics.setSystem(javaToModularHypergraph.modularSystem)
		monitor.subTask("System size")
		val systemSize = metrics.calculate
		monitor.worked(1)
		
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
					(part as AnalysisResultView).update(ResultModelProvider.INSTANCE)
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
		monitor?.subTask(message + " - S^#")
		// S^#
		val transformationHyperedgesOnlyGraph = new TransformationHyperedgesOnlyGraph(input)
		transformationHyperedgesOnlyGraph.transform
		monitor?.worked(1)
		
		// S^#_i
		//val transformationConnectedNodeHyperedgesOnlyGraph = 
		//	new TransformationConnectedNodeHyperedgesOnlyGraph(transformationHyperedgesOnlyGraph.result)
		
		//val resultConnectedNodeGraphs = new ArrayList<Hypergraph>() 
		
		// var int i = 0
	
		var List<Job> jobs = new ArrayList<Job>()
		
		resultConnectedNodeGraphs = new ArrayList<Hypergraph>() 
		globalHyperEdgesOnlyGraphNodes = transformationHyperedgesOnlyGraph.result.nodes.iterator
				
		for (var int j=0;j<8;j++) {
			val job = new CalculationSubJob("S^#_i " + j, this, transformationHyperedgesOnlyGraph.result)
			jobs.add(job)
			job.schedule
		}
		
		jobs.forEach[it.join]
				
		//for (Node node : transformationHyperedgesOnlyGraph.result.nodes) {
		//	monitor?.subTask(message + " - S^#_" + i)
			
		//	transformationConnectedNodeHyperedgesOnlyGraph.node = node
		//	transformationConnectedNodeHyperedgesOnlyGraph.transform
		//	resultConnectedNodeGraphs.add(transformationConnectedNodeHyperedgesOnlyGraph.result)
		//	i++			
		//}
		monitor?.worked(1)
		
		val metrics = new TransformationHypergraphMetrics(monitor)
		
		metrics.setSystem(transformationHyperedgesOnlyGraph.result)
		monitor?.subTask(message + " - calculate")
		val complexity = calculateComplexity(transformationHyperedgesOnlyGraph.result, resultConnectedNodeGraphs, monitor)
		monitor?.worked(1)
		
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
		for (var int j=0;j<8;j++) {
			val job = new MetricsSubJob("Metrics " + j, this)
			jobs.add(job)
			job.schedule
		}
		
		jobs.forEach[it.join]
		
		val metrics = new TransformationHypergraphMetrics(monitor)
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