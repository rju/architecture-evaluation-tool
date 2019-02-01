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
package de.cau.cs.se.software.evaluation.transformation.metric

import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
import de.cau.cs.se.software.evaluation.state.RowPatternTable
import de.cau.cs.se.software.evaluation.views.LogModelProvider
import de.cau.cs.se.software.evaluation.state.RowPattern
import de.cau.cs.se.software.evaluation.hypergraph.Node

/**
 * This is an processing intensive task. Therefore, we
 * externalized it into a job.
 * 
 * @author Reiner Jung
 */
class CalculateSizePartitionJob extends Job {
	
	public var int start 
	public var int end
	public var double resultSize
	
	Hypergraph input
	
	RowPatternTable table
	
	
	new (String label, Hypergraph input, RowPatternTable table) {
		super(label)
		this.input = input
		this.table = table
	}
	
	override protected run(IProgressMonitor monitor) {
		var double size = 0
		
		for (var int i=start;i<end;i++) {
			if (monitor.canceled)
				return Status.CANCEL_STATUS
			monitor.worked(input.nodes.size)
			val probability = table.patterns.lookupProbability(input.nodes.get(i), input)
			if (probability > 0.0d) 
				size+= (-log2(probability))
			else
				LogModelProvider.INSTANCE.addMessage("Hypergraph Model Error", "A component is disconnected, but should be connected. Result is tainted.")
		}
		
		resultSize = size
		
		return Status.OK_STATUS
	}
		
	/**
	 * Logarithm for base 2.
	 */
	private def double log2(double value) {
		return Math.log(value)/Math.log(2)
	}
	
	/**
	 * Find the row pattern for a given node and determine its probability. If no pattern
	 * is found then the node is totally disconnected and the probability is 0.
	 */
	private def double lookupProbability(RowPattern[] patterns, Node node, Hypergraph system) {
		val pattern = patterns.findFirst[it.nodes.contains(node)]
		val double count = if (pattern !== null) pattern.nodes.size as double else 0
			
		return count/((system.nodes.size + 1) as double)
	}
}