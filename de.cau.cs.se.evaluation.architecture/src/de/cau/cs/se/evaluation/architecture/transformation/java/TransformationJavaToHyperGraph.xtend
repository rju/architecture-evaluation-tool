package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map
import org.eclipse.jdt.core.IMethod
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.Statement
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.IJavaProject

class TransformationJavaToHyperGraph {
	
	extension JavaTypeHelper javaTypeHelper
		
	val ResolveStatementForClassAccess resolver
	int edgeId = 0
	
	public new (IJavaProject project) {
		val List<IJavaScope> scopes = new ArrayList<IJavaScope>()
		scopes.add(new GlobalJavaScope(project))
		 resolver = new ResolveStatementForClassAccess(scopes)
	}
		
	def Hypergraph transform (List<IType> classList) {
		val Hypergraph system = HypergraphFactory.eINSTANCE.createHypergraph
		val Map<IType,IMethod> classMethodMap = new HashMap<IType,IMethod>()
		
		classList.forEach[
			if (it.isClass) {
				/** add node and register public methods (determine provided interface). */
				val Node node = HypergraphFactory.eINSTANCE.createNode
				node.name = it.elementName
				system.nodes.add(node)
				
				findAllPublicMethods(it, classMethodMap)
			}
		]
		
		/** resolve references to provided interfaces. */
		classList.forEach[
			if (it.isClass) {
				it.findAllCalledClasses.forEach[type | {
					val edge = createEdge(it, type)
					system.edges.add(edge)
					system.nodes.findNode(it).edges.add(edge)
					system.nodes.findNode(type).edges.add(edge)
				}]
			}
		]
		
		return system
	}
	
	private def String getNextEdgeName() {
		this.edgeId++
		return Integer.toString(this.edgeId)
	} 
	
	private def createEdge(IType originType, IType destinationType) {
		val Edge edge = HypergraphFactory.eINSTANCE.createEdge
		edge.name = this.getNextEdgeName
					
		return edge
	}
	
	/**
	 * Find the node related to the type based on the name of the node.
	 */
	def Node findNode(List<Node> nodes, IType type) {
		return nodes.findFirst[it.name.equals(type.elementName)]
	}

	
	private def List<IType> findAllCalledClasses(IType type) {
		val List<IType> classCalls = new ArrayList<IType>()
		
		val ASTParser parser = ASTParser.newParser(AST.JLS3)
		val options = JavaCore.getOptions()
 		JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options)
 		parser.setCompilerOptions(options)
 		parser.setSource(type.compilationUnit.buffer.contents.toCharArray())
 		
		val CompilationUnit unit = parser.createAST(null) as CompilationUnit
		
		/** check method bodies. */
		val object = unit.types.get(0)
		if (object instanceof TypeDeclaration) {
			val declaredType = object as TypeDeclaration
			if (!declaredType.isInterface) {
				declaredType.methods.forEach[method | classCalls.addUnique(method.body.statements.findClassCall)]		
			}
		}
		
		/** check field declarations. */
		
		/** check complexity of parent class */

		return classCalls
	}
		
	def List<IType> findClassCall(List<Statement> list) {
		val List<IType> types = new ArrayList<IType>()
		
		list.forEach[statement | types.addUnique(resolver.resolve(statement))]
		
		return types
	}
		
	/** ------------------------------------------- */
	
	
	private def findAllPublicMethods(IType type, Map<IType, IMethod> map) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
		
}