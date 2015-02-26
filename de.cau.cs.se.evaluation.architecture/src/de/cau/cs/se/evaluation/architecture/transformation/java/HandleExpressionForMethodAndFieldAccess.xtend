package de.cau.cs.se.evaluation.architecture.transformation.java

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
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.TypeDeclaration
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJavaExtension.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.VariableDeclarationResolver.findVariableDeclaration
import static extension de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper.*

import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ImportDeclaration
import org.eclipse.jdt.core.dom.ParameterizedType
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.EnumDeclaration
import org.eclipse.jdt.core.dom.FieldDeclaration
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.core.runtime.Path
import org.eclipse.jdt.core.dom.ASTNode

class HandleExpressionForMethodAndFieldAccess {
			
	var ModularHypergraph modularSystem
	
	var MethodDeclaration method
	
	var AbstractTypeDeclaration type
	
	IJavaProject project
	
	public new (IJavaProject project) {
		this.project = project
	}
	
	/**
	 * Central handler for expressions.
	 * 
	 * @param modularSystem the hypergraph
	 * @param clazz the present class declaration in JDT DOM
	 * @param method one method from that class declaration
	 * @param the expression to evaluate
	 */
	def void handle(ModularHypergraph modularSystem, AbstractTypeDeclaration type, MethodDeclaration method, Expression expression) {
		this.modularSystem = modularSystem
		this.type = type
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
			val caller = modularSystem.findNodeForMethod(type, method)
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
			type.fields.forEach[
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
						val node = modularSystem.findNodeForMethod(type, method)
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
	 * on the FieldDeclaration? reference stored in the edge.
	 */
	def checkVariable(VariableDeclarationFragment variable, Edge edge) {
		if (edge.derivedFrom instanceof FieldTrace) {
			return edge.name.equals(type.determineFullQualifiedName(variable))
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
			val caller = modularSystem.findNodeForMethod(type, method)
			val callee = modularSystem.findNodeForMethod(type, methodName, expression.arguments)
			/** callee is null for inherited methods. */
			// TODO shall we include inherited methods? shall we include inherited methods when the source code is part of the project?
			if (callee != null) 
				modularSystem.createEdgeBetweenMethods(caller, callee)
		} else {
			/** access to another class */
			val caller = modularSystem.findNodeForMethod(type, method)
			switch(variable) {
				SimpleName case !variable.isClassStaticEnvocation: {
					val edge = modularSystem.findEdgeForVariable(type, variable.fullyQualifiedName)
					if (edge == null) {
						/** this is a local variable, handle as call, */
						val SimpleType localType = variable.findVariableDeclaration.resolveBaseType						
						val classImport = localType.parent.findCompilationUnit.imports.findFirst[classImport |
							(classImport as ImportDeclaration).name.fullyQualifiedName.endsWith(localType.name.fullyQualifiedName)
						]
						val module = 
							if (classImport != null) {
								(classImport as ImportDeclaration).name.fullyQualifiedName.findCorrespondingModule
							} else { /** same package */ 
								((type.parent as CompilationUnit).package.name.fullyQualifiedName + "." + (localType as SimpleType).name.fullyQualifiedName).findCorrespondingModule
							}
						if (module != null) { /** class is part of the system */
							val callee = module.nodes.findFirst[node | node.name.equals(module.name + "." + methodName)]
							modularSystem.createEdgeBetweenMethods(caller,callee)
						} 
					} else {
						val fieldName = ((edge.derivedFrom as FieldTrace).field as VariableDeclarationFragment).name.fullyQualifiedName
						val field = type.fields.findFirst[field | field.fragments.exists[frag | (frag as VariableDeclarationFragment).name.fullyQualifiedName.equals(fieldName)]]
						val classImport = (type.parent as CompilationUnit).imports.findFirst[classImport |
							(classImport as ImportDeclaration).name.fullyQualifiedName.endsWith((field.type as SimpleType).name.fullyQualifiedName)
						]
						val module = 
							if (classImport != null) {
								(classImport as ImportDeclaration).name.fullyQualifiedName.findCorrespondingModule
							} else { /** same package */ 
								((type.parent as CompilationUnit).package.name.fullyQualifiedName + "." + (field.type as SimpleType).name.fullyQualifiedName).findCorrespondingModule
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
		
	def CompilationUnit findCompilationUnit(ASTNode astNode) {
		if (astNode instanceof CompilationUnit)
			return astNode as CompilationUnit
		else
			return astNode.parent.findCompilationUnit
	}
	
	def SimpleType resolveBaseType(Type type) {
		switch(type) {
			SimpleType: type
			ParameterizedType: type.type.resolveBaseType
			default:
				throw new UnsupportedOperationException("type kind " + type.class + " unkown to implementation.")
		} 
	}
	
	def boolean isClassStaticEnvocation(SimpleName name) {
		if ((type.parent as CompilationUnit).imports.exists[classImport | 
			(classImport as ImportDeclaration).name.fullyQualifiedName.endsWith(name.fullyQualifiedName)
		]) {
			return true
		} else { /* check class in package */
			val packageName = (type.parent as CompilationUnit).package.name.fullyQualifiedName
			return modularSystem.modules.exists[module  | name.equals(packageName + "." + name.fullyQualifiedName)]
		}
	}
	
	def findCorrespondingModule(String fqn) {
		modularSystem.modules.findFirst[module | module.name.equals(fqn)]
	}
		
	
	/**
	 * check what name that is. If it is a variable connect to edge
	 */
	private dispatch def void findMethodAndFieldCallInExpression(Name expression) {
		val edge = modularSystem.edges.findFirst[
			val variableName = type.determineFullQualifiedName + "." + expression.fullyQualifiedName
			it.name.equals(variableName)
		]
		// TODO make this a function
		if (edge != null) {
			val node = modularSystem.findNodeForMethod(type, method)
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
		// TODO it is unclear if this should be ignored or evaluated as a variable to itself which would be interpreted as local variable
		// and therefore would be an edge
	}
	
	/**
	 * Access the class property of a class. This is ignored at the moment.
	 * TODO make this a node creation for the specific module representing the class and add
	 * a caller::callee edge to the modular system for it. 
	 */
	private dispatch def void findMethodAndFieldCallInExpression(TypeLiteral expression) {
		
	}
	
	/**
	 * Create edges for variables if necessary.
	 */
	private dispatch def void findMethodAndFieldCallInExpression(VariableDeclarationExpression expression) {		
		expression.fragments.forEach[(it as VariableDeclarationFragment).processVariableDeclarationFragment]
	}
	
	/**
	 * 
	 */
	private def void processVariableDeclarationFragment(VariableDeclarationFragment fragment) {
		// TODO check if necessary to create edges for variables (do not do so for local variables)
		fragment.initializer.findMethodAndFieldCallInExpression
	}

	/**
	 * Catcher for all expression types not handled above. As this is a serious error in the implementation. Throw an error.
	 */
	private dispatch def void findMethodAndFieldCallInExpression(Expression expression) {
		throw new UnsupportedOperationException("Coder forgot to implement support for class " + expression.class.name + " therefore it is not a supported expression type.")
	}
	
	/** -- special service functions -- */
	def getFields(AbstractTypeDeclaration type) {
		switch (type) {
			TypeDeclaration: type.fields.filter(FieldDeclaration)
			EnumDeclaration: type.bodyDeclarations.filter(FieldDeclaration)
			default:
				throw new UnsupportedOperationException("Cannot handle" + type.class + " in getFields")
		}
	}


}
