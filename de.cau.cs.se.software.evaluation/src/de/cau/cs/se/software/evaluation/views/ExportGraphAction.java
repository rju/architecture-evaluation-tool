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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * UI action for the export of graph information.
 *
 * @author Reiner Jung
 *
 */
public class ExportGraphAction extends Action {

	private final Shell shell;

	/**
	 * Create a export graph action.
	 *
	 * @param shell
	 *            the shell to be used to display a dialog
	 */
	public ExportGraphAction(final Shell shell) {
		super("Graph Export", UIIcons.ICON_GRAPH_EXPORT);
		this.shell = shell;
	}

	@Override
	public void run() {
		try {
			if (AnalysisResultModelProvider.INSTANCE.getHypergraph() == null) {
				MessageDialog.openWarning(null, "Missing EObject", "No Graph (EObject) found.");
			} else {
				final IProject project = AnalysisResultModelProvider.INSTANCE.getProject();
				final FileDialog dialog = new FileDialog(this.shell, SWT.SAVE);
				dialog.setText("Save");
				if (project != null) {
					dialog.setFilterPath(project.getLocation().toString());
				}
				final String[] filterExt = { "*.xmi", "*.*" };
				dialog.setFilterExtensions(filterExt);
				final String outputFilePath = dialog.open();
				if (outputFilePath != null) {

					if (!outputFilePath.endsWith(".xmi")) {
						outputFilePath.concat(".xmi");
					}

					final ResourceSet resourceSet = new ResourceSetImpl();

					// Register XMI Factory implementation to handle .ecore files
					resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());

					// Create empty resource with the given URI
					final Resource resource = resourceSet.createResource(URI.createURI(outputFilePath));

					// Add model to contents list of the resource
					resource.getContents().add(AnalysisResultModelProvider.INSTANCE.getHypergraph());

					// Save the resource
					final File destination = new File(outputFilePath);
					final FileOutputStream stream = new FileOutputStream(destination);
					resource.save(stream, null);
					stream.close();
					try {
						project.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (final CoreException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (final IOException e) {
			MessageDialog.openError(this.shell,
					"Export Error", "Error exporting hypergraph " + e.getLocalizedMessage());
		}
	}
}
