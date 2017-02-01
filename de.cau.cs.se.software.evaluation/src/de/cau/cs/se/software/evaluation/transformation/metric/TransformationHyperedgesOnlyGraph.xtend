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
package de.cau.cs.se.software.evaluation.transformation.metric

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import org.eclipse.core.runtime.IProgressMonitor
import java.util.HashMap
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationFactory

/**
 * Copy only connected nodes to the result graph.
 * 
 * @author Reiner Jung
 */
class TransformationHyperedgesOnlyGraph extends AbstractTransformation<Hypergraph, Hypergraph> {
			
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(Hypergraph input) {
		val edgeMap = new HashMap<Edge,Edge>
		
		this.result = HypergraphFactory.eINSTANCE.createHypergraph
		for (Edge edge : input.edges) {
			val derivedEdge = HypergraphCreationFactory.deriveEdge(edge)
			edgeMap.put(edge, derivedEdge)
			this.result.edges.add(derivedEdge)
		}
		for (Node node : input.nodes) {
			if (!monitor.canceled) {			
				if (node.edges.size > 0) {
					val resultNode = HypergraphCreationFactory.deriveNode(node)
					node.edges.forEach[edge | 
						resultNode.edges.add(edgeMap.get(edge))
					]
					this.result.nodes.add(resultNode)
				}
			}
		}
		
		return this.result		
	}
	
	override workEstimate(Hypergraph input) {
		if (input.nodes.size == 0) {
			println("Warning: hypergraph is empty")
			return 1
		}
		
		input.edges.size + input.nodes.map[it.edges.size].reduce[p1, p2| p1 + p2]
	}

	
}