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

import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationConnectedNodeHyperedgesOnlyGraph
import org.eclipse.core.runtime.Status
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphSize

/**
 * Determine a connected node hyperedges only graph and calculate
 * its size.
 */
class ConnectedNodeHyperedgeOnlySizeJob extends Job {
		
	CalculateComplexity parent
	
	Hypergraph input
		
	new(String name, CalculateComplexity parent, Hypergraph input) {
		super(name)
		this.parent = parent
		this.input = input
	}
	
	override protected run(IProgressMonitor monitor) {
		var Node node 
		var int i = 0
		val transformationConnectedNodeHyperedgesOnlyGraph = new TransformationConnectedNodeHyperedgesOnlyGraph(monitor)
		transformationConnectedNodeHyperedgesOnlyGraph.input = input
		val transformationHypergraphSize = new TransformationHypergraphSize(monitor)
		
		while ((node = parent.getNextConnectedNodeTask) != null) {
			monitor.beginTask("Determine S^#_" + i + ")", 0)
			// S^#_i	
			transformationConnectedNodeHyperedgesOnlyGraph.node = node
			transformationHypergraphSize.name = "Determine Size(S^#_" + i + ")"
			transformationHypergraphSize.input = transformationConnectedNodeHyperedgesOnlyGraph.transform
			
			parent.deliverConnectedNodeHyperedgesOnlySizeResult(transformationHypergraphSize.transform)
		}
		
		monitor.done
		
		return Status.OK_STATUS
	}
	
}