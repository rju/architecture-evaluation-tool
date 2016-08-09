package de.cau.cs.se.software.evaluation.jobs;

import com.google.inject.Inject;
import com.google.inject.Injector;
import de.cau.cs.se.geco.architecture.ArchitectureStandaloneSetup;
import de.cau.cs.se.geco.architecture.architecture.GecoModel;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.transformation.geco.TransformationGecoMegamodelToHypergraph;
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider;
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
      final AnalysisResultModelProvider result = AnalysisResultModelProvider.INSTANCE;
      EList<EObject> _contents_1 = source.getContents();
      EObject _get = _contents_1.get(0);
      final GecoModel model = ((GecoModel) _get);
      final TransformationGecoMegamodelToHypergraph gecoMegamodel = new TransformationGecoMegamodelToHypergraph(monitor);
      gecoMegamodel.generate(model);
      String _name = this.project.getName();
      Hypergraph _result = gecoMegamodel.getResult();
      EList<Node> _nodes = _result.getNodes();
      int _size_1 = _nodes.size();
      result.addResult(_name, "number of nodes", _size_1);
      String _name_1 = this.project.getName();
      Hypergraph _result_1 = gecoMegamodel.getResult();
      EList<Edge> _edges = _result_1.getEdges();
      int _size_2 = _edges.size();
      result.addResult(_name_1, "number of edges", _size_2);
      Hypergraph _result_2 = gecoMegamodel.getResult();
      this.calculateSize(_result_2, monitor, result);
      Hypergraph _result_3 = gecoMegamodel.getResult();
      this.calculateComplexity(_result_3, monitor, result);
    } else {
      MessageDialog.openError(this.shell, "Model empty", "The selected resource is empty.");
    }
    monitor.done();
    return Status.OK_STATUS;
  }
}
