package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Node;

@SuppressWarnings("all")
public interface ICalculationTask {
  public abstract Node getNextConnectedNodeTask();
  
  public abstract void deliverConnectedNodeHyperedgesOnlySizeResult(final double size);
}
