package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaASTEvaluation;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationJavaMethodsToModularHypergraph implements ITransformation {
  private ModularHypergraph modularSystem;
  
  private final IJavaProject project;
  
  private final IProgressMonitor monitor;
  
  private final List<AbstractTypeDeclaration> classList;
  
  private final List<AbstractTypeDeclaration> dataTypeList;
  
  /**
   * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
   * 
   * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
   * @param scope the global scoper used to resolve classes during transformation
   * @param eclipse progress monitor
   */
  public TransformationJavaMethodsToModularHypergraph(final IJavaProject project, final List<IType> dataTypeList, final List<IType> classList, final IProgressMonitor monitor) {
    this.project = project;
    ArrayList<AbstractTypeDeclaration> _arrayList = new ArrayList<AbstractTypeDeclaration>();
    this.classList = _arrayList;
    this.fillClassList(this.classList, classList);
    ArrayList<AbstractTypeDeclaration> _arrayList_1 = new ArrayList<AbstractTypeDeclaration>();
    this.dataTypeList = _arrayList_1;
    this.fillClassList(this.dataTypeList, dataTypeList);
    this.monitor = monitor;
  }
  
  private void fillClassList(final List<AbstractTypeDeclaration> declarations, final List<IType> types) {
    final Consumer<IType> _function = new Consumer<IType>() {
      public void accept(final IType jdtType) {
        final CompilationUnit unit = TransformationJavaMethodsToModularHypergraph.this.getUnitForType(jdtType, TransformationJavaMethodsToModularHypergraph.this.monitor, TransformationJavaMethodsToModularHypergraph.this.project);
        List _types = unit.types();
        final Consumer<Object> _function = new Consumer<Object>() {
          public void accept(final Object unitType) {
            boolean _or = false;
            if ((unitType instanceof TypeDeclaration)) {
              _or = true;
            } else {
              _or = (unitType instanceof EnumDeclaration);
            }
            if (_or) {
              final AbstractTypeDeclaration type = ((AbstractTypeDeclaration) unitType);
              System.out.println(("type is " + type));
              declarations.add(type);
            }
          }
        };
        _types.forEach(_function);
      }
    };
    types.forEach(_function);
  }
  
  /**
   * Get compilation unit for JDT type.
   */
  private CompilationUnit getUnitForType(final IType type, final IProgressMonitor monitor, final IJavaProject project) {
    try {
      final Hashtable options = JavaCore.getOptions();
      JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
      final ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setProject(project);
      parser.setCompilerOptions(options);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      ICompilationUnit _compilationUnit = type.getCompilationUnit();
      IBuffer _buffer = _compilationUnit.getBuffer();
      String _contents = _buffer.getContents();
      char[] _charArray = _contents.toCharArray();
      parser.setSource(_charArray);
      ICompilationUnit _compilationUnit_1 = type.getCompilationUnit();
      String _elementName = _compilationUnit_1.getElementName();
      parser.setUnitName(_elementName);
      parser.setResolveBindings(true);
      parser.setBindingsRecovery(true);
      parser.setStatementsRecovery(true);
      ASTNode _createAST = parser.createAST(null);
      return ((CompilationUnit) _createAST);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Return the generated result.
   */
  public ModularHypergraph getModularSystem() {
    return this.modularSystem;
  }
  
  public void transform() {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.modularSystem = _createModularHypergraph;
    final Consumer<AbstractTypeDeclaration> _function = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        EList<Module> _modules = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getModules();
        ITypeBinding _resolveBinding = clazz.resolveBinding();
        Module _createModuleForTypeBinding = JavaHypergraphElementFactory.createModuleForTypeBinding(_resolveBinding);
        _modules.add(_createModuleForTypeBinding);
      }
    };
    this.classList.forEach(_function);
    final Consumer<AbstractTypeDeclaration> _function_1 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        EList<Edge> _edges = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getEdges();
        TransformationJavaMethodsToModularHypergraph.this.createEdgesForClassProperties(_edges, clazz, TransformationJavaMethodsToModularHypergraph.this.dataTypeList);
      }
    };
    this.classList.forEach(_function_1);
    final Consumer<AbstractTypeDeclaration> _function_2 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        EList<Node> _nodes = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getNodes();
        TransformationJavaMethodsToModularHypergraph.this.createNodesForMethods(_nodes, clazz);
      }
    };
    this.classList.forEach(_function_2);
    final Function1<AbstractTypeDeclaration, Boolean> _function_3 = new Function1<AbstractTypeDeclaration, Boolean>() {
      public Boolean apply(final AbstractTypeDeclaration clazz) {
        return Boolean.valueOf(TransformationJavaMethodsToModularHypergraph.this.hasImplicitConstructor(clazz));
      }
    };
    Iterable<AbstractTypeDeclaration> _filter = IterableExtensions.<AbstractTypeDeclaration>filter(this.classList, _function_3);
    final Consumer<AbstractTypeDeclaration> _function_4 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        EList<Node> _nodes = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getNodes();
        ITypeBinding _resolveBinding = clazz.resolveBinding();
        Node _createNodeForImplicitConstructor = JavaHypergraphElementFactory.createNodeForImplicitConstructor(_resolveBinding);
        _nodes.add(_createNodeForImplicitConstructor);
      }
    };
    _filter.forEach(_function_4);
    final Consumer<AbstractTypeDeclaration> _function_5 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        TransformationJavaMethodsToModularHypergraph.this.resolveEdges(TransformationJavaMethodsToModularHypergraph.this.modularSystem, clazz);
      }
    };
    this.classList.forEach(_function_5);
  }
  
  /**
   * Create edges for each method found in the observed system.
   */
  private void resolveEdges(final ModularHypergraph graph, final AbstractTypeDeclaration type) {
    if ((type instanceof TypeDeclaration)) {
      final TypeDeclaration clazz = ((TypeDeclaration) type);
      MethodDeclaration[] _methods = clazz.getMethods();
      final Consumer<MethodDeclaration> _function = new Consumer<MethodDeclaration>() {
        public void accept(final MethodDeclaration method) {
          EList<Node> _nodes = graph.getNodes();
          final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
            public Boolean apply(final Node it) {
              NodeReference _derivedFrom = it.getDerivedFrom();
              Object _method = ((MethodTrace) _derivedFrom).getMethod();
              IMethodBinding _resolveBinding = method.resolveBinding();
              return Boolean.valueOf(((IMethodBinding) _method).isSubsignature(_resolveBinding));
            }
          };
          final Node node = IterableExtensions.<Node>findFirst(_nodes, _function);
          JavaASTEvaluation.evaluteMethod(graph, node, clazz, method);
        }
      };
      ((List<MethodDeclaration>)Conversions.doWrapArray(_methods)).forEach(_function);
    }
  }
  
  /**
   * Determine if a given class has no explicit constructors which
   * implies an implicit constructor.
   * 
   * @param type a class, enum or interface type from the selection
   * 
   * @return returns true if the given type is a normal class with no constructor
   */
  private boolean hasImplicitConstructor(final AbstractTypeDeclaration type) {
    if ((type instanceof TypeDeclaration)) {
      final TypeDeclaration clazz = ((TypeDeclaration) type);
      boolean _and = false;
      boolean _isInterface = clazz.isInterface();
      boolean _not = (!_isInterface);
      if (!_not) {
        _and = false;
      } else {
        int _modifiers = clazz.getModifiers();
        boolean _isAbstract = Modifier.isAbstract(_modifiers);
        boolean _not_1 = (!_isAbstract);
        _and = _not_1;
      }
      if (_and) {
        MethodDeclaration[] _methods = clazz.getMethods();
        final Function1<MethodDeclaration, Boolean> _function = new Function1<MethodDeclaration, Boolean>() {
          public Boolean apply(final MethodDeclaration method) {
            return Boolean.valueOf(method.isConstructor());
          }
        };
        return IterableExtensions.<MethodDeclaration>exists(((Iterable<MethodDeclaration>)Conversions.doWrapArray(_methods)), _function);
      }
    }
    return false;
  }
  
  /**
   * Create all nodes for a class.
   * 
   * @param nodes list where the nodes will be inserted
   * @param type the class where the methods come from
   */
  private void createNodesForMethods(final EList<Node> nodes, final AbstractTypeDeclaration type) {
    if ((type instanceof TypeDeclaration)) {
      EList<Module> _modules = this.modularSystem.getModules();
      final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
        public Boolean apply(final Module it) {
          ModuleReference _derivedFrom = it.getDerivedFrom();
          Object _type = ((TypeTrace) _derivedFrom).getType();
          ITypeBinding _resolveBinding = ((TypeDeclaration)type).resolveBinding();
          return Boolean.valueOf(((ITypeBinding) _type).isSubTypeCompatible(_resolveBinding));
        }
      };
      final Module module = IterableExtensions.<Module>findFirst(_modules, _function);
      MethodDeclaration[] _methods = ((TypeDeclaration)type).getMethods();
      final Consumer<MethodDeclaration> _function_1 = new Consumer<MethodDeclaration>() {
        public void accept(final MethodDeclaration method) {
          IMethodBinding _resolveBinding = method.resolveBinding();
          final Node node = JavaHypergraphElementFactory.createNodeForMethod(_resolveBinding);
          nodes.add(node);
          EList<Node> _nodes = module.getNodes();
          _nodes.add(node);
        }
      };
      ((List<MethodDeclaration>)Conversions.doWrapArray(_methods)).forEach(_function_1);
    }
  }
  
  /**
   * Create edges for all properties in the given type which are data type properties.
   */
  private void createEdgesForClassProperties(final EList<Edge> edges, final AbstractTypeDeclaration type, final List<AbstractTypeDeclaration> dataTypes) {
    if ((type instanceof TypeDeclaration)) {
      FieldDeclaration[] _fields = ((TypeDeclaration)type).getFields();
      final Consumer<FieldDeclaration> _function = new Consumer<FieldDeclaration>() {
        public void accept(final FieldDeclaration field) {
          Type _type = field.getType();
          boolean _isDataType = TransformationJavaMethodsToModularHypergraph.this.isDataType(_type, dataTypes);
          if (_isDataType) {
            List _fragments = field.fragments();
            final Consumer<VariableDeclarationFragment> _function = new Consumer<VariableDeclarationFragment>() {
              public void accept(final VariableDeclarationFragment fragment) {
                Edge _createDataEdge = JavaHypergraphElementFactory.createDataEdge(type, fragment);
                edges.add(_createDataEdge);
              }
            };
            _fragments.forEach(_function);
          }
        }
      };
      ((List<FieldDeclaration>)Conversions.doWrapArray(_fields)).forEach(_function);
    }
  }
  
  /**
   * Determine if the given type is a data type.
   * 
   * @param type the type to be evaluated
   * @param dataTypes a list of data types
   * 
   * @return returns true if the given type is a data type and not a behavior type.
   */
  private boolean isDataType(final Type type, final List<AbstractTypeDeclaration> dataTypes) {
    boolean _matched = false;
    if (!_matched) {
      if (type instanceof ArrayType) {
        _matched=true;
        Type _elementType = ((ArrayType)type).getElementType();
        return this.isDataType(_elementType, dataTypes);
      }
    }
    if (!_matched) {
      if (type instanceof ParameterizedType) {
        _matched=true;
        Type _type = ((ParameterizedType)type).getType();
        return this.isDataType(_type, dataTypes);
      }
    }
    if (!_matched) {
      if (type instanceof PrimitiveType) {
        _matched=true;
        return true;
      }
    }
    if (!_matched) {
      if (type instanceof QualifiedType) {
        _matched=true;
        Type _qualifier = ((QualifiedType)type).getQualifier();
        return this.isDataType(_qualifier, dataTypes);
      }
    }
    if (!_matched) {
      if (type instanceof SimpleType) {
        _matched=true;
        final Function1<AbstractTypeDeclaration, Boolean> _function = new Function1<AbstractTypeDeclaration, Boolean>() {
          public Boolean apply(final AbstractTypeDeclaration dataType) {
            SimpleName _name = dataType.getName();
            String _fullyQualifiedName = _name.getFullyQualifiedName();
            Name _name_1 = ((SimpleType)type).getName();
            String _fullyQualifiedName_1 = _name_1.getFullyQualifiedName();
            return Boolean.valueOf(_fullyQualifiedName.equals(_fullyQualifiedName_1));
          }
        };
        return IterableExtensions.<AbstractTypeDeclaration>exists(dataTypes, _function);
      }
    }
    if (!_matched) {
      if (type instanceof UnionType) {
        _matched=true;
        List _types = ((UnionType)type).types();
        final Function1<Type, Boolean> _function = new Function1<Type, Boolean>() {
          public Boolean apply(final Type it) {
            return Boolean.valueOf(TransformationJavaMethodsToModularHypergraph.this.isDataType(it, dataTypes));
          }
        };
        return IterableExtensions.<Type>forall(_types, _function);
      }
    }
    if (!_matched) {
      if (type instanceof WildcardType) {
        _matched=true;
        boolean _xifexpression = false;
        Type _bound = ((WildcardType)type).getBound();
        boolean _notEquals = (!Objects.equal(_bound, null));
        if (_notEquals) {
          Type _bound_1 = ((WildcardType)type).getBound();
          _xifexpression = this.isDataType(_bound_1, dataTypes);
        } else {
          _xifexpression = true;
        }
        return _xifexpression;
      }
    }
    return false;
  }
}
