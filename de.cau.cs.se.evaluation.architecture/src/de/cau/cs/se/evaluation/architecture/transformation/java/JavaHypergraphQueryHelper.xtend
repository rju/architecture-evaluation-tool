package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Module
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
import org.eclipse.emf.common.util.EList
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.VariableDeclarationFragment

import static de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory.*

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
		edges.findFirst[edge |
			if (edge.derivedFrom instanceof CallerCalleeTrace) {
				val trace = (edge.derivedFrom as CallerCalleeTrace)
				val callee = trace.callee as IMethodBinding
				val caller = trace.caller as IMethodBinding
				
				if ((caller.isEqualTo(endOne) && callee.isEqualTo(endTwo)) ||
				    (caller.isEqualTo(endTwo) && callee.isEqualTo(endOne))) {
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
		edges.findFirst[edge |
			if (edge.derivedFrom instanceof FieldTrace) {
				val trace = (edge.derivedFrom as FieldTrace).field as VariableDeclarationFragment
				trace.resolveBinding.isEqualTo(binding)
			} else
				false
		]
	}
	
	/**
     * Find the corresponding node in the graph and if none can be found create one.
     * Missing nodes occur, because framework classes are not automatically scanned form method.
     * It a new node is created it is also added to the graph.
     * 
     * @param graph
     * @param typeBinding
     * @param methodBinding
     * 
     * @return returns the found or created node 
     */
    def static findOrCreateTargetNode(ModularHypergraph graph, ITypeBinding typeBinding, IMethodBinding methodBinding) {
    	var targetNode = graph.nodes.findNodeForMethodBinding(methodBinding)
    	if (targetNode == null) { /** node does not yet exist. It must be a framework class. */
    		var module = graph.modules.findModule(typeBinding)
    		if (module == null) { /** Module does not exists. Add it on demand. */
    			module = createModuleForTypeBinding(typeBinding)
    			graph.modules.add(module) 
    		}
    		targetNode = createNodeForMethod(methodBinding)
    		module.nodes.add(targetNode)
    		graph.nodes.add(targetNode)
    	}
    	
    	return targetNode
    }
}