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

import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.state.RowPattern
import de.cau.cs.se.software.evaluation.state.RowPatternTable
import de.cau.cs.se.software.evaluation.state.StateFactory
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import java.util.ArrayList
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.emf.common.util.EList

import static extension de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper.*
import java.util.HashMap
import java.util.Map

/**
 * Calculate the information size of a hypergraph.
 */
class TransformationHypergraphSize extends AbstractTransformation<Hypergraph,Double> {
	
	val PARALLEL_JOBS = 8
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(Hypergraph input) {
		if (this.monitor.canceled)
			return 0.0
			
		val systemGraph = createSystemGraph(input)
		val table = systemGraph.createRowPatternTable(input)
					
		this.result = calculateSize(systemGraph, table)

		return result
	}
	

	/**
	 * Calculate the size of given system.
	 */
	private def double calculateSize(Hypergraph input, RowPatternTable table) {
		var double size = 0
		
		val int length = input.nodes.size
		val int partitionLength = length/PARALLEL_JOBS
		
		var start = 0
		
		val jobs = new ArrayList<CalculateSizePartitionJob>
		
		for(var i=0; i<PARALLEL_JOBS; i++) {
			val job = new CalculateSizePartitionJob("Calculate size task " + i, input, table)
			job.start = start
			start = job.end = job.start + partitionLength
			
			if (i == 7 && job.end < length)
				job.end = length
			
			job.schedule
			jobs.add(job)
		}

		for(var i=0; i<8 ; i++) {
			val job = jobs.get(i)
			job.join
			size += job.resultSize
		}	
		
		return size
	}

	/**
	 * Create a row pattern table for a system and a system graph.
	 * First, register the edges in the pattern table as column definitions.
	 * Second, calculate the pattern row for each node of the system graph.
	 * Compact, rows with the same pattern
	 */
	private def RowPatternTable createRowPatternTable(Hypergraph systemGraph, Hypergraph input) {
		monitor.subTask("Construct row pattern table")
		val RowPatternTable patternTable = StateFactory.eINSTANCE.createRowPatternTable()
		patternTable.edges.addAll(systemGraph.edges)
		monitor.worked(input.edges.size)
		if (this.monitor.canceled)
			return null
		
		systemGraph.nodes.forEach[node, i | 
			monitor.subTask("Calculate row patterns " + i + " of " + systemGraph.nodes.size + " (width " + patternTable.edges.size + ")")
			patternTable.patterns.add(node.calculateRowPattern(patternTable.edges))
		]
		monitor.worked(input.nodes.size * input.nodes.size)
		
		patternTable.compactPatternTable
		monitor.worked(input.nodes.size)
						
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
		monitor.subTask("Compact row patterns " + table.patterns.size)
		
		val tick = table.patterns.size * table.patterns.get(0).pattern.size
		val length = table.patterns.size
		
		for (var int i=0;i<table.patterns.size;i++) {
			monitor.subTask("Compact row patterns " + i + " of " + table.patterns.size)
			monitor.worked(tick)
			if (!this.monitor.canceled) {
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
		
		monitor.worked((length-table.patterns.size)*tick)
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
		monitor.subTask("Create system graph")
		val Hypergraph systemGraph = HypergraphFactory.eINSTANCE.createHypergraph
		val environmentNode = HypergraphFactory.eINSTANCE.createNode
		environmentNode.name = '_environment'
		
		systemGraph.nodes.add(environmentNode)

		/** Lookup map for derived edges. */
		val Map<Edge,Edge> derivedEdgeMap = new HashMap<Edge,Edge>
		
		system.edges.forEach[
			val derived = it.deriveEdge
			derivedEdgeMap.put(it, derived)
			systemGraph.edges.add(derived)
		]
		monitor.worked(system.edges.size)
		if (this.monitor.canceled)
			return null
					
		system.nodes.forEach[node,i |
			monitor.subTask("Create system graph: nodes " + i + " of " + system.nodes.size)
			if (!this.monitor.canceled) {
				val derivedNode = node.deriveNode 
				node.edges.forEach[derivedNode.edges.add(derivedEdgeMap.get(it))]
				systemGraph.nodes.add(derivedNode)
				monitor.worked(node.edges.size)
			}
		]
		monitor.worked(system.nodes.size)
					
		return systemGraph
	}
	
	override workEstimate(Hypergraph input) {
		input.edges.size + input.nodes.size + input.nodes.map[it.edges.size].reduce[p1, p2| p1 + p2] + // createSystemGraph
		input.edges.size + input.nodes.size * input.nodes.size + input.nodes.size * input.nodes.size * input.edges.size // createRowPatternTable
		input.nodes.size * input.nodes.size // calculateSize
	}
	
}
