package de.cau.cs.se.evaluation.architecture.transformation.java.old

import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.AssertStatement
import org.eclipse.jdt.core.dom.Block
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
import org.eclipse.jdt.core.dom.CatchClause
import org.eclipse.jdt.core.dom.VariableDeclarationExpression
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.ParameterizedType
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.dom.PrimitiveType
import org.eclipse.jdt.core.dom.ArrayType

class HandleStatementForMethodAndFieldAccess {
		
	val HandleExpressionForMethodAndFieldAccess expressionHandler
		
	var ModularHypergraph modularSystem
	
	var MethodDeclaration method
	
	var AbstractTypeDeclaration type
	
	public new (IJavaProject project) {
		expressionHandler = new HandleExpressionForMethodAndFieldAccess(project, this)
	}
	
	def void handle(ModularHypergraph modularSystem, AbstractTypeDeclaration type, MethodDeclaration method, Statement statement) {
		this.modularSystem = modularSystem
		this.type = type
		this.method = method
		statement.findMethodAndFieldCallInStatement
	}
	
	/**
	 * Assert statement.
	 * assert Expression [ : Expression ] ;
	 */
	private dispatch def void findMethodAndFieldCallInStatement(AssertStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
		if (statement.message != null)
			expressionHandler.handle(modularSystem, type, method, statement.message)
	}
		
	/**
	 * Block statement.
	 * { seq }
	 */
	private dispatch def void findMethodAndFieldCallInStatement(Block statement) {
		statement.statements.forEach[it.findMethodAndFieldCallInStatement]
	}

	
	/**
	 * ConstructorInvocation:
     *   [ < Type { , Type } > ]
     *                 this ( [ Expression { , Expression } ] ) ;
	 */
	private dispatch def void findMethodAndFieldCallInStatement(ConstructorInvocation statement) {
		statement.arguments.forEach[expression | expressionHandler.handle(modularSystem, type, method, expression)]
	}
	
	/**
	 * DoStatement:
     *    do Statement while ( Expression ) ;
	 */
	private dispatch def void findMethodAndFieldCallInStatement(DoStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
		statement.body.findMethodAndFieldCallInStatement
	}
	
	/**
	 * EnhancedForStatement:
	 *   for ( FormalParameter : Expression )
     *                  Statement
	 */
	private dispatch def void findMethodAndFieldCallInStatement(EnhancedForStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
		statement.body.findMethodAndFieldCallInStatement
	}
	
	/**
	 *  ExpressionStatement:
     *     StatementExpression ;
	 */
	private dispatch def void findMethodAndFieldCallInStatement(ExpressionStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
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
	private dispatch def void findMethodAndFieldCallInStatement(ForStatement statement) {
		statement.initializers.forEach[expressionHandler.handle(modularSystem, type, method, it)]
		if (statement.expression != null)
			expressionHandler.handle(modularSystem, type, method, statement.expression)
		statement.updaters.forEach[expressionHandler.handle(modularSystem, type, method, it)]
		
		statement.body.findMethodAndFieldCallInStatement
	}
	
	/**
	 * IfStatement:
     *   if ( Expression ) Statement [ else Statement]
 	 */
	private dispatch def void findMethodAndFieldCallInStatement(IfStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
		statement.thenStatement.findMethodAndFieldCallInStatement
		if (statement.elseStatement != null)
			statement.elseStatement.findMethodAndFieldCallInStatement
	}
	
	/**
	 * LabeledStatement:
     *   Identifier : Statement
	 */
	private dispatch def void findMethodAndFieldCallInStatement(LabeledStatement statement) {
		statement.body.findMethodAndFieldCallInStatement
	}
	
	/**
	 * ReturnStatement:
     *    return [ Expression ] ;
	 */
	private dispatch def void findMethodAndFieldCallInStatement(ReturnStatement statement) {
		if (statement.expression != null) 
			expressionHandler.handle(modularSystem, type, method, statement.expression)
	}
	
	/**
	 * SuperConstructorInvocation:
     *   [ Expression . ]
     *      [ < Type { , Type } > ]
     *      super ( [ Expression { , Expression } ] ) ;
     */
	private dispatch def void findMethodAndFieldCallInStatement(SuperConstructorInvocation statement) {
		if (statement.expression != null)
			expressionHandler.handle(modularSystem, type, method, statement.expression)
		statement.arguments.forEach[expressionHandler.handle(modularSystem, type, method, it)]
	}
	
	/**
	 *  SwitchCase:
     *           case Expression  :
     *           default :
	 */
	private dispatch def void findMethodAndFieldCallInStatement(SwitchCase statement) {
		if (statement.expression != null)
			expressionHandler.handle(modularSystem, type, method, statement.expression)
	}
	
	/**
	 * SwitchStatement:
     *           switch ( Expression )
     *                   { { SwitchCase | Statement } } }
	 */
	private dispatch def void findMethodAndFieldCallInStatement(SwitchStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
		
		statement.statements.forEach[(it as Statement).findMethodAndFieldCallInStatement]
	}
	
	/**
	 * SynchronizedStatement:
     *   synchronized ( Expression ) Block
	 */
	private dispatch def void findMethodAndFieldCallInStatement(SynchronizedStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
		
		statement.body.statements.forEach[it.findMethodAndFieldCallInStatement]
	}
	
	/**
	 * ThrowStatement:
     *   throw Expression ;
	 */
	private dispatch def void findMethodAndFieldCallInStatement(ThrowStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
	}
	
	/**
	 * TryStatement:
     *   try [ ( Resources ) ]
     *      Block
     *      [ { CatchClause } ]
     *      [ finally Block ]
	 */
	private dispatch def void findMethodAndFieldCallInStatement(TryStatement statement) {
		statement.body.statements.forEach[it.findMethodAndFieldCallInStatement]
		statement.catchClauses.forEach[(it as CatchClause).body.statements.forEach[it.findMethodAndFieldCallInStatement]]
		if (statement.^finally != null)
			statement.^finally.statements.forEach[it.findMethodAndFieldCallInStatement]
		statement.resources.forEach[expressionHandler.handle(modularSystem, type, method, it as VariableDeclarationExpression)]
	}
	
	/**
	 * Process internal variable declarations. They do not produce edges.
	 */
	private dispatch def void findMethodAndFieldCallInStatement(VariableDeclarationStatement statement) {
		if (statement.type instanceof ArrayType) {
			if ((statement.type as ArrayType).elementType instanceof SimpleType ||
				(statement.type as ArrayType).elementType instanceof ParameterizedType) {
				statement.fragments.forEach[fragment | 
					expressionHandler.handle(modularSystem, type, method,
						(fragment as VariableDeclarationFragment).initializer
					)
				]
			}
		} else if (statement.type instanceof SimpleType ||
			statement.type instanceof ParameterizedType) {
			statement.fragments.forEach[fragment | 
				expressionHandler.handle(modularSystem, type, method,
					(fragment as VariableDeclarationFragment).initializer
				)
			]
		} else if (statement.type instanceof PrimitiveType) {
			System.out.println("primitive type")	
		} else {
			throw new Exception ("Variable declaration type is not supported " + statement.type.class)
		}
	}
	
	/**
	 * WhileStatement:
     *   while ( Expression ) Statement
	 */
	private dispatch def void findMethodAndFieldCallInStatement(WhileStatement statement) {
		expressionHandler.handle(modularSystem, type, method, statement.expression)
		statement.body.findMethodAndFieldCallInStatement
	}
	
	/** statements which do not contain code */
	private dispatch def void findMethodAndFieldCallInStatement(BreakStatement statement) {}
	private dispatch def void findMethodAndFieldCallInStatement(ContinueStatement statement) {}
	private dispatch def void findMethodAndFieldCallInStatement(EmptyStatement statement) {}

	/**
	 * TypeDeclarationStatement:
     *   TypeDeclaration
     *   EnumDeclaration
	 */
	private dispatch def void findMethodAndFieldCallInStatement(TypeDeclarationStatement statement) {}
		
	private dispatch def void findMethodAndFieldCallInStatement(Statement statement) {
		throw new UnsupportedOperationException(statement.class.name + " is not supported as a statement.")
	}
}