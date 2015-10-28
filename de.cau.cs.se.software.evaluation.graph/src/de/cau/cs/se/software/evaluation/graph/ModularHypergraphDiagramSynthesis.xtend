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
package de.cau.cs.se.software.evaluation.graph

import com.google.common.collect.ImmutableList
import de.cau.cs.kieler.core.kgraph.KNode
import de.cau.cs.kieler.core.kgraph.KPort
import de.cau.cs.kieler.core.krendering.KColor
import de.cau.cs.kieler.core.krendering.KContainerRendering
import de.cau.cs.kieler.core.krendering.KRenderingFactory
import de.cau.cs.kieler.core.krendering.LineJoin
import de.cau.cs.kieler.core.krendering.LineStyle
import de.cau.cs.kieler.core.krendering.extensions.KColorExtensions
import de.cau.cs.kieler.core.krendering.extensions.KContainerRenderingExtensions
import de.cau.cs.kieler.core.krendering.extensions.KEdgeExtensions
import de.cau.cs.kieler.core.krendering.extensions.KLabelExtensions
import de.cau.cs.kieler.core.krendering.extensions.KNodeExtensions
import de.cau.cs.kieler.core.krendering.extensions.KPolylineExtensions
import de.cau.cs.kieler.core.krendering.extensions.KPortExtensions
import de.cau.cs.kieler.core.krendering.extensions.KRenderingExtensions
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout
import de.cau.cs.kieler.kiml.options.Direction
import de.cau.cs.kieler.kiml.options.EdgeRouting
import de.cau.cs.kieler.kiml.options.LayoutOptions
import de.cau.cs.kieler.kiml.options.SizeConstraint
import de.cau.cs.kieler.klay.layered.properties.Properties
import de.cau.cs.kieler.klighd.SynthesisOption
import de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Module
import de.cau.cs.se.software.evaluation.hypergraph.Node
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import javax.inject.Inject
import org.eclipse.emf.common.util.EList
import static extension de.cau.cs.se.software.evaluation.graph.DiagramModelHelper.*
import de.cau.cs.kieler.kiml.options.PortConstraints

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
		     
		     
	/** changes in visualization nodes on off */
    private static val VISIBLE_NODES_NAME = "Nodes Visible"
    private static val VISIBLE_NODES_NO = "Modules only"
    private static val VISIBLE_NODES_YES = "Show nodes in modules"
    
    /** changes in visualization anonymous classes on off */
    private static val VISIBLE_ANONYMOUS_NAME = "Anonymous Classes"
    private static val VISIBLE_ANONYMOUS_NO = "hidden"
    private static val VISIBLE_ANONYMOUS_YES = "visible"
    
    /** changes in visualization anonymous classes on off */
//    private static val VISIBLE_FRAMEWORK_NAME = "Framework Classes"
//    private static val VISIBLE_FRAMEWORK_NO = "hidden"
//    private static val VISIBLE_FRAMEWORK_YES = "visible"
    
    /** changes in layout direction */
    private static val DIRECTION_NAME = "Layout Direction"
    private static val DIRECTION_UP = "up"
    private static val DIRECTION_DOWN = "down"
    private static val DIRECTION_LEFT = "left"
    private static val DIRECTION_RIGHT = "right"
    
    /** changes in edge routing */
    private static val ROUTING_NAME = "Edge Routing"
    private static val ROUTING_POLYLINE = "polyline"
    private static val ROUTING_ORTHOGONAL = "orthogonal"
    private static val ROUTING_SPLINES = "splines"
    
    private static val SPACING_NAME = "Spacing"
    
    /**
     * The filter option definition that allows users to customize the constructed class diagrams.
     */
    private static val SynthesisOption VISIBLE_NODES = SynthesisOption::createChoiceOption(VISIBLE_NODES_NAME,
       ImmutableList::of(VISIBLE_NODES_YES, VISIBLE_NODES_NO), VISIBLE_NODES_NO)
       
    private static val SynthesisOption VISIBLE_ANONYMOUS = SynthesisOption::createChoiceOption(VISIBLE_ANONYMOUS_NAME,
       ImmutableList::of(VISIBLE_ANONYMOUS_YES, VISIBLE_ANONYMOUS_NO), VISIBLE_ANONYMOUS_NO)
       
 //   private static val SynthesisOption VISIBLE_FRAMEWORK = SynthesisOption::createChoiceOption(VISIBLE_FRAMEWORK_NAME,
 //      ImmutableList::of(VISIBLE_FRAMEWORK_YES, VISIBLE_FRAMEWORK_NO), VISIBLE_FRAMEWORK_YES)
       
    private static val SynthesisOption DIRECTION = SynthesisOption::createChoiceOption(DIRECTION_NAME,
       ImmutableList::of(DIRECTION_UP, DIRECTION_DOWN, DIRECTION_LEFT, DIRECTION_RIGHT), DIRECTION_LEFT)
       
    private static val SynthesisOption ROUTING = SynthesisOption::createChoiceOption(ROUTING_NAME,
       ImmutableList::of(ROUTING_POLYLINE, ROUTING_ORTHOGONAL, ROUTING_SPLINES), ROUTING_ORTHOGONAL)
       
   	private static val SynthesisOption SPACING = SynthesisOption::createRangeOption(SPACING_NAME, 5f, 200f, 50f)
       
    
    var Map<Node,KNode> nodeMap = new HashMap<Node,KNode>()
    var Map<Module,KNode> moduleMap = new HashMap<Module,KNode>()
    var Map<String,KPort> portMap = new HashMap<String,KPort>()
    var processedModules = new ArrayList<Module>()
       
    /**
     * {@inheritDoc}<br>
     * <br>
     * Registers the diagram filter option declared above, which allow users to tailor the constructed diagrams.
     */
    override public getDisplayedSynthesisOptions() {
        return ImmutableList::of(VISIBLE_NODES, VISIBLE_ANONYMOUS, /*VISIBLE_FRAMEWORK,*/ DIRECTION, ROUTING, SPACING)
    }

    
    override KNode transform(ModularHypergraph hypergraph) {
        val root = hypergraph.createNode().associateWith(hypergraph);
                
        root => [
            // de.cau.cs.kieler.klay.layered.properties.Properties
            it.setLayoutOption(Properties.MERGE_EDGES, true)
            it.setLayoutOption(LayoutOptions.LAYOUT_HIERARCHY, true)
            it.setLayoutOption(LayoutOptions::ALGORITHM, "de.cau.cs.kieler.klay.layered")	

            it.setLayoutOption(LayoutOptions::SPACING, SPACING.objectValue as Float)
            it.setLayoutOption(LayoutOptions::DIRECTION, switch(DIRECTION.objectValue) {
            	case DIRECTION_UP: Direction::UP
            	case DIRECTION_DOWN: Direction::DOWN
            	case DIRECTION_LEFT: Direction::LEFT
            	case DIRECTION_RIGHT: Direction::RIGHT
            })
            it.setLayoutOption(LayoutOptions::EDGE_ROUTING, switch(ROUTING.objectValue) {
            	case ROUTING_POLYLINE: EdgeRouting::POLYLINE
            	case ROUTING_ORTHOGONAL: EdgeRouting::ORTHOGONAL
            	case ROUTING_SPLINES: EdgeRouting::SPLINES
            })
            
            if (VISIBLE_NODES.objectValue == VISIBLE_NODES_NO) {
            	hypergraph.modules.forEach[module | 
            		if (!(module.kind == EModuleKind.ANONYMOUS && VISIBLE_ANONYMOUS.objectValue == VISIBLE_ANONYMOUS_NO)) 
						root.children += module.createEmptyModule
            	]
            	hypergraph.modules.forEach[module | 
            		if (!(module.kind == EModuleKind.ANONYMOUS && VISIBLE_ANONYMOUS.objectValue == VISIBLE_ANONYMOUS_NO)) 
            			module.createCombindEdges(hypergraph)
            	]
            } else {
        		hypergraph.modules.forEach[module | root.children += module.createModuleWithNodes]
        		hypergraph.edges.forEach[edge | edge.createGraphEdge(hypergraph.nodes, root.children)]    	
            }      
            
        ]
        
        return root
    }
    
    /**
     * Return the correct color for a module.
     */
    private def getBackgroundColor(Module module) {
		switch (module.kind) {
			case SYSTEM: "LemonChiffon".color
			case FRAMEWORK: "Blue".color
			case ANONYMOUS: "Orange".color
			case INTERFACE: "White".color
		}
		
	} 
	
	/** -- aggregated view ------------------ */
	
	private def void createCombindEdges(Module sourceModule, ModularHypergraph hypergraph) {
		val edges = new ArrayList<Edge>()
		sourceModule.nodes.forEach[node | node.edges.forEach[edges.addUnique(it)]]
		processedModules.add(sourceModule)
		
		val targetModuleMap = new HashMap<Module,Integer>()
		edges.forEach[edge |
			hypergraph.nodes.filter[node | node.edges.exists[it.equals(edge)]].forEach[node |
				if (!sourceModule.nodes.exists[it.equals(node)]) {
					/** external node, determine module */
					val targetModule = hypergraph.modules.findFirst[module | module.nodes.exists[it.equals(node)]]
					if (targetModule.determineVisibility) {
						targetModuleMap.registerConnection(targetModule)
					} else {
						/** module is not shown, compute transitive set. */
						targetModule.computeTransitiveModules(hypergraph).forEach[transitive |
							if (!transitive.equals(sourceModule))
								targetModuleMap.registerConnection(targetModule)
						]
					}
				}
			]
		]
		
		targetModuleMap.forEach[module, count | createAggregatedEdge(sourceModule, module, count) ]
	}
	
	/**
	 * Determine if the given module should be visible depending on type and option values.
	 */
	private def boolean determineVisibility(Module module) {
		switch(module.kind) {
			case FRAMEWORK: return true // VISIBLE_FRAMEWORK.objectValue == VISIBLE_FRAMEWORK_YES
			case ANONYMOUS: return VISIBLE_ANONYMOUS.objectValue == VISIBLE_ANONYMOUS_YES
			case INTERFACE: return true
			case SYSTEM: true
			default: true
		}
	}
	
	
	/**
	 * Calculate transitive set of modules.
	 */
	private def List<Module> computeTransitiveModules(Module sourceModule, ModularHypergraph hypergraph) {
		val transitiveModules = new ArrayList<Module>()
		
		val edges = new ArrayList<Edge>()
		sourceModule.nodes.forEach[node | node.edges.forEach[edges.addUnique(it)]]
		
		edges.forEach[edge |
			hypergraph.nodes.filter[node | node.edges.exists[it.equals(edge)]].forEach[node |
				if (!sourceModule.nodes.exists[it.equals(node)]) {
					val targetModule = hypergraph.modules.findFirst[module | module.nodes.exists[it.equals(node)]]
					if (targetModule.determineVisibility) {
						transitiveModules.addUnique(targetModule)
					} else {
						/** module is not shown, compute transitive set. */
						transitiveModules.addAllUnique(targetModule.computeTransitiveModules(hypergraph))	
					}
				}
			]
		]
		
		
		return transitiveModules
	}
	
	/**
	 * Register a target module or increase its hit count.
	 */
	private def registerConnection(HashMap<Module, Integer> targetModules, Module targetModule) {
		if (!processedModules.exists[it.equals(targetModule)]) {
			var value = targetModules.get(targetModule)
			if (value == null) {
				targetModules.put(targetModule,1)
			} else {
				targetModules.put(targetModule,value+1)
			}
		}
	}
	
	/**
	 * Create an edge in the correct width for an aggregated edge.
	 */
	private def createAggregatedEdge(Module sourceModule, Module targetModule, Integer size) {
		val sourceNode = moduleMap.get(sourceModule)
		val targetNode = moduleMap.get(targetModule)
		
		val lineWidth = if (size <= 1)
    		1
    	else if (size <= 3)
    		2
    	else if (size <= 7)
    		3
    	else if (size <= 10)
    		4
    	else if (size <= 15)
    		5
    	else if (size <= 20)
    		6
    	else
    		7
    	val portSize = lineWidth + 2
		
		val sourcePort = createPort() => [
   			it.setPortSize(portSize,portSize)
    		it.addRectangle.setBackground("black".color).lineJoin=LineJoin.JOIN_ROUND
    	]
    	
    	val targetPort = createPort() => [
   			it.setPortSize(portSize,portSize)
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
            	it.lineWidth = lineWidth
            	it.lineStyle = LineStyle.SOLID
            ]
		]
	}
	   
    
    /**
     * Create module without nodes for simple display.
     */
    private def KNode createEmptyModule(Module module) {
		val moduleQualifier = if (module.kind == EModuleKind.ANONYMOUS) {
			val separator = module.name.lastIndexOf('$')
			module.name.substring(0,separator)
		} else
			module.name
		val separator = moduleQualifier.lastIndexOf('.')
		val moduleNode = if (separator == -1) 
			drawEmptyModule(module, 
				"", 
				moduleQualifier,
				module.backgroundColor
			)
		else
			drawEmptyModule(module, 
				moduleQualifier.substring(0,separator), 
				moduleQualifier.substring(separator+1),
				module.backgroundColor
			)
		moduleMap.put(module, moduleNode)
		
		return moduleNode
	}
	
	/**
	 * Draw the empty module.
	 */
	private def KNode drawEmptyModule(Module module, String packageName, String moduleName, KColor backgroundColor) {
		val moduleNode = module.createNode().associateWith(module)
				
		moduleNode => [
			it.setLayoutOption(LayoutOptions::PORT_SPACING, 20f)
			it.setLayoutOption(LayoutOptions::SIZE_CONSTRAINT, SizeConstraint.minimumSizeWithPorts)
			it.addRoundedRectangle(10, 10) => [
				it.lineWidth = 2
                it.setBackgroundGradient("white".color, backgroundColor, 0)
                it.shadow = "black".color
                it.setGridPlacement(1).from(LEFT, 10, 0, TOP, 20, 0).to(RIGHT, 10, 0, BOTTOM, 20, 0)
	            it.addText(packageName) => [
	              	it.fontBold = false
	               	it.cursorSelectable = false
	               	it.setLeftTopAlignedPointPlacementData(1,1,1,1)            	
	            ]
	            it.addText(moduleName) => [
	               	it.fontBold = true
	               	it.cursorSelectable = true
	               	it.setLeftTopAlignedPointPlacementData(1,1,1,1)
                ]
            ]
		]
	}
	
	/** -- complete view ------------------ */
    
    /**
	 * Draw module as a rectangle with its nodes inside.
	 * 
	 * @param module the module to be rendered
	 */
	private def createModuleWithNodes(Module module) {
		// a module should be a rectangle with preferably round corners,
		// a top-left name, and a set of nodes inside.
						 
		val moduleNode = module.createNode().associateWith(module)
		moduleNode.getData(KShapeLayout).insets.top = 15 //bringt nicht wirklich Effekt

		moduleNode => [	
			it.setLayoutOption(LayoutOptions.PORT_CONSTRAINTS, PortConstraints.FREE)
			it.setLayoutOption(LayoutOptions::EDGE_ROUTING, EdgeRouting::POLYLINE)
			it.addRoundedRectangle(10, 10) => [
				it.lineWidth = 2
                it.setBackgroundGradient("white".color, module.backgroundColor, 0)
                it.shadow = "black".color
                it.setGridPlacement(1).from(LEFT, 10, 0, TOP, 10, 0).to(RIGHT, 10, 0, BOTTOM, 10, 0)
                it.addText(module.name) => [
                	it.fontBold = true
                	it.cursorSelectable = true
                	it.setLeftTopAlignedPointPlacementData(1,1,1,1)            	
                ]
                it.addChildArea()             
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
			it.setLayoutOption(LayoutOptions.PORT_CONSTRAINTS, PortConstraints.FREE)
			
			//node.edges.forEach[graphEdge | graphEdge.createEdgePort(it, graphEdge.name)]
			it.addRoundedRectangle(2,2) => [ 
				it.lineWidth = 2
                it.background = "white".color
                it.setSurroundingSpace(1,0,1,0)
                it.addText(node.name.substring(module.name.length+1)) => [
                	it.setSurroundingSpace(10,0,10,0)
                	it.cursorSelectable = true
                ]
			]
		] 
		
	}
	
	//create iff not exists & add to portMap & return port
	private def KPort getOrCreateEdgePort(KNode kNode, String label) {
		if (portMap.get(label) == null) {
			kNode.ports.add(createPort() => [
	    		it.setPortSize(2,2)
	    		it.addRectangle.setBackground("black".color).lineJoin=LineJoin.JOIN_ROUND
	    		portMap.put(label,it)
	    	])
	    }
    	return portMap.get(label)
	}

	private def createGraphEdge(Edge edge, EList<Node> nodes, EList<KNode> siblings) {		
		val referencedNodes = nodes.filter[node | node.edges.exists[it.equals(edge)]]
		//System.out.println("graph edge " + edge.name + " size " + referencedNodes.size )
		if (referencedNodes.size > 1) {
			if (referencedNodes.size == 2) { /** direct link */
				constructEdge(nodeMap.get(referencedNodes.get(0)), nodeMap.get(referencedNodes.get(1)), edge)
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
		nodes.forEach[node | constructEdge(edgeNode, nodeMap.get(node), graphEdge)]
		//System.out.println("edge " + edgeNode)
		return edgeNode
	}
	
	private def constructEdge(KNode left, KNode right, Edge graphEdge) {
		//System.out.println("draw edge " + left + " " + right)
		
		//draw edges direct iff same parentNodes
		if(right.parent.equals(left.parent)) {	
			drawEdge(left, right, 
				getOrCreateEdgePort(left, left.toString + '_to_' + right.toString), 
				getOrCreateEdgePort(right, right.toString + '_to_' + left.toString)
			)	
			// else use ports	
		} else {
			//if no HyperEdgePart
			if(left.parent != null) {		
				//Edge from inner-Node to parentPort (left) (iff not exists yet)
				if (portMap.get(left.toString + '_to_' + right.parent.toString) == null) {
					drawEdge(left, left.parent,
						getOrCreateEdgePort(left, left.toString + '_to_' + right.parent.toString),
						getOrCreateEdgePort(left.parent, left.parent.toString + '_to_' + right.parent.toString)
					)
				}
				//Edge between parentPorts (iff not exists yet)
				if (portMap.get(right.parent.toString + '_to_' + left.parent.toString) == null) {
					drawEdge(left.parent, right.parent,
						getOrCreateEdgePort(left.parent, left.parent.toString + '_to_' + right.parent.toString),
						getOrCreateEdgePort(right.parent, right.parent.toString + '_to_' + left.parent.toString)
					)
				}
				//Edge from inner-Node to parentPort (right) (iff not exists yet)
				if (portMap.get(right.toString + '_to_' + left.parent.toString) == null) {
					drawEdge(right, right.parent, 
						getOrCreateEdgePort(right, right.toString + '_to_' + left.parent.toString),
						getOrCreateEdgePort(right.parent, right.parent.toString + '_to_' + left.parent.toString)
					)
				}		
			} else { //if HyperEdgePart
				//Edge between parentPort and HyperEdgeNode
				drawEdge(left, right.parent,
					getOrCreateEdgePort(left, left.toString),
					getOrCreateEdgePort(right.parent, right.parent.toString + '_to_' + left.toString)
	            )
				//Edge from inner-Node to parentPort (right)
				drawEdge(right, right.parent,
					getOrCreateEdgePort(right, right.toString + '_to_' + left.toString),
					getOrCreateEdgePort(right.parent, right.parent.toString + '_to_' + left.toString)
	            )
			}			
		}
	}
	
	/**
	 * Draw a single edge.
	 */
	private def void drawEdge(KNode left, KNode right, KPort leftPort, KPort rightPort) {
		createEdge() => [
			it.source = left
            it.target = right
            it.sourcePort = leftPort
            it.targetPort = rightPort
            it.addPolyline => [
            	it.lineWidth = 2
            	it.lineStyle = LineStyle.SOLID
            ]
		]
	}

}