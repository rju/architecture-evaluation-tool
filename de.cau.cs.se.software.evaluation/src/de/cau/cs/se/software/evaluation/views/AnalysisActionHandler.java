/***************************************************************************
 * Copyright 2015
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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;

/**
 * Handles Actions for Buttons in AnalysisResultView.
 *
 * @author Yannic Kropp
 *
 */
class AnalysisActionHandler {

	/**
	 * Default constructor.
	 */
	public AnalysisActionHandler() {
		super();
	}

	/**
	 * Action-Logic for 'export_Data'-Button in AnalysisResultView.
	 */
	protected void exportData(final TableViewer table, final Shell shell, final IProject project) throws IOException {
		if (table.getTable().getItems().length == 0) {
			MessageDialog.openWarning(null, "Missing values", "There is nothing to export.");
		} else {
			final FileDialog dialog = new FileDialog(shell, SWT.SAVE);
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
				for (final TableItem element : table.getTable().getItems()) {
					@SuppressWarnings("unchecked")
					final NamedValue<Double> data = (NamedValue<Double>) (element.getData());
					sb.append(data.getProjectName() + ";" + data.getPropertyName() + ";" + data.getValue() + "\n");
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
	}

	/**
	 * Action-Logic for 'export_Graph'-Button in AnalysisResultView.
	 */
	protected void exportGraph(final Hypergraph model, final Shell shell, final IProject project) throws IOException {

		if (model == null) {
			MessageDialog.openWarning(null, "Missing EObject", "No Graph (EObject) found.");
		} else {

			final FileDialog dialog = new FileDialog(shell, SWT.SAVE);
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
				resource.getContents().add(model);

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
	}

	/**
	 * Action-Logic for 'visualize_Graph'-Button in AnalysisResultView.
	 */
	protected void visualize() {
		MessageDialog.openWarning(null, "Not implemented", "Not implemented yet.");
	}

	/**
	 * Delete the data in the view.
	 */
	protected void clearViewData(final AnalysisResultView view) {
		AnalysisResultModelProvider.INSTANCE.clearValues();
		view.update();
	}

}
