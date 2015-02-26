package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper;
import de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJavaExtension;
import de.cau.cs.se.evaluation.architecture.transformation.java.VariableDeclarationResolver;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
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
  private ModularHypergraph modularSystem;
  
  private MethodDeclaration method;
  
  private AbstractTypeDeclaration type;
  
  private IJavaProject project;
  
  public HandleExpressionForMethodAndFieldAccess(final IJavaProject project) {
    this.project = project;
  }
  
  /**
   * Central handler for expressions.
   * 
   * @param modularSystem the hypergraph
   * @param clazz the present class declaration in JDT DOM
   * @param method one method from that class declaration
   * @param the expression to evaluate
   */
  public void handle(final ModularHypergraph modularSystem, final AbstractTypeDeclaration type, final MethodDeclaration method, final Expression expression) {
    this.modularSystem = modularSystem;
    this.type = type;
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
   * cast TODO
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
    final Node callee = HypergraphJavaExtension.findConstructorMethod(this.modularSystem, _type, _arguments);
    boolean _notEquals = (!Objects.equal(callee, null));
    if (_notEquals) {
      final Node caller = HypergraphJavaExtension.findNodeForMethod(this.modularSystem, this.type, this.method);
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
  
  /**
   * If then else - conditional expression.
   */
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
  
  /**
   * Field access.
   */
  private void _findMethodAndFieldCallInExpression(final FieldAccess expression) {
    Expression _expression = expression.getExpression();
    if ((_expression instanceof ThisExpression)) {
      Iterable<FieldDeclaration> _fields = this.getFields(this.type);
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
                final Node node = HypergraphJavaExtension.findNodeForMethod(HandleExpressionForMethodAndFieldAccess.this.modularSystem, HandleExpressionForMethodAndFieldAccess.this.type, HandleExpressionForMethodAndFieldAccess.this.method);
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
      _fields.forEach(_function);
    }
  }
  
  /**
   * Match if a given variable in jdt.dom corresponds to an edge in the hypergraph based
   * on the FieldDeclaration? reference stored in the edge.
   */
  public boolean checkVariable(final VariableDeclarationFragment variable, final Edge edge) {
    EdgeReference _derivedFrom = edge.getDerivedFrom();
    if ((_derivedFrom instanceof FieldTrace)) {
      String _name = edge.getName();
      String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(this.type, variable);
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
    SimpleName _name = expression.getName();
    final String methodName = _name.getFullyQualifiedName();
    Expression _expression = null;
    if (expression!=null) {
      _expression=expression.getExpression();
    }
    final Expression variable = _expression;
    boolean _equals = Objects.equal(variable, null);
    if (_equals) {
      final Node caller = HypergraphJavaExtension.findNodeForMethod(this.modularSystem, this.type, this.method);
      List _arguments_1 = expression.arguments();
      final Node callee = HypergraphJavaExtension.findNodeForMethod(this.modularSystem, this.type, methodName, _arguments_1);
      boolean _notEquals = (!Objects.equal(callee, null));
      if (_notEquals) {
        TransformationHelper.createEdgeBetweenMethods(this.modularSystem, caller, callee);
      }
    } else {
      final Node caller_1 = HypergraphJavaExtension.findNodeForMethod(this.modularSystem, this.type, this.method);
      boolean _matched = false;
      if (!_matched) {
        if (variable instanceof SimpleName) {
          boolean _isClassStaticEnvocation = this.isClassStaticEnvocation(((SimpleName)variable));
          boolean _not = (!_isClassStaticEnvocation);
          if (_not) {
            _matched=true;
            String _fullyQualifiedName = ((SimpleName)variable).getFullyQualifiedName();
            final Edge edge = HypergraphJavaExtension.findEdgeForVariable(this.modularSystem, this.type, _fullyQualifiedName);
            boolean _equals_1 = Objects.equal(edge, null);
            if (_equals_1) {
              Type _findVariableDeclaration = VariableDeclarationResolver.findVariableDeclaration(((SimpleName)variable));
              final SimpleType localType = this.resolveBaseType(_findVariableDeclaration);
              ASTNode _parent = localType.getParent();
              CompilationUnit _findCompilationUnit = this.findCompilationUnit(_parent);
              List _imports = _findCompilationUnit.imports();
              final Function1<Object, Boolean> _function_1 = new Function1<Object, Boolean>() {
                public Boolean apply(final Object classImport) {
                  Name _name = ((ImportDeclaration) classImport).getName();
                  String _fullyQualifiedName = _name.getFullyQualifiedName();
                  Name _name_1 = localType.getName();
                  String _fullyQualifiedName_1 = _name_1.getFullyQualifiedName();
                  return Boolean.valueOf(_fullyQualifiedName.endsWith(_fullyQualifiedName_1));
                }
              };
              final Object classImport = IterableExtensions.<Object>findFirst(_imports, _function_1);
              Module _xifexpression = null;
              boolean _notEquals_1 = (!Objects.equal(classImport, null));
              if (_notEquals_1) {
                Name _name_1 = ((ImportDeclaration) classImport).getName();
                String _fullyQualifiedName_1 = _name_1.getFullyQualifiedName();
                _xifexpression = this.findCorrespondingModule(_fullyQualifiedName_1);
              } else {
                ASTNode _parent_1 = this.type.getParent();
                PackageDeclaration _package = ((CompilationUnit) _parent_1).getPackage();
                Name _name_2 = _package.getName();
                String _fullyQualifiedName_2 = _name_2.getFullyQualifiedName();
                String _plus = (_fullyQualifiedName_2 + ".");
                Name _name_3 = ((SimpleType) localType).getName();
                String _fullyQualifiedName_3 = _name_3.getFullyQualifiedName();
                String _plus_1 = (_plus + _fullyQualifiedName_3);
                _xifexpression = this.findCorrespondingModule(_plus_1);
              }
              final Module module = _xifexpression;
              boolean _notEquals_2 = (!Objects.equal(module, null));
              if (_notEquals_2) {
                EList<Node> _nodes = module.getNodes();
                final Function1<Node, Boolean> _function_2 = new Function1<Node, Boolean>() {
                  public Boolean apply(final Node node) {
                    String _name = node.getName();
                    String _name_1 = module.getName();
                    String _plus = (_name_1 + ".");
                    String _plus_1 = (_plus + methodName);
                    return Boolean.valueOf(_name.equals(_plus_1));
                  }
                };
                final Node callee_1 = IterableExtensions.<Node>findFirst(_nodes, _function_2);
                TransformationHelper.createEdgeBetweenMethods(this.modularSystem, caller_1, callee_1);
              }
            } else {
              EdgeReference _derivedFrom = edge.getDerivedFrom();
              Object _field = ((FieldTrace) _derivedFrom).getField();
              SimpleName _name_4 = ((VariableDeclarationFragment) _field).getName();
              final String fieldName = _name_4.getFullyQualifiedName();
              Iterable<FieldDeclaration> _fields = this.getFields(this.type);
              final Function1<FieldDeclaration, Boolean> _function_3 = new Function1<FieldDeclaration, Boolean>() {
                public Boolean apply(final FieldDeclaration field) {
                  List _fragments = field.fragments();
                  final Function1<Object, Boolean> _function = new Function1<Object, Boolean>() {
                    public Boolean apply(final Object frag) {
                      SimpleName _name = ((VariableDeclarationFragment) frag).getName();
                      String _fullyQualifiedName = _name.getFullyQualifiedName();
                      return Boolean.valueOf(_fullyQualifiedName.equals(fieldName));
                    }
                  };
                  return Boolean.valueOf(IterableExtensions.<Object>exists(_fragments, _function));
                }
              };
              final FieldDeclaration field = IterableExtensions.<FieldDeclaration>findFirst(_fields, _function_3);
              ASTNode _parent_2 = this.type.getParent();
              List _imports_1 = ((CompilationUnit) _parent_2).imports();
              final Function1<Object, Boolean> _function_4 = new Function1<Object, Boolean>() {
                public Boolean apply(final Object classImport) {
                  Name _name = ((ImportDeclaration) classImport).getName();
                  String _fullyQualifiedName = _name.getFullyQualifiedName();
                  Type _type = field.getType();
                  Name _name_1 = ((SimpleType) _type).getName();
                  String _fullyQualifiedName_1 = _name_1.getFullyQualifiedName();
                  return Boolean.valueOf(_fullyQualifiedName.endsWith(_fullyQualifiedName_1));
                }
              };
              final Object classImport_1 = IterableExtensions.<Object>findFirst(_imports_1, _function_4);
              Module _xifexpression_1 = null;
              boolean _notEquals_3 = (!Objects.equal(classImport_1, null));
              if (_notEquals_3) {
                Name _name_5 = ((ImportDeclaration) classImport_1).getName();
                String _fullyQualifiedName_4 = _name_5.getFullyQualifiedName();
                _xifexpression_1 = this.findCorrespondingModule(_fullyQualifiedName_4);
              } else {
                ASTNode _parent_3 = this.type.getParent();
                PackageDeclaration _package_1 = ((CompilationUnit) _parent_3).getPackage();
                Name _name_6 = _package_1.getName();
                String _fullyQualifiedName_5 = _name_6.getFullyQualifiedName();
                String _plus_2 = (_fullyQualifiedName_5 + ".");
                Type _type = field.getType();
                Name _name_7 = ((SimpleType) _type).getName();
                String _fullyQualifiedName_6 = _name_7.getFullyQualifiedName();
                String _plus_3 = (_plus_2 + _fullyQualifiedName_6);
                _xifexpression_1 = this.findCorrespondingModule(_plus_3);
              }
              final Module module_1 = _xifexpression_1;
              EList<Node> _nodes_1 = module_1.getNodes();
              final Function1<Node, Boolean> _function_5 = new Function1<Node, Boolean>() {
                public Boolean apply(final Node node) {
                  String _name = node.getName();
                  String _name_1 = module_1.getName();
                  String _plus = (_name_1 + ".");
                  String _plus_1 = (_plus + methodName);
                  return Boolean.valueOf(_name.equals(_plus_1));
                }
              };
              final Node callee_2 = IterableExtensions.<Node>findFirst(_nodes_1, _function_5);
              EList<Edge> _edges = caller_1.getEdges();
              boolean _contains = _edges.contains(edge);
              boolean _not_1 = (!_contains);
              if (_not_1) {
                EList<Edge> _edges_1 = caller_1.getEdges();
                _edges_1.add(edge);
              }
              EList<Edge> _edges_2 = callee_2.getEdges();
              boolean _contains_1 = _edges_2.contains(edge);
              boolean _not_2 = (!_contains_1);
              if (_not_2) {
                EList<Edge> _edges_3 = callee_2.getEdges();
                _edges_3.add(edge);
              }
            }
          }
        }
      }
      if (!_matched) {
        if (variable instanceof SimpleName) {
          boolean _isClassStaticEnvocation = this.isClassStaticEnvocation(((SimpleName)variable));
          if (_isClassStaticEnvocation) {
            _matched=true;
          }
        }
      }
    }
  }
  
  public CompilationUnit findCompilationUnit(final ASTNode astNode) {
    if ((astNode instanceof CompilationUnit)) {
      return ((CompilationUnit) astNode);
    } else {
      ASTNode _parent = astNode.getParent();
      return this.findCompilationUnit(_parent);
    }
  }
  
  public SimpleType resolveBaseType(final Type type) {
    SimpleType _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (type instanceof SimpleType) {
        _matched=true;
        _switchResult = ((SimpleType)type);
      }
    }
    if (!_matched) {
      if (type instanceof ParameterizedType) {
        _matched=true;
        Type _type = ((ParameterizedType)type).getType();
        _switchResult = this.resolveBaseType(_type);
      }
    }
    if (!_matched) {
      Class<? extends Type> _class = type.getClass();
      String _plus = ("type kind " + _class);
      String _plus_1 = (_plus + " unkown to implementation.");
      throw new UnsupportedOperationException(_plus_1);
    }
    return _switchResult;
  }
  
  public boolean isClassStaticEnvocation(final SimpleName name) {
    ASTNode _parent = this.type.getParent();
    List _imports = ((CompilationUnit) _parent).imports();
    final Function1<Object, Boolean> _function = new Function1<Object, Boolean>() {
      public Boolean apply(final Object classImport) {
        Name _name = ((ImportDeclaration) classImport).getName();
        String _fullyQualifiedName = _name.getFullyQualifiedName();
        String _fullyQualifiedName_1 = name.getFullyQualifiedName();
        return Boolean.valueOf(_fullyQualifiedName.endsWith(_fullyQualifiedName_1));
      }
    };
    boolean _exists = IterableExtensions.<Object>exists(_imports, _function);
    if (_exists) {
      return true;
    } else {
      ASTNode _parent_1 = this.type.getParent();
      PackageDeclaration _package = ((CompilationUnit) _parent_1).getPackage();
      Name _name = _package.getName();
      final String packageName = _name.getFullyQualifiedName();
      EList<Module> _modules = this.modularSystem.getModules();
      final Function1<Module, Boolean> _function_1 = new Function1<Module, Boolean>() {
        public Boolean apply(final Module module) {
          String _fullyQualifiedName = name.getFullyQualifiedName();
          String _plus = ((packageName + ".") + _fullyQualifiedName);
          return Boolean.valueOf(name.equals(_plus));
        }
      };
      return IterableExtensions.<Module>exists(_modules, _function_1);
    }
  }
  
  public Module findCorrespondingModule(final String fqn) {
    EList<Module> _modules = this.modularSystem.getModules();
    final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
      public Boolean apply(final Module module) {
        String _name = module.getName();
        return Boolean.valueOf(_name.equals(fqn));
      }
    };
    return IterableExtensions.<Module>findFirst(_modules, _function);
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
          String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(HandleExpressionForMethodAndFieldAccess.this.type);
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
      final Node node = HypergraphJavaExtension.findNodeForMethod(this.modularSystem, this.type, this.method);
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
  }
  
  /**
   * Access the class property of a class. This is ignored at the moment.
   * TODO make this a node creation for the specific module representing the class and add
   * a caller::callee edge to the modular system for it.
   */
  private void _findMethodAndFieldCallInExpression(final TypeLiteral expression) {
  }
  
  /**
   * Create edges for variables if necessary.
   */
  private void _findMethodAndFieldCallInExpression(final VariableDeclarationExpression expression) {
    List _fragments = expression.fragments();
    final Consumer<Object> _function = new Consumer<Object>() {
      public void accept(final Object it) {
        HandleExpressionForMethodAndFieldAccess.this.processVariableDeclarationFragment(((VariableDeclarationFragment) it));
      }
    };
    _fragments.forEach(_function);
  }
  
  private void processVariableDeclarationFragment(final VariableDeclarationFragment fragment) {
    Expression _initializer = fragment.getInitializer();
    this.findMethodAndFieldCallInExpression(_initializer);
  }
  
  /**
   * Catcher for all expression types not handled above. As this is a serious error in the implementation. Throw an error.
   */
  private void _findMethodAndFieldCallInExpression(final Expression expression) {
    Class<? extends Expression> _class = expression.getClass();
    String _name = _class.getName();
    String _plus = ("Coder forgot to implement support for class " + _name);
    String _plus_1 = (_plus + " therefore it is not a supported expression type.");
    throw new UnsupportedOperationException(_plus_1);
  }
  
  /**
   * -- special service functions --
   */
  public Iterable<FieldDeclaration> getFields(final AbstractTypeDeclaration type) {
    Iterable<FieldDeclaration> _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (type instanceof TypeDeclaration) {
        _matched=true;
        FieldDeclaration[] _fields = ((TypeDeclaration)type).getFields();
        _switchResult = Iterables.<FieldDeclaration>filter(((Iterable<?>)Conversions.doWrapArray(_fields)), FieldDeclaration.class);
      }
    }
    if (!_matched) {
      if (type instanceof EnumDeclaration) {
        _matched=true;
        List _bodyDeclarations = ((EnumDeclaration)type).bodyDeclarations();
        _switchResult = Iterables.<FieldDeclaration>filter(_bodyDeclarations, FieldDeclaration.class);
      }
    }
    if (!_matched) {
      Class<? extends AbstractTypeDeclaration> _class = type.getClass();
      String _plus = ("Cannot handle" + _class);
      String _plus_1 = (_plus + " in getFields");
      throw new UnsupportedOperationException(_plus_1);
    }
    return _switchResult;
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
