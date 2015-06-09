package de.cau.cs.se.evaluation.architecture.graph;

import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.core.krendering.KAreaPlacementData;
import de.cau.cs.kieler.core.krendering.KColor;
import de.cau.cs.kieler.core.krendering.KEllipse;
import de.cau.cs.kieler.core.krendering.KGridPlacement;
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
import de.cau.cs.kieler.kiml.options.Direction;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import java.util.function.Consumer;
import javax.inject.Inject;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Extension;
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
            String _name = module.getName();
            KText _addText = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, _name);
            KText _associateWith = ModularHypergraphDiagramSynthesis.this.<KText>associateWith(_addText, module);
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
        ObjectExtensions.<KRoundedRectangle>operator_doubleArrow(_addRoundedRectangle, _function);
        EList<Node> _nodes = module.getNodes();
        final Consumer<Node> _function_1 = new Consumer<Node>() {
          public void accept(final Node node) {
            EList<KNode> _children = it.getChildren();
            KNode _createGraphNode = ModularHypergraphDiagramSynthesis.this.createGraphNode(node);
            _children.add(_createGraphNode);
          }
        };
        _nodes.forEach(_function_1);
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
}
