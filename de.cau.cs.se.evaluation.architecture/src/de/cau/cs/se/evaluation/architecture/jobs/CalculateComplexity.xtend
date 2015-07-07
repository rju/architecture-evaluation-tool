package de.cau.cs.se.evaluation.architecture.jobs

import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationHyperedgesOnlyGraph
import java.util.Iterator
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import java.util.List
import org.eclipse.core.runtime.jobs.Job
import java.util.ArrayList
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphSize

class CalculateComplexity {
	
	private static val PARALLEL_TASKS = 8
	
    /** Used in the parallelized version of this. */
	var volatile Iterator<Node> globalHyperEdgesOnlyGraphNodes
	
	var volatile double complexity
	
	val IProgressMonitor monitor
	
	new(IProgressMonitor monitor) {
		this.monitor = monitor
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
	def calculate(Hypergraph input, String message) {
		monitor.beginTask(message + " - S^#", input.nodes.size + 1)
		/** S^# (hyperedges only graph) */
		val transformationHyperedgesOnlyGraph = new TransformationHyperedgesOnlyGraph(monitor)
		transformationHyperedgesOnlyGraph.input = input
		transformationHyperedgesOnlyGraph.transform
		monitor.worked(1)
		
		/** S^#_i (hyperedges only graphs for each node graph) */	
		globalHyperEdgesOnlyGraphNodes = transformationHyperedgesOnlyGraph.result.nodes.iterator
		
		complexity = 0
		
		val List<Job> jobs = new ArrayList<Job>()
		
		/** construct S^#_i and calculate the size of S^#_i */
		for (var int j=0;j<PARALLEL_TASKS;j++) {
			val job = new ConnectedNodeHyperedgeOnlySizeJob("S^#_i " + j, this, transformationHyperedgesOnlyGraph.result)
			jobs.add(job)
			job.schedule
		}
		
		jobs.forEach[it.join]

		/** calculate size of S^# and S^#_i */
		val size = new TransformationHypergraphSize(monitor)
		size.name = "S^#"
		size.input = transformationHyperedgesOnlyGraph.result
		
		this.complexity -= size.transform
				
		return this.complexity
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
	
	synchronized def deliverConnectedNodeHyperedgesOnlySizeResult(double size) {
		this.monitor.worked(1)
		complexity += size
	}	
}