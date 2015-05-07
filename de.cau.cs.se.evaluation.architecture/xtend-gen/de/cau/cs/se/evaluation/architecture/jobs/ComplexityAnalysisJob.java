package de.cau.cs.se.evaluation.architecture.jobs;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.jobs.CalculationSubJob;
import de.cau.cs.se.evaluation.architecture.jobs.MetricsSubJob;
import de.cau.cs.se.evaluation.architecture.transformation.java.TransformationJavaMethodsToModularHypergraph;
import de.cau.cs.se.evaluation.architecture.transformation.metrics.NamedValue;
import de.cau.cs.se.evaluation.architecture.transformation.metrics.ResultModelProvider;
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphMetrics;
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationHyperedgesOnlyGraph;
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationIntermoduleHyperedgesOnlyGraph;
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationMaximalInterconnectedGraph;
import de.cau.cs.se.evaluation.architecture.views.AnalysisResultView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ComplexityAnalysisJob extends Job {
  private ArrayList<IType> types = new ArrayList<IType>();
  
  private List<String> dataTypePatterns;
  
  private final Shell shell;
  
  private List<String> observedSystemPatterns;
  
  private ISelection selection;
  
  private volatile Iterator<Node> globalHyperEdgesOnlyGraphNodes;
  
  private volatile List<Hypergraph> resultConnectedNodeGraphs;
  
  private volatile double complexity;
  
  private volatile List<Hypergraph> globalMetricsSubGraphs;
  
  private volatile int globalMetricsSubGraphCounter;
  
  private volatile int globalMetricsSubGraphTotal;
  
  /**
   * The constructor scans the selection for files.
   * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
   */
  public ComplexityAnalysisJob(final ISelection selection, final Shell shell) {
    super("Analysis Complexity");
    this.shell = shell;
    this.selection = selection;
  }
  
  protected IStatus run(final IProgressMonitor monitor) {
    this.determineClassFiles(monitor);
    final ResultModelProvider result = ResultModelProvider.INSTANCE;
    final ArrayList<IJavaProject> projects = new ArrayList<IJavaProject>();
    final Consumer<IType> _function = new Consumer<IType>() {
      public void accept(final IType type) {
        IJavaProject _javaProject = type.getJavaProject();
        boolean _contains = projects.contains(_javaProject);
        boolean _not = (!_contains);
        if (_not) {
          IJavaProject _javaProject_1 = type.getJavaProject();
          projects.add(_javaProject_1);
        }
      }
    };
    this.types.forEach(_function);
    monitor.beginTask("Processing project", 0);
    IJavaProject _get = projects.get(0);
    IProject _project = _get.getProject();
    final String projectName = _project.getName();
    monitor.subTask("Determining observed system classes");
    IJavaProject _get_1 = projects.get(0);
    final TransformationJavaMethodsToModularHypergraph javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(_get_1, this.types, this.dataTypePatterns, this.observedSystemPatterns, monitor);
    List<NamedValue> _values = result.getValues();
    List<AbstractTypeDeclaration> _classList = javaToModularHypergraph.getClassList();
    int _size = _classList.size();
    NamedValue _namedValue = new NamedValue((projectName + " size of observed system"), _size);
    _values.add(_namedValue);
    this.updateView();
    monitor.subTask("Reading Project");
    javaToModularHypergraph.transform();
    List<NamedValue> _values_1 = result.getValues();
    ModularHypergraph _modularSystem = javaToModularHypergraph.getModularSystem();
    EList<Module> _modules = _modularSystem.getModules();
    int _size_1 = _modules.size();
    NamedValue _namedValue_1 = new NamedValue((projectName + " number of modules"), _size_1);
    _values_1.add(_namedValue_1);
    List<NamedValue> _values_2 = result.getValues();
    ModularHypergraph _modularSystem_1 = javaToModularHypergraph.getModularSystem();
    EList<Node> _nodes = _modularSystem_1.getNodes();
    int _size_2 = _nodes.size();
    NamedValue _namedValue_2 = new NamedValue((projectName + " number of nodes"), _size_2);
    _values_2.add(_namedValue_2);
    List<NamedValue> _values_3 = result.getValues();
    ModularHypergraph _modularSystem_2 = javaToModularHypergraph.getModularSystem();
    EList<Edge> _edges = _modularSystem_2.getEdges();
    int _size_3 = _edges.size();
    NamedValue _namedValue_3 = new NamedValue((projectName + " number of edges"), _size_3);
    _values_3.add(_namedValue_3);
    this.updateView();
    ModularHypergraph _modularSystem_3 = javaToModularHypergraph.getModularSystem();
    final TransformationMaximalInterconnectedGraph transformationMaximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(_modularSystem_3);
    ModularHypergraph _modularSystem_4 = javaToModularHypergraph.getModularSystem();
    final TransformationIntermoduleHyperedgesOnlyGraph transformationIntermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(_modularSystem_4);
    monitor.subTask("Create maximal interconnected graph");
    transformationMaximalInterconnectedGraph.transform();
    monitor.subTask("Create intermodule hyperedges only graph");
    transformationIntermoduleHyperedgesOnlyGraph.transform();
    monitor.beginTask("Calculating metrics", (1 + (3 * 3)));
    final TransformationHypergraphMetrics metrics = new TransformationHypergraphMetrics(monitor);
    ModularHypergraph _modularSystem_5 = javaToModularHypergraph.getModularSystem();
    metrics.setSystem(_modularSystem_5);
    monitor.subTask("System size");
    final double systemSize = metrics.calculate();
    monitor.worked(1);
    ModularHypergraph _modularSystem_6 = javaToModularHypergraph.getModularSystem();
    final double complexity = this.calculateComplexity(_modularSystem_6, monitor, "Complexity");
    ModularHypergraph _result = transformationMaximalInterconnectedGraph.getResult();
    final double complexityMaximalInterconnected = this.calculateComplexity(_result, monitor, "Maximal interconnected graph complexity");
    ModularHypergraph _result_1 = transformationIntermoduleHyperedgesOnlyGraph.getResult();
    final double complexityIntermodule = this.calculateComplexity(_result_1, monitor, "Intermodule complexity");
    List<NamedValue> _values_4 = result.getValues();
    NamedValue _namedValue_4 = new NamedValue((projectName + " Size"), systemSize);
    _values_4.add(_namedValue_4);
    List<NamedValue> _values_5 = result.getValues();
    NamedValue _namedValue_5 = new NamedValue((projectName + " Complexity"), complexity);
    _values_5.add(_namedValue_5);
    List<NamedValue> _values_6 = result.getValues();
    NamedValue _namedValue_6 = new NamedValue((projectName + " Cohesion"), (complexityIntermodule / complexityMaximalInterconnected));
    _values_6.add(_namedValue_6);
    List<NamedValue> _values_7 = result.getValues();
    NamedValue _namedValue_7 = new NamedValue((projectName + " Coupling"), complexityIntermodule);
    _values_7.add(_namedValue_7);
    monitor.done();
    this.updateView();
    return Status.OK_STATUS;
  }
  
  private void updateView() {
    IWorkbench _workbench = PlatformUI.getWorkbench();
    Display _display = _workbench.getDisplay();
    _display.syncExec(new Runnable() {
      public void run() {
        try {
          IWorkbench _workbench = PlatformUI.getWorkbench();
          IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
          IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
          final IViewPart part = _activePage.showView(AnalysisResultView.ID);
          ((AnalysisResultView) part).update(ResultModelProvider.INSTANCE);
        } catch (final Throwable _t) {
          if (_t instanceof PartInitException) {
            final PartInitException e = (PartInitException)_t;
            e.printStackTrace();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
    });
  }
  
  /**
   * Determine class files.
   */
  private void determineClassFiles(final IProgressMonitor monitor) {
    if ((this.selection instanceof IStructuredSelection)) {
      System.out.println("Got a structured selection");
      if ((this.selection instanceof ITreeSelection)) {
        final TreeSelection treeSelection = ((TreeSelection) this.selection);
        Iterator _iterator = treeSelection.iterator();
        final Procedure1<Object> _function = new Procedure1<Object>() {
          public void apply(final Object element) {
            if ((element instanceof IProject)) {
              ComplexityAnalysisJob.this.scanForClasses(((IProject)element), monitor);
            } else {
              MessageDialog.openWarning(ComplexityAnalysisJob.this.shell, "Wrong Selection", "Only project roots can be selected.");
            }
          }
        };
        IteratorExtensions.<Object>forEach(_iterator, _function);
      }
    }
  }
  
  /**
   * Create Megamodel graph
   */
  public Hypergraph createMegaModelAnalysis() {
    final Hypergraph graph = HypergraphFactory.eINSTANCE.createHypergraph();
    for (int i = 1; (i < 22); i++) {
      {
        final Node node = HypergraphFactory.eINSTANCE.createNode();
        node.setName(("Node " + Integer.valueOf(i)));
        EList<Node> _nodes = graph.getNodes();
        _nodes.add(node);
      }
    }
    for (int i = 1; (i < 26); i++) {
      {
        final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
        edge.setName(("Edge " + Integer.valueOf(i)));
        EList<Edge> _edges = graph.getEdges();
        _edges.add(edge);
      }
    }
    EList<Node> _nodes = graph.getNodes();
    EList<Edge> _edges = graph.getEdges();
    this.connectNode(_nodes, _edges, "Node 1", "Edge 3");
    EList<Node> _nodes_1 = graph.getNodes();
    EList<Edge> _edges_1 = graph.getEdges();
    this.connectNode(_nodes_1, _edges_1, "Node 1", "Edge 9");
    EList<Node> _nodes_2 = graph.getNodes();
    EList<Edge> _edges_2 = graph.getEdges();
    this.connectNode(_nodes_2, _edges_2, "Node 2", "Edge 3");
    EList<Node> _nodes_3 = graph.getNodes();
    EList<Edge> _edges_3 = graph.getEdges();
    this.connectNode(_nodes_3, _edges_3, "Node 2", "Edge 4");
    EList<Node> _nodes_4 = graph.getNodes();
    EList<Edge> _edges_4 = graph.getEdges();
    this.connectNode(_nodes_4, _edges_4, "Node 2", "Edge 5");
    EList<Node> _nodes_5 = graph.getNodes();
    EList<Edge> _edges_5 = graph.getEdges();
    this.connectNode(_nodes_5, _edges_5, "Node 2", "Edge 6");
    EList<Node> _nodes_6 = graph.getNodes();
    EList<Edge> _edges_6 = graph.getEdges();
    this.connectNode(_nodes_6, _edges_6, "Node 2", "Edge 2");
    EList<Node> _nodes_7 = graph.getNodes();
    EList<Edge> _edges_7 = graph.getEdges();
    this.connectNode(_nodes_7, _edges_7, "Node 3", "Edge 2");
    EList<Node> _nodes_8 = graph.getNodes();
    EList<Edge> _edges_8 = graph.getEdges();
    this.connectNode(_nodes_8, _edges_8, "Node 3", "Edge 1");
    EList<Node> _nodes_9 = graph.getNodes();
    EList<Edge> _edges_9 = graph.getEdges();
    this.connectNode(_nodes_9, _edges_9, "Node 4", "Edge 9");
    EList<Node> _nodes_10 = graph.getNodes();
    EList<Edge> _edges_10 = graph.getEdges();
    this.connectNode(_nodes_10, _edges_10, "Node 4", "Edge 4");
    EList<Node> _nodes_11 = graph.getNodes();
    EList<Edge> _edges_11 = graph.getEdges();
    this.connectNode(_nodes_11, _edges_11, "Node 4", "Edge 25");
    EList<Node> _nodes_12 = graph.getNodes();
    EList<Edge> _edges_12 = graph.getEdges();
    this.connectNode(_nodes_12, _edges_12, "Node 4", "Edge 7");
    EList<Node> _nodes_13 = graph.getNodes();
    EList<Edge> _edges_13 = graph.getEdges();
    this.connectNode(_nodes_13, _edges_13, "Node 5", "Edge 7");
    EList<Node> _nodes_14 = graph.getNodes();
    EList<Edge> _edges_14 = graph.getEdges();
    this.connectNode(_nodes_14, _edges_14, "Node 5", "Edge 1");
    EList<Node> _nodes_15 = graph.getNodes();
    EList<Edge> _edges_15 = graph.getEdges();
    this.connectNode(_nodes_15, _edges_15, "Node 6", "Edge 5");
    EList<Node> _nodes_16 = graph.getNodes();
    EList<Edge> _edges_16 = graph.getEdges();
    this.connectNode(_nodes_16, _edges_16, "Node 6", "Edge 22");
    EList<Node> _nodes_17 = graph.getNodes();
    EList<Edge> _edges_17 = graph.getEdges();
    this.connectNode(_nodes_17, _edges_17, "Node 6", "Edge 24");
    EList<Node> _nodes_18 = graph.getNodes();
    EList<Edge> _edges_18 = graph.getEdges();
    this.connectNode(_nodes_18, _edges_18, "Node 7", "Edge 6");
    EList<Node> _nodes_19 = graph.getNodes();
    EList<Edge> _edges_19 = graph.getEdges();
    this.connectNode(_nodes_19, _edges_19, "Node 7", "Edge 24");
    EList<Node> _nodes_20 = graph.getNodes();
    EList<Edge> _edges_20 = graph.getEdges();
    this.connectNode(_nodes_20, _edges_20, "Node 7", "Edge 13");
    EList<Node> _nodes_21 = graph.getNodes();
    EList<Edge> _edges_21 = graph.getEdges();
    this.connectNode(_nodes_21, _edges_21, "Node 7", "Edge 23");
    EList<Node> _nodes_22 = graph.getNodes();
    EList<Edge> _edges_22 = graph.getEdges();
    this.connectNode(_nodes_22, _edges_22, "Node 8", "Edge 22");
    EList<Node> _nodes_23 = graph.getNodes();
    EList<Edge> _edges_23 = graph.getEdges();
    this.connectNode(_nodes_23, _edges_23, "Node 8", "Edge 1");
    EList<Node> _nodes_24 = graph.getNodes();
    EList<Edge> _edges_24 = graph.getEdges();
    this.connectNode(_nodes_24, _edges_24, "Node 9", "Edge 13");
    EList<Node> _nodes_25 = graph.getNodes();
    EList<Edge> _edges_25 = graph.getEdges();
    this.connectNode(_nodes_25, _edges_25, "Node 9", "Edge 12");
    EList<Node> _nodes_26 = graph.getNodes();
    EList<Edge> _edges_26 = graph.getEdges();
    this.connectNode(_nodes_26, _edges_26, "Node 10", "Edge 9");
    EList<Node> _nodes_27 = graph.getNodes();
    EList<Edge> _edges_27 = graph.getEdges();
    this.connectNode(_nodes_27, _edges_27, "Node 10", "Edge 10");
    EList<Node> _nodes_28 = graph.getNodes();
    EList<Edge> _edges_28 = graph.getEdges();
    this.connectNode(_nodes_28, _edges_28, "Node 11", "Edge 10");
    EList<Node> _nodes_29 = graph.getNodes();
    EList<Edge> _edges_29 = graph.getEdges();
    this.connectNode(_nodes_29, _edges_29, "Node 11", "Edge 25");
    EList<Node> _nodes_30 = graph.getNodes();
    EList<Edge> _edges_30 = graph.getEdges();
    this.connectNode(_nodes_30, _edges_30, "Node 11", "Edge 24");
    EList<Node> _nodes_31 = graph.getNodes();
    EList<Edge> _edges_31 = graph.getEdges();
    this.connectNode(_nodes_31, _edges_31, "Node 11", "Edge 23");
    EList<Node> _nodes_32 = graph.getNodes();
    EList<Edge> _edges_32 = graph.getEdges();
    this.connectNode(_nodes_32, _edges_32, "Node 11", "Edge 11");
    EList<Node> _nodes_33 = graph.getNodes();
    EList<Edge> _edges_33 = graph.getEdges();
    this.connectNode(_nodes_33, _edges_33, "Node 11", "Edge 16");
    EList<Node> _nodes_34 = graph.getNodes();
    EList<Edge> _edges_34 = graph.getEdges();
    this.connectNode(_nodes_34, _edges_34, "Node 12", "Edge 10");
    EList<Node> _nodes_35 = graph.getNodes();
    EList<Edge> _edges_35 = graph.getEdges();
    this.connectNode(_nodes_35, _edges_35, "Node 12", "Edge 8");
    EList<Node> _nodes_36 = graph.getNodes();
    EList<Edge> _edges_36 = graph.getEdges();
    this.connectNode(_nodes_36, _edges_36, "Node 13", "Edge 10");
    EList<Node> _nodes_37 = graph.getNodes();
    EList<Edge> _edges_37 = graph.getEdges();
    this.connectNode(_nodes_37, _edges_37, "Node 13", "Edge 21");
    EList<Node> _nodes_38 = graph.getNodes();
    EList<Edge> _edges_38 = graph.getEdges();
    this.connectNode(_nodes_38, _edges_38, "Node 14", "Edge 8");
    EList<Node> _nodes_39 = graph.getNodes();
    EList<Edge> _edges_39 = graph.getEdges();
    this.connectNode(_nodes_39, _edges_39, "Node 14", "Edge 21");
    EList<Node> _nodes_40 = graph.getNodes();
    EList<Edge> _edges_40 = graph.getEdges();
    this.connectNode(_nodes_40, _edges_40, "Node 15", "Edge 21");
    EList<Node> _nodes_41 = graph.getNodes();
    EList<Edge> _edges_41 = graph.getEdges();
    this.connectNode(_nodes_41, _edges_41, "Node 15", "Edge 20");
    EList<Node> _nodes_42 = graph.getNodes();
    EList<Edge> _edges_42 = graph.getEdges();
    this.connectNode(_nodes_42, _edges_42, "Node 15", "Edge 17");
    EList<Node> _nodes_43 = graph.getNodes();
    EList<Edge> _edges_43 = graph.getEdges();
    this.connectNode(_nodes_43, _edges_43, "Node 16", "Edge 20");
    EList<Node> _nodes_44 = graph.getNodes();
    EList<Edge> _edges_44 = graph.getEdges();
    this.connectNode(_nodes_44, _edges_44, "Node 16", "Edge 1");
    EList<Node> _nodes_45 = graph.getNodes();
    EList<Edge> _edges_45 = graph.getEdges();
    this.connectNode(_nodes_45, _edges_45, "Node 16", "Edge 19");
    EList<Node> _nodes_46 = graph.getNodes();
    EList<Edge> _edges_46 = graph.getEdges();
    this.connectNode(_nodes_46, _edges_46, "Node 17", "Edge 19");
    EList<Node> _nodes_47 = graph.getNodes();
    EList<Edge> _edges_47 = graph.getEdges();
    this.connectNode(_nodes_47, _edges_47, "Node 17", "Edge 18");
    EList<Node> _nodes_48 = graph.getNodes();
    EList<Edge> _edges_48 = graph.getEdges();
    this.connectNode(_nodes_48, _edges_48, "Node 18", "Edge 8");
    EList<Node> _nodes_49 = graph.getNodes();
    EList<Edge> _edges_49 = graph.getEdges();
    this.connectNode(_nodes_49, _edges_49, "Node 18", "Edge 16");
    EList<Node> _nodes_50 = graph.getNodes();
    EList<Edge> _edges_50 = graph.getEdges();
    this.connectNode(_nodes_50, _edges_50, "Node 18", "Edge 17");
    EList<Node> _nodes_51 = graph.getNodes();
    EList<Edge> _edges_51 = graph.getEdges();
    this.connectNode(_nodes_51, _edges_51, "Node 18", "Edge 18");
    EList<Node> _nodes_52 = graph.getNodes();
    EList<Edge> _edges_52 = graph.getEdges();
    this.connectNode(_nodes_52, _edges_52, "Node 19", "Edge 18");
    EList<Node> _nodes_53 = graph.getNodes();
    EList<Edge> _edges_53 = graph.getEdges();
    this.connectNode(_nodes_53, _edges_53, "Node 19", "Edge 11");
    EList<Node> _nodes_54 = graph.getNodes();
    EList<Edge> _edges_54 = graph.getEdges();
    this.connectNode(_nodes_54, _edges_54, "Node 20", "Edge 11");
    EList<Node> _nodes_55 = graph.getNodes();
    EList<Edge> _edges_55 = graph.getEdges();
    this.connectNode(_nodes_55, _edges_55, "Node 20", "Edge 12");
    EList<Node> _nodes_56 = graph.getNodes();
    EList<Edge> _edges_56 = graph.getEdges();
    this.connectNode(_nodes_56, _edges_56, "Node 20", "Edge 18");
    EList<Node> _nodes_57 = graph.getNodes();
    EList<Edge> _edges_57 = graph.getEdges();
    this.connectNode(_nodes_57, _edges_57, "Node 21", "Edge 12");
    EList<Node> _nodes_58 = graph.getNodes();
    EList<Edge> _edges_58 = graph.getEdges();
    this.connectNode(_nodes_58, _edges_58, "Node 21", "Edge 14");
    EList<Node> _nodes_59 = graph.getNodes();
    EList<Edge> _edges_59 = graph.getEdges();
    this.connectNode(_nodes_59, _edges_59, "Node 21", "Edge 1");
    return graph;
  }
  
  public boolean connectNode(final EList<Node> nodes, final EList<Edge> edges, final String nodeName, final String edgeName) {
    boolean _xblockexpression = false;
    {
      final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
        public Boolean apply(final Node node) {
          String _name = node.getName();
          return Boolean.valueOf(_name.equals(nodeName));
        }
      };
      final Node node = IterableExtensions.<Node>findFirst(nodes, _function);
      final Function1<Edge, Boolean> _function_1 = new Function1<Edge, Boolean>() {
        public Boolean apply(final Edge edge) {
          String _name = edge.getName();
          return Boolean.valueOf(_name.equals(edgeName));
        }
      };
      final Edge edge = IterableExtensions.<Edge>findFirst(edges, _function_1);
      EList<Edge> _edges = node.getEdges();
      _xblockexpression = _edges.add(edge);
    }
    return _xblockexpression;
  }
  
  /**
   * Calculate for a given modular hyper graph:
   * - hyperedges only graph
   * - hyperedges only graphs for each node in the graph which is connected to the i-th node
   * - calculate the size of all graphs
   * - calculate the complexity
   * 
   * @param input a modular system
   */
  private double calculateComplexity(final Hypergraph input, final IProgressMonitor monitor, final String message) {
    if (monitor!=null) {
      monitor.subTask((message + " - S^#"));
    }
    final TransformationHyperedgesOnlyGraph transformationHyperedgesOnlyGraph = new TransformationHyperedgesOnlyGraph(input);
    transformationHyperedgesOnlyGraph.transform();
    if (monitor!=null) {
      monitor.worked(1);
    }
    List<Job> jobs = new ArrayList<Job>();
    ArrayList<Hypergraph> _arrayList = new ArrayList<Hypergraph>();
    this.resultConnectedNodeGraphs = _arrayList;
    Hypergraph _result = transformationHyperedgesOnlyGraph.getResult();
    EList<Node> _nodes = _result.getNodes();
    Iterator<Node> _iterator = _nodes.iterator();
    this.globalHyperEdgesOnlyGraphNodes = _iterator;
    for (int j = 0; (j < 8); j++) {
      {
        Hypergraph _result_1 = transformationHyperedgesOnlyGraph.getResult();
        final CalculationSubJob job = new CalculationSubJob(("S^#_i " + Integer.valueOf(j)), this, _result_1);
        jobs.add(job);
        job.schedule();
      }
    }
    final Consumer<Job> _function = new Consumer<Job>() {
      public void accept(final Job it) {
        try {
          it.join();
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    jobs.forEach(_function);
    if (monitor!=null) {
      monitor.worked(1);
    }
    final TransformationHypergraphMetrics metrics = new TransformationHypergraphMetrics(monitor);
    Hypergraph _result_1 = transformationHyperedgesOnlyGraph.getResult();
    metrics.setSystem(_result_1);
    if (monitor!=null) {
      monitor.subTask((message + " - calculate"));
    }
    Hypergraph _result_2 = transformationHyperedgesOnlyGraph.getResult();
    final double complexity = this.calculateComplexity(_result_2, this.resultConnectedNodeGraphs, monitor);
    if (monitor!=null) {
      monitor.worked(1);
    }
    return complexity;
  }
  
  /**
   * Used for the parallelization. Return the next task
   */
  public synchronized Node getNextConnectedNodeTask() {
    Node _xifexpression = null;
    boolean _hasNext = this.globalHyperEdgesOnlyGraphNodes.hasNext();
    if (_hasNext) {
      _xifexpression = this.globalHyperEdgesOnlyGraphNodes.next();
    } else {
      _xifexpression = null;
    }
    return _xifexpression;
  }
  
  /**
   * Used for the parallelization. Deliver the result
   */
  public synchronized boolean deliverResult(final Hypergraph hypergraph) {
    return this.resultConnectedNodeGraphs.add(hypergraph);
  }
  
  /**
   * Calculate complexity.
   */
  private double calculateComplexity(final Hypergraph hypergraph, final List<Hypergraph> subgraphs, final IProgressMonitor monitor) {
    this.complexity = 0;
    this.globalMetricsSubGraphs = subgraphs;
    this.globalMetricsSubGraphCounter = 0;
    EList<Node> _nodes = hypergraph.getNodes();
    int _size = _nodes.size();
    this.globalMetricsSubGraphTotal = _size;
    List<Job> jobs = new ArrayList<Job>();
    for (int j = 0; (j < 8); j++) {
      {
        final MetricsSubJob job = new MetricsSubJob(("Metrics " + Integer.valueOf(j)), this);
        jobs.add(job);
        job.schedule();
      }
    }
    final Consumer<Job> _function = new Consumer<Job>() {
      public void accept(final Job it) {
        try {
          it.join();
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      }
    };
    jobs.forEach(_function);
    final TransformationHypergraphMetrics metrics = new TransformationHypergraphMetrics(monitor);
    metrics.setSystem(hypergraph);
    double _complexity = this.complexity;
    double _calculate = metrics.calculate();
    this.complexity = (_complexity - _calculate);
    return this.complexity;
  }
  
  public synchronized Hypergraph getNextSubgraph() {
    if ((this.globalMetricsSubGraphCounter < this.globalMetricsSubGraphTotal)) {
      final Hypergraph result = this.globalMetricsSubGraphs.get(this.globalMetricsSubGraphCounter);
      this.globalMetricsSubGraphCounter++;
      return result;
    } else {
      return null;
    }
  }
  
  public synchronized double deliverMetricsResult(final double d) {
    double _complexity = this.complexity;
    return this.complexity = (_complexity + d);
  }
  
  private void scanForClasses(final IProject object, final IProgressMonitor monitor) {
    try {
      String _string = object.toString();
      String _plus = ("  IProject " + _string);
      System.out.println(_plus);
      String _name = object.getName();
      String _plus_1 = ("Scanning project " + _name);
      monitor.beginTask(_plus_1, 0);
      IResource _findMember = object.findMember("data-type-pattern.cfg");
      final IFile dataTypePatternsFile = ((IFile) _findMember);
      boolean _isSynchronized = dataTypePatternsFile.isSynchronized(1);
      if (_isSynchronized) {
        List<String> _readPattern = this.readPattern(dataTypePatternsFile);
        this.dataTypePatterns = _readPattern;
        IResource _findMember_1 = object.findMember("observed-system.cfg");
        final IFile observedSystemPatternsFile = ((IFile) _findMember_1);
        boolean _isSynchronized_1 = observedSystemPatternsFile.isSynchronized(1);
        if (_isSynchronized_1) {
          List<String> _readPattern_1 = this.readPattern(observedSystemPatternsFile);
          this.observedSystemPatterns = _readPattern_1;
          boolean _hasNature = object.hasNature(JavaCore.NATURE_ID);
          if (_hasNature) {
            final IJavaProject project = JavaCore.create(object);
            IPackageFragmentRoot[] _allPackageFragmentRoots = project.getAllPackageFragmentRoots();
            final Consumer<IPackageFragmentRoot> _function = new Consumer<IPackageFragmentRoot>() {
              public void accept(final IPackageFragmentRoot root) {
                ComplexityAnalysisJob.this.checkForTypes(root, monitor);
              }
            };
            ((List<IPackageFragmentRoot>)Conversions.doWrapArray(_allPackageFragmentRoots)).forEach(_function);
          }
        } else {
          MessageDialog.openError(this.shell, "Configuration Error", "Observed system file (observed-system.cfg) listing patterns for classes of the observed system is missing.");
        }
      } else {
        MessageDialog.openError(this.shell, "Configuration Error", "Data type pattern file (data-type-pattern.cfg) listing patterns for data type classes is missing.");
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Read full qualified class name patterns form an IFile.
   * 
   * @param file the file to read.
   */
  private List<String> readPattern(final IFile file) {
    try {
      final List<String> patterns = new ArrayList<String>();
      InputStream _contents = file.getContents();
      InputStreamReader _inputStreamReader = new InputStreamReader(_contents);
      final BufferedReader reader = new BufferedReader(_inputStreamReader);
      String line = null;
      while ((!Objects.equal((line = reader.readLine()), null))) {
        String _replaceAll = line.replaceAll("\\.", "\\.");
        patterns.add(_replaceAll);
      }
      return patterns;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * in fragment roots
   */
  private void checkForTypes(final IPackageFragmentRoot root, final IProgressMonitor monitor) {
    try {
      String _elementName = root.getElementName();
      monitor.subTask(_elementName);
      IJavaElement[] _children = root.getChildren();
      final Consumer<IJavaElement> _function = new Consumer<IJavaElement>() {
        public void accept(final IJavaElement element) {
          if ((element instanceof IPackageFragment)) {
            ComplexityAnalysisJob.this.checkForTypes(((IPackageFragment) element), monitor);
          }
        }
      };
      ((List<IJavaElement>)Conversions.doWrapArray(_children)).forEach(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * in fragments
   */
  private void checkForTypes(final IPackageFragment fragment, final IProgressMonitor monitor) {
    try {
      IJavaElement[] _children = fragment.getChildren();
      final Consumer<IJavaElement> _function = new Consumer<IJavaElement>() {
        public void accept(final IJavaElement element) {
          if ((element instanceof IPackageFragment)) {
            ComplexityAnalysisJob.this.checkForTypes(((IPackageFragment) element), monitor);
          } else {
            if ((element instanceof ICompilationUnit)) {
              ComplexityAnalysisJob.this.checkForTypes(((ICompilationUnit) element), monitor);
            }
          }
        }
      };
      ((List<IJavaElement>)Conversions.doWrapArray(_children)).forEach(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * in compilation units
   */
  private void checkForTypes(final ICompilationUnit unit, final IProgressMonitor monitor) {
    try {
      IType[] _allTypes = unit.getAllTypes();
      final Consumer<IType> _function = new Consumer<IType>() {
        public void accept(final IType type) {
          if ((type instanceof IType)) {
            boolean _isBinary = ((IType) type).isBinary();
            boolean _not = (!_isBinary);
            if (_not) {
              ComplexityAnalysisJob.this.types.add(type);
            }
          }
        }
      };
      ((List<IType>)Conversions.doWrapArray(_allTypes)).forEach(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
