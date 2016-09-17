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
package de.cau.cs.se.software.evaluation.java;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

import de.cau.cs.se.software.evaluation.commands.AbstractAnalysisHandler;

/**
 * Handler for the java project analysis running the Java
 * analysis job.
 *
 * @author Reiner Jung
 *
 */
public class JavaProjectAnalysisHandler extends AbstractAnalysisHandler implements IHandler {

	/**
	 * Empty default constructor, as proposed by checkstyle.
	 */
	public JavaProjectAnalysisHandler() {
		super();
	}

	@Override
	protected void executeCalculation(final ISelection selection, final IWorkbenchPage activePage, final Shell shell) throws ExecutionException {
		final IProject project = this.findProject(selection);
		if (project != null) {
			final Job job = new JavaProjectAnalysisJob(project, shell);
			job.schedule();
		}

		this.createAnalysisView(activePage);
		this.createLogView(activePage);
	}

	/**
	 * Find java project.
	 *
	 * @param selection
	 *            the selected Java project
	 * @param monitor
	 *            the progress monitor
	 */
	private IProject findProject(final ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			if (selection instanceof ITreeSelection) {
				final TreeSelection treeSelection = (TreeSelection) selection;

				while (treeSelection.iterator().hasNext()) {
					final Object selectedElement = treeSelection.iterator().next();
					if (selectedElement instanceof IProject) {
						return (IProject) selectedElement;
					} else if (selectedElement instanceof IJavaProject) {
						return ((IJavaProject) selectedElement).getProject();
					}
				}
			}
		}
		return null;
	}
}
