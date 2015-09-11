package de.cau.cs.se.software.evaluation.transformation.emf

import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import org.eclipse.emf.ecore.EPackage
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory

class TransformationEMFInstanceToHypergraph extends AbstractTransformation<EPackage, ModularHypergraph> {
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override transform(EPackage input) {
		result = HypergraphFactory.eINSTANCE.createModularHypergraph
		
		// TODO create hypergraph from model here
		
		return result
	}
	
}