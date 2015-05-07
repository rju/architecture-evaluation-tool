package de.cau.cs.se.evaluation.architecture.jobs

import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationConnectedNodeHyperedgesOnlyGraph
import org.eclipse.core.runtime.Status
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node

class CalculationSubJob extends Job {
		
	ComplexityAnalysisJob parent
	
	Hypergraph input
		
	new(String name, ComplexityAnalysisJob parent, Hypergraph input) {
		super(name)
		this.parent = parent
		this.input = input
	}
	
	override protected run(IProgressMonitor monitor) {
		var Node node 
		while ((node = parent.getNextConnectedNodeTask) != null) {
			// S^#_i
			val transformationConnectedNodeHyperedgesOnlyGraph = 
				new TransformationConnectedNodeHyperedgesOnlyGraph(input)
			
			transformationConnectedNodeHyperedgesOnlyGraph.node = node
			transformationConnectedNodeHyperedgesOnlyGraph.transform
			parent.deliverResult(transformationConnectedNodeHyperedgesOnlyGraph.result)
		}
		
		return Status.OK_STATUS
	}
	
}