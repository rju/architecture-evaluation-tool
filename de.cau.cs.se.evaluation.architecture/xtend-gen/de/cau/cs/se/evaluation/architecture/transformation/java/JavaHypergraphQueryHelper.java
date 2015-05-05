package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class JavaHypergraphQueryHelper {
  /**
   * Find the node for the given constructor invocation.
   * 
   * @param nodes
   * @param binding
   */
  public static Node findNodeForMethodBinding(final EList<Node> nodes, final IMethodBinding binding) {
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node it) {
        boolean _xblockexpression = false;
        {
          final NodeReference derivedFrom = it.getDerivedFrom();
          boolean _switchResult = false;
          boolean _matched = false;
          if (!_matched) {
            if (derivedFrom instanceof MethodTrace) {
              _matched=true;
              Object _method = ((MethodTrace)derivedFrom).getMethod();
              _switchResult = ((IMethodBinding) _method).isEqualTo(binding);
            }
          }
          if (!_matched) {
            if (derivedFrom instanceof TypeTrace) {
              _matched=true;
              Object _type = ((TypeTrace)derivedFrom).getType();
              ITypeBinding _declaringClass = binding.getDeclaringClass();
              _switchResult = ((ITypeBinding) _type).isEqualTo(_declaringClass);
            }
          }
          if (!_matched) {
            Class<? extends NodeReference> _class = derivedFrom.getClass();
            String _plus = ("Nodes cannot be derived from " + _class);
            throw new UnsupportedOperationException(_plus);
          }
          _xblockexpression = _switchResult;
        }
        return Boolean.valueOf(_xblockexpression);
      }
    };
    return IterableExtensions.<Node>findFirst(nodes, _function);
  }
  
  /**
   * Find a module which corresponds to the type binding.
   */
  public static Module findModule(final EList<Module> modules, final ITypeBinding binding) {
    final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
      public Boolean apply(final Module it) {
        ModuleReference _derivedFrom = it.getDerivedFrom();
        Object _type = ((TypeTrace) _derivedFrom).getType();
        return Boolean.valueOf(((ITypeBinding) _type).isSubTypeCompatible(binding));
      }
    };
    return IterableExtensions.<Module>findFirst(modules, _function);
  }
  
  /**
   * Find an edge which has the two corresponding method bindings.
   */
  public static Edge findCallEdge(final EList<Edge> edges, final IMethodBinding endOne, final IMethodBinding endTwo) {
    final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
      public Boolean apply(final Edge edge) {
        boolean _xifexpression = false;
        EdgeReference _derivedFrom = edge.getDerivedFrom();
        if ((_derivedFrom instanceof CallerCalleeTrace)) {
          boolean _xblockexpression = false;
          {
            EdgeReference _derivedFrom_1 = edge.getDerivedFrom();
            final CallerCalleeTrace trace = ((CallerCalleeTrace) _derivedFrom_1);
            Object _callee = trace.getCallee();
            final IMethodBinding callee = ((IMethodBinding) _callee);
            Object _caller = trace.getCaller();
            final IMethodBinding caller = ((IMethodBinding) _caller);
            boolean _xifexpression_1 = false;
            boolean _or = false;
            boolean _and = false;
            boolean _isEqualTo = caller.isEqualTo(endOne);
            if (!_isEqualTo) {
              _and = false;
            } else {
              boolean _isEqualTo_1 = callee.isEqualTo(endTwo);
              _and = _isEqualTo_1;
            }
            if (_and) {
              _or = true;
            } else {
              boolean _and_1 = false;
              boolean _isEqualTo_2 = caller.isEqualTo(endTwo);
              if (!_isEqualTo_2) {
                _and_1 = false;
              } else {
                boolean _isEqualTo_3 = callee.isEqualTo(endOne);
                _and_1 = _isEqualTo_3;
              }
              _or = _and_1;
            }
            if (_or) {
              _xifexpression_1 = true;
            } else {
              _xifexpression_1 = false;
            }
            _xblockexpression = _xifexpression_1;
          }
          _xifexpression = _xblockexpression;
        } else {
          _xifexpression = false;
        }
        return Boolean.valueOf(_xifexpression);
      }
    };
    return IterableExtensions.<Edge>findFirst(edges, _function);
  }
  
  /**
   * Find an data edge which has the given variable binding.
   */
  public static Edge findDataEdge(final EList<Edge> edges, final IVariableBinding binding) {
    final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
      public Boolean apply(final Edge edge) {
        boolean _xifexpression = false;
        EdgeReference _derivedFrom = edge.getDerivedFrom();
        if ((_derivedFrom instanceof FieldTrace)) {
          boolean _xblockexpression = false;
          {
            EdgeReference _derivedFrom_1 = edge.getDerivedFrom();
            Object _field = ((FieldTrace) _derivedFrom_1).getField();
            final VariableDeclarationFragment trace = ((VariableDeclarationFragment) _field);
            IVariableBinding _resolveBinding = trace.resolveBinding();
            _xblockexpression = _resolveBinding.isEqualTo(binding);
          }
          _xifexpression = _xblockexpression;
        } else {
          _xifexpression = false;
        }
        return Boolean.valueOf(_xifexpression);
      }
    };
    return IterableExtensions.<Edge>findFirst(edges, _function);
  }
  
  /**
   * Find the corresponding node in the graph and if none can be found create one.
   * Missing nodes occur, because framework classes are not automatically scanned form method.
   * It a new node is created it is also added to the graph.
   * 
   * @param graph
   * @param typeBinding
   * @param methodBinding
   * 
   * @return returns the found or created node
   */
  public static Node findOrCreateTargetNode(final ModularHypergraph graph, final ITypeBinding typeBinding, final IMethodBinding methodBinding) {
    EList<Node> _nodes = graph.getNodes();
    Node targetNode = JavaHypergraphQueryHelper.findNodeForMethodBinding(_nodes, methodBinding);
    boolean _equals = Objects.equal(targetNode, null);
    if (_equals) {
      EList<Module> _modules = graph.getModules();
      Module module = JavaHypergraphQueryHelper.findModule(_modules, typeBinding);
      boolean _equals_1 = Objects.equal(module, null);
      if (_equals_1) {
        Module _createModuleForTypeBinding = JavaHypergraphElementFactory.createModuleForTypeBinding(typeBinding);
        module = _createModuleForTypeBinding;
        EList<Module> _modules_1 = graph.getModules();
        _modules_1.add(module);
      }
      Node _createNodeForMethod = JavaHypergraphElementFactory.createNodeForMethod(methodBinding);
      targetNode = _createNodeForMethod;
      EList<Node> _nodes_1 = module.getNodes();
      _nodes_1.add(targetNode);
      EList<Node> _nodes_2 = graph.getNodes();
      _nodes_2.add(targetNode);
    }
    return targetNode;
  }
}
