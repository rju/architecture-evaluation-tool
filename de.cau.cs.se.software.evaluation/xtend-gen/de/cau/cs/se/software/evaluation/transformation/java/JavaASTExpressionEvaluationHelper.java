package de.cau.cs.se.software.evaluation.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.transformation.java.JavaASTEvaluation;
import de.cau.cs.se.software.evaluation.transformation.java.JavaASTExpressionEvaluation;
import de.cau.cs.se.software.evaluation.transformation.java.JavaHypergraphElementFactory;
import de.cau.cs.se.software.evaluation.transformation.java.JavaHypergraphQueryHelper;
import de.cau.cs.se.software.evaluation.views.LogModelProvider;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class JavaASTExpressionEvaluationHelper {
  private static void displayMessage(final String title, final String message) {
    Display _default = Display.getDefault();
    _default.asyncExec(new Runnable() {
      @Override
      public void run() {
        IWorkbench _workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
        final Shell shell = _activeWorkbenchWindow.getShell();
        MessageDialog.openError(shell, title, message);
      }
    });
  }
  
  /**
   * Process an field access to a field of the super class. Generate a connection to a data edge.
   * If the edge is missing, which implies this is a framework super class, then add the edge.
   * 
   * @param superFieldAccess the super field accessed in an expression
   * @param sourceNode the node which must be connected to the edge
   * @param graph the hypergraph
   * @param dataTypePatterns string patterns to identify data types.
   */
  public static boolean processSuperFieldAccess(final SuperFieldAccess superFieldAccess, final Node sourceNode, final ModularHypergraph graph, final List<String> dataTypePatterns) {
    boolean _xblockexpression = false;
    {
      final IVariableBinding fieldBinding = superFieldAccess.resolveFieldBinding();
      boolean _xifexpression = false;
      boolean _notEquals = (!Objects.equal(fieldBinding, null));
      if (_notEquals) {
        boolean _xifexpression_1 = false;
        ITypeBinding _type = fieldBinding.getType();
        boolean _isDataType = JavaHypergraphQueryHelper.isDataType(_type, dataTypePatterns);
        if (_isDataType) {
          boolean _xblockexpression_1 = false;
          {
            EList<Edge> _edges = graph.getEdges();
            Edge edge = JavaHypergraphQueryHelper.findDataEdge(_edges, fieldBinding);
            boolean _equals = Objects.equal(edge, null);
            if (_equals) {
              Edge _createDataEdge = JavaHypergraphElementFactory.createDataEdge(fieldBinding);
              edge = _createDataEdge;
              EList<Edge> _edges_1 = graph.getEdges();
              _edges_1.add(edge);
            }
            EList<Edge> _edges_2 = sourceNode.getEdges();
            _xblockexpression_1 = _edges_2.add(edge);
          }
          _xifexpression_1 = _xblockexpression_1;
        }
        _xifexpression = _xifexpression_1;
      } else {
        SimpleName _name = superFieldAccess.getName();
        String _plus = ("Field binding for " + _name);
        String _plus_1 = (_plus + " of node ");
        String _name_1 = sourceNode.getName();
        String _plus_2 = (_plus_1 + _name_1);
        String _plus_3 = (_plus_2 + " could not be resolved.");
        JavaASTExpressionEvaluationHelper.displayMessage("Java model incomplete", _plus_3);
        throw new UnsupportedOperationException("Field binding could not be resolved. Java model incomplete.");
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Process a class instance creation.
   * - data type class = ignore
   * - framework or system class = connect
   * - anonymous class = create module and connect
   * 
   * @param callee the instance creation
   * @param sourceNode the node representing the caller
   * @param graph the hypergraph
   * @param dataTypePatterns list to identify data types
   */
  public static void processClassInstanceCreation(final ClassInstanceCreation callee, final Node sourceNode, final ModularHypergraph graph, final List<String> dataTypePatterns) {
    final ITypeBinding calleeTypeBinding = callee.resolveTypeBinding();
    boolean _isDataType = JavaHypergraphQueryHelper.isDataType(calleeTypeBinding, dataTypePatterns);
    boolean _not = (!_isDataType);
    if (_not) {
      final IMethodBinding calleeConstructorBinding = callee.resolveConstructorBinding();
      boolean _notEquals = (!Objects.equal(calleeConstructorBinding, null));
      if (_notEquals) {
        boolean _isAnonymous = calleeTypeBinding.isAnonymous();
        if (_isAnonymous) {
          final Module module = JavaHypergraphElementFactory.createModuleForTypeBinding(calleeTypeBinding, EModuleKind.ANONYMOUS);
          EList<Module> _modules = graph.getModules();
          _modules.add(module);
          AnonymousClassDeclaration _anonymousClassDeclaration = callee.getAnonymousClassDeclaration();
          ITypeBinding _resolveBinding = _anonymousClassDeclaration.resolveBinding();
          IMethodBinding[] _declaredMethods = _resolveBinding.getDeclaredMethods();
          final Consumer<IMethodBinding> _function = (IMethodBinding anonMethod) -> {
            final Node anonNode = JavaHypergraphElementFactory.createNodeForMethod(anonMethod);
            EList<Node> _nodes = module.getNodes();
            _nodes.add(anonNode);
            EList<Node> _nodes_1 = graph.getNodes();
            _nodes_1.add(anonNode);
          };
          ((List<IMethodBinding>)Conversions.doWrapArray(_declaredMethods)).forEach(_function);
        }
        final Node targetNode = JavaHypergraphQueryHelper.findOrCreateTargetNode(graph, calleeTypeBinding, calleeConstructorBinding);
        JavaASTExpressionEvaluationHelper.handleCallEdgeInsertion(graph, sourceNode, targetNode);
      } else {
        String _name = sourceNode.getName();
        String _plus = ((("Callee constructor binding cannot be resolved in " + callee) + " for node ") + _name);
        JavaASTExpressionEvaluationHelper.displayMessage("Binding Error", _plus);
      }
      List _arguments = callee.arguments();
      final Consumer<Object> _function_1 = (Object argument) -> {
        JavaASTExpressionEvaluation.evaluate(((Expression) argument), sourceNode, graph, dataTypePatterns);
      };
      _arguments.forEach(_function_1);
    }
  }
  
  /**
   * Process a method invocation in an expression. This might be a local method,
   * an external method, or an data type method.
   * 
   * @param callee the called method
   * @param sourceNode the node representing the caller
   * @param graph the hypergraph
   * @param dataTypePatterns a list of patterns to identify data types
   */
  public static void processMethodInvocation(final MethodInvocation callee, final Node sourceNode, final ModularHypergraph graph, final List<String> dataTypePatterns) {
    final IMethodBinding calleeMethodBinding = callee.resolveMethodBinding();
    boolean _notEquals = (!Objects.equal(calleeMethodBinding, null));
    if (_notEquals) {
      final ITypeBinding calleeTypeBinding = calleeMethodBinding.getDeclaringClass();
      boolean _isDataType = JavaHypergraphQueryHelper.isDataType(calleeTypeBinding, dataTypePatterns);
      boolean _not = (!_isDataType);
      if (_not) {
        final Node targetNode = JavaHypergraphQueryHelper.findOrCreateTargetNode(graph, calleeTypeBinding, calleeMethodBinding);
        JavaASTExpressionEvaluationHelper.handleCallEdgeInsertion(graph, sourceNode, targetNode);
        List _arguments = callee.arguments();
        final Consumer<Object> _function = (Object argument) -> {
          JavaASTExpressionEvaluation.evaluate(((Expression) argument), sourceNode, graph, dataTypePatterns);
        };
        _arguments.forEach(_function);
      }
    } else {
      String _string = callee.toString();
      String _plus = ("Internal error: The method binding could not be resolved for " + _string);
      throw new UnsupportedOperationException(_plus);
    }
  }
  
  /**
   * Process the invocation of a method of the super class as an call edge. If the target node is part of a framework, create the node
   * and place it in the graph.
   * 
   * @param lambda the lambda expression
   * @param sourceNode the context node
   * @param graph the modular graph
   * @param dataTypePatterns a list of patterns to identify data types
   */
  public static void processLambdaExpression(final LambdaExpression lambda, final Node sourceNode, final ModularHypergraph graph, final List<String> dataTypePatterns) {
    final ASTNode body = lambda.getBody();
    boolean _matched = false;
    if (body instanceof Block) {
      _matched=true;
      List _statements = ((Block)body).statements();
      final Consumer<Object> _function = (Object it) -> {
        JavaASTEvaluation.evaluateStatement(((Statement) it), graph, dataTypePatterns, sourceNode);
      };
      _statements.forEach(_function);
    }
    if (!_matched) {
      if (body instanceof Expression) {
        _matched=true;
        JavaASTExpressionEvaluation.evaluate(((Expression)body), sourceNode, graph, dataTypePatterns);
      }
    }
  }
  
  /**
   * Process the invocation of a method of the super class as an call edge. If the target node is part of a framework, create the node
   * and place it in the graph.
   * 
   * @param invocation the callee
   * @param sourceNode the caller
   * @param graph the modular graph
   * @param dataTypePatterns a list of patterns to identify data types
   */
  public static void processSuperMethodInvocation(final SuperMethodInvocation callee, final Node sourceNode, final ModularHypergraph graph, final List<String> dataTypePatterns) {
    final IMethodBinding targetSuperMethodBinding = callee.resolveMethodBinding();
    boolean _notEquals = (!Objects.equal(targetSuperMethodBinding, null));
    if (_notEquals) {
      ITypeBinding _declaringClass = targetSuperMethodBinding.getDeclaringClass();
      final Node targetNode = JavaHypergraphQueryHelper.findOrCreateTargetNode(graph, _declaringClass, targetSuperMethodBinding);
      JavaASTExpressionEvaluationHelper.handleCallEdgeInsertion(graph, sourceNode, targetNode);
      List _arguments = callee.arguments();
      final Consumer<Object> _function = (Object it) -> {
        JavaASTExpressionEvaluation.evaluate(((Expression) it), sourceNode, graph, dataTypePatterns);
      };
      _arguments.forEach(_function);
    } else {
      String _string = callee.toString();
      String _plus = ("Internal error: The method binding for a super call could not be resolved for " + _string);
      throw new UnsupportedOperationException(_plus);
    }
  }
  
  /**
   * Process a simple name which can be a variable or something else.
   * - variable == data access -> data edge
   * 
   * @param name the simple name which
   * @param sourceNode the node causing the access
   * @param graph the graph containing the edge
   */
  public static void processSimpleName(final SimpleName name, final Node sourceNode, final ModularHypergraph graph) {
    final IBinding nameBinding = name.resolveBinding();
    boolean _matched = false;
    if (nameBinding instanceof IVariableBinding) {
      _matched=true;
      EList<Edge> _edges = graph.getEdges();
      final Edge edge = JavaHypergraphQueryHelper.findDataEdge(_edges, ((IVariableBinding)nameBinding));
      boolean _notEquals = (!Objects.equal(edge, null));
      if (_notEquals) {
        EList<Edge> _edges_1 = sourceNode.getEdges();
        _edges_1.add(edge);
      }
    }
    if (!_matched) {
      Class<? extends IBinding> _class = nameBinding.getClass();
      String _plus = ("Binding type " + _class);
      String _plus_1 = (_plus + " is not supported by processSimpleName");
      throw new UnsupportedOperationException(_plus_1);
    }
  }
  
  /**
   * Process a qualified name which can be a variable or something else.
   * - variable == data access -> data edge
   * 
   * @param name the simple name which
   * @param sourceNode the node causing the access
   * @param graph the graph containing the edge
   */
  public static void processQualifiedName(final QualifiedName name, final Node sourceNode, final ModularHypergraph graph) {
    final IBinding nameBinding = name.resolveBinding();
    boolean _matched = false;
    if (nameBinding instanceof IVariableBinding) {
      _matched=true;
      EList<Edge> _edges = graph.getEdges();
      final Edge edge = JavaHypergraphQueryHelper.findDataEdge(_edges, ((IVariableBinding)nameBinding));
      boolean _notEquals = (!Objects.equal(edge, null));
      if (_notEquals) {
        EList<Edge> _edges_1 = sourceNode.getEdges();
        _edges_1.add(edge);
      }
    }
    if (!_matched) {
      {
        Class<? extends IBinding> _class = nameBinding.getClass();
        String _plus = ("Binding type " + _class);
        String _plus_1 = (_plus + " is not supported by processQualifiedName");
        JavaASTExpressionEvaluationHelper.displayMessage("Missing Functionality", _plus_1);
        Class<? extends IBinding> _class_1 = nameBinding.getClass();
        String _plus_2 = ("Binding type " + _class_1);
        String _plus_3 = (_plus_2 + " is not supported by processQualifiedName");
        throw new UnsupportedOperationException(_plus_3);
      }
    }
  }
  
  /**
   * Create a data edge link form the source node to the data edge corresponding to the field.
   * 
   * @param field the accessed field
   * @param sourceNode the node accessing the edge
   * @param contextTypebinding the type binding of the context class
   * @param graph the modular hypergraph
   * @param dataTypePatterns a list of patterns to identify data types
   */
  public static Boolean processFieldAccess(final FieldAccess fieldAccess, final Node sourceNode, final ITypeBinding contextTypeBinding, final ModularHypergraph graph, final List<String> dataTypePatterns) {
    Boolean _xblockexpression = null;
    {
      final Expression prefix = fieldAccess.getExpression();
      Boolean _switchResult = null;
      boolean _matched = false;
      if (prefix instanceof ThisExpression) {
        _matched=true;
        boolean _xifexpression = false;
        ITypeBinding _resolveTypeBinding = fieldAccess.resolveTypeBinding();
        boolean _isDataType = JavaHypergraphQueryHelper.isDataType(_resolveTypeBinding, dataTypePatterns);
        if (_isDataType) {
          boolean _xblockexpression_1 = false;
          {
            EList<Edge> _edges = graph.getEdges();
            IVariableBinding _resolveFieldBinding = fieldAccess.resolveFieldBinding();
            final Edge edge = JavaHypergraphQueryHelper.findDataEdge(_edges, _resolveFieldBinding);
            boolean _xifexpression_1 = false;
            boolean _equals = Objects.equal(edge, null);
            if (_equals) {
              Class<? extends ThisExpression> _class = ((ThisExpression)prefix).getClass();
              String _plus = (("Missing edge for a data type property. " + 
                " Prefix ") + _class);
              String _plus_1 = (_plus + " field ");
              IVariableBinding _resolveFieldBinding_1 = fieldAccess.resolveFieldBinding();
              String _plus_2 = (_plus_1 + _resolveFieldBinding_1);
              String _plus_3 = (_plus_2 + 
                " in ");
              String _name = JavaASTExpressionEvaluationHelper.class.getName();
              String _plus_4 = (_plus_3 + _name);
              String _plus_5 = (_plus_4 + ".processFieldAccess");
              LogModelProvider.INSTANCE.addMessage("Resolving Error", _plus_5);
            } else {
              EList<Edge> _edges_1 = sourceNode.getEdges();
              _xifexpression_1 = _edges_1.add(edge);
            }
            _xblockexpression_1 = _xifexpression_1;
          }
          _xifexpression = _xblockexpression_1;
        }
        _switchResult = Boolean.valueOf(_xifexpression);
      }
      if (!_matched) {
        if (prefix instanceof MethodInvocation) {
          _matched=true;
          _switchResult = null;
        }
      }
      if (!_matched) {
        if (prefix instanceof FieldAccess) {
          _matched=true;
          boolean _xifexpression = false;
          ITypeBinding _resolveTypeBinding = fieldAccess.resolveTypeBinding();
          boolean _isDataType = JavaHypergraphQueryHelper.isDataType(_resolveTypeBinding, dataTypePatterns);
          if (_isDataType) {
            boolean _xblockexpression_1 = false;
            {
              EList<Edge> _edges = graph.getEdges();
              IVariableBinding _resolveFieldBinding = fieldAccess.resolveFieldBinding();
              final Edge edge = JavaHypergraphQueryHelper.findDataEdge(_edges, _resolveFieldBinding);
              boolean _xifexpression_1 = false;
              boolean _equals = Objects.equal(edge, null);
              if (_equals) {
                Class<? extends FieldAccess> _class = ((FieldAccess)prefix).getClass();
                String _plus = (("Missing edge for a data type property. " + 
                  " Prefix ") + _class);
                String _plus_1 = (_plus + " field ");
                IVariableBinding _resolveFieldBinding_1 = fieldAccess.resolveFieldBinding();
                String _plus_2 = (_plus_1 + _resolveFieldBinding_1);
                String _plus_3 = (_plus_2 + 
                  " in ");
                String _name = JavaASTExpressionEvaluationHelper.class.getName();
                String _plus_4 = (_plus_3 + _name);
                String _plus_5 = (_plus_4 + ".processFieldAccess");
                LogModelProvider.INSTANCE.addMessage("Resolving Error", _plus_5);
              } else {
                EList<Edge> _edges_1 = sourceNode.getEdges();
                _xifexpression_1 = _edges_1.add(edge);
              }
              _xblockexpression_1 = _xifexpression_1;
            }
            _xifexpression = _xblockexpression_1;
          }
          _switchResult = Boolean.valueOf(_xifexpression);
        }
      }
      if (!_matched) {
        {
          Class<? extends Expression> _class = prefix.getClass();
          String _name = _class.getName();
          String _plus = ("Prefix type " + _name);
          String _plus_1 = (_plus + " not supported ");
          String _name_1 = JavaASTExpressionEvaluationHelper.class.getName();
          String _plus_2 = (_plus_1 + _name_1);
          String _plus_3 = (_plus_2 + ".processFieldAccess");
          JavaASTExpressionEvaluationHelper.displayMessage("Missing Functionality", _plus_3);
          Class<? extends Expression> _class_1 = prefix.getClass();
          String _name_2 = _class_1.getName();
          String _plus_4 = ("Prefix type " + _name_2);
          String _plus_5 = (_plus_4 + " not supported ");
          String _name_3 = JavaASTExpressionEvaluationHelper.class.getName();
          String _plus_6 = (_plus_5 + _name_3);
          String _plus_7 = (_plus_6 + ".processFieldAccess");
          throw new UnsupportedOperationException(_plus_7);
        }
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
  
  /**
   * Create a new edge between two nodes if there is no such connection already.
   * 
   * @param graph modular graph of the system
   * @param sourceNode source node
   * @param tagetNode target node
   */
  private static void handleCallEdgeInsertion(final ModularHypergraph graph, final Node sourceNode, final Node targetNode) {
    final IMethodBinding sourceMethodBinding = JavaASTExpressionEvaluationHelper.getMethodBinding(sourceNode);
    final IMethodBinding targetMethodBinding = JavaASTExpressionEvaluationHelper.getMethodBinding(targetNode);
    EList<Edge> _edges = graph.getEdges();
    Edge edge = JavaHypergraphQueryHelper.findCallEdge(_edges, sourceMethodBinding, targetMethodBinding);
    boolean _equals = Objects.equal(edge, null);
    if (_equals) {
      Edge _createCallEdge = JavaHypergraphElementFactory.createCallEdge(sourceMethodBinding, targetMethodBinding);
      edge = _createCallEdge;
      EList<Edge> _edges_1 = graph.getEdges();
      _edges_1.add(edge);
      EList<Edge> _edges_2 = sourceNode.getEdges();
      _edges_2.add(edge);
      EList<Edge> _edges_3 = targetNode.getEdges();
      _edges_3.add(edge);
    }
  }
  
  /**
   * Get the method binding of a node
   */
  private static IMethodBinding getMethodBinding(final Node node) {
    NodeReference _derivedFrom = node.getDerivedFrom();
    Object _method = ((MethodTrace) _derivedFrom).getMethod();
    return ((IMethodBinding) _method);
  }
}
