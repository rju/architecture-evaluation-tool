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
package de.cau.cs.se.evaluation.architecture.transformation.metric

import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.hypergraph.Module
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace
import de.cau.cs.se.evaluation.architecture.transformation.AbstractTransformation
import org.eclipse.core.runtime.IProgressMonitor
import java.util.HashMap
import java.util.Map

/**
 * Create a intermodule only hypergraph.
 */
class TransformationIntermoduleHyperedgesOnlyGraph extends AbstractTransformation<ModularHypergraph, ModularHypergraph> {

	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override transform() {
		monitor.beginTask("Create intermodule hyperedges only graph",
			this.input.edges.size * (this.input.nodes.size + this.input.modules.size) + // find all intermodule edges
			this.input.edges.size * this.input.nodes.size + // upper bound of node and edge iteration
			this.input.modules.size * this.input.nodes.size // copy modules
		)
		this.result = HypergraphFactory.eINSTANCE.createModularHypergraph
		
		/** detect all edges crossing module boundaries */
		val moduleNodeMap = new HashMap<Node,Module>()
		this.input.modules.forEach[module |
			module.nodes.forEach[moduleNodeMap.put(it, module)]
		]
		val interModuleEdges = this.input.edges.filter[edge | 
			monitor.worked(this.input.nodes.size + this.input.modules.size)
			edge.isIntermoduleEdge(moduleNodeMap, this.input.nodes)			
		]
		
		/** 
		 * copy those edges to new graph
		 * copy all nodes connected to those edges
		 */
		interModuleEdges.forEach[edge | 
			val derivedEdge = TransformationHelper.deriveEdge(edge)
			this.result.edges.add(derivedEdge)
			this.input.nodes.filter[node | node.edges.contains(edge)].forEach[node |
				var derivedNode = this.result.nodes.findFirst[derivedNode | 
					(derivedNode.derivedFrom as NodeTrace).node == node
				]
				if (derivedNode == null) {
					derivedNode = TransformationHelper.deriveNode(node)
					this.result.nodes.add(derivedNode)
				}
				derivedNode.edges.add(derivedEdge)
			]
			monitor.worked(this.input.nodes.size)
		]
		monitor.worked(this.input.nodes.size * (this.input.edges.size-interModuleEdges.size))
		
		/** copy modules */
		this.input.modules.forEach[module |
			val derivedModule = TransformationHelper.deriveModule(module)
			module.nodes.forEach[node | 
				val derivedNode = this.result.nodes.findFirst[derivedNode | (derivedNode.derivedFrom as NodeTrace).node == node]
				if (derivedNode != null) {
					derivedModule.nodes.add(derivedNode)
				}
			]
			monitor.worked(this.input.nodes.size)
		]
		
		return this.result
	}
	
	
	/**
	 * Check if the edge is an intermodule edge.
	 */
	private def Boolean isIntermoduleEdge(Edge edge, Map<Node,Module> moduleNodeMap, EList<Node> nodes) {
		val connectedNodes = nodes.filter[node | node.edges.contains(edge)]
		var Module lastMatch = null
		for (Node node : connectedNodes) {
			val match = moduleNodeMap.get(node)
			if (lastMatch != null) {
				if (lastMatch != match)
					return true
			}
			lastMatch = match
		}
		
		return false
	}
	
}