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
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace

class TransformationHyperedgesOnlyGraph implements ITransformation {
	
	val Hypergraph hypergraph
	var Hypergraph resultHypergraph
	
	new (Hypergraph hypergraph) {
		this.hypergraph = hypergraph
	}
	
	def getResult() {
		return this.resultHypergraph
	}
	
	/**
	 * Copy only connected nodes to the result graph.
	 */
	override transform() {
		resultHypergraph = HypergraphFactory.eINSTANCE.createHypergraph
		for (Edge edge : hypergraph.edges) {
			resultHypergraph.edges.add(TransformationHelper.deriveEdge(edge))
		}
		for (Node node : hypergraph.nodes) {
			if (node.edges.size > 0) {
				val resultNode = TransformationHelper.deriveNode(node)
				node.edges.forEach[edge | resultNode.edges.add(resultHypergraph.edges.findFirst[(it.derivedFrom as EdgeTrace).edge == edge])]
				resultHypergraph.nodes.add(resultNode)
			}
		}		
	}
	
}