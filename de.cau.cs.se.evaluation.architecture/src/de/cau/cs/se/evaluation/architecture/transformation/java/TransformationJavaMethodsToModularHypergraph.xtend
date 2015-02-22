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
import org.eclipse.jdt.core.IField
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.IMethod

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*

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
		classList.forEach[clazz | modularSystem.edges.createEdgesForClassVariables(clazz)]

		// find all method declarations and create nodes for it, grouped by class as modules
		classList.forEach[clazz | modularSystem.nodes.createNodesForClassMethods(clazz)]
		
		// find all method invocations create edges for them 
		// find all variable accesses from expressions inside methods
		classList.forEach[clazz | createEdgesForInvocations(clazz)]
		
	}
	
	/**
	 * create edges for invocations and edges for variable access.
	 */
	private def void createEdgesForInvocations(IType type) {
		System.out.println("XX")	
		val ASTParser parser = ASTParser.newParser(AST.JLS8)
		val options = JavaCore.getOptions()
				
 		JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options)
 		
 		parser.setCompilerOptions(options)
 		parser.setSource(type.compilationUnit.buffer.contents.toCharArray())
 		
 		val CompilationUnit unit = parser.createAST(null) as CompilationUnit
 		val scope = new JavaLocalScope(unit, new JavaPackageScope(type.packageFragment, globalScope))
 		val handler = new HandleStatementForMethodAndFieldAccess(scope)
 				
		val object = unit.types.get(0)
		if (object instanceof TypeDeclaration) {
			val declaredType = object as TypeDeclaration
			val Iterable<Modifier> modifiers = declaredType.modifiers().filter(Modifier)
			if (!declaredType.isInterface && (modifiers.findFirst[(it as Modifier).isAbstract()] == null)) {
				/** check method bodies. */
				declaredType.methods.forEach[method | 
					method.body.statements.forEach[handler.handle(modularSystem, declaredType, method, it)]
				]
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
	 * Create a node for a given method.
	 */
	private def createNodeForMethod(IMethod method) {
		val node = HypergraphFactory.eINSTANCE.createNode
		// TODO redo this based on name qualification resolving
		node.name = method.compilationUnit.parent.elementName + "." + 
			method.parent.elementName + "." + method.elementName
		val derivedFrom = HypergraphFactory.eINSTANCE.createMethodTrace
		derivedFrom.method = method
		node.derivedFrom = derivedFrom
		
		return node
	}
	
	/**
	 * Create edges for all variables in the given type.
	 */
	private def void createEdgesForClassVariables(EList<Edge> edges, IType type) {
		type.fields.forEach[field | edges.add(createEdgeForField(field))]
	}
	
	/**
	 * Create for one IField an edge.
	 */
	private def createEdgeForField(IField field) {
		val edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = field.compilationUnit.parent.elementName + "." + field.parent.elementName + "." + field.elementName
		val derivedFrom = HypergraphFactory.eINSTANCE.createFieldTrace
		derivedFrom.field = field
		edge.derivedFrom = derivedFrom
		
		return edge
	}
	
	/**
	 * Create a module for each class in the list. Pointing to the class as derived from
	 * element. 
	 * 
	 * @return one new module
	 */
	private def createModuleForClass(IType type) {
		val module = HypergraphFactory.eINSTANCE.createModule
		module.name = type.fullyQualifiedName
		val derivedFrom = HypergraphFactory.eINSTANCE.createTypeTrace
		derivedFrom.type = type
		module.derivedFrom = derivedFrom
		
		return module
	}
	
}