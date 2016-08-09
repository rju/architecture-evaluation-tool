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

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * This class is only a temporary place for this model. In real
 * we might need a language or at least an EMF editor for that and store
 * such models in real files.
 */
@SuppressWarnings("all")
public class CoCoMEMegaModel {
  private final List<String> trmInitialNodes = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("TRM_PCM", "TRM_DTL"));
  
  private final List<String> modelInitialNodes = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("PCM", "Behavior", "DTL", "EJB/Servlet Stubs", "EJB/Servlets", "Snippets", "Entities", "Bean Classes", "Entity Classes"));
  
  private final List<String> transInitialNodes = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("T_ProtoCom", "T_behavior", "T_DTL", "T_JW", "T_javac,ajc", "T_javac3"));
  
  private final List<String> trmCompleteNodes = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("TRM_IRL", "TRM_PCM", "TRM_DTL"));
  
  private final List<String> modelCompleteNodes = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("IRL", "IAL", "PCM", "Behavior", "DTL", "Kieker Records", "Sensors", "aspect.xml", "EJB/Servlet Stubs", "EJB/Servlets", "Snippets", "Entities", "Record Classes", "Sensor Classes", "web.xml", "Bean Classes", "Entity Classes"));
  
  private final List<String> transCompleteNodes = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("T_IRL", "T_sensor", "T_web", "T_aspect", "T_ProtoCom", "T_behavior", "T_DTL", "T_JW", "T_javac1", "T_javac2", "T_javac,ajc", "T_javac3"));
  
  /**
   * Create Megamodel graph
   */
  public Hypergraph createMegaModelAnalysis(final boolean complete) {
    final Hypergraph graph = HypergraphFactory.eINSTANCE.createHypergraph();
    if (complete) {
      this.makeNodes(graph, this.modelCompleteNodes);
      this.makeNodes(graph, this.transCompleteNodes);
      this.makeNodes(graph, this.trmCompleteNodes);
    } else {
      this.makeNodes(graph, this.modelInitialNodes);
      this.makeNodes(graph, this.transInitialNodes);
      this.makeNodes(graph, this.trmInitialNodes);
    }
    this.connectNode(graph, "IRL", "T_IRL");
    this.connectNode(graph, "T_IRL", "Kieker Records");
    this.connectNode(graph, "T_IRL", "TRM_IRL");
    this.connectNode(graph, "Kieker Records", "T_javac1");
    this.connectNode(graph, "TRM_IRL", "T_sensor");
    this.connectNode(graph, "IAL", "IRL");
    this.connectNode(graph, "IAL", "T_sensor");
    this.connectNode(graph, "IAL", "T_web");
    this.connectNode(graph, "IAL", "T_aspect");
    this.connectNode(graph, "IAL", "PCM");
    this.connectNode(graph, "T_sensor", "Sensors");
    this.connectNode(graph, "T_web", "web.xml");
    this.connectNode(graph, "web.xml", "Bean Classes");
    this.connectNode(graph, "Sensor Classes", "Record Classes");
    this.connectNode(graph, "T_aspect", "aspect.xml");
    this.connectNode(graph, "Sensors", "T_javac2");
    this.connectNode(graph, "T_javac2", "Sensor Classes");
    this.connectNode(graph, "Sensors", "T_javac,ajc");
    this.connectNode(graph, "aspect.xml", "T_javac,ajc");
    this.connectNode(graph, "PCM", "T_ProtoCom");
    this.connectNode(graph, "PCM", "DTL");
    this.connectNode(graph, "T_ProtoCom", "TRM_PCM");
    this.connectNode(graph, "T_ProtoCom", "EJB/Servlet Stubs");
    this.connectNode(graph, "TRM_PCM", "T_sensor");
    this.connectNode(graph, "TRM_PCM", "T_web");
    this.connectNode(graph, "TRM_PCM", "T_aspect");
    this.connectNode(graph, "TRM_PCM", "T_behavior");
    this.connectNode(graph, "EJB/Servlet Stubs", "T_JW");
    this.connectNode(graph, "T_JW", "EJB/Servlets");
    this.connectNode(graph, "EJB/Servlets", "T_javac,ajc");
    this.connectNode(graph, "T_javac,ajc", "Bean Classes");
    this.connectNode(graph, "Bean Classes", "Entity Classes");
    this.connectNode(graph, "Behavior", "T_behavior");
    this.connectNode(graph, "Behavior", "PCM");
    this.connectNode(graph, "Behavior", "DTL");
    this.connectNode(graph, "T_behavior", "Snippets");
    this.connectNode(graph, "Snippets", "T_JW");
    this.connectNode(graph, "Snippets", "Entities");
    this.connectNode(graph, "Snippets", "EJB/Servlet Stubs");
    this.connectNode(graph, "DTL", "T_DTL");
    this.connectNode(graph, "T_DTL", "TRM_DTL");
    this.connectNode(graph, "T_DTL", "Entities");
    this.connectNode(graph, "TRM_DTL", "T_behavior");
    this.connectNode(graph, "TRM_DTL", "T_ProtoCom");
    this.connectNode(graph, "Entities", "T_javac3");
    this.connectNode(graph, "T_javac3", "Entity Classes");
    return graph;
  }
  
  private void makeNodes(final Hypergraph hypergraph, final List<String> strings) {
    final Consumer<String> _function = (String name) -> {
      final Node node = HypergraphFactory.eINSTANCE.createNode();
      node.setName(name);
      EList<Node> _nodes = hypergraph.getNodes();
      _nodes.add(node);
    };
    strings.forEach(_function);
  }
  
  private Boolean connectNode(final Hypergraph graph, final String sourceNodeName, final String targetNodeName) {
    boolean _xblockexpression = false;
    {
      EList<Node> _nodes = graph.getNodes();
      final Function1<Node, Boolean> _function = (Node node) -> {
        String _name = node.getName();
        return Boolean.valueOf(_name.equals(sourceNodeName));
      };
      final Node sourceNode = IterableExtensions.<Node>findFirst(_nodes, _function);
      EList<Node> _nodes_1 = graph.getNodes();
      final Function1<Node, Boolean> _function_1 = (Node node) -> {
        String _name = node.getName();
        return Boolean.valueOf(_name.equals(targetNodeName));
      };
      final Node targetNode = IterableExtensions.<Node>findFirst(_nodes_1, _function_1);
      boolean _xifexpression = false;
      if (((!Objects.equal(sourceNode, null)) && (!Objects.equal(targetNode, null)))) {
        boolean _xblockexpression_1 = false;
        {
          final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
          edge.setName(((sourceNodeName + "::") + targetNodeName));
          EList<Edge> _edges = graph.getEdges();
          _edges.add(edge);
          EList<Edge> _edges_1 = sourceNode.getEdges();
          _edges_1.add(edge);
          EList<Edge> _edges_2 = targetNode.getEdges();
          _xblockexpression_1 = _edges_2.add(edge);
        }
        _xifexpression = _xblockexpression_1;
      } else {
        boolean _equals = Objects.equal(sourceNode, null);
        if (_equals) {
          System.out.println(("missing source node " + sourceNodeName));
        }
        boolean _equals_1 = Objects.equal(targetNode, null);
        if (_equals_1) {
          System.out.println(("missing target node " + targetNodeName));
        }
      }
      _xblockexpression = _xifexpression;
    }
    return Boolean.valueOf(_xblockexpression);
  }
}
