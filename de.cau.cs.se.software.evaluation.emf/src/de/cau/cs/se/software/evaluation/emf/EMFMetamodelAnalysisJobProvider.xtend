package de.cau.cs.se.software.evaluation.emf

import de.cau.cs.se.software.evaluation.jobs.IAnalysisJobProvider
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import org.eclipse.swt.widgets.Shell

class EMFMetamodelAnalysisJobProvider implements IAnalysisJobProvider {
	
	override createAnalysisJob(IProject project, IFile file, Shell shell) {
		return new EMFMetamodelAnalysisJob(project, file, shell)
	}
	
	override getFileExtension() { "ecore" }
	
}