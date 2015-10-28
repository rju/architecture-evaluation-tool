package de.cau.cs.se.software.evaluation.transformation

import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.CatchClause
import org.eclipse.jdt.core.dom.ConditionalExpression
import org.eclipse.jdt.core.dom.DoStatement
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.SwitchCase
import org.eclipse.jdt.core.dom.WhileStatement
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.InfixExpression
import org.eclipse.jdt.core.dom.LambdaExpression

class CyclomaticComplexityVisitor extends ASTVisitor {

	private var int cyclomatic = 1;
	private val String source;

	new(String source) {
		this.source = source;
	}
	
	def getCyclomatic() {
		return cyclomatic
	}
	

	override visit(CatchClause node) {
		cyclomatic++
		return true
	}

	override visit(ConditionalExpression node) {
		cyclomatic++
		return true
	}

	override visit(InfixExpression node) {
		switch (node.operator) {
			case InfixExpression.Operator.CONDITIONAL_AND: cyclomatic++
			case InfixExpression.Operator.CONDITIONAL_OR: cyclomatic++
		}
		return true
	}
	
	override visit(LambdaExpression node) {
		return true
	}

	override visit(DoStatement node) {
		cyclomatic++
		return true
	}

	override visit(ForStatement node) {
		cyclomatic++
		return true
	}

	override visit(IfStatement node) {
		cyclomatic++
		return true
	}

	override visit(SwitchCase node) {
		cyclomatic++
		return true
	}

	override visit(WhileStatement node) {
		cyclomatic++
		return true
	}

	override visit(ExpressionStatement node) {
		return false
	}
}