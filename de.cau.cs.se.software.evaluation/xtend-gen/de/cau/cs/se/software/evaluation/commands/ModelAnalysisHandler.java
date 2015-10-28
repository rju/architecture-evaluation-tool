package de.cau.cs.se.software.evaluation.commands;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import de.cau.cs.se.software.evaluation.commands.AbstractAnalysisHandler;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.jobs.CoCoMEAnalysisJob;
import de.cau.cs.se.software.evaluation.jobs.EMFMetamodelAnalysisJob;
import de.cau.cs.se.software.evaluation.jobs.GecoMegamodelAnalysisJob;
import de.cau.cs.se.software.evaluation.jobs.PCMDeploymentAnalysisJob;
import java.util.Iterator;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class ModelAnalysisHandler extends AbstractAnalysisHandler {
  /**
   * Empty default constructor, as proposed by checkstyle.
   */
  public ModelAnalysisHandler() {
    super();
  }
  
  @Override
  protected void executeCalculation(final ISelection selection, final IWorkbenchPage activePage, final Shell shell) throws ExecutionException {
    try {
      if ((selection instanceof IStructuredSelection)) {
        if ((selection instanceof ITreeSelection)) {
          final TreeSelection treeSelection = ((TreeSelection) selection);
          Iterator _iterator = treeSelection.iterator();
          final Iterator<IFile> iterator = Iterators.<IFile>filter(_iterator, IFile.class);
          boolean _hasNext = iterator.hasNext();
          if (_hasNext) {
            final IFile file = iterator.next();
            AbstractHypergraphAnalysisJob _switchResult = null;
            String _fileExtension = file.getFileExtension();
            switch (_fileExtension) {
              case "geco":
                IProject _project = file.getProject();
                _switchResult = new GecoMegamodelAnalysisJob(_project, file, shell);
                break;
              case "ecore":
                IProject _project_1 = file.getProject();
                _switchResult = new EMFMetamodelAnalysisJob(_project_1, file, shell);
                break;
              case "cocome":
                IProject _project_2 = file.getProject();
                String _name = file.getName();
                boolean _equals = _name.equals("megamodel.cocome");
                _switchResult = new CoCoMEAnalysisJob(_project_2, _equals, shell);
                break;
              case "system":
                IProject _project_3 = file.getProject();
                _switchResult = new PCMDeploymentAnalysisJob(_project_3, file, shell);
                break;
              default:
                Object _xblockexpression = null;
                {
                  String _fileExtension_1 = file.getFileExtension();
                  String _plus = ("The model type implied by the extension " + _fileExtension_1);
                  String _plus_1 = (_plus + 
                    " is not supported.");
                  MessageDialog.openInformation(shell, "Unknown Model Type", _plus_1);
                  _xblockexpression = null;
                }
                _switchResult = ((AbstractHypergraphAnalysisJob)_xblockexpression);
                break;
            }
            final AbstractHypergraphAnalysisJob job = _switchResult;
            boolean _notEquals = (!Objects.equal(job, null));
            if (_notEquals) {
              job.schedule();
              job.join();
              this.createAnalysisView(activePage);
            }
          } else {
            MessageDialog.openInformation(shell, "Empty selection", "No model selected for execution.");
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
