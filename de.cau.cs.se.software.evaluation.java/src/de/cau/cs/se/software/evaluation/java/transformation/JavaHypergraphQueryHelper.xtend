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
package de.cau.cs.se.software.evaluation.java.transformation

import de.cau.cs.se.software.evaluation.hypergraph.CallerCalleeTrace
import de.cau.cs.se.software.evaluation.hypergraph.Edge
import de.cau.cs.se.software.evaluation.hypergraph.FieldTrace
import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import de.cau.cs.se.software.evaluation.hypergraph.Module
import de.cau.cs.se.software.evaluation.hypergraph.Node
import de.cau.cs.se.software.evaluation.hypergraph.TypeTrace
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind

import java.util.List
import org.eclipse.emf.common.util.EList
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.SimpleName

import static de.cau.cs.se.software.evaluation.java.transformation.JavaHypergraphElementFactory.*

import static extension de.cau.cs.se.software.evaluation.java.transformation.NameResolutionHelper.*

class JavaHypergraphQueryHelper {
	/**
	 * Find the node for the given method invocation.
	 * 
	 * @param nodes
	 * @param binding
	 */
	def static findNodeForMethodBinding(EList<Node> nodes, IMethodBinding binding) {
		nodes.findFirst[
			val derivedFrom = it.derivedFrom
			switch (derivedFrom) {
				MethodTrace: (derivedFrom.method as IMethodBinding).methodDeclaration.isEqualTo(binding.methodDeclaration)
				TypeTrace: false
				default:
					throw new UnsupportedOperationException("Nodes cannot be derived from " + derivedFrom.class)
			}
		]
	}
	
	/**
	 * Find the node for the given method invocation.
	 * 
	 * @param nodes
	 * @param binding
	 */
	def static findNodeForConstructorBinding(EList<Node> nodes, IMethodBinding binding) {
		val result = nodes.findNodeForMethodBinding(binding)
		if (result === null) { /** now check if there is an implicit constructor node. */
			nodes.findFirst[
				val derivedFrom = it.derivedFrom
				switch (derivedFrom) {
					MethodTrace: false
					TypeTrace: (derivedFrom.type as ITypeBinding).isEqualTo(binding.declaringClass)
					default:
						throw new UnsupportedOperationException("Nodes cannot be derived from " + derivedFrom.class)
				}
			]
		} else
			return result
	}
	
	/**
	 * Find a module which corresponds to the type binding.
	 */
	def static findModule(EList<Module> modules, ITypeBinding binding) {
		modules.findFirst[((it.derivedFrom as TypeTrace).type as ITypeBinding).isEqualTo(binding)]
	}
	
	
	/**
	 * Find a module which corresponds to the type binding.
	 */
	def static findModule(EList<Module> modules, String fullyQualifiedName) {
		modules.findFirst[it.name.equals(fullyQualifiedName)]
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
				val trace = (edge.derivedFrom as FieldTrace).field as IVariableBinding
				trace.isEqualTo(binding)
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
    	if (targetNode === null) { /** node does not yet exist. It must be a framework class. */
    		var module = graph.modules.findModule(typeBinding)
    		if (module === null) { /** Module does not exists. Add it on demand. */
    			module = createModuleForTypeBinding(typeBinding, EModuleKind.FRAMEWORK)
    			graph.modules.add(module) 
    		}
    		targetNode = createNodeForMethod(methodBinding)
    		module.nodes.add(targetNode)
    		graph.nodes.add(targetNode)
    	}
    	
    	return targetNode
    }
    
    /**
     * Find the given variable declared in the class given by the typeBinding and check if it is
     * a data property. 
     * 
     * @param property the property name
     * @param typeBinding the type binding of the class
     * @param dataTypePatterns a list of patterns used to determine data types
     * 
     * @return Returns the variable binding if the property exists and is a data property, else null
     */
    def static IVariableBinding findDataPropertyInClass(SimpleName property, ITypeBinding typeBinding, List<String> dataTypePatterns) {
    	typeBinding.declaredFields.findFirst[it.name.equals(property.fullyQualifiedName) && it.type.isDataType(dataTypePatterns)]
    } 
    
    /**
     * Determine if a given type is considered a data type.
     */
    def static boolean isDataType(ITypeBinding type, List<String> dataTypePatterns) {
    	if (type.isPrimitive)
    		return true
    	else
    		return dataTypePatterns.exists[type.determineFullyQualifiedName.matches(it)]
    }
    
}