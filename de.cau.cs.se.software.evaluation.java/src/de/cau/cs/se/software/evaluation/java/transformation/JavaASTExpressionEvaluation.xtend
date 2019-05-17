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
import org.eclipse.jdt.core.dom.QualifiedName
import org.eclipse.jdt.core.dom.LambdaExpression
import org.eclipse.jdt.core.dom.ExpressionMethodReference

import static extension de.cau.cs.se.software.evaluation.java.transformation.JavaASTExpressionEvaluationHelper.*
import de.cau.cs.se.software.evaluation.jobs.IOutputHandler

class JavaASTExpressionEvaluation {

	/**
	 * Expression evaluation tree walker.
	 */
	static def void evaluate(Expression expression, Node sourceNode,
		ModularHypergraph graph, List<String> dataTypePatterns, IOutputHandler handler 
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
				expression.array.evaluate(sourceNode, graph, dataTypePatterns, handler)
				expression.index.evaluate(sourceNode, graph, dataTypePatterns, handler)
			}
    		ArrayCreation: {
    			expression.dimensions.forEach[(it as Expression).evaluate(sourceNode, graph, dataTypePatterns, handler)]
    			expression.initializer?.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		}
    		ArrayInitializer: {
				expression.expressions.forEach[
					(it as Expression).evaluate(sourceNode, graph, dataTypePatterns, handler)
				]
			}
    		Assignment: {
				expression.leftHandSide.evaluate(sourceNode, graph, dataTypePatterns, handler)
				expression.rightHandSide.evaluate(sourceNode, graph, dataTypePatterns, handler)
			}
    		CastExpression: expression.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		ClassInstanceCreation: expression.processClassInstanceCreation(sourceNode, graph, dataTypePatterns, handler)
    		ConditionalExpression: {
    			expression.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			expression.thenExpression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    			expression.elseExpression?.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		}
    		//CreationReference:
    		// TODO check if the following line is correct.
    		ExpressionMethodReference: expression.processExpressionMethodReference(sourceNode, contextTypeBinding, graph, dataTypePatterns)
    		FieldAccess: expression.processFieldAccess(sourceNode, contextTypeBinding, graph, dataTypePatterns, handler)
    		InfixExpression: {
    			expression.leftOperand.evaluate(sourceNode, graph, dataTypePatterns, handler)
				expression.rightOperand?.evaluate(sourceNode, graph, dataTypePatterns, handler)
			}
    		InstanceofExpression: expression.leftOperand.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		LambdaExpression: expression.processLambdaExpression(sourceNode, graph, dataTypePatterns, handler)
    		MethodInvocation: expression.processMethodInvocation(sourceNode, graph, dataTypePatterns, handler)
    		//MethodReference:
    		//Name:
    		ParenthesizedExpression: expression.expression.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		PostfixExpression: expression.operand.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		PrefixExpression: expression.operand.evaluate(sourceNode, graph, dataTypePatterns, handler)
    		QualifiedName: expression.processQualifiedName(sourceNode, graph, handler)
    		SimpleName: expression.processSimpleName(sourceNode, graph, handler)
    		SuperFieldAccess: expression.processSuperFieldAccess(sourceNode, graph, dataTypePatterns, handler)
    		SuperMethodInvocation: expression.processSuperMethodInvocation(sourceNode, graph, dataTypePatterns, handler) 
    		//SuperMethodReference: 
    		//TypeMethodReference:
    		VariableDeclarationExpression: expression.fragments.forEach[
    			/** Note: local variable declaration is ignored. */
				(it as VariableDeclarationFragment).initializer.evaluate(sourceNode, graph, dataTypePatterns, handler)
			] 
    		default:
    			throw new UnsupportedOperationException("Expressions of type " + expression.class + " are not supported. [" + expression + "]")
		}
	}
	
}