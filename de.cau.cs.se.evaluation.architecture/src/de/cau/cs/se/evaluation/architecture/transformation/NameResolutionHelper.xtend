package de.cau.cs.se.evaluation.architecture.transformation

import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.IType

class NameResolutionHelper {
	def static determineFullQualifiedName(TypeDeclaration clazz, MethodDeclaration method) {
		clazz.determineFullQualifiedName + "." + method.name.fullyQualifiedName
	}
	
	def static determineFullQualifiedName(TypeDeclaration clazz, VariableDeclarationFragment fragment) {
		clazz.determineFullQualifiedName + "." + fragment.name.fullyQualifiedName
	}
	
	def static determineFullQualifiedName(TypeDeclaration clazz) {
		val name = (clazz.parent as CompilationUnit).package.name.fullyQualifiedName + "." + clazz.name.fullyQualifiedName
		return name
	}
	
}