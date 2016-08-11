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
import de.cau.cs.se.software.evaluation.state.RowPattern;
import de.cau.cs.se.software.evaluation.state.RowPatternTable;
import de.cau.cs.se.software.evaluation.state.StateFactory;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import de.cau.cs.se.software.evaluation.views.LogModelProvider;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.Functions.Function2;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

/**
 * Calculate the information size of a hypergraph.
 */
@SuppressWarnings("all")
public class TransformationHypergraphSize extends AbstractTransformation<Hypergraph, Double> {
  public TransformationHypergraphSize(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public Double generate(final Hypergraph input) {
    boolean _isCanceled = this.monitor.isCanceled();
    if (_isCanceled) {
      return Double.valueOf(0.0);
    }
    final Hypergraph systemGraph = this.createSystemGraph(input);
    final RowPatternTable table = this.createRowPatternTable(systemGraph, input);
    double _calculateSize = this.calculateSize(systemGraph, table);
    this.result = Double.valueOf(_calculateSize);
    return this.result;
  }
  
  /**
   * Calculate the size of given system.
   */
  private double calculateSize(final Hypergraph system, final RowPatternTable table) {
    double size = 0;
    for (int i = 0; (i < system.getNodes().size()); i++) {
      {
        boolean _isCanceled = this.monitor.isCanceled();
        if (_isCanceled) {
          return 0.0;
        }
        EList<Node> _nodes = system.getNodes();
        int _size = _nodes.size();
        this.monitor.worked(_size);
        EList<RowPattern> _patterns = table.getPatterns();
        EList<Node> _nodes_1 = system.getNodes();
        Node _get = _nodes_1.get(i);
        final double probability = this.lookupProbability(_patterns, _get, system);
        if ((probability > 0.0d)) {
          double _size_1 = size;
          double _log2 = this.log2(probability);
          double _minus = (-_log2);
          size = (_size_1 + _minus);
        } else {
          LogModelProvider.INSTANCE.addMessage("Hypergraph Model Error", "A component is disconnected, but should be connected. Result is tainted.");
        }
      }
    }
    return size;
  }
  
  /**
   * Logarithm for base 2.
   */
  private double log2(final double value) {
    double _log = Math.log(value);
    double _log_1 = Math.log(2);
    return (_log / _log_1);
  }
  
  /**
   * Find the row pattern for a given node and determine its probability. If no pattern
   * is found then the node is totally disconnected and the probability is 0.
   */
  private double lookupProbability(final EList<RowPattern> patternList, final Node node, final Hypergraph system) {
    final Function1<RowPattern, Boolean> _function = (RowPattern p) -> {
      EList<Node> _nodes = p.getNodes();
      return Boolean.valueOf(_nodes.contains(node));
    };
    final Iterable<RowPattern> pattern = IterableExtensions.<RowPattern>filter(patternList, _function);
    double _xifexpression = (double) 0;
    int _size = IterableExtensions.size(pattern);
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      RowPattern _get = ((RowPattern[])Conversions.unwrapArray(pattern, RowPattern.class))[0];
      EList<Node> _nodes = _get.getNodes();
      int _size_1 = _nodes.size();
      _xifexpression = ((double) _size_1);
    } else {
      _xifexpression = 0;
    }
    final double count = _xifexpression;
    EList<Node> _nodes_1 = system.getNodes();
    int _size_2 = _nodes_1.size();
    int _plus = (_size_2 + 1);
    return (count / ((double) _plus));
  }
  
  /**
   * Create a row pattern table for a system and a system graph.
   * First, register the edges in the pattern table as column definitions.
   * Second, calculate the pattern row for each node of the system graph.
   * Compact, rows with the same pattern
   */
  private RowPatternTable createRowPatternTable(final Hypergraph systemGraph, final Hypergraph input) {
    final RowPatternTable patternTable = StateFactory.eINSTANCE.createRowPatternTable();
    EList<Edge> _edges = systemGraph.getEdges();
    final Consumer<Edge> _function = (Edge edge) -> {
      EList<Edge> _edges_1 = patternTable.getEdges();
      _edges_1.add(edge);
    };
    _edges.forEach(_function);
    EList<Edge> _edges_1 = input.getEdges();
    int _size = _edges_1.size();
    this.monitor.worked(_size);
    boolean _isCanceled = this.monitor.isCanceled();
    if (_isCanceled) {
      return null;
    }
    EList<Node> _nodes = systemGraph.getNodes();
    final Consumer<Node> _function_1 = (Node node) -> {
      EList<RowPattern> _patterns = patternTable.getPatterns();
      EList<Edge> _edges_2 = patternTable.getEdges();
      RowPattern _calculateRowPattern = this.calculateRowPattern(node, _edges_2);
      _patterns.add(_calculateRowPattern);
    };
    _nodes.forEach(_function_1);
    EList<Node> _nodes_1 = input.getNodes();
    int _size_1 = _nodes_1.size();
    EList<Node> _nodes_2 = input.getNodes();
    int _size_2 = _nodes_2.size();
    int _multiply = (_size_1 * _size_2);
    this.monitor.worked(_multiply);
    this.compactPatternTable(patternTable);
    EList<Node> _nodes_3 = input.getNodes();
    int _size_3 = _nodes_3.size();
    this.monitor.worked(_size_3);
    return patternTable;
  }
  
  /**
   * Calculate the row pattern of a node based on its edges.
   * 
   * @param node where the pattern is calculated for
   * @param edgeList sequence of edges which define the table wide order of edges
   * 
   * @returns the complete pattern
   */
  private RowPattern calculateRowPattern(final Node node, final EList<Edge> edgeList) {
    final RowPattern pattern = StateFactory.eINSTANCE.createRowPattern();
    EList<Node> _nodes = pattern.getNodes();
    _nodes.add(node);
    final Consumer<Edge> _function = (Edge edge) -> {
      EList<Boolean> _pattern = pattern.getPattern();
      EList<Edge> _edges = node.getEdges();
      final Function1<Edge, Boolean> _function_1 = (Edge it) -> {
        return Boolean.valueOf(Objects.equal(it, edge));
      };
      boolean _exists = IterableExtensions.<Edge>exists(_edges, _function_1);
      _pattern.add(Boolean.valueOf(_exists));
    };
    edgeList.forEach(_function);
    return pattern;
  }
  
  /**
   * Find duplicate pattern in the pattern table and merge the pattern rows.
   */
  private void compactPatternTable(final RowPatternTable table) {
    EList<RowPattern> _patterns = table.getPatterns();
    int _size = _patterns.size();
    EList<RowPattern> _patterns_1 = table.getPatterns();
    RowPattern _get = _patterns_1.get(0);
    EList<Boolean> _pattern = _get.getPattern();
    int _size_1 = _pattern.size();
    final int tick = (_size * _size_1);
    EList<RowPattern> _patterns_2 = table.getPatterns();
    final int length = _patterns_2.size();
    for (int i = 0; (i < table.getPatterns().size()); i++) {
      {
        this.monitor.worked(tick);
        boolean _isCanceled = this.monitor.isCanceled();
        boolean _not = (!_isCanceled);
        if (_not) {
          for (int j = (i + 1); (j < table.getPatterns().size()); j++) {
            EList<RowPattern> _patterns_3 = table.getPatterns();
            RowPattern _get_1 = _patterns_3.get(j);
            EList<Boolean> _pattern_1 = _get_1.getPattern();
            EList<RowPattern> _patterns_4 = table.getPatterns();
            RowPattern _get_2 = _patterns_4.get(i);
            EList<Boolean> _pattern_2 = _get_2.getPattern();
            boolean _matchPattern = this.matchPattern(_pattern_1, _pattern_2);
            if (_matchPattern) {
              EList<RowPattern> _patterns_5 = table.getPatterns();
              final RowPattern basePattern = _patterns_5.get(i);
              EList<RowPattern> _patterns_6 = table.getPatterns();
              RowPattern _get_3 = _patterns_6.get(j);
              EList<Node> _nodes = _get_3.getNodes();
              final Consumer<Node> _function = (Node node) -> {
                EList<Node> _nodes_1 = basePattern.getNodes();
                _nodes_1.add(node);
              };
              _nodes.forEach(_function);
              EList<RowPattern> _patterns_7 = table.getPatterns();
              _patterns_7.remove(j);
              j--;
            }
          }
        }
      }
    }
    EList<RowPattern> _patterns_3 = table.getPatterns();
    int _size_2 = _patterns_3.size();
    int _minus = (length - _size_2);
    int _multiply = (_minus * tick);
    this.monitor.worked(_multiply);
  }
  
  /**
   * Return true if both lists contain the same values in the list.
   */
  private boolean matchPattern(final EList<Boolean> leftList, final EList<Boolean> rightList) {
    int _size = leftList.size();
    int _size_1 = rightList.size();
    boolean _notEquals = (_size != _size_1);
    if (_notEquals) {
      return false;
    }
    for (int i = 0; (i < leftList.size()); i++) {
      Boolean _get = leftList.get(i);
      Boolean _get_1 = rightList.get(i);
      boolean _equals = _get.equals(_get_1);
      boolean _not = (!_equals);
      if (_not) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Create a system graph from a hypergraph of a system by adding an additional not connected
   * node for the environment.
   * 
   * @param hypergraph the graph which is used as input
   * 
   * @returns the system graph (Note: the system graph and the system share nodes)
   */
  private Hypergraph createSystemGraph(final Hypergraph system) {
    final Hypergraph systemGraph = HypergraphFactory.eINSTANCE.createHypergraph();
    final Node environmentNode = HypergraphFactory.eINSTANCE.createNode();
    environmentNode.setName("_environment");
    EList<Node> _nodes = systemGraph.getNodes();
    _nodes.add(environmentNode);
    EList<Edge> _edges = system.getEdges();
    final Consumer<Edge> _function = (Edge edge) -> {
      EList<Edge> _edges_1 = systemGraph.getEdges();
      Edge _deriveEdge = HypergraphCreationHelper.deriveEdge(edge);
      _edges_1.add(_deriveEdge);
    };
    _edges.forEach(_function);
    EList<Edge> _edges_1 = system.getEdges();
    int _size = _edges_1.size();
    this.monitor.worked(_size);
    boolean _isCanceled = this.monitor.isCanceled();
    if (_isCanceled) {
      return null;
    }
    EList<Node> _nodes_1 = system.getNodes();
    final Consumer<Node> _function_1 = (Node node) -> {
      boolean _isCanceled_1 = this.monitor.isCanceled();
      boolean _not = (!_isCanceled_1);
      if (_not) {
        final Node derivedNode = HypergraphCreationHelper.deriveNode(node);
        EList<Edge> _edges_2 = node.getEdges();
        final Consumer<Edge> _function_2 = (Edge edge) -> {
          EList<Edge> _edges_3 = systemGraph.getEdges();
          final Function1<Edge, Boolean> _function_3 = (Edge it) -> {
            EdgeReference _derivedFrom = it.getDerivedFrom();
            Edge _edge = ((EdgeTrace) _derivedFrom).getEdge();
            return Boolean.valueOf(Objects.equal(_edge, edge));
          };
          final Edge derivedEdge = IterableExtensions.<Edge>findFirst(_edges_3, _function_3);
          EList<Edge> _edges_4 = derivedNode.getEdges();
          _edges_4.add(derivedEdge);
        };
        _edges_2.forEach(_function_2);
        EList<Node> _nodes_2 = systemGraph.getNodes();
        _nodes_2.add(derivedNode);
        EList<Edge> _edges_3 = node.getEdges();
        int _size_1 = _edges_3.size();
        this.monitor.worked(_size_1);
      }
    };
    _nodes_1.forEach(_function_1);
    EList<Node> _nodes_2 = system.getNodes();
    int _size_1 = _nodes_2.size();
    this.monitor.worked(_size_1);
    return systemGraph;
  }
  
  @Override
  public int workEstimate(final Hypergraph input) {
    int _xblockexpression = (int) 0;
    {
      EList<Edge> _edges = input.getEdges();
      int _size = _edges.size();
      EList<Node> _nodes = input.getNodes();
      int _size_1 = _nodes.size();
      int _plus = (_size + _size_1);
      EList<Node> _nodes_1 = input.getNodes();
      final Function1<Node, Integer> _function = (Node it) -> {
        EList<Edge> _edges_1 = it.getEdges();
        return Integer.valueOf(_edges_1.size());
      };
      List<Integer> _map = ListExtensions.<Node, Integer>map(_nodes_1, _function);
      final Function2<Integer, Integer, Integer> _function_1 = (Integer p1, Integer p2) -> {
        return Integer.valueOf(((p1).intValue() + (p2).intValue()));
      };
      Integer _reduce = IterableExtensions.<Integer>reduce(_map, _function_1);
      int _plus_1 = (_plus + (_reduce).intValue());
      EList<Edge> _edges_1 = input.getEdges();
      int _size_2 = _edges_1.size();
      int _plus_2 = (_plus_1 + _size_2);
      EList<Node> _nodes_2 = input.getNodes();
      int _size_3 = _nodes_2.size();
      EList<Node> _nodes_3 = input.getNodes();
      int _size_4 = _nodes_3.size();
      int _multiply = (_size_3 * _size_4);
      int _plus_3 = (_plus_2 + _multiply);
      EList<Node> _nodes_4 = input.getNodes();
      int _size_5 = _nodes_4.size();
      EList<Node> _nodes_5 = input.getNodes();
      int _size_6 = _nodes_5.size();
      int _multiply_1 = (_size_5 * _size_6);
      EList<Edge> _edges_2 = input.getEdges();
      int _size_7 = _edges_2.size();
      int _multiply_2 = (_multiply_1 * _size_7);
      /* (_plus_3 + _multiply_2); */
      EList<Node> _nodes_6 = input.getNodes();
      int _size_8 = _nodes_6.size();
      EList<Node> _nodes_7 = input.getNodes();
      int _size_9 = _nodes_7.size();
      _xblockexpression = (_size_8 * _size_9);
    }
    return _xblockexpression;
  }
}
