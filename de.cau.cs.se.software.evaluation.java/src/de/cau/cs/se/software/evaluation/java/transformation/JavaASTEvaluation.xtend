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

import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.hypergraph.TypeTrace
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind

import java.util.List
import org.eclipse.jdt.core.dom.AssertStatement
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.BreakStatement
import org.eclipse.jdt.core.dom.CatchClause
import org.eclipse.jdt.core.dom.ConstructorInvocation
import org.eclipse.jdt.core.dom.DoStatement
import org.eclipse.jdt.core.dom.EmptyStatement
import org.eclipse.jdt.core.dom.EnhancedForStatement
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.LabeledStatement
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.SuperConstructorInvocation
import org.eclipse.jdt.core.dom.SwitchCase
import org.eclipse.jdt.core.dom.SwitchStatement
import org.eclipse.jdt.core.dom.SynchronizedStatement
import org.eclipse.jdt.core.dom.ThrowStatement
import org.eclipse.jdt.core.dom.TryStatement
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.WhileStatement
import org.eclipse.jdt.core.dom.ContinueStatement

import static de.cau.cs.se.software.evaluation.java.transformation.JavaHypergraphElementFactory.*

import static extension de.cau.cs.se.software.evaluation.java.transformation.NameResolutionHelper.*
import static extension de.cau.cs.se.software.evaluation.java.transformation.JavaASTExpressionEvaluation.*
import static extension de.cau.cs.se.software.evaluation.java.transformation.JavaHypergraphQueryHelper.*
import de.cau.cs.se.software.evaluation.jobs.IOutputHandler

class JavaASTEvaluation {
	
	/**
	 * Scan the AST of a method body for property access and method calls.
	 * 
	 * @param graph the hypergraph holding node, edges, and modules
	 * @param dataTypePatterns list of pattern strings for classes which must be handled as data types
	 * @param node the corresponding node to the method
	 * @param clazz declaring class of the given method
	 * @param method the method to be evaluated for property access and method calls
	 */
	static def void evaluteMethod(ModularHypergraph graph, List<String> dataTypePatterns, Node sourceNode, TypeDeclaration clazz, MethodDeclaration method, IOutputHandler handler) {
		// System.out.println("evaluateMethod <" + sourceNode.name + "> " + clazz.name.fullyQualifiedName + " - " + method.name.fullyQualifiedName)
		if (!clazz.interface && !Modifier.isAbstract(method.getModifiers())) {
			if (method.body.statements !== null) {
				method.body.statements.forEach[statement | (statement as Statement).evaluateStatement(graph, dataTypePatterns, sourceNode, handler)]
			}
		}
	}

	/**
	 * Evaluate a single statement.
	 * 
	 * @param statement the statement to be evaluated.
	 * @param graph the modular hypergraph
	 * @param dataTypePatterns pattern strings identifying data types
	 * @param node the context node (node of the method)
	 * @param clazz the context class
	 * @param method the context method
	 */
	static def void evaluateStatement(Statement statement, ModularHypergraph graph, List<String> dataTypePatterns, Node sourceNode, IOutputHandler handler) {
		// System.out.println("statement evaluate <" + node.name + "> " + clazz.name.fullyQualifiedName + " - " + method.name.fullyQualifiedName)
		switch (statement) {
			AssertStatement: statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		Block: statement.statements.forEach[(it as Statement).evaluateStatement(graph, dataTypePatterns, sourceNode, handler)]
    		ConstructorInvocation: handleConstructorInvocation(statement, graph, dataTypePatterns, sourceNode, handler)
    		DoStatement: {
    			statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			statement.body.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    		}
    		EnhancedForStatement: {
    			/* we ignore local value declarations here (see paper) */
    			statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			statement.body.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    		}
    		ExpressionStatement: statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		ForStatement: {
    			statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			statement.initializers.forEach[(it as Expression).evaluate(sourceNode, graph, dataTypePatterns, handler)]
    			statement.updaters.forEach[(it as Expression).evaluate(sourceNode, graph, dataTypePatterns, handler)]
    			statement.body.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    		}
    		IfStatement: {
    			statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			statement.thenStatement.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    			statement.elseStatement?.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    		}
    		LabeledStatement: statement.body.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    		ReturnStatement: statement.expression?.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		SuperConstructorInvocation: handleSuperConstructorInvocation(statement, graph, sourceNode, handler)
    		SwitchCase: statement.expression?.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		SwitchStatement: {
    			statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			statement.statements.forEach[(it as Statement).evaluateStatement(graph, dataTypePatterns, sourceNode, handler)]
    		}
    		SynchronizedStatement: {
    			statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			statement.body.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    		}
    		ThrowStatement: statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		TryStatement: {
    			statement.body.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    			statement.catchClauses.forEach[
    				(it as CatchClause).body.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    			]
    			statement.^finally?.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    		}
    		// TODO TypeDeclarationStatement: for now this must be ignored. However, I am not totally sure if the could 
    		// not happen inside a method anyway, as the whole AST is a little weird
    		VariableDeclarationStatement: statement.fragments.forEach[(it as VariableDeclarationFragment).
    			initializer?.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		]
    		WhileStatement: {
    			statement.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			statement.body.evaluateStatement(graph, dataTypePatterns, sourceNode, handler)
    		}
			BreakStatement, ContinueStatement, EmptyStatement: return
    		default:
    			throw new UnsupportedOperationException("Expressions of type " + statement.class + " are not supported.")
		}
	}
	
	
		
	/**
	 * Handle an constructor call to the super class. This could be a method which is part of the framework.
	 */
	def static handleSuperConstructorInvocation(SuperConstructorInvocation invocation, ModularHypergraph graph, Node sourceNode, IOutputHandler handler) {
		/** check if a modules exists for the super constructor class and a node exists for the super constructor. */
		val sourceMethodBinding = (sourceNode.derivedFrom as MethodTrace).method as IMethodBinding
		val targetMethodBinding = invocation.resolveConstructorBinding
		var module = graph.modules.findFirst[
			val type = (it.derivedFrom as TypeTrace).type
			switch(type) {
				TypeDeclaration: type.resolveBinding.isSubTypeCompatible(targetMethodBinding.declaringClass)
				ITypeBinding: type.isSubTypeCompatible(targetMethodBinding.declaringClass)
				default: throw new UnsupportedOperationException(type.class + " is not supported as a source for module.")
			}
		]
		if (module === null) {
			module = createModuleForTypeBinding(invocation.resolveConstructorBinding.declaringClass, EModuleKind.FRAMEWORK)
			graph.modules.add(module)
		}
		
		/** create edge between this constructor and the invoked constructor. */
		var targetNode = module.nodes.findFirst[
			if (it.derivedFrom instanceof MethodTrace) {
				val localMethod = (it.derivedFrom as MethodTrace).method
				switch(localMethod) {
					MethodDeclaration: localMethod.resolveBinding.isSubsignature(targetMethodBinding)
					IMethodBinding: localMethod.isSubsignature(targetMethodBinding)
					default: false  	
				}
			} else if (it.derivedFrom instanceof TypeTrace) {
				// TODO find out if TypeTrace references are correct and produce the correct response here
				false
			} else {
				new UnsupportedOperationException("Internal error: Node " + it.name + " is not derived from a method " + it.derivedFrom)
				false
			}
		]
		if (targetNode === null) {
			targetNode = createNodeForSuperConstructorInvocation(targetMethodBinding)
			module.nodes.add(targetNode)
			graph.nodes.add(targetNode)
		}

		val edge = createCallEdge(sourceMethodBinding, targetMethodBinding)
		graph.edges.add(edge)
		sourceNode.edges.add(edge)
		targetNode.edges.add(edge)
		// System.out.println("handleSuperConstructorInvocation <" + node.name + "> <" + targetNode.name + "> [" + edge.name + "]")
	}
	
	
 	/**
 	 * Handle an constructor 'this' invocation. This requires (a) an call edge from
 	 * this method to the called constructor and (b) an evaluation of all parameters.
 	 */
	private def static handleConstructorInvocation(ConstructorInvocation invocation, ModularHypergraph graph, List<String> dataTypePatterns, Node sourceNode, IOutputHandler handler) {
		val sourceMethodBinding = (sourceNode.derivedFrom as MethodTrace).method as IMethodBinding
		val targetMethodBinding = invocation.resolveConstructorBinding
		val edge = createCallEdge(sourceMethodBinding, targetMethodBinding)
		if (!graph.edges.exists[it.name.equals(edge.name)]) {
			var targetNode = graph.nodes.findNodeForConstructorBinding(targetMethodBinding)
			if (targetNode === null) {
				throw new UnsupportedOperationException("Internal error: Missing target node for method " + targetMethodBinding.determineFullyQualifiedName)
			} else {
				graph.edges.add(edge)
				targetNode.edges.add(edge)
				sourceNode.edges.add(edge)
				// System.out.println("handleConstructorInvocation <" + node.name + "> <" + targetNode.name + "> [" + edge.name + "]")
			}
		}
		invocation.arguments.forEach[(it as Expression).evaluate(sourceNode, graph, dataTypePatterns, handler)]
	}
		
}