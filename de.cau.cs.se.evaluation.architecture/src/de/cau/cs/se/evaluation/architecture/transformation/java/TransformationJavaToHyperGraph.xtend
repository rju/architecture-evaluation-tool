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
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.Flags
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet
import de.cau.cs.se.evaluation.architecture.transformation.IScope
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.IMethod
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.ITypeHierarchy

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
				
				if (it.parent instanceof IType) {
					val parent = it.parent as IType
					parent.methods.forEach[method | if (Flags.isPublic(method.flags)) classMethodMap.put(it, method) ]
				}
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
					system.nodes.findMatchingNode(type).edges.add(edge)
				}]
			}
			
			monitor?.worked(1)
		]
		
		return system
	}

	/**
	 * Recurse into the type hierarchy and find all public methods.
	 * 
	 * @param type the type to be resolved.
	 * @param classInterfaceMap the present map
	 */
	private def Map<IType,IMethod> resolvePublicClassInterface(IType type, Map<IType,IMethod> classInterfaceMap) {
		if (type.parent != null) {
			if (type.parent instanceof IType) {
				val parent = type.parent as IType
				return getPublicClassInterface(type, resolvePublicClassInterface(parent, classInterfaceMap))
			}
		}
		
		return getPublicClassInterface(type, classInterfaceMap) 
	}
	
	/**
	 * Determine all methods of a class which are public and not abstract.
	 */
	private def Map<IType,IMethod> getPublicClassInterface(IType type, Map<IType,IMethod> classInterfaceMap) {
		type.methods.forEach[method | if (Flags.isPublic(method.flags) && !Flags.isAbstract(method.flags)) classInterfaceMap.put(type, method) ]
		return classInterfaceMap
	}
	
	/**
	 * Create names for edges based on a counter. 
	 */
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
	
	private def Node findMatchingNode(List<Node> nodes, IType type) {
		val node = nodes.findNode(type)
		if (node == null) {
			val subtype = type.getParentTypesList.findFirst[nodes.findNode(it) != null]
			if (subtype != null)
				return nodes.findNode(subtype)
			else {
				throw new Exception("No subtype of " + type.elementName + " is matching any node.")
			}
		} else
			return node
	}

	private def List<IType> getParentTypesList(IType type) {
		val List<IType> result = new ArrayList<IType>()
		val ITypeHierarchy typeHierarchy = type.newTypeHierarchy(monitor)
		for (subtype : typeHierarchy.getSubclasses(type)) {
			result.add(subtype)
			result.addAll(subtype.getParentTypesList)
		}
		return result
	}
	
	/**
	 * Determine all classes which are called from the given class.
	 * This determines the required interfaces of a "component" while the
	 * set of exposed (public) methods represent the provided interface of
	 * a component.
	 * 
	 * @param type the type which required interfaces must be determined
	 * 
	 * @return list of called classes
	 */
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
 				
		val object = unit.types.get(0)
		if (object instanceof TypeDeclaration) {
			val declaredType = object as TypeDeclaration
			val Iterable<Modifier> modifiers = declaredType.modifiers().filter(Modifier)
			if (!declaredType.isInterface && (modifiers.findFirst[(it as Modifier).isAbstract()] == null)) {
				/** check method bodies. */
				declaredType.methods.forEach[method | 
					method.body.statements.forEach[classCalls.addUnique(resolver.resolve(it))]
				]
				/** check field declarations. */
				declaredType.fields.forEach[field | classCalls.addUnique(field.type.findType(scope))]
			}
		}
	
		
	
		return classCalls
	}

	// TODO this should be moved to the scope handler
	private def IType findType(Type type, IScope scope) {
		if (type.isArrayType) {
			// TODO array type handling
			return null
		} else if (type.isSimpleType) {
			scope.getType((type as SimpleType).name.fullyQualifiedName)
		}
	}	
	
}