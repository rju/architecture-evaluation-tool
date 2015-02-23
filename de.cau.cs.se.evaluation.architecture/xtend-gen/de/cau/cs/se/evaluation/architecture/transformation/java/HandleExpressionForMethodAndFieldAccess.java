package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper;
import de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJavaExtensions;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.jdt.core.dom.FieldDeclaration;
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
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class HandleExpressionForMethodAndFieldAccess {
  private IScope scopes;
  
  private ModularHypergraph modularSystem;
  
  private MethodDeclaration method;
  
  private TypeDeclaration clazz;
  
  public HandleExpressionForMethodAndFieldAccess(final IScope scopes) {
    this.scopes = scopes;
  }
  
  public void handle(final ModularHypergraph modularSystem, final TypeDeclaration clazz, final MethodDeclaration method, final Expression expression) {
    this.modularSystem = modularSystem;
    this.clazz = clazz;
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
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    _dimensions.forEach(_function);
  }
  
  /**
   * array initialization
   */
  private void _findMethodAndFieldCallInExpression(final ArrayInitializer expression) {
    List _expressions = expression.expressions();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    _expressions.forEach(_function);
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
    boolean _notEquals = (!Objects.equal(callee, null));
    if (_notEquals) {
      final Node caller = HypergraphJavaExtensions.findNodeForMethod(this.modularSystem, this.clazz, this.method);
      TransformationHelper.createEdgeBetweenMethods(this.modularSystem, caller, callee);
    }
    List _arguments_1 = expression.arguments();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    _arguments_1.forEach(_function);
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
    if ((_expression instanceof ThisExpression)) {
      FieldDeclaration[] _fields = this.clazz.getFields();
      final Consumer<FieldDeclaration> _function = new Consumer<FieldDeclaration>() {
        public void accept(final FieldDeclaration it) {
          try {
            List _fragments = it.fragments();
            final Function1<Object, Boolean> _function = new Function1<Object, Boolean>() {
              public Boolean apply(final Object fragment) {
                SimpleName _name = ((VariableDeclarationFragment) fragment).getName();
                String _fullyQualifiedName = _name.getFullyQualifiedName();
                SimpleName _name_1 = expression.getName();
                String _fullyQualifiedName_1 = _name_1.getFullyQualifiedName();
                return Boolean.valueOf(_fullyQualifiedName.equals(_fullyQualifiedName_1));
              }
            };
            final Object variableDecl = IterableExtensions.<Object>findFirst(_fragments, _function);
            boolean _notEquals = (!Objects.equal(variableDecl, null));
            if (_notEquals) {
              EList<Edge> _edges = HandleExpressionForMethodAndFieldAccess.this.modularSystem.getEdges();
              final Function1<Edge, Boolean> _function_1 = new Function1<Edge, Boolean>() {
                public Boolean apply(final Edge it) {
                  return Boolean.valueOf(HandleExpressionForMethodAndFieldAccess.this.checkVariable(((VariableDeclarationFragment) variableDecl), it));
                }
              };
              final Edge edge = IterableExtensions.<Edge>findFirst(_edges, _function_1);
              boolean _notEquals_1 = (!Objects.equal(edge, null));
              if (_notEquals_1) {
                final Node node = HypergraphJavaExtensions.findNodeForMethod(HandleExpressionForMethodAndFieldAccess.this.modularSystem, HandleExpressionForMethodAndFieldAccess.this.clazz, HandleExpressionForMethodAndFieldAccess.this.method);
                EList<Edge> _edges_1 = node.getEdges();
                _edges_1.add(edge);
              } else {
                String _string = variableDecl.toString();
                String _plus = ("Missing edge for variable " + _string);
                throw new Exception(_plus);
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      ((List<FieldDeclaration>)Conversions.doWrapArray(_fields)).forEach(_function);
    }
  }
  
  /**
   * Match if a given variable in jdt.dom corresponds to an edge in the hypergraph based
   * on the IField reference stored in the edge.
   */
  public boolean checkVariable(final VariableDeclarationFragment variable, final Edge edge) {
    EdgeReference _derivedFrom = edge.getDerivedFrom();
    if ((_derivedFrom instanceof FieldTrace)) {
      String _name = edge.getName();
      String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(this.clazz, variable);
      return _name.equals(_determineFullQualifiedName);
    }
    return false;
  }
  
  private void _findMethodAndFieldCallInExpression(final InfixExpression expression) {
    Expression _leftOperand = expression.getLeftOperand();
    this.findMethodAndFieldCallInExpression(_leftOperand);
    Expression _rightOperand = expression.getRightOperand();
    this.findMethodAndFieldCallInExpression(_rightOperand);
    List _extendedOperands = expression.extendedOperands();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    _extendedOperands.forEach(_function);
  }
  
  private void _findMethodAndFieldCallInExpression(final InstanceofExpression expression) {
  }
  
  private void _findMethodAndFieldCallInExpression(final MethodInvocation expression) {
    List _arguments = expression.arguments();
    final Consumer<Expression> _function = new Consumer<Expression>() {
      public void accept(final Expression it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInExpression(it);
      }
    };
    _arguments.forEach(_function);
    expression.getParent();
  }
  
  /**
   * check what name that is. If it is a variable connect to edge
   */
  private void _findMethodAndFieldCallInExpression(final Name expression) {
    EList<Edge> _edges = this.modularSystem.getEdges();
    final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
      public Boolean apply(final Edge it) {
        boolean _xblockexpression = false;
        {
          String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(HandleExpressionForMethodAndFieldAccess.this.clazz);
          String _plus = (_determineFullQualifiedName + ".");
          String _fullyQualifiedName = expression.getFullyQualifiedName();
          final String variableName = (_plus + _fullyQualifiedName);
          String _name = it.getName();
          _xblockexpression = _name.equals(variableName);
        }
        return Boolean.valueOf(_xblockexpression);
      }
    };
    final Edge edge = IterableExtensions.<Edge>findFirst(_edges, _function);
    boolean _notEquals = (!Objects.equal(edge, null));
    if (_notEquals) {
      final Node node = HypergraphJavaExtensions.findNodeForMethod(this.modularSystem, this.clazz, this.method);
      EList<Edge> _edges_1 = node.getEdges();
      _edges_1.add(edge);
    }
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
  
  /**
   * this should not be called by the dispatcher it must be resolved in variable or method access expressions
   */
  private void _findMethodAndFieldCallInExpression(final ThisExpression expression) {
    try {
      throw new Exception("Eeek ThisExpression must not be evaluated by the dispatcher");
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
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
    final Consumer<Object> _function = new Consumer<Object>() {
      public void accept(final Object it) {
        HandleExpressionForMethodAndFieldAccess.this.findMethodAndFieldCallInFragment(((VariableDeclarationFragment) it));
      }
    };
    _fragments.forEach(_function);
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
