/***************************************************************************
 * Copyright 2015
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
package de.cau.cs.se.evaluation.architecture.transformation

import org.eclipse.core.runtime.IProgressMonitor

/**
 * Abstract transformation class also defining the general interface
 * of transformations.
 */
abstract class AbstractTransformation<S,T> {
	
	val protected IProgressMonitor monitor
	var protected T result
	var protected S input
		
	new(IProgressMonitor monitor) {
		this.monitor = monitor
	}
	
	def T transform()
	
	def T getResult() {
		this.result
	}
	
	def void setInput(S input) {
		this.input = input
	}
	
}