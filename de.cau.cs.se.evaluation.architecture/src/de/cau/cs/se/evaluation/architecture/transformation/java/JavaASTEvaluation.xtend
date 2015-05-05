package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.jdt.core.dom.Block
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.AssertStatement
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.ConstructorInvocation

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphQueryHelper.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaASTExpressionEvaluationHelper.*
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
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.ClassInstanceCreation
import org.eclipse.jdt.core.dom.MethodInvocation
import java.util.List
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.jdt.core.dom.NullLiteral
import org.eclipse.jdt.core.dom.NumberLiteral
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.BooleanLiteral
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.jdt.core.dom.VariableDeclarationExpression
import org.eclipse.jdt.core.dom.PostfixExpression
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.jdt.core.dom.ParenthesizedExpression
import org.eclipse.jdt.core.dom.ConditionalExpression
import org.eclipse.jdt.core.dom.ArrayCreation
import org.eclipse.jdt.core.dom.ArrayInitializer
import org.eclipse.jdt.core.dom.ArrayAccess
import org.eclipse.jdt.core.dom.CastExpression
import org.eclipse.jdt.core.dom.BreakStatement
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.InstanceofExpression
import org.eclipse.jdt.core.dom.EmptyStatement
import org.eclipse.jdt.core.dom.CharacterLiteral
import org.eclipse.jdt.core.dom.ThisExpression

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
				method.body.statements.forEach[statement | (statement as Statement).evaluate(graph, dataTypePatterns, node, clazz, method)]
			}
		}
	} 
	
	/**
	 * Evaluate statements.
	 */
	private static def void evaluate(Statement statement, ModularHypergraph graph, List<String> dataTypePatterns, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		// System.out.println("statement evaluate <" + node.name + "> " + clazz.name.fullyQualifiedName + " - " + method.name.fullyQualifiedName)
		switch (statement) {
			AssertStatement: statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    		Block: statement.statements.forEach[(it as Statement).evaluate(graph, dataTypePatterns, node, clazz, method)]
    		ConstructorInvocation: handleConstructorInvocation(statement, graph, dataTypePatterns, node, clazz, method)
    		DoStatement: {
    			statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.body.evaluate(graph, dataTypePatterns, node, clazz, method)
    		}
    		EnhancedForStatement: {
    			/* we ignore local value declarations here (see paper) */
    			statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.body.evaluate(graph, dataTypePatterns, node, clazz, method)
    		}
    		ExpressionStatement: statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    		ForStatement: {
    			statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.initializers.forEach[(it as Expression).evaluate(graph, dataTypePatterns, node, clazz, method)]
    			statement.updaters.forEach[(it as Expression).evaluate(graph, dataTypePatterns, node, clazz, method)]
    			statement.body.evaluate(graph, dataTypePatterns, node, clazz, method)
    		}
    		IfStatement: {
    			statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.thenStatement.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.elseStatement?.evaluate(graph, dataTypePatterns, node, clazz, method)
    		}
    		LabeledStatement: statement.body.evaluate(graph, dataTypePatterns, node, clazz, method)
    		ReturnStatement: statement.expression?.evaluate(graph, dataTypePatterns, node, clazz, method)
    		SuperConstructorInvocation: handleSuperConstructorInvocation(statement, graph, node, clazz, method)
    		SwitchCase: statement.expression?.evaluate(graph, dataTypePatterns, node, clazz, method)
    		SwitchStatement: {
    			statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.statements.forEach[(it as Statement).evaluate(graph, dataTypePatterns, node, clazz, method)]
    		}
    		SynchronizedStatement: {
    			statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.body.evaluate(graph, dataTypePatterns, node, clazz, method)
    		}
    		ThrowStatement: statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    		TryStatement: {
    			statement.body.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.catchClauses.forEach[(it as CatchClause).body.evaluate(graph, dataTypePatterns, node, clazz, method)]
    			statement.^finally?.evaluate(graph, dataTypePatterns, node, clazz, method)
    		}
    		// TODO TypeDeclarationStatement: for now this must be ignored. However, I am not totally sure if the could 
    		// not happen inside a method anyway, as the whole AST is a little weird
    		VariableDeclarationStatement: statement.fragments.forEach[(it as VariableDeclarationFragment).
    			initializer?.evaluate(graph, dataTypePatterns, node, clazz, method)
    		]
    		WhileStatement: {
    			statement.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			statement.body.evaluate(graph, dataTypePatterns, node, clazz, method)
    		}
			BreakStatement, EmptyStatement: return
    		default:
    			throw new UnsupportedOperationException("Expressions of type " + statement.class + " are not supported.")
		}
	}
	
	/**
	 * Expression evaluation hook.
	 */
	static def void evaluate(Expression expression, ModularHypergraph graph, List<String> dataTypePatterns, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		// System.out.println("expression evaluate <" + node.name + "> " + clazz.name.fullyQualifiedName + " - " + method.name.fullyQualifiedName)
		switch(expression) {
			ArrayAccess: {
				expression.array.evaluate(graph, dataTypePatterns, node, clazz, method)
				expression.index.evaluate(graph, dataTypePatterns, node, clazz, method)
			}
    		ArrayCreation: {
    			expression.dimensions.forEach[(it as Expression).evaluate(graph, dataTypePatterns, node, clazz, method)]
    			// TODO add missing support for expression.initializer
    		}
    		Assignment: expression.processAssignment(graph, dataTypePatterns, node, clazz, method)
    		CastExpression: expression.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    		ClassInstanceCreation: expression.processClassInstanceCreation(graph, dataTypePatterns, node, clazz, method)
    		ConditionalExpression: {
    			expression.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			expression.thenExpression.evaluate(graph, dataTypePatterns, node, clazz, method)
    			expression.elseExpression?.evaluate(graph, dataTypePatterns, node, clazz, method)
    		}
    		//CreationReference:
    		//ExpressionMethodReference:
    		FieldAccess: expression.processFieldAccess(graph, dataTypePatterns, clazz.resolveBinding, node)
    		InfixExpression: {
    			expression.leftOperand.evaluate(graph, dataTypePatterns, node, clazz, method)
				expression.rightOperand?.evaluate(graph, dataTypePatterns, node, clazz, method)
			}
    		InstanceofExpression: expression.leftOperand.evaluate(graph, dataTypePatterns, node, clazz, method)
    		//LambdaExpression:
    		MethodInvocation: expression.processMethodInvocation(graph, dataTypePatterns, node, clazz, method)
    		//MethodReference:
    		//Name:
    		ParenthesizedExpression: expression.expression.evaluate(graph, dataTypePatterns, node, clazz, method)
    		SimpleName: {
    			if (expression.resolveBinding instanceof IVariableBinding) {
	    			val edge = findDataEdge(graph.edges, expression.resolveBinding as IVariableBinding)
	    			if (edge != null) { /** found an data edge */
	    				node.edges.add(edge)
	    				// System.out.println("expression evaluate <" + node.name + "> [" + edge.name + "]")
	    			}
    			}
    		}
    		//SuperFieldAccess:
    		SuperMethodInvocation: {
    			val superMethodBinding = expression.resolveMethodBinding
    			val targetNode = findOrCreateTargetNode(graph, superMethodBinding.declaringClass, superMethodBinding)
    			val edge = createCallEdge(method.resolveBinding, superMethodBinding)  
    			node.edges.add(edge)
    			targetNode.edges.add(edge)
    			graph.edges.add(edge)
    		}
    		//SuperMethodReference:
    		TypeLiteral: {
    			val typeBinding = expression.type.resolveBinding
    			if (typeBinding.interface) { /** possible framework module */
    				var module = graph.modules.findModule(typeBinding)
    				if (module == null) {
    					module = createModuleForTypeBinding(typeBinding)
    					graph.modules.add(module)
    				}
    				var targetNode = module.nodes.findFirst[it.name.equals(typeBinding.determineFullyQualifiedName + "." + typeBinding.name)]
    				if (targetNode == null) {
    					targetNode = createNodeForImplicitConstructor(typeBinding)
    					graph.nodes.add(targetNode)
    				}
    				// TODO edge cannot be built, as there is not constructor
    				// Most likely the code above is not helpful and the case Class.class should be handled differently.
    			} else {
    				val constructorBinding = typeBinding.declaredMethods.findFirst[it.constructor]
    				val targetNode = findOrCreateTargetNode(graph, typeBinding, constructorBinding) 
    				val edge = createCallEdge(method.resolveBinding, constructorBinding) 
    				
    				node.edges.add(edge)
    				targetNode.edges.add(edge)
    				graph.edges.add(edge)
    			}	
    		}
    		//TypeMethodReference:
    		VariableDeclarationExpression: {
    			val typeName = expression.resolveTypeBinding.determineFullyQualifiedName
    			if (!dataTypePatterns.exists[typeName.matches(it)] && !expression.resolveTypeBinding.primitive) {
    				/** is not a data type. */
    				expression.fragments.forEach[(it as VariableDeclarationFragment).
    					initializer.evaluate(graph, dataTypePatterns, node, clazz, method)
    				]
    			} 
    		}
    		/** ignore the following types. */
    		ArrayInitializer, // TODO ArrayInitializer should be handled with call and data edges
    		BooleanLiteral,
    		CharacterLiteral,
    		NullLiteral, NumberLiteral, 
    		PostfixExpression, PrefixExpression,
    		QualifiedName, // TODO maybe there are qualified names which we should consider? Constants must be ignored.
    		StringLiteral,
    		ThisExpression: return
    		default:
    			throw new UnsupportedOperationException("Expressions of type " + expression.class + " are not supported. [" + expression + "]")
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
		// System.out.println("handleSuperConstructorInvocation <" + node.name + "> <" + targetNode.name + "> [" + edge.name + "]")
	}
	
	
 	/**
 	 * Handle an constructor 'this' invocation. This requires (a) an call edge from
 	 * this method to the called constructor and (b) an evaluation of all parameters.
 	 */
	private def static handleConstructorInvocation(ConstructorInvocation invocation, ModularHypergraph graph, List<String> dataTypePatterns, Node node, TypeDeclaration clazz, MethodDeclaration method) {
		val edge = createCallEdge(clazz, method, clazz, invocation)
		if (!graph.edges.exists[it.name.equals(edge.name)]) {
			graph.edges.add(edge)
			val targetNode = graph.nodes.findNodeForMethodBinding(invocation.resolveConstructorBinding)
			targetNode.edges.add(edge)
			node.edges.add(edge)
			// System.out.println("handleConstructorInvocation <" + node.name + "> <" + targetNode.name + "> [" + edge.name + "]")
		}
		invocation.arguments.forEach[(it as Expression).evaluate(graph, dataTypePatterns, node, clazz, method)]
	}
	
}