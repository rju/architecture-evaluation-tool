package de.cau.cs.se.software.evaluation.graph.transformation;

import com.google.common.base.Objects;
import de.cau.cs.se.geco.architecture.framework.IGenerator;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph;
import de.cau.cs.se.software.evaluation.graph.transformation.TransformationFactory;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.emf.common.util.EList;

@SuppressWarnings("all")
public class ManipulatePlanarGraph implements IGenerator<PlanarVisualizationGraph, PlanarVisualizationGraph> {
  private final boolean framework;
  
  private final boolean anonymous;
  
  private final boolean iface;
  
  private final Map<PlanarNode, PlanarNode> nodeMap = new HashMap<PlanarNode, PlanarNode>();
  
  public ManipulatePlanarGraph(final boolean framework, final boolean anonymous, final boolean iface) {
    this.framework = framework;
    this.anonymous = anonymous;
    this.iface = iface;
  }
  
  @Override
  public PlanarVisualizationGraph generate(final PlanarVisualizationGraph input) {
    final PlanarVisualizationGraph result = TransformationFactory.eINSTANCE.createPlanarVisualizationGraph();
    EList<PlanarNode> _nodes = input.getNodes();
    final Consumer<PlanarNode> _function = (PlanarNode node) -> {
      PlanarNode _switchResult = null;
      EModuleKind _kind = node.getKind();
      if (_kind != null) {
        switch (_kind) {
          case FRAMEWORK:
            PlanarNode _xifexpression = null;
            if (this.framework) {
              _xifexpression = this.duplicate(node);
            }
            _switchResult = _xifexpression;
            break;
          case ANONYMOUS:
            PlanarNode _xifexpression_1 = null;
            if (this.anonymous) {
              _xifexpression_1 = this.duplicate(node);
            }
            _switchResult = _xifexpression_1;
            break;
          case SYSTEM:
            _switchResult = this.duplicate(node);
            break;
          case INTERFACE:
            PlanarNode _xifexpression_2 = null;
            if (this.iface) {
              _xifexpression_2 = this.duplicate(node);
            }
            _switchResult = _xifexpression_2;
            break;
          default:
            break;
        }
      }
      final PlanarNode duplicateNode = _switchResult;
      boolean _notEquals = (!Objects.equal(duplicateNode, null));
      if (_notEquals) {
        EList<PlanarNode> _nodes_1 = result.getNodes();
        _nodes_1.add(duplicateNode);
        this.nodeMap.put(node, duplicateNode);
      }
    };
    _nodes.forEach(_function);
    EList<PlanarEdge> _edges = input.getEdges();
    final Consumer<PlanarEdge> _function_1 = (PlanarEdge edge) -> {
      PlanarNode _start = edge.getStart();
      final PlanarNode start = this.nodeMap.get(_start);
      PlanarNode _end = edge.getEnd();
      final PlanarNode end = this.nodeMap.get(_end);
      boolean _notEquals = (!Objects.equal(start, null));
      if (_notEquals) {
        boolean _notEquals_1 = (!Objects.equal(end, null));
        if (_notEquals_1) {
          final PlanarEdge duplicateEdge = TransformationFactory.eINSTANCE.createPlanarEdge();
          int _count = edge.getCount();
          duplicateEdge.setCount(_count);
          EList<PlanarEdge> _edges_1 = start.getEdges();
          _edges_1.add(duplicateEdge);
          EList<PlanarEdge> _edges_2 = end.getEdges();
          _edges_2.add(duplicateEdge);
          duplicateEdge.setStart(start);
          duplicateEdge.setEnd(end);
          EList<PlanarEdge> _edges_3 = result.getEdges();
          _edges_3.add(duplicateEdge);
        }
      }
    };
    _edges.forEach(_function_1);
    return result;
  }
  
  private PlanarNode duplicate(final PlanarNode node) {
    final PlanarNode duplicateNode = TransformationFactory.eINSTANCE.createPlanarNode();
    String _name = node.getName();
    duplicateNode.setName(_name);
    String _context = node.getContext();
    duplicateNode.setContext(_context);
    EModuleKind _kind = node.getKind();
    duplicateNode.setKind(_kind);
    return duplicateNode;
  }
}
