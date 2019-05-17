/**
 *
 */
package de.cau.cs.se.software.evaluation.jobs;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import de.cau.cs.se.software.evaluation.views.AnalysisResultView;
import de.cau.cs.se.software.evaluation.views.LogView;

/**
 * @author Reiner Jung
 *
 */
public class UIOutputHandler implements IOutputHandler {

	private final Shell shell;

	public UIOutputHandler(final Shell shell) {
		this.shell = shell;
	}

	@Override
	public void error(final String header, final String message) {
		PlatformUI.getWorkbench().getDisplay().syncExec(() -> {
			MessageDialog.openError(UIOutputHandler.this.shell, header, message);
		});
	}

	@Override
	public void updateLogView() {
		PlatformUI.getWorkbench().getDisplay().syncExec(() -> {
			final IViewPart part2 = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(LogView.ID);
			((LogView) part2).update();
		});
	}

	@Override
	public void updateResultView() {
		PlatformUI.getWorkbench().getDisplay().syncExec(() -> {
			final IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(AnalysisResultView.ID);
			if (part != null) {
				((AnalysisResultView) part).update();
			} else {
				// TODO this is for debugging only???
				System.out.println("Analysis result view is not open; could not update.");
			}
		});
	}
}
