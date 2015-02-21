/**
 *
 */
package de.cau.cs.se.evaluation.architecture.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.cau.cs.se.evaluation.architecture.jobs.ComplexityAnalysisJob;
import de.cau.cs.se.evaluation.architecture.views.AnalysisResultView;

/**
 * @author rju
 *
 */
public class AnalysisComplexityHandler extends AbstractHandler implements IHandler {

	/**
	 * This routine checks if there is an active selection.
	 * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final IWorkbenchPage activePage = window.getActivePage();
		final ISelection selection = activePage.getSelection();
		if (selection != null) {
			final Job job = new ComplexityAnalysisJob(selection);
			job.schedule();
			try {
				activePage.showView(AnalysisResultView.ID);
			} catch (final PartInitException e) {
				throw new ExecutionException("View initialization failed.", e);
			}
		}

		return null;
	}

}
