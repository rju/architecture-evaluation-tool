package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import java.util.List
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.emf.common.util.EList
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.ArrayType
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.ParameterizedType
import org.eclipse.jdt.core.dom.PrimitiveType
import org.eclipse.jdt.core.dom.QualifiedType
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.UnionType
import org.eclipse.jdt.core.dom.WildcardType

import static de.cau.cs.se.evaluation.architecture.transformation.java.JavaASTEvaluation.*
import static de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory.*

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import de.cau.cs.se.evaluation.architecture.hypergraph.EModuleKind

class TransformationJavaMethodsToModularHypergraph implements ITransformation {
	
	var ModularHypergraph modularSystem
	val IJavaProject project
	val IProgressMonitor monitor
	val List<AbstractTypeDeclaration> classes
	val List<String> dataTypePatterns
	val List<String> observedSystemPatterns
		
	/**
	 * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
	 *  
	 * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
	 * @param scope the global scoper used to resolve classes during transformation
	 * @param eclipse progress monitor
	 */
	public new(IJavaProject project, List<AbstractTypeDeclaration> classes, List<String> dataTypePatterns, List<String> observedSystemPatterns, IProgressMonitor monitor) {
		this.project = project
		this.monitor = monitor
		this.dataTypePatterns = dataTypePatterns
		this.observedSystemPatterns = observedSystemPatterns
		this.classes = classes 
	}
	
	/**
	 * Return the generated result.
	 */
	def ModularHypergraph getModularSystem() {
		return modularSystem
	}
	
	/**
	 * Main transformation routine.
	 */
	override transform() {
		modularSystem = HypergraphFactory.eINSTANCE.createModularHypergraph
		// create modules for all classes
		classes.forEach[clazz | modularSystem.modules.add(createModuleForTypeBinding(clazz.resolveBinding, EModuleKind.SYSTEM))]

		// define edges for all internal variables of a class
		classes.forEach[clazz | modularSystem.edges.createEdgesForClassProperties(clazz, dataTypePatterns)]

		// find all method declarations and create nodes for it, grouped by class as modules
		classes.forEach[clazz | modularSystem.nodes.createNodesForMethods(clazz)]
		classes.filter[clazz | clazz.hasImplicitConstructor].
			forEach[clazz |
				val node = createNodeForImplicitConstructor(clazz.resolveBinding)
				val module = modularSystem.modules.findFirst[((it.derivedFrom as TypeTrace).type as ITypeBinding).isSubTypeCompatible(clazz.resolveBinding)]
				module.nodes.add(node) 
				modularSystem.nodes.add(node)
			]
		
		classes.forEach[clazz | resolveEdges(modularSystem, dataTypePatterns, clazz)]
	}
	
	/**
	 * Create edges for each method found in the observed system.
	 * 
	 * @param graph the hypergraph where the edges will be added to
	 * @param type the type which is processed for edges
	 */
	private def void resolveEdges(ModularHypergraph graph, List<String> dataTypePatterns, AbstractTypeDeclaration type) {
		if (type instanceof TypeDeclaration) {
			val clazz = type as TypeDeclaration
			clazz.methods.forEach[method |
				val node = graph.nodes.findFirst[
					((it.derivedFrom as MethodTrace).method as IMethodBinding).
						isEqualTo(method.resolveBinding)
				]
				evaluteMethod(graph, dataTypePatterns, node, clazz, method)
			]
		}
	}
	
	/**
	 * Determine if a given class has no explicit constructors which
	 * implies an implicit constructor.
	 * 
	 * @param type a class, enum or interface type from the selection
	 * 
	 * @return returns true if the given type is a normal class with no constructor
	 */
	private def boolean hasImplicitConstructor(AbstractTypeDeclaration type) {
		if (type instanceof TypeDeclaration) {
			val clazz = type as TypeDeclaration
			if (!clazz.interface && !Modifier.isAbstract(clazz.getModifiers())) {
				val isImplicit = !clazz.methods.exists[method | method.constructor]
				return isImplicit
			}
		}
		
		return false
	}
	

	/**
	 * Create all nodes for a class.
	 * 
	 * @param nodes list where the nodes will be inserted
	 * @param type the class where the methods come from
	 */
	private def void createNodesForMethods(EList<Node> nodes, AbstractTypeDeclaration type) {
		if (type instanceof TypeDeclaration) {
			val module = modularSystem.modules.findFirst[((it.derivedFrom as TypeTrace).type as ITypeBinding).isSubTypeCompatible(type.resolveBinding)]
			type.methods.forEach[method |
				val node = createNodeForMethod(method.resolveBinding)
				nodes.add(node)
				module.nodes.add(node)
			]
		}
	}
	
	/**
	 * Create edges for all properties in the given type which are data type properties.
	 */
	private def void createEdgesForClassProperties(EList<Edge> edges, AbstractTypeDeclaration type, List<String> dataTypePatterns) {
		if (type instanceof TypeDeclaration) {
			type.fields.forEach[field |
				if (field.type.isDataType(dataTypePatterns))
					field.fragments.forEach[fragment | edges.add(createDataEdge((fragment as VariableDeclarationFragment).resolveBinding))]
			]
		}
	}
	
	/**
	 * Determine if the given type is a data type.
	 * 
	 * @param type the type to be evaluated
	 * @param dataTypes a list of data types
	 * 
	 * @return returns true if the given type is a data type and not a behavior type.
	 */
	private def boolean isDataType(Type type, List<String> dataTypePatterns) {
		switch(type) {
			ArrayType:
				return type.elementType.isDataType(dataTypePatterns)
			ParameterizedType:
				return type.type.isDataType(dataTypePatterns)
			PrimitiveType: /** primitive types are all data types */
				return true
			QualifiedType:
				return type.qualifier.isDataType(dataTypePatterns) 
			SimpleType:
				return dataTypePatterns.exists[dataTypePattern | type.resolveBinding.determineFullyQualifiedName.matches(dataTypePattern)]
			UnionType:
				return type.types.forall[it.isDataType(dataTypePatterns)]
			WildcardType:
				return if (type.bound != null)
					type.bound.isDataType(dataTypePatterns)
				else
					true	
			default:
				return false
		}
	}
	
	
		
}