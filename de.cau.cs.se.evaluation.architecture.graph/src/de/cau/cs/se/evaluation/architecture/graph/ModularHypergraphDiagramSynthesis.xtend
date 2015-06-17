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

    
    override KNode transform(ModularHypergraph model) {
        val root = model.createNode().associateWith(model);
        
        root => [
        	it.addLayoutParam(LayoutOptions::ALGORITHM, "de.cau.cs.kieler.kiml.ogdf.planarization")
            it.addLayoutParam(LayoutOptions::SPACING, 75f)
            it.addLayoutParam(LayoutOptions::DIRECTION, Direction::UP)
            
            model.modules.forEach[module | it.children += module.createModule]
            createEdges(model.nodes)
        ]
        
        return root;
    }
	
	/**
	 * Draw module as a rectangle with its nodes inside.
	 * 
	 * @param module the module to be rendered
	 */
	def createModule(Module module) {
		// TODO a module should be a rectangle with preferably round corners,
		// a top-left name, and a set of nodes inside. 
		module.createNode().associateWith(module) => [
			it.addRoundedRectangle(10, 10) => [
				it.lineWidth = 2
                it.setBackgroundGradient("white".color, "LemonChiffon".color, 0)
                it.shadow = "black".color
                it.setGridPlacement(2).from(LEFT, 2, 0, TOP, 2, 0).to(RIGHT, 2, 0, BOTTOM, 2, 0)
                it.addRectangle => [
                it.invisible = true
                it.addText(module.name).associateWith(module) => [
                	it.fontSize = 18
                	it.fontBold = true
                	it.cursorSelectable = true
                	it.setLeftTopAlignedPointPlacementData(1,1,1,1)
                	//it.setLeftTopAlignedPointPlacementData(1,1,1,1)
                	//it.setAreaPlacementData.from(LEFT, 1, 0, TOP, 1, 0.5f).to(RIGHT, 20, 0, TOP, 20, 0.5f)               	
                ]
                ]  
                module.nodes.forEach[node | module.createNode().children += node.createGraphNode]
                ]
			]
			//module.nodes.forEach[node | it.children += node.createGraphNode]
	}
	
	/**
	 * Draw a single node as a circle with its name at the center.
	 * 
	 * @param node the node to be rendered
	 */
	def createGraphNode(Node node) {
		node.createNode().associateWith(node) => [
			it.addEllipse => [
				it.lineWidth = 2
                it.background = "white".color
                it.addText(node.name).associateWith(node) => [
                	it.fontSize = 15
                	it.fontBold = true
                	it.cursorSelectable = true
                	it.setAreaPlacementData.from(LEFT, 20, 0, TOP, 1, 0.5f).to(RIGHT, 20, 0, BOTTOM, 10, 0)
                ]
			]
		]
	}
	
	def createEdges(EList<Node> nodes){
		for (var i = 0; i<nodes.size; i++){
			//for (var j = 0; j<nodes.get(i).getEdges.size; j++){
			for(Edge j : nodes.get(i).edges){
				var temp = findNode(j, nodes, nodes.get(i))
				if(temp != null){
				val child = temp//getKNode(root)
				val parent = nodes.get(i)//getKNode(root)
			new Pair(child,parent).createEdge() => [
				
            it.addLayoutParam(LayoutOptions::EDGE_TYPE, EdgeType::GENERALIZATION);
            //add semantic data
            it.getData(typeof(KLayoutData)).setProperty(KlighdProperties.SEMANTIC_DATA, 
                        KlighdSemanticDiagramData.of(KlighdConstants.SEMANTIC_DATA_CLASS, "inheritence"))
    	    
    	    it.source = child.node;
	        it.target = parent.node;
	        it.data addPolyline() => [
                it.lineWidth = 2;
                it.foreground = "gray25".color
                //it.addInheritanceTriangleArrowDecorator();
	        ]		    
		]
				}
			}
		}
	}
	
	def Node findNode(Edge edge, EList<Node> nodes, Node parent){
		var Node result = null
		System.out.println(edge.name)
		//TODO teste ob es ein(?) anderen(!) Node mit der gleicher Edge gibt (oder mehr)
		for (var i = 0; i<nodes.size; i++){
			var test1 = nodes.get(i)
			if(nodes.get(i) != parent){
				var test = nodes.get(i).getEdges.size
		//for (var j = 0; j<nodes.get(i).getEdges.size; j++){
		for(Edge j : nodes.get(i).edges){
			System.out.println(j.name)
			if(edge.name.equals(j.name)){
				result = nodes.get(i)
			}
		}}}
		return result
	}  
}
