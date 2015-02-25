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
import org.eclipse.jdt.core.dom.TypeDeclaration
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJavaExtension.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJDTDOMExtension.*

import static extension de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper.*

import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.IField
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.ASTNode
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ImportDeclaration
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace

class HandleExpressionForMethodAndFieldAccess {
		
	var IScope scopes
	
	var ModularHypergraph modularSystem
	
	var MethodDeclaration method
	
	var TypeDeclaration clazz
	
	public new (IScope scopes) {
		this.scopes = scopes
	}
	
	/**
	 * Central handler for expressions.
	 * 
	 * @param modularSystem the hypergraph
	 * @param clazz the present class declaration in JDT DOM
	 * @param method one method from that class declaration
	 * @param the expression to evaluate
	 */
	def void handle(ModularHypergraph modularSystem, TypeDeclaration clazz, MethodDeclaration method, Expression expression) {
		this.modularSystem = modularSystem
		this.clazz = clazz
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
	
	/** cast TODO */
	private dispatch def void findMethodAndFieldCallInExpression(CastExpression expression) {
		expression.expression.findMethodAndFieldCallInExpression	
	}
	
	/** Literal no references */
	private dispatch def void findMethodAndFieldCallInExpression(CharacterLiteral expression) {}
	
	/** class instance creation */
	private dispatch def void findMethodAndFieldCallInExpression(ClassInstanceCreation expression) {
		val callee = modularSystem.findConstructorMethod(expression.type, expression.arguments)
		if (callee != null) {
			val caller = modularSystem.findNodeForMethod(clazz, method)
			modularSystem.createEdgeBetweenMethods(caller, callee)
		}
		expression.arguments.forEach[it.findMethodAndFieldCallInExpression]
		expression.expression?.findMethodAndFieldCallInExpression
	}
	
	/**
	 * If then else - conditional expression.
	 */
	private dispatch def void findMethodAndFieldCallInExpression(ConditionalExpression expression) {
		expression.expression?.findMethodAndFieldCallInExpression
		expression.thenExpression?.findMethodAndFieldCallInExpression
		expression.elseExpression?.findMethodAndFieldCallInExpression
	}
	
	/**
	 * Field access.
	 */
	private dispatch def void findMethodAndFieldCallInExpression(FieldAccess expression) {
		// check out complete field access expression. expression is for instance this, field name is in fieldName
		if (expression.expression instanceof ThisExpression) {
			clazz.fields.forEach[
				val variableDecl = it.fragments.findFirst[fragment | 
					(fragment as VariableDeclarationFragment).name.
						fullyQualifiedName.equals(expression.name.fullyQualifiedName)
				]
				if (variableDecl != null) {
					// the edge represents a internal variable of the class
					val edge = modularSystem.edges.findFirst[checkVariable(variableDecl as VariableDeclarationFragment,it)]
					if (edge != null) {
						// there is an edge for this variable (if not this is an error)
						// connect present method node to variable edge
						val node = modularSystem.findNodeForMethod(clazz, method)
						node.edges.add(edge)
					} else {
						throw new Exception("Missing edge for variable " + variableDecl.toString)
					}
				}
			]
		}
	}
	
	/**
	 * Match if a given variable in jdt.dom corresponds to an edge in the hypergraph based
	 * on the IField reference stored in the edge.
	 */
	def checkVariable(VariableDeclarationFragment variable, Edge edge) {
		if (edge.derivedFrom instanceof FieldTrace) {
			return edge.name.equals(clazz.determineFullQualifiedName(variable))
		}
		return false
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
		val methodName = expression.name.fullyQualifiedName
		val variable = expression?.expression
		if (variable == null) {
			/** this is a local call */
			val caller = modularSystem.findNodeForMethod(clazz, method)
			val callee = modularSystem.findNodeForMethod(clazz, methodName, expression.arguments)
			modularSystem.createEdgeBetweenMethods(caller, callee)
		} else {
			/** access to another class */
			val caller = modularSystem.findNodeForMethod(clazz, method)
			switch(variable) {
				SimpleName case !variable.isClassStaticEnvocation: {
					val edge = modularSystem.findEdgeForVariable(clazz, variable.fullyQualifiedName)
					if (edge == null) {
						/** this is a local variable, handle as call, */
						val Type type = variable.findVariableDeclaration 
						val classImport = (clazz.parent as CompilationUnit).imports.findFirst[classImport |
							(classImport as ImportDeclaration).name.fullyQualifiedName.endsWith((type as SimpleType).name.fullyQualifiedName)
						]
						val module = 
							if (classImport != null) {
								(classImport as ImportDeclaration).name.fullyQualifiedName.loadClass
							} else { /** same package */ 
								((clazz.parent as CompilationUnit).package.name.fullyQualifiedName + "." + (type as SimpleType).name.fullyQualifiedName).loadClass
							}
							
						val callee = module.nodes.findFirst[node | node.name.equals(module.name + "." + methodName)]
						modularSystem.createEdgeBetweenMethods(caller,callee)
					} else {
						val fieldName = ((edge.derivedFrom as FieldTrace).field as IField).elementName
						val field = clazz.fields.findFirst[field | field.fragments.exists[frag | (frag as VariableDeclarationFragment).name.fullyQualifiedName.equals(fieldName)]]
						val classImport = (clazz.parent as CompilationUnit).imports.findFirst[classImport |
							(classImport as ImportDeclaration).name.fullyQualifiedName.endsWith((field.type as SimpleType).name.fullyQualifiedName)
						]
						val module = 
							if (classImport != null) {
								(classImport as ImportDeclaration).name.fullyQualifiedName.loadClass
							} else { /** same package */ 
								((clazz.parent as CompilationUnit).package.name.fullyQualifiedName + "." + (field.type as SimpleType).name.fullyQualifiedName).loadClass
							}
						val callee = module.nodes.findFirst[node | node.name.equals(module.name + "." + methodName)]	
						if (!caller.edges.contains(edge)) caller.edges.add(edge)
						if (!callee.edges.contains(edge)) callee.edges.add(edge)
					}
				}
				SimpleName case variable.isClassStaticEnvocation: {
					// TODO
				}
			}
		}
	}
	
	def boolean isClassStaticEnvocation(SimpleName name) {
		return (clazz.parent as CompilationUnit).imports.exists[classImport | 
			(classImport as ImportDeclaration).name.fullyQualifiedName.endsWith(name.fullyQualifiedName)
		]
	}
	
	def loadClass(String fqn) {
		modularSystem.modules.findFirst[module | module.name.equals(fqn)]
	}
	
	def Type findVariableDeclaration(SimpleName name) {
		if (name.parent instanceof Expression)
			return name.parent.findVariableDeclaration(name.fullyQualifiedName)
		else if (name.parent instanceof Statement)
			return name.parent.findVariableDeclaration(name.fullyQualifiedName)
		else
			return null
	}
	
	def dispatch Type findVariableDeclaration(Expression node, String variableName) {
		return node.parent.findVariableDeclaration(variableName)
	}
	
	def dispatch Type findVariableDeclaration(Statement node, String variableName) {
		return node.parent.findVariableDeclaration(variableName)
	}
	
	def dispatch Type findVariableDeclaration(Block node, String variableName) {
		val declaration = node.statements.filter(VariableDeclarationStatement).findFirst[declaration | 
			declaration.fragments.exists[(it as VariableDeclarationFragment).name.fullyQualifiedName.equals(variableName)]
		]
		return declaration?.type
	}
	
	def dispatch Type findVariableDeclaration(MethodDeclaration node, String variableName) {
		return node.body.findVariableDeclaration(variableName)
	}
	
	def dispatch Type findVariableDeclaration(ASTNode node, String variableName) {
		throw new Exception ("unhandled node type " + node + " " + node.class)
	}
	
	
	/**
	 * check what name that is. If it is a variable connect to edge
	 */
	private dispatch def void findMethodAndFieldCallInExpression(Name expression) {
		val edge = modularSystem.edges.findFirst[
			val variableName = clazz.determineFullQualifiedName + "." + expression.fullyQualifiedName
			it.name.equals(variableName)
		]
		// TODO make this a function
		if (edge != null) {
			val node = modularSystem.findNodeForMethod(clazz, method)
			node.edges.add(edge)
		}
	}
	
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
	
	/** this should not be called by the dispatcher it must be resolved in variable or method access expressions */
	private dispatch def void findMethodAndFieldCallInExpression(ThisExpression expression) {
		throw new Exception("Eeek ThisExpression must not be evaluated by the dispatcher")
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
