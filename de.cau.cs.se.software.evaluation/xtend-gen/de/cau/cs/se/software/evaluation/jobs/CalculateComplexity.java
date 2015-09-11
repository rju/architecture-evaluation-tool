package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.ConnectedNodeHyperedgeOnlySizeJob;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHyperedgesOnlyGraph;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class CalculateComplexity {
  private final static int PARALLEL_TASKS = 8;
  
  /**
   * Used in the parallelized version of this.
   */
  private volatile Iterator<Node> globalHyperEdgesOnlyGraphNodes;
  
  private volatile double complexity;
  
  private final IProgressMonitor monitor;
  
  public CalculateComplexity(final IProgressMonitor monitor) {
    this.monitor = monitor;
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
  public double calculate(final Hypergraph input, final String message) {
    EList<Node> _nodes = input.getNodes();
    int _size = _nodes.size();
    int _plus = (_size + 1);
    this.monitor.beginTask(message, _plus);
    final TransformationHyperedgesOnlyGraph hyperedgesOnlyGraph = new TransformationHyperedgesOnlyGraph(this.monitor);
    hyperedgesOnlyGraph.transform(input);
    this.monitor.worked(1);
    Hypergraph _result = hyperedgesOnlyGraph.getResult();
    EList<Node> _nodes_1 = _result.getNodes();
    Iterator<Node> _iterator = _nodes_1.iterator();
    this.globalHyperEdgesOnlyGraphNodes = _iterator;
    this.complexity = 0;
    final List<Job> jobs = new ArrayList<Job>();
    for (int j = 0; (j < CalculateComplexity.PARALLEL_TASKS); j++) {
      {
        Hypergraph _result_1 = hyperedgesOnlyGraph.getResult();
        final ConnectedNodeHyperedgeOnlySizeJob job = new ConnectedNodeHyperedgeOnlySizeJob(("S^#_i " + Integer.valueOf(j)), this, _result_1);
        jobs.add(job);
        job.schedule();
      }
    }
    final TransformationHypergraphSize size = new TransformationHypergraphSize(this.monitor);
    size.setName("Determine Size(S^#)");
    Hypergraph _result_1 = hyperedgesOnlyGraph.getResult();
    size.transform(_result_1);
    final Consumer<Job> _function = (Job it) -> {
      try {
        it.join();
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    jobs.forEach(_function);
    double _complexity = this.complexity;
    Double _result_2 = size.getResult();
    this.complexity = (_complexity - (_result_2).doubleValue());
    return this.complexity;
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
  
  public synchronized double deliverConnectedNodeHyperedgesOnlySizeResult(final double size) {
    double _xblockexpression = (double) 0;
    {
      this.monitor.worked(1);
      double _complexity = this.complexity;
      _xblockexpression = this.complexity = (_complexity + size);
    }
    return _xblockexpression;
  }
}
