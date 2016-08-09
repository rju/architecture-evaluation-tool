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
import de.cau.cs.kieler.klighd.SynthesisOption
import de.cau.cs.kieler.klighd.krendering.HorizontalAlignment
import de.cau.cs.kieler.klighd.krendering.KRenderingFactory
import de.cau.cs.kieler.klighd.krendering.LineStyle
import de.cau.cs.kieler.klighd.krendering.VerticalAlignment
import de.cau.cs.kieler.klighd.krendering.extensions.KColorExtensions
import de.cau.cs.kieler.klighd.krendering.extensions.KContainerRenderingExtensions
import de.cau.cs.kieler.klighd.krendering.extensions.KEdgeExtensions
import de.cau.cs.kieler.klighd.krendering.extensions.KLabelExtensions
import de.cau.cs.kieler.klighd.krendering.extensions.KNodeExtensions
import de.cau.cs.kieler.klighd.krendering.extensions.KPolylineExtensions
import de.cau.cs.kieler.klighd.krendering.extensions.KPortExtensions
import de.cau.cs.kieler.klighd.krendering.extensions.KRenderingExtensions
import de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Node
import java.util.HashMap
import java.util.Map
import javax.inject.Inject
import org.eclipse.elk.alg.layered.properties.LayeredOptions
import org.eclipse.elk.core.options.Direction
import org.eclipse.elk.core.options.EdgeRouting
import org.eclipse.elk.graph.KNode

class HypergraphDiagramSynthesis extends AbstractDiagramSynthesis<Hypergraph> {
    
	@Inject extension KNodeExtensions
	@Inject extension KEdgeExtensions
    @Inject extension KPortExtensions
    @Inject extension KLabelExtensions
    @Inject extension KRenderingExtensions
    @Inject extension KContainerRenderingExtensions
    @Inject extension KPolylineExtensions
    @Inject extension KColorExtensions
    extension KRenderingFactory = KRenderingFactory.eINSTANCE
		         
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
    private static val SynthesisOption DIRECTION = SynthesisOption::createChoiceOption(DIRECTION_NAME,
       ImmutableList::of(DIRECTION_UP, DIRECTION_DOWN, DIRECTION_LEFT, DIRECTION_RIGHT), DIRECTION_LEFT)
       
    private static val SynthesisOption ROUTING = SynthesisOption::createChoiceOption(ROUTING_NAME,
       ImmutableList::of(ROUTING_POLYLINE, ROUTING_ORTHOGONAL, ROUTING_SPLINES), ROUTING_POLYLINE)
       
   	private static val SynthesisOption SPACING = SynthesisOption::createRangeOption(SPACING_NAME, 5f, 200f, 50f)
       
    
    var Map<Node,KNode> nodeMap = new HashMap<Node,KNode>()
    var Map<Edge,KNode> edgeMap = new HashMap<Edge,KNode>()
       
    /**
     * {@inheritDoc}<br>
     * <br>
     * Registers the diagram filter option declared above, which allow users to tailor the constructed diagrams.
     */
    override public getDisplayedSynthesisOptions() {
        return ImmutableList::of(DIRECTION, ROUTING, SPACING)
    }

    
    override KNode transform(Hypergraph hypergraph) {
        val root = hypergraph.createNode().associateWith(hypergraph);
                
        root => [
        	// TODO the merging must be false
            // de.cau.cs.kieler.klay.layered.properties.Properties
            // it.setLayoutOption(Properties.MERGE_EDGES, false)
            it.setLayoutOption(LayeredOptions.LAYOUT_HIERARCHY, false)
//            it.setLayoutOption(LayeredOptions::ALGORITHM, "de.cau.cs.kieler.kiml.ogdf.circular")	

            it.setLayoutOption(LayeredOptions::SPACING_NODE, SPACING.objectValue as Float)
            it.setLayoutOption(LayeredOptions::DIRECTION, switch(DIRECTION.objectValue) {
            	case DIRECTION_UP: Direction::UP
            	case DIRECTION_DOWN: Direction::DOWN
            	case DIRECTION_LEFT: Direction::LEFT
            	case DIRECTION_RIGHT: Direction::RIGHT
            })
            it.setLayoutOption(LayeredOptions::EDGE_ROUTING, switch(ROUTING.objectValue) {
            	case ROUTING_POLYLINE: EdgeRouting::POLYLINE
            	case ROUTING_ORTHOGONAL: EdgeRouting::ORTHOGONAL
            	case ROUTING_SPLINES: EdgeRouting::SPLINES
            })
            
            
            hypergraph.nodes.forEach[node | root.children += node.createGraphNode]
        	hypergraph.edges.forEach[edge | root.children += edge.createGraphEdge]
        	hypergraph.nodes.forEach[node | 
        		node.edges.forEach[edge | drawEdge(edgeMap.get(edge), nodeMap.get(node))]
        	]
        ]
        
        return root
    }

	    
   	/**
	 * Draw a single node as a circle with its name at the center.
	 * 
	 * @param node the node to be rendered
	 */
	private def createGraphNode(Node node) {
		val kNode = node.createNode().associateWith(node)
		nodeMap.put(node, kNode)
		kNode => [
			it.addEllipse => [ 
				it.lineWidth = 2
                it.background = "white".color
                it.setSurroundingSpace(0,0,0,0)
                it.addText(node.name) => [
                	it.horizontalAlignment = HorizontalAlignment.CENTER
                	it.verticalAlignment = VerticalAlignment.CENTER
                	it.setAreaPlacementData.from(LEFT, 5, 0, TOP, 5, 0).to(RIGHT, 5, 0, BOTTOM, 5, 0)
                	it.setSurroundingSpace(20,0,30,0)
                	it.cursorSelectable = false
                ]
			]
		] 
		
		return kNode
	}
	
	
	private def createGraphEdge(Edge edge) {		
		//System.out.println("graph edge " + edge.name + " size " + referencedNodes.size )
		val kNode = edge.createNode().associateWith(edge)
		edgeMap.put(edge, kNode)
		kNode => [
			it.addEllipse => [ 
				it.lineWidth = 2
                it.background = "black".color
                it.setSurroundingSpace(0,0,0,0)
			]
		] 
		
		return kNode
	}


	private def drawEdge(KNode left, KNode right) {
		//System.out.println("draw edge " + left + " " + right)
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