package de.cau.cs.se.software.evaluation.commands;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import de.cau.cs.se.software.evaluation.commands.AbstractAnalysisHandler;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.jobs.IAnalysisJobProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class ModelAnalysisHandler extends AbstractAnalysisHandler {
  private final Map<String, IAnalysisJobProvider> providers = new HashMap<String, IAnalysisJobProvider>();
  
  /**
   * Initialization constructor.
   */
  public ModelAnalysisHandler() {
    super();
    final IExtensionRegistry registry = Platform.getExtensionRegistry();
    final IConfigurationElement[] config = registry.getConfigurationElementsFor(AbstractHypergraphAnalysisJob.HYPERGRAPH_ANALYSIS_JOBS);
    try {
      final Consumer<IConfigurationElement> _function = (IConfigurationElement element) -> {
        final IConfigurationElement ext = element;
        if ((ext instanceof IAnalysisJobProvider)) {
          final IAnalysisJobProvider provider = ((IAnalysisJobProvider) ext);
          String _fileExtension = provider.getFileExtension();
          this.providers.put(_fileExtension, provider);
        }
      };
      ((List<IConfigurationElement>)Conversions.doWrapArray(config)).forEach(_function);
    } catch (final Throwable _t) {
      if (_t instanceof CoreException) {
        final CoreException ex = (CoreException)_t;
        String _message = ex.getMessage();
        System.out.println(_message);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
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
            String _fileExtension = file.getFileExtension();
            final IAnalysisJobProvider provider = this.providers.get(_fileExtension);
            boolean _notEquals = (!Objects.equal(provider, null));
            if (_notEquals) {
              IProject _project = file.getProject();
              final AbstractHypergraphAnalysisJob job = provider.createAnalysisJob(_project, file, shell);
              job.schedule();
              job.join();
              this.createAnalysisView(activePage);
            } else {
              String _fileExtension_1 = file.getFileExtension();
              String _plus = ("The model type implied by the extension " + _fileExtension_1);
              String _plus_1 = (_plus + 
                " is not supported.");
              MessageDialog.openInformation(shell, "Unknown Model Type", _plus_1);
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
