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
package de.cau.cs.se.evaluation.architecture.views;

import de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;

/**
 * Is used to transform a modular hypergraph into a serializable form.
 *
 * @author Yannic Kropp
 *
 */
public class GraphTransformer {

	/**
	 * Empty default constructor. Required by checkstyle.
	 */
	public GraphTransformer() {}

	ModularHypergraph makeSerializable(final ModularHypergraph graph) {
		// fix Modules
		for (final Module module : graph.getModules()) {
			if (module.getDerivedFrom() instanceof TypeTrace) {
				((TypeTrace) module.getDerivedFrom()).setType(null);
			}
		}

		// fix Nodes
		for (final Node node : graph.getNodes()) {
			if (node.getDerivedFrom() instanceof TypeTrace) {
				((TypeTrace) node.getDerivedFrom()).setType(null);
			}
			if (node.getDerivedFrom() instanceof MethodTrace) {
				((MethodTrace) node.getDerivedFrom()).setMethod(null);
			}
		}

		// fix Edges
		for (final Edge edge : graph.getEdges()) {
			if (edge.getDerivedFrom() instanceof FieldTrace) {
				((FieldTrace) edge.getDerivedFrom()).setField(null);
			} else if (edge.getDerivedFrom() instanceof CallerCalleeTrace) {
				((CallerCalleeTrace) edge.getDerivedFrom()).setCaller(null);
				((CallerCalleeTrace) edge.getDerivedFrom()).setCallee(null);
			}
		}

		return graph;

	}

}
