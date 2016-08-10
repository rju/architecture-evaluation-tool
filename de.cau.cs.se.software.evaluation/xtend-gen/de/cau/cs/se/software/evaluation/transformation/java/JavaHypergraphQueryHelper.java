package de.cau.cs.se.software.evaluation.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.EdgeReference;
import de.cau.cs.se.software.evaluation.hypergraph.FieldTrace;
import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleReference;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.hypergraph.TypeTrace;
import de.cau.cs.se.software.evaluation.transformation.java.JavaHypergraphElementFactory;
import de.cau.cs.se.software.evaluation.transformation.java.NameResolutionHelper;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class JavaHypergraphQueryHelper {
  /**
   * Find the node for the given method invocation.
   * 
   * @param nodes
   * @param binding
   */
  public static Node findNodeForMethodBinding(final EList<Node> nodes, final IMethodBinding binding) {
    final Function1<Node, Boolean> _function = (Node it) -> {
      boolean _xblockexpression = false;
      {
        final NodeReference derivedFrom = it.getDerivedFrom();
        boolean _switchResult = false;
        boolean _matched = false;
        if (derivedFrom instanceof MethodTrace) {
          _matched=true;
          Object _method = ((MethodTrace)derivedFrom).getMethod();
          IMethodBinding _methodDeclaration = ((IMethodBinding) _method).getMethodDeclaration();
          IMethodBinding _methodDeclaration_1 = binding.getMethodDeclaration();
          _switchResult = _methodDeclaration.isEqualTo(_methodDeclaration_1);
        }
        if (!_matched) {
          if (derivedFrom instanceof TypeTrace) {
            _matched=true;
            _switchResult = false;
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
    };
    return IterableExtensions.<Node>findFirst(nodes, _function);
  }
  
  /**
   * Find the node for the given method invocation.
   * 
   * @param nodes
   * @param binding
   */
  public static Node findNodeForConstructorBinding(final EList<Node> nodes, final IMethodBinding binding) {
    Node _xblockexpression = null;
    {
      final Node result = JavaHypergraphQueryHelper.findNodeForMethodBinding(nodes, binding);
      Node _xifexpression = null;
      boolean _equals = Objects.equal(result, null);
      if (_equals) {
        final Function1<Node, Boolean> _function = (Node it) -> {
          boolean _xblockexpression_1 = false;
          {
            final NodeReference derivedFrom = it.getDerivedFrom();
            boolean _switchResult = false;
            boolean _matched = false;
            if (derivedFrom instanceof MethodTrace) {
              _matched=true;
              _switchResult = false;
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
            _xblockexpression_1 = _switchResult;
          }
          return Boolean.valueOf(_xblockexpression_1);
        };
        _xifexpression = IterableExtensions.<Node>findFirst(nodes, _function);
      } else {
        return result;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  /**
   * Find a module which corresponds to the type binding.
   */
  public static Module findModule(final EList<Module> modules, final ITypeBinding binding) {
    final Function1<Module, Boolean> _function = (Module it) -> {
      ModuleReference _derivedFrom = it.getDerivedFrom();
      Object _type = ((TypeTrace) _derivedFrom).getType();
      return Boolean.valueOf(((ITypeBinding) _type).isEqualTo(binding));
    };
    return IterableExtensions.<Module>findFirst(modules, _function);
  }
  
  /**
   * Find a module which corresponds to the type binding.
   */
  public static Module findModule(final EList<Module> modules, final String fullyQualifiedName) {
    final Function1<Module, Boolean> _function = (Module it) -> {
      String _name = it.getName();
      return Boolean.valueOf(_name.equals(fullyQualifiedName));
    };
    return IterableExtensions.<Module>findFirst(modules, _function);
  }
  
  /**
   * Find an edge which has the two corresponding method bindings.
   */
  public static Edge findCallEdge(final EList<Edge> edges, final IMethodBinding endOne, final IMethodBinding endTwo) {
    final Function1<Edge, Boolean> _function = (Edge edge) -> {
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
          if (((caller.isEqualTo(endOne) && callee.isEqualTo(endTwo)) || (caller.isEqualTo(endTwo) && callee.isEqualTo(endOne)))) {
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
    };
    return IterableExtensions.<Edge>findFirst(edges, _function);
  }
  
  /**
   * Find an data edge which has the given variable binding.
   */
  public static Edge findDataEdge(final EList<Edge> edges, final IVariableBinding binding) {
    final Function1<Edge, Boolean> _function = (Edge edge) -> {
      boolean _xifexpression = false;
      EdgeReference _derivedFrom = edge.getDerivedFrom();
      if ((_derivedFrom instanceof FieldTrace)) {
        boolean _xblockexpression = false;
        {
          EdgeReference _derivedFrom_1 = edge.getDerivedFrom();
          Object _field = ((FieldTrace) _derivedFrom_1).getField();
          final IVariableBinding trace = ((IVariableBinding) _field);
          _xblockexpression = trace.isEqualTo(binding);
        }
        _xifexpression = _xblockexpression;
      } else {
        _xifexpression = false;
      }
      return Boolean.valueOf(_xifexpression);
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
        Module _createModuleForTypeBinding = JavaHypergraphElementFactory.createModuleForTypeBinding(typeBinding, EModuleKind.FRAMEWORK);
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
  
  /**
   * Find the given variable declared in the class given by the typeBinding and check if it is
   * a data property.
   * 
   * @param property the property name
   * @param typeBinding the type binding of the class
   * @param dataTypePatterns a list of patterns used to determine data types
   * 
   * @return Returns the variable binding if the property exists and is a data property, else null
   */
  public static IVariableBinding findDataPropertyInClass(final SimpleName property, final ITypeBinding typeBinding, final List<String> dataTypePatterns) {
    IVariableBinding[] _declaredFields = typeBinding.getDeclaredFields();
    final Function1<IVariableBinding, Boolean> _function = (IVariableBinding it) -> {
      return Boolean.valueOf((it.getName().equals(property.getFullyQualifiedName()) && JavaHypergraphQueryHelper.isDataType(it.getType(), dataTypePatterns)));
    };
    return IterableExtensions.<IVariableBinding>findFirst(((Iterable<IVariableBinding>)Conversions.doWrapArray(_declaredFields)), _function);
  }
  
  /**
   * Determine if a given type is considered a data type.
   */
  public static boolean isDataType(final ITypeBinding type, final List<String> dataTypePatterns) {
    boolean _isPrimitive = type.isPrimitive();
    if (_isPrimitive) {
      return true;
    } else {
      final Function1<String, Boolean> _function = (String it) -> {
        String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(type);
        return Boolean.valueOf(_determineFullyQualifiedName.matches(it));
      };
      return IterableExtensions.<String>exists(dataTypePatterns, _function);
    }
  }
}
