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
package de.cau.cs.se.evaluation.architecture.graph;

import com.google.common.base.Objects;
import de.cau.cs.kieler.core.kgraph.KEdge;
import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.core.krendering.KAreaPlacementData;
import de.cau.cs.kieler.core.krendering.KColor;
import de.cau.cs.kieler.core.krendering.KEllipse;
import de.cau.cs.kieler.core.krendering.KGridPlacement;
import de.cau.cs.kieler.core.krendering.KPolyline;
import de.cau.cs.kieler.core.krendering.KRectangle;
import de.cau.cs.kieler.core.krendering.KRendering;
import de.cau.cs.kieler.core.krendering.KRenderingFactory;
import de.cau.cs.kieler.core.krendering.KRoundedRectangle;
import de.cau.cs.kieler.core.krendering.KText;
import de.cau.cs.kieler.core.krendering.LineStyle;
import de.cau.cs.kieler.core.krendering.extensions.KColorExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KContainerRenderingExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KEdgeExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KLabelExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KNodeExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KPolylineExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KPortExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KRenderingExtensions;
import de.cau.cs.kieler.kiml.options.Direction;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ModularHypergraphDiagramSynthesis extends AbstractDiagramSynthesis<ModularHypergraph> {
  @Inject
  @Extension
  private KNodeExtensions _kNodeExtensions;
  
  @Inject
  @Extension
  private KEdgeExtensions _kEdgeExtensions;
  
  @Inject
  @Extension
  private KPortExtensions _kPortExtensions;
  
  @Inject
  @Extension
  private KLabelExtensions _kLabelExtensions;
  
  @Inject
  @Extension
  private KRenderingExtensions _kRenderingExtensions;
  
  @Inject
  @Extension
  private KContainerRenderingExtensions _kContainerRenderingExtensions;
  
  @Inject
  @Extension
  private KPolylineExtensions _kPolylineExtensions;
  
  @Inject
  @Extension
  private KColorExtensions _kColorExtensions;
  
  @Extension
  private KRenderingFactory _kRenderingFactory = KRenderingFactory.eINSTANCE;
  
  private Map<Node, KNode> nodeMap = new HashMap<Node, KNode>();
  
  public KNode transform(final ModularHypergraph model) {
    KNode _createNode = this._kNodeExtensions.createNode(model);
    final KNode root = this.<KNode>associateWith(_createNode, model);
    final Procedure1<KNode> _function = new Procedure1<KNode>() {
      public void apply(final KNode it) {
        ModularHypergraphDiagramSynthesis.this._kNodeExtensions.<String>addLayoutParam(it, LayoutOptions.ALGORITHM, "de.cau.cs.kieler.kiml.ogdf.planarization");
        ModularHypergraphDiagramSynthesis.this._kNodeExtensions.<Float>addLayoutParam(it, LayoutOptions.SPACING, Float.valueOf(75f));
        ModularHypergraphDiagramSynthesis.this._kNodeExtensions.<Direction>addLayoutParam(it, LayoutOptions.DIRECTION, Direction.UP);
        EList<Module> _modules = model.getModules();
        final Consumer<Module> _function = new Consumer<Module>() {
          public void accept(final Module module) {
            EList<KNode> _children = it.getChildren();
            KNode _createModule = ModularHypergraphDiagramSynthesis.this.createModule(module);
            _children.add(_createModule);
          }
        };
        _modules.forEach(_function);
        EList<Edge> _edges = model.getEdges();
        final Consumer<Edge> _function_1 = new Consumer<Edge>() {
          public void accept(final Edge edge) {
            EList<Node> _nodes = model.getNodes();
            EList<KNode> _children = it.getChildren();
            ModularHypergraphDiagramSynthesis.this.createGraphEdge(edge, _nodes, _children);
          }
        };
        _edges.forEach(_function_1);
      }
    };
    ObjectExtensions.<KNode>operator_doubleArrow(root, _function);
    return root;
  }
  
  /**
   * Draw module as a rectangle with its nodes inside.
   * 
   * @param module the module to be rendered
   */
  public KNode createModule(final Module module) {
    KNode _xblockexpression = null;
    {
      KNode _createNode = this._kNodeExtensions.createNode(module);
      final KNode moduleNode = this.<KNode>associateWith(_createNode, module);
      final Procedure1<KNode> _function = new Procedure1<KNode>() {
        public void apply(final KNode it) {
          KRoundedRectangle _addRoundedRectangle = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addRoundedRectangle(it, 10, 10);
          final Procedure1<KRoundedRectangle> _function = new Procedure1<KRoundedRectangle>() {
            public void apply(final KRoundedRectangle it) {
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
              KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("white");
              KColor _color_1 = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("LemonChiffon");
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KRoundedRectangle>setBackgroundGradient(it, _color, _color_1, 0);
              KColor _color_2 = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("black");
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setShadow(it, _color_2);
              KGridPlacement _setGridPlacement = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.setGridPlacement(it, 1);
              KGridPlacement _from = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.from(_setGridPlacement, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.LEFT, 10, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.TOP, 10, 0);
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.to(_from, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.RIGHT, 10, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.BOTTOM, 10, 0);
              KRectangle _addRectangle = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addRectangle(it);
              final Procedure1<KRectangle> _function = new Procedure1<KRectangle>() {
                public void apply(final KRectangle it) {
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setInvisible(it, true);
                  String _name = module.getName();
                  KText _addText = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, _name);
                  final Procedure1<KText> _function = new Procedure1<KText>() {
                    public void apply(final KText it) {
                      ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontSize(it, 16);
                      ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontBold(it, true);
                      it.setCursorSelectable(true);
                      ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KText>setLeftTopAlignedPointPlacementData(it, 1, 1, 1, 1);
                    }
                  };
                  ObjectExtensions.<KText>operator_doubleArrow(_addText, _function);
                }
              };
              ObjectExtensions.<KRectangle>operator_doubleArrow(_addRectangle, _function);
              KRectangle _addRectangle_1 = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addRectangle(it);
              final Procedure1<KRectangle> _function_1 = new Procedure1<KRectangle>() {
                public void apply(final KRectangle it) {
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setInvisible(it, true);
                  KGridPlacement _setGridPlacement = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.setGridPlacement(it, 2);
                  KGridPlacement _from = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.from(_setGridPlacement, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.LEFT, 10, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.TOP, 10, 0);
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.to(_from, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.RIGHT, 10, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.BOTTOM, 10, 0);
                  EList<Node> _nodes = module.getNodes();
                  final Consumer<Node> _function = new Consumer<Node>() {
                    public void accept(final Node node) {
                      EList<KNode> _children = moduleNode.getChildren();
                      ModularHypergraphDiagramSynthesis.this.createGraphNode(node, it, _children);
                    }
                  };
                  _nodes.forEach(_function);
                }
              };
              ObjectExtensions.<KRectangle>operator_doubleArrow(_addRectangle_1, _function_1);
            }
          };
          ObjectExtensions.<KRoundedRectangle>operator_doubleArrow(_addRoundedRectangle, _function);
        }
      };
      _xblockexpression = ObjectExtensions.<KNode>operator_doubleArrow(moduleNode, _function);
    }
    return _xblockexpression;
  }
  
  /**
   * Draw a single node as a circle with its name at the center.
   * 
   * @param node the node to be rendered
   */
  private boolean createGraphNode(final Node node, final KRectangle parent, final EList<KNode> siblings) {
    boolean _xblockexpression = false;
    {
      final KNode kNode = this._kNodeExtensions.createNode(node);
      this.nodeMap.put(node, kNode);
      final Procedure1<KNode> _function = new Procedure1<KNode>() {
        public void apply(final KNode it) {
          EList<KRendering> _children = parent.getChildren();
          KEllipse _addEllipse = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addEllipse(it);
          final Procedure1<KEllipse> _function = new Procedure1<KEllipse>() {
            public void apply(final KEllipse it) {
              ModularHypergraphDiagramSynthesis.this.<KEllipse>associateWith(it, node);
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
              KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("white");
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setBackground(it, _color);
              KAreaPlacementData _setAreaPlacementData = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setAreaPlacementData(it);
              KAreaPlacementData _from = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.from(_setAreaPlacementData, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.LEFT, 20, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.TOP, 20, 0.5f);
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.to(_from, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.RIGHT, 20, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.BOTTOM, 20, 0);
              String _name = node.getName();
              KText _addText = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, _name);
              final Procedure1<KText> _function = new Procedure1<KText>() {
                public void apply(final KText it) {
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontSize(it, 15);
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontBold(it, false);
                  it.setCursorSelectable(true);
                }
              };
              ObjectExtensions.<KText>operator_doubleArrow(_addText, _function);
              String _name_1 = node.getName();
              it.setId(_name_1);
            }
          };
          KEllipse _doubleArrow = ObjectExtensions.<KEllipse>operator_doubleArrow(_addEllipse, _function);
          _children.add(_doubleArrow);
        }
      };
      ObjectExtensions.<KNode>operator_doubleArrow(kNode, _function);
      _xblockexpression = siblings.add(kNode);
    }
    return _xblockexpression;
  }
  
  /**
   * def createEdges(EList<Node> nodes){
   * for (var i = 0; i<nodes.size; i++){
   * //for (var j = 0; j<nodes.get(i).getEdges.size; j++){
   * for(Edge j : nodes.get(i).edges){
   * var temp = findNode(j, nodes, nodes.get(i))
   * if(temp != null){
   * val child = temp
   * val parent = nodes.get(i)
   * //				new Pair(child, parent).createEdge() => [
   * //		            it.addLayoutParam(LayoutOptions::EDGE_TYPE, EdgeType::GENERALIZATION);
   * //		            // add semantic data
   * //		            it.getData(typeof(KLayoutData)).setProperty(KlighdProperties.SEMANTIC_DATA,
   * //		                        KlighdSemanticDiagramData.of(KlighdConstants.SEMANTIC_DATA_CLASS, "inheritence"))
   * //		    	    it.source = child.node
   * //			        it.target = parent.node
   * //			        it.data addPolyline() => [
   * //		                it.lineWidth = 2
   * //		                it.foreground = "gray25".color
   * //		                it.addInheritanceTriangleArrowDecorator()
   * //			        ]
   * //				]
   * child.createEdge(parent)=>[
   * it.addPolyline => [
   * it.lineWidth = 2
   * it.foreground = "gray25".color
   * ]
   * ]
   * 
   * }
   * }
   * }
   * }
   * 
   * def Node findNode(Edge edge, EList<Node> nodes, Node parent){
   * var Node result = null
   * //System.out.println(edge.name)
   * //TODO teste ob es ein(?) anderen(!) Node mit der gleicher Edge gibt (oder mehr)
   * for (var i = 0; i<nodes.size; i++){
   * if(nodes.get(i) != parent){
   * for(Edge j : nodes.get(i).edges){
   * //System.out.println(j.name)
   * if(edge.name.equals(j.name)){
   * result = nodes.get(i)
   * }
   * }}}
   * return result
   * }
   * }
   */
  private Object createGraphEdge(final Edge edge, final EList<Node> nodes, final EList<KNode> siblings) {
    Object _xblockexpression = null;
    {
      final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
        public Boolean apply(final Node node) {
          EList<Edge> _edges = node.getEdges();
          final Function1<Edge, Boolean> _function = new Function1<Edge, Boolean>() {
            public Boolean apply(final Edge it) {
              return Boolean.valueOf(it.equals(edge));
            }
          };
          return Boolean.valueOf(IterableExtensions.<Edge>exists(_edges, _function));
        }
      };
      final Iterable<Node> referencedNodes = IterableExtensions.<Node>filter(nodes, _function);
      String _name = edge.getName();
      String _plus = ("graph edge " + _name);
      String _plus_1 = (_plus + " size ");
      int _size = IterableExtensions.size(referencedNodes);
      String _plus_2 = (_plus_1 + Integer.valueOf(_size));
      System.out.println(_plus_2);
      Object _xifexpression = null;
      int _size_1 = IterableExtensions.size(referencedNodes);
      boolean _greaterThan = (_size_1 > 1);
      if (_greaterThan) {
        Object _xifexpression_1 = null;
        boolean _equals = Objects.equal(referencedNodes, Integer.valueOf(2));
        if (_equals) {
          Object _get = ((Object[])Conversions.unwrapArray(referencedNodes, Object.class))[0];
          KNode _get_1 = this.nodeMap.get(_get);
          Object _get_2 = ((Object[])Conversions.unwrapArray(referencedNodes, Object.class))[1];
          KNode _get_3 = this.nodeMap.get(_get_2);
          _xifexpression_1 = this.drawEdge(_get_1, _get_3);
        } else {
          KNode _drawHyperEdge = this.drawHyperEdge(edge, referencedNodes);
          _xifexpression_1 = Boolean.valueOf(siblings.add(_drawHyperEdge));
        }
        _xifexpression = _xifexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  private KNode drawHyperEdge(final Edge edge, final Iterable<Node> nodes) {
    KNode _createNode = this._kNodeExtensions.createNode(edge);
    final Procedure1<KNode> _function = new Procedure1<KNode>() {
      public void apply(final KNode it) {
        KEllipse _addEllipse = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addEllipse(it);
        final Procedure1<KEllipse> _function = new Procedure1<KEllipse>() {
          public void apply(final KEllipse it) {
            ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
            String _name = edge.getName();
            ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, _name);
          }
        };
        ObjectExtensions.<KEllipse>operator_doubleArrow(_addEllipse, _function);
      }
    };
    final KNode edgeNode = ObjectExtensions.<KNode>operator_doubleArrow(_createNode, _function);
    final Consumer<Node> _function_1 = new Consumer<Node>() {
      public void accept(final Node node) {
        KNode _get = ModularHypergraphDiagramSynthesis.this.nodeMap.get(node);
        ModularHypergraphDiagramSynthesis.this.drawEdge(edgeNode, _get);
      }
    };
    nodes.forEach(_function_1);
    System.out.println(("edge " + edgeNode));
    return edgeNode;
  }
  
  private KEdge drawEdge(final KNode left, final KNode right) {
    KEdge _xblockexpression = null;
    {
      System.out.println(((("draw edge " + left) + " ") + right));
      KEdge _createEdge = this._kEdgeExtensions.createEdge();
      final Procedure1<KEdge> _function = new Procedure1<KEdge>() {
        public void apply(final KEdge it) {
          it.setSource(left);
          it.setTarget(right);
          KPolyline _addPolyline = ModularHypergraphDiagramSynthesis.this._kEdgeExtensions.addPolyline(it);
          final Procedure1<KPolyline> _function = new Procedure1<KPolyline>() {
            public void apply(final KPolyline it) {
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineStyle(it, LineStyle.SOLID);
            }
          };
          ObjectExtensions.<KPolyline>operator_doubleArrow(_addPolyline, _function);
        }
      };
      _xblockexpression = ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge, _function);
    }
    return _xblockexpression;
  }
}
