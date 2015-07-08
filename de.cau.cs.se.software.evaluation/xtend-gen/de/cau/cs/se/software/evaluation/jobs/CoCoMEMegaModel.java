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
package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * This class is only a temporary place for this model. In real
 * we might need a language or at least an EMF editor for that and store
 * such models in real files.
 */
@SuppressWarnings("all")
public class CoCoMEMegaModel {
  /**
   * Create Megamodel graph
   */
  public Hypergraph createMegaModelAnalysis() {
    final Hypergraph graph = HypergraphFactory.eINSTANCE.createHypergraph();
    for (int i = 1; (i < 22); i++) {
      {
        final Node node = HypergraphFactory.eINSTANCE.createNode();
        node.setName(("Node " + Integer.valueOf(i)));
        EList<Node> _nodes = graph.getNodes();
        _nodes.add(node);
      }
    }
    for (int i = 1; (i < 26); i++) {
      {
        final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
        edge.setName(("Edge " + Integer.valueOf(i)));
        EList<Edge> _edges = graph.getEdges();
        _edges.add(edge);
      }
    }
    EList<Node> _nodes = graph.getNodes();
    EList<Edge> _edges = graph.getEdges();
    this.connectNode(_nodes, _edges, "Node 1", "Edge 3");
    EList<Node> _nodes_1 = graph.getNodes();
    EList<Edge> _edges_1 = graph.getEdges();
    this.connectNode(_nodes_1, _edges_1, "Node 1", "Edge 9");
    EList<Node> _nodes_2 = graph.getNodes();
    EList<Edge> _edges_2 = graph.getEdges();
    this.connectNode(_nodes_2, _edges_2, "Node 2", "Edge 3");
    EList<Node> _nodes_3 = graph.getNodes();
    EList<Edge> _edges_3 = graph.getEdges();
    this.connectNode(_nodes_3, _edges_3, "Node 2", "Edge 4");
    EList<Node> _nodes_4 = graph.getNodes();
    EList<Edge> _edges_4 = graph.getEdges();
    this.connectNode(_nodes_4, _edges_4, "Node 2", "Edge 5");
    EList<Node> _nodes_5 = graph.getNodes();
    EList<Edge> _edges_5 = graph.getEdges();
    this.connectNode(_nodes_5, _edges_5, "Node 2", "Edge 6");
    EList<Node> _nodes_6 = graph.getNodes();
    EList<Edge> _edges_6 = graph.getEdges();
    this.connectNode(_nodes_6, _edges_6, "Node 2", "Edge 2");
    EList<Node> _nodes_7 = graph.getNodes();
    EList<Edge> _edges_7 = graph.getEdges();
    this.connectNode(_nodes_7, _edges_7, "Node 3", "Edge 2");
    EList<Node> _nodes_8 = graph.getNodes();
    EList<Edge> _edges_8 = graph.getEdges();
    this.connectNode(_nodes_8, _edges_8, "Node 3", "Edge 1");
    EList<Node> _nodes_9 = graph.getNodes();
    EList<Edge> _edges_9 = graph.getEdges();
    this.connectNode(_nodes_9, _edges_9, "Node 4", "Edge 9");
    EList<Node> _nodes_10 = graph.getNodes();
    EList<Edge> _edges_10 = graph.getEdges();
    this.connectNode(_nodes_10, _edges_10, "Node 4", "Edge 4");
    EList<Node> _nodes_11 = graph.getNodes();
    EList<Edge> _edges_11 = graph.getEdges();
    this.connectNode(_nodes_11, _edges_11, "Node 4", "Edge 25");
    EList<Node> _nodes_12 = graph.getNodes();
    EList<Edge> _edges_12 = graph.getEdges();
    this.connectNode(_nodes_12, _edges_12, "Node 4", "Edge 7");
    EList<Node> _nodes_13 = graph.getNodes();
    EList<Edge> _edges_13 = graph.getEdges();
    this.connectNode(_nodes_13, _edges_13, "Node 5", "Edge 7");
    EList<Node> _nodes_14 = graph.getNodes();
    EList<Edge> _edges_14 = graph.getEdges();
    this.connectNode(_nodes_14, _edges_14, "Node 5", "Edge 1");
    EList<Node> _nodes_15 = graph.getNodes();
    EList<Edge> _edges_15 = graph.getEdges();
    this.connectNode(_nodes_15, _edges_15, "Node 6", "Edge 5");
    EList<Node> _nodes_16 = graph.getNodes();
    EList<Edge> _edges_16 = graph.getEdges();
    this.connectNode(_nodes_16, _edges_16, "Node 6", "Edge 22");
    EList<Node> _nodes_17 = graph.getNodes();
    EList<Edge> _edges_17 = graph.getEdges();
    this.connectNode(_nodes_17, _edges_17, "Node 6", "Edge 24");
    EList<Node> _nodes_18 = graph.getNodes();
    EList<Edge> _edges_18 = graph.getEdges();
    this.connectNode(_nodes_18, _edges_18, "Node 7", "Edge 6");
    EList<Node> _nodes_19 = graph.getNodes();
    EList<Edge> _edges_19 = graph.getEdges();
    this.connectNode(_nodes_19, _edges_19, "Node 7", "Edge 24");
    EList<Node> _nodes_20 = graph.getNodes();
    EList<Edge> _edges_20 = graph.getEdges();
    this.connectNode(_nodes_20, _edges_20, "Node 7", "Edge 13");
    EList<Node> _nodes_21 = graph.getNodes();
    EList<Edge> _edges_21 = graph.getEdges();
    this.connectNode(_nodes_21, _edges_21, "Node 7", "Edge 23");
    EList<Node> _nodes_22 = graph.getNodes();
    EList<Edge> _edges_22 = graph.getEdges();
    this.connectNode(_nodes_22, _edges_22, "Node 8", "Edge 22");
    EList<Node> _nodes_23 = graph.getNodes();
    EList<Edge> _edges_23 = graph.getEdges();
    this.connectNode(_nodes_23, _edges_23, "Node 8", "Edge 1");
    EList<Node> _nodes_24 = graph.getNodes();
    EList<Edge> _edges_24 = graph.getEdges();
    this.connectNode(_nodes_24, _edges_24, "Node 9", "Edge 13");
    EList<Node> _nodes_25 = graph.getNodes();
    EList<Edge> _edges_25 = graph.getEdges();
    this.connectNode(_nodes_25, _edges_25, "Node 9", "Edge 12");
    EList<Node> _nodes_26 = graph.getNodes();
    EList<Edge> _edges_26 = graph.getEdges();
    this.connectNode(_nodes_26, _edges_26, "Node 10", "Edge 9");
    EList<Node> _nodes_27 = graph.getNodes();
    EList<Edge> _edges_27 = graph.getEdges();
    this.connectNode(_nodes_27, _edges_27, "Node 10", "Edge 10");
    EList<Node> _nodes_28 = graph.getNodes();
    EList<Edge> _edges_28 = graph.getEdges();
    this.connectNode(_nodes_28, _edges_28, "Node 11", "Edge 10");
    EList<Node> _nodes_29 = graph.getNodes();
    EList<Edge> _edges_29 = graph.getEdges();
    this.connectNode(_nodes_29, _edges_29, "Node 11", "Edge 25");
    EList<Node> _nodes_30 = graph.getNodes();
    EList<Edge> _edges_30 = graph.getEdges();
    this.connectNode(_nodes_30, _edges_30, "Node 11", "Edge 24");
    EList<Node> _nodes_31 = graph.getNodes();
    EList<Edge> _edges_31 = graph.getEdges();
    this.connectNode(_nodes_31, _edges_31, "Node 11", "Edge 23");
    EList<Node> _nodes_32 = graph.getNodes();
    EList<Edge> _edges_32 = graph.getEdges();
    this.connectNode(_nodes_32, _edges_32, "Node 11", "Edge 11");
    EList<Node> _nodes_33 = graph.getNodes();
    EList<Edge> _edges_33 = graph.getEdges();
    this.connectNode(_nodes_33, _edges_33, "Node 11", "Edge 16");
    EList<Node> _nodes_34 = graph.getNodes();
    EList<Edge> _edges_34 = graph.getEdges();
    this.connectNode(_nodes_34, _edges_34, "Node 12", "Edge 10");
    EList<Node> _nodes_35 = graph.getNodes();
    EList<Edge> _edges_35 = graph.getEdges();
    this.connectNode(_nodes_35, _edges_35, "Node 12", "Edge 8");
    EList<Node> _nodes_36 = graph.getNodes();
    EList<Edge> _edges_36 = graph.getEdges();
    this.connectNode(_nodes_36, _edges_36, "Node 13", "Edge 10");
    EList<Node> _nodes_37 = graph.getNodes();
    EList<Edge> _edges_37 = graph.getEdges();
    this.connectNode(_nodes_37, _edges_37, "Node 13", "Edge 21");
    EList<Node> _nodes_38 = graph.getNodes();
    EList<Edge> _edges_38 = graph.getEdges();
    this.connectNode(_nodes_38, _edges_38, "Node 14", "Edge 8");
    EList<Node> _nodes_39 = graph.getNodes();
    EList<Edge> _edges_39 = graph.getEdges();
    this.connectNode(_nodes_39, _edges_39, "Node 14", "Edge 21");
    EList<Node> _nodes_40 = graph.getNodes();
    EList<Edge> _edges_40 = graph.getEdges();
    this.connectNode(_nodes_40, _edges_40, "Node 15", "Edge 21");
    EList<Node> _nodes_41 = graph.getNodes();
    EList<Edge> _edges_41 = graph.getEdges();
    this.connectNode(_nodes_41, _edges_41, "Node 15", "Edge 20");
    EList<Node> _nodes_42 = graph.getNodes();
    EList<Edge> _edges_42 = graph.getEdges();
    this.connectNode(_nodes_42, _edges_42, "Node 15", "Edge 17");
    EList<Node> _nodes_43 = graph.getNodes();
    EList<Edge> _edges_43 = graph.getEdges();
    this.connectNode(_nodes_43, _edges_43, "Node 16", "Edge 20");
    EList<Node> _nodes_44 = graph.getNodes();
    EList<Edge> _edges_44 = graph.getEdges();
    this.connectNode(_nodes_44, _edges_44, "Node 16", "Edge 1");
    EList<Node> _nodes_45 = graph.getNodes();
    EList<Edge> _edges_45 = graph.getEdges();
    this.connectNode(_nodes_45, _edges_45, "Node 16", "Edge 19");
    EList<Node> _nodes_46 = graph.getNodes();
    EList<Edge> _edges_46 = graph.getEdges();
    this.connectNode(_nodes_46, _edges_46, "Node 17", "Edge 19");
    EList<Node> _nodes_47 = graph.getNodes();
    EList<Edge> _edges_47 = graph.getEdges();
    this.connectNode(_nodes_47, _edges_47, "Node 17", "Edge 18");
    EList<Node> _nodes_48 = graph.getNodes();
    EList<Edge> _edges_48 = graph.getEdges();
    this.connectNode(_nodes_48, _edges_48, "Node 18", "Edge 8");
    EList<Node> _nodes_49 = graph.getNodes();
    EList<Edge> _edges_49 = graph.getEdges();
    this.connectNode(_nodes_49, _edges_49, "Node 18", "Edge 16");
    EList<Node> _nodes_50 = graph.getNodes();
    EList<Edge> _edges_50 = graph.getEdges();
    this.connectNode(_nodes_50, _edges_50, "Node 18", "Edge 17");
    EList<Node> _nodes_51 = graph.getNodes();
    EList<Edge> _edges_51 = graph.getEdges();
    this.connectNode(_nodes_51, _edges_51, "Node 18", "Edge 18");
    EList<Node> _nodes_52 = graph.getNodes();
    EList<Edge> _edges_52 = graph.getEdges();
    this.connectNode(_nodes_52, _edges_52, "Node 19", "Edge 18");
    EList<Node> _nodes_53 = graph.getNodes();
    EList<Edge> _edges_53 = graph.getEdges();
    this.connectNode(_nodes_53, _edges_53, "Node 19", "Edge 11");
    EList<Node> _nodes_54 = graph.getNodes();
    EList<Edge> _edges_54 = graph.getEdges();
    this.connectNode(_nodes_54, _edges_54, "Node 20", "Edge 11");
    EList<Node> _nodes_55 = graph.getNodes();
    EList<Edge> _edges_55 = graph.getEdges();
    this.connectNode(_nodes_55, _edges_55, "Node 20", "Edge 12");
    EList<Node> _nodes_56 = graph.getNodes();
    EList<Edge> _edges_56 = graph.getEdges();
    this.connectNode(_nodes_56, _edges_56, "Node 20", "Edge 18");
    EList<Node> _nodes_57 = graph.getNodes();
    EList<Edge> _edges_57 = graph.getEdges();
    this.connectNode(_nodes_57, _edges_57, "Node 21", "Edge 12");
    EList<Node> _nodes_58 = graph.getNodes();
    EList<Edge> _edges_58 = graph.getEdges();
    this.connectNode(_nodes_58, _edges_58, "Node 21", "Edge 14");
    EList<Node> _nodes_59 = graph.getNodes();
    EList<Edge> _edges_59 = graph.getEdges();
    this.connectNode(_nodes_59, _edges_59, "Node 21", "Edge 1");
    return graph;
  }
  
  private boolean connectNode(final EList<Node> nodes, final EList<Edge> edges, final String nodeName, final String edgeName) {
    boolean _xblockexpression = false;
    {
      final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
        public Boolean apply(final Node node) {
          String _name = node.getName();
          return Boolean.valueOf(_name.equals(nodeName));
        }
      };
      final Node node = IterableExtensions.<Node>findFirst(nodes, _function);
      final Function1<Edge, Boolean> _function_1 = new Function1<Edge, Boolean>() {
        public Boolean apply(final Edge edge) {
          String _name = edge.getName();
          return Boolean.valueOf(_name.equals(edgeName));
        }
      };
      final Edge edge = IterableExtensions.<Edge>findFirst(edges, _function_1);
      EList<Edge> _edges = node.getEdges();
      _xblockexpression = _edges.add(edge);
    }
    return _xblockexpression;
  }
}
