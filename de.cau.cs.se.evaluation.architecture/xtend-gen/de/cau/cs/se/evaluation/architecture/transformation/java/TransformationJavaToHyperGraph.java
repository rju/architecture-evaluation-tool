package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.GlobalJavaScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaLocalScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaPackageScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaTypeHelper;
import de.cau.cs.se.evaluation.architecture.transformation.java.ResolveStatementForClassAccess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

/**
 * Transform a java project based on a list of classes to a hypergraph.
 */
@SuppressWarnings("all")
public class TransformationJavaToHyperGraph {
  @Extension
  private JavaTypeHelper javaTypeHelper = new JavaTypeHelper();
  
  private int edgeId = 0;
  
  private final HypergraphSet hypergraphSet;
  
  private final IScope globalScope;
  
  private final IProgressMonitor monitor;
  
  public TransformationJavaToHyperGraph(final HypergraphSet hypergraphSet, final IScope scope) {
    this.globalScope = scope;
    this.hypergraphSet = hypergraphSet;
    this.monitor = null;
  }
  
  public TransformationJavaToHyperGraph(final HypergraphSet hypergraphSet, final GlobalJavaScope scope, final IProgressMonitor monitor) {
    this.globalScope = scope;
    this.hypergraphSet = hypergraphSet;
    this.monitor = monitor;
  }
  
  public Hypergraph transform(final List<IType> classList) {
    if (this.monitor!=null) {
      this.monitor.subTask("Constructing hypergraph");
    }
    final Hypergraph system = HypergraphFactory.eINSTANCE.createHypergraph();
    EList<Hypergraph> _graphs = this.hypergraphSet.getGraphs();
    _graphs.add(system);
    final Map<IType, IMethod> classMethodMap = new HashMap<IType, IMethod>();
    final Procedure1<IType> _function = new Procedure1<IType>() {
      public void apply(final IType it) {
        try {
          if (TransformationJavaToHyperGraph.this.monitor!=null) {
            String _elementName = it.getElementName();
            String _plus = ("Constructing hypergraph - types " + _elementName);
            TransformationJavaToHyperGraph.this.monitor.subTask(_plus);
          }
          boolean _isClass = it.isClass();
          if (_isClass) {
            final Node node = HypergraphFactory.eINSTANCE.createNode();
            String _elementName_1 = it.getElementName();
            node.setName(_elementName_1);
            EList<Node> _nodes = system.getNodes();
            _nodes.add(node);
            EList<Node> _nodes_1 = TransformationJavaToHyperGraph.this.hypergraphSet.getNodes();
            _nodes_1.add(node);
            IJavaElement _parent = it.getParent();
            if ((_parent instanceof IType)) {
              IJavaElement _parent_1 = it.getParent();
              final IType parent = ((IType) _parent_1);
              IMethod[] _methods = parent.getMethods();
              final Procedure1<IMethod> _function = new Procedure1<IMethod>() {
                public void apply(final IMethod method) {
                  try {
                    int _flags = method.getFlags();
                    boolean _isPublic = Flags.isPublic(_flags);
                    if (_isPublic) {
                      classMethodMap.put(it, method);
                    }
                  } catch (Throwable _e) {
                    throw Exceptions.sneakyThrow(_e);
                  }
                }
              };
              IterableExtensions.<IMethod>forEach(((Iterable<IMethod>)Conversions.doWrapArray(_methods)), _function);
            }
            IMethod[] _methods_1 = it.getMethods();
            final Procedure1<IMethod> _function_1 = new Procedure1<IMethod>() {
              public void apply(final IMethod method) {
                try {
                  int _flags = method.getFlags();
                  boolean _isPublic = Flags.isPublic(_flags);
                  if (_isPublic) {
                    classMethodMap.put(it, method);
                  }
                } catch (Throwable _e) {
                  throw Exceptions.sneakyThrow(_e);
                }
              }
            };
            IterableExtensions.<IMethod>forEach(((Iterable<IMethod>)Conversions.doWrapArray(_methods_1)), _function_1);
          }
          if (TransformationJavaToHyperGraph.this.monitor!=null) {
            TransformationJavaToHyperGraph.this.monitor.worked(1);
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    IterableExtensions.<IType>forEach(classList, _function);
    final Procedure1<IType> _function_1 = new Procedure1<IType>() {
      public void apply(final IType it) {
        try {
          if (TransformationJavaToHyperGraph.this.monitor!=null) {
            String _elementName = it.getElementName();
            String _plus = ("Constructing hypergraph - references " + _elementName);
            TransformationJavaToHyperGraph.this.monitor.subTask(_plus);
          }
          boolean _isClass = it.isClass();
          if (_isClass) {
            List<IType> _findAllCalledClasses = TransformationJavaToHyperGraph.this.findAllCalledClasses(it);
            final Function1<IType, Boolean> _function = new Function1<IType, Boolean>() {
              public Boolean apply(final IType it) {
                try {
                  boolean _and = false;
                  boolean _isClass = it.isClass();
                  if (!_isClass) {
                    _and = false;
                  } else {
                    boolean _isBinary = it.isBinary();
                    boolean _not = (!_isBinary);
                    _and = _not;
                  }
                  return Boolean.valueOf(_and);
                } catch (Throwable _e) {
                  throw Exceptions.sneakyThrow(_e);
                }
              }
            };
            Iterable<IType> _filter = IterableExtensions.<IType>filter(_findAllCalledClasses, _function);
            final Procedure1<IType> _function_1 = new Procedure1<IType>() {
              public void apply(final IType type) {
                final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
                String _nextEdgeName = TransformationJavaToHyperGraph.this.getNextEdgeName();
                edge.setName(_nextEdgeName);
                EList<Edge> _edges = system.getEdges();
                _edges.add(edge);
                EList<Edge> _edges_1 = TransformationJavaToHyperGraph.this.hypergraphSet.getEdges();
                _edges_1.add(edge);
                EList<Node> _nodes = system.getNodes();
                Node _findNode = TransformationJavaToHyperGraph.this.findNode(_nodes, it);
                EList<Edge> _edges_2 = _findNode.getEdges();
                _edges_2.add(edge);
                EList<Node> _nodes_1 = system.getNodes();
                Node _findMatchingNode = TransformationJavaToHyperGraph.this.findMatchingNode(_nodes_1, type);
                EList<Edge> _edges_3 = _findMatchingNode.getEdges();
                _edges_3.add(edge);
              }
            };
            IterableExtensions.<IType>forEach(_filter, _function_1);
          }
          if (TransformationJavaToHyperGraph.this.monitor!=null) {
            TransformationJavaToHyperGraph.this.monitor.worked(1);
          }
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    IterableExtensions.<IType>forEach(classList, _function_1);
    return system;
  }
  
  /**
   * Recurse into the type hierarchy and find all public methods.
   * 
   * @param type the type to be resolved.
   * @param classInterfaceMap the present map
   */
  private Map<IType, IMethod> resolvePublicClassInterface(final IType type, final Map<IType, IMethod> classInterfaceMap) {
    IJavaElement _parent = type.getParent();
    boolean _notEquals = (!Objects.equal(_parent, null));
    if (_notEquals) {
      IJavaElement _parent_1 = type.getParent();
      if ((_parent_1 instanceof IType)) {
        IJavaElement _parent_2 = type.getParent();
        final IType parent = ((IType) _parent_2);
        Map<IType, IMethod> _resolvePublicClassInterface = this.resolvePublicClassInterface(parent, classInterfaceMap);
        return this.getPublicClassInterface(type, _resolvePublicClassInterface);
      }
    }
    return this.getPublicClassInterface(type, classInterfaceMap);
  }
  
  /**
   * Determine all methods of a class which are public and not abstract.
   */
  private Map<IType, IMethod> getPublicClassInterface(final IType type, final Map<IType, IMethod> classInterfaceMap) {
    try {
      IMethod[] _methods = type.getMethods();
      final Procedure1<IMethod> _function = new Procedure1<IMethod>() {
        public void apply(final IMethod method) {
          try {
            boolean _and = false;
            int _flags = method.getFlags();
            boolean _isPublic = Flags.isPublic(_flags);
            if (!_isPublic) {
              _and = false;
            } else {
              int _flags_1 = method.getFlags();
              boolean _isAbstract = Flags.isAbstract(_flags_1);
              boolean _not = (!_isAbstract);
              _and = _not;
            }
            if (_and) {
              classInterfaceMap.put(type, method);
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      IterableExtensions.<IMethod>forEach(((Iterable<IMethod>)Conversions.doWrapArray(_methods)), _function);
      return classInterfaceMap;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Create names for edges based on a counter.
   */
  private String getNextEdgeName() {
    this.edgeId++;
    return Integer.toString(this.edgeId);
  }
  
  /**
   * Find the node related to the type based on the name of the node.
   */
  private Node findNode(final List<Node> nodes, final IType type) {
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node it) {
        String _name = it.getName();
        String _elementName = type.getElementName();
        return Boolean.valueOf(_name.equals(_elementName));
      }
    };
    return IterableExtensions.<Node>findFirst(nodes, _function);
  }
  
  private Node findMatchingNode(final List<Node> nodes, final IType type) {
    try {
      final Node node = this.findNode(nodes, type);
      boolean _equals = Objects.equal(node, null);
      if (_equals) {
        List<IType> _parentTypesList = this.getParentTypesList(type);
        final Function1<IType, Boolean> _function = new Function1<IType, Boolean>() {
          public Boolean apply(final IType it) {
            Node _findNode = TransformationJavaToHyperGraph.this.findNode(nodes, it);
            return Boolean.valueOf((!Objects.equal(_findNode, null)));
          }
        };
        final IType subtype = IterableExtensions.<IType>findFirst(_parentTypesList, _function);
        boolean _notEquals = (!Objects.equal(subtype, null));
        if (_notEquals) {
          return this.findNode(nodes, subtype);
        } else {
          String _elementName = type.getElementName();
          String _plus = ("No subtype of " + _elementName);
          String _plus_1 = (_plus + " is matching any node.");
          throw new Exception(_plus_1);
        }
      } else {
        return node;
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private List<IType> getParentTypesList(final IType type) {
    try {
      final List<IType> result = new ArrayList<IType>();
      final ITypeHierarchy typeHierarchy = type.newTypeHierarchy(this.monitor);
      IType[] _subclasses = typeHierarchy.getSubclasses(type);
      for (final IType subtype : _subclasses) {
        {
          result.add(subtype);
          List<IType> _parentTypesList = this.getParentTypesList(subtype);
          result.addAll(_parentTypesList);
        }
      }
      return result;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Determine all classes which are called from the given class.
   * This determines the required interfaces of a "component" while the
   * set of exposed (public) methods represent the provided interface of
   * a component.
   * 
   * @param type the type which required interfaces must be determined
   * 
   * @return list of called classes
   */
  private List<IType> findAllCalledClasses(final IType type) {
    try {
      final List<IType> classCalls = new ArrayList<IType>();
      final ASTParser parser = ASTParser.newParser(AST.JLS8);
      final Hashtable options = JavaCore.getOptions();
      JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options);
      parser.setCompilerOptions(options);
      ICompilationUnit _compilationUnit = type.getCompilationUnit();
      IBuffer _buffer = _compilationUnit.getBuffer();
      String _contents = _buffer.getContents();
      char[] _charArray = _contents.toCharArray();
      parser.setSource(_charArray);
      ASTNode _createAST = parser.createAST(null);
      final CompilationUnit unit = ((CompilationUnit) _createAST);
      IPackageFragment _packageFragment = type.getPackageFragment();
      JavaPackageScope _javaPackageScope = new JavaPackageScope(_packageFragment, this.globalScope);
      final JavaLocalScope scope = new JavaLocalScope(unit, _javaPackageScope);
      final ResolveStatementForClassAccess resolver = new ResolveStatementForClassAccess(scope);
      List _types = unit.types();
      final Object object = _types.get(0);
      if ((object instanceof TypeDeclaration)) {
        final TypeDeclaration declaredType = ((TypeDeclaration) object);
        List _modifiers = declaredType.modifiers();
        final Iterable<Modifier> modifiers = Iterables.<Modifier>filter(_modifiers, Modifier.class);
        boolean _and = false;
        boolean _isInterface = declaredType.isInterface();
        boolean _not = (!_isInterface);
        if (!_not) {
          _and = false;
        } else {
          final Function1<Modifier, Boolean> _function = new Function1<Modifier, Boolean>() {
            public Boolean apply(final Modifier it) {
              return Boolean.valueOf(((Modifier) it).isAbstract());
            }
          };
          Modifier _findFirst = IterableExtensions.<Modifier>findFirst(modifiers, _function);
          boolean _equals = Objects.equal(_findFirst, null);
          _and = _equals;
        }
        if (_and) {
          MethodDeclaration[] _methods = declaredType.getMethods();
          final Procedure1<MethodDeclaration> _function_1 = new Procedure1<MethodDeclaration>() {
            public void apply(final MethodDeclaration method) {
              Block _body = method.getBody();
              List _statements = _body.statements();
              final Procedure1<Statement> _function = new Procedure1<Statement>() {
                public void apply(final Statement it) {
                  List<IType> _resolve = resolver.resolve(it);
                  TransformationJavaToHyperGraph.this.javaTypeHelper.addUnique(classCalls, _resolve);
                }
              };
              IterableExtensions.<Statement>forEach(_statements, _function);
            }
          };
          IterableExtensions.<MethodDeclaration>forEach(((Iterable<MethodDeclaration>)Conversions.doWrapArray(_methods)), _function_1);
          FieldDeclaration[] _fields = declaredType.getFields();
          final Procedure1<FieldDeclaration> _function_2 = new Procedure1<FieldDeclaration>() {
            public void apply(final FieldDeclaration field) {
              Type _type = field.getType();
              IType _findType = TransformationJavaToHyperGraph.this.findType(_type, scope);
              TransformationJavaToHyperGraph.this.javaTypeHelper.addUnique(classCalls, _findType);
            }
          };
          IterableExtensions.<FieldDeclaration>forEach(((Iterable<FieldDeclaration>)Conversions.doWrapArray(_fields)), _function_2);
        }
      }
      return classCalls;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private IType findType(final Type type, final IScope scope) {
    IType _xifexpression = null;
    boolean _isArrayType = type.isArrayType();
    if (_isArrayType) {
      return null;
    } else {
      IType _xifexpression_1 = null;
      boolean _isSimpleType = type.isSimpleType();
      if (_isSimpleType) {
        Name _name = ((SimpleType) type).getName();
        String _fullyQualifiedName = _name.getFullyQualifiedName();
        _xifexpression_1 = scope.getType(_fullyQualifiedName);
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
}
