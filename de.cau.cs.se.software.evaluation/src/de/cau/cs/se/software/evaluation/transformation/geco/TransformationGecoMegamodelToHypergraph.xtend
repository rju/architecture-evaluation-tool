package de.cau.cs.se.software.evaluation.transformation.geco

import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import de.cau.cs.se.geco.architecture.architecture.GecoModel
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import java.util.Map
import de.cau.cs.se.geco.architecture.architecture.Model
import de.cau.cs.se.software.evaluation.hypergraph.Node
import java.util.HashMap
import de.cau.cs.se.geco.architecture.architecture.Weaver
import de.cau.cs.se.geco.architecture.architecture.Generator
import org.eclipse.xtext.common.types.JvmGenericType

import static extension de.cau.cs.se.geco.architecture.typing.ArchitectureTyping.*
import static extension de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper.*
import de.cau.cs.se.geco.architecture.architecture.CombinedModel
import de.cau.cs.se.geco.architecture.architecture.TargetModel
import de.cau.cs.se.geco.architecture.architecture.SeparateModels

/**
 * Transform geco model to a hypergraph.
 */
class TransformationGecoMegamodelToHypergraph extends AbstractTransformation<GecoModel, Hypergraph> {
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(GecoModel input) {
		result = HypergraphFactory.eINSTANCE.createHypergraph
		val Map<Model, Node> modelNodeMap = new HashMap<Model, Node>()
		
		input.models.forEach[sequence | 
			sequence.models.forEach[
				modelNodeMap.put(it, result.createNode(it.name, it))
			]
		]
		input.fragments.forEach[p | 
			switch (p) {
				Weaver: {
					val weaverNode = result.createNode(p.reference.simpleName, p)
					val baseModelNode = modelNodeMap.get(p.resolveWeaverSourceModel.reference)
					
					result.createEdge(weaverNode, baseModelNode, weaverNode.name + "::" + baseModelNode.name, null)
					
					switch(p.aspectModel) {
						CombinedModel: createWeaver(p.aspectModel as CombinedModel, modelNodeMap, weaverNode)
						SeparateModels: createSeparatePointcutWeaver(p.aspectModel as SeparateModels,
							modelNodeMap, weaverNode
						)	
					}
				}
				Generator: {
					val generatorNode = result.createNode(p.reference.simpleName, p)
					if (p.sourceModel.reference != null) {
						val sourceModelNode = modelNodeMap.get(p.sourceModel.reference)
						result.createEdge(generatorNode, sourceModelNode, generatorNode.name + "::" + sourceModelNode.name, null)
					}
					
					val targetModelNode = modelNodeMap.get(p.targetModel.reference)
					result.createEdge(generatorNode, targetModelNode, generatorNode.name + "::" + targetModelNode.name, null)
				}
			}
		]
		
		return result
	}
	
	/**
	 * create point cut reference for weaver.
	 */
	private def createSeparatePointcutWeaver(SeparateModels model, Map<Model, Node> modelNodeMap, Node weaverNode) {
		model.advice.createWeaver(modelNodeMap, weaverNode)
		val pointcutModelNode = modelNodeMap.get(model.pointcut.reference)
		result.createEdge(weaverNode, pointcutModelNode, weaverNode.name + "::" + pointcutModelNode.name, null)
	}
	
	/**
	 * create advice/aspect edge.
	 */
	private def createWeaver(CombinedModel adviceModel, Map<Model, Node> modelNodeMap, Node weaverNode) {
		switch (adviceModel) {
			Generator: {
				val aspectModelNode = if (adviceModel.reference instanceof JvmGenericType) {
					val aspectModelType = (adviceModel.reference as JvmGenericType).determineGeneratorOutputType
					result.createNode("anonymous model " + aspectModelType.simpleName, aspectModelType)
				} else {
					result.createNode("anonymous model " + "type unknown", adviceModel.reference)
				}
				val generatorNode = result.createNode(adviceModel.reference.simpleName, adviceModel)
				val sourceModelNode = modelNodeMap.get(adviceModel.sourceModel.reference)
				
				result.createEdge(generatorNode, sourceModelNode, generatorNode.name + "::" + sourceModelNode.name, null)
				result.createEdge(generatorNode, aspectModelNode, generatorNode.name + "::" + aspectModelNode.name, null)
				result.createEdge(weaverNode, aspectModelNode, weaverNode.name + "::" + aspectModelNode.name, null)
			}
			TargetModel: {
			 	val aspectModelNode = modelNodeMap.get(adviceModel as Model)
			 	result.createEdge(weaverNode, aspectModelNode, weaverNode.name + "::" + aspectModelNode.name, null)
			}
		}
	}
	
	override workEstimate(GecoModel input) {
		input.models.size +
		input.fragments.size
	}
		
}