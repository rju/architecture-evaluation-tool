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
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.SimpleType
import de.cau.cs.se.evaluation.architecture.transformation.IScope
import org.eclipse.jdt.core.dom.CatchClause
import org.eclipse.jdt.core.dom.VariableDeclarationExpression

class ResolveStatementForClassAccess {
	
	extension JavaTypeHelper javaTypeHelper = new JavaTypeHelper()
	
	val ResolveExpressionForClassAccess expressionResolver
	
	var IScope scopes
	
	public new (IScope scopes) {
		this.scopes = scopes
		expressionResolver = new ResolveExpressionForClassAccess(scopes)
	}
	
	def List<IType> resolve(Statement statement) {
		return statement.findClassCallInStatement
	}
	
	/**
	 * Assert statement.
	 * assert Expression [ : Expression ] ;
	 */
	private dispatch def List<IType> findClassCallInStatement(AssertStatement statement) {
		val List<IType> types = expressionResolver.resolve(statement.expression)
		if (statement.message != null)
			types.addUnique(expressionResolver.resolve(statement.message))
		
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

	
	/**
	 * ConstructorInvocation:
     *   [ < Type { , Type } > ]
     *                 this ( [ Expression { , Expression } ] ) ;
	 */
	private dispatch def List<IType> findClassCallInStatement(ConstructorInvocation statement) {
		val List<IType> types = new ArrayList<IType>()
		statement.arguments.forEach[expression | types.addUnique(expressionResolver.resolve(expression))]
		
		return types
	}
	
	/**
	 * DoStatement:
     *    do Statement while ( Expression ) ;
	 */
	private dispatch def List<IType> findClassCallInStatement(DoStatement statement) {
		val List<IType> types = new ArrayList<IType>()
		
		types.addUnique(expressionResolver.resolve(statement.expression))
		types.addUnique(statement.body.findClassCallInStatement)
		
		return types
	}
	
	/**
	 * EnhancedForStatement:
	 *   for ( FormalParameter : Expression )
     *                  Statement
	 */
	private dispatch def List<IType> findClassCallInStatement(EnhancedForStatement statement) {
		val List<IType> types = new ArrayList<IType>()
		
		types.addUnique(expressionResolver.resolve(statement.expression))
		types.addUnique(statement.body.findClassCallInStatement)
		
		return types
	}
	
	/**
	 *  ExpressionStatement:
     *     StatementExpression ;
	 */
	private dispatch def List<IType> findClassCallInStatement(ExpressionStatement statement) {
		return expressionResolver.resolve(statement.expression)
	}
	
	/**
	 * ForStatement:
	 *   for (
	 *			[ ForInit ];
	 * 			[ Expression ] ;
	 *			[ ForUpdate ] )
	 *			Statement
	 * ForInit:
	 *		 Expression { , Expression }
	 * ForUpdate:
	 *	    Expression { , Expression }
	 */
	private dispatch def List<IType> findClassCallInStatement(ForStatement statement) {
		val List<IType> types = new ArrayList<IType>()
		
		statement.initializers.forEach[types.addUnique(expressionResolver.resolve(it))]
		if (statement.expression != null)
			types.addUnique(expressionResolver.resolve(statement.expression))
		statement.updaters.forEach[types.addUnique(expressionResolver.resolve(it))]
		
		types.addUnique(statement.body.findClassCallInStatement)
		
		return types
	}
	
	/**
	 * IfStatement:
     *   if ( Expression ) Statement [ else Statement]
 	 */
	private dispatch def List<IType> findClassCallInStatement(IfStatement statement) {
		val List<IType> types = new ArrayList<IType>()
		
		types.addUnique(expressionResolver.resolve(statement.expression))
		types.addUnique(statement.thenStatement.findClassCallInStatement)
		if (statement.elseStatement != null)
			types.addUnique(statement.elseStatement.findClassCallInStatement)
		
		return types
	}
	
	/**
	 * LabeledStatement:
     *   Identifier : Statement
	 */
	private dispatch def List<IType> findClassCallInStatement(LabeledStatement statement) {
		return statement.body.findClassCallInStatement
	}
	
	/**
	 * ReturnStatement:
     *    return [ Expression ] ;
	 */
	private dispatch def List<IType> findClassCallInStatement(ReturnStatement statement) {
		if (statement.expression != null)
			return expressionResolver.resolve(statement.expression)
		else
			return null
	}
	
	/**
	 * SuperConstructorInvocation:
     *   [ Expression . ]
     *      [ < Type { , Type } > ]
     *      super ( [ Expression { , Expression } ] ) ;
     */
	private dispatch def List<IType> findClassCallInStatement(SuperConstructorInvocation statement) {
		val List<IType> types = new ArrayList<IType>()
		
		if (statement.expression != null)
			types.addUnique(expressionResolver.resolve(statement.expression))
		statement.arguments.forEach[types.addUnique(expressionResolver.resolve(it))]
		
		return types
	}
	
	/**
	 *  SwitchCase:
     *           case Expression  :
     *           default :
	 */
	private dispatch def List<IType> findClassCallInStatement(SwitchCase statement) {
		if (statement.expression != null)
			return expressionResolver.resolve(statement.expression)
		else
			return null
	}
	
	/**
	 * SwitchStatement:
     *           switch ( Expression )
     *                   { { SwitchCase | Statement } } }
	 */
	private dispatch def List<IType> findClassCallInStatement(SwitchStatement statement) {
		val List<IType> types = new ArrayList<IType>()
		
		types.addUnique(expressionResolver.resolve(statement.expression))
		
		statement.statements.forEach[types.addUnique((it as Statement).findClassCallInStatement)]
		
		return types
	}
	
	/**
	 * SynchronizedStatement:
     *   synchronized ( Expression ) Block
	 */
	private dispatch def List<IType> findClassCallInStatement(SynchronizedStatement statement) {
		val List<IType> types = new ArrayList<IType>()
		
		types.addUnique(expressionResolver.resolve(statement.expression))
		
		statement.body.statements.forEach[types.addUnique(it.findClassCallInStatement)]
		
		return types
	}
	
	/**
	 * ThrowStatement:
     *   throw Expression ;
	 */
	private dispatch def List<IType> findClassCallInStatement(ThrowStatement statement) {
		return 	expressionResolver.resolve(statement.expression)
	}
	
	/**
	 * TryStatement:
     *   try [ ( Resources ) ]
     *      Block
     *      [ { CatchClause } ]
     *      [ finally Block ]
	 */
	private dispatch def List<IType> findClassCallInStatement(TryStatement statement) {
		val List<IType> types = new ArrayList<IType>()
		
		statement.body.statements.forEach[types.addUnique(it.findClassCallInStatement)]
		statement.catchClauses.forEach[(it as CatchClause).body.statements.forEach[types.addUnique(it.findClassCallInStatement)]]
		if (statement.^finally != null)
			statement.^finally.statements.forEach[types.addUnique(it.findClassCallInStatement)]
		statement.resources.forEach[types.addUnique(expressionResolver.resolve(it as VariableDeclarationExpression))]
		
		return types
	}
	
	private dispatch def List<IType> findClassCallInStatement(VariableDeclarationStatement statement) {
		val List<IType> types = new ArrayList<IType>()
		if (statement.type instanceof SimpleType) {
			val name = (statement.type as SimpleType).name
			val fqn = name.fullyQualifiedName
			val result = scopes.getType(fqn)
			if (result != null) {
				types.addUnique(result)
				statement.fragments.forEach[fragment | types.addUnique(expressionResolver.resolve((fragment as VariableDeclarationFragment).initializer))]				
			}
		}
		
		return types
	}
	
	/**
	 * WhileStatement:
     *   while ( Expression ) Statement
	 */
	private dispatch def List<IType> findClassCallInStatement(WhileStatement statement) {
		val List<IType> types = new ArrayList<IType>() 
		
		types.addUnique(expressionResolver.resolve(statement.expression))
		types.addUnique(statement.body.findClassCallInStatement)
		
		return types
	}
	
	/** statements which do not contain code */
	private dispatch def List<IType> findClassCallInStatement(BreakStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(ContinueStatement statement) {}
	private dispatch def List<IType> findClassCallInStatement(EmptyStatement statement) {}
	/**
	 * TypeDeclarationStatement:
     *   TypeDeclaration
     *   EnumDeclaration
	 */
	private dispatch def List<IType> findClassCallInStatement(TypeDeclarationStatement statement) {}
	
	
	private dispatch def List<IType> findClassCallInStatement(Statement statement) {
		throw new UnsupportedOperationException(statement.class.name + " is not supported as a statement.")
	}
}