package de.cau.cs.se.software.evaluation.commands;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import de.cau.cs.se.software.evaluation.commands.AbstractComplexityAnalysisHandler;
import de.cau.cs.se.software.evaluation.jobs.GecoComplexityAnalysisJob;
import java.util.Iterator;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class GecoComplexityAnalysisHandler extends AbstractComplexityAnalysisHandler {
  /**
   * Empty default constructor, as proposed by checkstyle.
   */
  public GecoComplexityAnalysisHandler() {
    super();
  }
  
  @Override
  protected void executeCalculation(final ISelection selection, final IWorkbenchPage activePage, final Shell shell) throws ExecutionException {
    try {
      if ((selection instanceof IStructuredSelection)) {
        if ((selection instanceof ITreeSelection)) {
          final TreeSelection treeSelection = ((TreeSelection) selection);
          Iterator _iterator = treeSelection.iterator();
          Iterator<IFile> _filter = Iterators.<IFile>filter(_iterator, IFile.class);
          final Function1<IFile, Boolean> _function = (IFile file) -> {
            String _fileExtension = file.getFileExtension();
            return Boolean.valueOf("geco".equals(_fileExtension));
          };
          final IFile selectedElement = IteratorExtensions.<IFile>findFirst(_filter, _function);
          boolean _notEquals = (!Objects.equal(selectedElement, null));
          if (_notEquals) {
            IProject _project = selectedElement.getProject();
            final GecoComplexityAnalysisJob job = new GecoComplexityAnalysisJob(_project, selectedElement, shell);
            job.schedule();
            job.join();
            this.createAnalysisView(activePage);
          } else {
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
