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
package de.cau.cs.se.software.evaluation.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import de.cau.cs.se.software.evaluation.views.AnalysisResultView;
import de.cau.cs.se.software.evaluation.views.LogView;

/**
 * Abstract analysis handler.
 *
 * @author reiner
 *
 */
public abstract class AbstractAnalysisHandler extends AbstractHandler implements IHandler {

	/**
	 * Execute the given event.
	 *
	 * @param event
	 *            the event to be processed.
	 */
	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		final IWorkbenchPage activePage = window.getActivePage();
		final ISelection selection = activePage.getSelection();
		if (selection != null) {
			this.executeCalculation(selection, activePage, window.getShell());
		}

		return null;
	}

	/**
	 * Calculate the complete execution time of a job.
	 *
	 * @param selection
	 *            the selection which is processed by the job
	 * @param activePage
	 *            the active workbench page
	 * @param shell
	 *            the display shell
	 * @throws ExecutionException
	 *             thrown on any eclipse error
	 */
	protected abstract void executeCalculation(ISelection selection, IWorkbenchPage activePage, Shell shell) throws ExecutionException;

	/**
	 * Start analysis view.
	 *
	 * @param activePage
	 *            the active workbench page
	 * @throws ExecutionException
	 *             thrown on any eclipse error
	 */
	protected void createAnalysisView(final IWorkbenchPage activePage) throws ExecutionException {
		try {
			if (activePage.findView(AnalysisResultView.ID) == null) {
				activePage.showView(AnalysisResultView.ID);
			}
		} catch (final PartInitException e) {
			throw new ExecutionException(AnalysisResultView.ID + "View initialization failed.", e);
		}
	}

	/**
	 * Start log view.
	 *
	 * @param activePage
	 *            the active workbench page
	 * @throws ExecutionException
	 *             thrown on any eclipse error
	 */
	protected void createLogView(final IWorkbenchPage activePage) throws ExecutionException {
		try {
			if (activePage.findView(LogView.ID) == null) {
				activePage.showView(LogView.ID);
			}
		} catch (final PartInitException e) {
			throw new ExecutionException(LogView.ID + "View initialization failed.", e);
		}
	}

}
