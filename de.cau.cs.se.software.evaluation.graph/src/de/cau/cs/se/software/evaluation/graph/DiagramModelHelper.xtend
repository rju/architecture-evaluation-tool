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
package de.cau.cs.se.software.evaluation.graph

import java.util.List
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.hypergraph.Module

/**
 * Helper for the diagram model.
 * 
 * @author Reiner Jung
 */
class DiagramModelHelper {

	/**
	 * Add an edge only if it is not already present in the list.
	 * 
	 * @param edges list of all edges
	 * @param edge an edge to be added
	 */
	def static addUnique(List<Edge> edges, Edge edge) {
		if (!edges.contains(edge)) {
			edges.add(edge)
		}
	}

	/**
	 * Add a module only if it is not already present in the list.
	 * 
	 * @param modules list of all modules
	 * @param module a module to be added
	 */	
	def static addUnique(List<Module> modules, Module module) {
		if (!modules.contains(module)) {
			modules.add(module)
		}
	}

	/**
	 * Add modules of a module list to another list of modules, only if they
	 * are not already in the modules. 
	 * 
	 * @param modules list of all modules
	 * @param additionalModules list of additional modules to be added
	 */
	def static addAllUnique(List<Module> modules, List<Module> additionalModules) {
		additionalModules.forEach[modules.addUnique(it)]
	}
	
}