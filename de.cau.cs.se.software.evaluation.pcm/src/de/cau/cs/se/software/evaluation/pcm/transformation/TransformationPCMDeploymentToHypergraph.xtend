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
package de.cau.cs.se.software.evaluation.pcm.transformation

import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import org.palladiosimulator.pcm.system.System
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.hypergraph.Module
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import org.palladiosimulator.pcm.repository.RepositoryComponent
import org.palladiosimulator.pcm.repository.BasicComponent
import org.palladiosimulator.pcm.repository.CompleteComponentType
import org.palladiosimulator.pcm.repository.CompositeComponent
import de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace
import org.palladiosimulator.pcm.core.composition.AssemblyContext
import org.palladiosimulator.pcm.repository.OperationRequiredRole
import static extension de.cau.cs.se.software.evaluation.transformation.HypergraphCreationFactory.*

class TransformationPCMDeploymentToHypergraph extends AbstractTransformation<System, ModularHypergraph> {
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(System input) {
		result = HypergraphFactory.eINSTANCE.createModularHypergraph
		
		input.assemblyContexts__ComposedStructure.forEach[assemblyContext |
			java.lang.System.out.println("Module " + assemblyContext.entityName)
			val module = result.createModule(assemblyContext.entityName, assemblyContext)
			assemblyContext.encapsulatedComponent__AssemblyContext.createNodeForComponent(module)
		]
		
		result.modules.forEach[module |
			val component = ((module.derivedFrom as ModelElementTrace).element as AssemblyContext).encapsulatedComponent__AssemblyContext
			if (component instanceof BasicComponent) {
				java.lang.System.out.println(">> " + (component as BasicComponent).entityName)
				component.getRequiredRoles_InterfaceRequiringEntity.forEach[required | 
					switch (required) {
						OperationRequiredRole: required.requiredInterface__OperationRequiredRole
						default: java.lang.System.out.println("other role " + required)
					}
				]
			}
		]
	
		return result	
	}

	
	private dispatch def void createNodeForComponent(BasicComponent component, Module module) {
		component.providedRoles_InterfaceProvidingEntity.forEach[provided |
			java.lang.System.out.println("node " + provided.entityName)
			result.createNode(module, provided.entityName, provided)
		]
	}
	
	private dispatch def void createNodeForComponent(CompleteComponentType component, Module module) {
		result.createNode(module, component.entityName, component)
	}
	
	private dispatch def void createNodeForComponent(CompositeComponent component, Module module) {
		result.createNode(module,component.entityName, component)
	}
	
	private dispatch def void createNodeForComponent(RepositoryComponent component, Module module) {
		java.lang.System.out.println("strange component type " + component)
	}
	
	override workEstimate(System input) {
		input.assemblyContexts__ComposedStructure.size *2
	}
	
}