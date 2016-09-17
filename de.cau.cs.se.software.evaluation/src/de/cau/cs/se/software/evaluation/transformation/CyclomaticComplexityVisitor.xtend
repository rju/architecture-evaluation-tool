/***************************************************************************
 * Copyright (C) 2015 Reiner Jung
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

/**
 * Java AST visitor to compute the cyclomatic complexity.
 * 
 * @author Reiner Jung
 */
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