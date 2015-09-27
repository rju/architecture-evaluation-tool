package de.cau.cs.se.software.evaluation.commands

import org.eclipse.jface.viewers.ISelection
import org.eclipse.ui.IWorkbenchPage
import org.eclipse.swt.widgets.Shell
import org.eclipse.core.commands.ExecutionException
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ITreeSelection
import org.eclipse.jface.viewers.TreeSelection
import org.eclipse.core.resources.IFile
import org.eclipse.jface.dialogs.MessageDialog
import de.cau.cs.se.software.evaluation.jobs.GecoMegamodelAnalysisJob
import de.cau.cs.se.software.evaluation.jobs.EMFMetamodelAnalysisJob
import de.cau.cs.se.software.evaluation.jobs.CoCoMEAnalysisJob

class ModelAnalysisHandler extends AbstractAnalysisHandler {

	/**
	 * Empty default constructor, as proposed by checkstyle.
	 */
	public new() {
		super()
	}
	
	override protected executeCalculation(ISelection selection, IWorkbenchPage activePage, Shell shell) throws ExecutionException {
		if (selection instanceof IStructuredSelection) {
			if (selection instanceof ITreeSelection) {
				val TreeSelection treeSelection = selection as TreeSelection
				
				val iterator = treeSelection.iterator.filter(IFile)
				if (iterator.hasNext) {
					val file = iterator.next
					switch(file.fileExtension) {
						case "geco" : {
							val job = new GecoMegamodelAnalysisJob(file.project, file, shell)
							job.schedule()
							job.join
							this.createAnalysisView(activePage)
						}
						case "ecore" : {
							val job = new EMFMetamodelAnalysisJob(file.project, file, shell)
							job.schedule()
							job.join
							this.createAnalysisView(activePage)
						}
						case "cocome" : {
							val job = new CoCoMEAnalysisJob(file.project, file.name.equals("megamodel.cocome"), shell)
							job.schedule()
							job.join
							this.createAnalysisView(activePage)
						}
						default:
							MessageDialog.openInformation(shell, "Unknown Model Type", 
								"The model type implied by the extension " + file.fileExtension + 
								" is not supported.")
					}
				} else {
					MessageDialog.openInformation(shell, "Empty selection", "No model selected for execution.")
				}
			}
		}
	}

}