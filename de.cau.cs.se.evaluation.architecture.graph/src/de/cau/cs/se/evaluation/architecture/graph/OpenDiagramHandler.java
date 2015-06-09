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
package de.cau.cs.se.evaluation.architecture.graph;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.cau.cs.kieler.klighd.ui.DiagramViewManager;
import de.cau.cs.kieler.klighd.util.KlighdSynthesisProperties;

/**
 * A simple handler for opening diagrams.
 *
 * @author Reiner Jung - initial contribution
 */
public class OpenDiagramHandler extends AbstractHandler {

	/**
	 * Empty constructor with super.
	 */
	public OpenDiagramHandler() {
		super();
	}

	/**
	 * Handle the selection event and trigger the KlighD view.
	 *
	 * @param event
	 *            the event to be processed
	 *
	 * @return null
	 *
	 * @throws ExecutionException
	 *             on error
	 */
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection sSelection = (IStructuredSelection) selection;
			Object model = null;
			if (sSelection.getFirstElement() instanceof IFile) {
				// try {
				final IFile f = (IFile) sSelection.getFirstElement();
				final ResourceSet resourceSet = new ResourceSetImpl();
				resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi",
						new XMIResourceFactoryImpl());

				final URI uri = URI.createPlatformResourceURI(f.getFullPath().toString(), false);
				final Resource resource = resourceSet.getResource(uri, true);
				if (resource.getContents().size() > 0) {
					model = resource.getContents().get(0);
				}
				// } catch (final Exception e) {
				// StatusManager.getManager().handle(
				// new Status(IStatus.ERROR, this.getClass().getCanonicalName(),
				// "Could not load selected file.", e), StatusManager.SHOW);
				// }
			} else {
				model = sSelection.getFirstElement();
			}

			DiagramViewManager.createView("de.cau.cs.se.evaluation.architecture.graph.ModularHypergraphDiagram",
					"ModularHypergraph Diagram", model, KlighdSynthesisProperties.create());
		} else {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Unsupported element",
					"KLighD diagram synthesis is unsupported for the current selection "
							+ selection.toString() + ".");
		}
		return null;
	}
}
