package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import org.eclipse.swt.widgets.Shell

class CoCoMEAnalysisJob extends AbstractHypergraphAnalysisJob {
	
	val Shell shell
	
	val boolean complete
	
	new(IProject project, boolean complete, Shell shell) {
		super(project)
		this.shell = shell
		this.complete = complete
	}
	
	override protected run(IProgressMonitor monitor) {
		val result = AnalysisResultModelProvider.INSTANCE
		
		val generator = new CoCoMEMegaModel()
		val graph = generator.createMegaModelAnalysis(complete)
		
		result.addResult(project.name, "number of nodes", graph.nodes.size)
		result.addResult(project.name, "number of edges", graph.edges.size)
		result.resultHypergraph = graph
		
		updateView()
		
		calculateSize(graph, monitor, result)
		
		calculateComplexity(graph, monitor, result)
		
		monitor.done()
							
		return Status.OK_STATUS	
	}
	
}