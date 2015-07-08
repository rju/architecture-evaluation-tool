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
package de.cau.cs.se.software.evaluation.transformation

import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Module
import de.cau.cs.se.software.evaluation.hypergraph.Node

class TransformationHelper {
	
	/**
	 * Derive a new node from an old node and set the appropriate trace information.
	 * 
	 * @param node the old node
	 * 
	 * @returns the new node
	 */
	def static deriveNode(Node node) {
		val resultNode = HypergraphFactory.eINSTANCE.createNode
		resultNode.name = node.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace
		derivedFrom.node = node
		resultNode.derivedFrom = derivedFrom
		
		return resultNode
	}
	
	/**
	 * Derive a new edge from an old edge and set the appropriate trace information.
	 * 
	 * @param edge the old edge
	 * 
	 * @returns the new edge
	 */
	def static deriveEdge(Edge edge) {
		val resultEdge = HypergraphFactory.eINSTANCE.createEdge
		resultEdge.name = edge.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace
		derivedFrom.edge = edge
		resultEdge.derivedFrom = derivedFrom
		
		return resultEdge
	}
	
	/**
	 * Derive a new module from an old module and set the appropriate trace information.
	 * 
	 * @param module the old module
	 * 
	 * @returns the new module
	 */
	def static deriveModule(Module module) {
		val resultModule = HypergraphFactory.eINSTANCE.createModule
		resultModule.name = module.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createModuleTrace
		derivedFrom.module = module
		resultModule.derivedFrom = derivedFrom
		
		return resultModule
	}
	
	/**
	 * Create an edge between two nodes.
	 */
	def static createEdgeBetweenMethods(ModularHypergraph hypergraph, Node caller, Node callee) {
		val edgeSubset = caller.edges.filter[callerEdge | callee.edges.exists[calleeEdge | calleeEdge == callerEdge]]
		val edgeName = caller.name + "::" + callee.name
		val existingEdge = edgeSubset.findFirst[edge | edge.name.equals(edgeName)]
		if (existingEdge == null) {
			val edge = HypergraphFactory.eINSTANCE.createEdge
			edge.derivedFrom = null
			edge.name = edgeName
			hypergraph.edges.add(edge)
			callee.edges.add(edge)
			caller.edges.add(edge)
		}
	}
	
}