package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;
import de.cau.cs.se.evaluation.architecture.transformation.java.GlobalJavaScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.HandleStatementForMethodAndFieldAccess;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaLocalScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaPackageScope;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class TransformationJavaMethodsToModularHypergraph implements ITransformation {
  private ModularHypergraph modularSystem;
  
  private final IScope globalScope;
  
  private final IProgressMonitor monitor;
  
  private final List<IType> classList;
  
  /**
   * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
   * 
   * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
   * @param scope the global scoper used to resolve classes during transformation
   */
  public TransformationJavaMethodsToModularHypergraph(final IScope scope, final List<IType> classList) {
    this.globalScope = scope;
    this.classList = classList;
    this.monitor = null;
  }
  
  /**
   * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
   * 
   * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
   * @param scope the global scoper used to resolve classes during transformation
   * @param eclipse progress monitor
   */
  public TransformationJavaMethodsToModularHypergraph(final GlobalJavaScope scope, final List<IType> classList, final IProgressMonitor monitor) {
    this.globalScope = scope;
    this.classList = classList;
    this.monitor = monitor;
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
    final Procedure1<IType> _function = new Procedure1<IType>() {
      public void apply(final IType clazz) {
        EList<Module> _modules = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getModules();
        Module _createModuleForClass = TransformationJavaMethodsToModularHypergraph.this.createModuleForClass(clazz);
        _modules.add(_createModuleForClass);
      }
    };
    IterableExtensions.<IType>forEach(this.classList, _function);
    final Procedure1<IType> _function_1 = new Procedure1<IType>() {
      public void apply(final IType clazz) {
        EList<Edge> _edges = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getEdges();
        TransformationJavaMethodsToModularHypergraph.this.createEdgesForClassVariables(_edges, clazz);
      }
    };
    IterableExtensions.<IType>forEach(this.classList, _function_1);
    final Procedure1<IType> _function_2 = new Procedure1<IType>() {
      public void apply(final IType clazz) {
        EList<Node> _nodes = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getNodes();
        TransformationJavaMethodsToModularHypergraph.this.createNodesForClassMethods(_nodes, clazz);
      }
    };
    IterableExtensions.<IType>forEach(this.classList, _function_2);
    final Procedure1<IType> _function_3 = new Procedure1<IType>() {
      public void apply(final IType clazz) {
        TransformationJavaMethodsToModularHypergraph.this.createEdgesForInvocations(clazz);
      }
    };
    IterableExtensions.<IType>forEach(this.classList, _function_3);
  }
  
  /**
   * create edges for invocations and edges for variable access.
   */
  private void createEdgesForInvocations(final IType type) {
    try {
      System.out.println("XX");
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
      final HandleStatementForMethodAndFieldAccess handler = new HandleStatementForMethodAndFieldAccess(scope);
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
                  handler.handle(TransformationJavaMethodsToModularHypergraph.this.modularSystem, declaredType, method, it);
                }
              };
              IterableExtensions.<Statement>forEach(_statements, _function);
            }
          };
          IterableExtensions.<MethodDeclaration>forEach(((Iterable<MethodDeclaration>)Conversions.doWrapArray(_methods)), _function_1);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Resolve all methods of a given class.
   */
  private void createNodesForClassMethods(final EList<Node> nodes, final IType type) {
    try {
      EList<Module> _modules = this.modularSystem.getModules();
      final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
        public Boolean apply(final Module it) {
          String _name = it.getName();
          String _fullyQualifiedName = type.getFullyQualifiedName();
          return Boolean.valueOf(_name.equals(_fullyQualifiedName));
        }
      };
      final Module module = IterableExtensions.<Module>findFirst(_modules, _function);
      IMethod[] _methods = type.getMethods();
      final Procedure1<IMethod> _function_1 = new Procedure1<IMethod>() {
        public void apply(final IMethod method) {
          final Node node = TransformationJavaMethodsToModularHypergraph.this.createNodeForMethod(method);
          nodes.add(node);
          EList<Node> _nodes = module.getNodes();
          _nodes.add(node);
        }
      };
      IterableExtensions.<IMethod>forEach(((Iterable<IMethod>)Conversions.doWrapArray(_methods)), _function_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Create a node for a given method.
   */
  private Node createNodeForMethod(final IMethod method) {
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    ICompilationUnit _compilationUnit = method.getCompilationUnit();
    IJavaElement _parent = _compilationUnit.getParent();
    String _elementName = _parent.getElementName();
    String _plus = (_elementName + ".");
    IJavaElement _parent_1 = method.getParent();
    String _elementName_1 = _parent_1.getElementName();
    String _plus_1 = (_plus + _elementName_1);
    String _plus_2 = (_plus_1 + ".");
    String _elementName_2 = method.getElementName();
    String _plus_3 = (_plus_2 + _elementName_2);
    node.setName(_plus_3);
    final MethodTrace derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace();
    derivedFrom.setMethod(method);
    node.setDerivedFrom(derivedFrom);
    return node;
  }
  
  /**
   * Create edges for all variables in the given type.
   */
  private void createEdgesForClassVariables(final EList<Edge> edges, final IType type) {
    try {
      IField[] _fields = type.getFields();
      final Procedure1<IField> _function = new Procedure1<IField>() {
        public void apply(final IField field) {
          Edge _createEdgeForField = TransformationJavaMethodsToModularHypergraph.this.createEdgeForField(field);
          edges.add(_createEdgeForField);
        }
      };
      IterableExtensions.<IField>forEach(((Iterable<IField>)Conversions.doWrapArray(_fields)), _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Create for one IField an edge.
   */
  private Edge createEdgeForField(final IField field) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    ICompilationUnit _compilationUnit = field.getCompilationUnit();
    IJavaElement _parent = _compilationUnit.getParent();
    String _elementName = _parent.getElementName();
    String _plus = (_elementName + ".");
    IJavaElement _parent_1 = field.getParent();
    String _elementName_1 = _parent_1.getElementName();
    String _plus_1 = (_plus + _elementName_1);
    String _plus_2 = (_plus_1 + ".");
    String _elementName_2 = field.getElementName();
    String _plus_3 = (_plus_2 + _elementName_2);
    edge.setName(_plus_3);
    final FieldTrace derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace();
    derivedFrom.setField(field);
    edge.setDerivedFrom(derivedFrom);
    return edge;
  }
  
  /**
   * Create a module for each class in the list. Pointing to the class as derived from
   * element.
   * 
   * @return one new module
   */
  private Module createModuleForClass(final IType type) {
    final Module module = HypergraphFactory.eINSTANCE.createModule();
    String _fullyQualifiedName = type.getFullyQualifiedName();
    module.setName(_fullyQualifiedName);
    final TypeTrace derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace();
    derivedFrom.setType(type);
    module.setDerivedFrom(derivedFrom);
    return module;
  }
}
