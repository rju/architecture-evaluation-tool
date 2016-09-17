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
package de.cau.cs.se.software.evaluation.views;

import org.eclipse.jface.resource.ImageDescriptor;

import de.cau.cs.se.software.evaluation.Activator;

/**
 * This class defines image descriptors used for the UI of the log and
 * result view.
 *
 * @author Reiner Jung
 *
 */
public final class UIIcons {

	public final static ImageDescriptor ICON_DATA_EXPORT = Activator.getImageDescriptor("/icons/data-export.gif");
	public final static ImageDescriptor ICON_GRAPH_EXPORT = Activator.getImageDescriptor("/icons/graph-export.gif");
	public final static ImageDescriptor ICON_CLEAR_VIEW = Activator.getImageDescriptor("/icons/clear_co.gif");

	/** Do not instantiate. This is just a constant definition class. */
	private UIIcons() {}
}
