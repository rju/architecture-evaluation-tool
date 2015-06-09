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
package de.cau.cs.se.evaluation.architecture.transformation

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Module
import de.cau.cs.se.evaluation.architecture.hypergraph.Node

class TransformationHelper {
	
	def static deriveNode(Node node) {
		val resultNode = HypergraphFactory.eINSTANCE.createNode
		resultNode.name = node.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace
		derivedFrom.node = node
		resultNode.derivedFrom = derivedFrom
		
		return resultNode
	}
	
	def static deriveEdge(Edge edge) {
		val resultEdge = HypergraphFactory.eINSTANCE.createEdge
		resultEdge.name = edge.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace
		derivedFrom.edge = edge
		resultEdge.derivedFrom = derivedFrom
		
		return resultEdge
	}
	
	def static deriveModule(Module module) {
		val resultModule = HypergraphFactory.eINSTANCE.createModule
		resultModule.name = module.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createModuleTrace
		derivedFrom.module = module
		resultModule.derivedFrom = derivedFrom
		
		return resultModule
	}
	
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