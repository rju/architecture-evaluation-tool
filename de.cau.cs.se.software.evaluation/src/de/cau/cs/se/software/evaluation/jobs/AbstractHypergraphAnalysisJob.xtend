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
package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphToGraphMapping
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationIntermoduleHyperedgesOnlyGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationIntraModuleGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationMaximalInterconnectedGraph
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider
import de.cau.cs.se.software.evaluation.views.AnalysisResultView
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.ui.PlatformUI

/**
 * Abstract class implementing the basic metrics of the Edward B. Allen entropy
 * based metrics based on a modular hypergraph formalism
 * 
 * @author Reiner Jung
 */
abstract class AbstractHypergraphAnalysisJob extends Job {

	protected val IProject project
	
	new (IProject project) {
		super("Analysis " + project.name)
		this.project = project
	}
	
	/** 
	 * Calculating system size based on input hypergraph.
	 * 
	 * @param inputHypergraph a hypergraph which will be interpreted as a hypergraph without modules.
	 * @param monitor Eclipse progress monitor
	 * @param result handler for the result model
	 * 
	 * @return the calculated information size of the hypergraph
	 */
	protected def calculateSize(Hypergraph inputHypergraph, IProgressMonitor monitor, AnalysisResultModelProvider result) {
		val hypergraphSize = new TransformationHypergraphSize(monitor)	
		monitor.beginTask("Calculate system size", hypergraphSize.workEstimate(inputHypergraph))
		monitor.subTask("")
		
		hypergraphSize.generate(inputHypergraph)

		result.addResult(project.project.name, "hypergraph size", hypergraphSize.result)
		updateView()
				
		return hypergraphSize.result
	}
	
	/**
	 * Calculate graph complexity.
	 * 
	 * @param inputHypergraph a hypergraph which will be interpreted as a hypergraph without modules.
	 * @param monitor Eclipse progress monitor
	 * @param result handler for the result model
	 * 
	 * @return the calculated complexity of the hypergraph
	 */
	protected def calculateComplexity(Hypergraph inputHypergraph, IProgressMonitor monitor, AnalysisResultModelProvider result) {
		val calculateComplexity = new CalculateComplexity(monitor)

		/** Calculation for S -> S^#, S^#_i */
		val complexity = calculateComplexity.calculate(inputHypergraph, "Calculate system's hypergraph complexity")
		
		result.addResult(project.project.name, "hypergraph complexity", complexity)
		updateView()

		return complexity
	}
	
	/**
	 * Calculate coupling of a modular hypergraph with only inter module hyperedges.
	 * Calculation for MS^* -> MS^*#, MS^*#_i
	 * 
	 * @param inputHypergraph a modular hypergraph
	 * @param monitor Eclipse progress monitor
	 * @param result handler for the result model
	 * 
	 * @return the coupling of the modular inter-module hyperedges only hypergraph 
	 */
	protected def calculateCoupling(ModularHypergraph inputHypergraph, IProgressMonitor monitor, AnalysisResultModelProvider result) {
		/** Determine intermodule hyperedges only modular graph for MS^* */
		val intermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(monitor)
		
		monitor.beginTask("Create intermodule hyperedges only graph", 
			intermoduleHyperedgesOnlyGraph.workEstimate(inputHypergraph)
		)
		
		intermoduleHyperedgesOnlyGraph.generate(inputHypergraph)

		val calculateComplexity = new CalculateComplexity(monitor)
		val complexityIntermodule = calculateComplexity.calculate(intermoduleHyperedgesOnlyGraph.result, 
			"Calculate intermodule complexity")
		result.addResult(project.project.name, "inter module coupling", complexityIntermodule)	
		updateView()
	}
	
	
	/**
	 * Calculate cohesion of a modular graph only containing inter module edges. 
	 * For this calculation the hypergraph must also be a graph.
	 * 
	 * @param inputHypergraph a modular hypergraph
	 * @param monitor Eclipse progress monitor
	 * @param result handler for the result model
	 * @param coupling the coupling values for the inter module only edges modular hypergraph 
	 * 
	 * @return the cohesion of the modular inter-module hyperedges only hypergraph
	 */
	protected def calculateCohesion(ModularHypergraph inputHypergraph, IProgressMonitor monitor, AnalysisResultModelProvider result) {
		val modularGraph = new TransformationHypergraphToGraphMapping(monitor)
		val maximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(monitor)
		val intraModuleGraph = new TransformationIntraModuleGraph(monitor)
		
		monitor.beginTask("Calculate Cohesion", modularGraph.workEstimate(inputHypergraph) + 
			maximalInterconnectedGraph.workEstimate(inputHypergraph) +
			intraModuleGraph.workEstimate(inputHypergraph))
		
		/** Determine graph mapping of the hypergraph */
		monitor.subTask("Create graph from hypergraph")
		modularGraph.generate(inputHypergraph)
		
		if (monitor.canceled)
			return 0
		
		/** Determine maximal interconnected modular graph MS^(n) */	
		monitor.subTask("Create maximal interconnected graph")
		maximalInterconnectedGraph.generate(modularGraph.result)
		
		if (monitor.canceled)
			return 0
		
		/** Determine maximal intra module graph MS^° */
		monitor.subTask("Create intra module graph")		
		intraModuleGraph.generate(modularGraph.result)
		
		if (monitor.canceled)
			return 0
		
		val calculateComplexity = new CalculateComplexity(monitor)
		
		/** Calculation for MS^(n) -> MS^(n)#, MS^(n)#_i */
		val complexityMaximalInterconnected = 
			calculateComplexity.calculate(maximalInterconnectedGraph.result, 
				"Calculate maximal interconnected graph complexity")
			
		if (monitor.canceled)
			return 0
				
		val coupling = 
			calculateComplexity.calculate(intraModuleGraph.result, 
				"Calculate intra-module graph complexity")
				
		if (monitor.canceled)
			return 0
		
		val cohesion = coupling/complexityMaximalInterconnected
		
		/** display results */
		result.addResult(project.project.name, "graph cohesion", cohesion)
		updateView()
		
		return cohesion
	}
		
	/**
	 * Update the analysis view after updating its content.
	 */
	protected def updateView() {
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
       		public override void run() {
				val part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().
					getActivePage().findView(AnalysisResultView.ID)
				(part as AnalysisResultView).setProject(project)
				(part as AnalysisResultView).update()
	    	}
     	})
	}
			
	
	
}