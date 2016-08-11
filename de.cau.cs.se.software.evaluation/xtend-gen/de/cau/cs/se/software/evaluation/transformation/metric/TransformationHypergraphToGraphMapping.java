package de.cau.cs.se.software.evaluation.transformation.metric;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import de.cau.cs.se.software.evaluation.views.LogModelProvider;
import java.util.HashMap;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

/**
 * Transform a hypergraph into a graph.
 * 
 * All hyperedges with more than two participant nodes are replaced by a node
 * and edges for each of the hyperedge participant nodes.
 */
@SuppressWarnings("all")
public class TransformationHypergraphToGraphMapping extends AbstractTransformation<ModularHypergraph, ModularHypergraph> {
  public TransformationHypergraphToGraphMapping(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public ModularHypergraph generate(final ModularHypergraph input) {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.result = _createModularHypergraph;
    final HashMap<Node, Node> nodeMap = new HashMap<Node, Node>();
    final HashMap<Module, Module> moduleMap = new HashMap<Module, Module>();
    final HashMap<Node, Module> nodeModuleMap = new HashMap<Node, Module>();
    EList<Module> _modules = input.getModules();
    final Consumer<Module> _function = (Module module) -> {
      final Module derivedModule = HypergraphCreationHelper.deriveModule(module);
      moduleMap.put(module, derivedModule);
      EList<Node> _nodes = module.getNodes();
      final Consumer<Node> _function_1 = (Node it) -> {
        nodeModuleMap.put(it, module);
      };
      _nodes.forEach(_function_1);
      EList<Module> _modules_1 = this.result.getModules();
      _modules_1.add(derivedModule);
    };
    _modules.forEach(_function);
    boolean _isCanceled = this.monitor.isCanceled();
    if (_isCanceled) {
      return null;
    }
    EList<Node> _nodes = input.getNodes();
    final Consumer<Node> _function_1 = (Node node) -> {
      final Node derivedNode = HypergraphCreationHelper.deriveNode(node);
      nodeMap.put(node, derivedNode);
      EList<Node> _nodes_1 = this.result.getNodes();
      _nodes_1.add(derivedNode);
    };
    _nodes.forEach(_function_1);
    boolean _isCanceled_1 = this.monitor.isCanceled();
    if (_isCanceled_1) {
      return null;
    }
    EList<Edge> _edges = input.getEdges();
    final Consumer<Edge> _function_2 = (Edge edge) -> {
      boolean _isCanceled_2 = this.monitor.isCanceled();
      boolean _not = (!_isCanceled_2);
      if (_not) {
        EList<Node> _nodes_1 = input.getNodes();
        final Function1<Node, Boolean> _function_3 = (Node it) -> {
          EList<Edge> _edges_1 = it.getEdges();
          return Boolean.valueOf(_edges_1.contains(edge));
        };
        final Iterable<Node> connectedNodes = IterableExtensions.<Node>filter(_nodes_1, _function_3);
        int _size = IterableExtensions.size(connectedNodes);
        boolean _greaterThan = (_size > 2);
        if (_greaterThan) {
          Object _get = ((Object[])Conversions.unwrapArray(connectedNodes, Object.class))[0];
          final Module module = nodeModuleMap.get(_get);
          Module _get_1 = moduleMap.get(module);
          String _name = edge.getName();
          final Node derivedNode = HypergraphCreationHelper.createNode(this.result, _get_1, _name, edge);
          final Consumer<Node> _function_4 = (Node connectedNode) -> {
            Node _get_2 = nodeMap.get(connectedNode);
            String _name_1 = connectedNode.getName();
            String _plus = (_name_1 + "::");
            String _name_2 = derivedNode.getName();
            String _plus_1 = (_plus + _name_2);
            HypergraphCreationHelper.createEdge(this.result, _get_2, derivedNode, _plus_1, edge);
          };
          connectedNodes.forEach(_function_4);
        } else {
          int _size_1 = IterableExtensions.size(connectedNodes);
          boolean _equals = (_size_1 == 2);
          if (_equals) {
            final Edge derivedEdge = HypergraphCreationHelper.deriveEdge(edge);
            Object _get_2 = ((Object[])Conversions.unwrapArray(connectedNodes, Object.class))[0];
            Node _get_3 = nodeMap.get(_get_2);
            EList<Edge> _edges_1 = _get_3.getEdges();
            _edges_1.add(derivedEdge);
            Object _get_4 = ((Object[])Conversions.unwrapArray(connectedNodes, Object.class))[1];
            Node _get_5 = nodeMap.get(_get_4);
            EList<Edge> _edges_2 = _get_5.getEdges();
            _edges_2.add(derivedEdge);
            EList<Edge> _edges_3 = this.result.getEdges();
            _edges_3.add(derivedEdge);
          } else {
            int _size_2 = IterableExtensions.size(connectedNodes);
            boolean _equals_1 = (_size_2 == 1);
            if (_equals_1) {
              final Edge derivedEdge_1 = HypergraphCreationHelper.deriveEdge(edge);
              Object _get_6 = ((Object[])Conversions.unwrapArray(connectedNodes, Object.class))[0];
              Node _get_7 = nodeMap.get(_get_6);
              EList<Edge> _edges_4 = _get_7.getEdges();
              _edges_4.add(derivedEdge_1);
              Object _get_8 = ((Object[])Conversions.unwrapArray(connectedNodes, Object.class))[0];
              Node _get_9 = nodeMap.get(_get_8);
              EList<Edge> _edges_5 = _get_9.getEdges();
              _edges_5.add(derivedEdge_1);
              EList<Edge> _edges_6 = this.result.getEdges();
              _edges_6.add(derivedEdge_1);
            } else {
              String _name_1 = edge.getName();
              String _plus = ("The edge " + _name_1);
              String _plus_1 = (_plus + " is not used. Connected nodes are ");
              int _size_3 = IterableExtensions.size(connectedNodes);
              String _plus_2 = (_plus_1 + Integer.valueOf(_size_3));
              LogModelProvider.INSTANCE.addMessage("Edge Warning", _plus_2);
            }
          }
        }
      }
    };
    _edges.forEach(_function_2);
    return this.result;
  }
}
