package de.cau.cs.se.software.evaluation.jobs

import com.google.inject.Inject
import de.cau.cs.se.geco.architecture.ArchitectureStandaloneSetup
import de.cau.cs.se.geco.architecture.architecture.Generator
import de.cau.cs.se.geco.architecture.architecture.Model
import de.cau.cs.se.geco.architecture.architecture.Weaver
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import de.cau.cs.se.software.evaluation.views.ResultModelProvider
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.swt.widgets.Shell
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet

import static extension de.cau.cs.se.geco.architecture.typing.ArchitectureTyping.*
import org.eclipse.xtext.common.types.JvmGenericType
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.geco.architecture.architecture.Metamodel
import java.util.HashMap
import java.util.Map

class GecoComplexityAnalysisJob extends AbstractHypergraphAnalysisJob {
	
	val IFile file
	
	/** resource set for the compilation. */
	@Inject
	XtextResourceSet resourceSet
		
	new(IProject project, IFile file, Shell shell) {
		super(project)
		this.file = file
	}
	
	override protected run(IProgressMonitor monitor) {
		val injector = new ArchitectureStandaloneSetup().createInjectorAndDoEMFRegistration()
		injector.injectMembers(this);
		this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		
		val Resource source = resourceSet.getResource(URI.createPlatformResourceURI(file.fullPath.toString, true), true)
		
		if (source.contents.size > 0) {
			val result = ResultModelProvider.INSTANCE
			
			val model = source.contents.get(0) as Model
			
			val inputHypergraph = createGecoModelHypergraph(model)
			
			calculateSize(inputHypergraph, monitor, result)
		
			calculateComplexity(inputHypergraph, monitor, result)			
		} else {
			// TODO issue warning.
		}
								
		monitor.done()
							
		return Status.OK_STATUS	
	}
	
	private def createGecoModelHypergraph(Model model) {
		val hypergraph = HypergraphFactory.eINSTANCE.createHypergraph
		val Map<Metamodel, Node> mmNodeMap = new HashMap<Metamodel, Node>()
		
		model.metamodels.forEach[mm | mm.metamodels.forEach[
			mmNodeMap.put(it, hypergraph.createNode(it.name, it))
		]]
		model.processors.forEach[p | 
			switch (p) {
				Weaver: {
					val weaverNode = hypergraph.createNode(p.reference.simpleName, p)
					val baseModelNode = mmNodeMap.get(p.resolveWeaverSourceModel.reference)
					
					hypergraph.createEdge(weaverNode, baseModelNode, weaverNode.name + "::" + baseModelNode.name)
					
					if (p.aspectModel instanceof Generator) {
						val generator = p.aspectModel as Generator
						val aspectModelNode = if (generator.reference instanceof JvmGenericType) {
							val aspectModelType = (generator.reference as JvmGenericType).determineGeneratorOutputType
							hypergraph.createNode("anonymous model " + aspectModelType.simpleName, aspectModelType)
						} else {
							hypergraph.createNode("anonymous model " + "type unknown", generator.reference)
						}
						val generatorNode = hypergraph.createNode(generator.reference.simpleName, p.aspectModel)
						val sourceModelNode = mmNodeMap.get(generator.sourceModel.reference)
						
						hypergraph.createEdge(generatorNode, sourceModelNode, generatorNode.name + "::" + sourceModelNode.name)
						hypergraph.createEdge(generatorNode, aspectModelNode, generatorNode.name + "::" + aspectModelNode.name)
						hypergraph.createEdge(weaverNode, aspectModelNode, weaverNode.name + "::" + aspectModelNode.name)
					} else {
						 val aspectModelNode = mmNodeMap.get(p.aspectModel as Metamodel)
						 hypergraph.createEdge(weaverNode, aspectModelNode, weaverNode.name + "::" + aspectModelNode.name)
					}
				}
				Generator: {
					val generatorNode = hypergraph.createNode(p.reference.simpleName, p)
					if (p.sourceModel.reference != null) {
						val sourceModelNode = mmNodeMap.get(p.sourceModel.reference)
						hypergraph.createEdge(generatorNode, sourceModelNode, generatorNode.name + "::" + sourceModelNode.name)
					}
					
					val targetModelNode = mmNodeMap.get(p.targetModel.reference)
					hypergraph.createEdge(generatorNode, targetModelNode, generatorNode.name + "::" + targetModelNode.name)
				}
			}
		]
		
		return hypergraph
	}
	
	private def createNode(Hypergraph hypergraph, String name, EObject element) {
		val reference = HypergraphFactory.eINSTANCE.createModelElementTrace
		reference.element = element
		val node = HypergraphFactory.eINSTANCE.createNode
		node.name = name
		node.derivedFrom = reference
		hypergraph.nodes.add(node)
		
		return node
	}
	
	private def createEdge(Hypergraph hypergraph, Node source, Node target, String name) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = name
		hypergraph.edges.add(edge)
		source.edges.add(edge)
		target.edges.add(edge)
	}
	
	
}