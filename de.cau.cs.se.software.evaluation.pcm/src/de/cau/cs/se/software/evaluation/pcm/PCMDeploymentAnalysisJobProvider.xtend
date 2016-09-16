package de.cau.cs.se.software.evaluation.pcm

import de.cau.cs.se.software.evaluation.jobs.IAnalysisJobProvider
import org.eclipse.core.resources.IProject
import org.eclipse.core.resources.IFile
import org.eclipse.swt.widgets.Shell

class PCMDeploymentAnalysisJobProvider implements IAnalysisJobProvider {
	
	override createAnalysisJob(IProject project, IFile file, Shell shell) {
		return new PCMDeploymentAnalysisJob(project, file, shell)
	}
	
	override getFileExtension() { "system" }
	
}