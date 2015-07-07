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

import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace
import de.cau.cs.se.evaluation.architecture.transformation.AbstractTransformation
import org.eclipse.core.runtime.IProgressMonitor

/**
 * Copy only connected nodes to the result graph.
 */
class TransformationHyperedgesOnlyGraph extends AbstractTransformation<Hypergraph, Hypergraph> {
		
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override transform() {
		result = HypergraphFactory.eINSTANCE.createHypergraph
		for (Edge edge : input.edges) {
			result.edges.add(TransformationHelper.deriveEdge(edge))
		}
		for (Node node : input.nodes) {
			if (node.edges.size > 0) {
				val resultNode = TransformationHelper.deriveNode(node)
				node.edges.forEach[edge | resultNode.edges.add(result.edges.findFirst[(it.derivedFrom as EdgeTrace).edge == edge])]
				result.nodes.add(resultNode)
			}
		}
		
		return result		
	}
	
}