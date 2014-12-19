package de.cau.cs.se.evaluation.architecture.views

import org.eclipse.jface.viewers.IStructuredContentProvider
import org.eclipse.jface.viewers.Viewer

class TableContentProvider implements IStructuredContentProvider {
	
	override void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	override void dispose() {
	}

	override Object[] getElements(Object parent) {
		return #[ "One", "Two", "Three" ]
	}

}