package de.cau.cs.se.evaluation.architecture.transformation;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationHelper {
  public static Node deriveNode(final Node node) {
    final Node resultNode = HypergraphFactory.eINSTANCE.createNode();
    String _name = node.getName();
    resultNode.setName(_name);
    final NodeTrace derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace();
    derivedFrom.setNode(node);
    resultNode.setDerivedFrom(derivedFrom);
    return resultNode;
  }
  
  public static Edge deriveEdge(final Edge edge) {
    final Edge resultEdge = HypergraphFactory.eINSTANCE.createEdge();
    String _name = edge.getName();
    resultEdge.setName(_name);
    final EdgeTrace derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace();
    derivedFrom.setEdge(edge);
    resultEdge.setDerivedFrom(derivedFrom);
    return resultEdge;
  }
  
  public static Module deriveModule(final Module module) {
    final Module resultModule = HypergraphFactory.eINSTANCE.createModule();
    String _name = module.getName();
    resultModule.setName(_name);
    final ModuleTrace derivedFrom = HypergraphFactory.eINSTANCE.createModuleTrace();
    derivedFrom.setModule(module);
    resultModule.setDerivedFrom(derivedFrom);
    return resultModule;
  }
  
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
