package de.cau.cs.se.software.evaluation.graph.transformation;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph;
import de.cau.cs.se.software.evaluation.graph.transformation.TransformationFactory;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class VisualizationPlanarGraph /* implements IGenerator<ModularHypergraph, PlanarVisualizationGraph>  */{
  @Override
  public PlanarVisualizationGraph generate(final ModularHypergraph input) {
    final PlanarVisualizationGraph result = TransformationFactory.eINSTANCE.createPlanarVisualizationGraph();
    final Map<Module, PlanarNode> moduleMap = new HashMap<Module, PlanarNode>();
    EList<Module> _modules = input.getModules();
    final Consumer<Module> _function = (Module module) -> {
      final PlanarNode node = TransformationFactory.eINSTANCE.createPlanarNode();
      String _xifexpression = null;
      EModuleKind _kind = module.getKind();
      boolean _equals = Objects.equal(_kind, EModuleKind.ANONYMOUS);
      if (_equals) {
        String _xblockexpression = null;
        {
          String _name = module.getName();
          final int separator = _name.lastIndexOf("$");
          String _name_1 = module.getName();
          _xblockexpression = _name_1.substring(0, separator);
        }
        _xifexpression = _xblockexpression;
      } else {
        _xifexpression = module.getName();
      }
      final String moduleQualifier = _xifexpression;
      final int separator = moduleQualifier.lastIndexOf(".");
      String _substring = moduleQualifier.substring(0, separator);
      node.setName(_substring);
      String _substring_1 = moduleQualifier.substring((separator + 1));
      node.setContext(_substring_1);
      EModuleKind _kind_1 = module.getKind();
      node.setKind(_kind_1);
      EList<PlanarNode> _nodes = result.getNodes();
      _nodes.add(node);
      moduleMap.put(module, node);
    };
    _modules.forEach(_function);
    EList<Edge> _edges = input.getEdges();
    final Consumer<Edge> _function_1 = (Edge hyperedge) -> {
      EList<Node> _nodes = input.getNodes();
      final Function1<Node, Boolean> _function_2 = (Node node) -> {
        EList<Edge> _edges_1 = node.getEdges();
        return Boolean.valueOf(_edges_1.contains(hyperedge));
      };
      final Iterable<Node> edgeNodes = IterableExtensions.<Node>filter(_nodes, _function_2);
      final Consumer<Node> _function_3 = (Node startNode) -> {
        final Consumer<Node> _function_4 = (Node endNode) -> {
          EList<Module> _modules_1 = input.getModules();
          final Function1<Module, Boolean> _function_5 = (Module it) -> {
            EList<Node> _nodes_1 = it.getNodes();
            return Boolean.valueOf(_nodes_1.contains(startNode));
          };
          final Module startModule = IterableExtensions.<Module>findFirst(_modules_1, _function_5);
          EList<Module> _modules_2 = input.getModules();
          final Function1<Module, Boolean> _function_6 = (Module it) -> {
            EList<Node> _nodes_1 = it.getNodes();
            return Boolean.valueOf(_nodes_1.contains(endNode));
          };
          final Module endModule = IterableExtensions.<Module>findFirst(_modules_2, _function_6);
          boolean _notEquals = (!Objects.equal(startModule, endModule));
          if (_notEquals) {
            final PlanarNode startPlanarNode = moduleMap.get(startModule);
            final PlanarNode endPlanarNode = moduleMap.get(endModule);
            EList<PlanarEdge> _edges_1 = startPlanarNode.getEdges();
            final Function1<PlanarEdge, Boolean> _function_7 = (PlanarEdge it) -> {
              EList<PlanarEdge> _edges_2 = endPlanarNode.getEdges();
              return Boolean.valueOf(_edges_2.contains(it));
            };
            final PlanarEdge planarEdge = IterableExtensions.<PlanarEdge>findFirst(_edges_1, _function_7);
            boolean _equals = Objects.equal(planarEdge, null);
            if (_equals) {
              EList<PlanarEdge> _edges_2 = result.getEdges();
              PlanarEdge _createPlanarEdge = this.createPlanarEdge(startPlanarNode, endPlanarNode);
              _edges_2.add(_createPlanarEdge);
            } else {
              int _count = planarEdge.getCount();
              int _plus = (_count + 1);
              planarEdge.setCount(_plus);
            }
          }
        };
        edgeNodes.forEach(_function_4);
      };
      edgeNodes.forEach(_function_3);
    };
    _edges.forEach(_function_1);
    return result;
  }
  
  private PlanarEdge createPlanarEdge(final PlanarNode start, final PlanarNode end) {
    final PlanarEdge edge = TransformationFactory.eINSTANCE.createPlanarEdge();
    edge.setCount(1);
    edge.setStart(start);
    edge.setEnd(end);
    EList<PlanarEdge> _edges = start.getEdges();
    _edges.add(edge);
    EList<PlanarEdge> _edges_1 = end.getEdges();
    _edges_1.add(edge);
    return edge;
  }
}
