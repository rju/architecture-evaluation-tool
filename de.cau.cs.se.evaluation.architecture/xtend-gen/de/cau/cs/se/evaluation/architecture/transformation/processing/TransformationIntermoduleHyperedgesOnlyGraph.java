package de.cau.cs.se.evaluation.architecture.transformation.processing;

import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation;

@SuppressWarnings("all")
public class TransformationIntermoduleHyperedgesOnlyGraph implements ITransformation {
  private final ModularHypergraph hypergraph;
  
  private ModularHypergraph resultHypergraph;
  
  public TransformationIntermoduleHyperedgesOnlyGraph(final ModularHypergraph hypergraph) {
    this.hypergraph = hypergraph;
  }
  
  public ModularHypergraph getIntermoduleHyperedgesOnlyGraph() {
    return this.resultHypergraph;
  }
  
  public void transform() {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.resultHypergraph = _createModularHypergraph;
  }
}
