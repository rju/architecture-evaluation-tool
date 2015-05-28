package de.cau.cs.se.evaluation.architecture.transformation.java;

import de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

@SuppressWarnings("all")
public class JavaHypergraphElementFactory {
  /**
   * Create a module for the given class or interface.
   * 
   * @param type referencing a class or interface
   * 
   * @return one new module
   */
  public static Module createModuleForTypeBinding(final ITypeBinding type) {
    final Module module = HypergraphFactory.eINSTANCE.createModule();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(type);
    module.setName(_determineFullyQualifiedName);
    final TypeTrace derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace();
    derivedFrom.setType(type);
    module.setDerivedFrom(derivedFrom);
    return module;
  }
  
  /**
   * Create a node for a given method.
   * 
   * @param binding
   * 
   * @return a node
   */
  public static Node createNodeForMethod(final IMethodBinding binding) {
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(binding);
    node.setName(_determineFullyQualifiedName);
    final MethodTrace derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace();
    derivedFrom.setMethod(binding);
    node.setDerivedFrom(derivedFrom);
    return node;
  }
  
  /**
   * Create a node for a class' implicit constructor
   * 
   * @param type is the class containing the variable declaration
   * 
   * @return a node for the implicit constructor
   */
  public static Node createNodeForImplicitConstructor(final ITypeBinding type) {
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(type);
    String _plus = (_determineFullyQualifiedName + ".");
    String _name = type.getName();
    String _plus_1 = (_plus + _name);
    node.setName(_plus_1);
    final TypeTrace derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace();
    derivedFrom.setType(type);
    node.setDerivedFrom(derivedFrom);
    return node;
  }
  
  /**
   * Create a node for a method binding.
   * 
   * @param binding to a method
   * 
   * @return node derived from that binding
   */
  public static Node createNodeForSuperConstructorInvocation(final IMethodBinding binding) {
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(binding);
    node.setName(_determineFullyQualifiedName);
    final MethodTrace derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace();
    derivedFrom.setMethod(binding);
    node.setDerivedFrom(derivedFrom);
    return node;
  }
  
  /**
   * Create a data edge for an reference of a super class property. Used with framework classes.
   * 
   * @param variableBinding is the variable declaration fragment containing the name
   * 
   * @return the edge
   */
  public static Edge createDataEdge(final IVariableBinding variableBinding) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(variableBinding);
    edge.setName(_determineFullyQualifiedName);
    final FieldTrace derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace();
    derivedFrom.setField(variableBinding);
    edge.setDerivedFrom(derivedFrom);
    return edge;
  }
  
  /**
   * Create a call edge between a method and an constructor invocation "this(...)".
   * This can occur in constructors only.
   * 
   * @param callerType is the class containing the caller method
   * @param callerMethod method where the call originates
   * @param calleeType is the class containing the callee method
   * @param callee the called method
   * 
   * @return the edge
   */
  public static Edge createCallEdge(final AbstractTypeDeclaration callerType, final MethodDeclaration callerMethod, final AbstractTypeDeclaration calleeType, final ConstructorInvocation calleeMethod) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(callerType, callerMethod);
    String _plus = (_determineFullyQualifiedName + "::");
    String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(calleeType, calleeMethod);
    String _plus_1 = (_plus + _determineFullyQualifiedName_1);
    edge.setName(_plus_1);
    final CallerCalleeTrace derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace();
    IMethodBinding _resolveBinding = callerMethod.resolveBinding();
    derivedFrom.setCaller(_resolveBinding);
    IMethodBinding _resolveConstructorBinding = calleeMethod.resolveConstructorBinding();
    derivedFrom.setCallee(_resolveConstructorBinding);
    edge.setDerivedFrom(derivedFrom);
    return edge;
  }
  
  /**
   * Create a call edge between a method and a super constructor invocation "super(...)".
   * This can occur in constructors only.
   * 
   * @param callerType is the class containing the caller method
   * @param callerMethod method where the call originates
   * @param calleeType is the class containing the callee method
   * @param calleeMethod the called method
   * 
   * @return the edge
   */
  public static Edge createCallEdge(final AbstractTypeDeclaration callerType, final MethodDeclaration callerMethod, final AbstractTypeDeclaration calleeType, final SuperConstructorInvocation calleeMethod) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(callerType, callerMethod);
    String _plus = (_determineFullyQualifiedName + "::");
    String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(calleeType, calleeMethod);
    String _plus_1 = (_plus + _determineFullyQualifiedName_1);
    edge.setName(_plus_1);
    final CallerCalleeTrace derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace();
    derivedFrom.setCaller(callerMethod);
    derivedFrom.setCallee(calleeMethod);
    edge.setDerivedFrom(derivedFrom);
    return edge;
  }
  
  /**
   * Create a call edge between two methods.
   * 
   * @param callerType is the class containing the caller method
   * @param callerMethod method where the call originates
   * @param callee the called method in form of a method binding
   * 
   * @return the edge
   */
  public static Edge createCallEdge(final AbstractTypeDeclaration callerType, final MethodDeclaration callerMethod, final IMethodBinding callee) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(callerType, callerMethod);
    String _plus = (_determineFullyQualifiedName + "::");
    String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(callee);
    String _plus_1 = (_plus + _determineFullyQualifiedName_1);
    edge.setName(_plus_1);
    final CallerCalleeTrace derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace();
    IMethodBinding _resolveBinding = callerMethod.resolveBinding();
    derivedFrom.setCaller(_resolveBinding);
    derivedFrom.setCallee(callee);
    edge.setDerivedFrom(derivedFrom);
    return edge;
  }
  
  /**
   * Create a call edge between two methods.
   * 
   * @param caller method where the call originates
   * @param callee the called method in form of a method binding
   * 
   * @return the edge
   */
  public static Edge createCallEdge(final IMethodBinding caller, final IMethodBinding callee) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(caller);
    String _plus = (_determineFullyQualifiedName + "::");
    String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(callee);
    String _plus_1 = (_plus + _determineFullyQualifiedName_1);
    edge.setName(_plus_1);
    final CallerCalleeTrace derivedFrom = HypergraphFactory.eINSTANCE.createCallerCalleeTrace();
    derivedFrom.setCaller(caller);
    derivedFrom.setCallee(callee);
    edge.setDerivedFrom(derivedFrom);
    return edge;
  }
}
