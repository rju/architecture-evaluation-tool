/***************************************************************************
 * Copyright (C) 2016 Reiner Jung
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
 package de.cau.cs.se.software.evaluation.jobs

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.swt.widgets.Shell

/**
 * Extension point interface for analysis job provider.
 * A job provider must provide a file extension to indicate
 * which kind of file its job can process and an analysis
 * job which does the acutal work.
 * 
 * @author Reiner Jung
 */
interface IAnalysisJobProvider {
	/**
	 * Returns the file extension the job is designed for.
	 */
	def String getFileExtension()

	/**
	 * Creates an instantiated and configured analysis job.
	 * 
	 * @param project the project where the file belong to
	 * @param file the file containing the model or information where to find the model
	 * @param shell refers to the display which is required to inform the user on the progress
	 * 
	 * @return returns an analysis job
	 */
	def AbstractHypergraphAnalysisJob createAnalysisJob(IProject project, IFile file, Shell shell)
}