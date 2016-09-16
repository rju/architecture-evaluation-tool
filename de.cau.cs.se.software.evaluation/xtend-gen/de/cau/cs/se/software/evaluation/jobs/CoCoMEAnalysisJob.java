package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.jobs.CoCoMEMegaModel;
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("all")
public class CoCoMEAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final boolean complete;
  
  public CoCoMEAnalysisJob(final IProject project, final boolean complete, final Shell shell) {
    super(project, shell);
    this.complete = complete;
  }
  
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    final AnalysisResultModelProvider result = AnalysisResultModelProvider.INSTANCE;
    final CoCoMEMegaModel generator = new CoCoMEMegaModel();
    final Hypergraph graph = generator.createMegaModelAnalysis(this.complete);
    String _name = this.project.getName();
    EList<Node> _nodes = graph.getNodes();
    int _size = _nodes.size();
    result.addResult(_name, "number of nodes", _size);
    String _name_1 = this.project.getName();
    EList<Edge> _edges = graph.getEdges();
    int _size_1 = _edges.size();
    result.addResult(_name_1, "number of edges", _size_1);
    result.setResultHypergraph(graph);
    this.updateView();
    this.calculateSize(graph, monitor, result);
    this.calculateComplexity(graph, monitor, result);
    monitor.done();
    return Status.OK_STATUS;
  }
}
