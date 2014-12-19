package de.cau.cs.se.evaluation.architecture.transformation

import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.state.StateModel
import de.cau.cs.se.evaluation.architecture.state.StateFactory
import de.cau.cs.se.evaluation.architecture.state.RowPatternTable
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.state.RowPattern
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.state.SystemSetup

class TransformationHypergraphMetrics {
	
	public def transform(Hypergraph system) {
		val StateModel state = StateFactory.eINSTANCE.createStateModel
		
		val environmentNode = HypergraphFactory.eINSTANCE.createNode
		environmentNode.name = '_environment'	
		
		state.mainsystem = StateFactory.eINSTANCE.createSystemSetup
		state.mainsystem.system = system
		state.mainsystem.systemGraph = system.createSystemGraph(environmentNode)
		state.mainsystem.rowPatternTable = state.mainsystem.createRowPatternTable
				
		/** create subgraphs. */
		system.nodes.forEach[node | state.subsystems.add(createSetup(node, state.mainsystem, environmentNode))]
		
		/** calculate sizes. */
		val double size = calculateSize(state.mainsystem.system, state.mainsystem.rowPatternTable)
		/** calculate complexity. */
		val double complexity = calculateComplexity(state.mainsystem, state.subsystems)
		
		System.out.println("Size " + size + "  Complexity " + complexity)
	}
	
	/**
	 * Calculate complexity.
	 */
	def double calculateComplexity(SystemSetup setup, EList<SystemSetup> list) {
		var double complexity = 0
		
		/** Can start at zero, as we ignore environment node by using system and not system graph. */
		for (var int i=0;i < setup.system.nodes.size;i++) {
			val subSystemSetup = list.get(i)
			complexity += calculateSize (subSystemSetup.system, subSystemSetup.rowPatternTable)
		}
				
		complexity -= calculateSize(setup.system, setup.rowPatternTable)
				
		return complexity
	}
	
	/**
	 * Calculate the size of given system.
	 */
	def double calculateSize(Hypergraph system, RowPatternTable table) {
		var double size = 0
		
		for (var int i=0;i<system.nodes.size;i++) {
			size+= (-log2(table.patterns.lookupProbability(system.nodes.get(i), system)))
		}
		
		return size
	}
	
	/**
	 * Logarithmus for base 2.
	 */
	def double log2(double value) {
		return Math.log(value)/Math.log(2)
	}
	
	/**
	 * Find the row pattern for a given node and determine its probability.
	 */
	def double lookupProbability(EList<RowPattern> patternList, Node node, Hypergraph system) {
		val double count = patternList.filter[p | p.nodes.contains(node)].get(0).nodes.size as double
		return count/((system.nodes.size + 1) as double)
	}
	
	/**
	 * Create a system setup.
	 */
	def SystemSetup createSetup(Node node, SystemSetup mainSetup, Node environmentNode) {
		val setup = StateFactory.eINSTANCE.createSystemSetup
		setup.system = createSubsystem(node, mainSetup.system)
		setup.systemGraph = createSystemGraph(setup.system, environmentNode)
		setup.rowPatternTable = setup.createRowPatternTable		
		
		return setup
	}
	
	/**
	 * Create a row pattern table for a system and a system graph.
	 */
	def RowPatternTable createRowPatternTable(SystemSetup setup) {
		val RowPatternTable patternTable = StateFactory.eINSTANCE.createRowPatternTable()
		setup.system.edges.forEach[edge | patternTable.edges.add(edge)]
		
		setup.systemGraph.nodes.forEach[node | patternTable.patterns.add(node.calculateRowPattern(patternTable.edges))]
		patternTable.compactPatternTable
				
		return patternTable	
	}
	
	/**
	 * Find duplicate pattern in the pattern table and merge the pattern rows.
	 */
	def void compactPatternTable(RowPatternTable table) {
		for (var int i=0;i<table.patterns.size;i++) {
			for (var int j=i+1; j<table.patterns.size; j++) {
				if (matchPattern(table.patterns.get(j).pattern,table.patterns.get(i).pattern)) {
					val basePattern = table.patterns.get(i)
					table.patterns.get(j).nodes.forEach[node | basePattern.nodes.add(node)]
					table.patterns.remove(j)
				}
			}
		}
	}
	
	/**
	 * Return true if both lists contain the same values in the list.
	 */
	def matchPattern(EList<Boolean> leftList, EList<Boolean> rightList) {
		if (leftList.size != rightList.size)
			return false
		for (var int i=0;i<leftList.size;i++) {
			if (!leftList.get(i).equals(rightList.get(i)))
				return false
		}
		
		return true
	}
	
	/**
	 * Create an subgraph only containing those edges which are connected to the given start node.
	 * 
	 * @param node the start node
	 * @param system the system graph of a system
	 * 
	 * @returns a proper subgraph
	 */	
	def Hypergraph createSubsystem(Node node, Hypergraph system) {
		val Hypergraph subgraph = HypergraphFactory.eINSTANCE.createHypergraph
		
		node.edges.forEach[subgraph.edges.add(it)]
		system.nodes.forEach[subgraph.nodes.add(it)]
		
		return subgraph
	}
	
	/**
	 * Calculate the row pattern of a node based on its edges.
	 * 
	 * @param node where the pattern is calculated for
	 * @param edgeList sequence of edges which define the table wide order of edges
	 * 
	 * @returns the complete pattern
	 */
	def RowPattern calculateRowPattern(Node node, EList<Edge> edgeList) {
		val pattern = StateFactory.eINSTANCE.createRowPattern
		edgeList.forEach[edge | pattern.pattern.add(node.edges.exists[it == edge])]
		return pattern
	}
	
	/**
	 * Create a system graph from a hypergraph of a system by adding an additional not connected
	 * node for the environment.
	 * 
	 * @param hypergraph the graph which is used as input
	 * 
	 * @returns the system graph (Note: the system graph and the system share nodes)
	 */
	def Hypergraph createSystemGraph(Hypergraph system, Node environmentNode) {
		val Hypergraph systemGraph = HypergraphFactory.eINSTANCE.createHypergraph
		
		system.nodes.add(environmentNode)		
		system.nodes.forEach[node | systemGraph.nodes.add(node)]
		system.edges.forEach[edge | systemGraph.edges.add(edge)]
		
		return systemGraph
	}
	
}