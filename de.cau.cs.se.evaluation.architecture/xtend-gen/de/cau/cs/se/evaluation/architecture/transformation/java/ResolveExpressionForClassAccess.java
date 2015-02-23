package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaTypeHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
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

@SuppressWarnings("all")
public class ResolveExpressionForClassAccess {
  @Extension
  private JavaTypeHelper javaTypeHelper = new JavaTypeHelper();
  
  private IScope scopes;
  
  public ResolveExpressionForClassAccess(final IScope scopes) {
    this.scopes = scopes;
  }
  
  public List<IType> resolve(final Expression expression) {
    boolean _notEquals = (!Objects.equal(expression, null));
    if (_notEquals) {
      return this.findClassCallInExpression(expression);
    } else {
      return null;
    }
  }
  
  private List<IType> _findClassCallInExpression(final Annotation expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final ArrayAccess expression) {
    Expression _array = expression.getArray();
    final List<IType> types = this.findClassCallInExpression(_array);
    List<IType> _xifexpression = null;
    Expression _index = expression.getIndex();
    boolean _notEquals = (!Objects.equal(_index, null));
    if (_notEquals) {
      Expression _index_1 = expression.getIndex();
      List<IType> _findClassCallInExpression = this.findClassCallInExpression(_index_1);
      _xifexpression = this.javaTypeHelper.addUnique(_findClassCallInExpression, types);
    } else {
      _xifexpression = types;
    }
    return _xifexpression;
  }
  
  private List<IType> _findClassCallInExpression(final ArrayCreation expression) {
    final ArrayList<IType> types = new ArrayList<IType>();
    ArrayInitializer _initializer = expression.getInitializer();
    List<IType> _findClassCallInExpression = null;
    if (_initializer!=null) {
      _findClassCallInExpression=this.findClassCallInExpression(_initializer);
    }
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
    List _dimensions = expression.dimensions();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        List<IType> _findClassCallInExpression = ResolveExpressionForClassAccess.this.findClassCallInExpression(it);
        ResolveExpressionForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
      }
    };
    _dimensions.forEach(_function);
    return types;
  }
  
  private List<IType> _findClassCallInExpression(final ArrayInitializer expression) {
    final ArrayList<IType> types = new ArrayList<IType>();
    List _expressions = expression.expressions();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        List<IType> _findClassCallInExpression = ResolveExpressionForClassAccess.this.findClassCallInExpression(it);
        ResolveExpressionForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
      }
    };
    _expressions.forEach(_function);
    return types;
  }
  
  private List<IType> _findClassCallInExpression(final Assignment expression) {
    final ArrayList<IType> types = new ArrayList<IType>();
    Expression _leftHandSide = expression.getLeftHandSide();
    List<IType> _findClassCallInExpression = this.findClassCallInExpression(_leftHandSide);
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
    Expression _rightHandSide = expression.getRightHandSide();
    List<IType> _findClassCallInExpression_1 = this.findClassCallInExpression(_rightHandSide);
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression_1);
    return types;
  }
  
  private List<IType> _findClassCallInExpression(final BooleanLiteral expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final CastExpression expression) {
    Expression _expression = expression.getExpression();
    return this.findClassCallInExpression(_expression);
  }
  
  private List<IType> _findClassCallInExpression(final CharacterLiteral expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final ClassInstanceCreation expression) {
    final ArrayList<IType> types = new ArrayList<IType>();
    List _arguments = expression.arguments();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        List<IType> _findClassCallInExpression = ResolveExpressionForClassAccess.this.findClassCallInExpression(it);
        ResolveExpressionForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
      }
    };
    _arguments.forEach(_function);
    Expression _expression = expression.getExpression();
    List<IType> _findClassCallInExpression = null;
    if (_expression!=null) {
      _findClassCallInExpression=this.findClassCallInExpression(_expression);
    }
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
    return types;
  }
  
  private List<IType> _findClassCallInExpression(final ConditionalExpression expression) {
    final ArrayList<IType> types = new ArrayList<IType>();
    Expression _expression = expression.getExpression();
    List<IType> _findClassCallInExpression = null;
    if (_expression!=null) {
      _findClassCallInExpression=this.findClassCallInExpression(_expression);
    }
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
    Expression _thenExpression = expression.getThenExpression();
    List<IType> _findClassCallInExpression_1 = null;
    if (_thenExpression!=null) {
      _findClassCallInExpression_1=this.findClassCallInExpression(_thenExpression);
    }
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression_1);
    Expression _elseExpression = expression.getElseExpression();
    List<IType> _findClassCallInExpression_2 = null;
    if (_elseExpression!=null) {
      _findClassCallInExpression_2=this.findClassCallInExpression(_elseExpression);
    }
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression_2);
    return types;
  }
  
  private List<IType> _findClassCallInExpression(final FieldAccess expression) {
    Expression _expression = expression.getExpression();
    return this.findClassCallInExpression(_expression);
  }
  
  private List<IType> _findClassCallInExpression(final InfixExpression expression) {
    final ArrayList<IType> types = new ArrayList<IType>();
    Expression _leftOperand = expression.getLeftOperand();
    List<IType> _findClassCallInExpression = this.findClassCallInExpression(_leftOperand);
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
    Expression _rightOperand = expression.getRightOperand();
    List<IType> _findClassCallInExpression_1 = this.findClassCallInExpression(_rightOperand);
    this.javaTypeHelper.addUnique(types, _findClassCallInExpression_1);
    List _extendedOperands = expression.extendedOperands();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        List<IType> _findClassCallInExpression = ResolveExpressionForClassAccess.this.findClassCallInExpression(it);
        ResolveExpressionForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
      }
    };
    _extendedOperands.forEach(_function);
    return types;
  }
  
  private List<IType> _findClassCallInExpression(final InstanceofExpression expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final MethodInvocation expression) {
    final ArrayList<IType> types = new ArrayList<IType>();
    List _arguments = expression.arguments();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        List<IType> _findClassCallInExpression = ResolveExpressionForClassAccess.this.findClassCallInExpression(it);
        ResolveExpressionForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInExpression);
      }
    };
    _arguments.forEach(_function);
    expression.getParent();
    return types;
  }
  
  private List<IType> _findClassCallInExpression(final Name expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final NullLiteral expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final NumberLiteral expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final ParenthesizedExpression expression) {
    Expression _expression = expression.getExpression();
    return this.findClassCallInExpression(_expression);
  }
  
  private List<IType> _findClassCallInExpression(final PostfixExpression expression) {
    Expression _operand = expression.getOperand();
    return this.findClassCallInExpression(_operand);
  }
  
  private List<IType> _findClassCallInExpression(final PrefixExpression expression) {
    Expression _operand = expression.getOperand();
    return this.findClassCallInExpression(_operand);
  }
  
  private List<IType> _findClassCallInExpression(final StringLiteral expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final SuperFieldAccess expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final SuperMethodInvocation expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final ThisExpression expression) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final TypeLiteral expression) {
    final List<IType> types = new ArrayList<IType>();
    Type _type = expression.getType();
    final ITypeBinding binding = _type.resolveBinding();
    boolean _notEquals = (!Objects.equal(binding, null));
    if (_notEquals) {
      boolean _isClass = binding.isClass();
      if (_isClass) {
        String _qualifiedName = binding.getQualifiedName();
        IType _resolveType = this.resolveType(_qualifiedName);
        this.javaTypeHelper.addUnique(types, _resolveType);
      }
    }
    return types;
  }
  
  private IType resolveType(final String fqn) {
    return null;
  }
  
  private List<IType> _findClassCallInExpression(final VariableDeclarationExpression expression) {
    final List<IType> types = new ArrayList<IType>();
    List _fragments = expression.fragments();
    final Consumer<Object> _function = new Consumer<Object>() {
      public void accept(final Object it) {
        List<IType> _findClassCallInFragment = ResolveExpressionForClassAccess.this.findClassCallInFragment(((VariableDeclarationFragment) it));
        ResolveExpressionForClassAccess.this.javaTypeHelper.addUnique(types, _findClassCallInFragment);
      }
    };
    _fragments.forEach(_function);
    return types;
  }
  
  private List<IType> findClassCallInFragment(final VariableDeclarationFragment fragment) {
    Expression _initializer = fragment.getInitializer();
    return this.findClassCallInExpression(_initializer);
  }
  
  private List<IType> _findClassCallInExpression(final Expression expression) {
    Class<? extends Expression> _class = expression.getClass();
    String _name = _class.getName();
    String _plus = (_name + " is not supported in expressions.");
    throw new UnsupportedOperationException(_plus);
  }
  
  private List<IType> findClassCallInExpression(final Expression expression) {
    if (expression instanceof Annotation) {
      return _findClassCallInExpression((Annotation)expression);
    } else if (expression instanceof ArrayAccess) {
      return _findClassCallInExpression((ArrayAccess)expression);
    } else if (expression instanceof ArrayCreation) {
      return _findClassCallInExpression((ArrayCreation)expression);
    } else if (expression instanceof ArrayInitializer) {
      return _findClassCallInExpression((ArrayInitializer)expression);
    } else if (expression instanceof Assignment) {
      return _findClassCallInExpression((Assignment)expression);
    } else if (expression instanceof BooleanLiteral) {
      return _findClassCallInExpression((BooleanLiteral)expression);
    } else if (expression instanceof CastExpression) {
      return _findClassCallInExpression((CastExpression)expression);
    } else if (expression instanceof CharacterLiteral) {
      return _findClassCallInExpression((CharacterLiteral)expression);
    } else if (expression instanceof ClassInstanceCreation) {
      return _findClassCallInExpression((ClassInstanceCreation)expression);
    } else if (expression instanceof ConditionalExpression) {
      return _findClassCallInExpression((ConditionalExpression)expression);
    } else if (expression instanceof FieldAccess) {
      return _findClassCallInExpression((FieldAccess)expression);
    } else if (expression instanceof InfixExpression) {
      return _findClassCallInExpression((InfixExpression)expression);
    } else if (expression instanceof InstanceofExpression) {
      return _findClassCallInExpression((InstanceofExpression)expression);
    } else if (expression instanceof MethodInvocation) {
      return _findClassCallInExpression((MethodInvocation)expression);
    } else if (expression instanceof Name) {
      return _findClassCallInExpression((Name)expression);
    } else if (expression instanceof NullLiteral) {
      return _findClassCallInExpression((NullLiteral)expression);
    } else if (expression instanceof NumberLiteral) {
      return _findClassCallInExpression((NumberLiteral)expression);
    } else if (expression instanceof ParenthesizedExpression) {
      return _findClassCallInExpression((ParenthesizedExpression)expression);
    } else if (expression instanceof PostfixExpression) {
      return _findClassCallInExpression((PostfixExpression)expression);
    } else if (expression instanceof PrefixExpression) {
      return _findClassCallInExpression((PrefixExpression)expression);
    } else if (expression instanceof StringLiteral) {
      return _findClassCallInExpression((StringLiteral)expression);
    } else if (expression instanceof SuperFieldAccess) {
      return _findClassCallInExpression((SuperFieldAccess)expression);
    } else if (expression instanceof SuperMethodInvocation) {
      return _findClassCallInExpression((SuperMethodInvocation)expression);
    } else if (expression instanceof ThisExpression) {
      return _findClassCallInExpression((ThisExpression)expression);
    } else if (expression instanceof TypeLiteral) {
      return _findClassCallInExpression((TypeLiteral)expression);
    } else if (expression instanceof VariableDeclarationExpression) {
      return _findClassCallInExpression((VariableDeclarationExpression)expression);
    } else if (expression != null) {
      return _findClassCallInExpression(expression);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(expression).toString());
    }
  }
}
