package de.cau.cs.se.software.evaluation.transformation

import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import java.util.List
import org.eclipse.core.runtime.IProgressMonitor
import java.util.ArrayList
import org.eclipse.jdt.core.dom.TypeDeclaration

class TransformationCyclomaticComplexity extends AbstractTransformation<List<AbstractTypeDeclaration>, List<Integer>> {
	
	val MAX_BUCKET = 100
	
	var topBucket = 0
	val int[] buckets = newIntArrayOfSize(MAX_BUCKET)
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(List<AbstractTypeDeclaration> input) {
		result = new ArrayList<Integer>()
		
		input.filter(TypeDeclaration).forEach[type | type.checkMethods]
		
		for (var i=0;i<=topBucket;i++) 
			result.add(buckets.get(i))
		
		return result
	}
	
	private def checkMethods(TypeDeclaration declaration) {
		declaration.methods.forEach[method |
			if (method.body != null) {
				val visitor = new CyclomaticComplexityVisitor("")
				method.accept(visitor)
				var bucket = visitor.cyclomatic
				if (bucket > MAX_BUCKET)
					bucket = MAX_BUCKET
				if (bucket > topBucket) {
					topBucket = bucket
				}
				buckets.set(bucket, buckets.get(bucket) + 1)
			}
		]
	}
	
}