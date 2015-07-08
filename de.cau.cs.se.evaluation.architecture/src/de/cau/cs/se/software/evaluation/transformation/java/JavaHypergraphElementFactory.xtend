package de.cau.cs.se.software.evaluation.transformation.java

import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.ConstructorInvocation
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SuperConstructorInvocation

import static extension de.cau.cs.se.software.evaluation.transformation.NameResolutionHelper.*
import org.eclipse.jdt.core.dom.IVariableBinding
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind

class JavaHypergraphElementFactory {
		
	/**
	 * Create a module for the given class or interface.
	 * 
	 * @param type referencing a class or interface
	 * @param kind defines what kind of module this is
	 * 
	 * @return one new module
	 */
	def static createModuleForTypeBinding(ITypeBinding type, EModuleKind kind) {
		val module = HypergraphFactory.eINSTANCE.createModule
		module.name = type.determineFullyQualifiedName
		module.kind = kind
		val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
		derivedFrom.type = type
		module.derivedFrom = derivedFrom
		
		return module
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
		node.name = binding.determineFullyQualifiedName
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
		node.name = binding.determineFullyQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace
		derivedFrom.method = binding
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	/**
	 * Create a data edge for an reference of a super class property. Used with framework classes.
	 * 
	 * @param variableBinding is the variable declaration fragment containing the name
	 * 
	 * @return the edge
	 */
	def static createDataEdge(IVariableBinding variableBinding) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = variableBinding.determineFullyQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace
		derivedFrom.field = variableBinding
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Create a call edge between a method and an constructor invocation "this(...)".
	 * This can occur in constructors only.
	 * 
	 * @param callerType is the class containing the caller method
	 * @param callerMethod method where the call originates
	 * @param calleeType is the class containing the callee method
	 * @param callee the called method 
	 * 
	 * @return the edge
	 */
	def static createCallEdge(AbstractTypeDeclaration callerType, MethodDeclaration callerMethod, AbstractTypeDeclaration calleeType, ConstructorInvocation calleeMethod) {
		//System.out.println("createCallEdge atd,md,atd,ci" + callerType.determineFullyQualifiedName + ":" + callerMethod.name.fullyQualifiedName + " :: " + calleeType.determineFullyQualifiedName + ":this")
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = determineFullyQualifiedName(callerType, callerMethod) + "::" 
			+ determineFullyQualifiedName(calleeType, calleeMethod)
		val derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace
		derivedFrom.caller = callerMethod.resolveBinding
		derivedFrom.callee = calleeMethod.resolveConstructorBinding
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Create a call edge between a method and a super constructor invocation "super(...)".
	 * This can occur in constructors only.
	 * 
	 * @param callerType is the class containing the caller method
	 * @param callerMethod method where the call originates
	 * @param calleeType is the class containing the callee method
	 * @param calleeMethod the called method 
	 * 
	 * @return the edge
	 */
	def static createCallEdge(AbstractTypeDeclaration callerType, MethodDeclaration callerMethod, AbstractTypeDeclaration calleeType, SuperConstructorInvocation calleeMethod) {
		//System.out.println("createCallEdge atd,md,atd,sci" + callerType.determineFullyQualifiedName + ":" + callerMethod.name.fullyQualifiedName + " :: " + calleeType.determineFullyQualifiedName + ":super")
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
		//System.out.println("createCallEdge atd,md,mb" + callerType.determineFullyQualifiedName + ":" + callerMethod.name.fullyQualifiedName + " :: " + callee.determineFullyQualifiedName)
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = determineFullyQualifiedName(callerType, callerMethod) + "::" 
			+ determineFullyQualifiedName(callee)
		val derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace
		derivedFrom.caller = callerMethod.resolveBinding
		derivedFrom.callee = callee
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Create a call edge between two methods.
	 * 
	 * @param caller method where the call originates
	 * @param callee the called method in form of a method binding
	 * 
	 * @return the edge
	 */
	def static createCallEdge(IMethodBinding caller, IMethodBinding callee) {
		// System.out.println("createCallEdge mb mb " + caller.determineFullyQualifiedName + " :: " + callee.determineFullyQualifiedName)
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = caller.determineFullyQualifiedName + "::" + callee.determineFullyQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace
		derivedFrom.caller = caller
		derivedFrom.callee = callee
		edge.derivedFrom = derivedFrom
		
		return edge
	}

}