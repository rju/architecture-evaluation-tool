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

import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationIntermoduleHyperedgesOnlyGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationMaximalInterconnectedGraph
import de.cau.cs.se.software.evaluation.views.AnalysisResultView
import de.cau.cs.se.software.evaluation.views.NamedValue
import de.cau.cs.se.software.evaluation.views.ResultModelProvider
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.ui.PartInitException
import org.eclipse.ui.PlatformUI
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationIntraModuleGraph
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphToGraphMapping

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
	protected def calculateSize(Hypergraph inputHypergraph, IProgressMonitor monitor, ResultModelProvider result) {
		val hypergraphSize = new TransformationHypergraphSize(monitor)
		hypergraphSize.name = "Calculate system size"
		hypergraphSize.generate(inputHypergraph)

		result.values.add(new NamedValue(project.project.name, "hypergraph size", hypergraphSize.result))
		updateView(inputHypergraph)
		
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
	protected def calculateComplexity(Hypergraph inputHypergraph, IProgressMonitor monitor, ResultModelProvider result) {
		val calculateComplexity = new CalculateComplexity(monitor)

		/** Calculation for S -> S^#, S^#_i */
		val complexity = calculateComplexity.calculate(inputHypergraph, "Calculate system's hypergraph complexity")
		
		result.values.add(new NamedValue(project.project.name, "hypergraph complexity", complexity))
		updateView(inputHypergraph)

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
	protected def calculateCoupling(ModularHypergraph inputHypergraph, IProgressMonitor monitor, ResultModelProvider result) {
		/** Determine intermodule hyperedges only modular graph for MS^* */
		val intermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(monitor)
		intermoduleHyperedgesOnlyGraph.generate(inputHypergraph)

		val calculateComplexity = new CalculateComplexity(monitor)
		val complexityIntermodule = calculateComplexity.calculate(intermoduleHyperedgesOnlyGraph.result, "Calculate intermodule complexity")
		result.values.add(new NamedValue(project.project.name, "inter module coupling", complexityIntermodule))	
		updateView(inputHypergraph)
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
	protected def calculateCohesion(ModularHypergraph inputHypergraph, IProgressMonitor monitor, ResultModelProvider result) {
		/** Determine graph mapping of the hypergraph */
		val modularGraph = new TransformationHypergraphToGraphMapping(monitor)
		modularGraph.generate(inputHypergraph)
				
		/** Determine maximal interconnected modular graph MS^(n) */
		val maximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(monitor)
		maximalInterconnectedGraph.generate(modularGraph.result)
		
		/** Determine maximal intra module graph MS^° */
		val intraModuleGraph = new TransformationIntraModuleGraph(monitor)
		intraModuleGraph.generate(modularGraph.result)
		
		val calculateComplexity = new CalculateComplexity(monitor)
		
		/** Calculation for MS^(n) -> MS^(n)#, MS^(n)#_i */
		val complexityMaximalInterconnected = 
			calculateComplexity.calculate(maximalInterconnectedGraph.result, 
				"Calculate maximal interconnected graph complexity")
				
		val coupling = 
			calculateComplexity.calculate(intraModuleGraph.result, 
				"Calculate maximal interconnected graph complexity")
		
		val cohesion = coupling/complexityMaximalInterconnected
		
		/** display results */
		result.values.add(new NamedValue(project.project.name, "graph cohesion", cohesion))
		updateView(inputHypergraph)
		
		return cohesion
	}
		
	/**
	 * Update the analysis view after updating its content.
	 */
	protected def updateView(Hypergraph hypergraph) {
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
       		public override void run() {
	           try { 
					val part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AnalysisResultView.ID)
					if (hypergraph != null) (part as AnalysisResultView).setHypergraph(hypergraph)
					(part as AnalysisResultView).setProject(project)
					(part as AnalysisResultView).update()
	           } catch (PartInitException e) {
	                e.printStackTrace()
	           }
	    	}
     	})
	}
			
	
	
}