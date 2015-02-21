package de.cau.cs.se.evaluation.architecture.transformation.java

import de.cau.cs.se.evaluation.architecture.transformation.Scope
import de.cau.cs.se.evaluation.architecture.transformation.IScope
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IType
import java.util.HashMap
import java.util.Map

class JavaPackageScope extends Scope implements IScope {
	
	val Map<String,IType> typesMap = new HashMap<String,IType>()
		
	new(IPackageFragment packageFragment, IScope parentScope) {
		super(parentScope)
		packageFragment.children.forEach[
			if (it instanceof ICompilationUnit) {
				val unit = it as ICompilationUnit
				val type = unit.allTypes.get(0)
				if (type != null) {
					val fqn = type.fullyQualifiedName.split('\\.')
					typesMap.put(fqn.last, type)
				}
			}
		]
	}
	
	override IType getType(String name) {
		val result = typesMap.get(name)
		if (result == null) {
			if (parentScope != null) {
				return parentScope.getType(name)
			} else {
				return null
			}
		} else
			return result
	}
	
}