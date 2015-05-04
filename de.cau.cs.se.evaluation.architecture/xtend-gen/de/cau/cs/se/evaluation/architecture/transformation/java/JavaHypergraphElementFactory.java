package de.cau.cs.se.evaluation.architecture.transformation.java;

import de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

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
   * @param type
   * @param method
   * 
   * @return a node
   * 
   * @deprecated
   */
  public static Node createNodeForMethod(final AbstractTypeDeclaration type, final MethodDeclaration method) {
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(type, method);
    node.setName(_determineFullyQualifiedName);
    final MethodTrace derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace();
    derivedFrom.setMethod(method);
    node.setDerivedFrom(derivedFrom);
    return node;
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
   * Create for one VariableDeclarationFragment an data edge. The fragment belongs to
   * an FieldDeclaration.
   * 
   * @param type is the class containing the variable declaration
   * @param fragment is the variable declaration fragment containing the name
   * 
   * @return the edge
   */
  public static Edge createDataEdge(final AbstractTypeDeclaration type, final VariableDeclarationFragment fragment) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(type, fragment);
    edge.setName(_determineFullyQualifiedName);
    final FieldTrace derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace();
    derivedFrom.setField(fragment);
    edge.setDerivedFrom(derivedFrom);
    return edge;
  }
  
  /**
   * Create a call edge between two methods.
   * 
   * @param callerType is the class containing the caller method
   * @param callerMethod method where the call originates
   * @param calleeType is the class containing the callee method
   * @param callee the called method
   * 
   * @return the edge
   */
  public static Edge createCallEdge(final AbstractTypeDeclaration callerType, final MethodDeclaration callerMethod, final AbstractTypeDeclaration calleeType, final MethodInvocation calleeMethod) {
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
    derivedFrom.setCaller(callerMethod);
    derivedFrom.setCallee(callee);
    edge.setDerivedFrom(derivedFrom);
    return edge;
  }
  
  /**
   * Find the node for the given constructor invocation.
   * 
   * @param nodes
   * @param invocation
   */
  public static Node findNodeForConstructoreInvocation(final EList<Node> nodes, final ConstructorInvocation invocation) {
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node it) {
        NodeReference _derivedFrom = it.getDerivedFrom();
        Object _method = ((MethodTrace) _derivedFrom).getMethod();
        IMethodBinding _resolveBinding = ((MethodDeclaration) _method).resolveBinding();
        IMethodBinding _resolveConstructorBinding = invocation.resolveConstructorBinding();
        return Boolean.valueOf(_resolveBinding.isSubsignature(_resolveConstructorBinding));
      }
    };
    return IterableExtensions.<Node>findFirst(nodes, _function);
  }
}
