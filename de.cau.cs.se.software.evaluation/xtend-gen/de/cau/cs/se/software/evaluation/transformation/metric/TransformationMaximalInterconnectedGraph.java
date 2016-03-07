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
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class TransformationMaximalInterconnectedGraph extends AbstractTransformation<ModularHypergraph, ModularHypergraph> {
  public TransformationMaximalInterconnectedGraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public ModularHypergraph generate(final ModularHypergraph input) {
    EList<Node> _nodes = input.getNodes();
    int _size = _nodes.size();
    EList<Module> _modules = input.getModules();
    int _size_1 = _modules.size();
    EList<Node> _nodes_1 = input.getNodes();
    int _size_2 = _nodes_1.size();
    int _multiply = (_size_1 * _size_2);
    int _plus = (_size + _multiply);
    EList<Node> _nodes_2 = input.getNodes();
    int _size_3 = _nodes_2.size();
    EList<Node> _nodes_3 = input.getNodes();
    int _size_4 = _nodes_3.size();
    int _multiply_1 = (_size_3 * _size_4);
    int _plus_1 = (_plus + _multiply_1);
    this.monitor.beginTask("Create maximal interconnected graph", _plus_1);
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.result = _createModularHypergraph;
    EList<Node> _nodes_4 = input.getNodes();
    final Consumer<Node> _function = (Node it) -> {
      EList<Node> _nodes_5 = this.result.getNodes();
      Node _deriveNode = HypergraphCreationHelper.deriveNode(it);
      _nodes_5.add(_deriveNode);
    };
    _nodes_4.forEach(_function);
    EList<Node> _nodes_5 = input.getNodes();
    int _size_5 = _nodes_5.size();
    this.monitor.worked(_size_5);
    EList<Module> _modules_1 = input.getModules();
    final Consumer<Module> _function_1 = (Module module) -> {
      final Module derivedModule = HypergraphCreationHelper.deriveModule(module);
      EList<Node> _nodes_6 = module.getNodes();
      final Consumer<Node> _function_2 = (Node node) -> {
        EList<Node> _nodes_7 = derivedModule.getNodes();
        EList<Node> _nodes_8 = this.result.getNodes();
        final Function1<Node, Boolean> _function_3 = (Node derivedNode) -> {
          NodeReference _derivedFrom = derivedNode.getDerivedFrom();
          Node _node = ((NodeTrace) _derivedFrom).getNode();
          return Boolean.valueOf(Objects.equal(_node, node));
        };
        Node _findFirst = IterableExtensions.<Node>findFirst(_nodes_8, _function_3);
        _nodes_7.add(_findFirst);
      };
      _nodes_6.forEach(_function_2);
      EList<Module> _modules_2 = this.result.getModules();
      _modules_2.add(derivedModule);
      EList<Node> _nodes_7 = input.getNodes();
      int _size_6 = _nodes_7.size();
      this.monitor.worked(_size_6);
    };
    _modules_1.forEach(_function_1);
    EList<Node> _nodes_6 = this.result.getNodes();
    final Procedure2<Node, Integer> _function_2 = (Node startNode, Integer startIndex) -> {
      for (int index = ((startIndex).intValue() + 1); (index < this.result.getNodes().size()); index++) {
        {
          EList<Node> _nodes_7 = this.result.getNodes();
          final Node endNode = _nodes_7.get(index);
          final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
          String _name = startNode.getName();
          String _plus_2 = (_name + "-");
          String _name_1 = endNode.getName();
          String _plus_3 = (_plus_2 + _name_1);
          edge.setName(_plus_3);
          EList<Edge> _edges = startNode.getEdges();
          _edges.add(edge);
          EList<Edge> _edges_1 = endNode.getEdges();
          _edges_1.add(edge);
          EList<Edge> _edges_2 = this.result.getEdges();
          _edges_2.add(edge);
        }
      }
      EList<Node> _nodes_7 = input.getNodes();
      int _size_6 = _nodes_7.size();
      this.monitor.worked(_size_6);
    };
    IterableExtensions.<Node>forEach(_nodes_6, _function_2);
    return this.result;
  }
}
