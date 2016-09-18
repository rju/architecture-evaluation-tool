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
package de.cau.cs.se.software.evaluation.transformation.metric;

import org.eclipse.core.runtime.IProgressMonitor;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.state.RowPattern;
import de.cau.cs.se.software.evaluation.state.RowPatternTable;
import de.cau.cs.se.software.evaluation.state.StateFactory;

/**
 * Helper functions for pattern table handling.
 *
 * @author Reiner Jung
 *
 */
public final class TransformationHelper {

	/**
	 * Private constructor to indicate helper class.
	 */
	private TransformationHelper() {

	}

	/**
	 * Calculate the row pattern of a node based on its edges.
	 *
	 * @param table
	 *            the complete row table
	 * @param node
	 *            where the pattern is calculated for
	 *
	 * @returns the complete pattern
	 */
	public static void calculateRowPattern(final RowPatternTable table, final Node node) {
		final RowPattern pattern = StateFactory.createRowPattern(table.getColumns());
		pattern.getNodes().add(node);

		int i = 0;
		for (final Edge edge : table.getEdges()) {
			pattern.getPattern()[i] = node.getEdges().contains(edge);
			i++;
		}

		table.getPatterns().add(pattern);
	}

	/**
	 * Find duplicate pattern in the pattern table and merge the pattern rows.
	 *
	 * @param table
	 *            the row table to be modified
	 * @param monitor
	 *            the progress monitor
	 */
	public static void compactPatternTable(final RowPatternTable table, final IProgressMonitor monitor) {
		final int length = table.getPatterns().size();
		monitor.subTask("Compact row patterns " + length);

		final int tick = length * table.getPatterns().get(0).getPattern().length;

		for (int i = 0; i < length; i++) {
			monitor.subTask("Compact row patterns " + i + " of " + length);
			monitor.worked(tick);
			if (!monitor.isCanceled()) {
				for (int j = i + 1; j < table.getPatterns().size(); j++) {
					final RowPattern leftPattern = table.getPatterns().get(i);
					final RowPattern rightPattern = table.getPatterns().get(j);
					if (TransformationHelper.matchPattern(leftPattern.getPattern(), rightPattern.getPattern())) {
						leftPattern.getNodes().addAll(rightPattern.getNodes());
						table.getPatterns().remove(j);
						j--; // NOCS
					}
				}
			}
		}

		monitor.worked((length - table.getPatterns().size()) * tick);
	}

	/**
	 * Return true if both lists contain the same values in the list.
	 */
	private static boolean matchPattern(final boolean[] leftList, final boolean[] rightList) {
		if (leftList.length != rightList.length) {
			return false;
		}
		for (int i = 0; i < leftList.length; i++) {
			if (leftList[i] != rightList[i]) {
				return false;
			}
		}

		return true;
	}

}
