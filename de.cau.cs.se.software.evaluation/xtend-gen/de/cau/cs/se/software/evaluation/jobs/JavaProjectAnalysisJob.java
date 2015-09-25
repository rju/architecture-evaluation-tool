package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.transformation.java.TransformationJavaMethodsToModularHypergraph;
import de.cau.cs.se.software.evaluation.views.NamedValue;
import de.cau.cs.se.software.evaluation.views.ResultModelProvider;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;

@SuppressWarnings("all")
public class JavaProjectAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final List<AbstractTypeDeclaration> classes;
  
  private final List<String> dataTypePatterns;
  
  private final List<String> observedSystemPatterns;
  
  private final IJavaProject javaProject;
  
  public JavaProjectAnalysisJob(final IJavaProject project, final List<AbstractTypeDeclaration> classes, final List<String> dataTypePatterns, final List<String> observedSystemPatterns) {
    super(project.getProject());
    this.javaProject = project;
    this.classes = classes;
    this.dataTypePatterns = dataTypePatterns;
    this.observedSystemPatterns = observedSystemPatterns;
  }
  
  /**
   * Execute all metrics as requested.
   */
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    final ResultModelProvider result = ResultModelProvider.INSTANCE;
    List<NamedValue> _values = result.getValues();
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    int _size = this.classes.size();
    NamedValue _namedValue = new NamedValue(_name, "size of observed system", _size);
    _values.add(_namedValue);
    this.updateView(null);
    final ModularHypergraph inputHypergraph = this.createHypergraphForJavaProject(monitor, result);
    this.calculateSize(inputHypergraph, monitor, result);
    this.calculateComplexity(inputHypergraph, monitor, result);
    this.calculateCoupling(inputHypergraph, monitor, result);
    this.calculateCohesion(inputHypergraph, monitor, result);
    monitor.done();
    return Status.OK_STATUS;
  }
  
  /**
   * Construct hypergraph from java project
   */
  public ModularHypergraph createHypergraphForJavaProject(final IProgressMonitor monitor, final ResultModelProvider result) {
    final TransformationJavaMethodsToModularHypergraph javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(this.javaProject, this.dataTypePatterns, this.observedSystemPatterns, monitor);
    javaToModularHypergraph.transform(this.classes);
    List<NamedValue> _values = result.getValues();
    String _name = this.project.getName();
    ModularHypergraph _result = javaToModularHypergraph.getResult();
    EList<Module> _modules = _result.getModules();
    int _size = _modules.size();
    NamedValue _namedValue = new NamedValue(_name, "number of modules", _size);
    _values.add(_namedValue);
    List<NamedValue> _values_1 = result.getValues();
    String _name_1 = this.project.getName();
    ModularHypergraph _result_1 = javaToModularHypergraph.getResult();
    EList<Node> _nodes = _result_1.getNodes();
    int _size_1 = _nodes.size();
    NamedValue _namedValue_1 = new NamedValue(_name_1, "number of nodes", _size_1);
    _values_1.add(_namedValue_1);
    List<NamedValue> _values_2 = result.getValues();
    String _name_2 = this.project.getName();
    ModularHypergraph _result_2 = javaToModularHypergraph.getResult();
    EList<Edge> _edges = _result_2.getEdges();
    int _size_2 = _edges.size();
    NamedValue _namedValue_2 = new NamedValue(_name_2, "number of edges", _size_2);
    _values_2.add(_namedValue_2);
    ModularHypergraph _result_3 = javaToModularHypergraph.getResult();
    this.updateView(_result_3);
    return javaToModularHypergraph.getResult();
  }
}
