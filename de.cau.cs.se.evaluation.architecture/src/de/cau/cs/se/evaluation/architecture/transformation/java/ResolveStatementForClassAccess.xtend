package de.cau.cs.se.evaluation.architecture.transformation.java

import java.util.List
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.AssertStatement
import org.eclipse.jdt.core.dom.Block
import java.util.ArrayList
import org.eclipse.jdt.core.dom.BreakStatement
import org.eclipse.jdt.core.dom.ConstructorInvocation
import org.eclipse.jdt.core.dom.ContinueStatement
import org.eclipse.jdt.core.dom.DoStatement
import org.eclipse.jdt.core.dom.EmptyStatement
import org.eclipse.jdt.core.dom.ExpressionStatement
import org.eclipse.jdt.core.dom.ForStatement
import org.eclipse.jdt.core.dom.IfStatement
import org.eclipse.jdt.core.dom.LabeledStatement
import org.eclipse.jdt.core.dom.ReturnStatement
import org.eclipse.jdt.core.dom.SuperConstructorInvocation
import org.eclipse.jdt.core.dom.SwitchCase
import org.eclipse.jdt.core.dom.SwitchStatement
import org.eclipse.jdt.core.dom.SynchronizedStatement
import org.eclipse.jdt.core.dom.ThrowStatement
import org.eclipse.jdt.core.dom.TryStatement
import org.eclipse.jdt.core.dom.TypeDeclarationStatement
import org.eclipse.jdt.core.dom.VariableDeclarationStatement
import org.eclipse.jdt.core.dom.WhileStatement
import org.eclipse.jdt.core.dom.EnhancedForStatement

class ResolveStatementForClassAccess {
	
	extension JavaTypeHelper javaTypeHelper
	
	val ResolveExpressionForClassAccess resolver
	
	var List<IJavaScope> scopes
	
	public new (List<IJavaScope> scopes) {
		this.scopes = scopes
		resolver = new ResolveExpressionForClassAccess(scopes)
	}
	
	def List<IType> resolve(Statement statement) {
		return statement.findClassCallInStatement
	}
	
	/**
	 * Assert statement.
	 * assert Expression [ : Expression ] ;
	 */
	private dispatch def List<IType> findClassCallInStatement(AssertStatement statement) {
		val List<IType> types = resolver.resolve(statement.expression)
		types.addUnique(resolver.resolve(statement.message))
		
		return types	
	}
		
	/**
	 * Block statement.
	 * { seq }
	 */
	private dispatch def List<IType> findClassCallInStatement(Block statement) {
		val List<IType> types = new ArrayList<IType>()
		
		statement.statements.forEach[types.addUnique(it.findClassCallInStatement)]
		
		return types
	}

	private dispatch def List<IType> findClassCallInStatement(BreakStatement statement) {
		
	}
	
	private dispatch def List<IType> findClassCallInStatement(ConstructorInvocation statement) {
		
	}
	
	private dispatch def List<IType> findClassCallInStatement(ContinueStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(DoStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(EmptyStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(ExpressionStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(ForStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(IfStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(LabeledStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(ReturnStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(SuperConstructorInvocation statement) {}
	private dispatch def List<IType> findClassCallInStatement(SwitchCase statement) {}
	private dispatch def List<IType> findClassCallInStatement(SwitchStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(SynchronizedStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(ThrowStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(TryStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(TypeDeclarationStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(VariableDeclarationStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(WhileStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(EnhancedForStatement statement) {}
	
	private dispatch def List<IType> findClassCallInStatement(Statement statement) {
		throw new UnsupportedOperationException(statement.class.name + " is not supported as a statement.")
	}
}