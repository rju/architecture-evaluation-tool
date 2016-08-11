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

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

/**
 * Copy only connected nodes to the result graph.
 */
@SuppressWarnings("all")
public class TransformationHyperedgesOnlyGraph extends AbstractTransformation<Hypergraph, Hypergraph> {
  public TransformationHyperedgesOnlyGraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public Hypergraph generate(final Hypergraph input) {
    final HashMap<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();
    Hypergraph _createHypergraph = HypergraphFactory.eINSTANCE.createHypergraph();
    this.result = _createHypergraph;
    EList<Edge> _edges = input.getEdges();
    for (final Edge edge : _edges) {
      {
        final Edge derivedEdge = HypergraphCreationHelper.deriveEdge(edge);
        edgeMap.put(edge, derivedEdge);
        EList<Edge> _edges_1 = this.result.getEdges();
        _edges_1.add(derivedEdge);
      }
    }
    EList<Node> _nodes = input.getNodes();
    for (final Node node : _nodes) {
      boolean _isCanceled = this.monitor.isCanceled();
      boolean _not = (!_isCanceled);
      if (_not) {
        EList<Edge> _edges_1 = node.getEdges();
        int _size = _edges_1.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          final Node resultNode = HypergraphCreationHelper.deriveNode(node);
          EList<Edge> _edges_2 = node.getEdges();
          final Consumer<Edge> _function = (Edge edge_1) -> {
            EList<Edge> _edges_3 = resultNode.getEdges();
            Edge _get = edgeMap.get(edge_1);
            _edges_3.add(_get);
          };
          _edges_2.forEach(_function);
          EList<Node> _nodes_1 = this.result.getNodes();
          _nodes_1.add(resultNode);
        }
      }
    }
    return this.result;
  }
  
  @Override
  public int workEstimate(final Hypergraph input) {
    EList<Edge> _edges = input.getEdges();
    int _size = _edges.size();
    EList<Node> _nodes = input.getNodes();
    final Function1<Node, Integer> _function = (Node it) -> {
      EList<Edge> _edges_1 = it.getEdges();
      return Integer.valueOf(_edges_1.size());
    };
    List<Integer> _map = ListExtensions.<Node, Integer>map(_nodes, _function);
    final Function2<Integer, Integer, Integer> _function_1 = (Integer p1, Integer p2) -> {
      return Integer.valueOf(((p1).intValue() + (p2).intValue()));
    };
    Integer _reduce = IterableExtensions.<Integer>reduce(_map, _function_1);
    return (_size + (_reduce).intValue());
  }
}
