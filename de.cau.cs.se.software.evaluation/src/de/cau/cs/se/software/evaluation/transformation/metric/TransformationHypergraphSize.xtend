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
import de.cau.cs.se.software.evaluation.state.StateFactory
import de.cau.cs.se.software.evaluation.state.RowPatternTable
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.software.evaluation.state.RowPattern
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import org.eclipse.core.runtime.IProgressMonitor

import static extension de.cau.cs.se.software.evaluation.transformation.TransformationHelper.*
import de.cau.cs.se.software.evaluation.hypergraph.EdgeTrace
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation

/**
 * Calculate the information size of a hypergraph.
 */
class TransformationHypergraphSize extends AbstractTransformation<Hypergraph,Double> {

	var String name
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
		
	def setName(String name) {
		this.name = name
	}

	override transform() {
		monitor.beginTask(this.name, (this.input.edges.size + this.input.nodes.size)*2 + this.input.nodes.size)
		val systemGraph = createSystemGraph(this.input)
		val table = systemGraph.createRowPatternTable
					
		result = calculateSize(systemGraph, table)

		return result
	}
	

	/**
	 * Calculate the size of given system.
	 */
	private def double calculateSize(Hypergraph system, RowPatternTable table) {
		var double size = 0
		
		for (var int i=0;i<system.nodes.size;i++) {
			monitor.worked(1)
			val probability = table.patterns.lookupProbability(system.nodes.get(i), system)
			if (probability > 0.0d) 
				size+= (-log2(probability))
			else
				System.out.println("Disconnected component. Result is tainted.")
		}
				
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
	 * Create a row pattern table for a system and a system graph.
	 * First, register the edges in the pattern table as column definitions.
	 * Second, calculate the pattern row for each node of the system graph.
	 * Compact, rows with the same pattern
	 */
	private def RowPatternTable createRowPatternTable(Hypergraph systemGraph) {
		val RowPatternTable patternTable = StateFactory.eINSTANCE.createRowPatternTable()
		systemGraph.edges.forEach[edge | patternTable.edges.add(edge)]
		monitor.worked(this.input.edges.size)
		systemGraph.nodes.forEach[node | patternTable.patterns.add(node.calculateRowPattern(patternTable.edges))]
		patternTable.compactPatternTable
		monitor.worked(this.input.nodes.size)
				
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
					j--
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
	 * Create a system graph from a hypergraph of a system by adding an additional not connected
	 * node for the environment.
	 * 
	 * @param hypergraph the graph which is used as input
	 * 
	 * @returns the system graph (Note: the system graph and the system share nodes)
	 */
	private def Hypergraph createSystemGraph(Hypergraph system) {
		val Hypergraph systemGraph = HypergraphFactory.eINSTANCE.createHypergraph
		val environmentNode = HypergraphFactory.eINSTANCE.createNode
		environmentNode.name = '_environment'
		
		systemGraph.nodes.add(environmentNode)
		
		system.edges.forEach[edge | systemGraph.edges.add(edge.deriveEdge)]
		monitor.worked(system.edges.size)
		system.nodes.forEach[node |
			val derivedNode = node.deriveNode 
			node.edges.forEach[edge |
				val derivedEdge = systemGraph.edges.findFirst[(it.derivedFrom as EdgeTrace).edge == edge] 
				derivedNode.edges.add(derivedEdge)
			]
			systemGraph.nodes.add(derivedNode)
		]
		monitor.worked(system.nodes.size)
					
		return systemGraph
	}
	
}