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
package de.cau.cs.se.software.evaluation.jobs

import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationConnectedNodeHyperedgesOnlyGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize
import org.eclipse.core.runtime.Status
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Node

/**
 * Determine a connected node hyperedges only graph and calculate
 * its size.
 */
class ConnectedNodeHyperedgeOnlySizeJob extends Job {
		
	ICalculationTask parent
	
	Hypergraph input
		
	new(String name, ICalculationTask parent, Hypergraph input) {
		super(name)
		this.parent = parent
		this.input = input
	}
	
	override protected run(IProgressMonitor monitor) {
		var Node node 
		var int i = 0
		val connectedNodeHyperedgesOnlyGraph = new TransformationConnectedNodeHyperedgesOnlyGraph(monitor)
		val hypergraphSize = new TransformationHypergraphSize(monitor)
		
		while ((node = parent.getNextConnectedNodeTask) != null) {
			if (monitor.canceled)
				return Status.CANCEL_STATUS
			monitor.beginTask("Determine S^#_" + i, hypergraphSize.workEstimate(input) + 
				connectedNodeHyperedgesOnlyGraph.workEstimate(input)
			)
			// S^#_i 
			connectedNodeHyperedgesOnlyGraph.node = node
			val connectedNodeGraph = connectedNodeHyperedgesOnlyGraph.generate(input)
						
			parent.deliverConnectedNodeHyperedgesOnlySizeResult(hypergraphSize.generate(connectedNodeGraph))
			i++
		}
		
		monitor.done
		
		return Status.OK_STATUS
	}
	
}