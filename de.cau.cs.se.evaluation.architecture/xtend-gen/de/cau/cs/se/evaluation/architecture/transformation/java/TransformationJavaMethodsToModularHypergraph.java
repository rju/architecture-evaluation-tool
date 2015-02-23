package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;
import de.cau.cs.se.evaluation.architecture.transformation.java.GlobalJavaScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.HandleStatementForMethodAndFieldAccess;
import de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJavaExtensions;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaLocalScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.JavaPackageScope;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
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
    final Consumer<IType> _function = new Consumer<IType>() {
      public void accept(final IType clazz) {
        EList<Module> _modules = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getModules();
        Module _createModuleForClass = HypergraphJavaExtensions.createModuleForClass(clazz);
        _modules.add(_createModuleForClass);
      }
    };
    this.classList.forEach(_function);
    final Consumer<IType> _function_1 = new Consumer<IType>() {
      public void accept(final IType clazz) {
        EList<Edge> _edges = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getEdges();
        TransformationJavaMethodsToModularHypergraph.this.createEdgesForClassVariables(_edges, clazz);
      }
    };
    this.classList.forEach(_function_1);
    final Consumer<IType> _function_2 = new Consumer<IType>() {
      public void accept(final IType clazz) {
        EList<Node> _nodes = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getNodes();
        TransformationJavaMethodsToModularHypergraph.this.createNodesForClassMethods(_nodes, clazz);
      }
    };
    this.classList.forEach(_function_2);
    final Consumer<IType> _function_3 = new Consumer<IType>() {
      public void accept(final IType clazz) {
        EList<Node> _nodes = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getNodes();
        TransformationJavaMethodsToModularHypergraph.this.createNodesForInplicitClassConstructors(_nodes, clazz);
      }
    };
    this.classList.forEach(_function_3);
    final Consumer<IType> _function_4 = new Consumer<IType>() {
      public void accept(final IType clazz) {
        TransformationJavaMethodsToModularHypergraph.this.createEdgesForInvocations(clazz);
      }
    };
    this.classList.forEach(_function_4);
  }
  
  public void createNodesForInplicitClassConstructors(final EList<Node> nodes, final IType type) {
    String _fullyQualifiedName = type.getFullyQualifiedName();
    String _plus = (_fullyQualifiedName + ".");
    String _elementName = type.getElementName();
    final String tname = (_plus + _elementName);
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node node) {
        String _name = node.getName();
        return Boolean.valueOf(_name.equals(tname));
      }
    };
    boolean _exists = IterableExtensions.<Node>exists(nodes, _function);
    boolean _not = (!_exists);
    if (_not) {
      EList<Module> _modules = this.modularSystem.getModules();
      final Function1<Module, Boolean> _function_1 = new Function1<Module, Boolean>() {
        public Boolean apply(final Module it) {
          String _name = it.getName();
          String _fullyQualifiedName = type.getFullyQualifiedName();
          return Boolean.valueOf(_name.equals(_fullyQualifiedName));
        }
      };
      final Module module = IterableExtensions.<Module>findFirst(_modules, _function_1);
      final Node newNode = HypergraphFactory.eINSTANCE.createNode();
      String _fullyQualifiedName_1 = type.getFullyQualifiedName();
      String _plus_1 = (_fullyQualifiedName_1 + ".");
      String _elementName_1 = type.getElementName();
      String _plus_2 = (_plus_1 + _elementName_1);
      newNode.setName(_plus_2);
      final TypeTrace derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace();
      derivedFrom.setType(type);
      newNode.setDerivedFrom(derivedFrom);
      nodes.add(newNode);
      EList<Node> _nodes = module.getNodes();
      _nodes.add(newNode);
    }
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
          final Consumer<MethodDeclaration> _function_1 = new Consumer<MethodDeclaration>() {
            public void accept(final MethodDeclaration method) {
              Block _body = method.getBody();
              List _statements = _body.statements();
              final Consumer<Statement> _function = new Consumer<Statement>() {
                public void accept(final Statement it) {
                  handler.handle(TransformationJavaMethodsToModularHypergraph.this.modularSystem, declaredType, method, it);
                }
              };
              _statements.forEach(_function);
            }
          };
          ((List<MethodDeclaration>)Conversions.doWrapArray(_methods)).forEach(_function_1);
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
      final Consumer<IMethod> _function_1 = new Consumer<IMethod>() {
        public void accept(final IMethod method) {
          final Node node = HypergraphJavaExtensions.createNodeForMethod(method);
          nodes.add(node);
          EList<Node> _nodes = module.getNodes();
          _nodes.add(node);
        }
      };
      ((List<IMethod>)Conversions.doWrapArray(_methods)).forEach(_function_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Create edges for all variables in the given type.
   */
  private void createEdgesForClassVariables(final EList<Edge> edges, final IType type) {
    try {
      IField[] _fields = type.getFields();
      final Consumer<IField> _function = new Consumer<IField>() {
        public void accept(final IField field) {
          Edge _createEdgeForField = HypergraphJavaExtensions.createEdgeForField(field);
          edges.add(_createEdgeForField);
        }
      };
      ((List<IField>)Conversions.doWrapArray(_fields)).forEach(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
