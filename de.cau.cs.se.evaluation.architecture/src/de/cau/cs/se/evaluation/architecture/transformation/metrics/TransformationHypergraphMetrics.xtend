package de.cau.cs.se.evaluation.architecture.transformation.metrics

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
import org.eclipse.core.runtime.IProgressMonitor

class TransformationHypergraphMetrics {
	
	val IProgressMonitor monitor
	var Hypergraph system
	
	new () {
		this.monitor = null
	}
	
	new(IProgressMonitor monitor) {
		this.monitor = monitor
	}
	
	public def setSystem(Hypergraph system) {
		this.system = system
	}
	
	public def ResultModelProvider calculate() {
		monitor?.subTask("Calculating metrics")
		val StateModel state = StateFactory.eINSTANCE.createStateModel
		
		val environmentNode = HypergraphFactory.eINSTANCE.createNode
		environmentNode.name = '_environment'	
		
		state.mainsystem = StateFactory.eINSTANCE.createSystemSetup
		state.mainsystem.system = system
		state.mainsystem.systemGraph = system.createSystemGraph(environmentNode)
		state.mainsystem.rowPatternTable = state.mainsystem.createRowPatternTable
		monitor?.worked(1)
		
		
		/** create subgraphs. */
		system.nodes.forEach[node |
			monitor?.subTask("Calculating metrics - subgraphs" + node.name)
			state.subsystems.add(createSetup(node, state.mainsystem, environmentNode))
			monitor?.worked(1)
		]
		
		val result = ResultModelProvider.INSTANCE
		
		// result.clearValues
		
		/** calculate sizes. */
		result.getValues.add(new NamedValue("Size", calculateSize(state.mainsystem.system, state.mainsystem.rowPatternTable)))
		/** calculate complexity. */
		result.getValues.add(new NamedValue("Complexity",calculateComplexity(state.mainsystem, state.subsystems)))
		
		return result
	}
	
	/**
	 * Calculate complexity.
	 */
	private def double calculateComplexity(SystemSetup setup, EList<SystemSetup> list) {
		var double complexity = 0
		
		/** Can start at zero, as we ignore environment node by using system and not system graph. */
		for (var int i=0;i < setup.system.nodes.size;i++) {
			val subSystemSetup = list.get(i)
			complexity += calculateSize (subSystemSetup.system, subSystemSetup.rowPatternTable)
		}
				
		complexity -= calculateSize(setup.system, setup.rowPatternTable)
		
		monitor?.worked(1)
			
		return complexity
	}
	
	/**
	 * Calculate the size of given system.
	 */
	private def double calculateSize(Hypergraph system, RowPatternTable table) {
		var double size = 0
		
		for (var int i=0;i<system.nodes.size;i++) {
			val probability = table.patterns.lookupProbability(system.nodes.get(i), system)
			if (probability > 0.0d) 
				size+= (-log2(probability))
			else
				System.out.println("Disconnected component. Result is tainted.")
		}
		
		monitor?.worked(1)
		
		return size
	}
	
	/**
	 * Logarithm for base 2.
	 */
	private def double log2(double value) {
		return Math.log(value)/Math.log(2)
	}
	
	/**
	 * Find the row pattern for a given node and determine its probability. If no pattern
	 * is found then the node is totally disconnected and the probability is 0.
	 */
	private def double lookupProbability(EList<RowPattern> patternList, Node node, Hypergraph system) {
		val pattern = patternList.filter[p | p.nodes.contains(node)]
		val double count = if (pattern.size > 0) pattern.get(0).nodes.size as double else 0
			
		return count/((system.nodes.size + 1) as double)
	}
	
	/**
	 * Create a system setup.
	 */
	private def SystemSetup createSetup(Node node, SystemSetup mainSetup, Node environmentNode) {
		val setup = StateFactory.eINSTANCE.createSystemSetup
		setup.system = createSubsystem(node, mainSetup.system)
		setup.systemGraph = createSystemGraph(setup.system, environmentNode)
		setup.rowPatternTable = setup.createRowPatternTable		
		
		return setup
	}
	
	/**
	 * Create a row pattern table for a system and a system graph.
	 * First, register the edges in the pattern table as column definitions.
	 * Second, calculate the pattern row for each node of the system graph.
	 * Compact, rows with the same pattern
	 */
	private def RowPatternTable createRowPatternTable(SystemSetup setup) {
		val RowPatternTable patternTable = StateFactory.eINSTANCE.createRowPatternTable()
		setup.system.edges.forEach[edge | patternTable.edges.add(edge)]
		
		setup.systemGraph.nodes.forEach[node | patternTable.patterns.add(node.calculateRowPattern(patternTable.edges))]
		patternTable.compactPatternTable
				
		return patternTable	
	}
	
	/**
	 * Calculate the row pattern of a node based on its edges.
	 * 
	 * @param node where the pattern is calculated for
	 * @param edgeList sequence of edges which define the table wide order of edges
	 * 
	 * @returns the complete pattern
	 */
	private def RowPattern calculateRowPattern(Node node, EList<Edge> edgeList) {
		val pattern = StateFactory.eINSTANCE.createRowPattern
		pattern.nodes.add(node)
		edgeList.forEach[edge | pattern.pattern.add(node.edges.exists[it == edge])]
		return pattern
	}
	
	/**
	 * Find duplicate pattern in the pattern table and merge the pattern rows.
	 */
	private def void compactPatternTable(RowPatternTable table) {
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
	private def matchPattern(EList<Boolean> leftList, EList<Boolean> rightList) {
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
	private def Hypergraph createSubsystem(Node node, Hypergraph system) {
		val Hypergraph subgraph = HypergraphFactory.eINSTANCE.createHypergraph
		
		node.edges.forEach[subgraph.edges.add(it)]
		system.nodes.forEach[subgraph.nodes.add(it)]
		
		return subgraph
	}
		
	/**
	 * Create a system graph from a hypergraph of a system by adding an additional not connected
	 * node for the environment.
	 * 
	 * @param hypergraph the graph which is used as input
	 * 
	 * @returns the system graph (Note: the system graph and the system share nodes)
	 */
	private def Hypergraph createSystemGraph(Hypergraph system, Node environmentNode) {
		val Hypergraph systemGraph = HypergraphFactory.eINSTANCE.createHypergraph
		
		systemGraph.nodes.add(environmentNode)
		
		system.nodes.forEach[node | systemGraph.nodes.add(node)]
		system.edges.forEach[edge | systemGraph.edges.add(edge)]
			
		return systemGraph
	}
	
}