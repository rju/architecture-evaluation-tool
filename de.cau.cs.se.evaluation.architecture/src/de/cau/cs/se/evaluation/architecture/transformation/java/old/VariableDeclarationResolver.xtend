package de.cau.cs.se.evaluation.architecture.transformation.java.old

import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.Block
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.ASTNode
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.jdt.core.dom.CatchClause
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ImportDeclaration
import java.util.List
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.jdt.core.dom.EnhancedForStatement
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration
import org.eclipse.jdt.core.dom.FieldDeclaration

class VariableDeclarationResolver {
	
	def static Type findVariableDeclaration(SimpleName name, AbstractTypeDeclaration type) {
		name.parent.findVariableDeclaration(name.fullyQualifiedName, type)
	}
	
	private static def getCompilationUnit(AbstractTypeDeclaration type) {
		type.parent.compilationUnit
	}
	
	private static def CompilationUnit getCompilationUnit(ASTNode astNode) {
		if (astNode instanceof CompilationUnit)
			return astNode as CompilationUnit
		else
			return astNode.parent.compilationUnit
	}
	
	
	/** -- Expressions -- */
	
	private static def dispatch Type findVariableDeclaration(MethodInvocation astNode, String variableName, AbstractTypeDeclaration type) {
		/*if (astNode.expression instanceof SimpleName) {
			val nodeName = (astNode.expression as SimpleName).fullyQualifiedName
			val importDeclaration = (type.compilationUnit.imports as List<ImportDeclaration>).findFirst[declaration | declaration.name.fullyQualifiedName.endsWith(nodeName)]
			return null
		} else*/
			return astNode.parent.findVariableDeclaration(variableName, type)
	}
	
	private static def dispatch Type findVariableDeclaration(Expression astNode, String variableName, AbstractTypeDeclaration type) {
		return astNode.parent.findVariableDeclaration(variableName, type)
	}
	
	/** -- Statements -- */
	
	private static def dispatch Type findVariableDeclaration(IfStatement astNode, String variableName, AbstractTypeDeclaration type) {
		return astNode.parent.findVariableDeclaration(variableName, type)
	}
	
	private static def dispatch Type findVariableDeclaration(ForStatement astNode, String variableName, AbstractTypeDeclaration type) {
		// TODO initializers have been ignored
		astNode.parent.findVariableDeclaration(variableName, type)
	}
	
	private static def dispatch Type findVariableDeclaration(EnhancedForStatement astNode, String variableName, AbstractTypeDeclaration type) {
		if (astNode.parameter.name.fullyQualifiedName.equals(variableName)) {
			astNode.parameter.type
		} else {
			astNode.parent.findVariableDeclaration(variableName, type)
		}
	}
		
	private static def dispatch Type findVariableDeclaration(Block astNode, String variableName, AbstractTypeDeclaration type) {
		val declaration = astNode.statements.filter(VariableDeclarationStatement).findFirst[declaration | 
			declaration.fragments.exists[(it as VariableDeclarationFragment).name.fullyQualifiedName.equals(variableName)]
		]
		if (declaration == null)
			return astNode.parent.findVariableDeclaration(variableName, type)
		else
			return declaration.type
	}
		
	private static def dispatch Type findVariableDeclaration(Statement astNode, String variableName, AbstractTypeDeclaration type) {
		return astNode.parent.findVariableDeclaration(variableName, type)
	}
	
	/** -- Other node types -- */
	
	private static def dispatch Type findVariableDeclaration(MethodDeclaration astNode, String variableName, AbstractTypeDeclaration type) {
		val variableDeclaration = astNode.parameters.findFirst[(it as SingleVariableDeclaration).name.fullyQualifiedName.equals(variableName)]
		if (variableDeclaration != null)
			return (variableDeclaration as SingleVariableDeclaration).type
		else
		 	return astNode.parent.findVariableDeclaration(variableName, type)
	}
	
	private static def dispatch Type findVariableDeclaration(VariableDeclarationFragment astNode, String variableName, AbstractTypeDeclaration type) {
		if (astNode.name.fullyQualifiedName.equals(variableName)) {
			switch (astNode.parent) {
				VariableDeclarationStatement: return (astNode.parent as VariableDeclarationStatement).type
				default: new Exception ("unhandled parent for variable declaration fragment " + astNode + " " + astNode.class)
			}
		}
		
		return astNode.parent.findVariableDeclaration(variableName, type)
	}
	
	private static def dispatch Type findVariableDeclaration(CatchClause astNode, String variableName, AbstractTypeDeclaration type) {
		if (astNode.exception != null) {
			if (astNode.exception.name.fullyQualifiedName.equals(variableName)) {
				return astNode.exception.type
			}
		}
		
		astNode.parent.findVariableDeclaration(variableName, type)
	}
	
	private static def dispatch Type findVariableDeclaration(TypeDeclaration astNode, String variableName, AbstractTypeDeclaration type) {
		val field = astNode.fields.findFirst[field | field.fragments.exists[variable | (variable as VariableDeclarationFragment).name.fullyQualifiedName.equals(variableName)]]
		if (field != null)
			return field.type
		else
			return null
	}
	
	private static def dispatch Type findVariableDeclaration(AnonymousClassDeclaration astNode, String variableName, AbstractTypeDeclaration type) {
		val field = astNode.bodyDeclarations.filter(FieldDeclaration).
			findFirst[field | field.fragments.exists[variable | (variable as VariableDeclarationFragment).name.fullyQualifiedName.equals(variableName)]]
		if (field != null)
			return field.type
		else
			return null
	}
	
	private static def dispatch Type findVariableDeclaration(ASTNode astNode, String variableName, AbstractTypeDeclaration type) {
		throw new Exception ("unhandled AST node type " + astNode + " " + astNode.class)
	}
}