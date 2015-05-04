package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.ConstructorInvocation
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import org.eclipse.jdt.core.dom.SuperConstructorInvocation
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IMethodBinding

class JavaHypergraphElementFactory {
		
	/**
	 * Create a module for the given class or interface.
	 * 
	 * @param type referencing a class or interface
	 * 
	 * @return one new module
	 */
	def static createModuleForTypeBinding(ITypeBinding type) {
		val module = HypergraphFactory.eINSTANCE.createModule
		module.name = type.determineFullyQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
		derivedFrom.type = type
		module.derivedFrom = derivedFrom
		
		return module
	}
	
	/**
	 * Create a node for a given method.
	 * 
	 * @param type 
	 * @param method
	 * 
	 * @return a node
	 * 
	 * @deprecated
	 */
	def static createNodeForMethod(AbstractTypeDeclaration type, MethodDeclaration method) {
		val node = HypergraphFactory.eINSTANCE.createNode
		node.name = determineFullyQualifiedName(type, method)
		val derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace
		derivedFrom.method = method
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	/**
	 * Create a node for a given method.
	 * 
	 * @param binding 
	 * 
	 * @return a node
	 */
	def static createNodeForMethod(IMethodBinding binding) {
		val node = HypergraphFactory.eINSTANCE.createNode
		node.name = determineFullyQualifiedName(binding)
		val derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace
		derivedFrom.method = binding
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	/**
	 * Create a node for a class' implicit constructor
	 * 
	 * @param type is the class containing the variable declaration
	 * 
	 * @return a node for the implicit constructor
	 */
	def static createNodeForImplicitConstructor(ITypeBinding type) {
		val node = HypergraphFactory.eINSTANCE.createNode
		node.name = type.determineFullyQualifiedName + "." + type.name
		val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
		derivedFrom.type = type
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	/**
	 * Create a node for a method binding.
	 * 
	 * @param binding to a method
	 * 
	 * @return node derived from that binding
	 */
	def static createNodeForSuperConstructorInvocation(IMethodBinding binding) {
		val node = HypergraphFactory.eINSTANCE.createNode
		node.name = determineFullyQualifiedName(binding)
		val derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace
		derivedFrom.method = binding
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	
	/**
	 * Create for one VariableDeclarationFragment an data edge. The fragment belongs to
	 * an FieldDeclaration.
	 * 
	 * @param type is the class containing the variable declaration
	 * @param fragment is the variable declaration fragment containing the name
	 * 
	 * @return the edge
	 */
	def static createDataEdge(AbstractTypeDeclaration type, VariableDeclarationFragment fragment) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = type.determineFullyQualifiedName(fragment)
		val derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace
		derivedFrom.field = fragment
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	
	/**
	 * Create a call edge between two methods.
	 * 
	 * @param callerType is the class containing the caller method
	 * @param callerMethod method where the call originates
	 * @param calleeType is the class containing the callee method
	 * @param callee the called method 
	 * 
	 * @return the edge
	 */
	def static createCallEdge(AbstractTypeDeclaration callerType, MethodDeclaration callerMethod, AbstractTypeDeclaration calleeType, MethodInvocation calleeMethod) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = determineFullyQualifiedName(callerType, callerMethod) + "::" 
			+ determineFullyQualifiedName(calleeType, calleeMethod)
		val derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace
		derivedFrom.caller = callerMethod
		derivedFrom.callee = calleeMethod
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Create a call edge between two methods.
	 * 
	 * @param callerType is the class containing the caller method
	 * @param callerMethod method where the call originates
	 * @param calleeType is the class containing the callee method
	 * @param callee the called method 
	 * 
	 * @return the edge
	 */
	def static createCallEdge(AbstractTypeDeclaration callerType, MethodDeclaration callerMethod, AbstractTypeDeclaration calleeType, ConstructorInvocation calleeMethod) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = determineFullyQualifiedName(callerType, callerMethod) + "::" 
			+ determineFullyQualifiedName(calleeType, calleeMethod)
		val derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace
		derivedFrom.caller = callerMethod
		derivedFrom.callee = calleeMethod
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Create a call edge between two methods.
	 * 
	 * @param callerType is the class containing the caller method
	 * @param callerMethod method where the call originates
	 * @param calleeType is the class containing the callee method
	 * @param calleeMethod the called method 
	 * 
	 * @return the edge
	 */
	def static createCallEdge(AbstractTypeDeclaration callerType, MethodDeclaration callerMethod, AbstractTypeDeclaration calleeType, SuperConstructorInvocation calleeMethod) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = determineFullyQualifiedName(callerType, callerMethod) + "::" 
			+ determineFullyQualifiedName(calleeType, calleeMethod)
		val derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace
		derivedFrom.caller = callerMethod
		derivedFrom.callee = calleeMethod
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Create a call edge between two methods.
	 * 
	 * @param callerType is the class containing the caller method
	 * @param callerMethod method where the call originates
	 * @param callee the called method in form of a method binding
	 * 
	 * @return the edge
	 */
	def static createCallEdge(AbstractTypeDeclaration callerType, MethodDeclaration callerMethod, IMethodBinding callee) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = determineFullyQualifiedName(callerType, callerMethod) + "::" 
			+ determineFullyQualifiedName(callee)
		val derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace
		derivedFrom.caller = callerMethod
		derivedFrom.callee = callee
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Find the node for the given constructor invocation.
	 * 
	 * @param nodes
	 * @param invocation
	 */
	def static findNodeForConstructoreInvocation(EList<Node> nodes, ConstructorInvocation invocation) {
		nodes.findFirst[((it.derivedFrom as MethodTrace).method as MethodDeclaration).
			resolveBinding.isSubsignature(invocation.resolveConstructorBinding)
		]
	}
}