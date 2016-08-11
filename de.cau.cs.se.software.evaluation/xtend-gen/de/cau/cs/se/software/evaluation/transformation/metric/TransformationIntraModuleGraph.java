package de.cau.cs.se.software.evaluation.transformation.metric;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import java.util.HashMap;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationIntraModuleGraph extends AbstractTransformation<ModularHypergraph, ModularHypergraph> {
  public TransformationIntraModuleGraph(final IProgressMonitor monitor) {
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
      EList<Node> _nodes_1 = input.getNodes();
      final Function1<Node, Boolean> _function_3 = (Node it) -> {
        EList<Edge> _edges_1 = it.getEdges();
        return Boolean.valueOf(_edges_1.contains(edge));
      };
      final Iterable<Node> connectedNodes = IterableExtensions.<Node>filter(_nodes_1, _function_3);
      Object _get = ((Object[])Conversions.unwrapArray(connectedNodes, Object.class))[0];
      final Module module = nodeModuleMap.get(_get);
      final Function1<Node, Boolean> _function_4 = (Node node) -> {
        Module _get_1 = nodeModuleMap.get(node);
        return Boolean.valueOf(Objects.equal(_get_1, module));
      };
      boolean _forall = IterableExtensions.<Node>forall(connectedNodes, _function_4);
      if (_forall) {
        final Edge derivedEdge = HypergraphCreationHelper.deriveEdge(edge);
        final Consumer<Node> _function_5 = (Node connectedNode) -> {
          Node _get_1 = nodeMap.get(connectedNode);
          EList<Edge> _edges_1 = _get_1.getEdges();
          _edges_1.add(derivedEdge);
        };
        connectedNodes.forEach(_function_5);
        EList<Edge> _edges_1 = this.result.getEdges();
        _edges_1.add(derivedEdge);
      }
    };
    _edges.forEach(_function_2);
    return this.result;
  }
  
  @Override
  public int workEstimate(final ModularHypergraph input) {
    EList<Module> _modules = input.getModules();
    int _size = _modules.size();
    EList<Node> _nodes = input.getNodes();
    int _size_1 = _nodes.size();
    int _plus = (_size + _size_1);
    EList<Edge> _edges = input.getEdges();
    int _size_2 = _edges.size();
    EList<Node> _nodes_1 = input.getNodes();
    int _size_3 = _nodes_1.size();
    int _multiply = (_size_2 * _size_3);
    return (_plus + _multiply);
  }
}
