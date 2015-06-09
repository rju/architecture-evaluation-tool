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
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
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

	ModularHypergraph makeSerializable(final ModularHypergraph old) {
		// final HypergraphFactory HgFactory = HypergraphFactory.eINSTANCE;
		final ModularHypergraph result = old;

		// fix Modules
		for (int i = 0; i < result.getModules().size(); i++) {
			if (result.getModules().get(i).getDerivedFrom() instanceof TypeTrace) {
				((TypeTrace) result.getModules().get(i).getDerivedFrom()).setType(null);
			}
		}

		// fix Nodes
		for (int i = 0; i < result.getNodes().size(); i++) {
			if (result.getNodes().get(i).getDerivedFrom() instanceof TypeTrace) {
				((TypeTrace) result.getNodes().get(i).getDerivedFrom()).setType(null);
			}
			if (result.getNodes().get(i).getDerivedFrom() instanceof MethodTrace) {
				((MethodTrace) result.getNodes().get(i).getDerivedFrom()).setMethod(null);
			}
		}

		// fix Edges
		for (int i = 0; i < result.getEdges().size(); i++) {
			if (result.getEdges().get(i).getDerivedFrom() instanceof FieldTrace) {
				((FieldTrace) result.getEdges().get(i).getDerivedFrom()).setField(null);
			}
			if (result.getEdges().get(i).getDerivedFrom() instanceof CallerCalleeTrace) {
				((CallerCalleeTrace) result.getEdges().get(i).getDerivedFrom()).setCaller(null);
				((CallerCalleeTrace) result.getEdges().get(i).getDerivedFrom()).setCallee(null);
			}
		}

		return result;

	}

}
