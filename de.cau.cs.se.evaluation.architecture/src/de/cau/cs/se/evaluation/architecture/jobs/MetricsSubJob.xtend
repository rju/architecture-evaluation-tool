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
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphMetrics
import org.eclipse.core.runtime.Status
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph

class MetricsSubJob extends Job {
	
	ComplexityAnalysisJob parent
			
	new(String name, ComplexityAnalysisJob parent) {
		super(name)
		this.parent = parent
	}
	
	override protected run(IProgressMonitor monitor) {
		val metrics = new TransformationHypergraphMetrics(monitor)
		var Hypergraph subgraph
		while ((subgraph = parent.getNextSubgraph) != null) {
			metrics.setSystem(subgraph)
			parent.deliverMetricsResult(metrics.calculate)
		}
		
		return Status.OK_STATUS
	}
	
}