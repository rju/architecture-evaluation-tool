package de.cau.cs.se.software.evaluation.transformation

import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import java.util.List
import org.eclipse.core.runtime.IProgressMonitor
import java.util.ArrayList
import org.eclipse.jdt.core.dom.TypeDeclaration

class TransformationCyclomaticComplexity extends AbstractTransformation<List<AbstractTypeDeclaration>, List<Integer>> {
	
	val START_BUCKET = 100
	val MORE_BUCKET = 100
		
	var maxBucket = START_BUCKET
	var topBucket = 0
	var int[] buckets = newIntArrayOfSize(maxBucket)
	
	new(IProgressMonitor monitor) {
		super(monitor)
	}
	
	override generate(List<AbstractTypeDeclaration> input) {
		result = new ArrayList<Integer>()
		
		input.filter(TypeDeclaration).forEach[type | type.checkMethods]
		
		for (var i=0;i<maxBucket;i++) 
			result.add(buckets.get(i))
		
		return result
	}
	
	private def checkMethods(TypeDeclaration declaration) {
		declaration.methods.forEach[method |
			if (method.body != null) {
				val visitor = new CyclomaticComplexityVisitor("")
				method.accept(visitor)
				var bucket = visitor.cyclomatic
				while (bucket >= maxBucket) {
					maxBucket += MORE_BUCKET
					val int[] resizedBuckets = newIntArrayOfSize(maxBucket)
					buckets.forEach[value, i| resizedBuckets.set(i, value)]
					buckets = resizedBuckets
					System.out.println("Resizing bucket number " + maxBucket + " for " + method.name + " to " + bucket)
				}
				
				if (bucket > topBucket) {
					topBucket = bucket
				}
				
				buckets.set(bucket, buckets.get(bucket) + 1)
			}
		]
	}
	
}