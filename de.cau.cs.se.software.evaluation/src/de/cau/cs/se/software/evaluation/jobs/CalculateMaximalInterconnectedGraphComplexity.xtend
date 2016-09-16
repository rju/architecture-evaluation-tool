package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHyperedgesOnlyGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize
import java.util.Iterator
import org.eclipse.core.runtime.IProgressMonitor

/**
 * This class is a modified version of CalculateComplexity specifically for
 * the maximal interconnected graph. It generates the complexity of a maximal
 * interconnected graph much faster as it uses the symmetric properties of the
 * maximal interconnected graph to reduce necessary calculations.
 * 
 * @author Reiner Jung 
 */
class CalculateMaximalInterconnectedGraphComplexity implements ICalculationTask {
		
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
		val hyperedgesOnlyGraph = new TransformationHyperedgesOnlyGraph(monitor)
		val size = new TransformationHypergraphSize(monitor)
				
		this.monitor.beginTask(message, hyperedgesOnlyGraph.workEstimate(input) + size.workEstimate(input))
		
		/** S^# (hyperedges only graph) */
		hyperedgesOnlyGraph.generate(input)
		
		this.monitor.worked(hyperedgesOnlyGraph.workEstimate(input))
		if (this.monitor.canceled)
			return 0
		
		/** S^#_i (hyperedges only graphs for each node graph) */	
		globalHyperEdgesOnlyGraphNodes = hyperedgesOnlyGraph.result.nodes.iterator
		
		complexity = 0
		
		if (this.monitor.canceled)
			return 0

		/**
		 * For arbitrary graphs, each subgraph must be calculated separated,
		 * as they differ in structure. However, for the maximal interconnected
		 * graph, each subgraph is identical in shape. Therefore it is sufficient to
		 * calculate one of them and then multiply the result by the number of nodes.
		 */
				
		/** construct S^#_i and calculate the size of S^#_i */
		val job = new ConnectedNodeHyperedgeOnlySizeJob("S^#_i (once)", this, hyperedgesOnlyGraph.result)
		job.schedule
		
		if (this.monitor.canceled) {
			job.cancel
			return 0.0
		}
		
		/** calculate size of S^# and S^#_i */
		monitor.subTask("Determine Size(S^#)")
		size.generate(hyperedgesOnlyGraph.result)
		
		if (this.monitor.canceled) {
			job.cancel
			return 0.0
		}
		/** wait for subtask. */
		job.join

		this.complexity -= (size.result * globalHyperEdgesOnlyGraphNodes.size)
				
		return this.complexity
	}
	
	/**
	 * Used for the parallelization. Return the next task
	 */
	override synchronized getNextConnectedNodeTask() {
		if (globalHyperEdgesOnlyGraphNodes.hasNext)
			globalHyperEdgesOnlyGraphNodes.next
		else
			null
	}
	
	override synchronized deliverConnectedNodeHyperedgesOnlySizeResult(double size) {
		complexity += size
		monitor.worked(1)
	}
	
}