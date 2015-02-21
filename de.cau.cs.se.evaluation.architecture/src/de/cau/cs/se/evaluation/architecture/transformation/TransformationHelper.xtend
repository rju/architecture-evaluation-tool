package de.cau.cs.se.evaluation.architecture.transformation

import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.Module
import org.eclipse.jdt.core.dom.Type
import java.util.List
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.NameQualifiedType
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import org.eclipse.jdt.core.IMethod
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.jdt.core.dom.Expression

class TransformationHelper {
	
	def static deriveNode(Node node) {
		val resultNode = HypergraphFactory.eINSTANCE.createNode
		resultNode.name = node.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace
		derivedFrom.node = node
		resultNode.derivedFrom = derivedFrom
		
		return resultNode
	}
	
	def static deriveEdge(Edge edge) {
		val resultEdge = HypergraphFactory.eINSTANCE.createEdge
		resultEdge.name = edge.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace
		derivedFrom.edge = edge
		resultEdge.derivedFrom = derivedFrom
		
		return resultEdge
	}
	
	def static deriveModule(Module module) {
		val resultModule = HypergraphFactory.eINSTANCE.createModule
		resultModule.name = module.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createModuleTrace
		derivedFrom.module = module
		resultModule.derivedFrom = derivedFrom
		
		return resultModule
	}
	
	def static findConstructorMethod(ModularHypergraph graph, Type type, List arguments) {
		val typeName = switch(type) {
			SimpleType : type.name
			NameQualifiedType : type.name
			default: return null
		}
		 
		val module = graph.modules.findFirst[module | 
			((module.derivedFrom as TypeTrace).type as IType).elementName.equals(typeName.fullyQualifiedName)
		]
		val moduleName = ((module.derivedFrom as TypeTrace).type as IType).elementName
		val nodes = module.nodes.filter[node | 
			((node.derivedFrom as MethodTrace).method as IMethod).elementName.equals(moduleName)
		]
		return matchArguments(nodes, arguments)
	}

	def static matchArguments(Iterable<Node> nodes, List arguments) {
		nodes.findFirst[node |
			val method = (node.derivedFrom as MethodTrace).method as MethodDeclaration
			val parameters = method.parameters
			if (parameters.size == arguments.size)
				compareArgAndParam(parameters, arguments)
			else
				false
		]
	}
	
	/** returns true if the arguments match the parameters. */
	def static boolean compareArgAndParam(List parameters, List arguments) {
		// arguments are Expression
		// parameters SingleVariableDeclaration
		for(var i=0;i < parameters.size ; i++) {
			val pType = (parameters.get(i) as SingleVariableDeclaration).type.resolveBinding.erasure
			val aType = (arguments.get(i) as Expression).resolveTypeBinding.erasure
			
			if (!pType.isAssignmentCompatible(aType))
				return false	
		}
		
		return true
	}
	
}