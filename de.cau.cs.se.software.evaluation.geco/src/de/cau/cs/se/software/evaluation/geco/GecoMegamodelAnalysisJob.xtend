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
package de.cau.cs.se.software.evaluation.geco

import com.google.inject.Inject
import de.cau.cs.se.geco.architecture.ArchitectureStandaloneSetup
import de.cau.cs.se.geco.architecture.architecture.GecoModel
import de.cau.cs.se.software.evaluation.geco.transformation.TransformationGecoMegamodelToHypergraph
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob
import de.cau.cs.se.software.evaluation.jobs.IOutputHandler

/**
 * run job for the GECO megamodel evaluation.
 */
class GecoMegamodelAnalysisJob extends AbstractHypergraphAnalysisJob {
	
	val IFile file
	
	/** resource set for the compilation. */
	@Inject
	XtextResourceSet resourceSet
			
	new(IProject project, IFile file, IOutputHandler handler) {
		super(project, handler)
		this.file = file
	}
	
	override protected run(IProgressMonitor monitor) {
		val injector = new ArchitectureStandaloneSetup().createInjectorAndDoEMFRegistration()
		injector.injectMembers(this);
		this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		
		val Resource source = resourceSet.getResource(URI.createPlatformResourceURI(file.fullPath.toString, true), true)
		
		if (source.contents.size > 0) {
			val result = AnalysisResultModelProvider.INSTANCE
			
			val model = source.contents.get(0) as GecoModel
			
			val gecoMegamodel = new TransformationGecoMegamodelToHypergraph(monitor)
			gecoMegamodel.generate(model)
			
			result.addResult(project.name, "number of nodes", gecoMegamodel.result.nodes.size)
			result.addResult(project.name, "number of edges", gecoMegamodel.result.edges.size)
			
			calculateSize(gecoMegamodel.result, monitor, result)
		
			calculateComplexity(gecoMegamodel.result, monitor, result)			
		} else {
			handler.error("Model empty", "The selected resource is empty.")
		}
								
		monitor.done()
							
		return Status.OK_STATUS	
	}
	
}