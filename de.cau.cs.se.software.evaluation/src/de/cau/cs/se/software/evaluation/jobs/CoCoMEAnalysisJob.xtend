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

import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status

/** 
 * Special analysis job for megamodels.
 * 
 * @deprecated this should be replaced by a real megamodel DSL
 * 
 * @author Reiner Jung
 */
class CoCoMEAnalysisJob extends AbstractHypergraphAnalysisJob {
		
	val boolean complete
	
	new(IProject project, boolean complete, IOutputHandler handler) {
		super(project, handler)
		this.complete = complete
	}
		
	override protected run(IProgressMonitor monitor) {
		val result = AnalysisResultModelProvider.INSTANCE
		
		val generator = new CoCoMEMegaModel()
		val graph = generator.createMegaModelAnalysis(complete)
		
		result.addResult(project.name, "number of nodes", graph.nodes.size)
		result.addResult(project.name, "number of edges", graph.edges.size)
		result.hypergraph = graph
		
		updateView()
		
		calculateSize(graph, monitor, result)
		
		calculateComplexity(graph, monitor, result)
		
		monitor.done()
							
		return Status.OK_STATUS	
	}
	
}