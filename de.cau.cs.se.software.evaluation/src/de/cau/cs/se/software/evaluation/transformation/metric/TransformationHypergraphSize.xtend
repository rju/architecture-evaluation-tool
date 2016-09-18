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
import de.cau.cs.se.software.evaluation.state.RowPatternTable
import de.cau.cs.se.software.evaluation.state.StateFactory
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import java.util.ArrayList
import java.util.HashMap
import java.util.Map
import org.eclipse.core.runtime.IProgressMonitor

import static extension de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper.*

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
		val RowPatternTable patternTable = StateFactory.createRowPatternTable(
			systemGraph.edges.size, systemGraph.nodes.size
		)
		patternTable.setAllEdges(systemGraph.edges)
		monitor.worked(input.edges.size)
		if (this.monitor.canceled)
			return null
		
		systemGraph.nodes.forEach[node, i | 
			monitor.subTask("Calculate row patterns " + i + " of " + systemGraph.nodes.size + " (width " + patternTable.edges.size + ")")
			TransformationHelper.calculateRowPattern(patternTable, node)
		]
		monitor.worked(input.nodes.size * input.nodes.size)
		
		TransformationHelper.compactPatternTable(patternTable, monitor)
						
		return patternTable	
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
