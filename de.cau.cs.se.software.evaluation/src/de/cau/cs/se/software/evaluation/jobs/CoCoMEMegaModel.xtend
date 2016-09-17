/***************************************************************************
 * Copyright (C) 2015 Reiner Jung
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

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import java.util.List

/**
 * This class is only a temporary place for this model. In real
 * we might need a language or at least an EMF editor for that and store
 * such models in real files.
 */
class CoCoMEMegaModel {
	
	val trmInitialNodes = #[
		"TRM_PCM", "TRM_DTL"
	]
	val modelInitialNodes = #["PCM", "Behavior", "DTL", 
		"EJB/Servlet Stubs", "EJB/Servlets", "Snippets", "Entities", 
		"Bean Classes", "Entity Classes"
	]
	val transInitialNodes = #["T_ProtoCom", "T_behavior", "T_DTL",
		"T_JW",
		"T_javac,ajc", "T_javac3"	
	]
	
	val trmCompleteNodes = #[
		"TRM_IRL", "TRM_PCM", "TRM_DTL"
	]
	val modelCompleteNodes = #["IRL", "IAL", "PCM", "Behavior", "DTL", 
		"Kieker Records", "Sensors", "aspect.xml", "EJB/Servlet Stubs", "EJB/Servlets", "Snippets", "Entities", 
		"Record Classes", "Sensor Classes", "web.xml", "Bean Classes", "Entity Classes"
	]
	val transCompleteNodes = #["T_IRL", "T_sensor", "T_web", "T_aspect", "T_ProtoCom", "T_behavior", "T_DTL",
		"T_JW",
		"T_javac1", "T_javac2", "T_javac,ajc", "T_javac3"	
	]
	
	/**
	 * Create Megamodel graph
	 */
	def createMegaModelAnalysis(boolean complete) {
		val Hypergraph graph = HypergraphFactory.eINSTANCE.createHypergraph
		if (complete) {
			graph.makeNodes(modelCompleteNodes)
			graph.makeNodes(transCompleteNodes)
			graph.makeNodes(trmCompleteNodes)	
		} else {
			graph.makeNodes(modelInitialNodes)
			graph.makeNodes(transInitialNodes)
			graph.makeNodes(trmInitialNodes)
		}
	
		graph.connectNode("IRL",               "T_IRL")
		graph.connectNode("T_IRL",             "Kieker Records")
		graph.connectNode("T_IRL",             "TRM_IRL")
		graph.connectNode("Kieker Records",    "T_javac1")
		graph.connectNode("TRM_IRL",           "T_sensor")
		graph.connectNode("IAL",               "IRL")
		graph.connectNode("IAL",               "T_sensor")
		graph.connectNode("IAL",               "T_web")
		graph.connectNode("IAL",               "T_aspect")
		graph.connectNode("IAL",               "PCM")
		graph.connectNode("T_sensor",          "Sensors")
		graph.connectNode("T_web",             "web.xml")
		graph.connectNode("web.xml",           "Bean Classes")
		graph.connectNode("Sensor Classes",    "Record Classes")
		graph.connectNode("T_aspect",          "aspect.xml")
		graph.connectNode("Sensors",           "T_javac2")
		graph.connectNode("T_javac2",          "Sensor Classes")
		graph.connectNode("Sensors",           "T_javac,ajc")
		graph.connectNode("aspect.xml",        "T_javac,ajc")
		graph.connectNode("PCM",               "T_ProtoCom")
		graph.connectNode("PCM",               "DTL")
		graph.connectNode("T_ProtoCom",        "TRM_PCM")
		graph.connectNode("T_ProtoCom",        "EJB/Servlet Stubs")
		graph.connectNode("TRM_PCM",           "T_sensor")
		graph.connectNode("TRM_PCM",           "T_web")
		graph.connectNode("TRM_PCM",           "T_aspect")
		graph.connectNode("TRM_PCM",           "T_behavior")
		graph.connectNode("EJB/Servlet Stubs", "T_JW")
		graph.connectNode("T_JW",               "EJB/Servlets")
		graph.connectNode("EJB/Servlets",       "T_javac,ajc")
		graph.connectNode("T_javac,ajc",        "Bean Classes")
		graph.connectNode("Bean Classes",       "Entity Classes")
		graph.connectNode("Behavior",           "T_behavior")
		graph.connectNode("Behavior",           "PCM")
		graph.connectNode("Behavior",           "DTL")
		graph.connectNode("T_behavior",         "Snippets")
		graph.connectNode("Snippets",           "T_JW")
		graph.connectNode("Snippets",           "Entities")
		graph.connectNode("Snippets",           "EJB/Servlet Stubs")
		graph.connectNode("DTL",                "T_DTL")
		graph.connectNode("T_DTL",              "TRM_DTL")
		graph.connectNode("T_DTL",              "Entities")
		graph.connectNode("TRM_DTL",            "T_behavior")
		graph.connectNode("TRM_DTL",            "T_ProtoCom")
		graph.connectNode("Entities",           "T_javac3")
		graph.connectNode("T_javac3",           "Entity Classes")
		
		return graph		
	}
	
	private def void makeNodes(Hypergraph hypergraph, List<String> strings) {
		strings.forEach[name | 
			val node = HypergraphFactory.eINSTANCE.createNode
			node.name = name
			hypergraph.nodes.add(node)
		]
	}
	
	private def connectNode(Hypergraph graph, String sourceNodeName, String targetNodeName) {
		val sourceNode = graph.nodes.findFirst[node | node.name.equals(sourceNodeName)]
		val targetNode = graph.nodes.findFirst[node | node.name.equals(targetNodeName)]
		
		if (sourceNode != null && targetNode != null) {
			val edge = HypergraphFactory.eINSTANCE.createEdge
			edge.name = sourceNodeName + "::" + targetNodeName
			graph.edges.add(edge)
					
			sourceNode.edges.add(edge)
			targetNode.edges.add(edge)
		} else {
			if (sourceNode == null) System.out.println("missing source node " + sourceNodeName)
			if (targetNode == null) System.out.println("missing target node " + targetNodeName)
		}
	}
}