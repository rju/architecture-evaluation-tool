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
import de.cau.cs.kieler.core.krendering.KRenderingFactory;
import de.cau.cs.kieler.core.krendering.KRoundedRectangle;
import de.cau.cs.kieler.core.krendering.KText;
import de.cau.cs.kieler.core.krendering.extensions.KColorExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KContainerRenderingExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KEdgeExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KLabelExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KNodeExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KPolylineExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KPortExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KRenderingExtensions;
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutData;
import de.cau.cs.kieler.kiml.options.Direction;
import de.cau.cs.kieler.kiml.options.EdgeType;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.klighd.KlighdConstants;
import de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis;
import de.cau.cs.kieler.klighd.util.KlighdProperties;
import de.cau.cs.kieler.klighd.util.KlighdSemanticDiagramData;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import java.util.function.Consumer;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
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
        EList<Node> _nodes = model.getNodes();
        ModularHypergraphDiagramSynthesis.this.createEdges(_nodes);
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
    KNode _createNode = this._kNodeExtensions.createNode(module);
    KNode _associateWith = this.<KNode>associateWith(_createNode, module);
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
            KGridPlacement _setGridPlacement = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.setGridPlacement(it, 2);
            KGridPlacement _from = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.from(_setGridPlacement, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.LEFT, 2, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.TOP, 2, 0);
            ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.to(_from, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.RIGHT, 2, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.BOTTOM, 2, 0);
            KRectangle _addRectangle = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addRectangle(it);
            final Procedure1<KRectangle> _function = new Procedure1<KRectangle>() {
              public void apply(final KRectangle it) {
                ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setInvisible(it, true);
                String _name = module.getName();
                KText _addText = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, _name);
                KText _associateWith = ModularHypergraphDiagramSynthesis.this.<KText>associateWith(_addText, module);
                final Procedure1<KText> _function = new Procedure1<KText>() {
                  public void apply(final KText it) {
                    ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontSize(it, 18);
                    ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontBold(it, true);
                    it.setCursorSelectable(true);
                    ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KText>setLeftTopAlignedPointPlacementData(it, 1, 1, 1, 1);
                  }
                };
                ObjectExtensions.<KText>operator_doubleArrow(_associateWith, _function);
              }
            };
            ObjectExtensions.<KRectangle>operator_doubleArrow(_addRectangle, _function);
            EList<Node> _nodes = module.getNodes();
            final Consumer<Node> _function_1 = new Consumer<Node>() {
              public void accept(final Node node) {
                KNode _createNode = ModularHypergraphDiagramSynthesis.this._kNodeExtensions.createNode(module);
                EList<KNode> _children = _createNode.getChildren();
                KNode _createGraphNode = ModularHypergraphDiagramSynthesis.this.createGraphNode(node);
                _children.add(_createGraphNode);
              }
            };
            _nodes.forEach(_function_1);
          }
        };
        ObjectExtensions.<KRoundedRectangle>operator_doubleArrow(_addRoundedRectangle, _function);
      }
    };
    return ObjectExtensions.<KNode>operator_doubleArrow(_associateWith, _function);
  }
  
  /**
   * Draw a single node as a circle with its name at the center.
   * 
   * @param node the node to be rendered
   */
  public KNode createGraphNode(final Node node) {
    KNode _createNode = this._kNodeExtensions.createNode(node);
    KNode _associateWith = this.<KNode>associateWith(_createNode, node);
    final Procedure1<KNode> _function = new Procedure1<KNode>() {
      public void apply(final KNode it) {
        KEllipse _addEllipse = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addEllipse(it);
        final Procedure1<KEllipse> _function = new Procedure1<KEllipse>() {
          public void apply(final KEllipse it) {
            ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
            KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("white");
            ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setBackground(it, _color);
            String _name = node.getName();
            KText _addText = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, _name);
            KText _associateWith = ModularHypergraphDiagramSynthesis.this.<KText>associateWith(_addText, node);
            final Procedure1<KText> _function = new Procedure1<KText>() {
              public void apply(final KText it) {
                ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontSize(it, 15);
                ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontBold(it, true);
                it.setCursorSelectable(true);
                KAreaPlacementData _setAreaPlacementData = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setAreaPlacementData(it);
                KAreaPlacementData _from = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.from(_setAreaPlacementData, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.LEFT, 20, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.TOP, 1, 0.5f);
                ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.to(_from, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.RIGHT, 20, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.BOTTOM, 10, 0);
              }
            };
            ObjectExtensions.<KText>operator_doubleArrow(_associateWith, _function);
          }
        };
        ObjectExtensions.<KEllipse>operator_doubleArrow(_addEllipse, _function);
      }
    };
    return ObjectExtensions.<KNode>operator_doubleArrow(_associateWith, _function);
  }
  
  public void createEdges(final EList<Node> nodes) {
    for (int i = 0; (i < nodes.size()); i++) {
      Node _get = nodes.get(i);
      EList<Edge> _edges = _get.getEdges();
      for (final Edge j : _edges) {
        {
          Node _get_1 = nodes.get(i);
          Node temp = this.findNode(j, nodes, _get_1);
          boolean _notEquals = (!Objects.equal(temp, null));
          if (_notEquals) {
            final Node child = temp;
            final Node parent = nodes.get(i);
            Pair<Node, Node> _pair = new Pair<Node, Node>(child, parent);
            KEdge _createEdge = this._kEdgeExtensions.createEdge(_pair);
            final Procedure1<KEdge> _function = new Procedure1<KEdge>() {
              public void apply(final KEdge it) {
                ModularHypergraphDiagramSynthesis.this._kEdgeExtensions.<EdgeType>addLayoutParam(it, LayoutOptions.EDGE_TYPE, EdgeType.GENERALIZATION);
                KLayoutData _data = it.<KLayoutData>getData(KLayoutData.class);
                KlighdSemanticDiagramData _of = KlighdSemanticDiagramData.of(KlighdConstants.SEMANTIC_DATA_CLASS, "inheritence");
                _data.<KlighdSemanticDiagramData>setProperty(KlighdProperties.SEMANTIC_DATA, _of);
                KNode _node = ModularHypergraphDiagramSynthesis.this._kNodeExtensions.getNode(child);
                it.setSource(_node);
                KNode _node_1 = ModularHypergraphDiagramSynthesis.this._kNodeExtensions.getNode(parent);
                it.setTarget(_node_1);
                it.getData();
                KPolyline _addPolyline = ModularHypergraphDiagramSynthesis.this._kEdgeExtensions.addPolyline(it);
                final Procedure1<KPolyline> _function = new Procedure1<KPolyline>() {
                  public void apply(final KPolyline it) {
                    ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
                    KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("gray25");
                    ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setForeground(it, _color);
                  }
                };
                ObjectExtensions.<KPolyline>operator_doubleArrow(_addPolyline, _function);
              }
            };
            ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge, _function);
          }
        }
      }
    }
  }
  
  public Node findNode(final Edge edge, final EList<Node> nodes, final Node parent) {
    Node result = null;
    String _name = edge.getName();
    System.out.println(_name);
    for (int i = 0; (i < nodes.size()); i++) {
      {
        Node test1 = nodes.get(i);
        Node _get = nodes.get(i);
        boolean _notEquals = (!Objects.equal(_get, parent));
        if (_notEquals) {
          Node _get_1 = nodes.get(i);
          EList<Edge> _edges = _get_1.getEdges();
          int test = _edges.size();
          Node _get_2 = nodes.get(i);
          EList<Edge> _edges_1 = _get_2.getEdges();
          for (final Edge j : _edges_1) {
            {
              String _name_1 = j.getName();
              System.out.println(_name_1);
              String _name_2 = edge.getName();
              String _name_3 = j.getName();
              boolean _equals = _name_2.equals(_name_3);
              if (_equals) {
                Node _get_3 = nodes.get(i);
                result = _get_3;
              }
            }
          }
        }
      }
    }
    return result;
  }
}
