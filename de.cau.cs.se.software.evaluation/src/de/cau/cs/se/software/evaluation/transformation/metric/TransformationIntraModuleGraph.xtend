package de.cau.cs.se.software.evaluation.transformation.metric

import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import java.util.HashMap
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.hypergraph.Module
import static extension de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper.*


class TransformationIntraModuleGraph extends AbstractTransformation<ModularHypergraph, ModularHypergraph> {
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(ModularHypergraph input) {
		result = HypergraphFactory.eINSTANCE.createModularHypergraph
		
		val nodeMap = new HashMap<Node,Node>()
		val moduleMap = new HashMap<Module,Module>()
		val nodeModuleMap = new HashMap<Node,Module>()
				
		input.modules.forEach[module | 
			val derivedModule = module.deriveModule
			moduleMap.put(module, derivedModule)
			module.nodes.forEach[nodeModuleMap.put(it, module)]
			result.modules += derivedModule
		]
		
		if (monitor.canceled)
			return null
		
		input.nodes.forEach[node | 
			val derivedNode = node.deriveNode
			nodeMap.put(node, derivedNode)
			result.nodes += derivedNode
		]
		
		if (monitor.canceled)
			return null
		
		input.edges.forEach[edge |
			val connectedNodes = input.nodes.filter[it.edges.contains(edge)]
			val module = nodeModuleMap.get(connectedNodes.get(0))
			if (connectedNodes.forall[node | nodeModuleMap.get(node) == module]) {
				val derivedEdge = edge.deriveEdge
				connectedNodes.forEach[connectedNode | nodeMap.get(connectedNode).edges.add(derivedEdge)]
				result.edges += derivedEdge
			}
		]
		
		return result
	}
	
	override workEstimate(ModularHypergraph input) {
		input.modules.size + input.nodes.size + input.edges.size * input.nodes.size
	}
	
}