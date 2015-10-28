package de.cau.cs.se.software.evaluation.transformation.pcm;

import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleReference;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import java.util.Arrays;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.CompleteComponentType;
import org.palladiosimulator.pcm.repository.CompositeComponent;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RequiredRole;

@SuppressWarnings("all")
public class TransformationPCMDeploymentToHypergraph extends AbstractTransformation<org.palladiosimulator.pcm.system.System, ModularHypergraph> {
  public TransformationPCMDeploymentToHypergraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public ModularHypergraph transform(final org.palladiosimulator.pcm.system.System input) {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.result = _createModularHypergraph;
    EList<AssemblyContext> _assemblyContexts__ComposedStructure = input.getAssemblyContexts__ComposedStructure();
    final Consumer<AssemblyContext> _function = (AssemblyContext assemblyContext) -> {
      String _entityName = assemblyContext.getEntityName();
      String _plus = ("Module " + _entityName);
      System.out.println(_plus);
      String _entityName_1 = assemblyContext.getEntityName();
      final Module module = HypergraphCreationHelper.createModule(this.result, _entityName_1, assemblyContext);
      RepositoryComponent _encapsulatedComponent__AssemblyContext = assemblyContext.getEncapsulatedComponent__AssemblyContext();
      this.createNodeForComponent(_encapsulatedComponent__AssemblyContext, module);
    };
    _assemblyContexts__ComposedStructure.forEach(_function);
    EList<Module> _modules = this.result.getModules();
    final Consumer<Module> _function_1 = (Module module) -> {
      ModuleReference _derivedFrom = module.getDerivedFrom();
      EObject _element = ((ModelElementTrace) _derivedFrom).getElement();
      final RepositoryComponent component = ((AssemblyContext) _element).getEncapsulatedComponent__AssemblyContext();
      if ((component instanceof BasicComponent)) {
        String _entityName = ((BasicComponent) component).getEntityName();
        String _plus = (">> " + _entityName);
        System.out.println(_plus);
        EList<RequiredRole> _requiredRoles_InterfaceRequiringEntity = ((BasicComponent)component).getRequiredRoles_InterfaceRequiringEntity();
        final Consumer<RequiredRole> _function_2 = (RequiredRole required) -> {
          boolean _matched = false;
          if (!_matched) {
            if (required instanceof OperationRequiredRole) {
              _matched=true;
              ((OperationRequiredRole)required).getRequiredInterface__OperationRequiredRole();
            }
          }
          if (!_matched) {
            System.out.println(("other role " + required));
          }
        };
        _requiredRoles_InterfaceRequiringEntity.forEach(_function_2);
      }
    };
    _modules.forEach(_function_1);
    return this.result;
  }
  
  private void _createNodeForComponent(final BasicComponent component, final Module module) {
    EList<ProvidedRole> _providedRoles_InterfaceProvidingEntity = component.getProvidedRoles_InterfaceProvidingEntity();
    final Consumer<ProvidedRole> _function = (ProvidedRole provided) -> {
      String _entityName = provided.getEntityName();
      String _plus = ("node " + _entityName);
      System.out.println(_plus);
      String _entityName_1 = provided.getEntityName();
      HypergraphCreationHelper.createNode(this.result, module, _entityName_1, provided);
    };
    _providedRoles_InterfaceProvidingEntity.forEach(_function);
  }
  
  private void _createNodeForComponent(final CompleteComponentType component, final Module module) {
    String _entityName = component.getEntityName();
    HypergraphCreationHelper.createNode(this.result, module, _entityName, component);
  }
  
  private void _createNodeForComponent(final CompositeComponent component, final Module module) {
    String _entityName = component.getEntityName();
    HypergraphCreationHelper.createNode(this.result, module, _entityName, component);
  }
  
  private void _createNodeForComponent(final RepositoryComponent component, final Module module) {
    System.out.println(("strange component type " + component));
  }
  
  private void createNodeForComponent(final RepositoryComponent component, final Module module) {
    if (component instanceof BasicComponent) {
      _createNodeForComponent((BasicComponent)component, module);
      return;
    } else if (component instanceof CompositeComponent) {
      _createNodeForComponent((CompositeComponent)component, module);
      return;
    } else if (component instanceof CompleteComponentType) {
      _createNodeForComponent((CompleteComponentType)component, module);
      return;
    } else if (component != null) {
      _createNodeForComponent(component, module);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(component, module).toString());
    }
  }
}
