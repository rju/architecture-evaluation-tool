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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import de.cau.cs.kieler.core.kgraph.KEdge;
import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.core.kgraph.KPort;
import de.cau.cs.kieler.core.krendering.KColor;
import de.cau.cs.kieler.core.krendering.KContainerRendering;
import de.cau.cs.kieler.core.krendering.KEllipse;
import de.cau.cs.kieler.core.krendering.KGridPlacement;
import de.cau.cs.kieler.core.krendering.KPolyline;
import de.cau.cs.kieler.core.krendering.KRectangle;
import de.cau.cs.kieler.core.krendering.KRenderingFactory;
import de.cau.cs.kieler.core.krendering.KRoundedRectangle;
import de.cau.cs.kieler.core.krendering.KText;
import de.cau.cs.kieler.core.krendering.LineJoin;
import de.cau.cs.kieler.core.krendering.LineStyle;
import de.cau.cs.kieler.core.krendering.extensions.KColorExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KContainerRenderingExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KEdgeExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KLabelExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KNodeExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KPolylineExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KPortExtensions;
import de.cau.cs.kieler.core.krendering.extensions.KRenderingExtensions;
import de.cau.cs.kieler.kiml.klayoutdata.KInsets;
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout;
import de.cau.cs.kieler.kiml.options.Direction;
import de.cau.cs.kieler.kiml.options.EdgeRouting;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.kiml.options.SizeConstraint;
import de.cau.cs.kieler.klay.layered.properties.Properties;
import de.cau.cs.kieler.klighd.SynthesisOption;
import de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
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
  private final static SynthesisOption VISIBLE_NODES = SynthesisOption.createChoiceOption(ModularHypergraphDiagramSynthesis.VISIBLE_NODES_NAME, 
    ImmutableList.<String>of(ModularHypergraphDiagramSynthesis.VISIBLE_NODES_YES, ModularHypergraphDiagramSynthesis.VISIBLE_NODES_NO), ModularHypergraphDiagramSynthesis.VISIBLE_NODES_NO);
  
  private final static SynthesisOption VISIBLE_ANONYMOUS = SynthesisOption.createChoiceOption(ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS_NAME, 
    ImmutableList.<String>of(ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS_YES, ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS_NO), ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS_NO);
  
  private final static SynthesisOption DIRECTION = SynthesisOption.createChoiceOption(ModularHypergraphDiagramSynthesis.DIRECTION_NAME, 
    ImmutableList.<String>of(ModularHypergraphDiagramSynthesis.DIRECTION_UP, ModularHypergraphDiagramSynthesis.DIRECTION_DOWN, ModularHypergraphDiagramSynthesis.DIRECTION_LEFT, ModularHypergraphDiagramSynthesis.DIRECTION_RIGHT), ModularHypergraphDiagramSynthesis.DIRECTION_LEFT);
  
  private final static SynthesisOption ROUTING = SynthesisOption.createChoiceOption(ModularHypergraphDiagramSynthesis.ROUTING_NAME, 
    ImmutableList.<String>of(ModularHypergraphDiagramSynthesis.ROUTING_POLYLINE, ModularHypergraphDiagramSynthesis.ROUTING_ORTHOGONAL, ModularHypergraphDiagramSynthesis.ROUTING_SPLINES), ModularHypergraphDiagramSynthesis.ROUTING_ORTHOGONAL);
  
  private final static SynthesisOption SPACING = SynthesisOption.<Float>createRangeOption(ModularHypergraphDiagramSynthesis.SPACING_NAME, Float.valueOf(5f), Float.valueOf(200f), Float.valueOf(50f));
  
  private Map<Node, KNode> nodeMap = new HashMap<Node, KNode>();
  
  private Map<Module, KNode> moduleMap = new HashMap<Module, KNode>();
  
  private Map<String, KPort> portMap = new HashMap<String, KPort>();
  
  private ArrayList<Module> processedModules = new ArrayList<Module>();
  
  /**
   * {@inheritDoc}<br>
   * <br>
   * Registers the diagram filter option declared above, which allow users to tailor the constructed diagrams.
   */
  public List<SynthesisOption> getDisplayedSynthesisOptions() {
    return ImmutableList.<SynthesisOption>of(ModularHypergraphDiagramSynthesis.VISIBLE_NODES, ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS, ModularHypergraphDiagramSynthesis.DIRECTION, ModularHypergraphDiagramSynthesis.ROUTING, ModularHypergraphDiagramSynthesis.SPACING);
  }
  
  public KNode transform(final ModularHypergraph hypergraph) {
    KNode _createNode = this._kNodeExtensions.createNode(hypergraph);
    final KNode root = this.<KNode>associateWith(_createNode, hypergraph);
    final Procedure1<KNode> _function = new Procedure1<KNode>() {
      public void apply(final KNode it) {
        ModularHypergraphDiagramSynthesis.this.<KNode, Boolean>setLayoutOption(it, Properties.MERGE_EDGES, Boolean.valueOf(true));
        ModularHypergraphDiagramSynthesis.this.<KNode, Boolean>setLayoutOption(it, LayoutOptions.LAYOUT_HIERARCHY, Boolean.valueOf(true));
        ModularHypergraphDiagramSynthesis.this.<KNode, String>setLayoutOption(it, LayoutOptions.ALGORITHM, "de.cau.cs.kieler.klay.layered");
        Object _objectValue = ModularHypergraphDiagramSynthesis.this.getObjectValue(ModularHypergraphDiagramSynthesis.SPACING);
        ModularHypergraphDiagramSynthesis.this.<KNode, Float>setLayoutOption(it, LayoutOptions.SPACING, ((Float) _objectValue));
        Direction _switchResult = null;
        Object _objectValue_1 = ModularHypergraphDiagramSynthesis.this.getObjectValue(ModularHypergraphDiagramSynthesis.DIRECTION);
        boolean _matched = false;
        if (!_matched) {
          if (Objects.equal(_objectValue_1, ModularHypergraphDiagramSynthesis.DIRECTION_UP)) {
            _matched=true;
            _switchResult = Direction.UP;
          }
        }
        if (!_matched) {
          if (Objects.equal(_objectValue_1, ModularHypergraphDiagramSynthesis.DIRECTION_DOWN)) {
            _matched=true;
            _switchResult = Direction.DOWN;
          }
        }
        if (!_matched) {
          if (Objects.equal(_objectValue_1, ModularHypergraphDiagramSynthesis.DIRECTION_LEFT)) {
            _matched=true;
            _switchResult = Direction.LEFT;
          }
        }
        if (!_matched) {
          if (Objects.equal(_objectValue_1, ModularHypergraphDiagramSynthesis.DIRECTION_RIGHT)) {
            _matched=true;
            _switchResult = Direction.RIGHT;
          }
        }
        ModularHypergraphDiagramSynthesis.this.<KNode, Direction>setLayoutOption(it, LayoutOptions.DIRECTION, _switchResult);
        EdgeRouting _switchResult_1 = null;
        Object _objectValue_2 = ModularHypergraphDiagramSynthesis.this.getObjectValue(ModularHypergraphDiagramSynthesis.ROUTING);
        boolean _matched_1 = false;
        if (!_matched_1) {
          if (Objects.equal(_objectValue_2, ModularHypergraphDiagramSynthesis.ROUTING_POLYLINE)) {
            _matched_1=true;
            _switchResult_1 = EdgeRouting.POLYLINE;
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_objectValue_2, ModularHypergraphDiagramSynthesis.ROUTING_ORTHOGONAL)) {
            _matched_1=true;
            _switchResult_1 = EdgeRouting.ORTHOGONAL;
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_objectValue_2, ModularHypergraphDiagramSynthesis.ROUTING_SPLINES)) {
            _matched_1=true;
            _switchResult_1 = EdgeRouting.SPLINES;
          }
        }
        ModularHypergraphDiagramSynthesis.this.<KNode, EdgeRouting>setLayoutOption(it, LayoutOptions.EDGE_ROUTING, _switchResult_1);
        Object _objectValue_3 = ModularHypergraphDiagramSynthesis.this.getObjectValue(ModularHypergraphDiagramSynthesis.VISIBLE_NODES);
        boolean _equals = Objects.equal(_objectValue_3, ModularHypergraphDiagramSynthesis.VISIBLE_NODES_NO);
        if (_equals) {
          EList<Module> _modules = hypergraph.getModules();
          final Consumer<Module> _function = new Consumer<Module>() {
            public void accept(final Module module) {
              boolean _and = false;
              EModuleKind _kind = module.getKind();
              boolean _equals = Objects.equal(_kind, EModuleKind.ANONYMOUS);
              if (!_equals) {
                _and = false;
              } else {
                Object _objectValue = ModularHypergraphDiagramSynthesis.this.getObjectValue(ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS);
                boolean _equals_1 = Objects.equal(_objectValue, ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS_NO);
                _and = _equals_1;
              }
              boolean _not = (!_and);
              if (_not) {
                EList<KNode> _children = root.getChildren();
                KNode _createEmptyModule = ModularHypergraphDiagramSynthesis.this.createEmptyModule(module);
                _children.add(_createEmptyModule);
              }
            }
          };
          _modules.forEach(_function);
          EList<Module> _modules_1 = hypergraph.getModules();
          final Consumer<Module> _function_1 = new Consumer<Module>() {
            public void accept(final Module module) {
              boolean _and = false;
              EModuleKind _kind = module.getKind();
              boolean _equals = Objects.equal(_kind, EModuleKind.ANONYMOUS);
              if (!_equals) {
                _and = false;
              } else {
                Object _objectValue = ModularHypergraphDiagramSynthesis.this.getObjectValue(ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS);
                boolean _equals_1 = Objects.equal(_objectValue, ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS_NO);
                _and = _equals_1;
              }
              boolean _not = (!_and);
              if (_not) {
                ModularHypergraphDiagramSynthesis.this.createCombindEdges(module, hypergraph);
              }
            }
          };
          _modules_1.forEach(_function_1);
        } else {
          EList<Module> _modules_2 = hypergraph.getModules();
          final Consumer<Module> _function_2 = new Consumer<Module>() {
            public void accept(final Module module) {
              EList<KNode> _children = root.getChildren();
              KNode _createModuleWithNodes = ModularHypergraphDiagramSynthesis.this.createModuleWithNodes(module);
              _children.add(_createModuleWithNodes);
            }
          };
          _modules_2.forEach(_function_2);
          EList<Edge> _edges = hypergraph.getEdges();
          final Consumer<Edge> _function_3 = new Consumer<Edge>() {
            public void accept(final Edge edge) {
              EList<Node> _nodes = hypergraph.getNodes();
              EList<KNode> _children = root.getChildren();
              ModularHypergraphDiagramSynthesis.this.createGraphEdge(edge, _nodes, _children);
            }
          };
          _edges.forEach(_function_3);
        }
      }
    };
    ObjectExtensions.<KNode>operator_doubleArrow(root, _function);
    return root;
  }
  
  private void createCombindEdges(final Module sourceModule, final ModularHypergraph hypergraph) {
    final ArrayList<Edge> edges = new ArrayList<Edge>();
    EList<Node> _nodes = sourceModule.getNodes();
    final Consumer<Node> _function = new Consumer<Node>() {
      public void accept(final Node node) {
        EList<Edge> _edges = node.getEdges();
        final Consumer<Edge> _function = new Consumer<Edge>() {
          public void accept(final Edge it) {
            ModularHypergraphDiagramSynthesis.this.addUnique(edges, it);
          }
        };
        _edges.forEach(_function);
      }
    };
    _nodes.forEach(_function);
    this.processedModules.add(sourceModule);
    final HashMap<Module, Integer> targetModules = new HashMap<Module, Integer>();
    final Consumer<Edge> _function_1 = new Consumer<Edge>() {
      public void accept(final Edge edge) {
        EList<Node> _nodes = hypergraph.getNodes();
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
        Iterable<Node> _filter = IterableExtensions.<Node>filter(_nodes, _function);
        final Consumer<Node> _function_1 = new Consumer<Node>() {
          public void accept(final Node node) {
            EList<Node> _nodes = sourceModule.getNodes();
            final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
              public Boolean apply(final Node it) {
                return Boolean.valueOf(it.equals(node));
              }
            };
            boolean _exists = IterableExtensions.<Node>exists(_nodes, _function);
            boolean _not = (!_exists);
            if (_not) {
              EList<Module> _modules = hypergraph.getModules();
              final Function1<Module, Boolean> _function_1 = new Function1<Module, Boolean>() {
                public Boolean apply(final Module module) {
                  EList<Node> _nodes = module.getNodes();
                  final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
                    public Boolean apply(final Node it) {
                      return Boolean.valueOf(it.equals(node));
                    }
                  };
                  return Boolean.valueOf(IterableExtensions.<Node>exists(_nodes, _function));
                }
              };
              final Module targetModule = IterableExtensions.<Module>findFirst(_modules, _function_1);
              boolean _and = false;
              EModuleKind _kind = targetModule.getKind();
              boolean _equals = Objects.equal(_kind, EModuleKind.ANONYMOUS);
              if (!_equals) {
                _and = false;
              } else {
                Object _objectValue = ModularHypergraphDiagramSynthesis.this.getObjectValue(ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS);
                boolean _equals_1 = Objects.equal(_objectValue, ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS_NO);
                _and = _equals_1;
              }
              if (_and) {
                List<Module> _computeTransitiveModules = ModularHypergraphDiagramSynthesis.this.computeTransitiveModules(targetModule, hypergraph);
                final Consumer<Module> _function_2 = new Consumer<Module>() {
                  public void accept(final Module transitive) {
                    boolean _equals = transitive.equals(sourceModule);
                    boolean _not = (!_equals);
                    if (_not) {
                      ModularHypergraphDiagramSynthesis.this.registerConnection(targetModules, targetModule);
                    }
                  }
                };
                _computeTransitiveModules.forEach(_function_2);
              } else {
                ModularHypergraphDiagramSynthesis.this.registerConnection(targetModules, targetModule);
              }
            }
          }
        };
        _filter.forEach(_function_1);
      }
    };
    edges.forEach(_function_1);
    final BiConsumer<Module, Integer> _function_2 = new BiConsumer<Module, Integer>() {
      public void accept(final Module module, final Integer count) {
        ModularHypergraphDiagramSynthesis.this.createAggregatedEdge(sourceModule, module, count);
      }
    };
    targetModules.forEach(_function_2);
  }
  
  /**
   * Calculate transitive set of modules.
   */
  private List<Module> computeTransitiveModules(final Module sourceModule, final ModularHypergraph hypergraph) {
    final ArrayList<Module> transitiveModules = new ArrayList<Module>();
    final ArrayList<Edge> edges = new ArrayList<Edge>();
    EList<Node> _nodes = sourceModule.getNodes();
    final Consumer<Node> _function = new Consumer<Node>() {
      public void accept(final Node node) {
        EList<Edge> _edges = node.getEdges();
        final Consumer<Edge> _function = new Consumer<Edge>() {
          public void accept(final Edge it) {
            ModularHypergraphDiagramSynthesis.this.addUnique(edges, it);
          }
        };
        _edges.forEach(_function);
      }
    };
    _nodes.forEach(_function);
    final Consumer<Edge> _function_1 = new Consumer<Edge>() {
      public void accept(final Edge edge) {
        EList<Node> _nodes = hypergraph.getNodes();
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
        Iterable<Node> _filter = IterableExtensions.<Node>filter(_nodes, _function);
        final Consumer<Node> _function_1 = new Consumer<Node>() {
          public void accept(final Node node) {
            EList<Node> _nodes = sourceModule.getNodes();
            final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
              public Boolean apply(final Node it) {
                return Boolean.valueOf(it.equals(node));
              }
            };
            boolean _exists = IterableExtensions.<Node>exists(_nodes, _function);
            boolean _not = (!_exists);
            if (_not) {
              EList<Module> _modules = hypergraph.getModules();
              final Function1<Module, Boolean> _function_1 = new Function1<Module, Boolean>() {
                public Boolean apply(final Module module) {
                  EList<Node> _nodes = module.getNodes();
                  final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
                    public Boolean apply(final Node it) {
                      return Boolean.valueOf(it.equals(node));
                    }
                  };
                  return Boolean.valueOf(IterableExtensions.<Node>exists(_nodes, _function));
                }
              };
              final Module targetModule = IterableExtensions.<Module>findFirst(_modules, _function_1);
              boolean _and = false;
              EModuleKind _kind = targetModule.getKind();
              boolean _equals = Objects.equal(_kind, EModuleKind.ANONYMOUS);
              if (!_equals) {
                _and = false;
              } else {
                Object _objectValue = ModularHypergraphDiagramSynthesis.this.getObjectValue(ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS);
                boolean _equals_1 = Objects.equal(_objectValue, ModularHypergraphDiagramSynthesis.VISIBLE_ANONYMOUS_NO);
                _and = _equals_1;
              }
              if (_and) {
                List<Module> _computeTransitiveModules = ModularHypergraphDiagramSynthesis.this.computeTransitiveModules(targetModule, hypergraph);
                ModularHypergraphDiagramSynthesis.this.addAllUnique(transitiveModules, _computeTransitiveModules);
              } else {
                ModularHypergraphDiagramSynthesis.this.addUnique(transitiveModules, targetModule);
              }
            }
          }
        };
        _filter.forEach(_function_1);
      }
    };
    edges.forEach(_function_1);
    return transitiveModules;
  }
  
  /**
   * Register a target module or increase its hit count.
   */
  private Integer registerConnection(final HashMap<Module, Integer> targetModules, final Module targetModule) {
    Integer _xifexpression = null;
    final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
      public Boolean apply(final Module it) {
        return Boolean.valueOf(it.equals(targetModule));
      }
    };
    boolean _exists = IterableExtensions.<Module>exists(this.processedModules, _function);
    boolean _not = (!_exists);
    if (_not) {
      Integer _xblockexpression = null;
      {
        Integer value = targetModules.get(targetModule);
        Integer _xifexpression_1 = null;
        boolean _equals = Objects.equal(value, null);
        if (_equals) {
          _xifexpression_1 = targetModules.put(targetModule, Integer.valueOf(1));
        } else {
          _xifexpression_1 = targetModules.put(targetModule, Integer.valueOf(((value).intValue() + 1)));
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  /**
   * Create an edge in the correct width for an aggregated edge.
   */
  private KEdge createAggregatedEdge(final Module sourceModule, final Module targetModule, final Integer size) {
    KEdge _xblockexpression = null;
    {
      final KNode sourceNode = this.moduleMap.get(sourceModule);
      final KNode targetNode = this.moduleMap.get(targetModule);
      int _xifexpression = (int) 0;
      if (((size).intValue() <= 1)) {
        _xifexpression = 1;
      } else {
        int _xifexpression_1 = (int) 0;
        if (((size).intValue() <= 3)) {
          _xifexpression_1 = 2;
        } else {
          int _xifexpression_2 = (int) 0;
          if (((size).intValue() <= 7)) {
            _xifexpression_2 = 3;
          } else {
            int _xifexpression_3 = (int) 0;
            if (((size).intValue() <= 10)) {
              _xifexpression_3 = 4;
            } else {
              int _xifexpression_4 = (int) 0;
              if (((size).intValue() <= 15)) {
                _xifexpression_4 = 5;
              } else {
                int _xifexpression_5 = (int) 0;
                if (((size).intValue() <= 20)) {
                  _xifexpression_5 = 6;
                } else {
                  _xifexpression_5 = 7;
                }
                _xifexpression_4 = _xifexpression_5;
              }
              _xifexpression_3 = _xifexpression_4;
            }
            _xifexpression_2 = _xifexpression_3;
          }
          _xifexpression_1 = _xifexpression_2;
        }
        _xifexpression = _xifexpression_1;
      }
      final int lineWidth = _xifexpression;
      final int portSize = (lineWidth + 2);
      KPort _createPort = this._kPortExtensions.createPort();
      final Procedure1<KPort> _function = new Procedure1<KPort>() {
        public void apply(final KPort it) {
          ModularHypergraphDiagramSynthesis.this._kPortExtensions.setPortSize(it, portSize, portSize);
          KRectangle _addRectangle = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addRectangle(it);
          KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("black");
          KRectangle _setBackground = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KRectangle>setBackground(_addRectangle, _color);
          ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineJoin(_setBackground, LineJoin.JOIN_ROUND);
        }
      };
      final KPort sourcePort = ObjectExtensions.<KPort>operator_doubleArrow(_createPort, _function);
      KPort _createPort_1 = this._kPortExtensions.createPort();
      final Procedure1<KPort> _function_1 = new Procedure1<KPort>() {
        public void apply(final KPort it) {
          ModularHypergraphDiagramSynthesis.this._kPortExtensions.setPortSize(it, portSize, portSize);
          KRectangle _addRectangle = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addRectangle(it);
          KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("black");
          KRectangle _setBackground = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KRectangle>setBackground(_addRectangle, _color);
          ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineJoin(_setBackground, LineJoin.JOIN_ROUND);
        }
      };
      final KPort targetPort = ObjectExtensions.<KPort>operator_doubleArrow(_createPort_1, _function_1);
      EList<KPort> _ports = sourceNode.getPorts();
      _ports.add(sourcePort);
      EList<KPort> _ports_1 = targetNode.getPorts();
      _ports_1.add(targetPort);
      KEdge _createEdge = this._kEdgeExtensions.createEdge();
      final Procedure1<KEdge> _function_2 = new Procedure1<KEdge>() {
        public void apply(final KEdge it) {
          it.setSource(sourceNode);
          it.setSourcePort(sourcePort);
          it.setTarget(targetNode);
          it.setTargetPort(targetPort);
          KPolyline _addPolyline = ModularHypergraphDiagramSynthesis.this._kEdgeExtensions.addPolyline(it);
          final Procedure1<KPolyline> _function = new Procedure1<KPolyline>() {
            public void apply(final KPolyline it) {
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, lineWidth);
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineStyle(it, LineStyle.SOLID);
            }
          };
          ObjectExtensions.<KPolyline>operator_doubleArrow(_addPolyline, _function);
        }
      };
      _xblockexpression = ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge, _function_2);
    }
    return _xblockexpression;
  }
  
  private boolean addUnique(final List<Edge> edges, final Edge edge) {
    boolean _xifexpression = false;
    boolean _contains = edges.contains(edge);
    boolean _not = (!_contains);
    if (_not) {
      _xifexpression = edges.add(edge);
    }
    return _xifexpression;
  }
  
  private boolean addUnique(final List<Module> modules, final Module module) {
    boolean _xifexpression = false;
    boolean _contains = modules.contains(module);
    boolean _not = (!_contains);
    if (_not) {
      _xifexpression = modules.add(module);
    }
    return _xifexpression;
  }
  
  private void addAllUnique(final List<Module> modules, final List<Module> additionalModules) {
    final Consumer<Module> _function = new Consumer<Module>() {
      public void accept(final Module it) {
        ModularHypergraphDiagramSynthesis.this.addUnique(modules, it);
      }
    };
    additionalModules.forEach(_function);
  }
  
  /**
   * Create module without nodes for simple display.
   */
  private KNode createEmptyModule(final Module module) {
    KColor _switchResult = null;
    EModuleKind _kind = module.getKind();
    if (_kind != null) {
      switch (_kind) {
        case SYSTEM:
          _switchResult = this._kColorExtensions.getColor("LemonChiffon");
          break;
        case FRAMEWORK:
          _switchResult = this._kColorExtensions.getColor("Blue");
          break;
        case ANONYMOUS:
          _switchResult = this._kColorExtensions.getColor("Orange");
          break;
        case INTERFACE:
          _switchResult = this._kColorExtensions.getColor("White");
          break;
        default:
          break;
      }
    }
    final KColor otherColor = _switchResult;
    String _xifexpression = null;
    EModuleKind _kind_1 = module.getKind();
    boolean _equals = Objects.equal(_kind_1, EModuleKind.ANONYMOUS);
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
    String _substring_1 = moduleQualifier.substring((separator + 1));
    return this.drawEmptyModule(module, _substring, _substring_1, otherColor);
  }
  
  /**
   * Draw the empty module.
   */
  private KNode drawEmptyModule(final Module module, final String packageName, final String moduleName, final KColor otherColor) {
    KNode _xblockexpression = null;
    {
      KNode _createNode = this._kNodeExtensions.createNode(module);
      final KNode moduleNode = this.<KNode>associateWith(_createNode, module);
      this.moduleMap.put(module, moduleNode);
      final Procedure1<KNode> _function = new Procedure1<KNode>() {
        public void apply(final KNode it) {
          ModularHypergraphDiagramSynthesis.this.<KNode, Float>setLayoutOption(it, LayoutOptions.PORT_SPACING, Float.valueOf(20f));
          EnumSet<SizeConstraint> _minimumSizeWithPorts = SizeConstraint.minimumSizeWithPorts();
          ModularHypergraphDiagramSynthesis.this.<KNode, EnumSet<SizeConstraint>>setLayoutOption(it, LayoutOptions.SIZE_CONSTRAINT, _minimumSizeWithPorts);
          KRoundedRectangle _addRoundedRectangle = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addRoundedRectangle(it, 10, 10);
          final Procedure1<KRoundedRectangle> _function = new Procedure1<KRoundedRectangle>() {
            public void apply(final KRoundedRectangle it) {
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
              KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("white");
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KRoundedRectangle>setBackgroundGradient(it, _color, otherColor, 0);
              KColor _color_1 = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("black");
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setShadow(it, _color_1);
              KGridPlacement _setGridPlacement = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.setGridPlacement(it, 1);
              KGridPlacement _from = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.from(_setGridPlacement, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.LEFT, 10, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.TOP, 20, 0);
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.to(_from, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.RIGHT, 10, 0, ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.BOTTOM, 20, 0);
              KText _addText = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, packageName);
              final Procedure1<KText> _function = new Procedure1<KText>() {
                public void apply(final KText it) {
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontBold(it, false);
                  it.setCursorSelectable(false);
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KText>setLeftTopAlignedPointPlacementData(it, 1, 1, 1, 1);
                }
              };
              ObjectExtensions.<KText>operator_doubleArrow(_addText, _function);
              KText _addText_1 = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, moduleName);
              final Procedure1<KText> _function_1 = new Procedure1<KText>() {
                public void apply(final KText it) {
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontBold(it, true);
                  it.setCursorSelectable(true);
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KText>setLeftTopAlignedPointPlacementData(it, 1, 1, 1, 1);
                }
              };
              ObjectExtensions.<KText>operator_doubleArrow(_addText_1, _function_1);
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
   * Draw module as a rectangle with its nodes inside.
   * 
   * @param module the module to be rendered
   */
  private KNode createModuleWithNodes(final Module module) {
    KNode _xblockexpression = null;
    {
      KNode _createNode = this._kNodeExtensions.createNode(module);
      final KNode moduleNode = this.<KNode>associateWith(_createNode, module);
      KShapeLayout _data = moduleNode.<KShapeLayout>getData(KShapeLayout.class);
      KInsets _insets = _data.getInsets();
      _insets.setTop(15);
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
              String _name = module.getName();
              KText _addText = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, _name);
              final Procedure1<KText> _function = new Procedure1<KText>() {
                public void apply(final KText it) {
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setFontBold(it, true);
                  it.setCursorSelectable(true);
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KText>setLeftTopAlignedPointPlacementData(it, 1, 1, 1, 1);
                }
              };
              ObjectExtensions.<KText>operator_doubleArrow(_addText, _function);
              ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addChildArea(it);
              EList<Node> _nodes = module.getNodes();
              final Consumer<Node> _function_1 = new Consumer<Node>() {
                public void accept(final Node node) {
                  ModularHypergraphDiagramSynthesis.this.createGraphNode(node, it, module, moduleNode);
                }
              };
              _nodes.forEach(_function_1);
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
  private KNode createGraphNode(final Node node, final KContainerRendering parent, final Module module, final KNode moduleNode) {
    KNode _xblockexpression = null;
    {
      KNode _createNode = this._kNodeExtensions.createNode(node);
      final KNode kNode = this.<KNode>associateWith(_createNode, node);
      this.nodeMap.put(node, kNode);
      EList<KNode> _children = moduleNode.getChildren();
      _children.add(kNode);
      final Procedure1<KNode> _function = new Procedure1<KNode>() {
        public void apply(final KNode it) {
          KEllipse _addEllipse = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addEllipse(it);
          final Procedure1<KEllipse> _function = new Procedure1<KEllipse>() {
            public void apply(final KEllipse it) {
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
              KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("white");
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setBackground(it, _color);
              ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KEllipse>setSurroundingSpace(it, 10, 0, 10, 0);
              String _name = node.getName();
              String _name_1 = module.getName();
              int _length = _name_1.length();
              int _plus = (_length + 1);
              String _substring = _name.substring(_plus);
              KText _addText = ModularHypergraphDiagramSynthesis.this._kContainerRenderingExtensions.addText(it, _substring);
              final Procedure1<KText> _function = new Procedure1<KText>() {
                public void apply(final KText it) {
                  ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KText>setSurroundingSpace(it, 10, 0, 10, 0);
                  it.setCursorSelectable(true);
                }
              };
              ObjectExtensions.<KText>operator_doubleArrow(_addText, _function);
            }
          };
          ObjectExtensions.<KEllipse>operator_doubleArrow(_addEllipse, _function);
        }
      };
      _xblockexpression = ObjectExtensions.<KNode>operator_doubleArrow(kNode, _function);
    }
    return _xblockexpression;
  }
  
  private KPort getOrCreateEdgePort(final KNode kNode, final String label) {
    KPort _get = this.portMap.get(label);
    boolean _equals = Objects.equal(_get, null);
    if (_equals) {
      EList<KPort> _ports = kNode.getPorts();
      KPort _createPort = this._kPortExtensions.createPort();
      final Procedure1<KPort> _function = new Procedure1<KPort>() {
        public void apply(final KPort it) {
          ModularHypergraphDiagramSynthesis.this._kPortExtensions.setPortSize(it, 2, 2);
          KRectangle _addRectangle = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addRectangle(it);
          KColor _color = ModularHypergraphDiagramSynthesis.this._kColorExtensions.getColor("black");
          KRectangle _setBackground = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.<KRectangle>setBackground(_addRectangle, _color);
          ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineJoin(_setBackground, LineJoin.JOIN_ROUND);
          ModularHypergraphDiagramSynthesis.this.portMap.put(label, it);
        }
      };
      KPort _doubleArrow = ObjectExtensions.<KPort>operator_doubleArrow(_createPort, _function);
      _ports.add(_doubleArrow);
    }
    return this.portMap.get(label);
  }
  
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
      Object _xifexpression = null;
      int _size = IterableExtensions.size(referencedNodes);
      boolean _greaterThan = (_size > 1);
      if (_greaterThan) {
        Object _xifexpression_1 = null;
        int _size_1 = IterableExtensions.size(referencedNodes);
        boolean _equals = (_size_1 == 2);
        if (_equals) {
          Object _get = ((Object[])Conversions.unwrapArray(referencedNodes, Object.class))[0];
          KNode _get_1 = this.nodeMap.get(_get);
          Object _get_2 = ((Object[])Conversions.unwrapArray(referencedNodes, Object.class))[1];
          KNode _get_3 = this.nodeMap.get(_get_2);
          _xifexpression_1 = this.drawEdge(_get_1, _get_3, edge);
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
  
  private KNode drawHyperEdge(final Edge graphEdge, final Iterable<Node> nodes) {
    KNode _createNode = this._kNodeExtensions.createNode(graphEdge);
    final Procedure1<KNode> _function = new Procedure1<KNode>() {
      public void apply(final KNode it) {
        KEllipse _addEllipse = ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.addEllipse(it);
        final Procedure1<KEllipse> _function = new Procedure1<KEllipse>() {
          public void apply(final KEllipse it) {
            ModularHypergraphDiagramSynthesis.this._kRenderingExtensions.setLineWidth(it, 2);
          }
        };
        ObjectExtensions.<KEllipse>operator_doubleArrow(_addEllipse, _function);
      }
    };
    final KNode edgeNode = ObjectExtensions.<KNode>operator_doubleArrow(_createNode, _function);
    final Consumer<Node> _function_1 = new Consumer<Node>() {
      public void accept(final Node node) {
        KNode _get = ModularHypergraphDiagramSynthesis.this.nodeMap.get(node);
        ModularHypergraphDiagramSynthesis.this.drawEdge(edgeNode, _get, graphEdge);
      }
    };
    nodes.forEach(_function_1);
    return edgeNode;
  }
  
  private KEdge drawEdge(final KNode left, final KNode right, final Edge graphEdge) {
    KEdge _xifexpression = null;
    KNode _parent = right.getParent();
    KNode _parent_1 = left.getParent();
    boolean _equals = _parent.equals(_parent_1);
    if (_equals) {
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
      _xifexpression = ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge, _function);
    } else {
      KEdge _xifexpression_1 = null;
      KNode _parent_2 = left.getParent();
      boolean _notEquals = (!Objects.equal(_parent_2, null));
      if (_notEquals) {
        KEdge _xblockexpression = null;
        {
          String _string = left.toString();
          String _plus = (_string + "_to_");
          KNode _parent_3 = right.getParent();
          String _string_1 = _parent_3.toString();
          String _plus_1 = (_plus + _string_1);
          KPort _get = this.portMap.get(_plus_1);
          boolean _equals_1 = Objects.equal(_get, null);
          if (_equals_1) {
            KEdge _createEdge_1 = this._kEdgeExtensions.createEdge();
            final Procedure1<KEdge> _function_1 = new Procedure1<KEdge>() {
              public void apply(final KEdge it) {
                it.setSource(left);
                String _string = left.toString();
                String _plus = (_string + "_to_");
                KNode _parent = right.getParent();
                String _string_1 = _parent.toString();
                String _plus_1 = (_plus + _string_1);
                KPort _orCreateEdgePort = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(left, _plus_1);
                it.setSourcePort(_orCreateEdgePort);
                KNode _parent_1 = left.getParent();
                KNode _parent_2 = left.getParent();
                String _string_2 = _parent_2.toString();
                String _plus_2 = (_string_2 + "_to_");
                KNode _parent_3 = right.getParent();
                String _string_3 = _parent_3.toString();
                String _plus_3 = (_plus_2 + _string_3);
                KPort _orCreateEdgePort_1 = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(_parent_1, _plus_3);
                it.setTargetPort(_orCreateEdgePort_1);
                KNode _parent_4 = left.getParent();
                it.setTarget(_parent_4);
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
            ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge_1, _function_1);
          }
          KNode _parent_4 = right.getParent();
          String _string_2 = _parent_4.toString();
          String _plus_2 = (_string_2 + "_to_");
          KNode _parent_5 = left.getParent();
          String _string_3 = _parent_5.toString();
          String _plus_3 = (_plus_2 + _string_3);
          KPort _get_1 = this.portMap.get(_plus_3);
          boolean _equals_2 = Objects.equal(_get_1, null);
          if (_equals_2) {
            KEdge _createEdge_2 = this._kEdgeExtensions.createEdge();
            final Procedure1<KEdge> _function_2 = new Procedure1<KEdge>() {
              public void apply(final KEdge it) {
                KNode _parent = left.getParent();
                it.setSource(_parent);
                KNode _parent_1 = left.getParent();
                KNode _parent_2 = left.getParent();
                String _string = _parent_2.toString();
                String _plus = (_string + "_to_");
                KNode _parent_3 = right.getParent();
                String _string_1 = _parent_3.toString();
                String _plus_1 = (_plus + _string_1);
                KPort _orCreateEdgePort = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(_parent_1, _plus_1);
                it.setSourcePort(_orCreateEdgePort);
                KNode _parent_4 = right.getParent();
                KNode _parent_5 = right.getParent();
                String _string_2 = _parent_5.toString();
                String _plus_2 = (_string_2 + "_to_");
                KNode _parent_6 = left.getParent();
                String _string_3 = _parent_6.toString();
                String _plus_3 = (_plus_2 + _string_3);
                KPort _orCreateEdgePort_1 = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(_parent_4, _plus_3);
                it.setTargetPort(_orCreateEdgePort_1);
                KNode _parent_7 = right.getParent();
                it.setTarget(_parent_7);
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
            ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge_2, _function_2);
          }
          KEdge _xifexpression_2 = null;
          String _string_4 = right.toString();
          String _plus_4 = (_string_4 + "_to_");
          KNode _parent_6 = left.getParent();
          String _string_5 = _parent_6.toString();
          String _plus_5 = (_plus_4 + _string_5);
          KPort _get_2 = this.portMap.get(_plus_5);
          boolean _equals_3 = Objects.equal(_get_2, null);
          if (_equals_3) {
            KEdge _createEdge_3 = this._kEdgeExtensions.createEdge();
            final Procedure1<KEdge> _function_3 = new Procedure1<KEdge>() {
              public void apply(final KEdge it) {
                it.setSource(right);
                String _string = right.toString();
                String _plus = (_string + "_to_");
                KNode _parent = left.getParent();
                String _string_1 = _parent.toString();
                String _plus_1 = (_plus + _string_1);
                KPort _orCreateEdgePort = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(right, _plus_1);
                it.setSourcePort(_orCreateEdgePort);
                KNode _parent_1 = right.getParent();
                KNode _parent_2 = right.getParent();
                String _string_2 = _parent_2.toString();
                String _plus_2 = (_string_2 + "_to_");
                KNode _parent_3 = left.getParent();
                String _string_3 = _parent_3.toString();
                String _plus_3 = (_plus_2 + _string_3);
                KPort _orCreateEdgePort_1 = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(_parent_1, _plus_3);
                it.setTargetPort(_orCreateEdgePort_1);
                KNode _parent_4 = right.getParent();
                it.setTarget(_parent_4);
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
            _xifexpression_2 = ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge_3, _function_3);
          }
          _xblockexpression = _xifexpression_2;
        }
        _xifexpression_1 = _xblockexpression;
      } else {
        KEdge _xblockexpression_1 = null;
        {
          KEdge _createEdge_1 = this._kEdgeExtensions.createEdge();
          final Procedure1<KEdge> _function_1 = new Procedure1<KEdge>() {
            public void apply(final KEdge it) {
              it.setSource(left);
              String _string = left.toString();
              KPort _orCreateEdgePort = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(left, _string);
              it.setSourcePort(_orCreateEdgePort);
              KNode _parent = right.getParent();
              KNode _parent_1 = right.getParent();
              String _string_1 = _parent_1.toString();
              String _plus = (_string_1 + "_to_");
              String _string_2 = left.toString();
              String _plus_1 = (_plus + _string_2);
              KPort _orCreateEdgePort_1 = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(_parent, _plus_1);
              it.setTargetPort(_orCreateEdgePort_1);
              KNode _parent_2 = right.getParent();
              it.setTarget(_parent_2);
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
          ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge_1, _function_1);
          KEdge _createEdge_2 = this._kEdgeExtensions.createEdge();
          final Procedure1<KEdge> _function_2 = new Procedure1<KEdge>() {
            public void apply(final KEdge it) {
              it.setSource(right);
              String _string = right.toString();
              String _plus = (_string + "_to_");
              String _string_1 = left.toString();
              String _plus_1 = (_plus + _string_1);
              KPort _orCreateEdgePort = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(right, _plus_1);
              it.setSourcePort(_orCreateEdgePort);
              KNode _parent = right.getParent();
              KNode _parent_1 = right.getParent();
              String _string_2 = _parent_1.toString();
              String _plus_2 = (_string_2 + "_to_");
              String _string_3 = left.toString();
              String _plus_3 = (_plus_2 + _string_3);
              KPort _orCreateEdgePort_1 = ModularHypergraphDiagramSynthesis.this.getOrCreateEdgePort(_parent, _plus_3);
              it.setTargetPort(_orCreateEdgePort_1);
              KNode _parent_2 = right.getParent();
              it.setTarget(_parent_2);
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
          _xblockexpression_1 = ObjectExtensions.<KEdge>operator_doubleArrow(_createEdge_2, _function_2);
        }
        _xifexpression_1 = _xblockexpression_1;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
}
