package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import org.eclipse.jdt.core.dom.IMethodBinding
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
import org.eclipse.jdt.core.dom.ITypeBinding
import de.cau.cs.se.evaluation.architecture.hypergraph.Module
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace
import org.eclipse.jdt.core.dom.IVariableBinding
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace
import org.eclipse.jdt.core.dom.VariableDeclarationFragment

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*


class JavaHypergraphQueryHelper {
		/**
	 * Find the node for the given constructor invocation.
	 * 
	 * @param nodes
	 * @param binding
	 */
	def static findNodeForMethodBinding(EList<Node> nodes, IMethodBinding binding) {
		nodes.findFirst[
			val derivedFrom = it.derivedFrom
			switch (derivedFrom) {
				MethodTrace: (derivedFrom.method as IMethodBinding).isEqualTo(binding)
				TypeTrace: (derivedFrom.type as ITypeBinding).isEqualTo(binding.declaringClass)
				default:
					throw new UnsupportedOperationException("Nodes cannot be derived from " + derivedFrom.class)
			}
		]
	}
	
	/**
	 * Find a module which corresponds to the type binding.
	 */
	def static findModule(EList<Module> modules, ITypeBinding binding) {
		modules.findFirst[((it.derivedFrom as TypeTrace).type as ITypeBinding).isSubTypeCompatible(binding)]
	}
	
	/**
	 * Find an edge which has the two corresponding method bindings.
	 */
	def static findCallEdge(EList<Edge> edges, IMethodBinding endOne, IMethodBinding endTwo) {
		System.out.println("findCallEdge " + endOne.determineFullyQualifiedName + " :: " + endTwo.determineFullyQualifiedName)
		edges.findFirst[edge |
			if (edge.derivedFrom instanceof CallerCalleeTrace) {
				val trace = (edge.derivedFrom as CallerCalleeTrace)
				val callee = trace.callee as IMethodBinding
				val caller = trace.caller as IMethodBinding
				
				if ((caller.isEqualTo(endOne) && callee.isEqualTo(endTwo)) ||
				    (caller.isEqualTo(endTwo) && callee.isEqualTo(endOne))) {
				    System.out.println("\t " + caller.determineFullyQualifiedName + " :: " + callee.determineFullyQualifiedName)
				    true
				} else {
					false	
				}
			} else
				false
		]
	}
	
	/**
	 * Find an data edge which has the given variable binding.
	 */
	def static findDataEdge(EList<Edge> edges, IVariableBinding binding) {
		System.out.println("findDataEdge " + binding.determineFullyQualifiedName)
		edges.findFirst[edge |
			if (edge.derivedFrom instanceof FieldTrace) {
				val trace = (edge.derivedFrom as FieldTrace).field as VariableDeclarationFragment
				trace.resolveBinding.isEqualTo(binding)
			} else
				false
		]
	}
}