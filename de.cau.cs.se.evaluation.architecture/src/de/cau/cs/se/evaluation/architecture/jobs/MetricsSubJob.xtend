package de.cau.cs.se.evaluation.architecture.jobs

import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphMetrics
import org.eclipse.core.runtime.Status
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph

class MetricsSubJob extends Job {
	
	ComplexityAnalysisJob parent
			
	new(String name, ComplexityAnalysisJob parent) {
		super(name)
		this.parent = parent
	}
	
	override protected run(IProgressMonitor monitor) {
		val metrics = new TransformationHypergraphMetrics(monitor)
		var Hypergraph subgraph
		while ((subgraph = parent.getNextSubgraph) != null) {
			metrics.setSystem(subgraph)
			parent.deliverMetricsResult(metrics.calculate)
		}
		
		return Status.OK_STATUS
	}
	
}