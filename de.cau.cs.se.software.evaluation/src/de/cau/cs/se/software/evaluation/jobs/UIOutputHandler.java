/**
 *
 */
package de.cau.cs.se.software.evaluation.jobs;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

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

}
