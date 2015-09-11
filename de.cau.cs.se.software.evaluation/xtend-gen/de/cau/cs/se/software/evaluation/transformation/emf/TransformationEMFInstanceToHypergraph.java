package de.cau.cs.se.software.evaluation.transformation.emf;

import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EPackage;

@SuppressWarnings("all")
public class TransformationEMFInstanceToHypergraph extends AbstractTransformation<EPackage, ModularHypergraph> {
  public TransformationEMFInstanceToHypergraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public ModularHypergraph transform(final EPackage input) {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.result = _createModularHypergraph;
    return this.result;
  }
}
