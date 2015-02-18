package de.cau.cs.se.evaluation.architecture.transformation.processing

import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory

class TransformationIntermoduleHyperedgesOnlyGraph implements ITransformation {
	
	val ModularHypergraph hypergraph
	var ModularHypergraph resultHypergraph
	
	new (ModularHypergraph hypergraph) {
		this.hypergraph = hypergraph
	}
	
	def ModularHypergraph getIntermoduleHyperedgesOnlyGraph() {
		return this.resultHypergraph
	}
	
	override transform() {
		resultHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph
		// detect all edges crossing module boundaries
		// copy those edges to new graph
		// copy all nodes connected to those edges
	}
	
}