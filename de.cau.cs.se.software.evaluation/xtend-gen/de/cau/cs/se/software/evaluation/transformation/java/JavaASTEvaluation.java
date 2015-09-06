package de.cau.cs.se.software.evaluation.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleReference;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.hypergraph.TypeTrace;
import de.cau.cs.se.software.evaluation.transformation.NameResolutionHelper;
import de.cau.cs.se.software.evaluation.transformation.java.JavaASTExpressionEvaluation;
import de.cau.cs.se.software.evaluation.transformation.java.JavaHypergraphElementFactory;
import de.cau.cs.se.software.evaluation.transformation.java.JavaHypergraphQueryHelper;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class JavaASTEvaluation {
  /**
   * Scan the AST of a method body for property access and method calls.
   * 
   * @param graph the hypergraph holding node, edges, and modules
   * @param dataTypePatterns list of pattern strings for classes which must be handled as data types
   * @param node the corresponding node to the method
   * @param clazz declaring class of the given method
   * @param method the method to be evaluated for property access and method calls
   */
  public static void evaluteMethod(final ModularHypergraph graph, final List<String> dataTypePatterns, final Node sourceNode, final TypeDeclaration clazz, final MethodDeclaration method) {
    boolean _and = false;
    boolean _isInterface = clazz.isInterface();
    boolean _not = (!_isInterface);
    if (!_not) {
      _and = false;
    } else {
      int _modifiers = method.getModifiers();
      boolean _isAbstract = Modifier.isAbstract(_modifiers);
      boolean _not_1 = (!_isAbstract);
      _and = _not_1;
    }
    if (_and) {
      Block _body = method.getBody();
      List _statements = _body.statements();
      boolean _notEquals = (!Objects.equal(_statements, null));
      if (_notEquals) {
        Block _body_1 = method.getBody();
        List _statements_1 = _body_1.statements();
        final Consumer<Object> _function = (Object statement) -> {
          JavaASTEvaluation.evaluateStatement(((Statement) statement), graph, dataTypePatterns, sourceNode);
        };
        _statements_1.forEach(_function);
      }
    }
  }
  
  /**
   * Evaluate a single statement.
   * 
   * @param statement the statement to be evaluated.
   * @param graph the modular hypergraph
   * @param dataTypePatterns pattern strings identifying data types
   * @param node the context node (node of the method)
   * @param clazz the context class
   * @param method the context method
   */
  public static void evaluateStatement(final Statement statement, final ModularHypergraph graph, final List<String> dataTypePatterns, final Node sourceNode) {
    boolean _matched = false;
    if (!_matched) {
      if (statement instanceof AssertStatement) {
        _matched=true;
        Expression _expression = ((AssertStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (statement instanceof Block) {
        _matched=true;
        List _statements = ((Block)statement).statements();
        final Consumer<Object> _function = (Object it) -> {
          JavaASTEvaluation.evaluateStatement(((Statement) it), graph, dataTypePatterns, sourceNode);
        };
        _statements.forEach(_function);
      }
    }
    if (!_matched) {
      if (statement instanceof ConstructorInvocation) {
        _matched=true;
        JavaASTEvaluation.handleConstructorInvocation(((ConstructorInvocation)statement), graph, dataTypePatterns, sourceNode);
      }
    }
    if (!_matched) {
      if (statement instanceof DoStatement) {
        _matched=true;
        Expression _expression = ((DoStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        Statement _body = ((DoStatement)statement).getBody();
        JavaASTEvaluation.evaluateStatement(_body, graph, dataTypePatterns, sourceNode);
      }
    }
    if (!_matched) {
      if (statement instanceof EnhancedForStatement) {
        _matched=true;
        Expression _expression = ((EnhancedForStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        Statement _body = ((EnhancedForStatement)statement).getBody();
        JavaASTEvaluation.evaluateStatement(_body, graph, dataTypePatterns, sourceNode);
      }
    }
    if (!_matched) {
      if (statement instanceof ExpressionStatement) {
        _matched=true;
        Expression _expression = ((ExpressionStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (statement instanceof ForStatement) {
        _matched=true;
        Expression _expression = ((ForStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        List _initializers = ((ForStatement)statement).initializers();
        final Consumer<Object> _function = (Object it) -> {
          JavaASTExpressionEvaluation.evaluate(((Expression) it), sourceNode, graph, dataTypePatterns);
        };
        _initializers.forEach(_function);
        List _updaters = ((ForStatement)statement).updaters();
        final Consumer<Object> _function_1 = (Object it) -> {
          JavaASTExpressionEvaluation.evaluate(((Expression) it), sourceNode, graph, dataTypePatterns);
        };
        _updaters.forEach(_function_1);
        Statement _body = ((ForStatement)statement).getBody();
        JavaASTEvaluation.evaluateStatement(_body, graph, dataTypePatterns, sourceNode);
      }
    }
    if (!_matched) {
      if (statement instanceof IfStatement) {
        _matched=true;
        Expression _expression = ((IfStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        Statement _thenStatement = ((IfStatement)statement).getThenStatement();
        JavaASTEvaluation.evaluateStatement(_thenStatement, graph, dataTypePatterns, sourceNode);
        Statement _elseStatement = ((IfStatement)statement).getElseStatement();
        if (_elseStatement!=null) {
          JavaASTEvaluation.evaluateStatement(_elseStatement, graph, dataTypePatterns, sourceNode);
        }
      }
    }
    if (!_matched) {
      if (statement instanceof LabeledStatement) {
        _matched=true;
        Statement _body = ((LabeledStatement)statement).getBody();
        JavaASTEvaluation.evaluateStatement(_body, graph, dataTypePatterns, sourceNode);
      }
    }
    if (!_matched) {
      if (statement instanceof ReturnStatement) {
        _matched=true;
        Expression _expression = ((ReturnStatement)statement).getExpression();
        if (_expression!=null) {
          JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        }
      }
    }
    if (!_matched) {
      if (statement instanceof SuperConstructorInvocation) {
        _matched=true;
        JavaASTEvaluation.handleSuperConstructorInvocation(((SuperConstructorInvocation)statement), graph, sourceNode);
      }
    }
    if (!_matched) {
      if (statement instanceof SwitchCase) {
        _matched=true;
        Expression _expression = ((SwitchCase)statement).getExpression();
        if (_expression!=null) {
          JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        }
      }
    }
    if (!_matched) {
      if (statement instanceof SwitchStatement) {
        _matched=true;
        Expression _expression = ((SwitchStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        List _statements = ((SwitchStatement)statement).statements();
        final Consumer<Object> _function = (Object it) -> {
          JavaASTEvaluation.evaluateStatement(((Statement) it), graph, dataTypePatterns, sourceNode);
        };
        _statements.forEach(_function);
      }
    }
    if (!_matched) {
      if (statement instanceof SynchronizedStatement) {
        _matched=true;
        Expression _expression = ((SynchronizedStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        Block _body = ((SynchronizedStatement)statement).getBody();
        JavaASTEvaluation.evaluateStatement(_body, graph, dataTypePatterns, sourceNode);
      }
    }
    if (!_matched) {
      if (statement instanceof ThrowStatement) {
        _matched=true;
        Expression _expression = ((ThrowStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (statement instanceof TryStatement) {
        _matched=true;
        Block _body = ((TryStatement)statement).getBody();
        JavaASTEvaluation.evaluateStatement(_body, graph, dataTypePatterns, sourceNode);
        List _catchClauses = ((TryStatement)statement).catchClauses();
        final Consumer<Object> _function = (Object it) -> {
          Block _body_1 = ((CatchClause) it).getBody();
          JavaASTEvaluation.evaluateStatement(_body_1, graph, dataTypePatterns, sourceNode);
        };
        _catchClauses.forEach(_function);
        Block _finally = ((TryStatement)statement).getFinally();
        if (_finally!=null) {
          JavaASTEvaluation.evaluateStatement(_finally, graph, dataTypePatterns, sourceNode);
        }
      }
    }
    if (!_matched) {
      if (statement instanceof VariableDeclarationStatement) {
        _matched=true;
        List _fragments = ((VariableDeclarationStatement)statement).fragments();
        final Consumer<Object> _function = (Object it) -> {
          Expression _initializer = ((VariableDeclarationFragment) it).getInitializer();
          if (_initializer!=null) {
            JavaASTExpressionEvaluation.evaluate(_initializer, sourceNode, graph, dataTypePatterns);
          }
        };
        _fragments.forEach(_function);
      }
    }
    if (!_matched) {
      if (statement instanceof WhileStatement) {
        _matched=true;
        Expression _expression = ((WhileStatement)statement).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        Statement _body = ((WhileStatement)statement).getBody();
        JavaASTEvaluation.evaluateStatement(_body, graph, dataTypePatterns, sourceNode);
      }
    }
    if (!_matched) {
      if (statement instanceof BreakStatement) {
        _matched=true;
      }
      if (!_matched) {
        if (statement instanceof ContinueStatement) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (statement instanceof EmptyStatement) {
          _matched=true;
        }
      }
      if (_matched) {
        return;
      }
    }
    if (!_matched) {
      Class<? extends Statement> _class = statement.getClass();
      String _plus = ("Expressions of type " + _class);
      String _plus_1 = (_plus + " are not supported.");
      throw new UnsupportedOperationException(_plus_1);
    }
  }
  
  /**
   * Handle an constructor call to the super class. This could be a method which is part of the framework.
   */
  public static boolean handleSuperConstructorInvocation(final SuperConstructorInvocation invocation, final ModularHypergraph graph, final Node sourceNode) {
    boolean _xblockexpression = false;
    {
      NodeReference _derivedFrom = sourceNode.getDerivedFrom();
      Object _method = ((MethodTrace) _derivedFrom).getMethod();
      final IMethodBinding sourceMethodBinding = ((IMethodBinding) _method);
      final IMethodBinding targetMethodBinding = invocation.resolveConstructorBinding();
      EList<Module> _modules = graph.getModules();
      final Function1<Module, Boolean> _function = (Module it) -> {
        boolean _xblockexpression_1 = false;
        {
          ModuleReference _derivedFrom_1 = it.getDerivedFrom();
          final Object type = ((TypeTrace) _derivedFrom_1).getType();
          boolean _switchResult = false;
          boolean _matched = false;
          if (!_matched) {
            if (type instanceof TypeDeclaration) {
              _matched=true;
              ITypeBinding _resolveBinding = ((TypeDeclaration)type).resolveBinding();
              ITypeBinding _declaringClass = targetMethodBinding.getDeclaringClass();
              _switchResult = _resolveBinding.isSubTypeCompatible(_declaringClass);
            }
          }
          if (!_matched) {
            if (type instanceof ITypeBinding) {
              _matched=true;
              ITypeBinding _declaringClass = targetMethodBinding.getDeclaringClass();
              _switchResult = ((ITypeBinding)type).isSubTypeCompatible(_declaringClass);
            }
          }
          if (!_matched) {
            Class<?> _class = type.getClass();
            String _plus = (_class + " is not supported as a source for module.");
            throw new UnsupportedOperationException(_plus);
          }
          _xblockexpression_1 = _switchResult;
        }
        return Boolean.valueOf(_xblockexpression_1);
      };
      Module module = IterableExtensions.<Module>findFirst(_modules, _function);
      boolean _equals = Objects.equal(module, null);
      if (_equals) {
        IMethodBinding _resolveConstructorBinding = invocation.resolveConstructorBinding();
        ITypeBinding _declaringClass = _resolveConstructorBinding.getDeclaringClass();
        Module _createModuleForTypeBinding = JavaHypergraphElementFactory.createModuleForTypeBinding(_declaringClass, EModuleKind.FRAMEWORK);
        module = _createModuleForTypeBinding;
        EList<Module> _modules_1 = graph.getModules();
        _modules_1.add(module);
      }
      EList<Node> _nodes = module.getNodes();
      final Function1<Node, Boolean> _function_1 = (Node it) -> {
        boolean _xblockexpression_1 = false;
        {
          NodeReference _derivedFrom_1 = it.getDerivedFrom();
          final Object localMethod = ((MethodTrace) _derivedFrom_1).getMethod();
          boolean _switchResult = false;
          boolean _matched = false;
          if (!_matched) {
            if (localMethod instanceof MethodDeclaration) {
              _matched=true;
              IMethodBinding _resolveBinding = ((MethodDeclaration)localMethod).resolveBinding();
              _switchResult = _resolveBinding.isSubsignature(targetMethodBinding);
            }
          }
          if (!_matched) {
            if (localMethod instanceof IMethodBinding) {
              _matched=true;
              _switchResult = ((IMethodBinding)localMethod).isSubsignature(targetMethodBinding);
            }
          }
          if (!_matched) {
            _switchResult = false;
          }
          _xblockexpression_1 = _switchResult;
        }
        return Boolean.valueOf(_xblockexpression_1);
      };
      Node targetNode = IterableExtensions.<Node>findFirst(_nodes, _function_1);
      boolean _equals_1 = Objects.equal(targetNode, null);
      if (_equals_1) {
        Node _createNodeForSuperConstructorInvocation = JavaHypergraphElementFactory.createNodeForSuperConstructorInvocation(targetMethodBinding);
        targetNode = _createNodeForSuperConstructorInvocation;
        EList<Node> _nodes_1 = module.getNodes();
        _nodes_1.add(targetNode);
        EList<Node> _nodes_2 = graph.getNodes();
        _nodes_2.add(targetNode);
      }
      final Edge edge = JavaHypergraphElementFactory.createCallEdge(sourceMethodBinding, targetMethodBinding);
      EList<Edge> _edges = graph.getEdges();
      _edges.add(edge);
      EList<Edge> _edges_1 = sourceNode.getEdges();
      _edges_1.add(edge);
      EList<Edge> _edges_2 = targetNode.getEdges();
      _xblockexpression = _edges_2.add(edge);
    }
    return _xblockexpression;
  }
  
  /**
   * Handle an constructor 'this' invocation. This requires (a) an call edge from
   * this method to the called constructor and (b) an evaluation of all parameters.
   */
  private static void handleConstructorInvocation(final ConstructorInvocation invocation, final ModularHypergraph graph, final List<String> dataTypePatterns, final Node sourceNode) {
    NodeReference _derivedFrom = sourceNode.getDerivedFrom();
    Object _method = ((MethodTrace) _derivedFrom).getMethod();
    final IMethodBinding sourceMethodBinding = ((IMethodBinding) _method);
    final IMethodBinding targetMethodBinding = invocation.resolveConstructorBinding();
    final Edge edge = JavaHypergraphElementFactory.createCallEdge(sourceMethodBinding, targetMethodBinding);
    EList<Edge> _edges = graph.getEdges();
    final Function1<Edge, Boolean> _function = (Edge it) -> {
      String _name = it.getName();
      String _name_1 = edge.getName();
      return Boolean.valueOf(_name.equals(_name_1));
    };
    boolean _exists = IterableExtensions.<Edge>exists(_edges, _function);
    boolean _not = (!_exists);
    if (_not) {
      EList<Node> _nodes = graph.getNodes();
      Node targetNode = JavaHypergraphQueryHelper.findNodeForConstructorBinding(_nodes, targetMethodBinding);
      boolean _equals = Objects.equal(targetNode, null);
      if (_equals) {
        String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(targetMethodBinding);
        String _plus = ("Missing source node: This is an error!! " + _determineFullyQualifiedName);
        throw new UnsupportedOperationException(_plus);
      } else {
        EList<Edge> _edges_1 = graph.getEdges();
        _edges_1.add(edge);
        EList<Edge> _edges_2 = targetNode.getEdges();
        _edges_2.add(edge);
        EList<Edge> _edges_3 = sourceNode.getEdges();
        _edges_3.add(edge);
      }
    }
    List _arguments = invocation.arguments();
    final Consumer<Object> _function_1 = (Object it) -> {
      JavaASTExpressionEvaluation.evaluate(((Expression) it), sourceNode, graph, dataTypePatterns);
    };
    _arguments.forEach(_function_1);
  }
}
