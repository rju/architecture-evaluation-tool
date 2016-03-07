package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.jobs.CoCoMEMegaModel;
import de.cau.cs.se.software.evaluation.views.NamedValue;
import de.cau.cs.se.software.evaluation.views.ResultModelProvider;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("all")
public class CoCoMEAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final Shell shell;
  
  private final boolean complete;
  
  public CoCoMEAnalysisJob(final IProject project, final boolean complete, final Shell shell) {
    super(project);
    this.shell = shell;
    this.complete = complete;
  }
  
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    final ResultModelProvider result = ResultModelProvider.INSTANCE;
    final CoCoMEMegaModel generator = new CoCoMEMegaModel();
    final Hypergraph graph = generator.createMegaModelAnalysis(this.complete);
    List<NamedValue> _values = result.getValues();
    String _name = this.project.getName();
    EList<Node> _nodes = graph.getNodes();
    int _size = _nodes.size();
    NamedValue _namedValue = new NamedValue(_name, "number of nodes", _size);
    _values.add(_namedValue);
    List<NamedValue> _values_1 = result.getValues();
    String _name_1 = this.project.getName();
    EList<Edge> _edges = graph.getEdges();
    int _size_1 = _edges.size();
    NamedValue _namedValue_1 = new NamedValue(_name_1, "number of edges", _size_1);
    _values_1.add(_namedValue_1);
    this.updateView(graph);
    this.calculateSize(graph, monitor, result);
    this.calculateComplexity(graph, monitor, result);
    monitor.done();
    return Status.OK_STATUS;
  }
}
