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
package de.cau.cs.se.software.evaluation.jobs

import org.eclipse.emf.common.util.EList
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory

/**
 * This class is only a temporary place for this model. In real
 * we might need a language or at least an EMF editor for that and store
 * such models in real files.
 */
class CoCoMEMegaModel {
	
			// val megaModelGraph = createMegaModelAnalysis
		// metrics.system = megaModelGraph
		// val mmSize = metrics.calculate
		// val mmComplexity = calculateComplexity(megaModelGraph, monitor)
				
		// result.getValues.add(new NamedValue("Size", mmSize))
		// result.getValues.add(new NamedValue("Complexity", mmComplexity))
	
	
	/**
	 * Create Megamodel graph
	 */
	def createMegaModelAnalysis() {
		val Hypergraph graph = HypergraphFactory.eINSTANCE.createHypergraph
		for(var int i=1;i<22;i++) {
			val node = HypergraphFactory.eINSTANCE.createNode
			node.name = "Node " + i
			graph.nodes.add(node)
		}
	
		for(var int i=1;i<26;i++) {
			val edge = HypergraphFactory.eINSTANCE.createEdge
			edge.name = "Edge " + i
			graph.edges.add(edge)
		}
		
		connectNode(graph.nodes, graph.edges, "Node 1", "Edge 3")
		connectNode(graph.nodes, graph.edges, "Node 1", "Edge 9")
		
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 3")
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 4")
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 5")
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 6")
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 2")
		
		connectNode(graph.nodes, graph.edges, "Node 3", "Edge 2")
		connectNode(graph.nodes, graph.edges, "Node 3", "Edge 1")
		
		connectNode(graph.nodes, graph.edges, "Node 4", "Edge 9")
		connectNode(graph.nodes, graph.edges, "Node 4", "Edge 4")
		connectNode(graph.nodes, graph.edges, "Node 4", "Edge 25")
		connectNode(graph.nodes, graph.edges, "Node 4", "Edge 7")
		
		connectNode(graph.nodes, graph.edges, "Node 5", "Edge 7")
		connectNode(graph.nodes, graph.edges, "Node 5", "Edge 1")
		
		connectNode(graph.nodes, graph.edges, "Node 6", "Edge 5")
		connectNode(graph.nodes, graph.edges, "Node 6", "Edge 22")
		connectNode(graph.nodes, graph.edges, "Node 6", "Edge 24")
		
		connectNode(graph.nodes, graph.edges, "Node 7", "Edge 6")
		connectNode(graph.nodes, graph.edges, "Node 7", "Edge 24")
		connectNode(graph.nodes, graph.edges, "Node 7", "Edge 13")
		connectNode(graph.nodes, graph.edges, "Node 7", "Edge 23")
		
		connectNode(graph.nodes, graph.edges, "Node 8", "Edge 22")
		connectNode(graph.nodes, graph.edges, "Node 8", "Edge 1")
		
		connectNode(graph.nodes, graph.edges, "Node 9", "Edge 13")
		connectNode(graph.nodes, graph.edges, "Node 9", "Edge 12")
		
		connectNode(graph.nodes, graph.edges, "Node 10", "Edge 9")
		connectNode(graph.nodes, graph.edges, "Node 10", "Edge 10")
		
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 10")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 25")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 24")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 23")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 11")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 16")
		
		connectNode(graph.nodes, graph.edges, "Node 12", "Edge 10")
		connectNode(graph.nodes, graph.edges, "Node 12", "Edge 8")
		
		connectNode(graph.nodes, graph.edges, "Node 13", "Edge 10")
		connectNode(graph.nodes, graph.edges, "Node 13", "Edge 21")
		
		connectNode(graph.nodes, graph.edges, "Node 14", "Edge 8")
		connectNode(graph.nodes, graph.edges, "Node 14", "Edge 21")
		
		connectNode(graph.nodes, graph.edges, "Node 15", "Edge 21")
		connectNode(graph.nodes, graph.edges, "Node 15", "Edge 20")
		connectNode(graph.nodes, graph.edges, "Node 15", "Edge 17")
		
		connectNode(graph.nodes, graph.edges, "Node 16", "Edge 20")
		connectNode(graph.nodes, graph.edges, "Node 16", "Edge 1")
		connectNode(graph.nodes, graph.edges, "Node 16", "Edge 19")
		
		connectNode(graph.nodes, graph.edges, "Node 17", "Edge 19")
		connectNode(graph.nodes, graph.edges, "Node 17", "Edge 18")
		
		connectNode(graph.nodes, graph.edges, "Node 18", "Edge 8")
		connectNode(graph.nodes, graph.edges, "Node 18", "Edge 16")
		connectNode(graph.nodes, graph.edges, "Node 18", "Edge 17")
		connectNode(graph.nodes, graph.edges, "Node 18", "Edge 18")
		
		connectNode(graph.nodes, graph.edges, "Node 19", "Edge 18")
		connectNode(graph.nodes, graph.edges, "Node 19", "Edge 11")
			
		connectNode(graph.nodes, graph.edges, "Node 20", "Edge 11")	
		connectNode(graph.nodes, graph.edges, "Node 20", "Edge 12")	
		connectNode(graph.nodes, graph.edges, "Node 20", "Edge 18")	
		
		connectNode(graph.nodes, graph.edges, "Node 21", "Edge 12")	
		connectNode(graph.nodes, graph.edges, "Node 21", "Edge 14")	
		connectNode(graph.nodes, graph.edges, "Node 21", "Edge 1")	
		
		return graph
	}
	
	private def connectNode(EList<Node> nodes, EList<Edge> edges, String nodeName, String edgeName) {
		val node = nodes.findFirst[node | node.name.equals(nodeName)]
		val edge = edges.findFirst[edge | edge.name.equals(edgeName)]
		node.edges.add(edge)
	}
}