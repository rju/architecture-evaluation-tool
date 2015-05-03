package de.cau.cs.se.evaluation.architecture.transformation.java.old

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import java.util.ArrayList
import java.util.List
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.TypeDeclaration
import de.cau.cs.se.evaluation.architecture.transformation.IScope
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.ITypeHierarchy
import de.cau.cs.se.evaluation.architecture.transformation.ITransformation

/**
 * Transform a java project based on a list of classes to a hypergraph.
 */
class TransformationJavaClassesToHypergraph implements ITransformation {
	
	extension JavaTypeHelper javaTypeHelper = new JavaTypeHelper()
		
	int edgeId = 0
	val IScope globalScope
	val IProgressMonitor monitor
	val List<IType> classList
	val Hypergraph system = HypergraphFactory.eINSTANCE.createHypergraph
	
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
	 * @param eclipse progres monitor
	 */
	public new(GlobalJavaScope scope, List<IType> classList, IProgressMonitor monitor) {
		this.globalScope = scope
		this.classList = classList 
		this.monitor = monitor
	}
	
	/**
	 * Return the generated result.
	 */
	def Hypergraph getSystem() {
		return system
	}
		
	/**
	 * Transform a list of types into a hypergraph.
	 */
	override void transform () {
		monitor?.subTask("Constructing hypergraph")
		
		/** connections can only be resolved when all nodes are present. */
		classList.forEach[it.createNode(system)]
		classList.forEach[it.connectNodes(system)]
	}
	
	/**
	 * determine nodes.
	 * 
	 */
	private def void createNode(IType type, Hypergraph system) {
		monitor?.subTask("Constructing hypergraph - types " + type.elementName)
			
		if (type.isClass) {
			/** add node and register public methods (determine provided interface). */
			val Node node = HypergraphFactory.eINSTANCE.createNode
			node.name = type.elementName
			system.nodes.add(node)		
		}
			
		monitor?.worked(1)
	}
	
	/**
	 * Connect nodes.
	 */
	private def void connectNodes(IType sourceType, Hypergraph system) {
		monitor?.subTask("Constructing hypergraph - references " + sourceType.elementName)
		if (sourceType.isClass) {
			sourceType.findAllCalledClasses.filter[it.isClass && !it.isBinary].forEach[destinationType | {
				val Edge edge = HypergraphFactory.eINSTANCE.createEdge
				edge.name = this.getNextEdgeName
				
				system.edges.add(edge)
				system.nodes.findNode(sourceType).edges.add(edge)
				system.nodes.findMatchingNode(destinationType).edges.add(edge)
			}]
		}
			
		monitor?.worked(1)
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
		val ITypeHierarchy typeHierarchy = type.newTypeHierarchy(null)
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