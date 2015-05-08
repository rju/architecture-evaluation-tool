package de.cau.cs.se.evaluation.architecture.jobs;

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class ComplexityAnalysisJob extends Job {
  private final List<AbstractTypeDeclaration> classes;
  
  private final List<String> dataTypePatterns;
  
  private final List<String> observedSystemPatterns;
  
  private volatile Iterator<Node> globalHyperEdgesOnlyGraphNodes;
  
  private volatile List<Hypergraph> resultConnectedNodeGraphs;
  
  private volatile double complexity;
  
  private volatile List<Hypergraph> globalMetricsSubGraphs;
  
  private volatile int globalMetricsSubGraphCounter;
  
  private volatile int globalMetricsSubGraphTotal;
  
  private final IJavaProject project;
  
  /**
   * The constructor scans the selection for files.
   * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
   */
  public ComplexityAnalysisJob(final IJavaProject project, final List<AbstractTypeDeclaration> classes, final List<String> dataTypePatterns, final List<String> observedSystemPatterns) {
    super("Analysis Complexity");
    this.project = project;
    this.classes = classes;
    this.dataTypePatterns = dataTypePatterns;
    this.observedSystemPatterns = observedSystemPatterns;
  }
  
  protected IStatus run(final IProgressMonitor monitor) {
    final ResultModelProvider result = ResultModelProvider.INSTANCE;
    monitor.beginTask("Processing project", 0);
    final TransformationJavaMethodsToModularHypergraph javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(this.project, this.classes, this.dataTypePatterns, this.observedSystemPatterns, monitor);
    javaToModularHypergraph.transform();
    List<NamedValue> _values = result.getValues();
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    String _plus = (_name + " size of observed system");
    int _size = this.classes.size();
    NamedValue _namedValue = new NamedValue(_plus, _size);
    _values.add(_namedValue);
    List<NamedValue> _values_1 = result.getValues();
    IProject _project_1 = this.project.getProject();
    String _name_1 = _project_1.getName();
    String _plus_1 = (_name_1 + " number of modules");
    ModularHypergraph _modularSystem = javaToModularHypergraph.getModularSystem();
    EList<Module> _modules = _modularSystem.getModules();
    int _size_1 = _modules.size();
    NamedValue _namedValue_1 = new NamedValue(_plus_1, _size_1);
    _values_1.add(_namedValue_1);
    List<NamedValue> _values_2 = result.getValues();
    IProject _project_2 = this.project.getProject();
    String _name_2 = _project_2.getName();
    String _plus_2 = (_name_2 + " number of nodes");
    ModularHypergraph _modularSystem_1 = javaToModularHypergraph.getModularSystem();
    EList<Node> _nodes = _modularSystem_1.getNodes();
    int _size_2 = _nodes.size();
    NamedValue _namedValue_2 = new NamedValue(_plus_2, _size_2);
    _values_2.add(_namedValue_2);
    List<NamedValue> _values_3 = result.getValues();
    IProject _project_3 = this.project.getProject();
    String _name_3 = _project_3.getName();
    String _plus_3 = (_name_3 + " number of edges");
    ModularHypergraph _modularSystem_2 = javaToModularHypergraph.getModularSystem();
    EList<Edge> _edges = _modularSystem_2.getEdges();
    int _size_3 = _edges.size();
    NamedValue _namedValue_3 = new NamedValue(_plus_3, _size_3);
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
    IProject _project_4 = this.project.getProject();
    String _name_4 = _project_4.getName();
    String _plus_4 = (_name_4 + " Size");
    NamedValue _namedValue_4 = new NamedValue(_plus_4, systemSize);
    _values_4.add(_namedValue_4);
    List<NamedValue> _values_5 = result.getValues();
    IProject _project_5 = this.project.getProject();
    String _name_5 = _project_5.getName();
    String _plus_5 = (_name_5 + " Complexity");
    NamedValue _namedValue_5 = new NamedValue(_plus_5, complexity);
    _values_5.add(_namedValue_5);
    List<NamedValue> _values_6 = result.getValues();
    IProject _project_6 = this.project.getProject();
    String _name_6 = _project_6.getName();
    String _plus_6 = (_name_6 + " Cohesion");
    NamedValue _namedValue_6 = new NamedValue(_plus_6, (complexityIntermodule / complexityMaximalInterconnected));
    _values_6.add(_namedValue_6);
    List<NamedValue> _values_7 = result.getValues();
    IProject _project_7 = this.project.getProject();
    String _name_7 = _project_7.getName();
    String _plus_7 = (_name_7 + " Coupling");
    NamedValue _namedValue_7 = new NamedValue(_plus_7, complexityIntermodule);
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
}
