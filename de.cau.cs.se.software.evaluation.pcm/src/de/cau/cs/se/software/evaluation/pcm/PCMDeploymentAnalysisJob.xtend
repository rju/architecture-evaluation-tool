/***************************************************************************
 * Copyright (C) 2016 Reiner Jung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package de.cau.cs.se.software.evaluation.pcm

import de.cau.cs.se.software.evaluation.pcm.transformation.TransformationPCMDeploymentToHypergraph
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
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob

class PCMDeploymentAnalysisJob extends AbstractHypergraphAnalysisJob {
	

	IFile file
		
	new(IProject project, IFile file, Shell shell) {
		super(project, shell)
		this.file = file
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