package de.cau.cs.se.evaluation.architecture.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

@SuppressWarnings("all")
public class AnalysisResultTableContentProvider implements IStructuredContentProvider {
  public void inputChanged(final Viewer v, final Object oldInput, final Object newInput) {
  }
  
  public void dispose() {
  }
  
  public Object[] getElements(final Object parent) {
    return new Object[] { "One", "Two", "Three" };
  }
}
