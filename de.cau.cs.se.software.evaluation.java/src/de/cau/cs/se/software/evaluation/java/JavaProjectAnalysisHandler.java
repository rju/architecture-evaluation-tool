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
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

import de.cau.cs.se.software.evaluation.commands.AbstractAnalysisHandler;
import de.cau.cs.se.software.evaluation.jobs.IOutputHandler;
import de.cau.cs.se.software.evaluation.jobs.UIOutputHandler;

/**
 * Handler for the java project analysis running the Java
 * analysis job.
 *
 * @author Reiner Jung
 *
 */
public class JavaProjectAnalysisHandler extends AbstractAnalysisHandler implements IHandler {

	private final String DATA_TYPE_PATTERN_TITLE = "data type pattern";

	private final String OBSERVED_SYSTEM_TITLE = "observed system";

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
			final IOutputHandler handler = new UIOutputHandler(shell);
			final JavaAnalysisConfiguration configuration = new JavaAnalysisConfiguration();
			configuration.setDataTypePatternFile(
					this.getPatternFile(project, JavaAnalysisConfiguration.DATA_TYPE_PATTERN_FILENAME, this.DATA_TYPE_PATTERN_TITLE, handler));
			configuration.setObservedSystemPatternFile(
					this.getPatternFile(project, JavaAnalysisConfiguration.OBSERVED_SYSTEM_PATTERN_FILENAME, this.OBSERVED_SYSTEM_TITLE, handler));

			try {
				final IJavaProject javaProject = this.getJavaProject(project);
				if (javaProject != null) {
					final Job job = new JavaProjectAnalysisJob(configuration, javaProject, handler);
					job.schedule();

					this.createAnalysisView(activePage);
					this.createLogView(activePage);
				} else {
					handler.error("Project Error", "Project is not a Java project.");
				}
			} catch (final CoreException e) {
				handler.error("Project Error", "Could not open project. Cause: " + e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Get Java project of a project.
	 *
	 * @throws CoreException
	 *             on error
	 */
	private IJavaProject getJavaProject(final IProject project) throws CoreException {
		if (project.hasNature(JavaCore.NATURE_ID)) {
			return JavaCore.create(project);
		} else {
			return null;
		}
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

	/**
	 * Get data type patterns.
	 *
	 * @param project
	 *            the project containing the data-type-pattern.cfg
	 * @param handler
	 */
	private IFile getPatternFile(final IProject project, final String filename, final String title, final IOutputHandler handler) {
		final IFile patternFile = (IFile) project.findMember(filename);
		if (patternFile != null) {
			if (patternFile.isSynchronized(1)) {
				return patternFile;
			}
		}

		handler.error("Configuration Error", String.format("The %s file (%s) containing class name patterns is missing.", title, filename));

		return null;
	}
}
