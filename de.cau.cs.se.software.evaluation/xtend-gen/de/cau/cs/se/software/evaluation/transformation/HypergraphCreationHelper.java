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
package de.cau.cs.se.software.evaluation.transformation;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.EdgeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleTrace;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeTrace;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class HypergraphCreationHelper {
  /**
   * Create a node for a hypergraph and set the derived from reference to the
   * specified element.
   * 
   * @param hypergraph
   * @param name the name of the node
   * @param element a model element related to that node
   */
  public static Node createNode(final Hypergraph hypergraph, final String name, final EObject element) {
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    node.setName(name);
    boolean _notEquals = (!Objects.equal(element, null));
    if (_notEquals) {
      final ModelElementTrace reference = HypergraphFactory.eINSTANCE.createModelElementTrace();
      reference.setElement(element);
      node.setDerivedFrom(reference);
    } else {
      node.setDerivedFrom(null);
    }
    EList<Node> _nodes = hypergraph.getNodes();
    _nodes.add(node);
    return node;
  }
  
  /**
   * Create a node for a modular hypergraph and set the derived from reference to the
   * specified element.
   * 
   * @param hypergraph the modular hypergraph
   * @param module a module of that modular hypergraph
   * @param name the name of the node
   * @param element a model element related to that node
   */
  public static Node createNode(final ModularHypergraph hypergraph, final Module module, final String name, final EObject element) {
    final Node node = HypergraphCreationHelper.createNode(hypergraph, name, element);
    EList<Node> _nodes = module.getNodes();
    _nodes.add(node);
    return node;
  }
  
  /**
   * Create an edge for a hypergraph between the two specified nodes.
   * 
   * @param hypergraph
   * @param source the first node
   * @param target the second node
   * @param name the name of the edge
   * @param element a model element related to that edge
   */
  public static Edge createEdge(final Hypergraph hypergraph, final Node source, final Node target, final String name, final EObject element) {
    final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
    edge.setName(name);
    boolean _notEquals = (!Objects.equal(element, null));
    if (_notEquals) {
      final ModelElementTrace reference = HypergraphFactory.eINSTANCE.createModelElementTrace();
      reference.setElement(element);
      edge.setDerivedFrom(reference);
    } else {
      edge.setDerivedFrom(null);
    }
    EList<Edge> _edges = hypergraph.getEdges();
    _edges.add(edge);
    EList<Edge> _edges_1 = source.getEdges();
    _edges_1.add(edge);
    EList<Edge> _edges_2 = target.getEdges();
    _edges_2.add(edge);
    return edge;
  }
  
  /**
   * Create a module for a modular hypergraph and set the derived from reference to the
   * specified element.
   * 
   * @param hypergraph
   * @param name the name of the module
   * @param element a model element related to that module
   */
  public static Module createModule(final ModularHypergraph hypergraph, final String name, final EObject element) {
    final Module module = HypergraphFactory.eINSTANCE.createModule();
    module.setName(name);
    boolean _notEquals = (!Objects.equal(element, null));
    if (_notEquals) {
      final ModelElementTrace reference = HypergraphFactory.eINSTANCE.createModelElementTrace();
      reference.setElement(element);
      module.setDerivedFrom(reference);
    } else {
      module.setDerivedFrom(null);
    }
    EList<Module> _modules = hypergraph.getModules();
    _modules.add(module);
    return module;
  }
  
  /**
   * Derive a new node from an old node and set the appropriate trace information.
   * 
   * @param node the old node
   * 
   * @returns the new node
   */
  public static Node deriveNode(final Node node) {
    final Node resultNode = HypergraphFactory.eINSTANCE.createNode();
    String _name = node.getName();
    resultNode.setName(_name);
    final NodeTrace derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace();
    derivedFrom.setNode(node);
    resultNode.setDerivedFrom(derivedFrom);
    return resultNode;
  }
  
  /**
   * Derive a new edge from an old edge and set the appropriate trace information.
   * 
   * @param edge the old edge
   * 
   * @returns the new edge
   */
  public static Edge deriveEdge(final Edge edge) {
    final Edge resultEdge = HypergraphFactory.eINSTANCE.createEdge();
    String _name = edge.getName();
    resultEdge.setName(_name);
    final EdgeTrace derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace();
    derivedFrom.setEdge(edge);
    resultEdge.setDerivedFrom(derivedFrom);
    return resultEdge;
  }
  
  /**
   * Derive a new module from an old module and set the appropriate trace information.
   * 
   * @param module the old module
   * 
   * @returns the new module
   */
  public static Module deriveModule(final Module module) {
    final Module resultModule = HypergraphFactory.eINSTANCE.createModule();
    String _name = module.getName();
    resultModule.setName(_name);
    final ModuleTrace derivedFrom = HypergraphFactory.eINSTANCE.createModuleTrace();
    derivedFrom.setModule(module);
    resultModule.setDerivedFrom(derivedFrom);
    return resultModule;
  }
  
  /**
   * Create an edge between two nodes. Despite the fact that our hypergraph
   * edges do not have a direction, we use the terms source and target here for
   * nodes to increase the understandability of the reoutine.
   * 
   * @param hypergraph a modular hypergraph
   * @param source a node of that hypergraph
   * @param target a second node of that hypergraph
   * 
   * @return an edge between these two nodes which is also added to the source and target node, as
   * well as the hypergraph
   */
  public static Edge createUniqueEdge(final ModularHypergraph hypergraph, final Node source, final Node target) {
    EList<Edge> _edges = source.getEdges();
    final Function1<Edge, Boolean> _function = (Edge sourceEdge) -> {
      EList<Edge> _edges_1 = target.getEdges();
      final Function1<Edge, Boolean> _function_1 = (Edge targetEdge) -> {
        return Boolean.valueOf(Objects.equal(targetEdge, sourceEdge));
      };
      return Boolean.valueOf(IterableExtensions.<Edge>exists(_edges_1, _function_1));
    };
    final Iterable<Edge> edgeSubset = IterableExtensions.<Edge>filter(_edges, _function);
    String _name = source.getName();
    String _plus = (_name + "::");
    String _name_1 = target.getName();
    final String edgeName = (_plus + _name_1);
    final Function1<Edge, Boolean> _function_1 = (Edge edge) -> {
      String _name_2 = edge.getName();
      return Boolean.valueOf(_name_2.equals(edgeName));
    };
    final Edge existingEdge = IterableExtensions.<Edge>findFirst(edgeSubset, _function_1);
    boolean _equals = Objects.equal(existingEdge, null);
    if (_equals) {
      return HypergraphCreationHelper.createEdge(hypergraph, source, target, edgeName, null);
    } else {
      return existingEdge;
    }
  }
}
