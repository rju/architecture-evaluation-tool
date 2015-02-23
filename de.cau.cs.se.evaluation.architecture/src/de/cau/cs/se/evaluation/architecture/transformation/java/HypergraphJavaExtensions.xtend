package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import org.eclipse.jdt.core.dom.MethodDeclaration
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import org.eclipse.jdt.core.dom.TypeDeclaration

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import org.eclipse.jdt.core.IMethod
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import org.eclipse.jdt.core.IField
import org.eclipse.jdt.core.IType

class HypergraphJavaExtensions {
	
	def static findNodeForMethod(ModularHypergraph graph, TypeDeclaration clazz, MethodDeclaration method) {
		graph.nodes.findFirst[it.isDerivedFromMethod(clazz, method)]
	}
	
	def static boolean isDerivedFromMethod(Node node, TypeDeclaration clazz, MethodDeclaration declaration) {
		if (node.derivedFrom instanceof MethodTrace) {
			return node.name.equals(clazz.determineFullQualifiedName(declaration))
		}
		return false
	}
	
	/**
	 * Create a node for a given method.
	 */
	def static createNodeForMethod(IMethod method) {
		val node = HypergraphFactory.eINSTANCE.createNode
		// TODO redo this based on name qualification resolving
		node.name = method.compilationUnit.parent.elementName + "." + method.parent.elementName + "." + method.elementName
		val derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace
		derivedFrom.method = method
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	/**
	 * Create for one IField an edge.
	 */
	def static createEdgeForField(IField field) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = field.compilationUnit.parent.elementName + "." + field.parent.elementName + "." + field.elementName
		val derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace
		derivedFrom.field = field
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Create a module for each class in the list. Pointing to the class as derived from
	 * element. 
	 * 
	 * @return one new module
	 */
	def static createModuleForClass(IType type) {
		val module = HypergraphFactory.eINSTANCE.createModule
		module.name = type.fullyQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
		derivedFrom.type = type
		module.derivedFrom = derivedFrom
		
		return module
	}
	
}