package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaTypeHelper;
import de.cau.cs.se.evaluation.architecture.transformation.java.ResolveExpressionForClassAccess;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
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

@SuppressWarnings("all")
public class ResolveStatementForClassAccess {
  @Extension
  private JavaTypeHelper javaTypeHelper = new JavaTypeHelper();
  
  private final ResolveExpressionForClassAccess expressionResolver;
  
  private IScope scopes;
  
  public ResolveStatementForClassAccess(final IScope scopes) {
    this.scopes = scopes;
    ResolveExpressionForClassAccess _resolveExpressionForClassAccess = new ResolveExpressionForClassAccess(scopes);
    this.expressionResolver = _resolveExpressionForClassAccess;
  }
  
  public List<IType> resolve(final Statement statement) {
    return this.findClassCallInStatement(statement);
  }
  
  /**
   * Assert statement.
   * assert Expression [ : Expression ] ;
   */
  private List<IType> _findClassCallInStatement(final AssertStatement statement) {
    Expression _expression = statement.getExpression();
    final List<IType> types = this.expressionResolver.resolve(_expression);
    Expression _message = statement.getMessage();
    boolean _notEquals = (!Objects.equal(_message, null));
    if (_notEquals) {
      Expression _message_1 = statement.getMessage();
      List<IType> _resolve = this.expressionResolver.resolve(_message_1);
      this.javaTypeHelper.addUnique(types, _resolve);
    }
    return types;
  }
  
  /**
   * Block statement.
   * { seq }
   */
  private List<IType> _findClassCallInStatement(final Block statement) {
    final List<IType> types = new ArrayList<IType>();
    List _statements = statement.statements();
    final Consumer<Statement> _function = new Consumer<Statement>() {
      public void accept(final Statement it) {
        List<IType> _findClassCallInStatement = ResolveStatementForClassAccess.this.findClassCallInStatement(it);
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
      }
    };
    _statements.forEach(_function);
    return types;
  }
  
  /**
   * ConstructorInvocation:
   *   [ < Type { , Type } > ]
   *                 this ( [ Expression { , Expression } ] ) ;
   */
  private List<IType> _findClassCallInStatement(final ConstructorInvocation statement) {
    final List<IType> types = new ArrayList<IType>();
    List _arguments = statement.arguments();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression expression) {
        List<IType> _resolve = ResolveStatementForClassAccess.this.expressionResolver.resolve(expression);
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _resolve);
      }
    };
    _arguments.forEach(_function);
    return types;
  }
  
  /**
   * DoStatement:
   *    do Statement while ( Expression ) ;
   */
  private List<IType> _findClassCallInStatement(final DoStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    Expression _expression = statement.getExpression();
    List<IType> _resolve = this.expressionResolver.resolve(_expression);
    this.javaTypeHelper.addUnique(types, _resolve);
    Statement _body = statement.getBody();
    List<IType> _findClassCallInStatement = this.findClassCallInStatement(_body);
    this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
    return types;
  }
  
  /**
   * EnhancedForStatement:
   *   for ( FormalParameter : Expression )
   *                  Statement
   */
  private List<IType> _findClassCallInStatement(final EnhancedForStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    Expression _expression = statement.getExpression();
    List<IType> _resolve = this.expressionResolver.resolve(_expression);
    this.javaTypeHelper.addUnique(types, _resolve);
    Statement _body = statement.getBody();
    List<IType> _findClassCallInStatement = this.findClassCallInStatement(_body);
    this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
    return types;
  }
  
  /**
   * ExpressionStatement:
   *     StatementExpression ;
   */
  private List<IType> _findClassCallInStatement(final ExpressionStatement statement) {
    Expression _expression = statement.getExpression();
    return this.expressionResolver.resolve(_expression);
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
  private List<IType> _findClassCallInStatement(final ForStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    List _initializers = statement.initializers();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        List<IType> _resolve = ResolveStatementForClassAccess.this.expressionResolver.resolve(it);
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _resolve);
      }
    };
    _initializers.forEach(_function);
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      List<IType> _resolve = this.expressionResolver.resolve(_expression_1);
      this.javaTypeHelper.addUnique(types, _resolve);
    }
    List _updaters = statement.updaters();
    final Consumer<Expression> _function_1 = new Consumer<Expression>() {
      public void accept(final Expression it) {
        List<IType> _resolve = ResolveStatementForClassAccess.this.expressionResolver.resolve(it);
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _resolve);
      }
    };
    _updaters.forEach(_function_1);
    Statement _body = statement.getBody();
    List<IType> _findClassCallInStatement = this.findClassCallInStatement(_body);
    this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
    return types;
  }
  
  /**
   * IfStatement:
   *   if ( Expression ) Statement [ else Statement]
   */
  private List<IType> _findClassCallInStatement(final IfStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    Expression _expression = statement.getExpression();
    List<IType> _resolve = this.expressionResolver.resolve(_expression);
    this.javaTypeHelper.addUnique(types, _resolve);
    Statement _thenStatement = statement.getThenStatement();
    List<IType> _findClassCallInStatement = this.findClassCallInStatement(_thenStatement);
    this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
    Statement _elseStatement = statement.getElseStatement();
    boolean _notEquals = (!Objects.equal(_elseStatement, null));
    if (_notEquals) {
      Statement _elseStatement_1 = statement.getElseStatement();
      List<IType> _findClassCallInStatement_1 = this.findClassCallInStatement(_elseStatement_1);
      this.javaTypeHelper.addUnique(types, _findClassCallInStatement_1);
    }
    return types;
  }
  
  /**
   * LabeledStatement:
   *   Identifier : Statement
   */
  private List<IType> _findClassCallInStatement(final LabeledStatement statement) {
    Statement _body = statement.getBody();
    return this.findClassCallInStatement(_body);
  }
  
  /**
   * ReturnStatement:
   *    return [ Expression ] ;
   */
  private List<IType> _findClassCallInStatement(final ReturnStatement statement) {
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      return this.expressionResolver.resolve(_expression_1);
    } else {
      return null;
    }
  }
  
  /**
   * SuperConstructorInvocation:
   *   [ Expression . ]
   *      [ < Type { , Type } > ]
   *      super ( [ Expression { , Expression } ] ) ;
   */
  private List<IType> _findClassCallInStatement(final SuperConstructorInvocation statement) {
    final List<IType> types = new ArrayList<IType>();
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      List<IType> _resolve = this.expressionResolver.resolve(_expression_1);
      this.javaTypeHelper.addUnique(types, _resolve);
    }
    List _arguments = statement.arguments();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        List<IType> _resolve = ResolveStatementForClassAccess.this.expressionResolver.resolve(it);
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _resolve);
      }
    };
    _arguments.forEach(_function);
    return types;
  }
  
  /**
   * SwitchCase:
   *           case Expression  :
   *           default :
   */
  private List<IType> _findClassCallInStatement(final SwitchCase statement) {
    Expression _expression = statement.getExpression();
    boolean _notEquals = (!Objects.equal(_expression, null));
    if (_notEquals) {
      Expression _expression_1 = statement.getExpression();
      return this.expressionResolver.resolve(_expression_1);
    } else {
      return null;
    }
  }
  
  /**
   * SwitchStatement:
   *           switch ( Expression )
   *                   { { SwitchCase | Statement } } }
   */
  private List<IType> _findClassCallInStatement(final SwitchStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    Expression _expression = statement.getExpression();
    List<IType> _resolve = this.expressionResolver.resolve(_expression);
    this.javaTypeHelper.addUnique(types, _resolve);
    List _statements = statement.statements();
    final Consumer<Object> _function = new Consumer<Object>() {
      public void accept(final Object it) {
        List<IType> _findClassCallInStatement = ResolveStatementForClassAccess.this.findClassCallInStatement(((Statement) it));
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
      }
    };
    _statements.forEach(_function);
    return types;
  }
  
  /**
   * SynchronizedStatement:
   *   synchronized ( Expression ) Block
   */
  private List<IType> _findClassCallInStatement(final SynchronizedStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    Expression _expression = statement.getExpression();
    List<IType> _resolve = this.expressionResolver.resolve(_expression);
    this.javaTypeHelper.addUnique(types, _resolve);
    Block _body = statement.getBody();
    List _statements = _body.statements();
    final Consumer<Statement> _function = new Consumer<Statement>() {
      public void accept(final Statement it) {
        List<IType> _findClassCallInStatement = ResolveStatementForClassAccess.this.findClassCallInStatement(it);
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
      }
    };
    _statements.forEach(_function);
    return types;
  }
  
  /**
   * ThrowStatement:
   *   throw Expression ;
   */
  private List<IType> _findClassCallInStatement(final ThrowStatement statement) {
    Expression _expression = statement.getExpression();
    return this.expressionResolver.resolve(_expression);
  }
  
  /**
   * TryStatement:
   *   try [ ( Resources ) ]
   *      Block
   *      [ { CatchClause } ]
   *      [ finally Block ]
   */
  private List<IType> _findClassCallInStatement(final TryStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    Block _body = statement.getBody();
    List _statements = _body.statements();
    final Consumer<Statement> _function = new Consumer<Statement>() {
      public void accept(final Statement it) {
        List<IType> _findClassCallInStatement = ResolveStatementForClassAccess.this.findClassCallInStatement(it);
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
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
            List<IType> _findClassCallInStatement = ResolveStatementForClassAccess.this.findClassCallInStatement(it);
            ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
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
          List<IType> _findClassCallInStatement = ResolveStatementForClassAccess.this.findClassCallInStatement(it);
          ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
        }
      };
      _statements_1.forEach(_function_2);
    }
    List _resources = statement.resources();
    final Consumer<Object> _function_3 = new Consumer<Object>() {
      public void accept(final Object it) {
        List<IType> _resolve = ResolveStatementForClassAccess.this.expressionResolver.resolve(((VariableDeclarationExpression) it));
        ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _resolve);
      }
    };
    _resources.forEach(_function_3);
    return types;
  }
  
  private List<IType> _findClassCallInStatement(final VariableDeclarationStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    Type _type = statement.getType();
    if ((_type instanceof SimpleType)) {
      Type _type_1 = statement.getType();
      final Name name = ((SimpleType) _type_1).getName();
      final String fqn = name.getFullyQualifiedName();
      final IType result = this.scopes.getType(fqn);
      boolean _notEquals = (!Objects.equal(result, null));
      if (_notEquals) {
        this.javaTypeHelper.addUnique(types, result);
        List _fragments = statement.fragments();
        final Consumer<Object> _function = new Consumer<Object>() {
          public void accept(final Object fragment) {
            Expression _initializer = ((VariableDeclarationFragment) fragment).getInitializer();
            List<IType> _resolve = ResolveStatementForClassAccess.this.expressionResolver.resolve(_initializer);
            ResolveStatementForClassAccess.this.javaTypeHelper.addUnique(types, _resolve);
          }
        };
        _fragments.forEach(_function);
      }
    }
    return types;
  }
  
  /**
   * WhileStatement:
   *   while ( Expression ) Statement
   */
  private List<IType> _findClassCallInStatement(final WhileStatement statement) {
    final List<IType> types = new ArrayList<IType>();
    Expression _expression = statement.getExpression();
    List<IType> _resolve = this.expressionResolver.resolve(_expression);
    this.javaTypeHelper.addUnique(types, _resolve);
    Statement _body = statement.getBody();
    List<IType> _findClassCallInStatement = this.findClassCallInStatement(_body);
    this.javaTypeHelper.addUnique(types, _findClassCallInStatement);
    return types;
  }
  
  /**
   * statements which do not contain code
   */
  private List<IType> _findClassCallInStatement(final BreakStatement statement) {
    return null;
  }
  
  private List<IType> _findClassCallInStatement(final ContinueStatement statement) {
    return null;
  }
  
  private List<IType> _findClassCallInStatement(final EmptyStatement statement) {
    return null;
  }
  
  /**
   * TypeDeclarationStatement:
   *   TypeDeclaration
   *   EnumDeclaration
   */
  private List<IType> _findClassCallInStatement(final TypeDeclarationStatement statement) {
    return null;
  }
  
  private List<IType> _findClassCallInStatement(final Statement statement) {
    Class<? extends Statement> _class = statement.getClass();
    String _name = _class.getName();
    String _plus = (_name + " is not supported as a statement.");
    throw new UnsupportedOperationException(_plus);
  }
  
  private List<IType> findClassCallInStatement(final Statement statement) {
    if (statement instanceof AssertStatement) {
      return _findClassCallInStatement((AssertStatement)statement);
    } else if (statement instanceof Block) {
      return _findClassCallInStatement((Block)statement);
    } else if (statement instanceof BreakStatement) {
      return _findClassCallInStatement((BreakStatement)statement);
    } else if (statement instanceof ConstructorInvocation) {
      return _findClassCallInStatement((ConstructorInvocation)statement);
    } else if (statement instanceof ContinueStatement) {
      return _findClassCallInStatement((ContinueStatement)statement);
    } else if (statement instanceof DoStatement) {
      return _findClassCallInStatement((DoStatement)statement);
    } else if (statement instanceof EmptyStatement) {
      return _findClassCallInStatement((EmptyStatement)statement);
    } else if (statement instanceof EnhancedForStatement) {
      return _findClassCallInStatement((EnhancedForStatement)statement);
    } else if (statement instanceof ExpressionStatement) {
      return _findClassCallInStatement((ExpressionStatement)statement);
    } else if (statement instanceof ForStatement) {
      return _findClassCallInStatement((ForStatement)statement);
    } else if (statement instanceof IfStatement) {
      return _findClassCallInStatement((IfStatement)statement);
    } else if (statement instanceof LabeledStatement) {
      return _findClassCallInStatement((LabeledStatement)statement);
    } else if (statement instanceof ReturnStatement) {
      return _findClassCallInStatement((ReturnStatement)statement);
    } else if (statement instanceof SuperConstructorInvocation) {
      return _findClassCallInStatement((SuperConstructorInvocation)statement);
    } else if (statement instanceof SwitchCase) {
      return _findClassCallInStatement((SwitchCase)statement);
    } else if (statement instanceof SwitchStatement) {
      return _findClassCallInStatement((SwitchStatement)statement);
    } else if (statement instanceof SynchronizedStatement) {
      return _findClassCallInStatement((SynchronizedStatement)statement);
    } else if (statement instanceof ThrowStatement) {
      return _findClassCallInStatement((ThrowStatement)statement);
    } else if (statement instanceof TryStatement) {
      return _findClassCallInStatement((TryStatement)statement);
    } else if (statement instanceof TypeDeclarationStatement) {
      return _findClassCallInStatement((TypeDeclarationStatement)statement);
    } else if (statement instanceof VariableDeclarationStatement) {
      return _findClassCallInStatement((VariableDeclarationStatement)statement);
    } else if (statement instanceof WhileStatement) {
      return _findClassCallInStatement((WhileStatement)statement);
    } else if (statement != null) {
      return _findClassCallInStatement(statement);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(statement).toString());
    }
  }
}
