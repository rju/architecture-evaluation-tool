package de.cau.cs.se.software.evaluation.transformation

import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import java.util.List
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ASTNode

class TransformationLinesOfCode extends AbstractTransformation<List<AbstractTypeDeclaration>,Long> {
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override transform(List<AbstractTypeDeclaration> input) {
		result = 0L
		
		input.forEach[type |
			if (type.parent instanceof CompilationUnit) {
				val compilationUnit = type.parent as CompilationUnit
				var loc = compilationUnit.calculateLOC(type) - compilationUnit.calculateLOC(type.javadoc)
				result = result + Integer.valueOf(loc).longValue
			}
		]
		
		return result
	}
	
	private def calculateLOC(CompilationUnit compilationUnit, ASTNode node) {
		if (node != null)
			compilationUnit.getLineNumber(node.length + node.startPosition) - 
			compilationUnit.getLineNumber(node.startPosition) + 1
		else
			0
	}
	
}