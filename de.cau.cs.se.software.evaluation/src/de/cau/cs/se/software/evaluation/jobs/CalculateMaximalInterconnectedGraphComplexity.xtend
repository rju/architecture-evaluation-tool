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

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationConnectedNodeHyperedgesOnlyGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHyperedgesOnlyGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job

/**
 * This class is a modified version of CalculateComplexity specifically for
 * the maximal interconnected graph. It generates the complexity of a maximal
 * interconnected graph much faster as it uses the symmetric properties of the
 * maximal interconnected graph to reduce necessary calculations.
 * 
 * @author Reiner Jung 
 */
class CalculateMaximalInterconnectedGraphComplexity {
		
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
				
		this.monitor.beginTask(message, hyperedgesOnlyGraph.workEstimate(input))
		
		/** S^# (hyperedges only graph) */
		hyperedgesOnlyGraph.generate(input)
		
		this.monitor.worked(hyperedgesOnlyGraph.workEstimate(input))

		if (this.monitor.canceled)
			return 0
		
		/** S^#_i (hyperedges only graphs for each node graph) */	

		/**
		 * For arbitrary graphs, each subgraph must be calculated separated,
		 * as they differ in structure. However, for the maximal interconnected
		 * graph, each subgraph is identical in shape. Therefore it is sufficient to
		 * calculate one of them and then multiply the result by the number of nodes.
		 */
				
		/** construct S^#_i and calculate the size of S^#_i */
		val systemHashGraphJob = new Job("System graph computation") {
			
			private double size
			
			override protected run(IProgressMonitor monitor) {
				val connectedNodeHyperedgesOnlyGraph = new TransformationConnectedNodeHyperedgesOnlyGraph(monitor)
				val hypergraphSize = new TransformationHypergraphSize(monitor)
				
				monitor.beginTask("Compute size for all S^#_n", hypergraphSize.workEstimate(hyperedgesOnlyGraph.result) + 
					connectedNodeHyperedgesOnlyGraph.workEstimate(hyperedgesOnlyGraph.result)
				)
				
				// S^#_i 
				connectedNodeHyperedgesOnlyGraph.startNode = hyperedgesOnlyGraph.result.nodes.get(0)
				val connectedNodeGraph = connectedNodeHyperedgesOnlyGraph.generate(hyperedgesOnlyGraph.result)
				size = hypergraphSize.generate(connectedNodeGraph)
				
				return Status.OK_STATUS
			}
			
			def double getSize() {
				return size
			}
		}
				
		/** calculate size of S^# and S^#_i */
		val hyperEdgesOnlyGraphJob = new Job("Hyperedges only graph size computation") {
			
			private double size
			
			override protected run(IProgressMonitor monitor) {
				val hypergraphSize = new TransformationHypergraphSize(monitor)
				
				monitor.beginTask("Compute Size(S^#)", hypergraphSize.workEstimate(hyperedgesOnlyGraph.result))
				
				size = hypergraphSize.generate(hyperedgesOnlyGraph.result)
							
				return Status.OK_STATUS
			}
			
			def double getSize() {
				return size
			}
		}
		
		systemHashGraphJob.schedule
		hyperEdgesOnlyGraphJob.schedule
		
		systemHashGraphJob.join
		hyperEdgesOnlyGraphJob.join
		
		return (systemHashGraphJob.size * hyperedgesOnlyGraph.result.nodes.size) - hyperEdgesOnlyGraphJob.size
	}
	
	
}
