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
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace;
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;
import de.cau.cs.se.evaluation.architecture.transformation.TransformationHelper;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class TransformationMaximalInterconnectedGraph implements ITransformation {
  private final ModularHypergraph hypergraph;
  
  private ModularHypergraph resultHypergraph;
  
  public TransformationMaximalInterconnectedGraph(final ModularHypergraph hypergraph) {
    this.hypergraph = hypergraph;
  }
  
  public ModularHypergraph getResult() {
    return this.resultHypergraph;
  }
  
  public void transform() {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.resultHypergraph = _createModularHypergraph;
    EList<Node> _nodes = this.hypergraph.getNodes();
    final Consumer<Node> _function = new Consumer<Node>() {
      public void accept(final Node it) {
        EList<Node> _nodes = TransformationMaximalInterconnectedGraph.this.resultHypergraph.getNodes();
        Node _deriveNode = TransformationHelper.deriveNode(it);
        _nodes.add(_deriveNode);
      }
    };
    _nodes.forEach(_function);
    EList<Module> _modules = this.hypergraph.getModules();
    final Consumer<Module> _function_1 = new Consumer<Module>() {
      public void accept(final Module module) {
        final Module derivedModule = TransformationHelper.deriveModule(module);
        EList<Node> _nodes = module.getNodes();
        final Consumer<Node> _function = new Consumer<Node>() {
          public void accept(final Node node) {
            EList<Node> _nodes = derivedModule.getNodes();
            EList<Node> _nodes_1 = TransformationMaximalInterconnectedGraph.this.resultHypergraph.getNodes();
            final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
              public Boolean apply(final Node derivedNode) {
                NodeReference _derivedFrom = derivedNode.getDerivedFrom();
                Node _node = ((NodeTrace) _derivedFrom).getNode();
                return Boolean.valueOf(Objects.equal(_node, node));
              }
            };
            Node _findFirst = IterableExtensions.<Node>findFirst(_nodes_1, _function);
            _nodes.add(_findFirst);
          }
        };
        _nodes.forEach(_function);
        EList<Module> _modules = TransformationMaximalInterconnectedGraph.this.resultHypergraph.getModules();
        _modules.add(derivedModule);
      }
    };
    _modules.forEach(_function_1);
    EList<Node> _nodes_1 = this.resultHypergraph.getNodes();
    final Procedure2<Node, Integer> _function_2 = new Procedure2<Node, Integer>() {
      public void apply(final Node startNode, final Integer startIndex) {
        for (int index = ((startIndex).intValue() + 1); (index < TransformationMaximalInterconnectedGraph.this.resultHypergraph.getNodes().size()); index++) {
          {
            EList<Node> _nodes = TransformationMaximalInterconnectedGraph.this.resultHypergraph.getNodes();
            final Node endNode = _nodes.get(index);
            final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
            String _name = startNode.getName();
            String _plus = (_name + "-");
            String _name_1 = endNode.getName();
            String _plus_1 = (_plus + _name_1);
            edge.setName(_plus_1);
            EList<Edge> _edges = startNode.getEdges();
            _edges.add(edge);
            EList<Edge> _edges_1 = endNode.getEdges();
            _edges_1.add(edge);
            EList<Edge> _edges_2 = TransformationMaximalInterconnectedGraph.this.resultHypergraph.getEdges();
            _edges_2.add(edge);
          }
        }
      }
    };
    IterableExtensions.<Node>forEach(_nodes_1, _function_2);
  }
}
