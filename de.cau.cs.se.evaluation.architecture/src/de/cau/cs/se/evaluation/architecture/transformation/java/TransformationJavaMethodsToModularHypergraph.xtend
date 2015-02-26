package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import de.cau.cs.se.evaluation.architecture.transformation.IScope
import org.eclipse.core.runtime.IProgressMonitor
import java.util.List
import org.eclipse.jdt.core.IType
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.Modifier

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJavaExtension.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.HypergraphJDTDOMExtension.*
import org.eclipse.jdt.core.IJavaProject

class TransformationJavaMethodsToModularHypergraph implements ITransformation {
	
	var ModularHypergraph modularSystem
	val IScope globalScope
	val IJavaProject project
	val IProgressMonitor monitor
	val List<IType> classList
	
	/**
	 * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
	 *  
	 * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
	 * @param scope the global scoper used to resolve classes during transformation  
	 */
	public new (IJavaProject project, IScope scope, List<IType> classList) {
		this.project = project
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
	public new(IJavaProject project, GlobalJavaScope scope, List<IType> classList, IProgressMonitor monitor) {
		this.project = project
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
		classList.forEach[clazz | modularSystem.edges.createEdgesForClassVariables(clazz)]

		// find all method declarations and create nodes for it, grouped by class as modules
		classList.forEach[clazz | modularSystem.nodes.createNodesForClassMethods(clazz)]
		classList.forEach[clazz | modularSystem.nodes.createNodesForInplicitClassConstructors(clazz)]
		
		// find all method invocations create edges for them 
		// find all variable accesses from expressions inside methods
		classList.forEach[clazz | clazz.createEdgesForInvocations(project)]
		// find all internal variables and check whether they use any of the used modules.
		// Connect nodes accordingly
		// TODO
				
	}
	
	def void createNodesForInplicitClassConstructors(EList<Node> nodes, IType type) {
		val tname = type.fullyQualifiedName + "." + type.elementName
		if (!nodes.exists[node | node.name.equals(tname)]) {
			val module = modularSystem.modules.findFirst[it.name.equals(type.fullyQualifiedName)]
			val newNode = HypergraphFactory.eINSTANCE.createNode
			newNode.name = type.fullyQualifiedName + "." + type.elementName
			val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
			derivedFrom.type = type
			newNode.derivedFrom = derivedFrom
			nodes.add(newNode)
			module.nodes.add(newNode)
		}	
	}
	
	/**
	 * create edges for invocations and edges for variable access.
	 */
	private def void createEdgesForInvocations(IType type, IJavaProject project) {
		val unit = type.getUnitForType(monitor, project)
		val object = unit.types.get(0)
		if (object instanceof TypeDeclaration) {
			val declaredType =  object as TypeDeclaration
			if (declaredType != null) {
				val Iterable<Modifier> modifiers = declaredType.modifiers().filter(Modifier)
				if (!declaredType.isInterface && (modifiers.findFirst[(it as Modifier).isAbstract()] == null)) {
					val scope = new JavaLocalScope(unit, new JavaPackageScope(type.packageFragment, globalScope))
	 				val handler = new HandleStatementForMethodAndFieldAccess(scope)
					/** check method bodies. */
					declaredType.methods.forEach[method | 
						method.body.statements.forEach[handler.handle(modularSystem, declaredType, method, it)]
					]
				}
			}
		}
	}
	
	/**
	 * Resolve all methods of a given class.
	 */
	private def void createNodesForClassMethods(EList<Node> nodes, IType type) {
		val module = modularSystem.modules.findFirst[it.name.equals(type.fullyQualifiedName)]
		type.methods.forEach[method |
			val node = method.createNodeForMethod 
			nodes.add(node)
			module.nodes.add(node)
		]
	}
	
	/**
	 * Create edges for all variables in the given type.
	 */
	private def void createEdgesForClassVariables(EList<Edge> edges, IType type) {
		type.fields.forEach[field | edges.add(createEdgeForField(field))]
	}
		
}