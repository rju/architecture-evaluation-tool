package de.cau.cs.se.software.evaluation.emf

import de.cau.cs.se.software.evaluation.emf.transformation.TransformationEMFInstanceToHypergraph
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.widgets.Shell
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob

class EMFMetamodelAnalysisJob extends AbstractHypergraphAnalysisJob {
	
	val IFile file
		
	Shell shell
		
	new(IProject project, IFile file, Shell shell) {
		super(project, shell)
		this.file = file
	}
				
	override protected run(IProgressMonitor monitor) {
		val resourceSet = new ResourceSetImpl()
		
		val Resource source = resourceSet.getResource(URI.createPlatformResourceURI(file.fullPath.toString, true), true)
		
		if (source.contents.size > 0) {
			val result = AnalysisResultModelProvider.INSTANCE
			
			val model = source.contents.get(0) as EPackage
			
			val emfMetaModel = new TransformationEMFInstanceToHypergraph(monitor)
			emfMetaModel.generate(model)
			
			result.addResult(project.name, "number of modules", emfMetaModel.result.modules.size)
			result.addResult(project.name, "number of nodes", emfMetaModel.result.nodes.size)
			result.addResult(project.name, "number of edges", emfMetaModel.result.edges.size)
			
			calculateSize(emfMetaModel.result, monitor, result)
		
			calculateComplexity(emfMetaModel.result, monitor, result)
			
			calculateCoupling(emfMetaModel.result, monitor, result)
			
			calculateCohesion(emfMetaModel.result, monitor, result)	
		} else {
			MessageDialog.openError(this.shell, "Model empty", "The selected resource is empty.")
		}
								
		monitor.done()
							
		return Status.OK_STATUS	
	}
	
}