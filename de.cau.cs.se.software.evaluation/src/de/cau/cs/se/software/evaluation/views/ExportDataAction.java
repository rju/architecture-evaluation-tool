/***************************************************************************
 * Copyright (C) 2015 Reiner Jung
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

/**
 * UI action which exports the result list data.
 *
 * @author Reiner Jung
 *
 */
public class ExportDataAction extends Action {

	private final Shell shell;

	/**
	 * Create the export data action.
	 *
	 * @param shell
	 *            the shell to present UI elements
	 */
	public ExportDataAction(final Shell shell) {
		super("Data Export", UIIcons.ICON_DATA_EXPORT);
		this.shell = shell;
	}

	@Override
	public void run() {
		try {
			if (AnalysisResultModelProvider.INSTANCE.getValues().size() == 0) {
				MessageDialog.openWarning(this.shell, "Missing values", "There are values to export.");
			} else {
				final IProject project = AnalysisResultModelProvider.INSTANCE.getProject();
				final FileDialog dialog = new FileDialog(this.shell, SWT.SAVE);
				dialog.setText("Save");
				if (project != null) {
					dialog.setFilterPath(project.getLocation().toString());
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
						project.refreshLocal(IResource.DEPTH_INFINITE, null);
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
