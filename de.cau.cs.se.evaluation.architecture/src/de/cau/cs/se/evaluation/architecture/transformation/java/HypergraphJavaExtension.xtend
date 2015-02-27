package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import org.eclipse.jdt.core.dom.MethodDeclaration
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import org.eclipse.jdt.core.dom.TypeDeclaration

import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import org.eclipse.jdt.core.dom.Expression
import java.util.List

import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.NameQualifiedType
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import org.eclipse.jdt.core.dom.FieldDeclaration
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.EnumDeclaration

class HypergraphJavaExtension {
	
	/**
	 * Create a node for a given method.
	 */
	def static createNodeForMethod(MethodDeclaration method, AbstractTypeDeclaration type) {
		val node = HypergraphFactory.eINSTANCE.createNode
		node.name = type.determineFullQualifiedName(method)
		val derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace
		derivedFrom.method = method
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	/**
	 * Create a node for a given method.
	 */        
	def static createNodeForMethodName(String methodName, AbstractTypeDeclaration type) {
		val node = HypergraphFactory.eINSTANCE.createNode
		node.name = type.determineFullQualifiedName + "." + methodName
		node.derivedFrom = null
		
		return node
	}
	
	/**
	 * Create a node for a class' implicit constructor
	 */
	def static createNodeForImplicitConstructor(AbstractTypeDeclaration type) {
		val node = HypergraphFactory.eINSTANCE.createNode
		node.name = type.determineFullQualifiedName + "." + type.name.fullyQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
		derivedFrom.type = type
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	
	/**
	 * Create for one IField an edge.
	 */
	def static createEdgeForField(FieldDeclaration field, AbstractTypeDeclaration type) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		for (element : field.fragments) {
			val fragment = element as VariableDeclarationFragment
			edge.name = type.determineFullQualifiedName(fragment)
			val derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace
			derivedFrom.field = fragment
			edge.derivedFrom = derivedFrom
		}
		return edge
	}
	
	/**
	 * Create a module for each class in the list. Pointing to the class as derived from
	 * element. 
	 * 
	 * @return one new module
	 */
	def static createModuleForClass(AbstractTypeDeclaration type) {
		val module = HypergraphFactory.eINSTANCE.createModule
		module.name = type.determineFullQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
		derivedFrom.type = type
		module.derivedFrom = derivedFrom
		
		return module
	}
	
		/**
	 * Find node for a given method specified by name clazz, name and argment list.
	 */
	def static findNodeForMethod(ModularHypergraph graph, AbstractTypeDeclaration type, String methodName, List<Expression> arguments) {
		if (type instanceof TypeDeclaration) {
			val clazz = type as TypeDeclaration	
			val method = clazz.methods.filter[method | 
				method.name.fullyQualifiedName.equals(methodName)
			].findFirst[method |
				compareArgAndParam(method.parameters, arguments)
			]
			if (method != null)
				return graph.nodes.findFirst[it.isDerivedFromMethod(clazz, method)]
			else
				return null
		} else if (type instanceof  EnumDeclaration) {
			val enumerate = type as EnumDeclaration
			val method = enumerate.bodyDeclarations.filter(MethodDeclaration).filter[declaration |
				declaration.name.fullyQualifiedName.equals(methodName)
			].findFirst[declaration |
				compareArgAndParam(declaration.parameters, arguments)
			]
			if (method != null)
				return graph.nodes.findFirst[it.isDerivedFromMethod(enumerate, method)]
			else
				return null
		}
	}
	
	/**
	 * Find node for a given method declaration of a JDT DOM method in a given class.
	 */
	def static findNodeForMethod(ModularHypergraph graph, AbstractTypeDeclaration clazz, MethodDeclaration method) {
		graph.nodes.findFirst[it.isDerivedFromMethod(clazz, method)]
	}
	
	/**
	 * check if a given node is derived from a given method specified by clazz and method
	 */
	def static boolean isDerivedFromMethod(Node node, AbstractTypeDeclaration clazz, MethodDeclaration method) {
		if (node.derivedFrom instanceof MethodTrace) {
			return node.name.equals(clazz.determineFullQualifiedName(method))
		}
		return false
	}
	
	/**
	 * Find edge for a given variable in the given class.
	 */
	def static Edge findEdgeForVariable(ModularHypergraph hypergraph, AbstractTypeDeclaration clazz, String variable) {
		return hypergraph.edges.findFirst[edge | edge.name.equals(clazz.determineFullQualifiedName + "." + variable)]
	}
		
	/**
	 * Find the constructor node matching the type and argument list.
	 */
	def static findConstructorMethod(ModularHypergraph graph, Type type, List<?> arguments) {
		val typeName = switch(type) {
			SimpleType : type.name
			NameQualifiedType : type.name
			default: return null
		}
		 
		val module = graph.modules.findFirst[module | 
			((module.derivedFrom as TypeTrace).type as AbstractTypeDeclaration).determineFullQualifiedName.equals(typeName.fullyQualifiedName)
		]
		if (module != null) { // is null iff the type is an external type. External types must be ignored.
			val constructorName = ((module.derivedFrom as TypeTrace).type as TypeDeclaration).name.fullyQualifiedName
			val nodes = module.nodes.filter[node | 
				switch (node.derivedFrom) {
					MethodTrace: node.name.equals(module.name + "." + constructorName)
					TypeTrace: true // only implicit constructors are not derived from a method
					default: false
				} 
			]
			return matchArguments(nodes, arguments)
		} else
			return null
	}

	private def static matchArguments(Iterable<Node> nodes, List<?> arguments) {
		nodes.findFirst[node |
			switch(node.derivedFrom) {
				TypeTrace: return (arguments.size == 0) // name already matched. Implicit constructor has no parameters => only check argument size.
				MethodTrace: return matchArgumentsForExplicitMethods(node.derivedFrom as MethodTrace, arguments)
				default: throw new Exception("Unsupported derivedFrom type used for node " + node.derivedFrom.class)
			}
		]
	}
	
	private def static matchArgumentsForExplicitMethods(MethodTrace trace, List<?> arguments) {
		if (trace.method instanceof MethodDeclaration) {
			val parameters = (trace.method as MethodDeclaration).parameters
			if (parameters.size == arguments.size)
				compareArgAndParam(parameters, arguments)
			else
				false
		} else {
			 throw new Exception("Implementation error. Method type " + trace.method.class + " not supported.")
		}
	}
		
	/** returns true if the arguments match the parameters. */
	def static boolean compareArgAndParam(List<?> parameters, List<?> arguments) {
		if (parameters.size != arguments.size)
			return false;
		// arguments are Expression
		// parameters SingleVariableDeclaration
		for(var i=0;i < parameters.size ; i++) {
			// TODO fix this
			val pType = (parameters.get(i) as SingleVariableDeclaration).type.resolveBinding
			val aType = (arguments.get(i) as Expression).resolveTypeBinding
			
			if (!pType.isAssignmentCompatible(aType))
				return false	
		}
		
		return true
	}
	
}