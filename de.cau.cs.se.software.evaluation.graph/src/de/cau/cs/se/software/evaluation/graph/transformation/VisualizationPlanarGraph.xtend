package de.cau.cs.se.software.evaluation.graph.transformation

import de.cau.cs.se.geco.architecture.framework.IGenerator
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Module
import java.util.HashMap
import java.util.Map

class VisualizationPlanarGraph implements IGenerator<ModularHypergraph, PlanarVisualizationGraph> {
	
	
	
	override generate(ModularHypergraph input) {
		val result = TransformationFactory.eINSTANCE.createPlanarVisualizationGraph
		val Map<Module,PlanarNode> moduleMap = new HashMap<Module,PlanarNode>()
		
		input.modules.forEach[module |
			val node = TransformationFactory.eINSTANCE.createPlanarNode
			val moduleQualifier = if (module.kind == EModuleKind.ANONYMOUS) {
				val separator = module.name.lastIndexOf('$')
				module.name.substring(0,separator)
			} else
				module.name
			val separator = moduleQualifier.lastIndexOf('.')
			node.name = moduleQualifier.substring(0,separator) 
			node.context = moduleQualifier.substring(separator+1)
			node.kind = module.kind
			
			result.nodes.add(node)
			moduleMap.put(module,node)
		]
		
		input.edges.forEach[hyperedge |
			val edgeNodes = input.nodes.filter[node | node.edges.contains(hyperedge)]
			edgeNodes.forEach[startNode |
				edgeNodes.forEach[endNode |
					val startModule = input.modules.findFirst[it.nodes.contains(startNode)]
					val endModule = input.modules.findFirst[it.nodes.contains(endNode)]
					if (startModule != endModule) { // new edge
						val startPlanarNode = moduleMap.get(startModule)
						val endPlanarNode = moduleMap.get(endModule)
						val planarEdge = startPlanarNode.edges.findFirst[endPlanarNode.edges.contains(it)]
						if (planarEdge == null) {
							result.edges += createPlanarEdge(startPlanarNode, endPlanarNode)
						} else {
							planarEdge.count = planarEdge.count + 1
						}
					}
				]
			]	
		]
		
		return result
	}
	
	private def createPlanarEdge(PlanarNode start, PlanarNode end) {
		val edge = TransformationFactory.eINSTANCE.createPlanarEdge
		edge.count = 1
		edge.start= start
		edge.end = end
		
		start.edges += edge
		end.edges += edge	
		
		return edge
	}
	

	
}