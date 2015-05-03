package de.cau.cs.se.evaluation.architecture.transformation.java.old

import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration

class HypergraphJDTDOMExtension {
	
	/**
	 * Get compilation unit for JDT type.
	 */
	def static CompilationUnit getUnitForType(IType type, IProgressMonitor monitor, IJavaProject project) {
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
 	}
 	
 	def static AbstractTypeDeclaration getTypeDeclarationForType(IType type, IProgressMonitor monitor, IJavaProject project) {
 		val object = type.getUnitForType(monitor, project).types.get(0)
		if (object instanceof AbstractTypeDeclaration) {
			return object as AbstractTypeDeclaration
		} else
			return null
 	}
}