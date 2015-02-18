package de.cau.cs.se.evaluation.architecture.transformation.processing

import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace

class TransformationConnectedNodeHyperedgesOnlyGraph implements ITransformation {
	
	val Hypergraph hypergraph
	var Hypergraph resultHypergraph = null
	
	Node startNode
	
	new (Hypergraph hypergraph) {
		this.hypergraph = hypergraph
	}
	
	def void setNode(Node startNode) {
		this.startNode = startNode
	}
	
	def getConnectedNodeHyperedgesOnlyGraph() {
		return this.resultHypergraph
	}
	
	/**
	 * Find all nodes connected to the start node and create a graph for it.
	 */
	override transform() {
		
		// find start node
		val selectedNode = if (hypergraph.nodes.contains(startNode)) startNode else null
		if (selectedNode != null) {	
			resultHypergraph = HypergraphFactory.eINSTANCE.createHypergraph
			resultHypergraph.nodes.add(TransformationHelper.deriveNode(selectedNode))
			// find all connected edges and copy them
			selectedNode.edges.forEach[edge | resultHypergraph.edges.add(TransformationHelper.deriveEdge(edge))]
			// find all connected nodes
			resultHypergraph.edges.forEach[edge | createAndLinkNodesConnectedToEdge(edge, hypergraph.nodes, resultHypergraph.nodes)]
		} 
	}
	
	def createAndLinkNodesConnectedToEdge(Edge edge, EList<Node> originalNodes, EList<Node> nodes) {
		val originalEdge = (edge.derivedFrom as EdgeTrace).edge
		for (Node originalNode : originalNodes) {
			if (originalNode.edges.contains(originalEdge)) {
				var newNode = nodes.findFirst[node | (node.derivedFrom as NodeTrace).node == originalNode]
				if (newNode == null) {
					newNode = TransformationHelper.deriveNode(originalNode)
					nodes.add(newNode)
				}
				newNode.edges.add(edge)
			}
		}
	}
	
}