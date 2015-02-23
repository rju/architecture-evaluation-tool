package de.cau.cs.se.evaluation.architecture.transformation;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationHelper {
  public static Node deriveNode(final Node node) {
    final Node resultNode = HypergraphFactory.eINSTANCE.createNode();
    String _name = node.getName();
    resultNode.setName(_name);
    final NodeTrace derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace();
    derivedFrom.setNode(node);
    resultNode.setDerivedFrom(derivedFrom);
    return resultNode;
  }
  
  public static Edge deriveEdge(final Edge edge) {
    final Edge resultEdge = HypergraphFactory.eINSTANCE.createEdge();
    String _name = edge.getName();
    resultEdge.setName(_name);
    final EdgeTrace derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace();
    derivedFrom.setEdge(edge);
    resultEdge.setDerivedFrom(derivedFrom);
    return resultEdge;
  }
  
  public static Module deriveModule(final Module module) {
    final Module resultModule = HypergraphFactory.eINSTANCE.createModule();
    String _name = module.getName();
    resultModule.setName(_name);
    final ModuleTrace derivedFrom = HypergraphFactory.eINSTANCE.createModuleTrace();
    derivedFrom.setModule(module);
    resultModule.setDerivedFrom(derivedFrom);
    return resultModule;
  }
  
  public static Node findConstructorMethod(final ModularHypergraph graph, final Type type, final List arguments) {
    Name _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (type instanceof SimpleType) {
        _matched=true;
        _switchResult = ((SimpleType)type).getName();
      }
    }
    if (!_matched) {
      if (type instanceof NameQualifiedType) {
        _matched=true;
        _switchResult = ((NameQualifiedType)type).getName();
      }
    }
    if (!_matched) {
      return null;
    }
    final Name typeName = _switchResult;
    EList<Module> _modules = graph.getModules();
    final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
      public Boolean apply(final Module module) {
        ModuleReference _derivedFrom = module.getDerivedFrom();
        Object _type = ((TypeTrace) _derivedFrom).getType();
        String _elementName = ((IType) _type).getElementName();
        String _fullyQualifiedName = typeName.getFullyQualifiedName();
        return Boolean.valueOf(_elementName.equals(_fullyQualifiedName));
      }
    };
    final Module module = IterableExtensions.<Module>findFirst(_modules, _function);
    boolean _notEquals = (!Objects.equal(module, null));
    if (_notEquals) {
      ModuleReference _derivedFrom = module.getDerivedFrom();
      Object _type = ((TypeTrace) _derivedFrom).getType();
      final String moduleName = ((IType) _type).getElementName();
      EList<Node> _nodes = module.getNodes();
      final Function1<Node, Boolean> _function_1 = new Function1<Node, Boolean>() {
        public Boolean apply(final Node node) {
          boolean _switchResult = false;
          NodeReference _derivedFrom = node.getDerivedFrom();
          boolean _matched = false;
          if (!_matched) {
            if (_derivedFrom instanceof MethodTrace) {
              _matched=true;
              NodeReference _derivedFrom_1 = node.getDerivedFrom();
              Object _method = ((MethodTrace) _derivedFrom_1).getMethod();
              String _elementName = ((IMethod) _method).getElementName();
              _switchResult = _elementName.equals(moduleName);
            }
          }
          if (!_matched) {
            if (_derivedFrom instanceof TypeTrace) {
              _matched=true;
              _switchResult = true;
            }
          }
          if (!_matched) {
            _switchResult = false;
          }
          return Boolean.valueOf(_switchResult);
        }
      };
      final Iterable<Node> nodes = IterableExtensions.<Node>filter(_nodes, _function_1);
      return TransformationHelper.matchArguments(nodes, arguments);
    } else {
      return null;
    }
  }
  
  public static Node matchArguments(final Iterable<Node> nodes, final List arguments) {
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node node) {
        try {
          NodeReference _derivedFrom = node.getDerivedFrom();
          boolean _matched = false;
          if (!_matched) {
            if (_derivedFrom instanceof TypeTrace) {
              _matched=true;
              int _size = arguments.size();
              return Boolean.valueOf((_size == 0));
            }
          }
          if (!_matched) {
            if (_derivedFrom instanceof MethodTrace) {
              _matched=true;
              NodeReference _derivedFrom_1 = node.getDerivedFrom();
              return Boolean.valueOf(TransformationHelper.matchArgumentsForExplicitMethods(((MethodTrace) _derivedFrom_1), arguments));
            }
          }
          NodeReference _derivedFrom_1 = node.getDerivedFrom();
          Class<? extends NodeReference> _class = _derivedFrom_1.getClass();
          String _plus = ("Unsupported derivedFrom type used for node " + _class);
          throw new Exception(_plus);
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    return IterableExtensions.<Node>findFirst(nodes, _function);
  }
  
  private static boolean matchArgumentsForExplicitMethods(final MethodTrace trace, final List arguments) {
    try {
      boolean _switchResult = false;
      Object _method = trace.getMethod();
      boolean _matched = false;
      if (!_matched) {
        if (_method instanceof MethodDeclaration) {
          _matched=true;
          boolean _xblockexpression = false;
          {
            Object _method_1 = trace.getMethod();
            final List parameters = ((MethodDeclaration) _method_1).parameters();
            boolean _xifexpression = false;
            int _size = parameters.size();
            int _size_1 = arguments.size();
            boolean _equals = (_size == _size_1);
            if (_equals) {
              _xifexpression = TransformationHelper.compareArgAndParam(parameters, arguments);
            } else {
              _xifexpression = false;
            }
            _xblockexpression = _xifexpression;
          }
          _switchResult = _xblockexpression;
        }
      }
      if (!_matched) {
        if (_method instanceof IMethod) {
          _matched=true;
          boolean _xblockexpression = false;
          {
            Object _method_1 = trace.getMethod();
            final ILocalVariable[] parameters = ((IMethod) _method_1).getParameters();
            boolean _xifexpression = false;
            int _size = ((List<ILocalVariable>)Conversions.doWrapArray(parameters)).size();
            int _size_1 = arguments.size();
            boolean _equals = (_size == _size_1);
            if (_equals) {
              _xifexpression = TransformationHelper.compareArgAndParamIMethod(((List<ILocalVariable>)Conversions.doWrapArray(parameters)), arguments);
            } else {
              _xifexpression = false;
            }
            _xblockexpression = _xifexpression;
          }
          _switchResult = _xblockexpression;
        }
      }
      if (!_matched) {
        Object _method_1 = trace.getMethod();
        Class<?> _class = _method_1.getClass();
        String _plus = ("Implementation error. Method type " + _class);
        String _plus_1 = (_plus + " not supported.");
        throw new Exception(_plus_1);
      }
      return _switchResult;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * returns true if the arguments match the parameters.
   */
  private static boolean compareArgAndParamIMethod(final List<ILocalVariable> parameters, final List arguments) {
    for (int i = 0; (i < parameters.size()); i++) {
      {
        ILocalVariable _get = parameters.get(i);
        final String pType = _get.getTypeSignature();
        Object _get_1 = arguments.get(i);
        ITypeBinding _resolveTypeBinding = ((Expression) _get_1).resolveTypeBinding();
        final String aType = _resolveTypeBinding.getBinaryName();
        boolean _equals = pType.equals(aType);
        if (_equals) {
          return false;
        }
      }
    }
    return true;
  }
  
  /**
   * returns true if the arguments match the parameters.
   */
  private static boolean compareArgAndParam(final List parameters, final List arguments) {
    for (int i = 0; (i < parameters.size()); i++) {
      {
        Object _get = parameters.get(i);
        Type _type = ((SingleVariableDeclaration) _get).getType();
        ITypeBinding _resolveBinding = _type.resolveBinding();
        final ITypeBinding pType = _resolveBinding.getErasure();
        Object _get_1 = arguments.get(i);
        ITypeBinding _resolveTypeBinding = ((Expression) _get_1).resolveTypeBinding();
        final ITypeBinding aType = _resolveTypeBinding.getErasure();
        boolean _isAssignmentCompatible = pType.isAssignmentCompatible(aType);
        boolean _not = (!_isAssignmentCompatible);
        if (_not) {
          return false;
        }
      }
    }
    return true;
  }
  
  public static boolean createEdgeBetweenMethods(final ModularHypergraph hypergraph, final Node caller, final Node callee) {
    boolean _xblockexpression = false;
    {
      EList<Edge> _edges = caller.getEdges();
      final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
        public Boolean apply(final Edge callerEdge) {
          EList<Edge> _edges = callee.getEdges();
          final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
            public Boolean apply(final Edge calleeEdge) {
              return Boolean.valueOf(Objects.equal(calleeEdge, callerEdge));
            }
          };
          return Boolean.valueOf(IterableExtensions.<Edge>exists(_edges, _function));
        }
      };
      final Iterable<Edge> edgeSubset = IterableExtensions.<Edge>filter(_edges, _function);
      String _name = caller.getName();
      String _plus = (_name + "::");
      String _name_1 = callee.getName();
      final String edgeName = (_plus + _name_1);
      final Function1<Edge, Boolean> _function_1 = new Function1<Edge, Boolean>() {
        public Boolean apply(final Edge edge) {
          String _name = edge.getName();
          return Boolean.valueOf(_name.equals(edgeName));
        }
      };
      final Edge existingEdge = IterableExtensions.<Edge>findFirst(edgeSubset, _function_1);
      boolean _xifexpression = false;
      boolean _equals = Objects.equal(existingEdge, null);
      if (_equals) {
        boolean _xblockexpression_1 = false;
        {
          final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
          edge.setDerivedFrom(null);
          edge.setName(edgeName);
          EList<Edge> _edges_1 = hypergraph.getEdges();
          _edges_1.add(edge);
          EList<Edge> _edges_2 = callee.getEdges();
          _edges_2.add(edge);
          EList<Edge> _edges_3 = caller.getEdges();
          _xblockexpression_1 = _edges_3.add(edge);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
