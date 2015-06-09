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
		// a centered name, and a set of nodes inside. 
		module.createNode().associateWith(module) => [
			it.addRoundedRectangle(10, 10) => [
				it.lineWidth = 2
                it.setBackgroundGradient("white".color, "LemonChiffon".color, 0)
                it.shadow = "black".color
                it.setGridPlacement(2).from(LEFT, 2, 0, TOP, 2, 0).to(RIGHT, 2, 0, BOTTOM, 2, 0)
                it.addText(module.name).associateWith(module) => [
                	it.fontSize = 15
                	it.fontBold = true
                	it.cursorSelectable = true
                	it.setAreaPlacementData.from(LEFT, 20, 0, TOP, 1, 0.5f).to(RIGHT, 20, 0, BOTTOM, 10, 0)
                ]
			]
			module.nodes.forEach[node | it.children += node.createGraphNode]
		]
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

    
}
