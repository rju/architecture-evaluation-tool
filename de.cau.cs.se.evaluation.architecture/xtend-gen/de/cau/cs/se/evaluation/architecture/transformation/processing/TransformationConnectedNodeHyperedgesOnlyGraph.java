/**
 * Copyright 2015
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cau.cs.se.evaluation.architecture.transformation.processing;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationConnectedNodeHyperedgesOnlyGraph implements ITransformation {
  private final Hypergraph hypergraph;
  
  private Hypergraph resultHypergraph = null;
  
  private Node startNode;
  
  public TransformationConnectedNodeHyperedgesOnlyGraph(final Hypergraph hypergraph) {
    this.hypergraph = hypergraph;
  }
  
  public void setNode(final Node startNode) {
    this.startNode = startNode;
  }
  
  public Hypergraph getResult() {
    return this.resultHypergraph;
  }
  
  /**
   * Find all nodes connected to the start node and create a graph for it.
   */
  public void transform() {
    Node _xifexpression = null;
    EList<Node> _nodes = this.hypergraph.getNodes();
    boolean _contains = _nodes.contains(this.startNode);
    if (_contains) {
      _xifexpression = this.startNode;
    } else {
      _xifexpression = null;
    }
    final Node selectedNode = _xifexpression;
    boolean _notEquals = (!Objects.equal(selectedNode, null));
    if (_notEquals) {
      Hypergraph _createHypergraph = HypergraphFactory.eINSTANCE.createHypergraph();
      this.resultHypergraph = _createHypergraph;
      EList<Node> _nodes_1 = this.resultHypergraph.getNodes();
      Node _deriveNode = TransformationHelper.deriveNode(selectedNode);
      _nodes_1.add(_deriveNode);
      EList<Edge> _edges = selectedNode.getEdges();
      final Consumer<Edge> _function = new Consumer<Edge>() {
        public void accept(final Edge edge) {
          EList<Edge> _edges = TransformationConnectedNodeHyperedgesOnlyGraph.this.resultHypergraph.getEdges();
          Edge _deriveEdge = TransformationHelper.deriveEdge(edge);
          _edges.add(_deriveEdge);
        }
      };
      _edges.forEach(_function);
      EList<Edge> _edges_1 = this.resultHypergraph.getEdges();
      final Consumer<Edge> _function_1 = new Consumer<Edge>() {
        public void accept(final Edge edge) {
          EList<Node> _nodes = TransformationConnectedNodeHyperedgesOnlyGraph.this.hypergraph.getNodes();
          EList<Node> _nodes_1 = TransformationConnectedNodeHyperedgesOnlyGraph.this.resultHypergraph.getNodes();
          TransformationConnectedNodeHyperedgesOnlyGraph.this.createAndLinkNodesConnectedToEdge(edge, _nodes, _nodes_1);
        }
      };
      _edges_1.forEach(_function_1);
    }
  }
  
  public void createAndLinkNodesConnectedToEdge(final Edge edge, final EList<Node> originalNodes, final EList<Node> nodes) {
    EdgeReference _derivedFrom = edge.getDerivedFrom();
    final Edge originalEdge = ((EdgeTrace) _derivedFrom).getEdge();
    for (final Node originalNode : originalNodes) {
      EList<Edge> _edges = originalNode.getEdges();
      boolean _contains = _edges.contains(originalEdge);
      if (_contains) {
        final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
          public Boolean apply(final Node node) {
            NodeReference _derivedFrom = node.getDerivedFrom();
            Node _node = ((NodeTrace) _derivedFrom).getNode();
            return Boolean.valueOf(Objects.equal(_node, originalNode));
          }
        };
        Node newNode = IterableExtensions.<Node>findFirst(nodes, _function);
        boolean _equals = Objects.equal(newNode, null);
        if (_equals) {
          Node _deriveNode = TransformationHelper.deriveNode(originalNode);
          newNode = _deriveNode;
          nodes.add(newNode);
        }
        EList<Edge> _edges_1 = newNode.getEdges();
        _edges_1.add(edge);
      }
    }
  }
}
