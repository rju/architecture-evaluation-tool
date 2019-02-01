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
package de.cau.cs.se.software.evaluation.commands

import de.cau.cs.se.software.evaluation.jobs.IAnalysisJobProvider
import java.util.HashMap
import java.util.Map
import org.eclipse.core.commands.ExecutionException
import org.eclipse.core.resources.IFile
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.Platform
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ITreeSelection
import org.eclipse.jface.viewers.TreeSelection
import org.eclipse.swt.widgets.Shell
import org.eclipse.ui.IWorkbenchPage

/**
 * The handler for model analysis is used to choose the
 * correct analysis job for a specific type of model.
 * The different job providers can be added via Eclipse extension
 * points.
 * 
 * @author Reiner Jung
 */
class ModelAnalysisHandler extends AbstractAnalysisHandler {

	val Map<String,IAnalysisJobProvider> providers = new HashMap<String,IAnalysisJobProvider>

	/**
	 * Initialization constructor.
	 */
	new() {
		super()
	}
	
	private def initializeProviders() {
		val registry = Platform.getExtensionRegistry()
		val extP = registry.extensionPoints.findFirst["HypergraphProvider".equals(it.label)]
		extP.extensions.forEach[println("extension " + it.extensionPointUniqueIdentifier + " " + 
			it.configurationElements.map[it.name + " " + it.attributeNames.map["a:" + it].join(", ")].join('\n')
		)]
  		val config = registry.getConfigurationElementsFor("analysisJob")
	  	try {
	  		config.forEach[element |
	  			val ext = element
	  			if (ext instanceof IAnalysisJobProvider) {
	  				val provider = (ext as IAnalysisJobProvider)
	  				providers.put(provider.fileExtension, provider)
				}
  			]
		} catch (CoreException ex) {
			System.out.println(ex.getMessage())
		}
	}
	
	/**
	 * Execute the analysis for a given selection.
	 * 
	 * @param selection the selected element (model)
	 * @param activePage the workbench page required to trigge the analysis view
	 * @param shell the UI display access.
	 */
	override protected executeCalculation(ISelection selection, IWorkbenchPage activePage, Shell shell) throws ExecutionException {
		if (providers.size == 0) {
			initializeProviders
		}
		if (selection instanceof IStructuredSelection) {
			if (selection instanceof ITreeSelection) {
				val TreeSelection treeSelection = selection as TreeSelection
				
				val iterator = treeSelection.iterator.filter(IFile)
				if (iterator.hasNext) {
					val file = iterator.next
					val provider = providers.get(file.fileExtension)
						
					if (provider !== null) {
						val job = provider.createAnalysisJob(file.project, file, shell)
						job.schedule()
						job.join
						this.createAnalysisView(activePage)
					} else {
						// TODO make this a generic piece
						//case "cocome" : new CoCoMEAnalysisJob(file.project, file.name.equals("megamodel.cocome"), shell)
						MessageDialog.openInformation(shell, "Unknown Model Type", 
							"The model type implied by the extension " + file.fileExtension + 
							" is not supported.")
					}
				} else {
					MessageDialog.openInformation(shell, "Empty selection", "No model selected for execution.")
				}
			}
		}
	}

}
