package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
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

import static de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory.*

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaASTExpressionEvaluation.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphQueryHelper.*

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
	public static def void evaluteMethod(ModularHypergraph graph, List<String> dataTypePatterns, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		// System.out.println("evaluateMethod <" + node.name + "> " + clazz.name.fullyQualifiedName + " - " + method.name.fullyQualifiedName)
		if (!clazz.interface && !Modifier.isAbstract(method.getModifiers())) {
			if (method.body.statements != null) {
				method.body.statements.forEach[statement | (statement as Statement).evaluateStatement(graph, dataTypePatterns, node, clazz, method)]
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
	private static def void evaluateStatement(Statement statement, ModularHypergraph graph, List<String> dataTypePatterns, 
		Node node, TypeDeclaration clazz, MethodDeclaration method
	) {
		// System.out.println("statement evaluate <" + node.name + "> " + clazz.name.fullyQualifiedName + " - " + method.name.fullyQualifiedName)
		switch (statement) {
			AssertStatement: statement.expression.evaluate(node, graph, dataTypePatterns)
    		Block: statement.statements.forEach[(it as Statement).evaluateStatement(graph, dataTypePatterns, node, clazz, method)]
    		ConstructorInvocation: handleConstructorInvocation(statement, graph, dataTypePatterns, node, clazz, method)
    		DoStatement: {
    			statement.expression.evaluate(node, graph, dataTypePatterns)
    			statement.body.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    		}
    		EnhancedForStatement: {
    			/* we ignore local value declarations here (see paper) */
    			statement.expression.evaluate(node, graph, dataTypePatterns)
    			statement.body.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    		}
    		ExpressionStatement: statement.expression.evaluate(node, graph, dataTypePatterns)
    		ForStatement: {
    			statement.expression.evaluate(node, graph, dataTypePatterns)
    			statement.initializers.forEach[(it as Expression).evaluate(node, graph, dataTypePatterns)]
    			statement.updaters.forEach[(it as Expression).evaluate(node, graph, dataTypePatterns)]
    			statement.body.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    		}
    		IfStatement: {
    			statement.expression.evaluate(node, graph, dataTypePatterns)
    			statement.thenStatement.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    			statement.elseStatement?.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    		}
    		LabeledStatement: statement.body.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    		ReturnStatement: statement.expression?.evaluate(node, graph, dataTypePatterns)
    		SuperConstructorInvocation: handleSuperConstructorInvocation(statement, graph, node, clazz, method)
    		SwitchCase: statement.expression?.evaluate(node, graph, dataTypePatterns)
    		SwitchStatement: {
    			statement.expression.evaluate(node, graph, dataTypePatterns)
    			statement.statements.forEach[(it as Statement).evaluateStatement(graph, dataTypePatterns, node, clazz, method)]
    		}
    		SynchronizedStatement: {
    			statement.expression.evaluate(node, graph, dataTypePatterns)
    			statement.body.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    		}
    		ThrowStatement: statement.expression.evaluate(node, graph, dataTypePatterns)
    		TryStatement: {
    			statement.body.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    			statement.catchClauses.forEach[
    				(it as CatchClause).body.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    			]
    			statement.^finally?.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    		}
    		// TODO TypeDeclarationStatement: for now this must be ignored. However, I am not totally sure if the could 
    		// not happen inside a method anyway, as the whole AST is a little weird
    		VariableDeclarationStatement: statement.fragments.forEach[(it as VariableDeclarationFragment).
    			initializer?.evaluate(node, graph, dataTypePatterns)
    		]
    		WhileStatement: {
    			statement.expression.evaluate(node, graph, dataTypePatterns)
    			statement.body.evaluateStatement(graph, dataTypePatterns, node, clazz, method)
    		}
			BreakStatement, EmptyStatement: return
    		default:
    			throw new UnsupportedOperationException("Expressions of type " + statement.class + " are not supported.")
		}
	}
	
	
		
	/**
	 * Handle an constructor call to the super class. This could be a method which is part of the framework.
	 */
	def static handleSuperConstructorInvocation(SuperConstructorInvocation invocation, ModularHypergraph graph, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		/** check if a modules exists for the super constructor class and a node exists for the super constructor. */
		val methodBinding = invocation.resolveConstructorBinding
		var module = graph.modules.findFirst[
			val type = (it.derivedFrom as TypeTrace).type
			switch(type) {
				TypeDeclaration: type.resolveBinding.isSubTypeCompatible(methodBinding.declaringClass)
				ITypeBinding: type.isSubTypeCompatible(methodBinding.declaringClass)
				default: throw new UnsupportedOperationException(type.class + " is not supported as a source for module.")
			}
		]
		if (module == null) {
			module = createModuleForTypeBinding(invocation.resolveConstructorBinding.declaringClass)
			graph.modules.add(module)
		}
		
		/** create edge between this constructor and the invoked constructor. */
		var targetNode = module.nodes.findFirst[
			val localMethod = (it.derivedFrom as MethodTrace).method
			switch(localMethod) {
				MethodDeclaration: localMethod.resolveBinding.isSubsignature(methodBinding)
				IMethodBinding: localMethod.isSubsignature(methodBinding)
				default: false  	
			}
		]
		if (targetNode == null) {
			targetNode = createNodeForSuperConstructorInvocation(methodBinding)
			module.nodes.add(targetNode)
			graph.nodes.add(targetNode)
		}

		val edge = createCallEdge(clazz, method, methodBinding)
		graph.edges.add(edge)
		node.edges.add(edge)
		targetNode.edges.add(edge)
		// System.out.println("handleSuperConstructorInvocation <" + node.name + "> <" + targetNode.name + "> [" + edge.name + "]")
	}
	
	
 	/**
 	 * Handle an constructor 'this' invocation. This requires (a) an call edge from
 	 * this method to the called constructor and (b) an evaluation of all parameters.
 	 */
	private def static handleConstructorInvocation(ConstructorInvocation invocation, ModularHypergraph graph, List<String> dataTypePatterns, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		val sourceBinding = method.resolveBinding
		val targetBinding = invocation.resolveConstructorBinding
		val edge = createCallEdge(sourceBinding, targetBinding)
		if (!graph.edges.exists[it.name.equals(edge.name)]) {
			var targetNode = graph.nodes.findNodeForConstructorBinding(targetBinding)
			if (targetNode == null) {
				System.out.println("Missing source node: This is an error!! " + targetBinding.determineFullyQualifiedName)
			} else {
				graph.edges.add(edge)
				targetNode.edges.add(edge)
				node.edges.add(edge)
				// System.out.println("handleConstructorInvocation <" + node.name + "> <" + targetNode.name + "> [" + edge.name + "]")
			}
		}
		invocation.arguments.forEach[(it as Expression).evaluate(node, graph, dataTypePatterns)]
	}
	
}