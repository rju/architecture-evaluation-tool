package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaTypeHelper;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class HandleExpressionForMethodAndFieldAccess {
  @Extension
  private JavaTypeHelper javaTypeHelper = new JavaTypeHelper();
  
  private IScope scopes;
  
  private ModularHypergraph modularSystem;
  
  private MethodDeclaration method;
  
  public HandleExpressionForMethodAndFieldAccess(final IScope scopes) {
    this.scopes = scopes;
  }
  
  public void handle(final ModularHypergraph modularSystem, final MethodDeclaration method, final Expression expression) {
    this.modularSystem = modularSystem;
    this.method = method;
    boolean _notEquals = (!Objects.equal(expression, null));
    if (_notEquals) {
      this.findMethodAndFieldCallInExpression(expression);
    }
  }
  
  /**
   * annotation, no field or method invocation
   */
  private void _findMethodAndFieldCallInExpression(final Annotation expression) {
  }
  
  /**
   * array fields and index
   */
  private void _findMethodAndFieldCallInExpression(final ArrayAccess expression) {
    Expression _array = expression.getArray();
    this.findMethodAndFieldCallInExpression(_array);
    Expression _index = expression.getIndex();
    boolean _notEquals = (!Objects.equal(_index, null));
    if (_notEquals) {
      Expression _index_1 = expression.getIndex();
      this.findMethodAndFieldCallInExpression(_index_1);
    }
  }
  
  /**
   * array creation
   */
  private void _findMethodAndFieldCallInExpression(final ArrayCreation expression) {
    ArrayInitializer _initializer = expression.getInitializer();
    if (_initializer!=null) {
      this.findMethodAndFieldCallInExpression(_initializer);
    }
    List _dimensions = expression.dimensions();
    final Procedure1<Expression> _function = new Procedure1<Expression>() {
      public void apply(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    IterableExtensions.<Expression>forEach(_dimensions, _function);
  }
  
  /**
   * array initialization
   */
  private void _findMethodAndFieldCallInExpression(final ArrayInitializer expression) {
    List _expressions = expression.expressions();
    final Procedure1<Expression> _function = new Procedure1<Expression>() {
      public void apply(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    IterableExtensions.<Expression>forEach(_expressions, _function);
  }
  
  /**
   * assignment
   */
  private void _findMethodAndFieldCallInExpression(final Assignment expression) {
    Expression _leftHandSide = expression.getLeftHandSide();
    this.findMethodAndFieldCallInExpression(_leftHandSide);
    Expression _rightHandSide = expression.getRightHandSide();
    this.findMethodAndFieldCallInExpression(_rightHandSide);
  }
  
  /**
   * Literal no references
   */
  private void _findMethodAndFieldCallInExpression(final BooleanLiteral expression) {
  }
  
  /**
   * cast
   */
  private void _findMethodAndFieldCallInExpression(final CastExpression expression) {
    Expression _expression = expression.getExpression();
    this.findMethodAndFieldCallInExpression(_expression);
  }
  
  /**
   * Literal no references
   */
  private void _findMethodAndFieldCallInExpression(final CharacterLiteral expression) {
  }
  
  /**
   * class instance creation
   */
  private void _findMethodAndFieldCallInExpression(final ClassInstanceCreation expression) {
    Type _type = expression.getType();
    List _arguments = expression.arguments();
    final Node callee = TransformationHelper.findConstructorMethod(this.modularSystem, _type, _arguments);
    List _arguments_1 = expression.arguments();
    final Procedure1<Expression> _function = new Procedure1<Expression>() {
      public void apply(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    IterableExtensions.<Expression>forEach(_arguments_1, _function);
    Expression _expression = expression.getExpression();
    if (_expression!=null) {
      this.findMethodAndFieldCallInExpression(_expression);
    }
  }
  
  private void _findMethodAndFieldCallInExpression(final ConditionalExpression expression) {
    Expression _expression = expression.getExpression();
    if (_expression!=null) {
      this.findMethodAndFieldCallInExpression(_expression);
    }
    Expression _thenExpression = expression.getThenExpression();
    if (_thenExpression!=null) {
      this.findMethodAndFieldCallInExpression(_thenExpression);
    }
    Expression _elseExpression = expression.getElseExpression();
    if (_elseExpression!=null) {
      this.findMethodAndFieldCallInExpression(_elseExpression);
    }
  }
  
  private void _findMethodAndFieldCallInExpression(final FieldAccess expression) {
    Expression _expression = expression.getExpression();
    this.findMethodAndFieldCallInExpression(_expression);
  }
  
  private void _findMethodAndFieldCallInExpression(final InfixExpression expression) {
    Expression _leftOperand = expression.getLeftOperand();
    this.findMethodAndFieldCallInExpression(_leftOperand);
    Expression _rightOperand = expression.getRightOperand();
    this.findMethodAndFieldCallInExpression(_rightOperand);
    List _extendedOperands = expression.extendedOperands();
    final Procedure1<Expression> _function = new Procedure1<Expression>() {
      public void apply(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    IterableExtensions.<Expression>forEach(_extendedOperands, _function);
  }
  
  private void _findMethodAndFieldCallInExpression(final InstanceofExpression expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final MethodInvocation expression) {
    List _arguments = expression.arguments();
    final Procedure1<Expression> _function = new Procedure1<Expression>() {
      public void apply(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    IterableExtensions.<Expression>forEach(_arguments, _function);
    expression.getParent();
  }
  
  private void _findMethodAndFieldCallInExpression(final Name expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final NullLiteral expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final NumberLiteral expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final ParenthesizedExpression expression) {
    Expression _expression = expression.getExpression();
    this.findMethodAndFieldCallInExpression(_expression);
  }
  
  private void _findMethodAndFieldCallInExpression(final PostfixExpression expression) {
    Expression _operand = expression.getOperand();
    this.findMethodAndFieldCallInExpression(_operand);
  }
  
  private void _findMethodAndFieldCallInExpression(final PrefixExpression expression) {
    Expression _operand = expression.getOperand();
    this.findMethodAndFieldCallInExpression(_operand);
  }
  
  private void _findMethodAndFieldCallInExpression(final StringLiteral expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final SuperFieldAccess expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final SuperMethodInvocation expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final ThisExpression expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final TypeLiteral expression) {
    Type _type = expression.getType();
    final ITypeBinding binding = _type.resolveBinding();
    boolean _notEquals = (!Objects.equal(binding, null));
    if (_notEquals) {
      boolean _isClass = binding.isClass();
      if (_isClass) {
        String _qualifiedName = binding.getQualifiedName();
        this.resolveType(_qualifiedName);
      }
    }
  }
  
  private IType resolveType(final String fqn) {
    return null;
  }
  
  private void _findMethodAndFieldCallInExpression(final VariableDeclarationExpression expression) {
    List _fragments = expression.fragments();
    final Procedure1<Object> _function = new Procedure1<Object>() {
      public void apply(final Object it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInFragment(((VariableDeclarationFragment) it));
      }
    };
    IterableExtensions.<Object>forEach(_fragments, _function);
  }
  
  private void findMethodAndFieldCallInFragment(final VariableDeclarationFragment fragment) {
    Expression _initializer = fragment.getInitializer();
    this.findMethodAndFieldCallInExpression(_initializer);
  }
  
  private void _findMethodAndFieldCallInExpression(final Expression expression) {
    Class<? extends Expression> _class = expression.getClass();
    String _name = _class.getName();
    String _plus = (_name + " is not supported in expressions.");
    throw new UnsupportedOperationException(_plus);
  }
  
  private void findMethodAndFieldCallInExpression(final Expression expression) {
    if (expression instanceof Annotation) {
      _findMethodAndFieldCallInExpression((Annotation)expression);
      return;
    } else if (expression instanceof ArrayAccess) {
      _findMethodAndFieldCallInExpression((ArrayAccess)expression);
      return;
    } else if (expression instanceof ArrayCreation) {
      _findMethodAndFieldCallInExpression((ArrayCreation)expression);
      return;
    } else if (expression instanceof ArrayInitializer) {
      _findMethodAndFieldCallInExpression((ArrayInitializer)expression);
      return;
    } else if (expression instanceof Assignment) {
      _findMethodAndFieldCallInExpression((Assignment)expression);
      return;
    } else if (expression instanceof BooleanLiteral) {
      _findMethodAndFieldCallInExpression((BooleanLiteral)expression);
      return;
    } else if (expression instanceof CastExpression) {
      _findMethodAndFieldCallInExpression((CastExpression)expression);
      return;
    } else if (expression instanceof CharacterLiteral) {
      _findMethodAndFieldCallInExpression((CharacterLiteral)expression);
      return;
    } else if (expression instanceof ClassInstanceCreation) {
      _findMethodAndFieldCallInExpression((ClassInstanceCreation)expression);
      return;
    } else if (expression instanceof ConditionalExpression) {
      _findMethodAndFieldCallInExpression((ConditionalExpression)expression);
      return;
    } else if (expression instanceof FieldAccess) {
      _findMethodAndFieldCallInExpression((FieldAccess)expression);
      return;
    } else if (expression instanceof InfixExpression) {
      _findMethodAndFieldCallInExpression((InfixExpression)expression);
      return;
    } else if (expression instanceof InstanceofExpression) {
      _findMethodAndFieldCallInExpression((InstanceofExpression)expression);
      return;
    } else if (expression instanceof MethodInvocation) {
      _findMethodAndFieldCallInExpression((MethodInvocation)expression);
      return;
    } else if (expression instanceof Name) {
      _findMethodAndFieldCallInExpression((Name)expression);
      return;
    } else if (expression instanceof NullLiteral) {
      _findMethodAndFieldCallInExpression((NullLiteral)expression);
      return;
    } else if (expression instanceof NumberLiteral) {
      _findMethodAndFieldCallInExpression((NumberLiteral)expression);
      return;
    } else if (expression instanceof ParenthesizedExpression) {
      _findMethodAndFieldCallInExpression((ParenthesizedExpression)expression);
      return;
    } else if (expression instanceof PostfixExpression) {
      _findMethodAndFieldCallInExpression((PostfixExpression)expression);
      return;
    } else if (expression instanceof PrefixExpression) {
      _findMethodAndFieldCallInExpression((PrefixExpression)expression);
      return;
    } else if (expression instanceof StringLiteral) {
      _findMethodAndFieldCallInExpression((StringLiteral)expression);
      return;
    } else if (expression instanceof SuperFieldAccess) {
      _findMethodAndFieldCallInExpression((SuperFieldAccess)expression);
      return;
    } else if (expression instanceof SuperMethodInvocation) {
      _findMethodAndFieldCallInExpression((SuperMethodInvocation)expression);
      return;
    } else if (expression instanceof ThisExpression) {
      _findMethodAndFieldCallInExpression((ThisExpression)expression);
      return;
    } else if (expression instanceof TypeLiteral) {
      _findMethodAndFieldCallInExpression((TypeLiteral)expression);
      return;
    } else if (expression instanceof VariableDeclarationExpression) {
      _findMethodAndFieldCallInExpression((VariableDeclarationExpression)expression);
      return;
    } else if (expression != null) {
      _findMethodAndFieldCallInExpression(expression);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(expression).toString());
    }
  }
}
