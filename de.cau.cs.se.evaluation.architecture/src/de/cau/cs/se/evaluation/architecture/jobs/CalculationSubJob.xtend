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
package de.cau.cs.se.evaluation.architecture.jobs

import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationConnectedNodeHyperedgesOnlyGraph
import org.eclipse.core.runtime.Status
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node

class CalculationSubJob extends Job {
		
	ComplexityAnalysisJob parent
	
	Hypergraph input
		
	new(String name, ComplexityAnalysisJob parent, Hypergraph input) {
		super(name)
		this.parent = parent
		this.input = input
	}
	
	override protected run(IProgressMonitor monitor) {
		monitor.beginTask("Processing graphs.",0)
		var Node node 
		var int i = 0
		while ((node = parent.getNextConnectedNodeTask) != null) {
			monitor.subTask("task " + i++)
			// S^#_i
			val transformationConnectedNodeHyperedgesOnlyGraph = 
				new TransformationConnectedNodeHyperedgesOnlyGraph(input)
			
			transformationConnectedNodeHyperedgesOnlyGraph.node = node
			transformationConnectedNodeHyperedgesOnlyGraph.transform
			parent.deliverResult(transformationConnectedNodeHyperedgesOnlyGraph.result)
		}
		
		monitor.done
		
		return Status.OK_STATUS
	}
	
}