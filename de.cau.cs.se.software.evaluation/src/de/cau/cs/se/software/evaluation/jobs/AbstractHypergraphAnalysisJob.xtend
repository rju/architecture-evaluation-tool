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

abstract class AbstractHypergraphAnalysisJob extends Job {

	protected val IProject project
	
	new (IProject project) {
		super("Analysis " + project.name)
		this.project = project
	}
	
	/**
	 * Calculate cohesion of a graph.
	 */
	def calculateCohesion(ModularHypergraph inputHypergraph, IProgressMonitor monitor, ResultModelProvider result, double coupling) {
		/** Determine maximal interconnected modular graph MS^(n) */
		val maximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(monitor)
		maximalInterconnectedGraph.input = inputHypergraph
		maximalInterconnectedGraph.transform
		
		val calculateComplexity = new CalculateComplexity(monitor)
		
		/** Calculation for MS^(n) -> MS^(n)#, MS^(n)#_i */
		val complexityMaximalInterconnected = calculateComplexity.calculate(maximalInterconnectedGraph.result, 
			"Calculate maximal interconnected graph complexity"
		)
		
		val cohesion = coupling/complexityMaximalInterconnected
		
		/** display results */
		result.values.add(new NamedValue(project.project.name, "inter module cohesion", cohesion))
	
		return cohesion
	}
	
	/**
	 * Calculation for MS^* -> MS^*#, MS^*#_i
	 * Calculate coupling
	 */
	def calculateCoupling(ModularHypergraph inputHypergraph, IProgressMonitor monitor, ResultModelProvider result) {
		/** Determine intermodule hyperedges only modular graph for MS^* */
		val intermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(monitor)
		intermoduleHyperedgesOnlyGraph.input = inputHypergraph
		intermoduleHyperedgesOnlyGraph.transform

		val calculateComplexity = new CalculateComplexity(monitor)
		val complexityIntermodule = calculateComplexity.calculate(intermoduleHyperedgesOnlyGraph.result, "Calculate intermodule complexity")
		result.values.add(new NamedValue(project.project.name, "inter module coupling", complexityIntermodule))	
		updateView(inputHypergraph)
		
		return complexityIntermodule
	}
	
	/**
	 * Calculate graph complexity.
	 */
	def calculateComplexity(ModularHypergraph inputHypergraph, IProgressMonitor monitor, ResultModelProvider result) {
		val calculateComplexity = new CalculateComplexity(monitor)

		/** Calculation for S -> S^#, S^#_i */
		val complexity = calculateComplexity.calculate(inputHypergraph, "Calculate system's hypergraph complexity")
		
		result.values.add(new NamedValue(project.project.name, "hypergraph complexity", complexity))
		updateView(inputHypergraph)

		return complexity
	}
	
	/** 
	 * Calculating system size based on input modular hyper graph
	 */
	def calculateSize(ModularHypergraph inputHypergraph, IProgressMonitor monitor, ResultModelProvider result) {
		val hypergraphSize = new TransformationHypergraphSize(monitor)
		hypergraphSize.name = "Calculate system size"
		hypergraphSize.input = inputHypergraph
		hypergraphSize.transform

		result.values.add(new NamedValue(project.project.name, "hypergraph size", hypergraphSize.result))
		updateView(inputHypergraph)
		
		return hypergraphSize.result
	}
	
	

	
	/**
	 * Update the analysis view after updating its content.
	 */
	protected def updateView(ModularHypergraph hypergraph) {
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
       		public override void run() {
	           try { 
					val part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AnalysisResultView.ID)
					if (hypergraph != null) (part as AnalysisResultView).setGraph(hypergraph)
					(part as AnalysisResultView).setProject(project)
					(part as AnalysisResultView).update()
	           } catch (PartInitException e) {
	                e.printStackTrace()
	           }
	    	}
     	})
	}
			
	
	
}