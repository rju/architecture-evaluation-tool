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
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Extension;

@SuppressWarnings("all")
public class ModularHypergraphDiagramSynthesis /* implements AbstractDiagramSynthesis<ModularHypergraph>  */{
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
   * changes in visualization nodes on off
   */
  private final static String VISIBLE_NODES_NAME = "Nodes Visible";
  
  private final static String VISIBLE_NODES_NO = "Modules only";
  
  private final static String VISIBLE_NODES_YES = "Show nodes in modules";
  
  /**
   * changes in visualization anonymous classes on off
   */
  private final static String VISIBLE_ANONYMOUS_NAME = "Anonymous Classes";
  
  private final static String VISIBLE_ANONYMOUS_NO = "hidden";
  
  private final static String VISIBLE_ANONYMOUS_YES = "visible";
  
  /**
   * changes in visualization anonymous classes on off
   */
  private final static String VISIBLE_FRAMEWORK_NAME = "Framework Classes";
  
  private final static String VISIBLE_FRAMEWORK_NO = "hidden";
  
  private final static String VISIBLE_FRAMEWORK_YES = "visible";
  
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
  private final static /* SynthesisOption */Object VISIBLE_NODES /* Skipped initializer because of errors */;
  
  private final static /* SynthesisOption */Object VISIBLE_ANONYMOUS /* Skipped initializer because of errors */;
  
  private final static /* SynthesisOption */Object VISIBLE_FRAMEWORK /* Skipped initializer because of errors */;
  
  private final static /* SynthesisOption */Object DIRECTION /* Skipped initializer because of errors */;
  
  private final static /* SynthesisOption */Object ROUTING /* Skipped initializer because of errors */;
  
  private final static /* SynthesisOption */Object SPACING /* Skipped initializer because of errors */;
  
  private /* Map<Node, KNode> */Object nodeMap /* Skipped initializer because of errors */;
  
  private /* Map<Module, KNode> */Object moduleMap /* Skipped initializer because of errors */;
  
  private /* Map<String, KPort> */Object portMap /* Skipped initializer because of errors */;
  
  private ArrayList<Module> processedModules = new ArrayList<Module>();
  
  private /* Map<PlanarNode, KNode> */Object planarNodeMap /* Skipped initializer because of errors */;
  
  /**
   * {@inheritDoc}<br>
   * <br>
   * Registers the diagram filter option declared above, which allow users to tailor the constructed diagrams.
   */
  @Override
  public /* ImmutableList<SynthesisOption> */Object getDisplayedSynthesisOptions() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe field ModularHypergraphDiagramSynthesis.VISIBLE_NODES refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.VISIBLE_FRAMEWORK refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.DIRECTION refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.ROUTING refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.SPACING refers to the missing type SynthesisOption");
  }
  
  @Override
  public /* KNode */Object transform(final ModularHypergraph hypergraph) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createNode() is undefined for the type ModularHypergraph"
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
      + "\nThe field ModularHypergraphDiagramSynthesis.SPACING refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.DIRECTION refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.ROUTING refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.VISIBLE_NODES refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.VISIBLE_FRAMEWORK refers to the missing type SynthesisOption"
      + "\nThe field ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS refers to the missing type SynthesisOption"
      + "\nThe method createEmptyModule(PlanarNode) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe method createAggregatedModuleEdge(PlanarEdge, List<PlanarNode>) from the type ModularHypergraphDiagramSynthesis refers to the missing type Object"
      + "\nThe method createModuleWithNodes(Module) from the type ModularHypergraphDiagramSynthesis refers to the missing type Object"
      + "\nThe method createGraphEdge(Edge, EList<Node>, EList<KNode>) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
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
      + "\nobjectValue cannot be resolved"
      + "\n== cannot be resolved"
      + "\nobjectValue cannot be resolved"
      + "\n== cannot be resolved"
      + "\nobjectValue cannot be resolved"
      + "\n== cannot be resolved"
      + "\nchildren cannot be resolved"
      + "\n+= cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nchildren cannot be resolved"
      + "\n+= cannot be resolved"
      + "\nchildren cannot be resolved");
  }
  
  /**
   * Return the correct color for a module.
   */
  private Object getBackgroundColor(final EModuleKind kind) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method or field color is undefined for the type String");
  }
  
  /**
   * Create an edge in the correct width for an aggregated edge.
   */
  private Object createAggregatedModuleEdge(final PlanarEdge edge, final List<PlanarNode> nodes) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createPort() is undefined"
      + "\nThe method setPortSize(int, int) is undefined for the type Object"
      + "\nThe method or field addRectangle is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method or field LineJoin is undefined"
      + "\nThe method createPort() is undefined"
      + "\nThe method setPortSize(int, int) is undefined for the type Object"
      + "\nThe method or field addRectangle is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method or field LineJoin is undefined"
      + "\nThe method createEdge() is undefined"
      + "\nThe method source(KNode) is undefined for the type Object"
      + "\nThe method sourcePort(Object) is undefined for the type Object"
      + "\nThe method target(KNode) is undefined for the type Object"
      + "\nThe method targetPort(Object) is undefined for the type Object"
      + "\nThe method or field addPolyline is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method lineStyle(Object) is undefined for the type Object"
      + "\nThe method or field LineStyle is undefined"
      + "\nThe field ModularHypergraphDiagramSynthesis.planarNodeMap refers to the missing type KNode"
      + "\nThe field ModularHypergraphDiagramSynthesis.planarNodeMap refers to the missing type KNode"
      + "\n=> cannot be resolved"
      + "\nsetBackground cannot be resolved"
      + "\nlineJoin cannot be resolved"
      + "\nJOIN_ROUND cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nsetBackground cannot be resolved"
      + "\nlineJoin cannot be resolved"
      + "\nJOIN_ROUND cannot be resolved"
      + "\nports cannot be resolved"
      + "\nadd cannot be resolved"
      + "\nports cannot be resolved"
      + "\nadd cannot be resolved"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nSOLID cannot be resolved");
  }
  
  /**
   * Create module without nodes for simple display.
   */
  private /* KNode */Object createEmptyModule(final PlanarNode planarNode) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createNode() is undefined for the type PlanarNode"
      + "\nThe method setLayoutOption(Object, float) is undefined for the type Object"
      + "\nLayeredOptions cannot be resolved to a type."
      + "\nThe method addRoundedRectangle(int, int) is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method setBackgroundGradient(Object, Object, int) is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method shadow(Object) is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method setGridPlacement(int) is undefined for the type Object"
      + "\nThe method or field LEFT is undefined"
      + "\nThe method or field TOP is undefined"
      + "\nThe method or field RIGHT is undefined"
      + "\nThe method or field BOTTOM is undefined"
      + "\nThe method addText(String) is undefined for the type Object"
      + "\nThe method fontBold(boolean) is undefined for the type Object"
      + "\nThe method cursorSelectable(boolean) is undefined for the type Object"
      + "\nThe method setLeftTopAlignedPointPlacementData(int, int, int, int) is undefined for the type Object"
      + "\nThe method addText(String) is undefined for the type Object"
      + "\nThe method fontBold(boolean) is undefined for the type Object"
      + "\nThe method cursorSelectable(boolean) is undefined for the type Object"
      + "\nThe method setLeftTopAlignedPointPlacementData(int, int, int, int) is undefined for the type Object"
      + "\nThe field ModularHypergraphDiagramSynthesis.planarNodeMap refers to the missing type KNode"
      + "\nThe method getBackgroundColor(EModuleKind) from the type ModularHypergraphDiagramSynthesis refers to the missing type Object"
      + "\n=> cannot be resolved"
      + "\nPORT_BORDER_OFFSET cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nfrom cannot be resolved"
      + "\nto cannot be resolved"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved");
  }
  
  /**
   * Draw module as a rectangle with its nodes inside.
   * 
   * @param module the module to be rendered
   */
  private Object createModuleWithNodes(final Module module) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createNode() is undefined for the type Module"
      + "\nThe method or field KShapeLayout is undefined"
      + "\nThe method setLayoutOption(Object, Object) is undefined for the type Object"
      + "\nThe method or field LayeredOptions is undefined"
      + "\nThe method or field PortConstraints is undefined"
      + "\nThe method setLayoutOption(Object, Object) is undefined for the type Object"
      + "\nLayeredOptions cannot be resolved to a type."
      + "\nEdgeRouting cannot be resolved to a type."
      + "\nThe method addRoundedRectangle(int, int) is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method setBackgroundGradient(Object, Object, int) is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method shadow(Object) is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method setGridPlacement(int) is undefined for the type Object"
      + "\nThe method or field LEFT is undefined"
      + "\nThe method or field TOP is undefined"
      + "\nThe method or field RIGHT is undefined"
      + "\nThe method or field BOTTOM is undefined"
      + "\nThe method addText(String) is undefined for the type Object"
      + "\nThe method fontBold(boolean) is undefined for the type Object"
      + "\nThe method cursorSelectable(boolean) is undefined for the type Object"
      + "\nThe method setLeftTopAlignedPointPlacementData(int, int, int, int) is undefined for the type Object"
      + "\nThe method addChildArea() is undefined for the type Object"
      + "\nThe method getBackgroundColor(EModuleKind) from the type ModularHypergraphDiagramSynthesis refers to the missing type Object"
      + "\nThe method createGraphNode(Node, KContainerRendering, Module, KNode) from the type ModularHypergraphDiagramSynthesis refers to the missing type Object"
      + "\nassociateWith cannot be resolved"
      + "\ngetData cannot be resolved"
      + "\ninsets cannot be resolved"
      + "\ntop cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nPORT_CONSTRAINTS cannot be resolved"
      + "\nFREE cannot be resolved"
      + "\nEDGE_ROUTING cannot be resolved"
      + "\nPOLYLINE cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nfrom cannot be resolved"
      + "\nto cannot be resolved"
      + "\n=> cannot be resolved");
  }
  
  /**
   * Draw a single node as a circle with its name at the center.
   * 
   * @param node the node to be rendered
   */
  private Object createGraphNode(final Node node, final /* KContainerRendering */Object parent, final Module module, final /* KNode */Object moduleNode) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createNode() is undefined for the type Node"
      + "\nThe method setLayoutOption(Object, Object) is undefined for the type Object"
      + "\nThe method or field LayeredOptions is undefined"
      + "\nThe method or field PortConstraints is undefined"
      + "\nThe method addRoundedRectangle(int, int) is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method background(Object) is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method setSurroundingSpace(int, int, int, int) is undefined for the type Object"
      + "\nThe method addText(String) is undefined for the type Object"
      + "\nThe method setSurroundingSpace(int, int, int, int) is undefined for the type Object"
      + "\nThe method cursorSelectable(boolean) is undefined for the type Object"
      + "\nThe field ModularHypergraphDiagramSynthesis.nodeMap refers to the missing type KNode"
      + "\nassociateWith cannot be resolved"
      + "\nchildren cannot be resolved"
      + "\n+= cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nPORT_CONSTRAINTS cannot be resolved"
      + "\nFREE cannot be resolved"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved");
  }
  
  private /* KPort */Object getOrCreateEdgePort(final /* KNode */Object kNode, final String label) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createPort() is undefined"
      + "\nThe method setPortSize(int, int) is undefined for the type Object"
      + "\nThe method or field addRectangle is undefined for the type Object"
      + "\nThe method or field color is undefined for the type String"
      + "\nThe method or field LineJoin is undefined"
      + "\nThe field ModularHypergraphDiagramSynthesis.portMap refers to the missing type KPort"
      + "\nThe field ModularHypergraphDiagramSynthesis.portMap refers to the missing type KPort"
      + "\nThe field ModularHypergraphDiagramSynthesis.portMap refers to the missing type KPort"
      + "\n== cannot be resolved"
      + "\nports cannot be resolved"
      + "\nadd cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nsetBackground cannot be resolved"
      + "\nlineJoin cannot be resolved"
      + "\nJOIN_ROUND cannot be resolved");
  }
  
  private Boolean createGraphEdge(final Edge edge, final EList<Node> nodes, final /* EList<KNode> */Object siblings) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method constructEdge(KNode, KNode, Edge) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe field ModularHypergraphDiagramSynthesis.nodeMap refers to the missing type KNode"
      + "\nThe field ModularHypergraphDiagramSynthesis.nodeMap refers to the missing type KNode"
      + "\nThe method drawHyperEdge(Edge, Iterable<Node>) from the type ModularHypergraphDiagramSynthesis refers to the missing type Object");
  }
  
  private Object drawHyperEdge(final Edge graphEdge, final Iterable<Node> nodes) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method or field createNode is undefined for the type Edge"
      + "\nThe method addEllipse() is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method constructEdge(KNode, KNode, Edge) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe field ModularHypergraphDiagramSynthesis.nodeMap refers to the missing type KNode"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved");
  }
  
  private void constructEdge(final /* KNode */Object left, final /* KNode */Object right, final Edge graphEdge) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method drawEdge(KNode, KNode, KPort, KPort) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe field ModularHypergraphDiagramSynthesis.portMap refers to the missing type KPort"
      + "\nThe method drawEdge(KNode, KNode, KPort, KPort) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe field ModularHypergraphDiagramSynthesis.portMap refers to the missing type KPort"
      + "\nThe method drawEdge(KNode, KNode, KPort, KPort) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe field ModularHypergraphDiagramSynthesis.portMap refers to the missing type KPort"
      + "\nThe method drawEdge(KNode, KNode, KPort, KPort) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe method drawEdge(KNode, KNode, KPort, KPort) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe method drawEdge(KNode, KNode, KPort, KPort) from the type ModularHypergraphDiagramSynthesis refers to the missing type KNode"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nThe method getOrCreateEdgePort(KNode, String) from the type ModularHypergraphDiagramSynthesis refers to the missing type KPort"
      + "\nparent cannot be resolved"
      + "\nequals cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\n!= cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n== cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n== cannot be resolved"
      + "\nparent cannot be resolved"
      + "\nparent cannot be resolved"
      + "\nparent cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n== cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\nparent cannot be resolved"
      + "\nparent cannot be resolved"
      + "\ntoString cannot be resolved"
      + "\n+ cannot be resolved"
      + "\n+ cannot be resolved"
      + "\ntoString cannot be resolved");
  }
  
  /**
   * Draw a single edge.
   */
  private void drawEdge(final /* KNode */Object left, final /* KNode */Object right, final /* KPort */Object leftPort, final /* KPort */Object rightPort) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createEdge() is undefined"
      + "\nThe method source(KNode) is undefined for the type Object"
      + "\nThe method target(KNode) is undefined for the type Object"
      + "\nThe method sourcePort(KPort) is undefined for the type Object"
      + "\nThe method targetPort(KPort) is undefined for the type Object"
      + "\nThe method or field addPolyline is undefined for the type Object"
      + "\nThe method lineWidth(int) is undefined for the type Object"
      + "\nThe method lineStyle(Object) is undefined for the type Object"
      + "\nThe method or field LineStyle is undefined"
      + "\n=> cannot be resolved"
      + "\n=> cannot be resolved"
      + "\nSOLID cannot be resolved");
  }
}
