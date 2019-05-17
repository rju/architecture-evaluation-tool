/***************************************************************************
 * Copyright (C) 2016 Reiner Jung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package de.cau.cs.se.software.evaluation.java.transformation

import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind
import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.views.LogModelProvider
import java.util.List
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.ClassInstanceCreation
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.LambdaExpression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.SuperFieldAccess
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.jdt.core.dom.ThisExpression

import static de.cau.cs.se.software.evaluation.java.transformation.JavaHypergraphElementFactory.*

import static extension de.cau.cs.se.software.evaluation.java.transformation.JavaASTEvaluation.*
import static extension de.cau.cs.se.software.evaluation.java.transformation.JavaASTExpressionEvaluation.*
import static extension de.cau.cs.se.software.evaluation.java.transformation.JavaHypergraphQueryHelper.*
import org.eclipse.jdt.core.dom.ExpressionMethodReference
import de.cau.cs.se.software.evaluation.jobs.IOutputHandler

class JavaASTExpressionEvaluationHelper {
	
	/**
	 * Process an field access to a field of the super class. Generate a connection to a data edge.
	 * If the edge is missing, which implies this is a framework super class, then add the edge.
	 * 
	 * @param superFieldAccess the super field accessed in an expression
	 * @param sourceNode the node which must be connected to the edge
	 * @param graph the hypergraph
	 * @param dataTypePatterns string patterns to identify data types
	 * @param handler error and reporting handler
	 */
	def static processSuperFieldAccess(SuperFieldAccess superFieldAccess, Node sourceNode,
		ModularHypergraph graph, List<String> dataTypePatterns, IOutputHandler handler
	) {
		val fieldBinding = superFieldAccess.resolveFieldBinding
		if (fieldBinding !== null) {
			if (fieldBinding.type.isDataType(dataTypePatterns)) {
				var edge = graph.edges.findDataEdge(fieldBinding)
				if (edge === null) {
					edge = createDataEdge(fieldBinding)
					graph.edges.add(edge)
				}
				sourceNode.edges.add(edge)
			}
		} else {
			handler.error("Java model incomplete",String.format("Field binding for %s of node %s could not be resolved.", superFieldAccess.name, sourceNode.name))
			throw new UnsupportedOperationException("Field binding could not be resolved. Java model incomplete.")
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
	 * @param handler error and reporting handler    
     */
    def static void processClassInstanceCreation(ClassInstanceCreation callee, Node sourceNode,
    	 ModularHypergraph graph, List<String> dataTypePatterns, IOutputHandler handler 
    ) {
    	val calleeTypeBinding = callee.resolveTypeBinding
    	if (!calleeTypeBinding.isDataType(dataTypePatterns)) { /** create only for behavior classes. */
    		val calleeConstructorBinding = callee.resolveConstructorBinding
    		// TODO why can this binding be null?
    		if (calleeConstructorBinding !== null) {
	    		/** check if the class is an anonymous class. */
	    		if (calleeTypeBinding.anonymous) {
	    			/** create a module for each new anonymous class and scan for methods. */
	    			val module = createModuleForTypeBinding(calleeTypeBinding, EModuleKind.ANONYMOUS)
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
    		} else {
    			handler.error("Binding Error", String.format("Callee constructor binding cannot be resolved in %s for node %s", callee, sourceNode.name))
    		}
	    	/**
	    	 * node is the correct source node for evaluate, as all parameters are accessed inside of method and not the
	    	 * instantiated constructor.
	    	 */
	    	callee.arguments.forEach[argument | (argument as Expression).evaluate(sourceNode, graph, dataTypePatterns, handler)]
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
     * @param handler handler for reporting and error output
     */
    def static void processMethodInvocation(MethodInvocation callee, Node sourceNode, 
    	ModularHypergraph graph, List<String> dataTypePatterns, IOutputHandler handler 
    ) {
    	val calleeMethodBinding = callee.resolveMethodBinding
    	if (calleeMethodBinding !== null) {
	    	val calleeTypeBinding = calleeMethodBinding.declaringClass
	    	/**
	    	 * If the class of this method is a data type class then ignore it, as methods of data type classes are operations
	    	 * like +, -, *, etc. and are not handled by the complexity measure.
	    	 */
	    	if (!calleeTypeBinding.isDataType(dataTypePatterns)) {
	    		val targetNode = graph.findOrCreateTargetNode(calleeTypeBinding, calleeMethodBinding)
	    		
		    	handleCallEdgeInsertion(graph, sourceNode, targetNode)
		    	
		    	/**
		    	 * Parameter node is the correct source node to evaluate the parameters, as all parameters
		    	 * are accessed inside of method and not the instantiated constructor.
		    	 */
		    	callee.arguments.forEach[argument | (argument as Expression).evaluate(sourceNode, graph, dataTypePatterns, handler)]
	    	}
	    } else {
	    	handler.error("Internal error", String.format("The method binding could not be resolved for %s called %s", callee.toString, sourceNode.name))
	    }
    }
    
    /**
     * Process the invocation of a method of the super class as an call edge. If the target node is part of a framework, create the node
     * and place it in the graph.
     * 
     * @param lambda the lambda expression
     * @param sourceNode the context node
     * @param graph the modular graph
     * @param dataTypePatterns a list of patterns to identify data types
     */
    def static void processLambdaExpression(LambdaExpression lambda, Node sourceNode, ModularHypergraph graph, List<String> dataTypePatterns, IOutputHandler handler) {
    	val body = lambda.body
    	switch (body) {
    		Block: body.statements.forEach[(it as Statement).evaluateStatement(graph, dataTypePatterns, sourceNode, handler)]
    		Expression: body.evaluate(sourceNode, graph, dataTypePatterns, handler)
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
    def static void processSuperMethodInvocation(SuperMethodInvocation callee, Node sourceNode,
    	ModularHypergraph graph, List<String> dataTypePatterns, IOutputHandler handler
    ) {
		val targetSuperMethodBinding = callee.resolveMethodBinding
		if (targetSuperMethodBinding !== null) {
			val targetNode = findOrCreateTargetNode(graph, targetSuperMethodBinding.declaringClass, targetSuperMethodBinding)
			handleCallEdgeInsertion(graph, sourceNode, targetNode)
		   	callee.arguments.forEach[
				(it as Expression).evaluate(sourceNode, graph, dataTypePatterns, handler)
			]
		} else {
			throw new UnsupportedOperationException("Internal error: The method binding for a super call could not be resolved for " + callee.toString)
		}
	}
	
	
	/**
	 * Process a simple name which can be a variable or something else.
	 * - variable == data access -> data edge
	 * 
	 * @param name the simple name which
	 * @param sourceNode the node causing the access
	 * @param graph the graph containing the edge
	 */	
	def static void processSimpleName(SimpleName name, Node sourceNode, ModularHypergraph graph, IOutputHandler handler)  {
		val nameBinding = name.resolveBinding
		switch(nameBinding) {
			IVariableBinding: { /** this referes to a variable => data access edge. */
				val edge = findDataEdge(graph.edges, nameBinding)
				if (edge !== null) { /** found an data edge */
					sourceNode.edges.add(edge)
				}
			}
			case null: {
				handler.error("Resolving Failed", String.format("Tried to resolve a binding for %s in processSimpleName", name.toString))
			}
			default: {
				handler.error("Missing Functionality", String.format("Binding type %s is not supported by processSimpleName", nameBinding.class))
				throw new UnsupportedOperationException("Binding type " + nameBinding.class + " is not supported by processSimpleName")
			}
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
	def static void processQualifiedName(QualifiedName name, Node sourceNode, ModularHypergraph graph, IOutputHandler handler)  {
		// TODO most likely the qualifier should be handled here
		val nameBinding = name.resolveBinding
		switch(nameBinding) {
			IVariableBinding: {
				val edge = findDataEdge(graph.edges, nameBinding)
				if (edge !== null) { /** found an data edge */
					sourceNode.edges.add(edge)
				}
			}
			case null: {
				handler.error("Resolving Failed", String.format("Tried to resolve a binding for %s in processQualifiedName", name.toString))
			}
			default: {
				handler.error("Missing Functionality", String.format("Binding type %s is not supported by processQualifiedName", nameBinding.class))
				throw new UnsupportedOperationException("Binding type " 
					+ nameBinding.class + " is not supported by processQualifiedName"
				)
			}
		}
    }
    
    
    /**
     * Create a call edge link form the source node to the target method node.
     * 
     * @param field the accessed field
     * @param sourceNode the node accessing the edge
     * @param contextTypebinding the type binding of the context class
     * @param graph the modular hypergraph
     * @param dataTypePatterns a list of patterns to identify data types
     */
    def static processExpressionMethodReference(ExpressionMethodReference expressionMethodReference, Node sourceNode, ITypeBinding contextTypeBinding, 
    	ModularHypergraph graph, List<String> dataTypePatterns
    ) {
    	val methodBinding = expressionMethodReference.name.resolveBinding
    	
    	if (methodBinding instanceof IMethodBinding) {
    		val edge = createCallEdge(sourceNode.methodBinding, methodBinding)
    		sourceNode.edges.add(edge)
    		graph.edges.add(edge)
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
    	ModularHypergraph graph, List<String> dataTypePatterns, IOutputHandler handler
    ) {
    	val prefix = fieldAccess.expression
	    switch(prefix) {
	    	ThisExpression: { /** there must be an edge iff this is a data type property */
	    		if (fieldAccess.resolveTypeBinding.isDataType(dataTypePatterns)) {
		    		val edge = graph.edges.findDataEdge(fieldAccess.resolveFieldBinding.variableDeclaration)
		    		if (edge === null) {
		    			// TODO should use handler error message feature
		    			LogModelProvider.INSTANCE.addMessage("Error", 
		    				"Resolving failed in this expression. Missing edge for a data type property. "
		    				 + fieldAccess.resolveFieldBinding)
		    			//throw new UnsupportedOperationException("Missing edge for a data type property. " + 
		    			//" Prefix " + prefix.class + " field " + fieldAccess.resolveFieldBinding + 
		    			//" in " + JavaASTExpressionEvaluationHelper.name + ".processFieldAccess"
		    			//)
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
	    	FieldAccess: {
	    		if (fieldAccess.resolveTypeBinding.isDataType(dataTypePatterns)) {
		    		val edge = graph.edges.findDataEdge(fieldAccess.resolveFieldBinding)
		    		if (edge === null) {
		    			// TODO how can this happen?
		    			// TODO this should be part of the handler error logger
		    			LogModelProvider.INSTANCE.addMessage("Error", 
		    				"Resolving failed in field access expression. Missing edge for a data type property. "
		    				+ fieldAccess.resolveFieldBinding)
		    			//throw new UnsupportedOperationException("Missing edge for a data type property. " + 
		    			//" Prefix " + prefix.class + " field " + fieldAccess.resolveFieldBinding + 
		    			//" in " + JavaASTExpressionEvaluationHelper.name + ".processFieldAccess"
		    			//)
		    		} else {
			    		sourceNode.edges.add(edge)
			    	}
		    	}
	    	}
			// TODO other prefixes might contain method calls add evaluate here accordingly
	    	default: {
	    		handler.error("Missing Functionality", String.format("Prefix type %s not supported %s.processFieldAccess", prefix.class.name, JavaASTExpressionEvaluationHelper.name))
	    		throw new UnsupportedOperationException("Prefix type " + prefix.class.name
	    			+ " not supported " + JavaASTExpressionEvaluationHelper.name + ".processFieldAccess"
	    		)
	    	}
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
	   	if (edge === null) {
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