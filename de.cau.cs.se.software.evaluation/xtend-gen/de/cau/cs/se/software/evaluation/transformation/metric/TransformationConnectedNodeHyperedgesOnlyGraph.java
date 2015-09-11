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
package de.cau.cs.se.software.evaluation.transformation.metric;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.EdgeReference;
import de.cau.cs.se.software.evaluation.hypergraph.EdgeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.hypergraph.NodeTrace;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Create a hypergraph for a given hypergraph which contains only
 * those nodes which are connected by edges with the startNode.
 */
@SuppressWarnings("all")
public class TransformationConnectedNodeHyperedgesOnlyGraph extends AbstractTransformation<Hypergraph, Hypergraph> {
  private Node startNode;
  
  public TransformationConnectedNodeHyperedgesOnlyGraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  public void setNode(final Node startNode) {
    this.startNode = startNode;
  }
  
  /**
   * Find all nodes connected to the start node and create a graph for it.
   */
  @Override
  public Hypergraph transform(final Hypergraph input) {
    Object _xblockexpression = null;
    {
      Node _xifexpression = null;
      EList<Node> _nodes = input.getNodes();
      boolean _contains = _nodes.contains(this.startNode);
      if (_contains) {
        _xifexpression = this.startNode;
      } else {
        _xifexpression = null;
      }
      final Node selectedNode = _xifexpression;
      Object _xifexpression_1 = null;
      boolean _notEquals = (!Objects.equal(selectedNode, null));
      if (_notEquals) {
        Hypergraph _createHypergraph = HypergraphFactory.eINSTANCE.createHypergraph();
        this.result = _createHypergraph;
        EList<Node> _nodes_1 = this.result.getNodes();
        Node _deriveNode = HypergraphCreationHelper.deriveNode(selectedNode);
        _nodes_1.add(_deriveNode);
        EList<Edge> _edges = selectedNode.getEdges();
        final Consumer<Edge> _function = (Edge edge) -> {
          EList<Edge> _edges_1 = this.result.getEdges();
          Edge _deriveEdge = HypergraphCreationHelper.deriveEdge(edge);
          _edges_1.add(_deriveEdge);
        };
        _edges.forEach(_function);
        EList<Edge> _edges_1 = this.result.getEdges();
        final Consumer<Edge> _function_1 = (Edge edge) -> {
          EList<Node> _nodes_2 = input.getNodes();
          EList<Node> _nodes_3 = this.result.getNodes();
          this.createAndLinkNodesConnectedToEdge(edge, _nodes_2, _nodes_3);
        };
        _edges_1.forEach(_function_1);
        return this.result;
      } else {
        _xifexpression_1 = null;
      }
      _xblockexpression = _xifexpression_1;
    }
    return ((Hypergraph)_xblockexpression);
  }
  
  private void createAndLinkNodesConnectedToEdge(final Edge edge, final EList<Node> originalNodes, final EList<Node> nodes) {
    EdgeReference _derivedFrom = edge.getDerivedFrom();
    final Edge originalEdge = ((EdgeTrace) _derivedFrom).getEdge();
    for (final Node originalNode : originalNodes) {
      EList<Edge> _edges = originalNode.getEdges();
      boolean _contains = _edges.contains(originalEdge);
      if (_contains) {
        final Function1<Node, Boolean> _function = (Node node) -> {
          NodeReference _derivedFrom_1 = node.getDerivedFrom();
          Node _node = ((NodeTrace) _derivedFrom_1).getNode();
          return Boolean.valueOf(Objects.equal(_node, originalNode));
        };
        Node newNode = IterableExtensions.<Node>findFirst(nodes, _function);
        boolean _equals = Objects.equal(newNode, null);
        if (_equals) {
          Node _deriveNode = HypergraphCreationHelper.deriveNode(originalNode);
          newNode = _deriveNode;
          nodes.add(newNode);
        }
        EList<Edge> _edges_1 = newNode.getEdges();
        _edges_1.add(edge);
      }
    }
  }
}
