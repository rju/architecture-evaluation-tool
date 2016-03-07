package de.cau.cs.se.software.evaluation.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleReference;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.hypergraph.TypeTrace;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.java.JavaASTEvaluation;
import de.cau.cs.se.software.evaluation.transformation.java.JavaHypergraphElementFactory;
import de.cau.cs.se.software.evaluation.transformation.java.NameResolutionHelper;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
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
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationJavaMethodsToModularHypergraph extends AbstractTransformation<List<AbstractTypeDeclaration>, ModularHypergraph> {
  private final IJavaProject project;
  
  private final List<String> dataTypePatterns;
  
  private final List<String> observedSystemPatterns;
  
  /**
   * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
   * 
   * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
   * @param scope the global scoper used to resolve classes during transformation
   * @param eclipse progress monitor
   */
  public TransformationJavaMethodsToModularHypergraph(final IJavaProject project, final List<String> dataTypePatterns, final List<String> observedSystemPatterns, final IProgressMonitor monitor) {
    super(monitor);
    this.project = project;
    this.dataTypePatterns = dataTypePatterns;
    this.observedSystemPatterns = observedSystemPatterns;
  }
  
  /**
   * Main transformation routine.
   */
  @Override
  public ModularHypergraph generate(final List<AbstractTypeDeclaration> input) {
    String _elementName = this.project.getElementName();
    String _plus = ("Process java project " + _elementName);
    int _size = input.size();
    int _size_1 = input.size();
    int _plus_1 = (_size + _size_1);
    int _size_2 = input.size();
    int _plus_2 = (_plus_1 + _size_2);
    int _size_3 = input.size();
    int _plus_3 = (_plus_2 + _size_3);
    int _size_4 = input.size();
    int _plus_4 = (_plus_3 + _size_4);
    this.monitor.beginTask(_plus, _plus_4);
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.result = _createModularHypergraph;
    final Consumer<AbstractTypeDeclaration> _function = (AbstractTypeDeclaration clazz) -> {
      EList<Module> _modules = this.result.getModules();
      ITypeBinding _resolveBinding = clazz.resolveBinding();
      Module _createModuleForTypeBinding = JavaHypergraphElementFactory.createModuleForTypeBinding(_resolveBinding, EModuleKind.SYSTEM);
      _modules.add(_createModuleForTypeBinding);
    };
    input.forEach(_function);
    int _size_5 = input.size();
    this.monitor.worked(_size_5);
    final Consumer<AbstractTypeDeclaration> _function_1 = (AbstractTypeDeclaration clazz) -> {
      EList<Edge> _edges = this.result.getEdges();
      this.createEdgesForClassProperties(_edges, clazz, this.dataTypePatterns);
    };
    input.forEach(_function_1);
    int _size_6 = input.size();
    this.monitor.worked(_size_6);
    final Consumer<AbstractTypeDeclaration> _function_2 = (AbstractTypeDeclaration clazz) -> {
      EList<Node> _nodes = this.result.getNodes();
      this.createNodesForMethods(_nodes, clazz);
    };
    input.forEach(_function_2);
    int _size_7 = input.size();
    this.monitor.worked(_size_7);
    final Function1<AbstractTypeDeclaration, Boolean> _function_3 = (AbstractTypeDeclaration clazz) -> {
      return Boolean.valueOf(this.hasImplicitConstructor(clazz));
    };
    Iterable<AbstractTypeDeclaration> _filter = IterableExtensions.<AbstractTypeDeclaration>filter(input, _function_3);
    final Consumer<AbstractTypeDeclaration> _function_4 = (AbstractTypeDeclaration clazz) -> {
      ITypeBinding _resolveBinding = clazz.resolveBinding();
      final Node node = JavaHypergraphElementFactory.createNodeForImplicitConstructor(_resolveBinding);
      EList<Module> _modules = this.result.getModules();
      final Function1<Module, Boolean> _function_5 = (Module it) -> {
        ModuleReference _derivedFrom = it.getDerivedFrom();
        Object _type = ((TypeTrace) _derivedFrom).getType();
        ITypeBinding _resolveBinding_1 = clazz.resolveBinding();
        return Boolean.valueOf(((ITypeBinding) _type).isSubTypeCompatible(_resolveBinding_1));
      };
      final Module module = IterableExtensions.<Module>findFirst(_modules, _function_5);
      EList<Node> _nodes = module.getNodes();
      _nodes.add(node);
      EList<Node> _nodes_1 = this.result.getNodes();
      _nodes_1.add(node);
    };
    _filter.forEach(_function_4);
    int _size_8 = input.size();
    this.monitor.worked(_size_8);
    final Consumer<AbstractTypeDeclaration> _function_5 = (AbstractTypeDeclaration clazz) -> {
      this.resolveEdges(this.result, this.dataTypePatterns, clazz);
    };
    input.forEach(_function_5);
    int _size_9 = input.size();
    this.monitor.worked(_size_9);
    return this.result;
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
      final Consumer<MethodDeclaration> _function = (MethodDeclaration method) -> {
        EList<Node> _nodes = graph.getNodes();
        final Function1<Node, Boolean> _function_1 = (Node it) -> {
          NodeReference _derivedFrom = it.getDerivedFrom();
          Object _method = ((MethodTrace) _derivedFrom).getMethod();
          IMethodBinding _resolveBinding = method.resolveBinding();
          return Boolean.valueOf(((IMethodBinding) _method).isEqualTo(_resolveBinding));
        };
        final Node node = IterableExtensions.<Node>findFirst(_nodes, _function_1);
        JavaASTEvaluation.evaluteMethod(graph, dataTypePatterns, node, clazz, method);
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
        final Function1<MethodDeclaration, Boolean> _function = (MethodDeclaration method) -> {
          return Boolean.valueOf(method.isConstructor());
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
      EList<Module> _modules = this.result.getModules();
      final Function1<Module, Boolean> _function = (Module it) -> {
        ModuleReference _derivedFrom = it.getDerivedFrom();
        Object _type = ((TypeTrace) _derivedFrom).getType();
        ITypeBinding _resolveBinding = ((TypeDeclaration)type).resolveBinding();
        return Boolean.valueOf(((ITypeBinding) _type).isSubTypeCompatible(_resolveBinding));
      };
      final Module module = IterableExtensions.<Module>findFirst(_modules, _function);
      MethodDeclaration[] _methods = ((TypeDeclaration)type).getMethods();
      final Consumer<MethodDeclaration> _function_1 = (MethodDeclaration method) -> {
        IMethodBinding _resolveBinding = method.resolveBinding();
        final Node node = JavaHypergraphElementFactory.createNodeForMethod(_resolveBinding);
        nodes.add(node);
        EList<Node> _nodes = module.getNodes();
        _nodes.add(node);
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
      final Consumer<FieldDeclaration> _function = (FieldDeclaration field) -> {
        Type _type = field.getType();
        boolean _isDataType = this.isDataType(_type, dataTypePatterns);
        if (_isDataType) {
          List _fragments = field.fragments();
          final Consumer<Object> _function_1 = (Object fragment) -> {
            IVariableBinding _resolveBinding = ((VariableDeclarationFragment) fragment).resolveBinding();
            Edge _createDataEdge = JavaHypergraphElementFactory.createDataEdge(_resolveBinding);
            edges.add(_createDataEdge);
          };
          _fragments.forEach(_function_1);
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
        final Function1<String, Boolean> _function = (String dataTypePattern) -> {
          ITypeBinding _resolveBinding = ((SimpleType)type).resolveBinding();
          String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_resolveBinding);
          return Boolean.valueOf(_determineFullyQualifiedName.matches(dataTypePattern));
        };
        return IterableExtensions.<String>exists(dataTypePatterns, _function);
      }
    }
    if (!_matched) {
      if (type instanceof UnionType) {
        _matched=true;
        List _types = ((UnionType)type).types();
        final Function1<Type, Boolean> _function = (Type it) -> {
          return Boolean.valueOf(this.isDataType(it, dataTypePatterns));
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
}
