package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.transformation.emf.TransformationEMFInstanceToHypergraph
import de.cau.cs.se.software.evaluation.views.ResultModelProvider
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

class EMFMetamodelAnalysisJob extends AbstractHypergraphAnalysisJob {
	
	val IFile file
		
	Shell shell
		
	new(IProject project, IFile file, Shell shell) {
		super(project)
		this.file = file
		this.shell = shell
	}
	
	override protected run(IProgressMonitor monitor) {
		val resourceSet = new ResourceSetImpl()
		
		val Resource source = resourceSet.getResource(URI.createPlatformResourceURI(file.fullPath.toString, true), true)
		
		if (source.contents.size > 0) {
			val result = ResultModelProvider.INSTANCE
			
			val model = source.contents.get(0) as EPackage
			
			val emfMetaModel = new TransformationEMFInstanceToHypergraph(monitor)
			emfMetaModel.transform(model)
			
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