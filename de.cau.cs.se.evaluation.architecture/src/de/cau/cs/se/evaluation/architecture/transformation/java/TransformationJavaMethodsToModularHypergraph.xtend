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
import java.util.ArrayList
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.EnumDeclaration
import org.eclipse.jdt.core.dom.FieldDeclaration
import org.eclipse.jdt.core.dom.MethodDeclaration

class TransformationJavaMethodsToModularHypergraph implements ITransformation {
	
	var ModularHypergraph modularSystem
	val IScope globalScope
	val IJavaProject project
	val IProgressMonitor monitor
	val List<AbstractTypeDeclaration> classList
		
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
		this.classList = new ArrayList<AbstractTypeDeclaration>
		for (clazz : classList) {
			val type = clazz.getTypeDeclarationForType(monitor,project)
			if (type != null) this.classList.add(type)
		}
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
	
	/**
	 * Create a node for implicit constructors of classes.
	 */
	def void createNodesForInplicitClassConstructors(EList<Node> nodes, AbstractTypeDeclaration type) {
		val tname = type.determineFullQualifiedName + "." + type.name.fullyQualifiedName
		if (!nodes.exists[node | node.name.equals(tname)]) {
			val module = modularSystem.modules.findFirst[it.name.equals(type.determineFullQualifiedName)]
			val newNode = type.createNodeForImplicitConstructor
			nodes.add(newNode)
			module.nodes.add(newNode)
		}	
	}
	
	/**
	 * create edges for invocations and edges for variable access.
	 */
	private def void createEdgesForInvocations(AbstractTypeDeclaration type, IJavaProject project) {
		if (type != null) {
			val Iterable<Modifier> modifiers = type.modifiers().filter(Modifier)
			if (type instanceof TypeDeclaration) {
				if (!type.isInterface && (modifiers.findFirst[(it as Modifier).isAbstract()] == null)) {
	 				val handler = new HandleStatementForMethodAndFieldAccess(project)
					/** check method bodies. */
					type.methods.forEach[method | 
						method.body.statements.forEach[handler.handle(modularSystem, type, method, it)]
					]
				}
			} else if (type instanceof EnumDeclaration) {
				if (modifiers.findFirst[(it as Modifier).isAbstract()] == null) {
	 				val handler = new HandleStatementForMethodAndFieldAccess(project)
					/** check method bodies. */
					type.bodyDeclarations.filter(MethodDeclaration).forEach[method | 
						method.body.statements.forEach[handler.handle(modularSystem, type, method, it)]
					]
				}
			}
		}
	}
	
	/**
	 * Resolve all methods of a given class.
	 */
	private def void createNodesForClassMethods(EList<Node> nodes, AbstractTypeDeclaration type) {
		if (type instanceof TypeDeclaration) {
			val module = modularSystem.modules.findFirst[it.name.equals(type.determineFullQualifiedName)]
			type.methods.forEach[method |
				val node = method.createNodeForMethod(type) 
				nodes.add(node)
				module.nodes.add(node)
			]
		} else if (type instanceof EnumDeclaration) {
			val module = modularSystem.modules.findFirst[it.name.equals(type.determineFullQualifiedName)]
			type.bodyDeclarations.filter(MethodDeclaration).forEach[method |
				val node = method.createNodeForMethod(type) 
				nodes.add(node)
				module.nodes.add(node)
			]
		}
	}
	
	/**
	 * Create edges for all variables in the given type.
	 */
	private def void createEdgesForClassVariables(EList<Edge> edges, AbstractTypeDeclaration type) {
		if (type instanceof TypeDeclaration) {
			type.fields.forEach[field | edges.add(field.createEdgeForField(type))]
		} else if (type instanceof EnumDeclaration) {
			type.bodyDeclarations.filter(FieldDeclaration).forEach[field | edges.add(field.createEdgeForField(type))]
		}
	}
		
}