package de.cau.cs.se.software.evaluation.emf.transformation

import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
import org.eclipse.emf.ecore.EPackage
import org.eclipse.core.runtime.IProgressMonitor
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory
import org.eclipse.emf.ecore.EClass
import de.cau.cs.se.software.evaluation.hypergraph.Module
import java.util.HashMap

import de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace
import de.cau.cs.se.software.evaluation.hypergraph.Node
import java.util.ArrayList
import java.util.List
import static extension de.cau.cs.se.software.evaluation.transformation.HypergraphCreationFactory.*

class TransformationEMFInstanceToHypergraph extends AbstractTransformation<EPackage, ModularHypergraph> {
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(EPackage input) {
		result = HypergraphFactory.eINSTANCE.createModularHypergraph
		
		val packages = new ArrayList<Module>() 
		val nodeMap = new HashMap<EClass,Node>()
		
		/** packages */
		packages.resolvePackages(input)
		
		/** classes */
		packages.forEach[module | 
			val classifiers = ((module.derivedFrom as ModelElementTrace).element as EPackage).EClassifiers
			classifiers.filter(EClass).forEach[
				nodeMap.put(it, result.createNode(module, module.name + "." + it.name, it))
			]
		]
		
		/** references to edges (ignore inherited references) */
		nodeMap.values.forEach[sourceNode |
			val references = ((sourceNode.derivedFrom as ModelElementTrace).element as EClass).EReferences
			references.forEach[reference |
				val targetNode = nodeMap.get(reference.EReferenceType)
				if (targetNode != null)
					createEdge(result, sourceNode, targetNode, reference.name, reference)
				// else: the type is from an imported metamodel 
			]
		]
		
		return result
	}
	
	/**
	 * Find all packages recursively and create modules with fully qualified names of these packages.
	 */
	private def void resolvePackages(List<Module> packages, EPackage ePackage) {
		packages.add(result.createModule(ePackage.determineName, ePackage))
		ePackage.ESubpackages.forEach[resolvePackages(packages, it)]
	}
	
	/**
	 * Create full qualified package name.
	 */
	private def String determineName(EPackage ePackage) {
		if (ePackage.ESuperPackage != null)
			ePackage.ESuperPackage.determineName + "." + ePackage.name
		else
			ePackage.name
	}
	
	override workEstimate(EPackage input) {
		1 + // resolvePackages
		1 + // classes
		1 // references
	}
	

	
}