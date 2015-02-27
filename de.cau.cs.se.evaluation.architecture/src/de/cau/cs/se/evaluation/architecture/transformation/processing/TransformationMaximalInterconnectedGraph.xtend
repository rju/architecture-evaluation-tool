package de.cau.cs.se.evaluation.architecture.transformation.processing

import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace

class TransformationMaximalInterconnectedGraph implements ITransformation {
	
	val ModularHypergraph hypergraph
	var ModularHypergraph resultHypergraph
	
	new (ModularHypergraph hypergraph) {
		this.hypergraph = hypergraph
	}
	
	def ModularHypergraph getResult() {
		return this.resultHypergraph
	}
	
	override transform() {
		resultHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph
		// copy all nodes
		hypergraph.nodes.forEach[resultHypergraph.nodes.add(TransformationHelper.deriveNode(it))]
		// copy module boundaries
		hypergraph.modules.forEach[module |
			val derivedModule = TransformationHelper.deriveModule(module)
			module.nodes.forEach[node | 
				derivedModule.nodes.add(resultHypergraph.nodes.
					findFirst[derivedNode | (derivedNode.derivedFrom as NodeTrace).node == node])
			]
			resultHypergraph.modules.add(derivedModule)
		]
		// create one simple edge between every node
		resultHypergraph.nodes.forEach[startNode,startIndex |
			for(var index = startIndex + 1; index < resultHypergraph.nodes.size; index++) {
				val endNode = resultHypergraph.nodes.get(index)
				val edge = HypergraphFactory.eINSTANCE.createEdge
				edge.name = startNode.name + "-" + endNode.name
				startNode.edges.add(edge)
				endNode.edges.add(edge)
				resultHypergraph.edges.add(edge)
			}
		]
	}
	
}