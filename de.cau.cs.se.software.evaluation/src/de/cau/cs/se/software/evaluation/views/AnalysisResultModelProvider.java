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
package de.cau.cs.se.software.evaluation.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;

/**
 * Enumeration used as singleton for the result model provider.
 *
 * @author Reiner Jung
 *
 */
public enum AnalysisResultModelProvider {
	INSTANCE;

	private final List<NamedValue> values;
	private Hypergraph hypergraph;
	private IProject project;

	private AnalysisResultModelProvider() {
		this.values = new ArrayList<>();
	}

	/**
	 * Clear all values stored in the model.
	 */
	public void clearValues() {
		this.values.clear();
	}

	public void addResult(final String project, final String label, final Double value) {
		this.values.add(new NamedValue(project, label, String.valueOf(value)));
	}

	public void addResult(final String project, final String label, final int value) {
		this.values.add(new NamedValue(project, label, String.valueOf(value)));
	}

	public void addResult(final String project, final String label, final long value) {
		this.values.add(new NamedValue(project, label, String.valueOf(value)));
	}

	public void setResultHypergraph(final Hypergraph hypergraph) {
		this.hypergraph = hypergraph;
	}

	public Hypergraph getResultHypergraph() {
		return this.hypergraph;
	}

	public List<NamedValue> getValues() {
		return this.values;
	}

	public IProject getProject() {
		return this.project;
	}

	public void setProject(final IProject project) {
		this.project = project;
	}

}
