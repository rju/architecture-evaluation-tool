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
package de.cau.cs.se.software.evaluation.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("all")
public class AnalysisResultTableLabelProvider extends LabelProvider implements ITableLabelProvider {
  public String getColumnText(final Object obj, final int index) {
    return this.getText(obj);
  }
  
  public Image getColumnImage(final Object obj, final int index) {
    return this.getImage(obj);
  }
  
  public Image getImage(final Object obj) {
    IWorkbench _workbench = PlatformUI.getWorkbench();
    ISharedImages _sharedImages = _workbench.getSharedImages();
    return _sharedImages.getImage(ISharedImages.IMG_OBJ_ELEMENT);
  }
}
