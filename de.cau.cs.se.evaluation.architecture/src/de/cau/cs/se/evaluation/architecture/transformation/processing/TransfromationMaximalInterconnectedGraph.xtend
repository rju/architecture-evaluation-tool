package de.cau.cs.se.evaluation.architecture.transformation.processing

import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory

class TransfromationMaximalInterconnectedGraph implements ITransformation {
	
	val ModularHypergraph hypergraph
	var ModularHypergraph resultHypergraph
	
	new (ModularHypergraph hypergraph) {
		this.hypergraph = hypergraph
	}
	
	def ModularHypergraph getMaximalInterconnectedGraph() {
		return this.resultHypergraph
	}
	
	override transform() {
		resultHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph
		// copy all nodes
		// copy module boundaries
		// create one simple edge between every node
	}
	
}