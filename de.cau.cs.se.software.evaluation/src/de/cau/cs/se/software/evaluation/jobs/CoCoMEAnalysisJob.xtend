package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.swt.widgets.Shell
import org.eclipse.core.runtime.Status
import de.cau.cs.se.software.evaluation.views.ResultModelProvider
import de.cau.cs.se.software.evaluation.views.NamedValue

class CoCoMEAnalysisJob extends AbstractHypergraphAnalysisJob {
	
	val Shell shell
	
	val boolean complete
	
	new(IProject project, boolean complete, Shell shell) {
		super(project)
		this.shell = shell
		this.complete = complete
	}
	
	override protected run(IProgressMonitor monitor) {
		val result = ResultModelProvider.INSTANCE
		
		val generator = new CoCoMEMegaModel()
		val graph = generator.createMegaModelAnalysis(complete)
		
		result.values.add(new NamedValue(project.name, "number of nodes", graph.nodes.size))
		result.values.add(new NamedValue(project.name, "number of edges", graph.edges.size))
		
		updateView(graph)
		
		calculateSize(graph, monitor, result)
		
		calculateComplexity(graph, monitor, result)
		
		monitor.done()
							
		return Status.OK_STATUS	
	}
	
}