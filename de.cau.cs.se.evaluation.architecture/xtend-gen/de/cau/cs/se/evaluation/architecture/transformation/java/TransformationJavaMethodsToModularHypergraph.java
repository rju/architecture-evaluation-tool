package de.cau.cs.se.evaluation.architecture.transformation.java;

import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;
import de.cau.cs.se.evaluation.architecture.transformation.java.GlobalJavaScope;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IType;
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
  }
  
  public Module createModuleForClass(final IType type) {
    final Module module = HypergraphFactory.eINSTANCE.createModule();
    String _fullyQualifiedName = type.getFullyQualifiedName();
    module.setName(_fullyQualifiedName);
    final TypeTrace derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace();
    derivedFrom.setType(type);
    module.setDerivedFrom(derivedFrom);
    return module;
  }
}
