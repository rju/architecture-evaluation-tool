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
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
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
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
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
  
  private final List<String> dataTypePatterns;
  
  /**
   * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
   * 
   * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
   * @param scope the global scoper used to resolve classes during transformation
   * @param eclipse progress monitor
   */
  public TransformationJavaMethodsToModularHypergraph(final IJavaProject project, final List<String> dataTypePatterns, final List<IType> classList, final IProgressMonitor monitor) {
    this.project = project;
    ArrayList<AbstractTypeDeclaration> _arrayList = new ArrayList<AbstractTypeDeclaration>();
    this.classList = _arrayList;
    this.fillClassList(this.classList, classList, dataTypePatterns);
    this.dataTypePatterns = dataTypePatterns;
    this.monitor = monitor;
  }
  
  private void fillClassList(final List<AbstractTypeDeclaration> declarations, final List<IType> types, final List<String> dataTypePatterns) {
    final Consumer<IType> _function = new Consumer<IType>() {
      public void accept(final IType jdtType) {
        final CompilationUnit unit = TransformationJavaMethodsToModularHypergraph.this.getUnitForType(jdtType, TransformationJavaMethodsToModularHypergraph.this.monitor, TransformationJavaMethodsToModularHypergraph.this.project);
        List _types = unit.types();
        final Consumer<Object> _function = new Consumer<Object>() {
          public void accept(final Object unitType) {
            if ((unitType instanceof TypeDeclaration)) {
              final TypeDeclaration type = ((TypeDeclaration) unitType);
              System.out.println(("type is " + type));
              ITypeBinding _resolveBinding = type.resolveBinding();
              boolean _isClassDataType = TransformationJavaMethodsToModularHypergraph.this.isClassDataType(_resolveBinding, dataTypePatterns);
              boolean _not = (!_isClassDataType);
              if (_not) {
                declarations.add(type);
              }
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
  
  /**
   * Main transformation routine.
   */
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
        TransformationJavaMethodsToModularHypergraph.this.createEdgesForClassProperties(_edges, clazz, TransformationJavaMethodsToModularHypergraph.this.dataTypePatterns);
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
        ITypeBinding _resolveBinding = clazz.resolveBinding();
        final Node node = JavaHypergraphElementFactory.createNodeForImplicitConstructor(_resolveBinding);
        EList<Module> _modules = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getModules();
        final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
          public Boolean apply(final Module it) {
            ModuleReference _derivedFrom = it.getDerivedFrom();
            Object _type = ((TypeTrace) _derivedFrom).getType();
            ITypeBinding _resolveBinding = clazz.resolveBinding();
            return Boolean.valueOf(((ITypeBinding) _type).isSubTypeCompatible(_resolveBinding));
          }
        };
        final Module module = IterableExtensions.<Module>findFirst(_modules, _function);
        EList<Node> _nodes = module.getNodes();
        _nodes.add(node);
        EList<Node> _nodes_1 = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getNodes();
        _nodes_1.add(node);
      }
    };
    _filter.forEach(_function_4);
    final Consumer<AbstractTypeDeclaration> _function_5 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        TransformationJavaMethodsToModularHypergraph.this.resolveEdges(TransformationJavaMethodsToModularHypergraph.this.modularSystem, TransformationJavaMethodsToModularHypergraph.this.dataTypePatterns, clazz);
      }
    };
    this.classList.forEach(_function_5);
  }
  
  /**
   * Create edges for each method found in the observed system.
   * 
   * @param graph the hypergraph where the edges will be added to
   * @param type the type which is processed for edges
   */
  private void resolveEdges(final ModularHypergraph graph, final List<String> dataTypePatterns, final AbstractTypeDeclaration type) {
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
          JavaASTEvaluation.evaluteMethod(graph, dataTypePatterns, node, clazz, method);
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
        boolean _exists = IterableExtensions.<MethodDeclaration>exists(((Iterable<MethodDeclaration>)Conversions.doWrapArray(_methods)), _function);
        final boolean isImplicit = (!_exists);
        return isImplicit;
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
  private void createEdgesForClassProperties(final EList<Edge> edges, final AbstractTypeDeclaration type, final List<String> dataTypePatterns) {
    if ((type instanceof TypeDeclaration)) {
      FieldDeclaration[] _fields = ((TypeDeclaration)type).getFields();
      final Consumer<FieldDeclaration> _function = new Consumer<FieldDeclaration>() {
        public void accept(final FieldDeclaration field) {
          Type _type = field.getType();
          boolean _isDataType = TransformationJavaMethodsToModularHypergraph.this.isDataType(_type, dataTypePatterns);
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
  private boolean isDataType(final Type type, final List<String> dataTypePatterns) {
    boolean _matched = false;
    if (!_matched) {
      if (type instanceof ArrayType) {
        _matched=true;
        Type _elementType = ((ArrayType)type).getElementType();
        return this.isDataType(_elementType, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (type instanceof ParameterizedType) {
        _matched=true;
        Type _type = ((ParameterizedType)type).getType();
        return this.isDataType(_type, dataTypePatterns);
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
        return this.isDataType(_qualifier, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (type instanceof SimpleType) {
        _matched=true;
        final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
          public Boolean apply(final String dataTypePattern) {
            ITypeBinding _resolveBinding = ((SimpleType)type).resolveBinding();
            String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_resolveBinding);
            return Boolean.valueOf(_determineFullyQualifiedName.matches(dataTypePattern));
          }
        };
        return IterableExtensions.<String>exists(dataTypePatterns, _function);
      }
    }
    if (!_matched) {
      if (type instanceof UnionType) {
        _matched=true;
        List _types = ((UnionType)type).types();
        final Function1<Type, Boolean> _function = new Function1<Type, Boolean>() {
          public Boolean apply(final Type it) {
            return Boolean.valueOf(TransformationJavaMethodsToModularHypergraph.this.isDataType(it, dataTypePatterns));
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
          _xifexpression = this.isDataType(_bound_1, dataTypePatterns);
        } else {
          _xifexpression = true;
        }
        return _xifexpression;
      }
    }
    return false;
  }
  
  /**
   * Determine if the given type is a data type.
   * 
   * @param type the type to be evaluated
   * @param dataTypes a list of data types
   * 
   * @return returns true if the given type is a data type and not a behavior type.
   */
  private boolean isClassDataType(final ITypeBinding typeBinding, final List<String> dataTypePatterns) {
    final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
      public Boolean apply(final String pattern) {
        String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(typeBinding);
        return Boolean.valueOf(_determineFullyQualifiedName.matches(pattern));
      }
    };
    return IterableExtensions.<String>exists(dataTypePatterns, _function);
  }
}
