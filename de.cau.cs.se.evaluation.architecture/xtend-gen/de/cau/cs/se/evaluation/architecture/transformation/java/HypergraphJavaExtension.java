package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class HypergraphJavaExtension {
  /**
   * Create a node for a given method.
   */
  public static Node createNodeForMethod(final MethodDeclaration method, final AbstractTypeDeclaration type) {
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(type, method);
    node.setName(_determineFullQualifiedName);
    final MethodTrace derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace();
    derivedFrom.setMethod(method);
    node.setDerivedFrom(derivedFrom);
    return node;
  }
  
  /**
   * Create a node for a class' implicit constructor
   */
  public static Node createNodeForImplicitConstructor(final AbstractTypeDeclaration type) {
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(type);
    String _plus = (_determineFullQualifiedName + ".");
    SimpleName _name = type.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    String _plus_1 = (_plus + _fullyQualifiedName);
    node.setName(_plus_1);
    final TypeTrace derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace();
    derivedFrom.setType(type);
    node.setDerivedFrom(derivedFrom);
    return node;
  }
  
  /**
   * Create for one IField an edge.
   */
  public static Edge createEdgeForField(final FieldDeclaration field, final AbstractTypeDeclaration type) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    List _fragments = field.fragments();
    for (final Object element : _fragments) {
      {
        final VariableDeclarationFragment fragment = ((VariableDeclarationFragment) element);
        String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(type, fragment);
        edge.setName(_determineFullQualifiedName);
        final FieldTrace derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace();
        derivedFrom.setField(fragment);
        edge.setDerivedFrom(derivedFrom);
      }
    }
    return edge;
  }
  
  /**
   * Create a module for each class in the list. Pointing to the class as derived from
   * element.
   * 
   * @return one new module
   */
  public static Module createModuleForClass(final AbstractTypeDeclaration type) {
    final Module module = HypergraphFactory.eINSTANCE.createModule();
    String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(type);
    module.setName(_determineFullQualifiedName);
    final TypeTrace derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace();
    derivedFrom.setType(type);
    module.setDerivedFrom(derivedFrom);
    return module;
  }
  
  /**
   * Find node for a given method specified by name clazz, name and argment list.
   */
  public static Node findNodeForMethod(final ModularHypergraph graph, final AbstractTypeDeclaration type, final String methodName, final List<Expression> arguments) {
    if ((type instanceof TypeDeclaration)) {
      final TypeDeclaration clazz = ((TypeDeclaration) type);
      MethodDeclaration[] _methods = clazz.getMethods();
      final Function1<MethodDeclaration, Boolean> _function = new Function1<MethodDeclaration, Boolean>() {
        public Boolean apply(final MethodDeclaration method) {
          SimpleName _name = method.getName();
          String _fullyQualifiedName = _name.getFullyQualifiedName();
          return Boolean.valueOf(_fullyQualifiedName.equals(methodName));
        }
      };
      Iterable<MethodDeclaration> _filter = IterableExtensions.<MethodDeclaration>filter(((Iterable<MethodDeclaration>)Conversions.doWrapArray(_methods)), _function);
      final Function1<MethodDeclaration, Boolean> _function_1 = new Function1<MethodDeclaration, Boolean>() {
        public Boolean apply(final MethodDeclaration method) {
          List _parameters = method.parameters();
          return Boolean.valueOf(HypergraphJavaExtension.compareArgAndParam(_parameters, arguments));
        }
      };
      final MethodDeclaration method = IterableExtensions.<MethodDeclaration>findFirst(_filter, _function_1);
      boolean _notEquals = (!Objects.equal(method, null));
      if (_notEquals) {
        EList<Node> _nodes = graph.getNodes();
        final Function1<Node, Boolean> _function_2 = new Function1<Node, Boolean>() {
          public Boolean apply(final Node it) {
            return Boolean.valueOf(HypergraphJavaExtension.isDerivedFromMethod(it, clazz, method));
          }
        };
        return IterableExtensions.<Node>findFirst(_nodes, _function_2);
      } else {
        return null;
      }
    } else {
      if ((type instanceof EnumDeclaration)) {
        final EnumDeclaration enumerate = ((EnumDeclaration) type);
        List _bodyDeclarations = enumerate.bodyDeclarations();
        Iterable<MethodDeclaration> _filter_1 = Iterables.<MethodDeclaration>filter(_bodyDeclarations, MethodDeclaration.class);
        final Function1<MethodDeclaration, Boolean> _function_3 = new Function1<MethodDeclaration, Boolean>() {
          public Boolean apply(final MethodDeclaration declaration) {
            SimpleName _name = declaration.getName();
            String _fullyQualifiedName = _name.getFullyQualifiedName();
            return Boolean.valueOf(_fullyQualifiedName.equals(methodName));
          }
        };
        Iterable<MethodDeclaration> _filter_2 = IterableExtensions.<MethodDeclaration>filter(_filter_1, _function_3);
        final Function1<MethodDeclaration, Boolean> _function_4 = new Function1<MethodDeclaration, Boolean>() {
          public Boolean apply(final MethodDeclaration declaration) {
            List _parameters = declaration.parameters();
            return Boolean.valueOf(HypergraphJavaExtension.compareArgAndParam(_parameters, arguments));
          }
        };
        final MethodDeclaration method_1 = IterableExtensions.<MethodDeclaration>findFirst(_filter_2, _function_4);
        boolean _notEquals_1 = (!Objects.equal(method_1, null));
        if (_notEquals_1) {
          EList<Node> _nodes_1 = graph.getNodes();
          final Function1<Node, Boolean> _function_5 = new Function1<Node, Boolean>() {
            public Boolean apply(final Node it) {
              return Boolean.valueOf(HypergraphJavaExtension.isDerivedFromMethod(it, enumerate, method_1));
            }
          };
          return IterableExtensions.<Node>findFirst(_nodes_1, _function_5);
        } else {
          return null;
        }
      }
    }
    return null;
  }
  
  /**
   * Find node for a given method declaration of a JDT DOM method in a given class.
   */
  public static Node findNodeForMethod(final ModularHypergraph graph, final AbstractTypeDeclaration clazz, final MethodDeclaration method) {
    EList<Node> _nodes = graph.getNodes();
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node it) {
        return Boolean.valueOf(HypergraphJavaExtension.isDerivedFromMethod(it, clazz, method));
      }
    };
    return IterableExtensions.<Node>findFirst(_nodes, _function);
  }
  
  /**
   * check if a given node is derived from a given method specified by clazz and method
   */
  public static boolean isDerivedFromMethod(final Node node, final AbstractTypeDeclaration clazz, final MethodDeclaration method) {
    NodeReference _derivedFrom = node.getDerivedFrom();
    if ((_derivedFrom instanceof MethodTrace)) {
      String _name = node.getName();
      String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(clazz, method);
      return _name.equals(_determineFullQualifiedName);
    }
    return false;
  }
  
  /**
   * Find edge for a given variable in the given class.
   */
  public static Edge findEdgeForVariable(final ModularHypergraph hypergraph, final AbstractTypeDeclaration clazz, final String variable) {
    EList<Edge> _edges = hypergraph.getEdges();
    final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
      public Boolean apply(final Edge edge) {
        String _name = edge.getName();
        String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(clazz);
        String _plus = (_determineFullQualifiedName + ".");
        String _plus_1 = (_plus + variable);
        return Boolean.valueOf(_name.equals(_plus_1));
      }
    };
    return IterableExtensions.<Edge>findFirst(_edges, _function);
  }
  
  /**
   * Find the constructor node matching the type and argument list.
   */
  public static Node findConstructorMethod(final ModularHypergraph graph, final Type type, final List<?> arguments) {
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
        String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(((AbstractTypeDeclaration) _type));
        String _fullyQualifiedName = typeName.getFullyQualifiedName();
        return Boolean.valueOf(_determineFullQualifiedName.equals(_fullyQualifiedName));
      }
    };
    final Module module = IterableExtensions.<Module>findFirst(_modules, _function);
    boolean _notEquals = (!Objects.equal(module, null));
    if (_notEquals) {
      ModuleReference _derivedFrom = module.getDerivedFrom();
      Object _type = ((TypeTrace) _derivedFrom).getType();
      SimpleName _name = ((TypeDeclaration) _type).getName();
      final String constructorName = _name.getFullyQualifiedName();
      EList<Node> _nodes = module.getNodes();
      final Function1<Node, Boolean> _function_1 = new Function1<Node, Boolean>() {
        public Boolean apply(final Node node) {
          boolean _switchResult = false;
          NodeReference _derivedFrom = node.getDerivedFrom();
          boolean _matched = false;
          if (!_matched) {
            if (_derivedFrom instanceof MethodTrace) {
              _matched=true;
              String _name = node.getName();
              String _name_1 = module.getName();
              String _plus = (_name_1 + ".");
              String _plus_1 = (_plus + constructorName);
              _switchResult = _name.equals(_plus_1);
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
      return HypergraphJavaExtension.matchArguments(nodes, arguments);
    } else {
      return null;
    }
  }
  
  private static Node matchArguments(final Iterable<Node> nodes, final List<?> arguments) {
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
              return Boolean.valueOf(HypergraphJavaExtension.matchArgumentsForExplicitMethods(((MethodTrace) _derivedFrom_1), arguments));
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
  
  private static boolean matchArgumentsForExplicitMethods(final MethodTrace trace, final List<?> arguments) {
    try {
      boolean _xifexpression = false;
      Object _method = trace.getMethod();
      if ((_method instanceof MethodDeclaration)) {
        boolean _xblockexpression = false;
        {
          Object _method_1 = trace.getMethod();
          final List parameters = ((MethodDeclaration) _method_1).parameters();
          boolean _xifexpression_1 = false;
          int _size = parameters.size();
          int _size_1 = arguments.size();
          boolean _equals = (_size == _size_1);
          if (_equals) {
            _xifexpression_1 = HypergraphJavaExtension.compareArgAndParam(parameters, arguments);
          } else {
            _xifexpression_1 = false;
          }
          _xblockexpression = _xifexpression_1;
        }
        _xifexpression = _xblockexpression;
      } else {
        Object _method_1 = trace.getMethod();
        Class<?> _class = _method_1.getClass();
        String _plus = ("Implementation error. Method type " + _class);
        String _plus_1 = (_plus + " not supported.");
        throw new Exception(_plus_1);
      }
      return _xifexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * returns true if the arguments match the parameters.
   */
  public static boolean compareArgAndParam(final List<?> parameters, final List<?> arguments) {
    int _size = parameters.size();
    int _size_1 = arguments.size();
    boolean _notEquals = (_size != _size_1);
    if (_notEquals) {
      return false;
    }
    for (int i = 0; (i < parameters.size()); i++) {
      {
        Object _get = parameters.get(i);
        Type _type = ((SingleVariableDeclaration) _get).getType();
        final ITypeBinding pType = _type.resolveBinding();
        Object _get_1 = arguments.get(i);
        final ITypeBinding aType = ((Expression) _get_1).resolveTypeBinding();
        boolean _isAssignmentCompatible = pType.isAssignmentCompatible(aType);
        boolean _not = (!_isAssignmentCompatible);
        if (_not) {
          return false;
        }
      }
    }
    return true;
  }
}
