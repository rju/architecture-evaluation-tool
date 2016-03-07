/**
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
 */
package de.cau.cs.se.software.evaluation.transformation;

import de.cau.cs.se.geco.architecture.framework.IGenerator;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Abstract transformation class also defining the general interface
 * of transformations.
 */
@SuppressWarnings("all")
public abstract class AbstractTransformation<S extends Object, T extends Object> implements IGenerator<S, T> {
  protected final IProgressMonitor monitor;
  
  protected T result;
  
  public AbstractTransformation(final IProgressMonitor monitor) {
    this.monitor = monitor;
  }
  
  /**
   * Get the previously computed result.
   */
  public T getResult() {
    return this.result;
  }
}
