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

@SuppressWarnings("all")
public class TransformationIntermoduleHyperedgesOnlyGraph implements ITransformation {
  private final ModularHypergraph hypergraph;
  
  private ModularHypergraph resultHypergraph;
  
  public TransformationIntermoduleHyperedgesOnlyGraph(final ModularHypergraph hypergraph) {
    this.hypergraph = hypergraph;
  }
  
  public ModularHypergraph getIntermoduleHyperedgesOnlyGraph() {
    return this.resultHypergraph;
  }
  
  /**
   * Create a intermodule only hypergraph.
   */
  public void transform() {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.resultHypergraph = _createModularHypergraph;
    EList<Edge> _edges = this.hypergraph.getEdges();
    final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
      public Boolean apply(final Edge edge) {
        EList<Node> _nodes = TransformationIntermoduleHyperedgesOnlyGraph.this.hypergraph.getNodes();
        EList<Module> _modules = TransformationIntermoduleHyperedgesOnlyGraph.this.hypergraph.getModules();
        return TransformationIntermoduleHyperedgesOnlyGraph.this.isIntermoduleEdge(edge, _nodes, _modules);
      }
    };
    final Iterable<Edge> interModuleEdges = IterableExtensions.<Edge>filter(_edges, _function);
    final Consumer<Edge> _function_1 = new Consumer<Edge>() {
      public void accept(final Edge edge) {
        final Edge derivedEdge = TransformationHelper.deriveEdge(edge);
        EList<Edge> _edges = TransformationIntermoduleHyperedgesOnlyGraph.this.resultHypergraph.getEdges();
        _edges.add(derivedEdge);
        EList<Node> _nodes = TransformationIntermoduleHyperedgesOnlyGraph.this.hypergraph.getNodes();
        final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
          public Boolean apply(final Node node) {
            EList<Edge> _edges = node.getEdges();
            return Boolean.valueOf(_edges.contains(edge));
          }
        };
        Iterable<Node> _filter = IterableExtensions.<Node>filter(_nodes, _function);
        final Consumer<Node> _function_1 = new Consumer<Node>() {
          public void accept(final Node node) {
            EList<Node> _nodes = TransformationIntermoduleHyperedgesOnlyGraph.this.resultHypergraph.getNodes();
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
              EList<Node> _nodes_1 = TransformationIntermoduleHyperedgesOnlyGraph.this.resultHypergraph.getNodes();
              _nodes_1.add(derivedNode);
            }
            EList<Edge> _edges = derivedNode.getEdges();
            _edges.add(derivedEdge);
          }
        };
        _filter.forEach(_function_1);
      }
    };
    interModuleEdges.forEach(_function_1);
    EList<Module> _modules = this.hypergraph.getModules();
    final Consumer<Module> _function_2 = new Consumer<Module>() {
      public void accept(final Module module) {
        final Module derivedModule = TransformationHelper.deriveModule(module);
        EList<Node> _nodes = module.getNodes();
        final Consumer<Node> _function = new Consumer<Node>() {
          public void accept(final Node node) {
            EList<Node> _nodes = TransformationIntermoduleHyperedgesOnlyGraph.this.resultHypergraph.getNodes();
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
      }
    };
    _modules.forEach(_function_2);
  }
  
  /**
   * Check if the edge is an intermodule edge.
   */
  public Boolean isIntermoduleEdge(final Edge edge, final EList<Node> nodes, final EList<Module> modules) {
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node node) {
        EList<Edge> _edges = node.getEdges();
        return Boolean.valueOf(_edges.contains(edge));
      }
    };
    final Iterable<Node> connectedNodes = IterableExtensions.<Node>filter(nodes, _function);
    final Function1<Module, Boolean> _function_1 = new Function1<Module, Boolean>() {
      public Boolean apply(final Module module) {
        EList<Node> _nodes = module.getNodes();
        return TransformationIntermoduleHyperedgesOnlyGraph.this.intersects(_nodes, connectedNodes);
      }
    };
    final Iterable<Module> involvedModules = IterableExtensions.<Module>filter(modules, _function_1);
    int _size = IterableExtensions.size(involvedModules);
    return Boolean.valueOf((_size > 1));
  }
  
  /**
   * Check if the intersection of both sets is not empty.
   */
  public Boolean intersects(final EList<Node> set1, final Iterable<Node> set2) {
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node nodeSet1) {
        final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
          public Boolean apply(final Node nodeSet2) {
            return Boolean.valueOf(Objects.equal(nodeSet2, nodeSet1));
          }
        };
        return Boolean.valueOf(IterableExtensions.<Node>exists(set2, _function));
      }
    };
    final Iterable<Node> interset = IterableExtensions.<Node>filter(set1, _function);
    boolean _isEmpty = IterableExtensions.isEmpty(interset);
    return Boolean.valueOf((!_isEmpty));
  }
}
