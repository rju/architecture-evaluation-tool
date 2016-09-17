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

import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import de.cau.cs.se.software.evaluation.hypergraph.NodeTrace
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper

class TransformationMaximalInterconnectedGraph extends AbstractTransformation<ModularHypergraph, ModularHypergraph> {
	
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(ModularHypergraph input) {
		this.result = HypergraphFactory.eINSTANCE.createModularHypergraph
		/** copy all nodes */
		input.nodes.forEach[this.result.nodes.add(HypergraphCreationHelper.deriveNode(it))]
		monitor.worked(input.nodes.size)
		if (monitor.canceled)
			return null
		
		/** copy module boundaries */
		input.modules.forEach[module |
			if (!monitor.canceled) {
				val derivedModule = HypergraphCreationHelper.deriveModule(module)
				module.nodes.forEach[node | 
					derivedModule.nodes.add(this.result.nodes.
						findFirst[derivedNode | (derivedNode.derivedFrom as NodeTrace).node == node])
				]
				this.result.modules.add(derivedModule)
				monitor.worked(input.nodes.size)
			}
		]
		/** create one simple edge between every node */
		this.result.nodes.forEach[startNode,startIndex |
			if (!monitor.canceled) {
				for(var index = startIndex + 1; index < this.result.nodes.size; index++) {
					val endNode = this.result.nodes.get(index)
					val edge = HypergraphFactory.eINSTANCE.createEdge
					edge.name = startNode.name + "-" + endNode.name
					startNode.edges.add(edge)
					endNode.edges.add(edge)
					this.result.edges.add(edge)
				}
				monitor.worked(input.nodes.size)
			}
		]
		
		return this.result
	}
	
	override workEstimate(ModularHypergraph input) {
		input.nodes.size + // copy nodes
		input.modules.size * input.nodes.size + // copy modules and assign nodes
		input.nodes.size * input.nodes.size // create edges
	}
	
}