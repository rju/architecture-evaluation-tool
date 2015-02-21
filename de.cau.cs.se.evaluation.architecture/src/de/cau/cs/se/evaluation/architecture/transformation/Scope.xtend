package de.cau.cs.se.evaluation.architecture.transformation

abstract class Scope implements IScope {
	val IScope parentScope
	
	new (IScope parentScope) {
		this.parentScope = parentScope
	}
	
	def getParentScope() {
		return parentScope
	}
	
}