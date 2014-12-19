package de.cau.cs.se.evaluation.architecture.views

import org.eclipse.jface.viewers.ITableLabelProvider
import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.swt.graphics.Image
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.ISharedImages

class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	override String getColumnText(Object obj, int index) {
		return this.getText(obj)
	}

	override Image getColumnImage(Object obj, int index) {
		return this.getImage(obj)
	}

	override Image getImage(Object obj) {
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT)
	}
	
}