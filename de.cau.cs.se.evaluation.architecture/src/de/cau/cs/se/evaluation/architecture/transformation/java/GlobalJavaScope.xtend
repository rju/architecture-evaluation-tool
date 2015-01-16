package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.jdt.core.IJavaProject
import java.util.List
import org.eclipse.jdt.core.IType
import de.cau.cs.se.evaluation.architecture.transformation.Scope
import de.cau.cs.se.evaluation.architecture.transformation.IScope

/**
 * Provide the scope over different Java projects. 
 */
class GlobalJavaScope extends Scope implements IScope {
	
	val List<IJavaProject> projects
	
	public new(List<IJavaProject> projects, IScope parent) {
		super(parent)
		this.projects = projects
	}
	
	override getType(String name) {
		var IType type = null 
		for (IJavaProject project : projects) {
			type = project.findType(name)
			if (type != null)
				return type	
		}
		
		if (parentScope != null)
			return parentScope.getType(name)
		else
			return null
	}
	
}

