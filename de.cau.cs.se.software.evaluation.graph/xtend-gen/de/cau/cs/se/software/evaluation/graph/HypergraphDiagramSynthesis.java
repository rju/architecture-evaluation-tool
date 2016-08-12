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
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import java.util.Map;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class HypergraphDiagramSynthesis /* implements AbstractDiagramSynthesis<Hypergraph>  */{
  @Inject
  @Extension
  private /* KNodeExtensions */Object _kNodeExtensions;
  
  @Inject
  @Extension
  private /* KEdgeExtensions */Object _kEdgeExtensions;
  
  @Inject
  @Extension
  private /* KPortExtensions */Object _kPortExtensions;
  
  @Inject
  @Extension
  private /* KLabelExtensions */Object _kLabelExtensions;
  
  @Inject
  @Extension
  private /* KRenderingExtensions */Object _kRenderingExtensions;
  
  @Inject
  @Extension
  private /* KContainerRenderingExtensions */Object _kContainerRenderingExtensions;
  
  @Inject
  @Extension
  private /* KPolylineExtensions */Object _kPolylineExtensions;
  
  @Inject
  @Extension
  private /* KColorExtensions */Object _kColorExtensions;
  
  @Extension
  private /* KRenderingFactory */Object _kRenderingFactory /* Skipped initializer because of errors */;
  
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
  private final static /* SynthesisOption */Object DIRECTION /* Skipped initializer because of errors */;
  
  private final static /* SynthesisOption */Object ROUTING /* Skipped initializer because of errors */;
  
  private final static /* SynthesisOption */Object SPACING /* Skipped initializer because of errors */;
  
  private /* Map<Node, KNode> */Object nodeMap /* Skipped initializer because of errors */;
  
  private /* Map<Edge, KNode> */Object edgeMap /* Skipped initializer because of errors */;
  
  /**
   * {@inheritDoc}<br>
   * <br>
   * Registers the diagram filter option declared above, which allow users to tailor the constructed diagrams.
   */
  @Override
  public /* ImmutableList<SynthesisOption> */Object getDisplayedSynthesisOptions() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe field HypergraphDiagramSynthesis.DIRECTION refers to the missing type SynthesisOption"
      + "\nThe field HypergraphDiagramSynthesis.ROUTING refers to the missing type SynthesisOption"
      + "\nThe field HypergraphDiagramSynthesis.SPACING refers to the missing type SynthesisOption");
  }
  
  @Override
  public /* KNode */Object transform(final Hypergraph hypergraph) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createNode() is undefined for the type Hypergraph"
      + "\nThe method setLayoutOption(Object, boolean) is undefined for the type Object"
      + "\nThe method or field LayeredOptions is undefined"
      + "\nThe method setLayoutOption(Object, Float) is undefined for the type Object"
      + "\nLayeredOptions cannot be resolved to a type."
      + "\nThe method setLayoutOption(Object, Object) is undefined for the type Object"
      + "\nLayeredOptions cannot be resolved to a type."
      + "\nDirection cannot be resolved to a type."
      + "\nDirection cannot be resolved to a type."
      + "\nDirection cannot be resolved to a type."
      + "\nDirection cannot be resolved to a type."
      + "\nThe method setLayoutOption(Object, Object) is undefined for the type Object"
      + "\nLayeredOptions cannot be resolved to a type."
      + "\nEdgeRouting cannot be resolved to a type."
      + "\nEdgeRouting cannot be resolved to a type."
      + "\nEdgeRouting cannot be resolved to a type."
      + "\nThe field HypergraphDiagramSynthesis.SPACING refers to the missing type SynthesisOption"
      + "\nThe field HypergraphDiagramSynthesis.DIRECTION refers to the missing type SynthesisOption"
      + "\nThe field HypergraphDiagramSynthesis.ROUTING refers to the missing type SynthesisOption"
      + "\nThe method createGraphNode(Node) from the type HypergraphDiagramSynthesis refers to the missing type Object"
      + "\nThe method createGraphEdge(Edge) from the type HypergraphDiagramSynthesis refers to the missing type Object"
      + "\nThe method drawEdge(KNode, KNode) from the type HypergraphDiagramSynthesis refers to the missing type Object"
      + "\nThe field HypergraphDiagramSynthesis.edgeMap refers to the missing type KNode"
      + "\nThe field HypergraphDiagramSynthesis.nodeMap refers to the missing type KNode"
      + "\nassociateWith cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nLAYOUT_HIERARCHY cannot be resolved"
      + "\nSPACING_NODE cannot be resolved"
      + "\nobjectValue cannot be resolved"
      + "\nDIRECTION cannot be resolved"
      + "\nobjectValue cannot be resolved"
      + "\nUP cannot be resolved"
      + "\nDOWN cannot be resolved"
      + "\nLEFT cannot be resolved"
      + "\nRIGHT cannot be resolved"
      + "\nEDGE_ROUTING cannot be resolved"
      + "\nobjectValue cannot be resolved"
      + "\nPOLYLINE cannot be resolved"
      + "\nORTHOGONAL cannot be resolved"
      + "\nSPLINES cannot be resolved"
      + "\nchildren cannot be resolved"
      + "\n+= cannot be resolved"
      + "\nchildren cannot be resolved"
      + "\n+= cannot be resolved");
  }
  
  /**
   * Draw a single node as a circle with its name at the center.
   * 
   * @param node the node to be rendered
   */
  private Object createGraphNode(final Node node) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createNode() is undefined for the type Node"
      + "\nThe method or field addEllipse is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method background(Object) is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method setSurroundingSpace(int, int, int, int) is undefined for the type Object"
      + "\nThe method addText(String) is undefined for the type Object"
      + "\nThe method horizontalAlignment(Object) is undefined for the type Object"
      + "\nThe method or field HorizontalAlignment is undefined"
      + "\nThe method verticalAlignment(Object) is undefined for the type Object"
      + "\nThe method or field VerticalAlignment is undefined"
      + "\nThe method or field setAreaPlacementData is undefined for the type Object"
      + "\nThe method or field LEFT is undefined"
      + "\nThe method or field TOP is undefined"
      + "\nThe method or field RIGHT is undefined"
      + "\nThe method or field BOTTOM is undefined"
      + "\nThe method setSurroundingSpace(int, int, int, int) is undefined for the type Object"
      + "\nThe method cursorSelectable(boolean) is undefined for the type Object"
      + "\nThe field HypergraphDiagramSynthesis.nodeMap refers to the missing type KNode"
      + "\nassociateWith cannot be resolved"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nCENTER cannot be resolved"
      + "\nCENTER cannot be resolved"
      + "\nfrom cannot be resolved"
      + "\nto cannot be resolved");
  }
  
  private Object createGraphEdge(final Edge edge) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createNode() is undefined for the type Edge"
      + "\nThe method or field addEllipse is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method background(Object) is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method setSurroundingSpace(int, int, int, int) is undefined for the type Object"
      + "\nThe field HypergraphDiagramSynthesis.edgeMap refers to the missing type KNode"
      + "\nassociateWith cannot be resolved"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved");
  }
  
  private Object drawEdge(final /* KNode */Object left, final /* KNode */Object right) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createEdge() is undefined"
      + "\nThe method source(KNode) is undefined for the type Object"
      + "\nThe method target(KNode) is undefined for the type Object"
      + "\nThe method or field addPolyline is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method lineStyle(Object) is undefined for the type Object"
      + "\nThe method or field LineStyle is undefined"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nSOLID cannot be resolved");
  }
}
