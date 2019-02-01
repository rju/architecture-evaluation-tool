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

import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import java.util.List
import org.eclipse.core.runtime.IProgressMonitor
import java.util.ArrayList
import org.eclipse.jdt.core.dom.TypeDeclaration
import de.cau.cs.se.software.evaluation.views.LogModelProvider

/**
 * Transformation computing the cylomatic complexity of methods in
 * a Java project.
 * 
 * @author Reiner Jung
 */
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
		
		for (var i=0;i<=topBucket;i++) 
			result.add(buckets.get(i))
			
		monitor.worked(input.size)
		
		return result
	}
	
	private def checkMethods(TypeDeclaration declaration) {
		declaration.methods.forEach[method |
			if (method.body !== null) {
				val visitor = new CyclomaticComplexityVisitor("")
				method.accept(visitor)
				var bucket = visitor.cyclomatic
				while (bucket >= maxBucket) {
					maxBucket += MORE_BUCKET
					val int[] resizedBuckets = newIntArrayOfSize(maxBucket)
					buckets.forEach[value, i| resizedBuckets.set(i, value)]
					buckets = resizedBuckets
					LogModelProvider.INSTANCE.addMessage("Metric management", "Resizing bucket array to " + maxBucket + " for " + method.name + " with a complexity of " + bucket)
				}
				
				if (bucket > topBucket) {
					topBucket = bucket
				}
				
				buckets.set(bucket, buckets.get(bucket) + 1)
			}
		]
	}
	
	override workEstimate(List<AbstractTypeDeclaration> input) {
		input.size
	}
	
}