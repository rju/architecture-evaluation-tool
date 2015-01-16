package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.Flags
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet
import de.cau.cs.se.evaluation.architecture.transformation.IScope
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.IMethod

/**
 * Transform a java project based on a list of classes to a hypergraph.
 */
class TransformationJavaToHyperGraph {
	
	extension JavaTypeHelper javaTypeHelper = new JavaTypeHelper()
		
	int edgeId = 0
	val HypergraphSet hypergraphSet
	val IScope globalScope
	val IProgressMonitor monitor
	
	public new (HypergraphSet hypergraphSet, IScope scope) {
		this.globalScope = scope
		this.hypergraphSet = hypergraphSet 
		this.monitor = null
	}
	
	public new(HypergraphSet hypergraphSet, GlobalJavaScope scope, IProgressMonitor monitor) {
		this.globalScope = scope
		this.hypergraphSet = hypergraphSet 
		this.monitor = monitor
	}
		
	def Hypergraph transform (List<IType> classList) {
		monitor?.subTask("Constructing hypergraph")
		val Hypergraph system = HypergraphFactory.eINSTANCE.createHypergraph
		hypergraphSet.graphs.add(system)
		val Map<IType,IMethod> classMethodMap = new HashMap<IType,IMethod>()
		
		classList.forEach[
			monitor?.subTask("Constructing hypergraph - types " + it.elementName)
			
			if (it.isClass) {
				/** add node and register public methods (determine provided interface). */
				val Node node = HypergraphFactory.eINSTANCE.createNode
				node.name = it.elementName
				system.nodes.add(node)
				hypergraphSet.nodes.add(node)
				
				// TODO include parent types?	
				it.methods.forEach[method | if (Flags.isPublic(method.flags)) classMethodMap.put(it, method) ]
			}
			
			monitor?.worked(1)
		]
		
		/** resolve references to provided interfaces. */
		classList.forEach[
			monitor?.subTask("Constructing hypergraph - references " + it.elementName)
			if (it.isClass) {
				it.findAllCalledClasses.filter[it.isClass && !it.isBinary].forEach[type | {
					val Edge edge = HypergraphFactory.eINSTANCE.createEdge
					edge.name = this.getNextEdgeName
					
					system.edges.add(edge)
					hypergraphSet.edges.add(edge)

					system.nodes.findNode(it).edges.add(edge)
					system.nodes.findNode(type).edges.add(edge)
				}]
			}
			
			monitor?.worked(1)
		]
		
		return system
	}
	
	private def String getNextEdgeName() {
		this.edgeId++
		return Integer.toString(this.edgeId)
	} 
		
	/**
	 * Find the node related to the type based on the name of the node.
	 */
	private def Node findNode(List<Node> nodes, IType type) {
		return nodes.findFirst[it.name.equals(type.elementName)]
	}

	
	private def List<IType> findAllCalledClasses(IType type) {
		val List<IType> classCalls = new ArrayList<IType>()
		
		val ASTParser parser = ASTParser.newParser(AST.JLS8)
		val options = JavaCore.getOptions()
				
 		JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options)
 		
 		parser.setCompilerOptions(options)
 		parser.setSource(type.compilationUnit.buffer.contents.toCharArray())
 		
 		val CompilationUnit unit = parser.createAST(null) as CompilationUnit
 		val scope = new JavaLocalScope(unit, new JavaPackageScope(type.packageFragment, globalScope))
 		val resolver = new ResolveStatementForClassAccess(scope)
 				
		/** check method bodies. */
		val object = unit.types.get(0)
		if (object instanceof TypeDeclaration) {
			val declaredType = object as TypeDeclaration
			val Iterable<Modifier> modifiers = declaredType.modifiers().filter(Modifier)
			if (!declaredType.isInterface && (modifiers.findFirst[(it as Modifier).isAbstract()] == null)) {
				declaredType.methods.forEach[method | classCalls.addUnique(method.body.statements.findClassCall(resolver))]
			}
		}
	
		/** check field declarations. */
	
		return classCalls
	}
		
	private def List<IType> findClassCall(List<Statement> list, ResolveStatementForClassAccess resolver) {
		val List<IType> types = new ArrayList<IType>()
		
		list.forEach[statement | types.addUnique(resolver.resolve(statement))]
		
		return types
	}
		
	
		
}