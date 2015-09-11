package de.cau.cs.se.software.evaluation.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.cau.cs.se.software.evaluation.views.AnalysisResultView;

public abstract class AbstractAnalysisHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final IWorkbenchPage activePage = window.getActivePage();
		final ISelection selection = activePage.getSelection();
		if (selection != null) {
			this.executeCalculation(selection, activePage, window.getShell());
		}

		return null;
	}

	abstract protected void executeCalculation(ISelection selection, IWorkbenchPage activePage, Shell shell) throws ExecutionException;

	/**
	 * Start analysis view.
	 *
	 * @param activePage
	 * @throws ExecutionException
	 */
	protected void createAnalysisView(final IWorkbenchPage activePage) throws ExecutionException {
		try {
			activePage.showView(AnalysisResultView.ID);
		} catch (final PartInitException e) {
			throw new ExecutionException("View initialization failed.", e);
		}
	}

}
