package de.cau.cs.se.evaluation.architecture.transformation;

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace;

@SuppressWarnings("all")
public class TransformationHelper {
  public static Node deriveNode(final Node node) {
    final Node resultNode = HypergraphFactory.eINSTANCE.createNode();
    String _name = node.getName();
    resultNode.setName(_name);
    final NodeTrace derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace();
    derivedFrom.setNode(node);
    resultNode.setDerivedFrom(derivedFrom);
    return resultNode;
  }
  
  public static Edge deriveEdge(final Edge edge) {
    final Edge resultEdge = HypergraphFactory.eINSTANCE.createEdge();
    String _name = edge.getName();
    resultEdge.setName(_name);
    final EdgeTrace derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace();
    derivedFrom.setEdge(edge);
    resultEdge.setDerivedFrom(derivedFrom);
    return resultEdge;
  }
}
