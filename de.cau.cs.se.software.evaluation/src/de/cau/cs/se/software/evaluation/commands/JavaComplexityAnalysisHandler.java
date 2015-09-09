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
package de.cau.cs.se.software.evaluation.commands;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

import de.cau.cs.se.software.evaluation.jobs.CollectInputModelJob;
import de.cau.cs.se.software.evaluation.jobs.JavaComplexityAnalysisJob;

/**
 * @author Reiner Jung
 *
 */
public class JavaComplexityAnalysisHandler extends AbstractComplexityAnalysisHandler implements IHandler {

	/**
	 * Empty default constructor, as proposed by checkstyle.
	 */
	public JavaComplexityAnalysisHandler() {
		super();
	}

	@Override
	protected void executeCalculation(final ISelection selection, final IWorkbenchPage activePage, final Shell shell) throws ExecutionException {
		final CollectInputModelJob collectInputModelJob = new CollectInputModelJob(selection, shell);
		collectInputModelJob.schedule();
		try {
			collectInputModelJob.join();
			if (collectInputModelJob.getResult() == Status.OK_STATUS) {
				final Job job = new JavaComplexityAnalysisJob(collectInputModelJob.getProject(),
						collectInputModelJob.getClasses(),
						collectInputModelJob.getDataTypePatterns(),
						collectInputModelJob.getObservedSystemPatterns());
				job.schedule();
			}
		} catch (final InterruptedException e1) {
			e1.printStackTrace();
		}

		this.createAnalysisView(activePage);
	}

}
