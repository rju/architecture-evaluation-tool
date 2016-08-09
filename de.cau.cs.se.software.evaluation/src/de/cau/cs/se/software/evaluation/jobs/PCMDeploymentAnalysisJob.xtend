package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.transformation.pcm.TransformationPCMDeploymentToHypergraph
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.widgets.Shell
import org.palladiosimulator.pcm.system.System

class PCMDeploymentAnalysisJob extends AbstractHypergraphAnalysisJob {
	
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
			val result = AnalysisResultModelProvider.INSTANCE
			
			val model = source.contents.get(0) as System
			
			val deploymentModel = new TransformationPCMDeploymentToHypergraph(monitor)
			deploymentModel.generate(model)
			
			result.addResult(project.name, "number of modules", deploymentModel.result.modules.size)
			result.addResult(project.name, "number of nodes", deploymentModel.result.nodes.size)
			result.addResult(project.name, "number of edges", deploymentModel.result.edges.size)
			
			calculateSize(deploymentModel.result, monitor, result)
		
			calculateComplexity(deploymentModel.result, monitor, result)
			
			calculateCoupling(deploymentModel.result, monitor, result)
			
			calculateCohesion(deploymentModel.result, monitor, result)	
		} else {
			MessageDialog.openError(this.shell, "Model empty", "The selected resource is empty.")
		}
								
		monitor.done()
							
		return Status.OK_STATUS	
	}
	
}