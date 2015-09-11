package de.cau.cs.se.software.evaluation.commands

import org.eclipse.jface.viewers.ISelection
import org.eclipse.ui.IWorkbenchPage
import org.eclipse.swt.widgets.Shell
import org.eclipse.core.commands.ExecutionException
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ITreeSelection
import org.eclipse.jface.viewers.TreeSelection
import org.eclipse.core.resources.IFile
import de.cau.cs.se.software.evaluation.jobs.GecoComplexityAnalysisJob

class GecoComplexityAnalysisHandler extends AbstractComplexityAnalysisHandler {
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
				
				val selectedElement = treeSelection.iterator.filter(IFile).findFirst[file | "geco".equals(file.fileExtension)]
					
				if (selectedElement != null) {
					val job = new GecoComplexityAnalysisJob(selectedElement.project, selectedElement, shell)
					job.schedule()
					job.join
					this.createAnalysisView(activePage)
				} else {
					// TODO issue warning: No geco model found.
				}
			}
		}
	}

}