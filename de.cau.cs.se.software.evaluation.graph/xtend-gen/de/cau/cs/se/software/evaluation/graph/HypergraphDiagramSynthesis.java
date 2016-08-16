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
package de.cau.cs.se.software.evaluation.graph;

import com.google.common.collect.ImmutableList;
import de.cau.cs.kieler.klighd.SynthesisOption;
import de.cau.cs.kieler.klighd.krendering.HorizontalAlignment;
import de.cau.cs.kieler.klighd.krendering.KAreaPlacementData;
import de.cau.cs.kieler.klighd.krendering.KColor;
import de.cau.cs.kieler.klighd.krendering.KEllipse;
import de.cau.cs.kieler.klighd.krendering.KPolyline;
import de.cau.cs.kieler.klighd.krendering.KRenderingFactory;
import de.cau.cs.kieler.klighd.krendering.KText;
import de.cau.cs.kieler.klighd.krendering.LineStyle;
import de.cau.cs.kieler.klighd.krendering.VerticalAlignment;
import de.cau.cs.kieler.klighd.krendering.extensions.KColorExtensions;
import de.cau.cs.kieler.klighd.krendering.extensions.KContainerRenderingExtensions;
import de.cau.cs.kieler.klighd.krendering.extensions.KEdgeExtensions;
import de.cau.cs.kieler.klighd.krendering.extensions.KLabelExtensions;
import de.cau.cs.kieler.klighd.krendering.extensions.KNodeExtensions;
import de.cau.cs.kieler.klighd.krendering.extensions.KPolylineExtensions;
import de.cau.cs.kieler.klighd.krendering.extensions.KPortExtensions;
import de.cau.cs.kieler.klighd.krendering.extensions.KRenderingExtensions;
import de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.eclipse.elk.graph.KEdge;
import org.eclipse.elk.graph.KNode;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class HypergraphDiagramSynthesis extends AbstractDiagramSynthesis<Hypergraph> {
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
  
  /**
   * changes in layout direction
   */
  private final static String DIRECTION_NAME = "Layout Direction";
  
  private final static String DIRECTION_UP = "up";
  
  private final static String DIRECTION_DOWN = "down";
  
  private final static String DIRECTION_LEFT = "left";
  
  private final static String DIRECTION_RIGHT = "right";
  
  /**
   * changes in edge routing
   */
  private final static String ROUTING_NAME = "Edge Routing";
  
  private final static String ROUTING_POLYLINE = "polyline";
  
  private final static String ROUTING_ORTHOGONAL = "orthogonal";
  
  private final static String ROUTING_SPLINES = "splines";
  
  private final static String SPACING_NAME = "Spacing";
  
  /**
   * The filter option definition that allows users to customize the constructed class diagrams.
   */
  private final static SynthesisOption DIRECTION = SynthesisOption.createChoiceOption(HypergraphDiagramSynthesis.DIRECTION_NAME, 
    ImmutableList.<String>of(HypergraphDiagramSynthesis.DIRECTION_UP, HypergraphDiagramSynthesis.DIRECTION_DOWN, HypergraphDiagramSynthesis.DIRECTION_LEFT, HypergraphDiagramSynthesis.DIRECTION_RIGHT), HypergraphDiagramSynthesis.DIRECTION_LEFT);
  
  private final static SynthesisOption ROUTING = SynthesisOption.createChoiceOption(HypergraphDiagramSynthesis.ROUTING_NAME, 
    ImmutableList.<String>of(HypergraphDiagramSynthesis.ROUTING_POLYLINE, HypergraphDiagramSynthesis.ROUTING_ORTHOGONAL, HypergraphDiagramSynthesis.ROUTING_SPLINES), HypergraphDiagramSynthesis.ROUTING_POLYLINE);
  
  private final static SynthesisOption SPACING = SynthesisOption.<Float>createRangeOption(HypergraphDiagramSynthesis.SPACING_NAME, Float.valueOf(5f), Float.valueOf(200f), Float.valueOf(50f));
  
  private Map<Node, KNode> nodeMap = new HashMap<Node, KNode>();
  
  private Map<Edge, KNode> edgeMap = new HashMap<Edge, KNode>();
  
  /**
   * {@inheritDoc}<br>
   * <br>
   * Registers the diagram filter option declared above, which allow users to tailor the constructed diagrams.
   */
  @Override
  public List<SynthesisOption> getDisplayedSynthesisOptions() {
    return ImmutableList.<SynthesisOption>of(HypergraphDiagramSynthesis.DIRECTION, HypergraphDiagramSynthesis.ROUTING, HypergraphDiagramSynthesis.SPACING);
  }
  
  @Override
  public KNode transform(final Hypergraph hypergraph) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field LAYOUT_HIERARCHY is undefined for the type Class<LayeredOptions>"
      + "\nThe method or field SPACING_NODE is undefined for the type Class<LayeredOptions>"
      + "\nThe method or field DIRECTION is undefined for the type Class<LayeredOptions>"
      + "\nThe method or field EDGE_ROUTING is undefined for the type Class<LayeredOptions>");
  }
  
  /**
   * Draw a single node as a circle with its name at the center.
   * 
   * @param node the node to be rendered
   */
  private KNode createGraphNode(final Node node) {
    KNode _createNode = this._kNodeExtensions.createNode(node);
    final KNode kNode = this.<KNode>associateWith(_createNode, node);
    this.nodeMap.put(node, kNode);
    final Procedure1<KNode> _function = (KNode it) -> {
      KEllipse _addEllipse = this._kRenderingExtensions.addEllipse(it);
      final Procedure1<KEllipse> _function_1 = (KEllipse it_1) -> {
        this._kRenderingExtensions.setLineWidth(it_1, 2);
        KColor _color = this._kColorExtensions.getColor("white");
        this._kRenderingExtensions.setBackground(it_1, _color);
        this._kRenderingExtensions.<KEllipse>setSurroundingSpace(it_1, 0, 0, 0, 0);
        String _name = node.getName();
        KText _addText = this._kContainerRenderingExtensions.addText(it_1, _name);
        final Procedure1<KText> _function_2 = (KText it_2) -> {
          this._kRenderingExtensions.setHorizontalAlignment(it_2, HorizontalAlignment.CENTER);
          this._kRenderingExtensions.setVerticalAlignment(it_2, VerticalAlignment.CENTER);
          KAreaPlacementData _setAreaPlacementData = this._kRenderingExtensions.setAreaPlacementData(it_2);
          KAreaPlacementData _from = this._kRenderingExtensions.from(_setAreaPlacementData, this._kRenderingExtensions.LEFT, 5, 0, this._kRenderingExtensions.TOP, 5, 0);
          this._kRenderingExtensions.to(_from, this._kRenderingExtensions.RIGHT, 5, 0, this._kRenderingExtensions.BOTTOM, 5, 0);
          this._kRenderingExtensions.<KText>setSurroundingSpace(it_2, 20, 0, 30, 0);
          it_2.setCursorSelectable(false);
        };
        ObjectExtensions.<KText>operator_doubleArrow(_addText, _function_2);
      };
      ObjectExtensions.<KEllipse>operator_doubleArrow(_addEllipse, _function_1);
    };
    ObjectExtensions.<KNode>operator_doubleArrow(kNode, _function);
    return kNode;
  }
  
  private KNode createGraphEdge(final Edge edge) {
    KNode _createNode = this._kNodeExtensions.createNode(edge);
    final KNode kNode = this.<KNode>associateWith(_createNode, edge);
    this.edgeMap.put(edge, kNode);
    final Procedure1<KNode> _function = (KNode it) -> {
      KEllipse _addEllipse = this._kRenderingExtensions.addEllipse(it);
      final Procedure1<KEllipse> _function_1 = (KEllipse it_1) -> {
        this._kRenderingExtensions.setLineWidth(it_1, 2);
        KColor _color = this._kColorExtensions.getColor("black");
        this._kRenderingExtensions.setBackground(it_1, _color);
        this._kRenderingExtensions.<KEllipse>setSurroundingSpace(it_1, 0, 0, 0, 0);
      };
      ObjectExtensions.<KEllipse>operator_doubleArrow(_addEllipse, _function_1);
    };
    ObjectExtensions.<KNode>operator_doubleArrow(kNode, _function);
    return kNode;
  }
  
  private KEdge drawEdge(final KNode left, final KNode right) {
    KEdge _createEdge = this._kEdgeExtensions.createEdge();
    final Procedure1<KEdge> _function = (KEdge it) -> {
      it.setSource(left);
      it.setTarget(right);
      KPolyline _addPolyline = this._kEdgeExtensions.addPolyline(it);
      final Procedure1<KPolyline> _function_1 = (KPolyline it_1) -> {
        this._kRenderingExtensions.setLineWidth(it_1, 2);
        this._kRenderingExtensions.setLineStyle(it_1, LineStyle.SOLID);
      };
      ObjectExtensions.<KPolyline>operator_doubleArrow(_addPolyline, _function_1);
    };
    return ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge, _function);
  }
}
