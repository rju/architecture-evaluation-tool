/***************************************************************************
 * Copyright (C) 2015 Reiner Jung
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
package de.cau.cs.se.software.evaluation.jobs

import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHyperedgesOnlyGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize
import java.util.Iterator
import de.cau.cs.se.software.evaluation.hypergraph.Node
import java.util.List
import org.eclipse.core.runtime.jobs.Job
import java.util.ArrayList

/**
 * Calculate the complexity of a hypergraph.
 * 
 * @author Reiner Jung
 */
class CalculateComplexity implements ICalculationTask {
	
	static val PARALLEL_TASKS = 8
	
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
		
				
		val List<Job> jobs = new ArrayList<Job>()
		
		/** construct S^#_i and calculate the size of S^#_i */
		for (var int j=0;j<PARALLEL_TASKS;j++) {
			val job = new ConnectedNodeHyperedgeOnlySizeJob("S^#_i " + j, this, hyperedgesOnlyGraph.result)
			jobs.add(job)
			job.schedule
		}
		
		if (this.monitor.canceled) {
			jobs.forEach[it.cancel]
			return 0.0
		}
		
		/** calculate size of S^# and S^#_i */
		monitor.subTask("Determine Size(S^#)")
		size.generate(hyperedgesOnlyGraph.result)
		
		if (this.monitor.canceled) {
			jobs.forEach[it.cancel]
			return 0.0
		}
		/** wait for subtasks. */
		jobs.forEach[it.join]

		this.complexity -= size.result
				
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