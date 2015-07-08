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
package de.cau.cs.se.software.evaluation.views;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration used as singleton for the result model provider.
 *
 * @author Reiner Jung
 *
 */
public enum ResultModelProvider {
	INSTANCE;

	private final List<NamedValue> values;

	private ResultModelProvider() {
		this.values = new ArrayList<NamedValue>();
	}

	/**
	 * Clear all values stored in the model.
	 */
	public void clearValues() {
		this.values.clear();
	}

	public List<NamedValue> getValues() {
		return this.values;
	}
}
