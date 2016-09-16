package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.ConnectedNodeHyperedgeOnlySizeJob;
import de.cau.cs.se.software.evaluation.jobs.ICalculationTask;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHyperedgesOnlyGraph;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

/**
 * This class is a modified version of CalculateComplexity specifically for
 * the maximal interconnected graph. It generates the complexity of a maximal
 * interconnected graph much faster as it uses the symmetric properties of the
 * maximal interconnected graph to reduce necessary calculations.
 * 
 * @author Reiner Jung
 */
@SuppressWarnings("all")
public class CalculateMaximalInterconnectedGraphComplexity implements ICalculationTask {
  /**
   * Used in the parallelized version of this.
   */
  private volatile Iterator<Node> globalHyperEdgesOnlyGraphNodes;
  
  private volatile double complexity;
  
  private final IProgressMonitor monitor;
  
  public CalculateMaximalInterconnectedGraphComplexity(final IProgressMonitor monitor) {
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
    try {
      final TransformationHyperedgesOnlyGraph hyperedgesOnlyGraph = new TransformationHyperedgesOnlyGraph(this.monitor);
      final TransformationHypergraphSize size = new TransformationHypergraphSize(this.monitor);
      int _workEstimate = hyperedgesOnlyGraph.workEstimate(input);
      int _workEstimate_1 = size.workEstimate(input);
      int _plus = (_workEstimate + _workEstimate_1);
      this.monitor.beginTask(message, _plus);
      hyperedgesOnlyGraph.generate(input);
      int _workEstimate_2 = hyperedgesOnlyGraph.workEstimate(input);
      this.monitor.worked(_workEstimate_2);
      boolean _isCanceled = this.monitor.isCanceled();
      if (_isCanceled) {
        return 0;
      }
      Hypergraph _result = hyperedgesOnlyGraph.getResult();
      EList<Node> _nodes = _result.getNodes();
      Iterator<Node> _iterator = _nodes.iterator();
      this.globalHyperEdgesOnlyGraphNodes = _iterator;
      this.complexity = 0;
      boolean _isCanceled_1 = this.monitor.isCanceled();
      if (_isCanceled_1) {
        return 0;
      }
      Hypergraph _result_1 = hyperedgesOnlyGraph.getResult();
      final ConnectedNodeHyperedgeOnlySizeJob job = new ConnectedNodeHyperedgeOnlySizeJob("S^#_i (once)", this, _result_1);
      job.schedule();
      boolean _isCanceled_2 = this.monitor.isCanceled();
      if (_isCanceled_2) {
        job.cancel();
        return 0.0;
      }
      this.monitor.subTask("Determine Size(S^#)");
      Hypergraph _result_2 = hyperedgesOnlyGraph.getResult();
      size.generate(_result_2);
      boolean _isCanceled_3 = this.monitor.isCanceled();
      if (_isCanceled_3) {
        job.cancel();
        return 0.0;
      }
      job.join();
      double _complexity = this.complexity;
      Double _result_3 = size.getResult();
      int _size = IteratorExtensions.size(this.globalHyperEdgesOnlyGraphNodes);
      double _multiply = ((_result_3).doubleValue() * _size);
      this.complexity = (_complexity - _multiply);
      return this.complexity;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Used for the parallelization. Return the next task
   */
  @Override
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
  
  @Override
  public synchronized void deliverConnectedNodeHyperedgesOnlySizeResult(final double size) {
    double _complexity = this.complexity;
    this.complexity = (_complexity + size);
    this.monitor.worked(1);
  }
}
