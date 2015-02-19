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
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class TransfromationMaximalInterconnectedGraph implements ITransformation {
  private final ModularHypergraph hypergraph;
  
  private ModularHypergraph resultHypergraph;
  
  public TransfromationMaximalInterconnectedGraph(final ModularHypergraph hypergraph) {
    this.hypergraph = hypergraph;
  }
  
  public ModularHypergraph getMaximalInterconnectedGraph() {
    return this.resultHypergraph;
  }
  
  public void transform() {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.resultHypergraph = _createModularHypergraph;
    EList<Node> _nodes = this.hypergraph.getNodes();
    final Procedure1<Node> _function = new Procedure1<Node>() {
      public void apply(final Node it) {
        EList<Node> _nodes = TransfromationMaximalInterconnectedGraph.this.resultHypergraph.getNodes();
        Node _deriveNode = TransformationHelper.deriveNode(it);
        _nodes.add(_deriveNode);
      }
    };
    IterableExtensions.<Node>forEach(_nodes, _function);
    EList<Module> _modules = this.hypergraph.getModules();
    final Procedure1<Module> _function_1 = new Procedure1<Module>() {
      public void apply(final Module module) {
        final Module derivedModule = TransformationHelper.deriveModule(module);
        EList<Node> _nodes = module.getNodes();
        final Procedure1<Node> _function = new Procedure1<Node>() {
          public void apply(final Node node) {
            EList<Node> _nodes = derivedModule.getNodes();
            EList<Node> _nodes_1 = TransfromationMaximalInterconnectedGraph.this.resultHypergraph.getNodes();
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
        IterableExtensions.<Node>forEach(_nodes, _function);
        EList<Module> _modules = TransfromationMaximalInterconnectedGraph.this.resultHypergraph.getModules();
        _modules.add(derivedModule);
      }
    };
    IterableExtensions.<Module>forEach(_modules, _function_1);
    EList<Node> _nodes_1 = this.resultHypergraph.getNodes();
    final Procedure2<Node, Integer> _function_2 = new Procedure2<Node, Integer>() {
      public void apply(final Node startNode, final Integer startIndex) {
        for (int index = ((startIndex).intValue() + 1); (index < TransfromationMaximalInterconnectedGraph.this.resultHypergraph.getNodes().size()); index++) {
          {
            EList<Node> _nodes = TransfromationMaximalInterconnectedGraph.this.resultHypergraph.getNodes();
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
          }
        }
      }
    };
    IterableExtensions.<Node>forEach(_nodes_1, _function_2);
  }
}
