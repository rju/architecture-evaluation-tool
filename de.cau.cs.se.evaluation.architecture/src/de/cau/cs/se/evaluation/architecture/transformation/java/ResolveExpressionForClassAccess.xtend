package de.cau.cs.se.evaluation.architecture.transformation.java

import java.util.List
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.Annotation
import org.eclipse.jdt.core.dom.ArrayAccess
import org.eclipse.jdt.core.dom.ArrayCreation
import java.util.ArrayList
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

class ResolveExpressionForClassAccess {
	
	extension JavaTypeHelper javaTypeHelper = new JavaTypeHelper()
	
	var IScope scopes
	
	public new (IScope scopes) {
		this.scopes = scopes
	}
	
	def List<IType> resolve(Expression expression) {
		return expression.findClassCallInExpression
	}
	
	
	private dispatch def List<IType> findClassCallInExpression(Annotation expression) {
		return null
	}
	
	private dispatch def List<IType> findClassCallInExpression(ArrayAccess expression) {
		val types = expression.array.findClassCallInExpression
		return if (expression.index != null)
			expression.index.findClassCallInExpression.addUnique(types)
		else
			types
	}
	
	private dispatch def List<IType> findClassCallInExpression(ArrayCreation expression) {
		val types = new ArrayList<IType>() 
		types.addUnique(expression.initializer?.findClassCallInExpression)
		
		expression.dimensions.forEach[types.addUnique(it.findClassCallInExpression)]

		return types
	}
	
	private dispatch def List<IType> findClassCallInExpression(ArrayInitializer expression) {
		val types = new ArrayList<IType>()
		expression.expressions.forEach[types.addUnique(it.findClassCallInExpression)]

		return types
	}
	
	private dispatch def List<IType> findClassCallInExpression(Assignment expression) {
		val types = new ArrayList<IType>()
		
		types.addUnique(expression.leftHandSide.findClassCallInExpression)
		types.addUnique(expression.rightHandSide.findClassCallInExpression)
		
		return types
	}
	
	private dispatch def List<IType> findClassCallInExpression(BooleanLiteral expression) {
		return null
	}
	
	private dispatch def List<IType> findClassCallInExpression(CastExpression expression) {
		return expression.expression.findClassCallInExpression	
	}
	
	private dispatch def List<IType> findClassCallInExpression(CharacterLiteral expression) {
		return null
	}
	
	private dispatch def List<IType> findClassCallInExpression(ClassInstanceCreation expression) {
		val types = new ArrayList<IType>()
		
		expression.arguments.forEach[types.addUnique(it.findClassCallInExpression)]
		types.addUnique(expression.expression?.findClassCallInExpression)
		
		return types
	}
	
	private dispatch def List<IType> findClassCallInExpression(ConditionalExpression expression) {
		val types = new ArrayList<IType>()
		
		types.addUnique(expression.expression?.findClassCallInExpression)
		types.addUnique(expression.thenExpression?.findClassCallInExpression)
		types.addUnique(expression.elseExpression?.findClassCallInExpression)
		
		return types
	}
	
	private dispatch def List<IType> findClassCallInExpression(FieldAccess expression) {
		return expression.expression.findClassCallInExpression
	}
	
	private dispatch def List<IType> findClassCallInExpression(InfixExpression expression) {
		val types = new ArrayList<IType>()
		
		types.addUnique(expression.leftOperand.findClassCallInExpression)
		types.addUnique(expression.rightOperand.findClassCallInExpression)
		expression.extendedOperands.forEach[types.addUnique(it.findClassCallInExpression)]
		
		return types
	}
	
	private dispatch def List<IType> findClassCallInExpression(InstanceofExpression expression) {
		// TODO why does the remainder not work?
		return null // expression.leftOperand.findClassCallInExpression
	}
	
	private dispatch def List<IType> findClassCallInExpression(MethodInvocation expression) {
		val types = new ArrayList<IType>()
		
		expression.arguments.forEach[types.addUnique(it.findClassCallInExpression)]
		/** resolve context of the method to determine to which type it belongs */
		expression.parent
		
		// TODO determine the class of the method
		
		return types	
	}
	
	private dispatch def List<IType> findClassCallInExpression(Name expression) {
		return null
	}
	private dispatch def List<IType> findClassCallInExpression(NullLiteral expression) {
		return null
	}
	
	private dispatch def List<IType> findClassCallInExpression(NumberLiteral expression) {
		return null
	}
	
	private dispatch def List<IType> findClassCallInExpression(ParenthesizedExpression expression) {
		return expression.expression.findClassCallInExpression	
	}
	
	private dispatch def List<IType> findClassCallInExpression(PostfixExpression expression) {
		return expression.operand.findClassCallInExpression
	}
	
	private dispatch def List<IType> findClassCallInExpression(PrefixExpression expression) {
		return expression.operand.findClassCallInExpression
	}
	
	private dispatch def List<IType> findClassCallInExpression(StringLiteral expression) {
		return null
	}
	
	private dispatch def List<IType> findClassCallInExpression(SuperFieldAccess expression) {
		return null // because if we inherit a structure we inherit its provided and required interfaces
	}
	
	private dispatch def List<IType> findClassCallInExpression(SuperMethodInvocation expression) {
		return null // because if we inherit a structure we inherit its provided and required interfaces
	}
	
	private dispatch def List<IType> findClassCallInExpression(ThisExpression expression) {
		return null
	}
	
	private dispatch def List<IType> findClassCallInExpression(TypeLiteral expression) {
		val List<IType> types = new ArrayList<IType>()
		val binding = expression.type.resolveBinding
		if (binding != null) { // TODO when does this happen?
			if (binding.isClass) {
				types.addUnique(binding.qualifiedName.resolveType)
			}
		}
		return types
	}
	
	private def IType resolveType(String fqn) {
		// TODO add resolved types
		return null
	}
	
	private dispatch def List<IType> findClassCallInExpression(VariableDeclarationExpression expression) {
		val List<IType> types = new ArrayList<IType>()
		
		expression.fragments.forEach[types.addUnique((it as VariableDeclarationFragment).findClassCallInFragment)]
		
		return types
	}
	
	private def List<IType> findClassCallInFragment(VariableDeclarationFragment fragment) {
		return fragment.initializer.findClassCallInExpression
	}

	private dispatch def List<IType> findClassCallInExpression(Expression expression) {
		throw new UnsupportedOperationException(expression.class.name + " is not supported in expressions.")
	}


}
