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
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleTrace;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeTrace;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationHelper {
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
   * Create an edge between two nodes.
   */
  public static boolean createEdgeBetweenMethods(final ModularHypergraph hypergraph, final Node caller, final Node callee) {
    boolean _xblockexpression = false;
    {
      EList<Edge> _edges = caller.getEdges();
      final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
        public Boolean apply(final Edge callerEdge) {
          EList<Edge> _edges = callee.getEdges();
          final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
            public Boolean apply(final Edge calleeEdge) {
              return Boolean.valueOf(Objects.equal(calleeEdge, callerEdge));
            }
          };
          return Boolean.valueOf(IterableExtensions.<Edge>exists(_edges, _function));
        }
      };
      final Iterable<Edge> edgeSubset = IterableExtensions.<Edge>filter(_edges, _function);
      String _name = caller.getName();
      String _plus = (_name + "::");
      String _name_1 = callee.getName();
      final String edgeName = (_plus + _name_1);
      final Function1<Edge, Boolean> _function_1 = new Function1<Edge, Boolean>() {
        public Boolean apply(final Edge edge) {
          String _name = edge.getName();
          return Boolean.valueOf(_name.equals(edgeName));
        }
      };
      final Edge existingEdge = IterableExtensions.<Edge>findFirst(edgeSubset, _function_1);
      boolean _xifexpression = false;
      boolean _equals = Objects.equal(existingEdge, null);
      if (_equals) {
        boolean _xblockexpression_1 = false;
        {
          final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
          edge.setDerivedFrom(null);
          edge.setName(edgeName);
          EList<Edge> _edges_1 = hypergraph.getEdges();
          _edges_1.add(edge);
          EList<Edge> _edges_2 = callee.getEdges();
          _edges_2.add(edge);
          EList<Edge> _edges_3 = caller.getEdges();
          _xblockexpression_1 = _edges_3.add(edge);
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
}
