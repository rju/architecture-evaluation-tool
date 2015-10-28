package de.cau.cs.se.software.evaluation.graph

import java.util.List
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.hypergraph.Module

class DiagramModelHelper {
	def static addUnique(List<Edge> edges, Edge edge) {
		if (!edges.contains(edge)) {
			edges.add(edge)
		}
	}
	
	def static addUnique(List<Module> modules, Module module) {
		if (!modules.contains(module)) {
			modules.add(module)
		}
	}
	
	def static addAllUnique(List<Module> modules, List<Module> additionalModules) {
		additionalModules.forEach[modules.addUnique(it)]
	}
	
}