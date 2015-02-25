package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.TypeDeclaration

class HypergraphJDTDOMExtension {
	
	/**
	 * Get compilation unit for JDT type.
	 */
	def static CompilationUnit getUnitForType(IType type) {
		val ASTParser parser = ASTParser.newParser(AST.JLS8)
		val options = JavaCore.getOptions()
				
 		JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options)
 		
 		parser.setCompilerOptions(options)
 		parser.setSource(type.compilationUnit.buffer.contents.toCharArray())
 		
 		return parser.createAST(null) as CompilationUnit
 	}
 	
 	def static TypeDeclaration getTypeDeclarationForType(IType type) {
 		val object = type.getUnitForType.types.get(0)
		if (object instanceof TypeDeclaration) {
			return object as TypeDeclaration
		} else
			return null
 	}
}