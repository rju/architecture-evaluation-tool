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
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.hypergraph.NodeTrace;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.TransformationHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Create a intermodule only hypergraph.
 */
@SuppressWarnings("all")
public class TransformationIntermoduleHyperedgesOnlyGraph extends AbstractTransformation<ModularHypergraph, ModularHypergraph> {
  public TransformationIntermoduleHyperedgesOnlyGraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  public ModularHypergraph transform() {
    EList<Edge> _edges = this.input.getEdges();
    int _size = _edges.size();
    EList<Node> _nodes = this.input.getNodes();
    int _size_1 = _nodes.size();
    EList<Module> _modules = this.input.getModules();
    int _size_2 = _modules.size();
    int _plus = (_size_1 + _size_2);
    int _multiply = (_size * _plus);
    EList<Edge> _edges_1 = this.input.getEdges();
    int _size_3 = _edges_1.size();
    EList<Node> _nodes_1 = this.input.getNodes();
    int _size_4 = _nodes_1.size();
    int _multiply_1 = (_size_3 * _size_4);
    int _plus_1 = (_multiply + _multiply_1);
    EList<Module> _modules_1 = this.input.getModules();
    int _size_5 = _modules_1.size();
    EList<Node> _nodes_2 = this.input.getNodes();
    int _size_6 = _nodes_2.size();
    int _multiply_2 = (_size_5 * _size_6);
    int _plus_2 = (_plus_1 + _multiply_2);
    this.monitor.beginTask("Create intermodule hyperedges only graph", _plus_2);
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.result = _createModularHypergraph;
    final HashMap<Node, Module> moduleNodeMap = new HashMap<Node, Module>();
    EList<Module> _modules_2 = this.input.getModules();
    final Consumer<Module> _function = new Consumer<Module>() {
      public void accept(final Module module) {
        EList<Node> _nodes = module.getNodes();
        final Consumer<Node> _function = new Consumer<Node>() {
          public void accept(final Node it) {
            moduleNodeMap.put(it, module);
          }
        };
        _nodes.forEach(_function);
      }
    };
    _modules_2.forEach(_function);
    EList<Edge> _edges_2 = this.input.getEdges();
    final Function1<Edge, Boolean> _function_1 = new Function1<Edge, Boolean>() {
      public Boolean apply(final Edge edge) {
        Boolean _xblockexpression = null;
        {
          EList<Node> _nodes = TransformationIntermoduleHyperedgesOnlyGraph.this.input.getNodes();
          int _size = _nodes.size();
          EList<Module> _modules = TransformationIntermoduleHyperedgesOnlyGraph.this.input.getModules();
          int _size_1 = _modules.size();
          int _plus = (_size + _size_1);
          TransformationIntermoduleHyperedgesOnlyGraph.this.monitor.worked(_plus);
          EList<Node> _nodes_1 = TransformationIntermoduleHyperedgesOnlyGraph.this.input.getNodes();
          _xblockexpression = TransformationIntermoduleHyperedgesOnlyGraph.this.isIntermoduleEdge(edge, moduleNodeMap, _nodes_1);
        }
        return _xblockexpression;
      }
    };
    final Iterable<Edge> interModuleEdges = IterableExtensions.<Edge>filter(_edges_2, _function_1);
    final Consumer<Edge> _function_2 = new Consumer<Edge>() {
      public void accept(final Edge edge) {
        final Edge derivedEdge = TransformationHelper.deriveEdge(edge);
        EList<Edge> _edges = TransformationIntermoduleHyperedgesOnlyGraph.this.result.getEdges();
        _edges.add(derivedEdge);
        EList<Node> _nodes = TransformationIntermoduleHyperedgesOnlyGraph.this.input.getNodes();
        final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
          public Boolean apply(final Node node) {
            EList<Edge> _edges = node.getEdges();
            return Boolean.valueOf(_edges.contains(edge));
          }
        };
        Iterable<Node> _filter = IterableExtensions.<Node>filter(_nodes, _function);
        final Consumer<Node> _function_1 = new Consumer<Node>() {
          public void accept(final Node node) {
            EList<Node> _nodes = TransformationIntermoduleHyperedgesOnlyGraph.this.result.getNodes();
            final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
              public Boolean apply(final Node derivedNode) {
                NodeReference _derivedFrom = derivedNode.getDerivedFrom();
                Node _node = ((NodeTrace) _derivedFrom).getNode();
                return Boolean.valueOf(Objects.equal(_node, node));
              }
            };
            Node derivedNode = IterableExtensions.<Node>findFirst(_nodes, _function);
            boolean _equals = Objects.equal(derivedNode, null);
            if (_equals) {
              Node _deriveNode = TransformationHelper.deriveNode(node);
              derivedNode = _deriveNode;
              EList<Node> _nodes_1 = TransformationIntermoduleHyperedgesOnlyGraph.this.result.getNodes();
              _nodes_1.add(derivedNode);
            }
            EList<Edge> _edges = derivedNode.getEdges();
            _edges.add(derivedEdge);
          }
        };
        _filter.forEach(_function_1);
        EList<Node> _nodes_1 = TransformationIntermoduleHyperedgesOnlyGraph.this.input.getNodes();
        int _size = _nodes_1.size();
        TransformationIntermoduleHyperedgesOnlyGraph.this.monitor.worked(_size);
      }
    };
    interModuleEdges.forEach(_function_2);
    EList<Node> _nodes_3 = this.input.getNodes();
    int _size_7 = _nodes_3.size();
    EList<Edge> _edges_3 = this.input.getEdges();
    int _size_8 = _edges_3.size();
    int _size_9 = IterableExtensions.size(interModuleEdges);
    int _minus = (_size_8 - _size_9);
    int _multiply_3 = (_size_7 * _minus);
    this.monitor.worked(_multiply_3);
    EList<Module> _modules_3 = this.input.getModules();
    final Consumer<Module> _function_3 = new Consumer<Module>() {
      public void accept(final Module module) {
        final Module derivedModule = TransformationHelper.deriveModule(module);
        EList<Node> _nodes = module.getNodes();
        final Consumer<Node> _function = new Consumer<Node>() {
          public void accept(final Node node) {
            EList<Node> _nodes = TransformationIntermoduleHyperedgesOnlyGraph.this.result.getNodes();
            final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
              public Boolean apply(final Node derivedNode) {
                NodeReference _derivedFrom = derivedNode.getDerivedFrom();
                Node _node = ((NodeTrace) _derivedFrom).getNode();
                return Boolean.valueOf(Objects.equal(_node, node));
              }
            };
            final Node derivedNode = IterableExtensions.<Node>findFirst(_nodes, _function);
            boolean _notEquals = (!Objects.equal(derivedNode, null));
            if (_notEquals) {
              EList<Node> _nodes_1 = derivedModule.getNodes();
              _nodes_1.add(derivedNode);
            }
          }
        };
        _nodes.forEach(_function);
        EList<Node> _nodes_1 = TransformationIntermoduleHyperedgesOnlyGraph.this.input.getNodes();
        int _size = _nodes_1.size();
        TransformationIntermoduleHyperedgesOnlyGraph.this.monitor.worked(_size);
      }
    };
    _modules_3.forEach(_function_3);
    return this.result;
  }
  
  /**
   * Check if the edge is an intermodule edge.
   */
  private Boolean isIntermoduleEdge(final Edge edge, final Map<Node, Module> moduleNodeMap, final EList<Node> nodes) {
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node node) {
        EList<Edge> _edges = node.getEdges();
        return Boolean.valueOf(_edges.contains(edge));
      }
    };
    final Iterable<Node> connectedNodes = IterableExtensions.<Node>filter(nodes, _function);
    Module lastMatch = null;
    for (final Node node : connectedNodes) {
      {
        final Module match = moduleNodeMap.get(node);
        boolean _notEquals = (!Objects.equal(lastMatch, null));
        if (_notEquals) {
          boolean _notEquals_1 = (!Objects.equal(lastMatch, match));
          if (_notEquals_1) {
            return Boolean.valueOf(true);
          }
        }
        lastMatch = match;
      }
    }
    return Boolean.valueOf(false);
  }
}
