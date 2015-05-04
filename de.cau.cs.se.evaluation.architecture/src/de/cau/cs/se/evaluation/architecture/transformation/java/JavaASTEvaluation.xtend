package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.jdt.core.dom.Block
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.AssertStatement
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ConstructorInvocation

import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory.*
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.DoStatement
import org.eclipse.jdt.core.dom.EnhancedForStatement
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.LabeledStatement
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.jdt.core.dom.SuperConstructorInvocation
import org.eclipse.jdt.core.dom.SwitchCase
import org.eclipse.jdt.core.dom.SwitchStatement
import org.eclipse.jdt.core.dom.SynchronizedStatement
import org.eclipse.jdt.core.dom.ThrowStatement
import org.eclipse.jdt.core.dom.TryStatement
import org.eclipse.jdt.core.dom.CatchClause
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.WhileStatement
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IMethodBinding

class JavaASTEvaluation {
	
	/**
	 * Scan the AST of a method body for property access and method calls.
	 * 
	 * @param graph the hypergraph holding node, edges, and modules
	 * @param node the corresponding node to the method
	 * @param clazz declaring class of the given method
	 * @param method the method to be evaluated for property access and method calls
	 */
	public static def void evaluteMethod(ModularHypergraph graph, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		if (method.body.statements != null) {
			method.body.statements.forEach[statement | (statement as Statement).evaluate(graph, node, clazz, method)]
		}
	} 
	
	private static def void evaluate(Statement statement, ModularHypergraph graph, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		switch (statement) {
			AssertStatement: statement.expression.evaluate(graph, node, clazz, method)
    		Block: statement.statements.forEach[(it as Statement).evaluate(graph, node, clazz, method)]
    		ConstructorInvocation: handleConstructorInvocation(statement, graph, node, clazz, method)
    		DoStatement: {
    			statement.expression.evaluate(graph, node, clazz, method)
    			statement.body.evaluate(graph, node, clazz, method)
    		}
    		EnhancedForStatement: {
    			/* we ignore local value declarations here (see paper) */
    			statement.expression.evaluate(graph, node, clazz, method)
    			statement.body.evaluate(graph, node, clazz, method)
    		}
    		ExpressionStatement: statement.expression.evaluate(graph, node, clazz, method)
    		ForStatement: {
    			statement.expression.evaluate(graph, node, clazz, method)
    			statement.initializers.forEach[(it as Expression).evaluate(graph, node, clazz, method)]
    			statement.updaters.forEach[(it as Expression).evaluate(graph, node, clazz, method)]
    			statement.body.evaluate(graph, node, clazz, method)
    		}
    		IfStatement: {
    			statement.expression.evaluate(graph, node, clazz, method)
    			statement.thenStatement.evaluate(graph, node, clazz, method)
    			statement.elseStatement.evaluate(graph, node, clazz, method)
    		}
    		LabeledStatement: statement.body.evaluate(graph, node, clazz, method)
    		ReturnStatement: statement.expression.evaluate(graph, node, clazz, method)
    		SuperConstructorInvocation: handleSuperConstructorInvocation(statement, graph, node, clazz, method)
    		SwitchCase: statement.expression.evaluate(graph, node, clazz, method)
    		SwitchStatement: {
    			statement.expression.evaluate(graph, node, clazz, method)
    			statement.statements.forEach[(it as Statement).evaluate(graph, node, clazz, method)]
    		}
    		SynchronizedStatement: {
    			statement.expression.evaluate(graph, node, clazz, method)
    			statement.body.evaluate(graph, node, clazz, method)
    		}
    		ThrowStatement: statement.expression.evaluate(graph, node, clazz, method)
    		TryStatement: {
    			statement.body.evaluate(graph, node, clazz, method)
    			statement.catchClauses.forEach[(it as CatchClause).body.evaluate(graph, node, clazz, method)]
    			statement.^finally.evaluate(graph, node, clazz, method)
    		}
    		// TODO TypeDeclarationStatement: for now this must be ignored. However, I am not totally sure if the could 
    		// not happen inside a method anyway, as the whole AST is a little weird
    		VariableDeclarationStatement: statement.fragments.forEach[(it as VariableDeclarationFragment).initializer.evaluate(graph, node, clazz, method)]
    		WhileStatement: {
    			statement.expression.evaluate(graph, node, clazz, method)
    			statement.body.evaluate(graph, node, clazz, method)
    		}
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
		targetNode.edges.add(edge)
		node.edges.add(edge)
	}
	
	
 	/**
 	 * Handle an constructor 'this' invocation. This requires (a) an call edge from
 	 * this method to the called constructor and (b) an evaluation of all parameters.
 	 */
	private def static handleConstructorInvocation(ConstructorInvocation invocation, ModularHypergraph graph, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		val edge = createCallEdge(clazz, method, clazz, invocation)
		if (!graph.edges.exists[it.name.equals(edge.name)]) {
			graph.edges.add(edge)
			val targetNode = graph.nodes.findNodeForConstructoreInvocation(invocation)
			targetNode.edges.add(edge)
			node.edges.add(edge)
		}
		invocation.arguments.forEach[(it as Expression).evaluate(graph, node, clazz, method)]
	}
	
	
	/**
	 * Expression evaluation hook.
	 */
	private static def void evaluate(Expression expression, ModularHypergraph graph, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		
	}
}