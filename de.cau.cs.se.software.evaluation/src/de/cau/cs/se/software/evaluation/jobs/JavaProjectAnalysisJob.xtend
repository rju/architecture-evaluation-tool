package de.cau.cs.se.software.evaluation.jobs

import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.views.ResultModelProvider
import de.cau.cs.se.software.evaluation.transformation.java.TransformationJavaMethodsToModularHypergraph
import de.cau.cs.se.software.evaluation.views.NamedValue
import java.util.List
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status

class JavaProjectAnalysisJob extends AbstractHypergraphAnalysisJob {
	
	val List<AbstractTypeDeclaration> classes 
					
	val List<String> dataTypePatterns
	
	val List<String> observedSystemPatterns
		
	val IJavaProject javaProject
			
	public new (IJavaProject project, List<AbstractTypeDeclaration> classes,
		List<String> dataTypePatterns, List<String> observedSystemPatterns
	) {
		super(project.project)
		this.javaProject = project
		this.classes = classes
		this.dataTypePatterns = dataTypePatterns
		this.observedSystemPatterns = observedSystemPatterns
	}
	
	/**
	 * Execute all metrics as requested.
	 */
	protected override IStatus run(IProgressMonitor monitor) {
		val result = ResultModelProvider.INSTANCE
		
		result.values.add(new NamedValue(project.project.name, "size of observed system", this.classes.size))
		updateView(null)
				
		val inputHypergraph = createHypergraphForJavaProject(monitor, result)

		calculateSize(inputHypergraph, monitor, result)
		
		calculateComplexity(inputHypergraph, monitor, result)
		
		val coupling = calculateCoupling(inputHypergraph, monitor, result)
		
		calculateCohesion(inputHypergraph, monitor, result, coupling)
				
		monitor.done()
							
		return Status.OK_STATUS
	}
	
	/**
	 * Construct hypergraph from java project
	 */
	def createHypergraphForJavaProject(IProgressMonitor monitor, ResultModelProvider result) {
		/** construct analysis. */				
		val javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(javaProject, dataTypePatterns, observedSystemPatterns, monitor)
		
		javaToModularHypergraph.transform(this.classes)
		
		result.values.add(new NamedValue(project.name, "number of modules", javaToModularHypergraph.result.modules.size))
		result.values.add(new NamedValue(project.name, "number of nodes", javaToModularHypergraph.result.nodes.size))
		result.values.add(new NamedValue(project.name, "number of edges", javaToModularHypergraph.result.edges.size))
		
		updateView(javaToModularHypergraph.result)
		
		return javaToModularHypergraph.result
	}
}