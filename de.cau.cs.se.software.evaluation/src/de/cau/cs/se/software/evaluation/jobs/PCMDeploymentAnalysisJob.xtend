package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.resources.IFile
import org.eclipse.swt.widgets.Shell
import de.cau.cs.se.software.evaluation.views.NamedValue
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl
import org.eclipse.emf.ecore.resource.Resource
import de.cau.cs.se.software.evaluation.views.ResultModelProvider
import org.eclipse.emf.common.util.URI
import org.palladiosimulator.pcm.system.System
import de.cau.cs.se.software.evaluation.transformation.pcm.TransformationPCMDeploymentToHypergraph
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.core.runtime.Status

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
			val result = ResultModelProvider.INSTANCE
			
			val model = source.contents.get(0) as System
			
			val deploymentModel = new TransformationPCMDeploymentToHypergraph(monitor)
			deploymentModel.generate(model)
			
			result.values.add(new NamedValue(project.name, "number of modules", deploymentModel.result.modules.size))
			result.values.add(new NamedValue(project.name, "number of nodes", deploymentModel.result.nodes.size))
			result.values.add(new NamedValue(project.name, "number of edges", deploymentModel.result.edges.size))
			
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