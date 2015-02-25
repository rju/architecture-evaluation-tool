package de.cau.cs.se.evaluation.architecture.transformation

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Module
import de.cau.cs.se.evaluation.architecture.hypergraph.Node

class TransformationHelper {
	
	def static deriveNode(Node node) {
		val resultNode = HypergraphFactory.eINSTANCE.createNode
		resultNode.name = node.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace
		derivedFrom.node = node
		resultNode.derivedFrom = derivedFrom
		
		return resultNode
	}
	
	def static deriveEdge(Edge edge) {
		val resultEdge = HypergraphFactory.eINSTANCE.createEdge
		resultEdge.name = edge.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace
		derivedFrom.edge = edge
		resultEdge.derivedFrom = derivedFrom
		
		return resultEdge
	}
	
	def static deriveModule(Module module) {
		val resultModule = HypergraphFactory.eINSTANCE.createModule
		resultModule.name = module.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createModuleTrace
		derivedFrom.module = module
		resultModule.derivedFrom = derivedFrom
		
		return resultModule
	}
	
	def static createEdgeBetweenMethods(ModularHypergraph hypergraph, Node caller, Node callee) {
		val edgeSubset = caller.edges.filter[callerEdge | callee.edges.exists[calleeEdge | calleeEdge == callerEdge]]
		val edgeName = caller.name + "::" + callee.name
		val existingEdge = edgeSubset.findFirst[edge | edge.name.equals(edgeName)]
		if (existingEdge == null) {
			val edge = HypergraphFactory.eINSTANCE.createEdge
			edge.derivedFrom = null
			edge.name = edgeName
			hypergraph.edges.add(edge)
			callee.edges.add(edge)
			caller.edges.add(edge)
		}
	}
	
}