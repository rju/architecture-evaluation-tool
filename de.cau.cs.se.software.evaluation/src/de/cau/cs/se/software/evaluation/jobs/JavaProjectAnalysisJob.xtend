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
import de.cau.cs.se.software.evaluation.transformation.TransformationLinesOfCode
import de.cau.cs.se.software.evaluation.transformation.TransformationCyclomaticComplexity

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
		
		val linesOfCodeMetric = new TransformationLinesOfCode(monitor)
		linesOfCodeMetric.generate(this.classes)
		val javaMethodComplexity = new TransformationCyclomaticComplexity(monitor)
		javaMethodComplexity.generate(this.classes)
		
		result.values.add(new NamedValue(project.project.name, "size of observed system", this.classes.size))
		result.values.add(new NamedValue(project.project.name, "lines of code (LOC)", linesOfCodeMetric.result))
		for (var i=1;i<javaMethodComplexity.result.size;i++) {
			result.values.add(new NamedValue(project.project.name, 
				"cyclomatic complexity bucket " + i,
				javaMethodComplexity.result.get(i)))
		}
		updateView(null)
				
		val inputHypergraph = createHypergraphForJavaProject(monitor, result)

		calculateSize(inputHypergraph, monitor, result)
		
		calculateComplexity(inputHypergraph, monitor, result)
		
		calculateCoupling(inputHypergraph, monitor, result)
		
		calculateCohesion(inputHypergraph, monitor, result)
				
		monitor.done()
							
		return Status.OK_STATUS
	}
	
	/**
	 * Construct hypergraph from java project
	 */
	def createHypergraphForJavaProject(IProgressMonitor monitor, ResultModelProvider result) {
		/** construct analysis. */				
		val javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(javaProject, dataTypePatterns, observedSystemPatterns, monitor)
		
		javaToModularHypergraph.generate(this.classes)
		
		result.values.add(new NamedValue(project.name, "number of modules", javaToModularHypergraph.result.modules.size))
		result.values.add(new NamedValue(project.name, "number of nodes", javaToModularHypergraph.result.nodes.size))
		result.values.add(new NamedValue(project.name, "number of edges", javaToModularHypergraph.result.edges.size))
		
		updateView(javaToModularHypergraph.result)
		
		return javaToModularHypergraph.result
	}
}