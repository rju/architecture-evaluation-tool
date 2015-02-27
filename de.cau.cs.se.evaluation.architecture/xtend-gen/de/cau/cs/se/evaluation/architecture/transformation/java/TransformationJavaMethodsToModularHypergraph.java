package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import de.cau.cs.se.evaluation.architecture.transformation.java.GlobalJavaScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.HandleStatementForMethodAndFieldAccess;
import de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJDTDOMExtension;
import de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJavaExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationJavaMethodsToModularHypergraph implements ITransformation {
  private ModularHypergraph modularSystem;
  
  private final IScope globalScope;
  
  private final IJavaProject project;
  
  private final IProgressMonitor monitor;
  
  private final List<AbstractTypeDeclaration> classList;
  
  /**
   * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
   * 
   * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
   * @param scope the global scoper used to resolve classes during transformation
   * @param eclipse progress monitor
   */
  public TransformationJavaMethodsToModularHypergraph(final IJavaProject project, final GlobalJavaScope scope, final List<IType> classList, final IProgressMonitor monitor) {
    this.project = project;
    this.globalScope = scope;
    ArrayList<AbstractTypeDeclaration> _arrayList = new ArrayList<AbstractTypeDeclaration>();
    this.classList = _arrayList;
    for (final IType clazz : classList) {
      {
        final AbstractTypeDeclaration type = HypergraphJDTDOMExtension.getTypeDeclarationForType(clazz, monitor, project);
        boolean _notEquals = (!Objects.equal(type, null));
        if (_notEquals) {
          if ((type instanceof TypeDeclaration)) {
            boolean _isInterface = ((TypeDeclaration)type).isInterface();
            boolean _not = (!_isInterface);
            if (_not) {
              this.classList.add(type);
            }
          } else {
            this.classList.add(type);
          }
        }
      }
    }
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
    final Consumer<AbstractTypeDeclaration> _function = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        EList<Module> _modules = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getModules();
        Module _createModuleForClass = HypergraphJavaExtension.createModuleForClass(clazz);
        _modules.add(_createModuleForClass);
      }
    };
    this.classList.forEach(_function);
    final Consumer<AbstractTypeDeclaration> _function_1 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        EList<Edge> _edges = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getEdges();
        TransformationJavaMethodsToModularHypergraph.this.createEdgesForClassVariables(_edges, clazz);
      }
    };
    this.classList.forEach(_function_1);
    final Consumer<AbstractTypeDeclaration> _function_2 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        EList<Node> _nodes = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getNodes();
        TransformationJavaMethodsToModularHypergraph.this.createNodesForClassMethods(_nodes, clazz);
      }
    };
    this.classList.forEach(_function_2);
    final Consumer<AbstractTypeDeclaration> _function_3 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        EList<Node> _nodes = TransformationJavaMethodsToModularHypergraph.this.modularSystem.getNodes();
        TransformationJavaMethodsToModularHypergraph.this.createNodesForInplicitClassConstructors(_nodes, clazz);
      }
    };
    this.classList.forEach(_function_3);
    final Consumer<AbstractTypeDeclaration> _function_4 = new Consumer<AbstractTypeDeclaration>() {
      public void accept(final AbstractTypeDeclaration clazz) {
        TransformationJavaMethodsToModularHypergraph.this.createEdgesForInvocations(clazz, TransformationJavaMethodsToModularHypergraph.this.project);
      }
    };
    this.classList.forEach(_function_4);
  }
  
  /**
   * Create a node for implicit constructors of classes.
   */
  public void createNodesForInplicitClassConstructors(final EList<Node> nodes, final AbstractTypeDeclaration type) {
    String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(type);
    String _plus = (_determineFullQualifiedName + ".");
    SimpleName _name = type.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    final String tname = (_plus + _fullyQualifiedName);
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
          String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(type);
          return Boolean.valueOf(_name.equals(_determineFullQualifiedName));
        }
      };
      final Module module = IterableExtensions.<Module>findFirst(_modules, _function_1);
      final Node newNode = HypergraphJavaExtension.createNodeForImplicitConstructor(type);
      nodes.add(newNode);
      EList<Node> _nodes = module.getNodes();
      _nodes.add(newNode);
    }
  }
  
  /**
   * create edges for invocations and edges for variable access.
   */
  private void createEdgesForInvocations(final AbstractTypeDeclaration type, final IJavaProject project) {
    boolean _notEquals = (!Objects.equal(type, null));
    if (_notEquals) {
      List _modifiers = type.modifiers();
      final Iterable<Modifier> modifiers = Iterables.<Modifier>filter(_modifiers, Modifier.class);
      if ((type instanceof TypeDeclaration)) {
        boolean _and = false;
        boolean _isInterface = ((TypeDeclaration)type).isInterface();
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
          final HandleStatementForMethodAndFieldAccess handler = new HandleStatementForMethodAndFieldAccess(project);
          MethodDeclaration[] _methods = ((TypeDeclaration)type).getMethods();
          final Consumer<MethodDeclaration> _function_1 = new Consumer<MethodDeclaration>() {
            public void accept(final MethodDeclaration method) {
              Block _body = method.getBody();
              List _statements = _body.statements();
              final Consumer<Statement> _function = new Consumer<Statement>() {
                public void accept(final Statement it) {
                  handler.handle(TransformationJavaMethodsToModularHypergraph.this.modularSystem, type, method, it);
                }
              };
              _statements.forEach(_function);
            }
          };
          ((List<MethodDeclaration>)Conversions.doWrapArray(_methods)).forEach(_function_1);
        }
      } else {
        if ((type instanceof EnumDeclaration)) {
          final Function1<Modifier, Boolean> _function_2 = new Function1<Modifier, Boolean>() {
            public Boolean apply(final Modifier it) {
              return Boolean.valueOf(((Modifier) it).isAbstract());
            }
          };
          Modifier _findFirst_1 = IterableExtensions.<Modifier>findFirst(modifiers, _function_2);
          boolean _equals_1 = Objects.equal(_findFirst_1, null);
          if (_equals_1) {
            final HandleStatementForMethodAndFieldAccess handler_1 = new HandleStatementForMethodAndFieldAccess(project);
            List _bodyDeclarations = ((EnumDeclaration)type).bodyDeclarations();
            Iterable<MethodDeclaration> _filter = Iterables.<MethodDeclaration>filter(_bodyDeclarations, MethodDeclaration.class);
            final Consumer<MethodDeclaration> _function_3 = new Consumer<MethodDeclaration>() {
              public void accept(final MethodDeclaration method) {
                Block _body = method.getBody();
                List _statements = _body.statements();
                final Consumer<Statement> _function = new Consumer<Statement>() {
                  public void accept(final Statement it) {
                    handler_1.handle(TransformationJavaMethodsToModularHypergraph.this.modularSystem, type, method, it);
                  }
                };
                _statements.forEach(_function);
              }
            };
            _filter.forEach(_function_3);
          }
        }
      }
    }
  }
  
  /**
   * Resolve all methods of a given class.
   */
  private void createNodesForClassMethods(final EList<Node> nodes, final AbstractTypeDeclaration type) {
    if ((type instanceof TypeDeclaration)) {
      EList<Module> _modules = this.modularSystem.getModules();
      final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
        public Boolean apply(final Module it) {
          String _name = it.getName();
          String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(type);
          return Boolean.valueOf(_name.equals(_determineFullQualifiedName));
        }
      };
      final Module module = IterableExtensions.<Module>findFirst(_modules, _function);
      MethodDeclaration[] _methods = ((TypeDeclaration)type).getMethods();
      final Consumer<MethodDeclaration> _function_1 = new Consumer<MethodDeclaration>() {
        public void accept(final MethodDeclaration method) {
          final Node node = HypergraphJavaExtension.createNodeForMethod(method, type);
          nodes.add(node);
          EList<Node> _nodes = module.getNodes();
          _nodes.add(node);
        }
      };
      ((List<MethodDeclaration>)Conversions.doWrapArray(_methods)).forEach(_function_1);
    } else {
      if ((type instanceof EnumDeclaration)) {
        EList<Module> _modules_1 = this.modularSystem.getModules();
        final Function1<Module, Boolean> _function_2 = new Function1<Module, Boolean>() {
          public Boolean apply(final Module it) {
            String _name = it.getName();
            String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(type);
            return Boolean.valueOf(_name.equals(_determineFullQualifiedName));
          }
        };
        final Module module_1 = IterableExtensions.<Module>findFirst(_modules_1, _function_2);
        List _bodyDeclarations = ((EnumDeclaration)type).bodyDeclarations();
        Iterable<MethodDeclaration> _filter = Iterables.<MethodDeclaration>filter(_bodyDeclarations, MethodDeclaration.class);
        final Consumer<MethodDeclaration> _function_3 = new Consumer<MethodDeclaration>() {
          public void accept(final MethodDeclaration method) {
            final Node node = HypergraphJavaExtension.createNodeForMethod(method, type);
            nodes.add(node);
            EList<Node> _nodes = module_1.getNodes();
            _nodes.add(node);
          }
        };
        _filter.forEach(_function_3);
      }
    }
  }
  
  /**
   * Create edges for all variables in the given type.
   */
  private void createEdgesForClassVariables(final EList<Edge> edges, final AbstractTypeDeclaration type) {
    if ((type instanceof TypeDeclaration)) {
      FieldDeclaration[] _fields = ((TypeDeclaration)type).getFields();
      final Consumer<FieldDeclaration> _function = new Consumer<FieldDeclaration>() {
        public void accept(final FieldDeclaration field) {
          Edge _createEdgeForField = HypergraphJavaExtension.createEdgeForField(field, type);
          edges.add(_createEdgeForField);
        }
      };
      ((List<FieldDeclaration>)Conversions.doWrapArray(_fields)).forEach(_function);
    } else {
      if ((type instanceof EnumDeclaration)) {
        List _bodyDeclarations = ((EnumDeclaration)type).bodyDeclarations();
        Iterable<FieldDeclaration> _filter = Iterables.<FieldDeclaration>filter(_bodyDeclarations, FieldDeclaration.class);
        final Consumer<FieldDeclaration> _function_1 = new Consumer<FieldDeclaration>() {
          public void accept(final FieldDeclaration field) {
            Edge _createEdgeForField = HypergraphJavaExtension.createEdgeForField(field, type);
            edges.add(_createEdgeForField);
          }
        };
        _filter.forEach(_function_1);
      }
    }
  }
}
