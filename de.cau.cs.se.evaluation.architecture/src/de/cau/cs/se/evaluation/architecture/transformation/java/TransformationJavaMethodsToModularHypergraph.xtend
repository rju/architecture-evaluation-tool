package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.transformation.ITransformation
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
import org.eclipse.core.runtime.IProgressMonitor
import java.util.List
import org.eclipse.jdt.core.IType
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import org.eclipse.emf.common.util.EList
import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.Modifier

import org.eclipse.jdt.core.IJavaProject
import java.util.ArrayList
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.AST

import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaHypergraphElementFactory.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.java.JavaASTEvaluation.*
import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.ArrayType
import org.eclipse.jdt.core.dom.ParameterizedType
import org.eclipse.jdt.core.dom.PrimitiveType
import org.eclipse.jdt.core.dom.QualifiedType
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.UnionType
import org.eclipse.jdt.core.dom.WildcardType
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IMethodBinding

class TransformationJavaMethodsToModularHypergraph implements ITransformation {
	
	var ModularHypergraph modularSystem
	val IJavaProject project
	val IProgressMonitor monitor
	val List<AbstractTypeDeclaration> classList
	val List<String> dataTypePatterns
	val List<String> observedSystemPatterns
		
	/**
	 * Create a new hypergraph generator utilizing a specific hypergraph set for a specific set of java resources.
	 *  
	 * @param hypergraphSet the hypergraph set where the generated hypergraph will be added to
	 * @param scope the global scoper used to resolve classes during transformation
	 * @param eclipse progress monitor
	 */
	public new(IJavaProject project, List<IType> classList, List<String> dataTypePatterns, List<String> observedSystemPatterns, IProgressMonitor monitor) {
		this.project = project
		this.monitor = monitor
		this.dataTypePatterns = dataTypePatterns
		this.observedSystemPatterns = observedSystemPatterns
		this.classList = new ArrayList<AbstractTypeDeclaration>
		this.classList.fillClassList(classList, dataTypePatterns, observedSystemPatterns)
	}
	
	public def getClassList() {
		return classList
	}
	
	/**
	 * Find all classes in the IType list which belong to the observed system.
	 * 
	 * @param classes the classes of the observed system
	 * @param types the types found by scanning the project
	 * @param dataTypePatterns pattern list for data types to be excluded
	 * @param observedSystemPatterns pattern list for classes to be included
	 */
	private def void fillClassList(List<AbstractTypeDeclaration> declarations, List<IType> types, List<String> dataTypePatterns, List<String> observedSystemPatterns) {
		types.forEach[jdtType |
			val unit = jdtType.getUnitForType(project)
			if (unit != null) {
				unit.types.forEach[unitType |
					if (unitType instanceof TypeDeclaration) {
						val type = unitType as TypeDeclaration
						val typeBinding = type.resolveBinding
						val name = typeBinding.determineFullyQualifiedName
						if (observedSystemPatterns.exists[name.matches(it)])
							if (!isClassDataType(typeBinding, dataTypePatterns))
								declarations.add(type)
				 	}
				 ]
			 }
		]
	}
	
	/**
	 * Get compilation unit for JDT type.
	 */
	private def CompilationUnit getUnitForType(IType type, IJavaProject project) {
		val outerTypeName = type.packageFragment.elementName + "." + type.elementName
		if (observedSystemPatterns.exists[outerTypeName.matches(it)]) {
			monitor.subTask("parsing " + outerTypeName)
			val options = JavaCore.getOptions()
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options)
	 		
			val ASTParser parser = ASTParser.newParser(AST.JLS8)
			parser.setProject(project)	
	 		parser.setCompilerOptions(options)
	 		parser.kind = ASTParser.K_COMPILATION_UNIT
	 		parser.source = type.compilationUnit.buffer.contents.toCharArray()
	 		parser.unitName = type.compilationUnit.elementName
	 		parser.resolveBindings = true
	 		parser.bindingsRecovery = true
	 		parser.statementsRecovery = true
	 		
	 		return parser.createAST(null) as CompilationUnit
	 	} else
	 		return null
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
		classList.forEach[clazz | modularSystem.modules.add(createModuleForTypeBinding(clazz.resolveBinding))]

		// define edges for all internal variables of a class
		classList.forEach[clazz | modularSystem.edges.createEdgesForClassProperties(clazz, dataTypePatterns)]

		// find all method declarations and create nodes for it, grouped by class as modules
		classList.forEach[clazz | modularSystem.nodes.createNodesForMethods(clazz)]
		classList.filter[clazz | clazz.hasImplicitConstructor].
			forEach[clazz |
				val node = createNodeForImplicitConstructor(clazz.resolveBinding)
				val module = modularSystem.modules.findFirst[((it.derivedFrom as TypeTrace).type as ITypeBinding).isSubTypeCompatible(clazz.resolveBinding)]
				module.nodes.add(node) 
				modularSystem.nodes.add(node)
			]
		
		classList.forEach[clazz | resolveEdges(modularSystem, dataTypePatterns, clazz)]
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
					field.fragments.forEach[fragment | edges.add(createDataEdge(type, fragment))]
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
	
	/**
	 * Determine if the given type is a data type.
	 * 
	 * @param type the type to be evaluated
	 * @param dataTypes a list of data types
	 * 
	 * @return returns true if the given type is a data type and not a behavior type.
	 */
	private def boolean isClassDataType(ITypeBinding typeBinding, List<String> dataTypePatterns) {
		// TODO this might be to simple.
		val name = typeBinding.determineFullyQualifiedName
		return dataTypePatterns.exists[pattern | name.matches(pattern)]
	}
		
}