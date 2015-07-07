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
package de.cau.cs.se.evaluation.architecture.transformation.metrics;

/**
 * Data type class used to store name value pairs in a list. This is
 * used to populate the view of the analysis result view.
 *
 * @author Reiner Jung
 *
 */
public class NamedValue {
	private final String projectName;
	private final String propertyName;
	private final double value;

	/**
	 * Create one new name value pair.
	 *
	 * @param projectName
	 *            the name to be displayed in the table as project name
	 * @param propertyName
	 *            the name to be displayed in the table as property name
	 * @param value
	 *            the value associated for this label
	 */
	public NamedValue(final String projectName, final String propertyName, final double value) {
		this.projectName = projectName;
		this.propertyName = propertyName;
		this.value = value;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public double getValue() {
		return this.value;
	}

}
