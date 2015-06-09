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
package de.cau.cs.se.evaluation.architecture.transformation.processing

import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.hypergraph.Module
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace

class TransformationIntermoduleHyperedgesOnlyGraph implements ITransformation {
	
	val ModularHypergraph hypergraph
	var ModularHypergraph resultHypergraph
	
	new (ModularHypergraph hypergraph) {
		this.hypergraph = hypergraph
	}
	
	def ModularHypergraph getResult() {
		return this.resultHypergraph
	}
	
	/**
	 * Create a intermodule only hypergraph.
	 */
	override transform() {
		resultHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph
		// detect all edges crossing module boundaries
		val interModuleEdges = hypergraph.edges.filter[edge | edge.isIntermoduleEdge(hypergraph.nodes, hypergraph.modules)]
		// copy those edges to new graph
		// copy all nodes connected to those edges
		interModuleEdges.forEach[edge | 
			val derivedEdge = TransformationHelper.deriveEdge(edge)
			resultHypergraph.edges.add(derivedEdge)
			hypergraph.nodes.filter[node | node.edges.contains(edge)].forEach[node |
				var derivedNode = resultHypergraph.nodes.findFirst[derivedNode | 
					(derivedNode.derivedFrom as NodeTrace).node == node
				]
				if (derivedNode == null) {
					derivedNode = TransformationHelper.deriveNode(node)
					resultHypergraph.nodes.add(derivedNode)
				}
				derivedNode.edges.add(derivedEdge)
			]
		]
		// copy modules
		hypergraph.modules.forEach[module |
			val derivedModule = TransformationHelper.deriveModule(module)
			module.nodes.forEach[node | 
				val derivedNode = resultHypergraph.nodes.findFirst[derivedNode | (derivedNode.derivedFrom as NodeTrace).node == node]
				if (derivedNode != null) {
					derivedModule.nodes.add(derivedNode)
				}
			]
		]
	}
	
	/**
	 * Check if the edge is an intermodule edge.
	 */
	def Boolean isIntermoduleEdge(Edge edge, EList<Node> nodes, EList<Module> modules) {
		val connectedNodes = nodes.filter[node | node.edges.contains(edge)]
		val involvedModules = modules.filter[module | module.nodes.intersects(connectedNodes)]
		return involvedModules.size > 1
	}
	
	/**
	 * Check if the intersection of both sets is not empty. 
	 */
	def Boolean intersects(EList<Node> set1, Iterable<Node> set2) {
		val interset = set1.filter[nodeSet1 | set2.exists[nodeSet2 | nodeSet2 == nodeSet1]]
		return !interset.empty
	}
	
}