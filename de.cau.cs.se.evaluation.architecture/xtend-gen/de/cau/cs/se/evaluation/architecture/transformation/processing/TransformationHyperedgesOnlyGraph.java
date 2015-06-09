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
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationHyperedgesOnlyGraph implements ITransformation {
  private final Hypergraph hypergraph;
  
  private Hypergraph resultHypergraph;
  
  public TransformationHyperedgesOnlyGraph(final Hypergraph hypergraph) {
    this.hypergraph = hypergraph;
  }
  
  public Hypergraph getResult() {
    return this.resultHypergraph;
  }
  
  /**
   * Copy only connected nodes to the result graph.
   */
  public void transform() {
    Hypergraph _createHypergraph = HypergraphFactory.eINSTANCE.createHypergraph();
    this.resultHypergraph = _createHypergraph;
    EList<Edge> _edges = this.hypergraph.getEdges();
    for (final Edge edge : _edges) {
      EList<Edge> _edges_1 = this.resultHypergraph.getEdges();
      Edge _deriveEdge = TransformationHelper.deriveEdge(edge);
      _edges_1.add(_deriveEdge);
    }
    EList<Node> _nodes = this.hypergraph.getNodes();
    for (final Node node : _nodes) {
      EList<Edge> _edges_2 = node.getEdges();
      int _size = _edges_2.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        final Node resultNode = TransformationHelper.deriveNode(node);
        EList<Edge> _edges_3 = node.getEdges();
        final Consumer<Edge> _function = new Consumer<Edge>() {
          public void accept(final Edge edge) {
            EList<Edge> _edges = resultNode.getEdges();
            EList<Edge> _edges_1 = TransformationHyperedgesOnlyGraph.this.resultHypergraph.getEdges();
            final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
              public Boolean apply(final Edge it) {
                EdgeReference _derivedFrom = it.getDerivedFrom();
                Edge _edge = ((EdgeTrace) _derivedFrom).getEdge();
                return Boolean.valueOf(Objects.equal(_edge, edge));
              }
            };
            Edge _findFirst = IterableExtensions.<Edge>findFirst(_edges_1, _function);
            _edges.add(_findFirst);
          }
        };
        _edges_3.forEach(_function);
        EList<Node> _nodes_1 = this.resultHypergraph.getNodes();
        _nodes_1.add(resultNode);
      }
    }
  }
}
