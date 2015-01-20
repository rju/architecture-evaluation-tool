package de.cau.cs.se.evaluation.architecture.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("all")
public class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
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
