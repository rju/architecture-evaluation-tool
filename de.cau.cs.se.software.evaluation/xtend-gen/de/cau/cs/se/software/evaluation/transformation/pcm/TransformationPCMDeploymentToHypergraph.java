package de.cau.cs.se.software.evaluation.transformation.pcm;

import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import java.util.Arrays;
import org.eclipse.core.runtime.IProgressMonitor;

@SuppressWarnings("all")
public class TransformationPCMDeploymentToHypergraph extends AbstractTransformation<System, ModularHypergraph> {
  public TransformationPCMDeploymentToHypergraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public ModularHypergraph generate(final System input) {
    throw new Error("Unresolved compilation problems:"
      + "\nAssemblyContext cannot be resolved to a type."
      + "\nBasicComponent cannot be resolved to a type."
      + "\nBasicComponent cannot be resolved to a type."
      + "\nOperationRequiredRole cannot be resolved to a type."
      + "\nThe method or field assemblyContexts__ComposedStructure is undefined for the type System"
      + "\nThe method or field entityName is undefined for the type EObject"
      + "\nThe method or field entityName is undefined for the type EObject"
      + "\nThe method or field encapsulatedComponent__AssemblyContext is undefined for the type EObject"
      + "\nThere is no context to infer the closure\'s argument types from. Consider typing the arguments or put the closures into a typed context."
      + "\nThere is no context to infer the closure\'s argument types from. Consider typing the arguments or put the closures into a typed context."
      + "\nforEach cannot be resolved"
      + "\ncreateNodeForComponent cannot be resolved"
      + "\nencapsulatedComponent__AssemblyContext cannot be resolved"
      + "\nentityName cannot be resolved"
      + "\ngetRequiredRoles_InterfaceRequiringEntity cannot be resolved"
      + "\nforEach cannot be resolved"
      + "\nrequiredInterface__OperationRequiredRole cannot be resolved");
  }
  
  private void _createNodeForComponent(final /* BasicComponent */Object component, final Module module) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field entityName is undefined for the type EObject"
      + "\nThe method or field entityName is undefined for the type EObject"
      + "\nThere is no context to infer the closure\'s argument types from. Consider typing the arguments or put the closures into a typed context."
      + "\nprovidedRoles_InterfaceProvidingEntity cannot be resolved"
      + "\nforEach cannot be resolved");
  }
  
  private void _createNodeForComponent(final /* CompleteComponentType */Object component, final Module module) {
    throw new Error("Unresolved compilation problems:"
      + "\nentityName cannot be resolved");
  }
  
  private void _createNodeForComponent(final /* CompositeComponent */Object component, final Module module) {
    throw new Error("Unresolved compilation problems:"
      + "\nentityName cannot be resolved");
  }
  
  private void _createNodeForComponent(final /* RepositoryComponent */Object component, final Module module) {
    String _plus = ("strange component type " + component);
    System.out.println(_plus);
  }
  
  @Override
  public int workEstimate(final System input) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field assemblyContexts__ComposedStructure is undefined for the type System"
      + "\nsize cannot be resolved"
      + "\n* cannot be resolved");
  }
  
  private void createNodeForComponent(final BasicComponent component, final Module module) {
    if (component != null) {
      _createNodeForComponent(component, module);
      return; else {
        throw new IllegalArgumentException("Unhandled parameter types: " +
          Arrays.<Object>asList(component, module).toString());
      }
    }
  }
  