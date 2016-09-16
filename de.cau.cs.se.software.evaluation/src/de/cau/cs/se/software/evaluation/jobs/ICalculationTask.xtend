package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.hypergraph.Node

interface ICalculationTask {
	
	def Node getNextConnectedNodeTask()
	
	def void deliverConnectedNodeHyperedgesOnlySizeResult(double size)
	
}