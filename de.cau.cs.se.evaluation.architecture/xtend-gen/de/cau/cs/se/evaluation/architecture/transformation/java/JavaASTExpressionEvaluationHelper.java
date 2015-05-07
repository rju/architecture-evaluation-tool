package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaASTEvaluation;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphQueryHelper;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class JavaASTExpressionEvaluationHelper {
  /**
   * Process an assignment expression.
   */
  public static void processAssignment(final Assignment assignment, final ModularHypergraph graph, final List<String> dataTypePatterns, final Node node, final TypeDeclaration type, final MethodDeclaration method) {
    final ITypeBinding typeBinding = type.resolveBinding();
    Expression _leftHandSide = assignment.getLeftHandSide();
    boolean _isDataPropertyOfClass = JavaASTExpressionEvaluationHelper.isDataPropertyOfClass(_leftHandSide, dataTypePatterns, typeBinding);
    if (_isDataPropertyOfClass) {
      final Expression leftHandSide = assignment.getLeftHandSide();
      boolean _matched = false;
      if (!_matched) {
        if (leftHandSide instanceof SimpleName) {
          _matched=true;
          JavaASTExpressionEvaluationHelper.handleAssignmentSimpleNameEdgeResolving(assignment, graph, dataTypePatterns, typeBinding, node, type, method, ((SimpleName)leftHandSide));
        }
      }
      if (!_matched) {
        if (leftHandSide instanceof FieldAccess) {
          _matched=true;
          Expression prefix = ((FieldAccess)leftHandSide).getExpression();
          boolean _matched_1 = false;
          if (!_matched_1) {
            if (prefix instanceof ThisExpression) {
              _matched_1=true;
              SimpleName _name = ((FieldAccess)leftHandSide).getName();
              JavaASTExpressionEvaluationHelper.handleAssignmentSimpleNameEdgeResolving(assignment, graph, dataTypePatterns, typeBinding, node, type, method, _name);
            }
          }
          if (!_matched_1) {
            Class<? extends Expression> _class = prefix.getClass();
            String _plus = ("Left hand side of an assignment prefix expression type " + _class);
            String _plus_1 = (_plus + " is not supported by processAssignment");
            throw new UnsupportedOperationException(_plus_1);
          }
        }
      }
      if (!_matched) {
        Class<? extends Expression> _class = leftHandSide.getClass();
        String _plus = ("Expression type " + _class);
        String _plus_1 = (_plus + " is not supported by processAssignment");
        throw new UnsupportedOperationException(_plus_1);
      }
    } else {
      Expression _rightHandSide = assignment.getRightHandSide();
      JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_rightHandSide, graph, dataTypePatterns, typeBinding, node, type, method);
    }
  }
  
  /**
   * Create call edges for method and constructor calls found on the first level of the right hand side on an assignment
   * when the left hand side is a behavior type. For nested expressions call the expression handler.
   * 
   * @param expression the expression of the right hand side of the assignment
   * @param graph the modular hypergraph
   * @param dataTypePatterns a list of patterns of fully qualified class names of data types
   * @param typeBinding the binding of the context type
   * @param node the node of the context (caller)
   * @param type the context type
   * @param method the context method
   */
  private static ITypeBinding composeCallEdgesForAssignmentRightHandSide(final Expression expression, final ModularHypergraph graph, final List<String> dataTypePatterns, final ITypeBinding typeBinding, final Node node, final TypeDeclaration type, final MethodDeclaration method) {
    ITypeBinding _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (expression instanceof ArrayAccess) {
        _matched=true;
        Expression _index = ((ArrayAccess)expression).getIndex();
        JavaASTEvaluation.evaluate(_index, graph, dataTypePatterns, node, type, method);
        Expression _array = ((ArrayAccess)expression).getArray();
        return JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_array, graph, dataTypePatterns, typeBinding, node, type, method);
      }
    }
    if (!_matched) {
      if (expression instanceof CastExpression) {
        _matched=true;
        Expression _expression = ((CastExpression)expression).getExpression();
        _switchResult = JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_expression, graph, dataTypePatterns, typeBinding, node, type, method);
      }
    }
    if (!_matched) {
      if (expression instanceof ClassInstanceCreation) {
        _matched=true;
        List _arguments = ((ClassInstanceCreation)expression).arguments();
        final Consumer<Object> _function = new Consumer<Object>() {
          public void accept(final Object argument) {
            JavaASTEvaluation.evaluate(((Expression) argument), graph, dataTypePatterns, node, type, method);
          }
        };
        _arguments.forEach(_function);
        Expression _expression = ((ClassInstanceCreation)expression).getExpression();
        IMethodBinding _resolveConstructorBinding = ((ClassInstanceCreation)expression).resolveConstructorBinding();
        return JavaASTExpressionEvaluationHelper.evaluateRecursivelyInvocationChain(_expression, graph, dataTypePatterns, typeBinding, node, type, method, _resolveConstructorBinding);
      }
    }
    if (!_matched) {
      if (expression instanceof FieldAccess) {
        _matched=true;
        return ((FieldAccess)expression).resolveTypeBinding();
      }
    }
    if (!_matched) {
      if (expression instanceof InstanceofExpression) {
        _matched=true;
        Expression _leftOperand = ((InstanceofExpression)expression).getLeftOperand();
        _switchResult = JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_leftOperand, graph, dataTypePatterns, typeBinding, node, type, method);
      }
    }
    if (!_matched) {
      if (expression instanceof InfixExpression) {
        _matched=true;
        Expression _leftOperand = ((InfixExpression)expression).getLeftOperand();
        final ITypeBinding result = JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_leftOperand, graph, dataTypePatterns, typeBinding, node, type, method);
        Expression _rightOperand = ((InfixExpression)expression).getRightOperand();
        if (_rightOperand!=null) {
          JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_rightOperand, graph, dataTypePatterns, typeBinding, node, type, method);
        }
        return result;
      }
    }
    if (!_matched) {
      if (expression instanceof MethodInvocation) {
        _matched=true;
        List _arguments = ((MethodInvocation)expression).arguments();
        final Consumer<Object> _function = new Consumer<Object>() {
          public void accept(final Object argument) {
            JavaASTEvaluation.evaluate(((Expression) argument), graph, dataTypePatterns, node, type, method);
          }
        };
        _arguments.forEach(_function);
        Expression _expression = ((MethodInvocation)expression).getExpression();
        IMethodBinding _resolveMethodBinding = ((MethodInvocation)expression).resolveMethodBinding();
        return JavaASTExpressionEvaluationHelper.evaluateRecursivelyInvocationChain(_expression, graph, dataTypePatterns, typeBinding, node, type, method, _resolveMethodBinding);
      }
    }
    if (!_matched) {
      if (expression instanceof ParenthesizedExpression) {
        _matched=true;
        Expression _expression = ((ParenthesizedExpression)expression).getExpression();
        _switchResult = JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_expression, graph, dataTypePatterns, typeBinding, node, type, method);
      }
    }
    if (!_matched) {
      if (expression instanceof PostfixExpression) {
        _matched=true;
        Expression _operand = ((PostfixExpression)expression).getOperand();
        _switchResult = JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_operand, graph, dataTypePatterns, typeBinding, node, type, method);
      }
    }
    if (!_matched) {
      if (expression instanceof PrefixExpression) {
        _matched=true;
        Expression _operand = ((PrefixExpression)expression).getOperand();
        _switchResult = JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(_operand, graph, dataTypePatterns, typeBinding, node, type, method);
      }
    }
    if (!_matched) {
      if (expression instanceof SuperMethodInvocation) {
        _matched=true;
        List _arguments = ((SuperMethodInvocation)expression).arguments();
        final Consumer<Object> _function = new Consumer<Object>() {
          public void accept(final Object argument) {
            JavaASTEvaluation.evaluate(((Expression) argument), graph, dataTypePatterns, node, type, method);
          }
        };
        _arguments.forEach(_function);
        IMethodBinding _resolveMethodBinding = ((SuperMethodInvocation)expression).resolveMethodBinding();
        return JavaASTExpressionEvaluationHelper.evaluateRecursivelyInvocationChain(null, graph, dataTypePatterns, typeBinding, node, type, method, _resolveMethodBinding);
      }
    }
    if (!_matched) {
      if (expression instanceof BooleanLiteral) {
        _matched=true;
      }
      if (!_matched) {
        if (expression instanceof NullLiteral) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof NumberLiteral) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof QualifiedName) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof SimpleName) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof StringLiteral) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof ThisExpression) {
          _matched=true;
        }
      }
      if (_matched) {
        _switchResult = expression.resolveTypeBinding();
      }
    }
    if (!_matched) {
      Class<? extends Expression> _class = expression.getClass();
      String _plus = ("Expression type " + _class);
      String _plus_1 = (_plus + " is not supported by assignNodesToEdge");
      throw new UnsupportedOperationException(_plus_1);
    }
    return _switchResult;
  }
  
  private static ITypeBinding evaluateRecursivelyInvocationChain(final Expression parentExpression, final ModularHypergraph graph, final List<String> dataTypePatterns, final ITypeBinding typeBinding, final Node node, final TypeDeclaration type, final MethodDeclaration method, final IMethodBinding targetMethodBinding) {
    boolean _notEquals = (!Objects.equal(parentExpression, null));
    if (_notEquals) {
      final ITypeBinding parentTypeBinding = JavaASTExpressionEvaluationHelper.composeCallEdgesForAssignmentRightHandSide(parentExpression, graph, dataTypePatterns, typeBinding, node, type, method);
      boolean _isDataType = JavaASTExpressionEvaluationHelper.isDataType(parentTypeBinding, dataTypePatterns);
      boolean _not = (!_isDataType);
      if (_not) {
        IMethodBinding _resolveBinding = method.resolveBinding();
        JavaASTExpressionEvaluationHelper.composeCallEdge(graph, dataTypePatterns, node, _resolveBinding, targetMethodBinding);
      }
      return parentTypeBinding;
    } else {
      ITypeBinding _declaringClass = targetMethodBinding.getDeclaringClass();
      boolean _isDataType_1 = JavaASTExpressionEvaluationHelper.isDataType(_declaringClass, dataTypePatterns);
      boolean _not_1 = (!_isDataType_1);
      if (_not_1) {
        IMethodBinding _resolveBinding_1 = method.resolveBinding();
        JavaASTExpressionEvaluationHelper.composeCallEdge(graph, dataTypePatterns, node, _resolveBinding_1, targetMethodBinding);
      }
      return targetMethodBinding.getDeclaringClass();
    }
  }
  
  /**
   * Create an call edge between invocation method (target) and method (source) iff no such edge exists.
   */
  private static void composeCallEdge(final ModularHypergraph graph, final List<String> dataTypePatterns, final Node node, final IMethodBinding sourceMethodBinding, final IMethodBinding targetMethodBinding) {
    EList<Edge> _edges = graph.getEdges();
    Edge edge = JavaHypergraphQueryHelper.findCallEdge(_edges, sourceMethodBinding, targetMethodBinding);
    boolean _equals = Objects.equal(edge, null);
    if (_equals) {
      EList<Node> _nodes = graph.getNodes();
      final Node targetNode = JavaHypergraphQueryHelper.findNodeForMethodBinding(_nodes, targetMethodBinding);
      boolean _notEquals = (!Objects.equal(targetNode, null));
      if (_notEquals) {
        Edge _createCallEdge = JavaHypergraphElementFactory.createCallEdge(sourceMethodBinding, targetMethodBinding);
        edge = _createCallEdge;
        EList<Edge> _edges_1 = graph.getEdges();
        _edges_1.add(edge);
        EList<Edge> _edges_2 = node.getEdges();
        _edges_2.add(edge);
        EList<Edge> _edges_3 = targetNode.getEdges();
        _edges_3.add(edge);
      } else {
      }
    }
  }
  
  /**
   * Determine from a simple name to a data type and then reference
   * the right hand side and the context method to that data edge.
   */
  private static void handleAssignmentSimpleNameEdgeResolving(final Assignment assignment, final ModularHypergraph graph, final List<String> dataTypePatterns, final ITypeBinding typeBinding, final Node node, final TypeDeclaration type, final MethodDeclaration method, final SimpleName leftHandSide) {
    IVariableBinding[] _declaredFields = typeBinding.getDeclaredFields();
    final Function1<IVariableBinding, Boolean> _function = new Function1<IVariableBinding, Boolean>() {
      public Boolean apply(final IVariableBinding it) {
        String _name = it.getName();
        String _fullyQualifiedName = leftHandSide.getFullyQualifiedName();
        return Boolean.valueOf(_name.equals(_fullyQualifiedName));
      }
    };
    final IVariableBinding variableBinding = IterableExtensions.<IVariableBinding>findFirst(((Iterable<IVariableBinding>)Conversions.doWrapArray(_declaredFields)), _function);
    EList<Edge> _edges = graph.getEdges();
    final Edge edge = JavaHypergraphQueryHelper.findDataEdge(_edges, variableBinding);
    boolean _equals = Objects.equal(edge, null);
    if (_equals) {
      throw new UnsupportedOperationException(("Missing edge for " + variableBinding));
    } else {
      Expression _rightHandSide = assignment.getRightHandSide();
      JavaASTExpressionEvaluationHelper.assignNodesToEdge(_rightHandSide, graph, dataTypePatterns, node, type, method, edge);
      EList<Edge> _edges_1 = node.getEdges();
      _edges_1.add(edge);
    }
  }
  
  /**
   * Process a class instance creation in a local assignment.
   */
  public static void processClassInstanceCreation(final ClassInstanceCreation instanceCreation, final ModularHypergraph graph, final List<String> dataTypePatterns, final Node node, final TypeDeclaration clazz, final MethodDeclaration method) {
    final ITypeBinding calleeTypeBinding = instanceCreation.resolveTypeBinding();
    final IMethodBinding calleeConstructorBinding = instanceCreation.resolveConstructorBinding();
    final IMethodBinding callerBinding = method.resolveBinding();
    boolean _isDataType = JavaASTExpressionEvaluationHelper.isDataType(calleeTypeBinding, dataTypePatterns);
    if (_isDataType) {
      return;
    } else {
      boolean _isAnonymous = calleeTypeBinding.isAnonymous();
      if (_isAnonymous) {
        final Module module = JavaHypergraphElementFactory.createModuleForTypeBinding(calleeTypeBinding);
        EList<Module> _modules = graph.getModules();
        _modules.add(module);
        AnonymousClassDeclaration _anonymousClassDeclaration = instanceCreation.getAnonymousClassDeclaration();
        ITypeBinding _resolveBinding = _anonymousClassDeclaration.resolveBinding();
        IMethodBinding[] _declaredMethods = _resolveBinding.getDeclaredMethods();
        final Consumer<IMethodBinding> _function = new Consumer<IMethodBinding>() {
          public void accept(final IMethodBinding anonMethod) {
            final Node anonNode = JavaHypergraphElementFactory.createNodeForMethod(anonMethod);
            EList<Node> _nodes = module.getNodes();
            _nodes.add(anonNode);
            EList<Node> _nodes_1 = graph.getNodes();
            _nodes_1.add(anonNode);
          }
        };
        ((List<IMethodBinding>)Conversions.doWrapArray(_declaredMethods)).forEach(_function);
      }
      final Node targetNode = JavaHypergraphQueryHelper.findOrCreateTargetNode(graph, calleeTypeBinding, calleeConstructorBinding);
      final Edge edge = JavaHypergraphElementFactory.createCallEdge(callerBinding, calleeConstructorBinding);
      EList<Edge> _edges = graph.getEdges();
      _edges.add(edge);
      EList<Edge> _edges_1 = node.getEdges();
      _edges_1.add(edge);
      EList<Edge> _edges_2 = targetNode.getEdges();
      _edges_2.add(edge);
      List _arguments = instanceCreation.arguments();
      final Consumer<Object> _function_1 = new Consumer<Object>() {
        public void accept(final Object argument) {
          JavaASTEvaluation.evaluate(((Expression) argument), graph, dataTypePatterns, node, clazz, method);
        }
      };
      _arguments.forEach(_function_1);
    }
  }
  
  /**
   * Process a method invocation in an expression. This might be a local method, an external method, or an data type method.
   */
  public static void processMethodInvocation(final MethodInvocation invocation, final ModularHypergraph graph, final List<String> dataTypePatterns, final Node node, final TypeDeclaration clazz, final MethodDeclaration sourceMethod) {
    IMethodBinding _resolveMethodBinding = invocation.resolveMethodBinding();
    final ITypeBinding typeBinding = _resolveMethodBinding.getDeclaringClass();
    final IMethodBinding callerBinding = sourceMethod.resolveBinding();
    boolean _isDataType = JavaASTExpressionEvaluationHelper.isDataType(typeBinding, dataTypePatterns);
    if (_isDataType) {
      return;
    } else {
      final IMethodBinding calleeMethodBinding = invocation.resolveMethodBinding();
      final Node targetNode = JavaHypergraphQueryHelper.findOrCreateTargetNode(graph, typeBinding, calleeMethodBinding);
      final Edge edge = JavaHypergraphElementFactory.createCallEdge(callerBinding, calleeMethodBinding);
      EList<Edge> _edges = graph.getEdges();
      _edges.add(edge);
      EList<Edge> _edges_1 = node.getEdges();
      _edges_1.add(edge);
      EList<Edge> _edges_2 = targetNode.getEdges();
      _edges_2.add(edge);
      List _arguments = invocation.arguments();
      final Consumer<Object> _function = new Consumer<Object>() {
        public void accept(final Object argument) {
          JavaASTEvaluation.evaluate(((Expression) argument), graph, dataTypePatterns, node, clazz, sourceMethod);
        }
      };
      _arguments.forEach(_function);
    }
  }
  
  /**
   * Create a data edge link form the present method to the data edge corresponding to the field
   */
  public static boolean processFieldAccess(final FieldAccess field, final ModularHypergraph graph, final List<String> dataTypePatterns, final ITypeBinding typeBinding, final Node node) {
    boolean _xifexpression = false;
    boolean _isDataPropertyOfClass = JavaASTExpressionEvaluationHelper.isDataPropertyOfClass(field, dataTypePatterns, typeBinding);
    if (_isDataPropertyOfClass) {
      boolean _xblockexpression = false;
      {
        EList<Edge> _edges = graph.getEdges();
        IVariableBinding _resolveFieldBinding = field.resolveFieldBinding();
        final Edge edge = JavaHypergraphQueryHelper.findDataEdge(_edges, _resolveFieldBinding);
        EList<Edge> _edges_1 = node.getEdges();
        _xblockexpression = _edges_1.add(edge);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  /**
   * Recurse over the expression and relate all method invocations to the data property.
   */
  private static void assignNodesToEdge(final Expression expression, final ModularHypergraph graph, final List<String> dataTypePatterns, final Node node, final TypeDeclaration clazz, final MethodDeclaration method, final Edge edge) {
    boolean _matched = false;
    if (!_matched) {
      if (expression instanceof MethodInvocation) {
        _matched=true;
        IMethodBinding _resolveMethodBinding = ((MethodInvocation)expression).resolveMethodBinding();
        JavaASTExpressionEvaluationHelper.assignNodeToEdge(_resolveMethodBinding, graph, edge);
      }
    }
    if (!_matched) {
      if (expression instanceof InfixExpression) {
        _matched=true;
        Expression _leftOperand = ((InfixExpression)expression).getLeftOperand();
        JavaASTExpressionEvaluationHelper.assignNodesToEdge(_leftOperand, graph, dataTypePatterns, node, clazz, method, edge);
        Expression _rightOperand = ((InfixExpression)expression).getRightOperand();
        if (_rightOperand!=null) {
          JavaASTExpressionEvaluationHelper.assignNodesToEdge(_rightOperand, graph, dataTypePatterns, node, clazz, method, edge);
        }
      }
    }
    if (!_matched) {
      if (expression instanceof ClassInstanceCreation) {
        _matched=true;
        List _arguments = ((ClassInstanceCreation)expression).arguments();
        final Consumer<Object> _function = new Consumer<Object>() {
          public void accept(final Object argument) {
            JavaASTEvaluation.evaluate(((Expression) argument), graph, dataTypePatterns, node, clazz, method);
          }
        };
        _arguments.forEach(_function);
      }
    }
    if (!_matched) {
      if (expression instanceof BooleanLiteral) {
        _matched=true;
      }
      if (!_matched) {
        if (expression instanceof NullLiteral) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof NumberLiteral) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof QualifiedName) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof SimpleName) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (expression instanceof StringLiteral) {
          _matched=true;
        }
      }
      if (_matched) {
        return;
      }
    }
    if (!_matched) {
      Class<? extends Expression> _class = expression.getClass();
      String _plus = ("Expression type " + _class);
      String _plus_1 = (_plus + " is not supported by assignNodesToEdge");
      throw new UnsupportedOperationException(_plus_1);
    }
  }
  
  /**
   * Find or create an node for the given invocation and connect it to the data edge.
   */
  private static boolean assignNodeToEdge(final IMethodBinding methodBinding, final ModularHypergraph graph, final Edge edge) {
    boolean _xblockexpression = false;
    {
      ITypeBinding _declaringClass = methodBinding.getDeclaringClass();
      final Node node = JavaHypergraphQueryHelper.findOrCreateTargetNode(graph, _declaringClass, methodBinding);
      EList<Edge> _edges = node.getEdges();
      _xblockexpression = _edges.add(edge);
    }
    return _xblockexpression;
  }
  
  /**
   * Determine if a given type is considered a data type.
   */
  private static boolean isDataType(final ITypeBinding type, final List<String> dataTypePatterns) {
    boolean _isPrimitive = type.isPrimitive();
    if (_isPrimitive) {
      return true;
    } else {
      final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
        public Boolean apply(final String it) {
          String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(type);
          return Boolean.valueOf(_determineFullyQualifiedName.matches(it));
        }
      };
      return IterableExtensions.<String>exists(dataTypePatterns, _function);
    }
  }
  
  /**
   * Checks if an expression resolves to a class data property.
   * 
   * @param expression the expression to be evaluated
   * 
   * @return true for data properties false otherwise
   */
  private static boolean isDataPropertyOfClass(final Expression expression, final List<String> dataTypePatterns, final ITypeBinding typeBinding) {
    boolean _switchResult = false;
    boolean _matched = false;
    if (!_matched) {
      if (expression instanceof FieldAccess) {
        _matched=true;
        boolean _xblockexpression = false;
        {
          final Expression prefix = ((FieldAccess)expression).getExpression();
          boolean _switchResult_1 = false;
          boolean _matched_1 = false;
          if (!_matched_1) {
            if (prefix instanceof ParenthesizedExpression) {
              _matched_1=true;
              Expression _expression = ((ParenthesizedExpression)prefix).getExpression();
              _switchResult_1 = JavaASTExpressionEvaluationHelper.isDataPropertyOfClass(_expression, dataTypePatterns, typeBinding);
            }
          }
          if (!_matched_1) {
            if (prefix instanceof ThisExpression) {
              _matched_1=true;
              SimpleName _name = ((FieldAccess)expression).getName();
              _switchResult_1 = JavaASTExpressionEvaluationHelper.isDataPropertyOfClass(_name, dataTypePatterns, typeBinding);
            }
          }
          if (!_matched_1) {
            Class<? extends FieldAccess> _class = ((FieldAccess)expression).getClass();
            String _plus = ("FieldAccess expression type " + _class);
            String _plus_1 = (_plus + " is not supported by isClassDataProperty");
            throw new UnsupportedOperationException(_plus_1);
          }
          _xblockexpression = _switchResult_1;
        }
        _switchResult = _xblockexpression;
      }
    }
    if (!_matched) {
      if (expression instanceof SimpleName) {
        _matched=true;
        IVariableBinding[] _declaredFields = typeBinding.getDeclaredFields();
        final Function1<IVariableBinding, Boolean> _function = new Function1<IVariableBinding, Boolean>() {
          public Boolean apply(final IVariableBinding it) {
            boolean _and = false;
            String _name = it.getName();
            String _fullyQualifiedName = ((SimpleName)expression).getFullyQualifiedName();
            boolean _equals = _name.equals(_fullyQualifiedName);
            if (!_equals) {
              _and = false;
            } else {
              ITypeBinding _type = it.getType();
              boolean _isDataType = JavaASTExpressionEvaluationHelper.isDataType(_type, dataTypePatterns);
              _and = _isDataType;
            }
            return Boolean.valueOf(_and);
          }
        };
        _switchResult = IterableExtensions.<IVariableBinding>exists(((Iterable<IVariableBinding>)Conversions.doWrapArray(_declaredFields)), _function);
      }
    }
    if (!_matched) {
      if (expression instanceof QualifiedName) {
        _matched=true;
        _switchResult = false;
      }
    }
    if (!_matched) {
      if (expression instanceof ArrayAccess) {
        _matched=true;
        Expression _array = ((ArrayAccess)expression).getArray();
        _switchResult = JavaASTExpressionEvaluationHelper.isDataPropertyOfClass(_array, dataTypePatterns, typeBinding);
      }
    }
    if (!_matched) {
      return false;
    }
    return _switchResult;
  }
  
  /**
   * Find a variable declaration in the local context of a method which conforms to the given name.
   * 
   * @return returns a variable declaration of null
   */
  private static VariableDeclaration findVariableDeclaration(final SimpleName name, final Statement statement) {
    boolean _matched = false;
    if (!_matched) {
      if (statement instanceof Block) {
        _matched=true;
        List _statements = ((Block)statement).statements();
        return JavaASTExpressionEvaluationHelper.findVariableDeclarationInSequence(name, _statements);
      }
    }
    if (!_matched) {
      if (statement instanceof DoStatement) {
        _matched=true;
        Statement _body = ((DoStatement)statement).getBody();
        return JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _body);
      }
    }
    if (!_matched) {
      if (statement instanceof EnhancedForStatement) {
        _matched=true;
        SingleVariableDeclaration _parameter = ((EnhancedForStatement)statement).getParameter();
        SimpleName _name = _parameter.getName();
        String _fullyQualifiedName = _name.getFullyQualifiedName();
        String _fullyQualifiedName_1 = name.getFullyQualifiedName();
        boolean _equals = _fullyQualifiedName.equals(_fullyQualifiedName_1);
        if (_equals) {
          return ((EnhancedForStatement)statement).getParameter();
        } else {
          Statement _body = ((EnhancedForStatement)statement).getBody();
          return JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _body);
        }
      }
    }
    if (!_matched) {
      if (statement instanceof ForStatement) {
        _matched=true;
        Statement _body = ((ForStatement)statement).getBody();
        return JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _body);
      }
    }
    if (!_matched) {
      if (statement instanceof IfStatement) {
        _matched=true;
        Statement _thenStatement = ((IfStatement)statement).getThenStatement();
        final VariableDeclaration then = JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _thenStatement);
        boolean _notEquals = (!Objects.equal(then, null));
        if (_notEquals) {
          return then;
        } else {
          Statement _elseStatement = ((IfStatement)statement).getElseStatement();
          return JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _elseStatement);
        }
      }
    }
    if (!_matched) {
      if (statement instanceof LabeledStatement) {
        _matched=true;
        Statement _body = ((LabeledStatement)statement).getBody();
        return JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _body);
      }
    }
    if (!_matched) {
      if (statement instanceof SwitchStatement) {
        _matched=true;
        List _statements = ((SwitchStatement)statement).statements();
        return JavaASTExpressionEvaluationHelper.findVariableDeclarationInSequence(name, _statements);
      }
    }
    if (!_matched) {
      if (statement instanceof SynchronizedStatement) {
        _matched=true;
        Block _body = ((SynchronizedStatement)statement).getBody();
        return JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _body);
      }
    }
    if (!_matched) {
      if (statement instanceof TryStatement) {
        _matched=true;
        Block _body = ((TryStatement)statement).getBody();
        final VariableDeclaration declaration = JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _body);
        boolean _equals = Objects.equal(declaration, null);
        if (_equals) {
          List _catchClauses = ((TryStatement)statement).catchClauses();
          for (final Object clause : _catchClauses) {
            {
              Block _body_1 = ((TryStatement)statement).getBody();
              final VariableDeclaration declarationInClause = JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _body_1);
              boolean _notEquals = (!Objects.equal(declarationInClause, null));
              if (_notEquals) {
                return declarationInClause;
              }
            }
          }
          Block _finally = ((TryStatement)statement).getFinally();
          return JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _finally);
        } else {
          return declaration;
        }
      }
    }
    if (!_matched) {
      if (statement instanceof VariableDeclarationStatement) {
        _matched=true;
        List _fragments = ((VariableDeclarationStatement)statement).fragments();
        final Function1<VariableDeclaration, Boolean> _function = new Function1<VariableDeclaration, Boolean>() {
          public Boolean apply(final VariableDeclaration it) {
            SimpleName _name = ((VariableDeclarationFragment) it).getName();
            String _fullyQualifiedName = _name.getFullyQualifiedName();
            String _fullyQualifiedName_1 = name.getFullyQualifiedName();
            return Boolean.valueOf(_fullyQualifiedName.equals(_fullyQualifiedName_1));
          }
        };
        return IterableExtensions.<VariableDeclaration>findFirst(_fragments, _function);
      }
    }
    if (!_matched) {
      if (statement instanceof WhileStatement) {
        _matched=true;
        Statement _body = ((WhileStatement)statement).getBody();
        return JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, _body);
      }
    }
    return null;
  }
  
  /**
   * Handle variable search for statement sequences.
   */
  private static VariableDeclaration findVariableDeclarationInSequence(final SimpleName name, final List<Statement> statements) {
    for (final Statement statement : statements) {
      {
        final VariableDeclaration result = JavaASTExpressionEvaluationHelper.findVariableDeclaration(name, statement);
        boolean _notEquals = (!Objects.equal(result, null));
        if (_notEquals) {
          return result;
        }
      }
    }
    return null;
  }
}
