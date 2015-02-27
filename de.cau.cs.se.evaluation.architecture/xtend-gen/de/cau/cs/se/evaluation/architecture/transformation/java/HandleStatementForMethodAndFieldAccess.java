package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.transformation.java.HandleExpressionForMethodAndFieldAccess;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
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
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
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
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class HandleStatementForMethodAndFieldAccess {
  private final HandleExpressionForMethodAndFieldAccess expressionHandler;
  
  private ModularHypergraph modularSystem;
  
  private MethodDeclaration method;
  
  private AbstractTypeDeclaration type;
  
  public HandleStatementForMethodAndFieldAccess(final IJavaProject project) {
    HandleExpressionForMethodAndFieldAccess _handleExpressionForMethodAndFieldAccess = new HandleExpressionForMethodAndFieldAccess(project, this);
    this.expressionHandler = _handleExpressionForMethodAndFieldAccess;
  }
  
  public void handle(final ModularHypergraph modularSystem, final AbstractTypeDeclaration type, final MethodDeclaration method, final Statement statement) {
    this.modularSystem = modularSystem;
    this.type = type;
    this.method = method;
    this.findMethodAndFieldCallInStatement(statement);
  }
  
  /**
   * Assert statement.
   * assert Expression [ : Expression ] ;
   */
  private void _findMethodAndFieldCallInStatement(final AssertStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
    Expression _message = statement.getMessage();
    boolean _notEquals = (!Objects.equal(_message, null));
    if (_notEquals) {
      Expression _message_1 = statement.getMessage();
      this.expressionHandler.handle(this.modularSystem, this.type, this.method, _message_1);
    }
  }
  
  /**
   * Block statement.
   * { seq }
   */
  private void _findMethodAndFieldCallInStatement(final Block statement) {
    List _statements = statement.statements();
    final Consumer<Statement> _function = new Consumer<Statement>() {
      public void accept(final Statement it) {
        HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
      }
    };
    _statements.forEach(_function);
  }
  
  /**
   * ConstructorInvocation:
   *   [ < Type { , Type } > ]
   *                 this ( [ Expression { , Expression } ] ) ;
   */
  private void _findMethodAndFieldCallInStatement(final ConstructorInvocation statement) {
    List _arguments = statement.arguments();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression expression) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.type, HandleStatementForMethodAndFieldAccess.this.method, expression);
      }
    };
    _arguments.forEach(_function);
  }
  
  /**
   * DoStatement:
   *    do Statement while ( Expression ) ;
   */
  private void _findMethodAndFieldCallInStatement(final DoStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
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
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
    Statement _body = statement.getBody();
    this.findMethodAndFieldCallInStatement(_body);
  }
  
  /**
   * ExpressionStatement:
   *     StatementExpression ;
   */
  private void _findMethodAndFieldCallInStatement(final ExpressionStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
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
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.type, HandleStatementForMethodAndFieldAccess.this.method, it);
      }
    };
    _initializers.forEach(_function);
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression_1);
    }
    List _updaters = statement.updaters();
    final Consumer<Expression> _function_1 = new Consumer<Expression>() {
      public void accept(final Expression it) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.type, HandleStatementForMethodAndFieldAccess.this.method, it);
      }
    };
    _updaters.forEach(_function_1);
    Statement _body = statement.getBody();
    this.findMethodAndFieldCallInStatement(_body);
  }
  
  /**
   * IfStatement:
   *   if ( Expression ) Statement [ else Statement]
   */
  private void _findMethodAndFieldCallInStatement(final IfStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
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
      this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression_1);
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
      this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression_1);
    }
    List _arguments = statement.arguments();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.type, HandleStatementForMethodAndFieldAccess.this.method, it);
      }
    };
    _arguments.forEach(_function);
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
      this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression_1);
    }
  }
  
  /**
   * SwitchStatement:
   *           switch ( Expression )
   *                   { { SwitchCase | Statement } } }
   */
  private void _findMethodAndFieldCallInStatement(final SwitchStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
    List _statements = statement.statements();
    final Consumer<Object> _function = new Consumer<Object>() {
      public void accept(final Object it) {
        HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(((Statement) it));
      }
    };
    _statements.forEach(_function);
  }
  
  /**
   * SynchronizedStatement:
   *   synchronized ( Expression ) Block
   */
  private void _findMethodAndFieldCallInStatement(final SynchronizedStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
    Block _body = statement.getBody();
    List _statements = _body.statements();
    final Consumer<Statement> _function = new Consumer<Statement>() {
      public void accept(final Statement it) {
        HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
      }
    };
    _statements.forEach(_function);
  }
  
  /**
   * ThrowStatement:
   *   throw Expression ;
   */
  private void _findMethodAndFieldCallInStatement(final ThrowStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
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
    final Consumer<Statement> _function = new Consumer<Statement>() {
      public void accept(final Statement it) {
        HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
      }
    };
    _statements.forEach(_function);
    List _catchClauses = statement.catchClauses();
    final Consumer<Object> _function_1 = new Consumer<Object>() {
      public void accept(final Object it) {
        Block _body = ((CatchClause) it).getBody();
        List _statements = _body.statements();
        final Consumer<Statement> _function = new Consumer<Statement>() {
          public void accept(final Statement it) {
            HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
          }
        };
        _statements.forEach(_function);
      }
    };
    _catchClauses.forEach(_function_1);
    Block _finally = statement.getFinally();
    boolean _notEquals = (!Objects.equal(_finally, null));
    if (_notEquals) {
      Block _finally_1 = statement.getFinally();
      List _statements_1 = _finally_1.statements();
      final Consumer<Statement> _function_2 = new Consumer<Statement>() {
        public void accept(final Statement it) {
          HandleStatementForMethodAndFieldAccess.this.findMethodAndFieldCallInStatement(it);
        }
      };
      _statements_1.forEach(_function_2);
    }
    List _resources = statement.resources();
    final Consumer<Object> _function_3 = new Consumer<Object>() {
      public void accept(final Object it) {
        HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.type, HandleStatementForMethodAndFieldAccess.this.method, ((VariableDeclarationExpression) it));
      }
    };
    _resources.forEach(_function_3);
  }
  
  /**
   * Process internal variable declarations. They do not produce edges.
   */
  private void _findMethodAndFieldCallInStatement(final VariableDeclarationStatement statement) {
    try {
      Type _type = statement.getType();
      if ((_type instanceof ArrayType)) {
        boolean _or = false;
        Type _type_1 = statement.getType();
        Type _elementType = ((ArrayType) _type_1).getElementType();
        if ((_elementType instanceof SimpleType)) {
          _or = true;
        } else {
          Type _type_2 = statement.getType();
          Type _elementType_1 = ((ArrayType) _type_2).getElementType();
          _or = (_elementType_1 instanceof ParameterizedType);
        }
        if (_or) {
          List _fragments = statement.fragments();
          final Consumer<Object> _function = new Consumer<Object>() {
            public void accept(final Object fragment) {
              Expression _initializer = ((VariableDeclarationFragment) fragment).getInitializer();
              HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.type, HandleStatementForMethodAndFieldAccess.this.method, _initializer);
            }
          };
          _fragments.forEach(_function);
        }
      } else {
        boolean _or_1 = false;
        Type _type_3 = statement.getType();
        if ((_type_3 instanceof SimpleType)) {
          _or_1 = true;
        } else {
          Type _type_4 = statement.getType();
          _or_1 = (_type_4 instanceof ParameterizedType);
        }
        if (_or_1) {
          List _fragments_1 = statement.fragments();
          final Consumer<Object> _function_1 = new Consumer<Object>() {
            public void accept(final Object fragment) {
              Expression _initializer = ((VariableDeclarationFragment) fragment).getInitializer();
              HandleStatementForMethodAndFieldAccess.this.expressionHandler.handle(HandleStatementForMethodAndFieldAccess.this.modularSystem, HandleStatementForMethodAndFieldAccess.this.type, HandleStatementForMethodAndFieldAccess.this.method, _initializer);
            }
          };
          _fragments_1.forEach(_function_1);
        } else {
          Type _type_5 = statement.getType();
          if ((_type_5 instanceof PrimitiveType)) {
            System.out.println("primitive type");
          } else {
            Type _type_6 = statement.getType();
            Class<? extends Type> _class = _type_6.getClass();
            String _plus = ("Variable declaration type is not supported " + _class);
            throw new Exception(_plus);
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * WhileStatement:
   *   while ( Expression ) Statement
   */
  private void _findMethodAndFieldCallInStatement(final WhileStatement statement) {
    Expression _expression = statement.getExpression();
    this.expressionHandler.handle(this.modularSystem, this.type, this.method, _expression);
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
