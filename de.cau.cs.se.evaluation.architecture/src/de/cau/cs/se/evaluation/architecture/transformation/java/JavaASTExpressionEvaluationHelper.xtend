package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import java.util.List
import org.eclipse.jdt.core.dom.ClassInstanceCreation
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.SuperFieldAccess
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.jdt.core.dom.ThisExpression

import static de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory.*

import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaASTExpressionEvaluation.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphQueryHelper.*
import org.eclipse.jdt.core.dom.QualifiedName

class JavaASTExpressionEvaluationHelper {
	
	/**
	 * Process an field access to a field of the super class. Generate a connection to a data edge.
	 * If the edge is missing, which implies this is a framework super class, then add the edge.
	 * 
	 * @param superFieldAccess the super field accessed in an expression
	 * @param sourceNode the node which must be connected to the edge
	 * @param graph the hypergraph
	 * @param dataTypePatterns string patterns to identify data types.
	 */
	def static processSuperFieldAccess(SuperFieldAccess superFieldAccess, Node sourceNode,
		ModularHypergraph graph, List<String> dataTypePatterns
	) {
		val fieldBinding = superFieldAccess.resolveFieldBinding
		if (fieldBinding != null) {
			if (fieldBinding.type.isDataType(dataTypePatterns)) {
				var edge = graph.edges.findDataEdge(fieldBinding)
				if (edge == null) {
					edge = createDataEdge(fieldBinding)
					graph.edges.add(edge)
				}
				sourceNode.edges.add(edge)
			}
		} else {
			// TODO this should not happen when the Java model is complete. Maybe issue a warning.
		}
	}
   	
   	   	    	      	    
    /**
     * Process a class instance creation.
     * - data type class = ignore
     * - framework or system class = connect
     * - anonymous class = create module and connect
     * 
     * @param callee the instance creation
     * @param sourceNode the node representing the caller
     * @param graph the hypergraph
     * @param dataTypePatterns list to identify data types
     */
    def static void processClassInstanceCreation(ClassInstanceCreation callee, Node sourceNode,
    	 ModularHypergraph graph, List<String> dataTypePatterns 
    ) {
    	val calleeTypeBinding = callee.resolveTypeBinding
    	if (!calleeTypeBinding.isDataType(dataTypePatterns)) {
    		/** create only for behavior classes. */
    		val calleeConstructorBinding = callee.resolveConstructorBinding
    	
    		/** check if the class is an anonymous class. */
    		if (calleeTypeBinding.anonymous) {
    			/** create a module for each new anonymous class and scan for methods. */
    			val module = createModuleForTypeBinding(calleeTypeBinding)
    			graph.modules.add(module)
    			callee.anonymousClassDeclaration.resolveBinding.declaredMethods.forEach[anonMethod |
    				val anonNode = createNodeForMethod(anonMethod)
    				module.nodes.add(anonNode)
    				graph.nodes.add(anonNode)
    			]
    		}
	    	
	    	val targetNode = graph.findOrCreateTargetNode(calleeTypeBinding, calleeConstructorBinding)
	    	
	    	/** create call edge as this is a local context. */
	    	handleCallEdgeInsertion(graph, sourceNode, targetNode)
    	
	    	/**
	    	 * node is the correct source node for evaluate, as all parameters are accessed inside of method and not the
	    	 * instantiated constructor.
	    	 */
	    	callee.arguments.forEach[argument | (argument as Expression).evaluate(sourceNode, graph, dataTypePatterns)]
	    }
    }
    
       
    /**
     * Process a method invocation in an expression. This might be a local method, 
     * an external method, or an data type method.
     * 
     * @param callee the called method
     * @param sourceNode the node representing the caller
     * @param graph the hypergraph
     * @param dataTypePatterns a list of patterns to identify data types
     */
    def static void processMethodInvocation(MethodInvocation callee, ModularHypergraph graph, 
    	List<String> dataTypePatterns, Node sourceNode
    ) {
    	val calleeTypeBinding = callee.resolveMethodBinding.declaringClass
    	/**
    	 * If the class of this method is a data type class then ignore it, as methods of data type classes are operations
    	 * like +, -, *, etc. and are not handled by the complexity measure.
    	 */
    	if (!calleeTypeBinding.isDataType(dataTypePatterns)) {
    		val calleeMethodBinding = callee.resolveMethodBinding
    		val targetNode = graph.findOrCreateTargetNode(calleeTypeBinding, calleeMethodBinding)
    		
	    	handleCallEdgeInsertion(graph, sourceNode, targetNode)
	    	
	    	/**
	    	 * Parameter node is the correct source node to evaluate the parameters, as all parameters
	    	 * are accessed inside of method and not the instantiated constructor.
	    	 */
	    	callee.arguments.forEach[argument | (argument as Expression).evaluate(sourceNode, graph, dataTypePatterns)]
    	}
    }
    
    /**
     * Process the invocation of a method of the super class as an call edge. If the target node is part of a framework, create the node
     * and place it in the graph.
     * 
     * @param invocation the callee
     * @param sourceNode the caller
     * @param graph the modular graph
     * @param dataTypePatterns a list of patterns to identify data types
     */
    def static processSuperMethodInvocation(SuperMethodInvocation callee, Node sourceNode,
    	ModularHypergraph graph, List<String> dataTypePatterns
    ) {
		val targetSuperMethodBinding = callee.resolveMethodBinding
		val targetNode = findOrCreateTargetNode(graph, targetSuperMethodBinding.declaringClass, targetSuperMethodBinding)
		handleCallEdgeInsertion(graph, sourceNode, targetNode)
	   	callee.arguments.forEach[
			(it as Expression).evaluate(sourceNode, graph, dataTypePatterns)
		]
	}
	
	
	/**
	 * Process a simple name which can be a variable or something else.
	 * - variable == data access -> data edge
	 * 
	 * @param name the simple name which
	 * @param sourceNode the node causing the access
	 * @param graph the graph containing the edge
	 */	
	def static processSimpleName(SimpleName name, Node sourceNode, ModularHypergraph graph)  {
		val nameBinding = name.resolveBinding
		switch(nameBinding) {
			IVariableBinding: {
				val edge = findDataEdge(graph.edges, nameBinding)
				if (edge != null) { /** found an data edge */
					sourceNode.edges.add(edge)
				}
			}
			default:
				throw new UnsupportedOperationException("Binding type " + nameBinding.class + " is not supported by processSimpleName")
		}
    }

	/**
	 * Process a qualified name which can be a variable or something else.
	 * - variable == data access -> data edge
	 * 
	 * @param name the simple name which
	 * @param sourceNode the node causing the access
	 * @param graph the graph containing the edge
	 */	
	def static processQualifiedName(QualifiedName name, Node sourceNode, ModularHypergraph graph)  {
		// TODO most likely the qualifier should be handled here
		val nameBinding = name.resolveBinding
		switch(nameBinding) {
			IVariableBinding: {
				val edge = findDataEdge(graph.edges, nameBinding)
				if (edge != null) { /** found an data edge */
					sourceNode.edges.add(edge)
				}
			}
			default:
				throw new UnsupportedOperationException("Binding type " + nameBinding.class + " is not supported by processSimpleName")
		}
    }

    /**
     * Create a data edge link form the source node to the data edge corresponding to the field.
     * 
     * @param field the accessed field
     * @param sourceNode the node accessing the edge
     * @param contextTypebinding the type binding of the context class
     * @param graph the modular hypergraph
     * @param dataTypePatterns a list of patterns to identify data types
     */
    def static processFieldAccess(FieldAccess fieldAccess, Node sourceNode, ITypeBinding contextTypeBinding, 
    	ModularHypergraph graph, List<String> dataTypePatterns
    ) {
    	val prefix = fieldAccess.expression
	    switch(prefix) {
	    	ThisExpression: { /** there must be an edge iff this is a data type property */
	    		if (fieldAccess.resolveTypeBinding.isDataType(dataTypePatterns)) {
		    		val edge = graph.edges.findDataEdge(fieldAccess.resolveFieldBinding)
		    		if (edge == null) {
		    			throw new UnsupportedOperationException("Missing edge for a data type property. Prefix " + 
		    				prefix.class + " field " + fieldAccess.resolveFieldBinding + " in processFieldAccess"
		    			)
		    		} else {
			    		sourceNode.edges.add(edge)
			    	}
		    	}
	    	}
	    	MethodInvocation: { 
	    	/** 
	    	 * this is not a field access, to a field of the context type. 
	    	 * It is a method invocation, and is handled elsewhere
	    	 */
	    	}
			// TODO other prefixes might contain method calls add evaluate here accordingly
	    	default:
	    		throw new UnsupportedOperationException("Prefix type not supported "+ prefix.class + " in processFieldAccess")
	    }    	
    }
       	
   	/**
   	 * Create a new edge between two nodes if there is no such connection already.
   	 * 
   	 * @param graph modular graph of the system
   	 * @param sourceNode source node
   	 * @param tagetNode target node
   	 */
   	private static def void handleCallEdgeInsertion(ModularHypergraph graph, Node sourceNode, Node targetNode) {
   		val sourceMethodBinding = sourceNode.methodBinding
   		val targetMethodBinding = targetNode.methodBinding
   		var edge = findCallEdge(graph.edges, sourceMethodBinding, targetMethodBinding)
	   	if (edge == null) {
	   		edge = createCallEdge(sourceMethodBinding, targetMethodBinding)
	   		graph.edges.add(edge)
		   	sourceNode.edges.add(edge)
		   	targetNode.edges.add(edge)
		   	// System.out.println("composeCallEdge " + sourceMethodBinding.name + "--" + node.name + " " + targetNode.name + " " + edge.name)
	   	}
   	}
   			
	/**
	 * Get the method binding of a node
	 */
	private static def IMethodBinding getMethodBinding(Node node) {
		(node.derivedFrom as MethodTrace).method as IMethodBinding
	}
    
	
}