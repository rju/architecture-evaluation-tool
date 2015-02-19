package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.transformation.IScope
import org.eclipse.core.runtime.IProgressMonitor
import java.util.List
import org.eclipse.jdt.core.IType
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory

class TransformationJavaMethodsToModularHypergraph implements ITransformation {
	
	var ModularHypergraph modularSystem
	val IScope globalScope
	val IProgressMonitor monitor
	val List<IType> classList
	
	/**
	 * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
	 *  
	 * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
	 * @param scope the global scoper used to resolve classes during transformation  
	 */
	public new (IScope scope, List<IType> classList) {
		this.globalScope = scope
		this.classList = classList 
		this.monitor = null
	}
	
	/**
	 * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
	 *  
	 * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
	 * @param scope the global scoper used to resolve classes during transformation
	 * @param eclipse progress monitor
	 */
	public new(GlobalJavaScope scope, List<IType> classList, IProgressMonitor monitor) {
		this.globalScope = scope
		this.classList = classList 
		this.monitor = monitor
	}
	
	/**
	 * Return the generated result.
	 */
	def ModularHypergraph getModularSystem() {
		return modularSystem
	}
	
	override transform() {
		modularSystem = HypergraphFactory.eINSTANCE.createModularHypergraph
		// create modules for all classes
		classList.forEach[clazz | modularSystem.modules.add(createModuleForClass(clazz))]

		// define edges for all internal variables of a class
		
		// find all method declarations and create nodes for it, grouped by class as modules
		
		// find all method invocations in class and external and create edges for them 
		
		// find all variable accesses from expressions inside methods
		
	}
	
	def createModuleForClass(IType type) {
		val module = HypergraphFactory.eINSTANCE.createModule
		module.name = type.fullyQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
		derivedFrom.type = type
		module.derivedFrom = derivedFrom
		
		return module
	}
	
}