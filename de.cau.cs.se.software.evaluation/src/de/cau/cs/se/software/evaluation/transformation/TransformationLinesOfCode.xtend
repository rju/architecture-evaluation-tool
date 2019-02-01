/***************************************************************************
 * Copyright (C) 2015 Reiner Jung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
 package de.cau.cs.se.software.evaluation.transformation

import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import java.util.List
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ASTNode

/**
 * Transformation computing the lines of code metric 
 * for a Java project.
 * 
 * @author Reiner Jung
 */
class TransformationLinesOfCode extends AbstractTransformation<List<AbstractTypeDeclaration>,Long> {
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(List<AbstractTypeDeclaration> input) {
		result = 0L
		
		input.forEach[type |
			if (type.parent instanceof CompilationUnit) {
				val compilationUnit = type.parent as CompilationUnit
				var loc = compilationUnit.calculateLOC(type) - compilationUnit.calculateLOC(type.javadoc)
				result = result + Integer.valueOf(loc).longValue
			}
			monitor.worked(1)
		]
		
		return result
	}
	
	private def calculateLOC(CompilationUnit compilationUnit, ASTNode node) {
		if (node !== null)
			compilationUnit.getLineNumber(node.length + node.startPosition) - 
			compilationUnit.getLineNumber(node.startPosition) + 1
		else
			0
	}
	
	override workEstimate(List<AbstractTypeDeclaration> input) {
		input.size
	}
	
}