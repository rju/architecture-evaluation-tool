package de.cau.cs.se.software.evaluation.views;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class ExportDataAction extends Action {

	private final Shell shell;
	private final IProject project;

	public ExportDataAction(final Shell shell, final IProject project) {
		super("Data Export", UIIcons.ICON_DATA_EXPORT);
		this.shell = shell;
		this.project = project;
	}

	@Override
	public void run() {
		try {
			if (AnalysisResultModelProvider.INSTANCE.getValues().size() == 0) {
				MessageDialog.openWarning(this.shell, "Missing values", "There are values to export.");
			} else {
				final FileDialog dialog = new FileDialog(this.shell, SWT.SAVE);
				dialog.setText("Save");
				if (this.project != null) {
					dialog.setFilterPath(this.project.getLocation().toString());
				}
				final String[] filterExt = { "*.csv", "*.*" };
				dialog.setFilterExtensions(filterExt);
				final String outputFilePath = dialog.open();
				if (outputFilePath != null) {
					if (!outputFilePath.endsWith(".csv")) {
						outputFilePath.concat(".csv");
					}

					final File result = new File(outputFilePath);
					final BufferedWriter br = new BufferedWriter(new FileWriter(result));
					final StringBuilder sb = new StringBuilder();
					for (final NamedValue element : AnalysisResultModelProvider.INSTANCE.getValues()) {
						sb.append(element.getProjectName() + ";" + element.getPropertyName() + ";" + element.getValue() + "\n");
					}
					br.write(sb.toString());
					br.close();
					try {
						this.project.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (final CoreException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (final IOException e) {
			MessageDialog.openError(this.shell, "Export Error", "Error exporting data set " + e.getLocalizedMessage());
		}

	}
}
