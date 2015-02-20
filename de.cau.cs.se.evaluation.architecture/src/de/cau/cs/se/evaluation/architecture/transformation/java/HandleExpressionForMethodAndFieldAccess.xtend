package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.Annotation
import org.eclipse.jdt.core.dom.ArrayAccess
import org.eclipse.jdt.core.dom.ArrayCreation
import org.eclipse.jdt.core.dom.ArrayInitializer
import org.eclipse.jdt.core.dom.Assignment
import org.eclipse.jdt.core.dom.BooleanLiteral
import org.eclipse.jdt.core.dom.CastExpression
import org.eclipse.jdt.core.dom.ClassInstanceCreation
import org.eclipse.jdt.core.dom.CharacterLiteral
import org.eclipse.jdt.core.dom.ConditionalExpression
import org.eclipse.jdt.core.dom.FieldAccess
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.InstanceofExpression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Name
import org.eclipse.jdt.core.dom.NullLiteral
import org.eclipse.jdt.core.dom.NumberLiteral
import org.eclipse.jdt.core.dom.ParenthesizedExpression
import org.eclipse.jdt.core.dom.PostfixExpression
import org.eclipse.jdt.core.dom.PrefixExpression
import org.eclipse.jdt.core.dom.StringLiteral
import org.eclipse.jdt.core.dom.SuperFieldAccess
import org.eclipse.jdt.core.dom.SuperMethodInvocation
import org.eclipse.jdt.core.dom.ThisExpression
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.jdt.core.dom.VariableDeclarationExpression
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import de.cau.cs.se.evaluation.architecture.transformation.IScope
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import org.eclipse.jdt.core.dom.MethodDeclaration
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper

class HandleExpressionForMethodAndFieldAccess {
	
	extension JavaTypeHelper javaTypeHelper = new JavaTypeHelper()
	
	var IScope scopes
	
	var ModularHypergraph modularSystem
	
	var MethodDeclaration method
	
	public new (IScope scopes) {
		this.scopes = scopes
	}
	
	def void handle(ModularHypergraph modularSystem, MethodDeclaration method, Expression expression) {
		this.modularSystem = modularSystem
		this.method = method
		if (expression != null)
			expression.findMethodAndFieldCallInExpression
	}
	
	
	/** annotation, no field or method invocation */
	private dispatch def void findMethodAndFieldCallInExpression(Annotation expression) {}
	
	/** array fields and index */
	private dispatch def void findMethodAndFieldCallInExpression(ArrayAccess expression) {
		expression.array.findMethodAndFieldCallInExpression
		if (expression.index != null)
			expression.index.findMethodAndFieldCallInExpression
	}
	
	/** array creation */
	private dispatch def void findMethodAndFieldCallInExpression(ArrayCreation expression) {
		expression.initializer?.findMethodAndFieldCallInExpression
		
		expression.dimensions.forEach[it.findMethodAndFieldCallInExpression]
	}
	
	/** array initialization */
	private dispatch def void findMethodAndFieldCallInExpression(ArrayInitializer expression) {
		expression.expressions.forEach[it.findMethodAndFieldCallInExpression]
	}
	
	/** assignment */
	private dispatch def void findMethodAndFieldCallInExpression(Assignment expression) {
		expression.leftHandSide.findMethodAndFieldCallInExpression
		expression.rightHandSide.findMethodAndFieldCallInExpression
	}
	
	/** Literal no references */
	private dispatch def void findMethodAndFieldCallInExpression(BooleanLiteral expression) {}
	
	/** cast */
	private dispatch def void findMethodAndFieldCallInExpression(CastExpression expression) {
		expression.expression.findMethodAndFieldCallInExpression	
	}
	
	/** Literal no references */
	private dispatch def void findMethodAndFieldCallInExpression(CharacterLiteral expression) {}
	
	/** class instance creation */
	private dispatch def void findMethodAndFieldCallInExpression(ClassInstanceCreation expression) {
		val callee = TransformationHelper.findConstructorMethod(modularSystem, expression.type, expression.arguments)
		// TODO
		//val edge = TransformationHelper.createEdgeBetweenMethods(modularSystem, method, callee)
		//if (!modularSystem.edges.exists[TransformationHelper.compareWith(it,edge)]) {
		//	modularSystem.edges.add(edge)
		//}
		expression.arguments.forEach[it.findMethodAndFieldCallInExpression]
		expression.expression?.findMethodAndFieldCallInExpression
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(ConditionalExpression expression) {
		expression.expression?.findMethodAndFieldCallInExpression
		expression.thenExpression?.findMethodAndFieldCallInExpression
		expression.elseExpression?.findMethodAndFieldCallInExpression
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(FieldAccess expression) {
		// check out complete field access expression.
		expression.expression.findMethodAndFieldCallInExpression
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(InfixExpression expression) {
		expression.leftOperand.findMethodAndFieldCallInExpression
		expression.rightOperand.findMethodAndFieldCallInExpression
		expression.extendedOperands.forEach[it.findMethodAndFieldCallInExpression]
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(InstanceofExpression expression) {
		// TODO why does the remainder not work?
	    // expression.leftOperand.findMethodAndFieldCallInExpression
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(MethodInvocation expression) {
		expression.arguments.forEach[it.findMethodAndFieldCallInExpression]
		/** resolve context of the method to determine to which type it belongs */
		expression.parent
		
		// TODO determine the class of the method
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(Name expression) {}
	private dispatch def void findMethodAndFieldCallInExpression(NullLiteral expression) {}
	
	private dispatch def void findMethodAndFieldCallInExpression(NumberLiteral expression) {}
	
	private dispatch def void findMethodAndFieldCallInExpression(ParenthesizedExpression expression) {
		expression.expression.findMethodAndFieldCallInExpression	
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(PostfixExpression expression) {
		expression.operand.findMethodAndFieldCallInExpression
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(PrefixExpression expression) {
		expression.operand.findMethodAndFieldCallInExpression
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(StringLiteral expression) {}
	
	private dispatch def void findMethodAndFieldCallInExpression(SuperFieldAccess expression) {
		// TODO check if this is true
		// because if we inherit a structure we inherit its provided and required interfaces
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(SuperMethodInvocation expression) {
		// TODO check if this is true
		// because if we inherit a structure we inherit its provided and required interfaces
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(ThisExpression expression) {
		// TODO this is important it is either a variable access or a local method call
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(TypeLiteral expression) {
		val binding = expression.type.resolveBinding
		if (binding != null) { // TODO when does this happen?
			if (binding.isClass) {
				binding.qualifiedName.resolveType
			}
		}
	}
	
	private def IType resolveType(String fqn) {
		// TODO add resolved types
		return null
	}
	
	private dispatch def void findMethodAndFieldCallInExpression(VariableDeclarationExpression expression) {		
		expression.fragments.forEach[(it as VariableDeclarationFragment).findMethodAndFieldCallInFragment]
	}
	
	private def void findMethodAndFieldCallInFragment(VariableDeclarationFragment fragment) {
		fragment.initializer.findMethodAndFieldCallInExpression
	}

	private dispatch def void findMethodAndFieldCallInExpression(Expression expression) {
		throw new UnsupportedOperationException(expression.class.name + " is not supported in expressions.")
	}


}
