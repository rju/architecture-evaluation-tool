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
package de.cau.cs.se.software.evaluation.transformation.metric

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import de.cau.cs.se.software.evaluation.hypergraph.EdgeTrace
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.software.evaluation.hypergraph.NodeTrace
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper

/**
 * Create a hypergraph for a given hypergraph which contains only
 * those nodes which are connected by edges with the startNode. 
 */
class TransformationConnectedNodeHyperedgesOnlyGraph extends AbstractTransformation<Hypergraph, Hypergraph> {
		
	var Node startNode
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
			
	def void setNode(Node startNode) {
		this.startNode = startNode
	}
	
	/**
	 * Find all nodes connected to the start node and create a graph for it.
	 */
	override generate(Hypergraph input) {
		// find start node
		val selectedNode = if (input.nodes.contains(startNode)) startNode else null
		if (selectedNode != null) {	
			this.result = HypergraphFactory.eINSTANCE.createHypergraph
			this.result.nodes.add(HypergraphCreationHelper.deriveNode(selectedNode))
			// find all connected edges and copy them
			selectedNode.edges.forEach[edge | this.result.edges.add(HypergraphCreationHelper.deriveEdge(edge))]
			// find all connected nodes
			this.result.edges.forEach[edge | createAndLinkNodesConnectedToEdge(edge, input.nodes, this.result.nodes)]
			
			return this.result
		} else
			null
	}
	
	private def createAndLinkNodesConnectedToEdge(Edge edge, EList<Node> originalNodes, EList<Node> nodes) {
		val originalEdge = (edge.derivedFrom as EdgeTrace).edge
		for (Node originalNode : originalNodes) {
			if (originalNode.edges.contains(originalEdge)) {
				var newNode = nodes.findFirst[node | (node.derivedFrom as NodeTrace).node == originalNode]
				if (newNode == null) {
					newNode = HypergraphCreationHelper.deriveNode(originalNode)
					nodes.add(newNode)
				}
				newNode.edges.add(edge)
			}
		}
	}
	
}