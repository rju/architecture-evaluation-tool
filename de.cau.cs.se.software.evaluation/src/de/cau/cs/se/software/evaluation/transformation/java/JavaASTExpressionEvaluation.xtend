package de.cau.cs.se.software.evaluation.transformation.java

import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Node
import java.util.List
import org.eclipse.jdt.core.dom.Annotation
import org.eclipse.jdt.core.dom.ArrayAccess
import org.eclipse.jdt.core.dom.ArrayCreation
import org.eclipse.jdt.core.dom.ArrayInitializer
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.BooleanLiteral
import org.eclipse.jdt.core.dom.CastExpression
import org.eclipse.jdt.core.dom.CharacterLiteral
import org.eclipse.jdt.core.dom.ClassInstanceCreation
import org.eclipse.jdt.core.dom.ConditionalExpression
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.InstanceofExpression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.NullLiteral
import org.eclipse.jdt.core.dom.NumberLiteral
import org.eclipse.jdt.core.dom.ParenthesizedExpression
import org.eclipse.jdt.core.dom.PostfixExpression
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.jdt.core.dom.SuperFieldAccess
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.jdt.core.dom.ThisExpression
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.jdt.core.dom.VariableDeclarationExpression
import org.eclipse.jdt.core.dom.VariableDeclarationFragment

import static extension de.cau.cs.se.software.evaluation.transformation.java.JavaASTExpressionEvaluationHelper.*
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.jdt.core.dom.LambdaExpression
import org.eclipse.jdt.core.dom.ExpressionMethodReference

class JavaASTExpressionEvaluation {

	/**
	 * Expression evaluation tree walker.
	 */
	static def void evaluate(Expression expression, Node sourceNode,
		ModularHypergraph graph, List<String> dataTypePatterns 
	) {
		val sourceMethodBinding = (sourceNode.derivedFrom as MethodTrace).method as IMethodBinding
		val contextTypeBinding = sourceMethodBinding.declaringClass 
		switch(expression) {
			/** expression types which require no handling. */
			Annotation,
			BooleanLiteral,
			CharacterLiteral,
			NullLiteral, NumberLiteral,
			StringLiteral,
			ThisExpression,
			TypeLiteral: {}
			/** expression types which require checks for call and data edge creation. */
			ArrayAccess: {
				expression.array.evaluate(sourceNode, graph, dataTypePatterns)
				expression.index.evaluate(sourceNode, graph, dataTypePatterns)
			}
    		ArrayCreation: {
    			expression.dimensions.forEach[(it as Expression).evaluate(sourceNode, graph, dataTypePatterns)]
    			expression.initializer?.evaluate(sourceNode, graph, dataTypePatterns)
    		}
    		ArrayInitializer: {
				expression.expressions.forEach[
					(it as Expression).evaluate(sourceNode, graph, dataTypePatterns)
				]
			}
    		Assignment: {
				expression.leftHandSide.evaluate(sourceNode, graph, dataTypePatterns)		
				expression.rightHandSide.evaluate(sourceNode, graph, dataTypePatterns)
			}
    		CastExpression: expression.expression.evaluate(sourceNode, graph, dataTypePatterns)
    		ClassInstanceCreation: expression.processClassInstanceCreation(sourceNode, graph, dataTypePatterns)
    		ConditionalExpression: {
    			expression.expression.evaluate(sourceNode, graph, dataTypePatterns)
    			expression.thenExpression.evaluate(sourceNode, graph, dataTypePatterns)
    			expression.elseExpression?.evaluate(sourceNode, graph, dataTypePatterns)
    		}
    		//CreationReference:
    		// TODO check if the following line is correct.
    		ExpressionMethodReference: expression.processExpressionMethodReference(sourceNode, contextTypeBinding, graph, dataTypePatterns)
    		FieldAccess: expression.processFieldAccess(sourceNode, contextTypeBinding, graph, dataTypePatterns)
    		InfixExpression: {
    			expression.leftOperand.evaluate(sourceNode, graph, dataTypePatterns)
				expression.rightOperand?.evaluate(sourceNode, graph, dataTypePatterns)
			}
    		InstanceofExpression: expression.leftOperand.evaluate(sourceNode, graph, dataTypePatterns)
    		LambdaExpression: expression.processLambdaExpression(sourceNode, graph, dataTypePatterns)
    		MethodInvocation: expression.processMethodInvocation(sourceNode, graph, dataTypePatterns)
    		//MethodReference:
    		//Name:
    		ParenthesizedExpression: expression.expression.evaluate(sourceNode, graph, dataTypePatterns)
    		PostfixExpression: expression.operand.evaluate(sourceNode, graph, dataTypePatterns)
    		PrefixExpression: expression.operand.evaluate(sourceNode, graph, dataTypePatterns)
    		QualifiedName: expression.processQualifiedName(sourceNode, graph)
    		SimpleName: expression.processSimpleName(sourceNode, graph)
    		SuperFieldAccess: expression.processSuperFieldAccess(sourceNode, graph, dataTypePatterns)
    		SuperMethodInvocation: expression.processSuperMethodInvocation(sourceNode, graph, dataTypePatterns) 
    		//SuperMethodReference: 
    		//TypeMethodReference:
    		VariableDeclarationExpression: expression.fragments.forEach[
    			/** Note: local variable declaration is ignored. */
				(it as VariableDeclarationFragment).initializer.evaluate(sourceNode, graph, dataTypePatterns)
			] 
    		default:
    			throw new UnsupportedOperationException("Expressions of type " + expression.class + " are not supported. [" + expression + "]")
		}
	}
	
}