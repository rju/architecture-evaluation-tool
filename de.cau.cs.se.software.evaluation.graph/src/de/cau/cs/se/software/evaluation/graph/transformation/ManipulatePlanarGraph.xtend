package de.cau.cs.se.software.evaluation.graph.transformation

import de.cau.cs.se.geco.architecture.framework.IGenerator
import java.util.Map
import java.util.HashMap

class ManipulatePlanarGraph implements IGenerator<PlanarVisualizationGraph, PlanarVisualizationGraph> {
	
	val boolean framework
	val boolean anonymous
	val boolean iface
	val Map<PlanarNode,PlanarNode> nodeMap = new HashMap<PlanarNode,PlanarNode>()
	
	new(boolean framework, boolean anonymous, boolean iface) {
		this.framework = framework
		this.anonymous = anonymous
		this.iface = iface
	}
	
	override generate(PlanarVisualizationGraph input) {
		val result = TransformationFactory.eINSTANCE.createPlanarVisualizationGraph
		
		input.nodes.forEach[node |
			val duplicateNode = switch(node.kind) {
				case FRAMEWORK: if (framework) node.duplicate
				case ANONYMOUS: if (anonymous) node.duplicate
				case SYSTEM: node.duplicate
				case INTERFACE: if (iface) node.duplicate
			}
			if (duplicateNode != null) {
				result.nodes.add(duplicateNode)
				nodeMap.put(node,duplicateNode)
			}
 		]
 		
 		input.edges.forEach[edge |
 			val start = nodeMap.get(edge.start)
 			val end = nodeMap.get(edge.end)
 			
 			if (start != null) {
 				if (end != null) {
 					val duplicateEdge = TransformationFactory.eINSTANCE.createPlanarEdge
 					duplicateEdge.count = edge.count
 					start.edges.add(duplicateEdge)
 					end.edges.add(duplicateEdge)
 					duplicateEdge.start = start
 					duplicateEdge.end = end
 					
 					result.edges.add(duplicateEdge)
 				}
 			}
 		]
		
		return result
	}
	
	private def duplicate (PlanarNode node) {
		val duplicateNode = TransformationFactory.eINSTANCE.createPlanarNode
		duplicateNode.name = node.name
		duplicateNode.context = node.context
		duplicateNode.kind = node.kind
		
		return duplicateNode
	}
	
}