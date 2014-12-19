package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.jdt.core.dom.Name
import org.eclipse.jdt.core.IJavaProject

class GlobalJavaScope implements IJavaScope {
	
	val IJavaProject project
	
	public new(IJavaProject project) {
		this.project = project
	}
	
	override getType(Name name) {
		this.project.findType(name.fullyQualifiedName)
	}
	
}