package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.transformation.IScope
import de.cau.cs.se.evaluation.architecture.transformation.Scope
import org.eclipse.jdt.core.dom.CompilationUnit
import java.util.Map
import java.util.HashMap
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.ImportDeclaration

class JavaLocalScope extends Scope implements IScope {

	
	val Map<String,IType> importedClasses = new HashMap<String,IType>
	
	public new (CompilationUnit unit, IScope parent) {
		super(parent)
		unit.imports.forEach[ref |
			val importDecl = ref as ImportDeclaration
			val fqn = importDecl.name.fullyQualifiedName
			val type = parent.getType(fqn)
			importedClasses.put(fqn.split('\\.').last, type)
		]
	}
	
	override IType getType(String name) {
		val result =  importedClasses.get(name)
		if (result == null) {
			if (parentScope != null) {
				return parentScope.getType(name)
			} else {
				return null
			}
		} else {	
			return result
		}
	}
	
}