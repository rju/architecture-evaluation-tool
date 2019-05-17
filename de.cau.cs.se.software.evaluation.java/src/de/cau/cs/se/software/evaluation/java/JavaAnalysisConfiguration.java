/***************************************************************************
 * Copyright (C) 2019 Software Engineering Group, Kiel University
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

import org.eclipse.core.resources.IFile;

/**
 * @author Reiner Jung
 *
 */
public class JavaAnalysisConfiguration {

	public static final String DATA_TYPE_PATTERN_FILENAME = "data-type-pattern.cfg";

	public static final String OBSERVED_SYSTEM_PATTERN_FILENAME = "observed-system.cfg";

	private IFile dataTypePattern;

	private IFile observedSystemPatternFile;

	/** return relative or absolute file path. */
	public IFile getDataTypePatternFile() {
		return this.dataTypePattern;
	}

	public void setDataTypePatternFile(final IFile dataTypePatternFile) {
		this.dataTypePattern = dataTypePatternFile;
	}

	/** return relative or absolute file path. */
	public IFile getObservedSystemPatternFile() {
		return this.observedSystemPatternFile;
	}

	public void setObservedSystemPatternFile(final IFile observedSystemPatternFile) {
		this.observedSystemPatternFile = observedSystemPatternFile;
	}

}
