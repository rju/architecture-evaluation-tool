package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import org.eclipse.jdt.core.dom.MethodDeclaration
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import org.eclipse.jdt.core.dom.TypeDeclaration

import org.eclipse.jdt.core.IMethod
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import org.eclipse.jdt.core.IField
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.Expression
import java.util.List

import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.NameQualifiedType
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
import org.eclipse.jdt.core.ILocalVariable

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJDTDOMExtension.*
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge

class HypergraphJavaExtension {
	
	/**
	 * Find node for a given method specified by name clazz, name and argment list.
	 */
	def static findNodeForMethod(ModularHypergraph graph, TypeDeclaration clazz, String methodName, List<Expression> arguments) {
		val method = clazz.methods.filter[method | method.name.fullyQualifiedName.equals(methodName)].findFirst[method |
			compareArgAndParam(method.parameters, arguments)
		]
		graph.nodes.findFirst[it.isDerivedFromMethod(clazz, method)]
	}
	
	/**
	 * Find node for a given method declaration of a JDT DOM method in a given class.
	 */
	def static findNodeForMethod(ModularHypergraph graph, TypeDeclaration clazz, MethodDeclaration method) {
		graph.nodes.findFirst[it.isDerivedFromMethod(clazz, method)]
	}
	
	/**
	 * check if a given node is derived from a given method specified by clazz and method
	 */
	def static boolean isDerivedFromMethod(Node node, TypeDeclaration clazz, MethodDeclaration method) {
		if (node.derivedFrom instanceof MethodTrace) {
			return node.name.equals(clazz.determineFullQualifiedName(method))
		}
		return false
	}
	
	/**
	 * Find edge for a given variable in the given class.
	 */
	def static Edge findEdgeForVariable(ModularHypergraph hypergraph, TypeDeclaration clazz, String variable) {
		return hypergraph.edges.findFirst[edge | edge.name.equals(clazz.determineFullQualifiedName + "." + variable)]
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
	
		
	def static findConstructorMethod(ModularHypergraph graph, Type type, List arguments) {
		val typeName = switch(type) {
			SimpleType : type.name
			NameQualifiedType : type.name
			default: return null
		}
		 
		val module = graph.modules.findFirst[module | 
			((module.derivedFrom as TypeTrace).type as IType).elementName.equals(typeName.fullyQualifiedName)
		]
		if (module != null) { // is null iff the type is an external type. External types must be ignored.
			val moduleName = ((module.derivedFrom as TypeTrace).type as IType).elementName
			val nodes = module.nodes.filter[node | 
				switch (node.derivedFrom) {
					MethodTrace: ((node.derivedFrom as MethodTrace).method as IMethod).elementName.equals(moduleName)
					TypeTrace: true // only implicit constructors are not derived from anything directly
					default: false
				} 
			]
			return matchArguments(nodes, arguments)
		} else
			return null
	}

	def static matchArguments(Iterable<Node> nodes, List arguments) {
		nodes.findFirst[node |
			switch(node.derivedFrom) {
				TypeTrace: return (arguments.size == 0) // name already matched. Implicit constructor has no parameters => only check argument size.
				MethodTrace: return matchArgumentsForExplicitMethods(node.derivedFrom as MethodTrace, arguments)
				default: throw new Exception("Unsupported derivedFrom type used for node " + node.derivedFrom.class)
			}
		]
	}
	
	private def static matchArgumentsForExplicitMethods(MethodTrace trace, List arguments) {
		switch(trace.method) {
			 MethodDeclaration: {
			 	val parameters = (trace.method as MethodDeclaration).parameters
				if (parameters.size == arguments.size)
					compareArgAndParam(parameters, arguments)
				else
					false
			 }
			 IMethod: {
			 	val parameters = (trace.method as IMethod).parameters
			 	if (parameters.size == arguments.size)
			 		compareArgAndParamIMethod(parameters, arguments)
			 	else
			 		false
			 }
			 default:
			 	throw new Exception("Implementation error. Method type " + trace.method.class + " not supported.")
		}
	}
	
	/** returns true if the arguments match the parameters. */
	private def static boolean compareArgAndParamIMethod(List<ILocalVariable> parameters, List arguments) {
		// arguments are Expression
		// parameters ILocalVariable
		// TODO this is not a satisfying implementation
		for(var i=0;i < parameters.size ; i++) {
			val pType = parameters.get(i).typeSignature
			val aType = (arguments.get(i) as Expression).resolveTypeBinding.binaryName
			
			if (pType.equals(aType))
				return false	
		}
		
		return true
	}
	
	/** returns true if the arguments match the parameters. */
	def static boolean compareArgAndParam(List parameters, List arguments) {
		if (parameters.size != arguments.size)
			return false;
		// arguments are Expression
		// parameters SingleVariableDeclaration
		for(var i=0;i < parameters.size ; i++) {
			// TODO fix this
			/*val pType = (parameters.get(i) as SingleVariableDeclaration).type.resolveBinding
			val aType = (arguments.get(i) as Expression).resolveTypeBinding
			
			if (!pType.isAssignmentCompatible(aType))
				return false*/	
		}
		
		return true
	}
	
}