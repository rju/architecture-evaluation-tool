package de.cau.cs.se.evaluation.architecture.transformation.java;

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class HypergraphJavaExtensions {
  public static Node findNodeForMethod(final ModularHypergraph graph, final TypeDeclaration clazz, final MethodDeclaration method) {
    EList<Node> _nodes = graph.getNodes();
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node it) {
        return Boolean.valueOf(HypergraphJavaExtensions.isDerivedFromMethod(it, clazz, method));
      }
    };
    return IterableExtensions.<Node>findFirst(_nodes, _function);
  }
  
  public static boolean isDerivedFromMethod(final Node node, final TypeDeclaration clazz, final MethodDeclaration declaration) {
    NodeReference _derivedFrom = node.getDerivedFrom();
    if ((_derivedFrom instanceof MethodTrace)) {
      String _name = node.getName();
      String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(clazz, declaration);
      return _name.equals(_determineFullQualifiedName);
    }
    return false;
  }
  
  /**
   * Create a node for a given method.
   */
  public static Node createNodeForMethod(final IMethod method) {
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
   * Create for one IField an edge.
   */
  public static Edge createEdgeForField(final IField field) {
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
  public static Module createModuleForClass(final IType type) {
    final Module module = HypergraphFactory.eINSTANCE.createModule();
    String _fullyQualifiedName = type.getFullyQualifiedName();
    module.setName(_fullyQualifiedName);
    final TypeTrace derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace();
    derivedFrom.setType(type);
    module.setDerivedFrom(derivedFrom);
    return module;
  }
}
