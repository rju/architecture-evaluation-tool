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
package de.cau.cs.se.evaluation.architecture.graph

import javax.inject.Inject

import de.cau.cs.kieler.core.kgraph.KNode
import de.cau.cs.kieler.core.krendering.KRenderingFactory
import de.cau.cs.kieler.core.krendering.extensions.KNodeExtensions
import de.cau.cs.kieler.core.krendering.extensions.KEdgeExtensions
import de.cau.cs.kieler.core.krendering.extensions.KPortExtensions
import de.cau.cs.kieler.core.krendering.extensions.KLabelExtensions
import de.cau.cs.kieler.core.krendering.extensions.KRenderingExtensions
import de.cau.cs.kieler.core.krendering.extensions.KContainerRenderingExtensions
import de.cau.cs.kieler.core.krendering.extensions.KPolylineExtensions
import de.cau.cs.kieler.core.krendering.extensions.KColorExtensions
import de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis

import static extension de.cau.cs.kieler.klighd.syntheses.DiagramSyntheses.*

import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.kieler.kiml.options.LayoutOptions
import de.cau.cs.kieler.kiml.options.Direction
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.hypergraph.Module
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference
import java.util.ArrayList
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutData
import de.cau.cs.kieler.klighd.util.KlighdSemanticDiagramData
import de.cau.cs.kieler.klighd.util.KlighdProperties
import de.cau.cs.kieler.klighd.KlighdConstants
import de.cau.cs.kieler.kiml.options.EdgeType
import de.cau.cs.kieler.core.krendering.KRendering
import de.cau.cs.kieler.core.krendering.KRectangle
import org.eclipse.emf.ecore.EClass
import java.util.HashMap
import java.util.Map
import de.cau.cs.kieler.core.kgraph.KEdge
import de.cau.cs.kieler.core.krendering.LineStyle
import de.cau.cs.kieler.kiml.options.EdgeRouting
import de.cau.cs.kieler.core.krendering.LineJoin
import de.cau.cs.kieler.core.krendering.KContainerRendering
import de.cau.cs.kieler.kiml.options.PortConstraints
import de.cau.cs.kieler.klay.layered.properties.Properties
import com.google.common.collect.ImmutableList
import de.cau.cs.kieler.klighd.SynthesisOption
import de.cau.cs.kieler.kiml.options.SizeConstraint
import java.util.EnumSet

class ModularHypergraphDiagramSynthesis extends AbstractDiagramSynthesis<ModularHypergraph> {
    
	@Inject extension KNodeExtensions
	@Inject extension KEdgeExtensions
    @Inject extension KPortExtensions
    @Inject extension KLabelExtensions
    @Inject extension KRenderingExtensions
    @Inject extension KContainerRenderingExtensions
    @Inject extension KPolylineExtensions
    @Inject extension KColorExtensions
    extension KRenderingFactory = KRenderingFactory.eINSTANCE
		       
    private static val VISIBLE_NODES_NAME = "Nodes Visible"
    private static val VISIBLE_NODES_NO = "Modules only"
    private static val VISIBLE_NODES_YES = "Show nodes in modules"
        
    /**
     * The filter option definition that allows users to customize the constructed class diagrams.
     */
    private static val SynthesisOption VISIBLE_NODES = SynthesisOption::createChoiceOption(VISIBLE_NODES_NAME,
       ImmutableList::of(VISIBLE_NODES_YES, VISIBLE_NODES_NO), VISIBLE_NODES_NO)
    
    var Map<Node,KNode> nodeMap = new HashMap<Node,KNode>()
    var Map<Module,KNode> moduleMap = new HashMap<Module,KNode>()
    var processedModules = new ArrayList<Module>()
       
    /**
     * {@inheritDoc}<br>
     * <br>
     * Registers the diagram filter option declared above, which allow users to tailor the constructed diagrams.
     */
    override public getDisplayedSynthesisOptions() {
        return ImmutableList::of(VISIBLE_NODES)
    }

    
    override KNode transform(ModularHypergraph hypergraph) {
        val root = hypergraph.createNode().associateWith(hypergraph);
                
        root => [
            // de.cau.cs.kieler.klay.layered.properties.Properties
            it.setLayoutOption(Properties.MERGE_EDGES, true)
            it.setLayoutOption(LayoutOptions.LAYOUT_HIERARCHY, true)

            it.setLayoutOption(LayoutOptions::SPACING, 75f)
            it.setLayoutOption(LayoutOptions::DIRECTION, Direction::UP)
            it.setLayoutOption(LayoutOptions::EDGE_ROUTING, EdgeRouting::POLYLINE)
            
            if (VISIBLE_NODES.objectValue == VISIBLE_NODES_NO) {
            	//it.setLayoutOption(LayoutOptions::ALGORITHM, "de.cau.cs.kieler.kiml.ogdf.planarization")
            	it.setLayoutOption(LayoutOptions::ALGORITHM, "de.cau.cs.kieler.klay.layered")
                it.setLayoutOption(LayoutOptions::EDGE_ROUTING, EdgeRouting::ORTHOGONAL)
                it.setLayoutOption(LayoutOptions::DIRECTION, Direction::LEFT)
	
            	hypergraph.modules.forEach[module | root.children += module.createEmptyModule]
            	hypergraph.modules.forEach[module | module.createCombindEdges(hypergraph)]
            } else {
            	it.setLayoutOption(LayoutOptions.ALGORITHM, "de.cau.cs.kieler.klay.layered")
        		hypergraph.modules.forEach[module | root.children += module.createModuleWithNodes]
        		hypergraph.edges.forEach[edge | edge.createGraphEdge(hypergraph.nodes, root.children)]    	
            }      
            
        ]
        
        return root
    }
	
	private def void createCombindEdges(Module sourceModule, ModularHypergraph hypergraph) {
		val edges = new ArrayList<Edge>()
		sourceModule.nodes.forEach[node | node.edges.forEach[edges.addUnique(it)]]
		processedModules.add(sourceModule)
		
		val targetModules = new HashMap<Module,Integer>()
		edges.forEach[edge |
			hypergraph.nodes.filter[node | node.edges.exists[it.equals(edge)]].forEach[node |
				if (!sourceModule.nodes.exists[it.equals(node)]) {
					/** external node, determine module */
					val targetModule = hypergraph.modules.findFirst[module | module.nodes.exists[it.equals(node)]]
					if (!processedModules.exists[it.equals(targetModule)]) {
						var value = targetModules.get(targetModule)
						if (value == null) {
							targetModules.put(targetModule,1)
						} else {
							targetModules.put(targetModule,value+1)
						}
					}
				}
			]
		]
		
		targetModules.forEach[module, count | createAggregatedEdge(sourceModule, module, count) ]
	}
	
	private def createAggregatedEdge(Module sourceModule, Module targetModule, Integer width) {
		val sourceNode = moduleMap.get(sourceModule)
		val targetNode = moduleMap.get(targetModule)
		
		val sourcePort = createPort() => [
   			it.setPortSize(2,2)
    		it.addRectangle.setBackground("black".color).lineJoin=LineJoin.JOIN_ROUND
    	]
    	
    	val targetPort = createPort() => [
   			it.setPortSize(2,2)
    		it.addRectangle.setBackground("black".color).lineJoin=LineJoin.JOIN_ROUND
    	]
		
		sourceNode.ports.add(sourcePort)
		targetNode.ports.add(targetPort)
				
		createEdge() => [
			it.source = sourceNode
			it.sourcePort = sourcePort
            it.target = targetNode
            it.targetPort = targetPort
            it.addPolyline => [
            	it.lineWidth = width
            	it.lineStyle = LineStyle.SOLID
            ]
		]
	}
	
	private def addUnique(ArrayList<Edge> edges, Edge edge) {
		if (!edges.contains(edge)) {
			edges.add(edge)
		}
	}
    
    
    /**
     * Create module without nodes for simple display.
     */
    private def KNode createEmptyModule(Module module) {
		val moduleNode = module.createNode().associateWith(module)
		moduleMap.put(module, moduleNode)
		
		val otherColor = switch (module.kind) {
			case SYSTEM: "LemonChiffon".color
			case FRAMEWORK: "Blue".color
			case ANONYMOUS: "Orange".color
			case INTERFACE: "White".color
		}
			
		moduleNode => [
			it.setLayoutOption(LayoutOptions::PORT_SPACING, 20f)
			it.setLayoutOption(LayoutOptions::SIZE_CONSTRAINT, SizeConstraint.minimumSizeWithPorts)
			it.addRoundedRectangle(10, 10) => [
				it.lineWidth = 2
                it.setBackgroundGradient("white".color, otherColor, 0)
                it.shadow = "black".color
                it.setGridPlacement(1).from(LEFT, 10, 0, TOP, 20, 0).to(RIGHT, 10, 0, BOTTOM, 20, 0)
                it.addText(module.name) => [
                	it.fontBold = true
                	it.cursorSelectable = true
                	it.setLeftTopAlignedPointPlacementData(1,1,1,1)            	
                ]
            ]
		]
	}
    
    /**
	 * Draw module as a rectangle with its nodes inside.
	 * 
	 * @param module the module to be rendered
	 */
	private def createModuleWithNodes(Module module) {
		// TODO a module should be a rectangle with preferably round corners,
		// a top-left name, and a set of nodes inside. 
		val moduleNode = module.createNode().associateWith(module)
		
		moduleNode => [	
			it.addRoundedRectangle(10, 10) => [
				it.lineWidth = 2
                it.setBackgroundGradient("white".color, "LemonChiffon".color, 0)
                it.shadow = "black".color
                it.setGridPlacement(1).from(LEFT, 10, 0, TOP, 10, 0).to(RIGHT, 10, 0, BOTTOM, 10, 0)
                it.addRectangle => [
	                it.invisible = true
	                it.addText(module.name) => [
	                	it.fontBold = true
	                	it.cursorSelectable = true
	                	it.setLeftTopAlignedPointPlacementData(1,1,1,1)            	
	                ]
                ]
                //it.addRectangle => [
                //	it.invisible =true
                //	it.setGridPlacement(2).from(LEFT, 10, 0, TOP, 10, 0).to(RIGHT, 10, 0, BOTTOM, 10, 0)     	
                	module.nodes.forEach[node | node.createGraphNode(it, module, moduleNode)]
                //]
            ]
		]
	}
			
	/**
	 * Draw a single node as a circle with its name at the center.
	 * 
	 * @param node the node to be rendered
	 */
	private def createGraphNode(Node node, KContainerRendering parent, Module module, KNode moduleNode) {
		val kNode = node.createNode().associateWith(node)
		nodeMap.put(node, kNode)
		moduleNode.children += kNode
		
		kNode => [
			//it.setLayoutOption(LayoutOptions.PORT_CONSTRAINTS, PortConstraints.FREE)
			//node.edges.forEach[graphEdge | graphEdge.createEdgePort(it, graphEdge.name)]
			parent.children.add(it.addEllipse => [
				it.lineWidth = 2
                it.background = "white".color
                it.setSurroundingSpace(10,0,10,0)
                it.addText(node.name.substring(module.name.length+1)) => [
                	it.setSurroundingSpace(10,0,10,0)
                	it.cursorSelectable = true
                ]
			])
		] 
		
	}
	
	private def createEdgePort(Edge edge, KNode kNode, String label) {
		kNode.ports.add(createPort() => [
    		it.setPortSize(2,2)
    		it.addRectangle.setBackground("black".color).lineJoin=LineJoin.JOIN_ROUND
                
            // last but not least add a label exhibiting the ports name
            //it.addInsidePortLabel(label, 8, KlighdConstants.DEFAULT_FONT_NAME)
    	])
	}

	private def createGraphEdge(Edge edge, EList<Node> nodes, EList<KNode> siblings) {		
		val referencedNodes = nodes.filter[node | node.edges.exists[it.equals(edge)]]
		System.out.println("graph edge " + edge.name + " size " + referencedNodes.size )
		if (referencedNodes.size > 1) {
			if (referencedNodes.size == 2) { /** direct link */
				drawEdge(nodeMap.get(referencedNodes.get(0)), nodeMap.get(referencedNodes.get(1)), edge)
			} else { /** hyper edge */
				siblings += drawHyperEdge(edge, referencedNodes)
			}
		}		
	}
	
 	private def drawHyperEdge(Edge graphEdge, Iterable<Node> nodes) {
		val edgeNode = graphEdge.createNode => [
			it.addEllipse() => [
				it.lineWidth = 2
				//it.addText(graphEdge.name)
			]
		]
		
		nodes.forEach[node | drawEdge(edgeNode, nodeMap.get(node), graphEdge)]
		System.out.println("edge " + edgeNode)
		return edgeNode
	}
	
	private def drawEdge(KNode left, KNode right, Edge graphEdge) {
		System.out.println("draw edge " + left + " " + right)
		createEdge() => [
			it.source = left
            it.target = right
            it.addPolyline => [
            	it.lineWidth = 2
            	it.lineStyle = LineStyle.SOLID
            ]
		]
	}

}