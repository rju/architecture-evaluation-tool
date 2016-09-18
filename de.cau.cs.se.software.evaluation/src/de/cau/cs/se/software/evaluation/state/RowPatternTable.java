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
package de.cau.cs.se.software.evaluation.state;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;

public class RowPatternTable {

	private final Edge[] edges;
	private final List<RowPattern> patterns;

	public RowPatternTable(final int columns, final int rows) {
		this.edges = new Edge[columns];
		this.patterns = new ArrayList<>(rows);
	}

	public int getColumns() {
		return this.edges.length;
	}

	public Edge[] getEdges() {
		return this.edges;
	}

	public List<RowPattern> getPatterns() {
		return this.patterns;
	}

	public void setAllEdges(final EList<Edge> list) {
		for (int i = 0; i < this.edges.length; i++) {
			this.edges[i] = list.get(i);
		}
	}

}
