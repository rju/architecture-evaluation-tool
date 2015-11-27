package de.cau.cs.se.software.evaluation.jobs

import com.google.inject.Inject
import de.cau.cs.se.geco.architecture.ArchitectureStandaloneSetup
import de.cau.cs.se.geco.architecture.architecture.GecoModel
import de.cau.cs.se.software.evaluation.transformation.geco.TransformationGecoMegamodelToHypergraph
import de.cau.cs.se.software.evaluation.views.ResultModelProvider
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.widgets.Shell
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet
import de.cau.cs.se.software.evaluation.views.NamedValue

/**
 * run job for the GECO megamodel evaluation.
 */
class GecoMegamodelAnalysisJob extends AbstractHypergraphAnalysisJob {
	
	val IFile file
	
	/** resource set for the compilation. */
	@Inject
	XtextResourceSet resourceSet
	
	Shell shell
		
	new(IProject project, IFile file, Shell shell) {
		super(project)
		this.file = file
		this.shell = shell
	}
	
	override protected run(IProgressMonitor monitor) {
		val injector = new ArchitectureStandaloneSetup().createInjectorAndDoEMFRegistration()
		injector.injectMembers(this);
		this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		
		val Resource source = resourceSet.getResource(URI.createPlatformResourceURI(file.fullPath.toString, true), true)
		
		if (source.contents.size > 0) {
			val result = ResultModelProvider.INSTANCE
			
			val model = source.contents.get(0) as GecoModel
			
			val gecoMegamodel = new TransformationGecoMegamodelToHypergraph(monitor)
			gecoMegamodel.generate(model)
			
			result.values.add(new NamedValue(project.name, "number of nodes", gecoMegamodel.result.nodes.size))
			result.values.add(new NamedValue(project.name, "number of edges", gecoMegamodel.result.edges.size))
			
			calculateSize(gecoMegamodel.result, monitor, result)
		
			calculateComplexity(gecoMegamodel.result, monitor, result)			
		} else {
			MessageDialog.openError(this.shell, "Model empty", "The selected resource is empty.")
		}
								
		monitor.done()
							
		return Status.OK_STATUS	
	}


	
	
}