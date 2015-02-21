package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.HandleExpressionForMethodAndFieldAccess;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaTypeHelper;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class HandleStatementForMethodAndFieldAccess {
  @Extension
  private JavaTypeHelper javaTypeHelper = new JavaTypeHelper();
  
  private final HandleExpressionForMethodAndFieldAccess expressionHandler;
  
  private IScope scopes;
  
  private ModularHypergraph modularSystem;
  
  private MethodDeclaration method;
  
  public HandleStatementForMethodAndFieldAccess(final IScope scopes) {
    this.scopes = scopes;
    HandleExpressionForMethodAndFieldAccess _handleExpressionForMethodAndFieldAccess = new HandleExpressionForMethodAndFieldAccess(scopes);
    this.expressionHandler = _handleExpressionForMethodAndFieldAccess;
  }
  
  public void handle(final ModularHypergraph modularSystem, final MethodDeclaration method, final Statement statement) {
    this.modularSystem = modularSystem;
    this.method = method;
    this.findMethodAndFieldCallInStatement(statement);
  }
  
  /**
   * Assert statement.
   * assert Expression [ : Expression ] ;
   */
  private void _findMethodAndFieldCallInStatement(final AssertStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
    Expression _message = statement.getMessage();
    boolean _notEquals = (!Objects.equal(_message, null));
    if (_notEquals) {
      Expression _message_1 = statement.getMessage();
      this.expressionHandler.handle(this.modularSystem, this.method, _message_1);
    }
  }
  
  /**
   * Block statement.
   * { seq }
   */
  private void _findMethodAndFieldCallInStatement(final Block statement) {
    List _statements = statement.statements();
    final Procedure1<Statement> _function = new Procedure1<Statement>() {
      public void apply(final Statement it) {
        HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
      }
    };
    IterableExtensions.<Statement>forEach(_statements, _function);
  }
  
  /**
   * ConstructorInvocation:
   *   [ < Type { , Type } > ]
   *                 this ( [ Expression { , Expression } ] ) ;
   */
  private void _findMethodAndFieldCallInStatement(final ConstructorInvocation statement) {
    List _arguments = statement.arguments();
    final Procedure1<Expression> _function = new Procedure1<Expression>() {
      public void apply(final Expression expression) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.method, expression);
      }
    };
    IterableExtensions.<Expression>forEach(_arguments, _function);
  }
  
  /**
   * DoStatement:
   *    do Statement while ( Expression ) ;
   */
  private void _findMethodAndFieldCallInStatement(final DoStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
    Statement _body = statement.getBody();
    this.findMethodAndFieldCallInStatement(_body);
  }
  
  /**
   * EnhancedForStatement:
   *   for ( FormalParameter : Expression )
   *                  Statement
   */
  private void _findMethodAndFieldCallInStatement(final EnhancedForStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
    Statement _body = statement.getBody();
    this.findMethodAndFieldCallInStatement(_body);
  }
  
  /**
   * ExpressionStatement:
   *     StatementExpression ;
   */
  private void _findMethodAndFieldCallInStatement(final ExpressionStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
  }
  
  /**
   * ForStatement:
   *   for (
   * 			[ ForInit ];
   * 			[ Expression ] ;
   * 			[ ForUpdate ] )
   * 			Statement
   * ForInit:
   * 		 Expression { , Expression }
   * ForUpdate:
   * 	    Expression { , Expression }
   */
  private void _findMethodAndFieldCallInStatement(final ForStatement statement) {
    List _initializers = statement.initializers();
    final Procedure1<Expression> _function = new Procedure1<Expression>() {
      public void apply(final Expression it) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.method, it);
      }
    };
    IterableExtensions.<Expression>forEach(_initializers, _function);
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      this.expressionHandler.handle(this.modularSystem, this.method, _expression_1);
    }
    List _updaters = statement.updaters();
    final Procedure1<Expression> _function_1 = new Procedure1<Expression>() {
      public void apply(final Expression it) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.method, it);
      }
    };
    IterableExtensions.<Expression>forEach(_updaters, _function_1);
    Statement _body = statement.getBody();
    this.findMethodAndFieldCallInStatement(_body);
  }
  
  /**
   * IfStatement:
   *   if ( Expression ) Statement [ else Statement]
   */
  private void _findMethodAndFieldCallInStatement(final IfStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
    Statement _thenStatement = statement.getThenStatement();
    this.findMethodAndFieldCallInStatement(_thenStatement);
    Statement _elseStatement = statement.getElseStatement();
    boolean _notEquals = (!Objects.equal(_elseStatement, null));
    if (_notEquals) {
      Statement _elseStatement_1 = statement.getElseStatement();
      this.findMethodAndFieldCallInStatement(_elseStatement_1);
    }
  }
  
  /**
   * LabeledStatement:
   *   Identifier : Statement
   */
  private void _findMethodAndFieldCallInStatement(final LabeledStatement statement) {
    Statement _body = statement.getBody();
    this.findMethodAndFieldCallInStatement(_body);
  }
  
  /**
   * ReturnStatement:
   *    return [ Expression ] ;
   */
  private void _findMethodAndFieldCallInStatement(final ReturnStatement statement) {
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      this.expressionHandler.handle(this.modularSystem, this.method, _expression_1);
    }
  }
  
  /**
   * SuperConstructorInvocation:
   *   [ Expression . ]
   *      [ < Type { , Type } > ]
   *      super ( [ Expression { , Expression } ] ) ;
   */
  private void _findMethodAndFieldCallInStatement(final SuperConstructorInvocation statement) {
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      this.expressionHandler.handle(this.modularSystem, this.method, _expression_1);
    }
    List _arguments = statement.arguments();
    final Procedure1<Expression> _function = new Procedure1<Expression>() {
      public void apply(final Expression it) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.method, it);
      }
    };
    IterableExtensions.<Expression>forEach(_arguments, _function);
  }
  
  /**
   * SwitchCase:
   *           case Expression  :
   *           default :
   */
  private void _findMethodAndFieldCallInStatement(final SwitchCase statement) {
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      this.expressionHandler.handle(this.modularSystem, this.method, _expression_1);
    }
  }
  
  /**
   * SwitchStatement:
   *           switch ( Expression )
   *                   { { SwitchCase | Statement } } }
   */
  private void _findMethodAndFieldCallInStatement(final SwitchStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
    List _statements = statement.statements();
    final Procedure1<Object> _function = new Procedure1<Object>() {
      public void apply(final Object it) {
        HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(((Statement) it));
      }
    };
    IterableExtensions.<Object>forEach(_statements, _function);
  }
  
  /**
   * SynchronizedStatement:
   *   synchronized ( Expression ) Block
   */
  private void _findMethodAndFieldCallInStatement(final SynchronizedStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
    Block _body = statement.getBody();
    List _statements = _body.statements();
    final Procedure1<Statement> _function = new Procedure1<Statement>() {
      public void apply(final Statement it) {
        HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
      }
    };
    IterableExtensions.<Statement>forEach(_statements, _function);
  }
  
  /**
   * ThrowStatement:
   *   throw Expression ;
   */
  private void _findMethodAndFieldCallInStatement(final ThrowStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
  }
  
  /**
   * TryStatement:
   *   try [ ( Resources ) ]
   *      Block
   *      [ { CatchClause } ]
   *      [ finally Block ]
   */
  private void _findMethodAndFieldCallInStatement(final TryStatement statement) {
    Block _body = statement.getBody();
    List _statements = _body.statements();
    final Procedure1<Statement> _function = new Procedure1<Statement>() {
      public void apply(final Statement it) {
        HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
      }
    };
    IterableExtensions.<Statement>forEach(_statements, _function);
    List _catchClauses = statement.catchClauses();
    final Procedure1<Object> _function_1 = new Procedure1<Object>() {
      public void apply(final Object it) {
        Block _body = ((CatchClause) it).getBody();
        List _statements = _body.statements();
        final Procedure1<Statement> _function = new Procedure1<Statement>() {
          public void apply(final Statement it) {
            HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
          }
        };
        IterableExtensions.<Statement>forEach(_statements, _function);
      }
    };
    IterableExtensions.<Object>forEach(_catchClauses, _function_1);
    Block _finally = statement.getFinally();
    boolean _notEquals = (!Objects.equal(_finally, null));
    if (_notEquals) {
      Block _finally_1 = statement.getFinally();
      List _statements_1 = _finally_1.statements();
      final Procedure1<Statement> _function_2 = new Procedure1<Statement>() {
        public void apply(final Statement it) {
          HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
        }
      };
      IterableExtensions.<Statement>forEach(_statements_1, _function_2);
    }
    List _resources = statement.resources();
    final Procedure1<Object> _function_3 = new Procedure1<Object>() {
      public void apply(final Object it) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.method, ((VariableDeclarationExpression) it));
      }
    };
    IterableExtensions.<Object>forEach(_resources, _function_3);
  }
  
  private void _findMethodAndFieldCallInStatement(final VariableDeclarationStatement statement) {
    Type _type = statement.getType();
    if ((_type instanceof SimpleType)) {
      Type _type_1 = statement.getType();
      final Name name = ((SimpleType) _type_1).getName();
      final String fqn = name.getFullyQualifiedName();
      final IType result = this.scopes.getType(fqn);
      boolean _notEquals = (!Objects.equal(result, null));
      if (_notEquals) {
        List _fragments = statement.fragments();
        final Procedure1<Object> _function = new Procedure1<Object>() {
          public void apply(final Object fragment) {
            Expression _initializer = ((VariableDeclarationFragment) fragment).getInitializer();
            HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.method, _initializer);
          }
        };
        IterableExtensions.<Object>forEach(_fragments, _function);
      }
    }
  }
  
  /**
   * WhileStatement:
   *   while ( Expression ) Statement
   */
  private void _findMethodAndFieldCallInStatement(final WhileStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.method, _expression);
    Statement _body = statement.getBody();
    this.findMethodAndFieldCallInStatement(_body);
  }
  
  /**
   * statements which do not contain code
   */
  private void _findMethodAndFieldCallInStatement(final BreakStatement statement) {
  }
  
  private void _findMethodAndFieldCallInStatement(final ContinueStatement statement) {
  }
  
  private void _findMethodAndFieldCallInStatement(final EmptyStatement statement) {
  }
  
  /**
   * TypeDeclarationStatement:
   *   TypeDeclaration
   *   EnumDeclaration
   */
  private void _findMethodAndFieldCallInStatement(final TypeDeclarationStatement statement) {
  }
  
  private void _findMethodAndFieldCallInStatement(final Statement statement) {
    Class<? extends Statement> _class = statement.getClass();
    String _name = _class.getName();
    String _plus = (_name + " is not supported as a statement.");
    throw new UnsupportedOperationException(_plus);
  }
  
  private void findMethodAndFieldCallInStatement(final Statement statement) {
    if (statement instanceof AssertStatement) {
      _findMethodAndFieldCallInStatement((AssertStatement)statement);
      return;
    } else if (statement instanceof Block) {
      _findMethodAndFieldCallInStatement((Block)statement);
      return;
    } else if (statement instanceof BreakStatement) {
      _findMethodAndFieldCallInStatement((BreakStatement)statement);
      return;
    } else if (statement instanceof ConstructorInvocation) {
      _findMethodAndFieldCallInStatement((ConstructorInvocation)statement);
      return;
    } else if (statement instanceof ContinueStatement) {
      _findMethodAndFieldCallInStatement((ContinueStatement)statement);
      return;
    } else if (statement instanceof DoStatement) {
      _findMethodAndFieldCallInStatement((DoStatement)statement);
      return;
    } else if (statement instanceof EmptyStatement) {
      _findMethodAndFieldCallInStatement((EmptyStatement)statement);
      return;
    } else if (statement instanceof EnhancedForStatement) {
      _findMethodAndFieldCallInStatement((EnhancedForStatement)statement);
      return;
    } else if (statement instanceof ExpressionStatement) {
      _findMethodAndFieldCallInStatement((ExpressionStatement)statement);
      return;
    } else if (statement instanceof ForStatement) {
      _findMethodAndFieldCallInStatement((ForStatement)statement);
      return;
    } else if (statement instanceof IfStatement) {
      _findMethodAndFieldCallInStatement((IfStatement)statement);
      return;
    } else if (statement instanceof LabeledStatement) {
      _findMethodAndFieldCallInStatement((LabeledStatement)statement);
      return;
    } else if (statement instanceof ReturnStatement) {
      _findMethodAndFieldCallInStatement((ReturnStatement)statement);
      return;
    } else if (statement instanceof SuperConstructorInvocation) {
      _findMethodAndFieldCallInStatement((SuperConstructorInvocation)statement);
      return;
    } else if (statement instanceof SwitchCase) {
      _findMethodAndFieldCallInStatement((SwitchCase)statement);
      return;
    } else if (statement instanceof SwitchStatement) {
      _findMethodAndFieldCallInStatement((SwitchStatement)statement);
      return;
    } else if (statement instanceof SynchronizedStatement) {
      _findMethodAndFieldCallInStatement((SynchronizedStatement)statement);
      return;
    } else if (statement instanceof ThrowStatement) {
      _findMethodAndFieldCallInStatement((ThrowStatement)statement);
      return;
    } else if (statement instanceof TryStatement) {
      _findMethodAndFieldCallInStatement((TryStatement)statement);
      return;
    } else if (statement instanceof TypeDeclarationStatement) {
      _findMethodAndFieldCallInStatement((TypeDeclarationStatement)statement);
      return;
    } else if (statement instanceof VariableDeclarationStatement) {
      _findMethodAndFieldCallInStatement((VariableDeclarationStatement)statement);
      return;
    } else if (statement instanceof WhileStatement) {
      _findMethodAndFieldCallInStatement((WhileStatement)statement);
      return;
    } else if (statement != null) {
      _findMethodAndFieldCallInStatement(statement);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(statement).toString());
    }
  }
}
