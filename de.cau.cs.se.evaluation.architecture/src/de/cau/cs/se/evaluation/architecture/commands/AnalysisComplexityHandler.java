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
package de.cau.cs.se.evaluation.architecture.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.cau.cs.se.evaluation.architecture.jobs.CollectInputModelJob;
import de.cau.cs.se.evaluation.architecture.jobs.ComplexityAnalysisJob;
import de.cau.cs.se.evaluation.architecture.views.AnalysisResultView;

/**
 * @author Reiner Jung
 *
 */
public class AnalysisComplexityHandler extends AbstractHandler implements IHandler {

	/**
	 * Empty default constructor, as proposed by checkstyle.
	 */
	public AnalysisComplexityHandler() {
		super();
	}

	/**
	 * This routine checks if there is an active selection.
	 * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final IWorkbenchPage activePage = window.getActivePage();
		final ISelection selection = activePage.getSelection();
		if (selection != null) {
			final CollectInputModelJob collectInputModelJob = new CollectInputModelJob(selection, window.getShell());
			collectInputModelJob.schedule();
			try {
				collectInputModelJob.join();
				if (collectInputModelJob.getResult() == Status.OK_STATUS) {
					final Job job = new ComplexityAnalysisJob(collectInputModelJob.getProject(),
							collectInputModelJob.getClasses(),
							collectInputModelJob.getDataTypePatterns(),
							collectInputModelJob.getObservedSystemPatterns());
					job.schedule();
					try {
						activePage.showView(AnalysisResultView.ID);
					} catch (final PartInitException e) {
						throw new ExecutionException("View initialization failed.", e);
					}
				}
			} catch (final InterruptedException e1) {
				e1.printStackTrace();
			}

		}

		return null;
	}

}
