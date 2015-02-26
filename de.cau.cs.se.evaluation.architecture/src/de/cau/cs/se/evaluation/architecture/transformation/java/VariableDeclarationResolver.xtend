package de.cau.cs.se.evaluation.architecture.transformation.java

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

class VariableDeclarationResolver {
	
	def static Type findVariableDeclaration(SimpleName name) {
		name.parent.findVariableDeclaration(name.fullyQualifiedName)
	}
	
	/** -- Expressions -- */
	
	private static def dispatch Type findVariableDeclaration(MethodInvocation astNode, String variableName) {
		if (astNode.expression instanceof SimpleName) {
			(astNode.expression as SimpleName).findCorrespondingClass
		} else
			return astNode.parent.findVariableDeclaration(variableName)
	}
	
	private static def dispatch Type findVariableDeclaration(Expression astNode, String variableName) {
		return astNode.parent.findVariableDeclaration(variableName)
	}
	
	/** -- Statements -- */
	
	private static def dispatch Type findVariableDeclaration(IfStatement astNode, String variableName) {
		return astNode.parent.findVariableDeclaration(variableName)
	}
		
	private static def dispatch Type findVariableDeclaration(Block astNode, String variableName) {
		val declaration = astNode.statements.filter(VariableDeclarationStatement).findFirst[declaration | 
			declaration.fragments.exists[(it as VariableDeclarationFragment).name.fullyQualifiedName.equals(variableName)]
		]
		if (declaration == null)
			return astNode.parent.findVariableDeclaration(variableName)
		else
			return declaration.type
	}
		
	private static def dispatch Type findVariableDeclaration(Statement astNode, String variableName) {
		return astNode.parent.findVariableDeclaration(variableName)
	}
	
	/** -- Other node types -- */
	
	private static def dispatch Type findVariableDeclaration(MethodDeclaration astNode, String variableName) {
		val variableDeclaration = astNode.parameters.findFirst[(it as SingleVariableDeclaration).name.fullyQualifiedName.equals(variableName)]
		if (variableDeclaration != null)
			return (variableDeclaration as SingleVariableDeclaration).type
		else
		 	return astNode.parent.findVariableDeclaration(variableName)
	}
	
	private static def dispatch Type findVariableDeclaration(VariableDeclarationFragment astNode, String variableName) {
		if (astNode.name.fullyQualifiedName.equals(variableName)) {
			switch (astNode.parent) {
				VariableDeclarationStatement: return (astNode.parent as VariableDeclarationStatement).type
				default: new Exception ("unhandled parent for variable declaration fragment " + astNode + " " + astNode.class)
			}
		}
		
		return astNode.parent.findVariableDeclaration(variableName)
	}
	
	private static def dispatch Type findVariableDeclaration(CatchClause astNode, String variableName) {
		if (astNode.exception != null) {
			if (astNode.exception.name.fullyQualifiedName.equals(variableName)) {
				return astNode.exception.type
			}
		}
		
		astNode.parent.findVariableDeclaration(variableName)
	}
	
	private static def dispatch Type findVariableDeclaration(TypeDeclaration astNode, String variableName) {
		val field = astNode.fields.findFirst[field | field.fragments.exists[variable | (variable as VariableDeclarationFragment).name.fullyQualifiedName.equals(variableName)]]
		if (field != null)
			return field.type
		else
			return null
	}
	
	private static def dispatch Type findVariableDeclaration(ASTNode astNode, String variableName) {
		throw new Exception ("unhandled AST node type " + astNode + " " + astNode.class)
	}
}