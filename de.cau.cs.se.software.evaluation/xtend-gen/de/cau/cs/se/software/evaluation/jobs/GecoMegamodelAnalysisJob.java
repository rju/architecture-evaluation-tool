package de.cau.cs.se.software.evaluation.jobs;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.cau.cs.se.geco.architecture.ArchitectureStandaloneSetup;
import de.cau.cs.se.geco.architecture.architecture.Model;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.transformation.geco.TransformationGecoMegamodelToHypergraph;
import de.cau.cs.se.software.evaluation.views.ResultModelProvider;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

/**
 * run job for the GECO megamodel evaluation.
 */
@SuppressWarnings("all")
public class GecoMegamodelAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final IFile file;
  
  /**
   * resource set for the compilation.
   */
  @Inject
  private XtextResourceSet resourceSet;
  
  private Shell shell;
  
  public GecoMegamodelAnalysisJob(final IProject project, final IFile file, final Shell shell) {
    super(project);
    this.file = file;
    this.shell = shell;
  }
  
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    ArchitectureStandaloneSetup _architectureStandaloneSetup = new ArchitectureStandaloneSetup();
    final Injector injector = _architectureStandaloneSetup.createInjectorAndDoEMFRegistration();
    injector.injectMembers(this);
    this.resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
    IPath _fullPath = this.file.getFullPath();
    String _string = _fullPath.toString();
    URI _createPlatformResourceURI = URI.createPlatformResourceURI(_string, true);
    final Resource source = this.resourceSet.getResource(_createPlatformResourceURI, true);
    EList<EObject> _contents = source.getContents();
    int _size = _contents.size();
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      final ResultModelProvider result = ResultModelProvider.INSTANCE;
      EList<EObject> _contents_1 = source.getContents();
      EObject _get = _contents_1.get(0);
      final Model model = ((Model) _get);
      final TransformationGecoMegamodelToHypergraph gecoMegamodel = new TransformationGecoMegamodelToHypergraph(monitor);
      gecoMegamodel.transform(model);
      Hypergraph _result = gecoMegamodel.getResult();
      this.calculateSize(_result, monitor, result);
      Hypergraph _result_1 = gecoMegamodel.getResult();
      this.calculateComplexity(_result_1, monitor, result);
    } else {
      MessageDialog.openError(this.shell, "Model empty", "The selected resource is empty.");
    }
    monitor.done();
    return Status.OK_STATUS;
  }
}
