package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.transformation.emf.TransformationEMFInstanceToHypergraph;
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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("all")
public class EMFMetamodelAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final IFile file;
  
  private Shell shell;
  
  public EMFMetamodelAnalysisJob(final IProject project, final IFile file, final Shell shell) {
    super(project);
    this.file = file;
    this.shell = shell;
  }
  
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    final ResourceSetImpl resourceSet = new ResourceSetImpl();
    IPath _fullPath = this.file.getFullPath();
    String _string = _fullPath.toString();
    URI _createPlatformResourceURI = URI.createPlatformResourceURI(_string, true);
    final Resource source = resourceSet.getResource(_createPlatformResourceURI, true);
    EList<EObject> _contents = source.getContents();
    int _size = _contents.size();
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      final ResultModelProvider result = ResultModelProvider.INSTANCE;
      EList<EObject> _contents_1 = source.getContents();
      EObject _get = _contents_1.get(0);
      final EPackage model = ((EPackage) _get);
      final TransformationEMFInstanceToHypergraph emfMetaModel = new TransformationEMFInstanceToHypergraph(monitor);
      emfMetaModel.transform(model);
      ModularHypergraph _result = emfMetaModel.getResult();
      this.calculateSize(_result, monitor, result);
      ModularHypergraph _result_1 = emfMetaModel.getResult();
      this.calculateComplexity(_result_1, monitor, result);
      ModularHypergraph _result_2 = emfMetaModel.getResult();
      final double coupling = this.calculateCoupling(_result_2, monitor, result);
      ModularHypergraph _result_3 = emfMetaModel.getResult();
      this.calculateCohesion(_result_3, monitor, result, coupling);
    } else {
      MessageDialog.openError(this.shell, "Model empty", "The selected resource is empty.");
    }
    monitor.done();
    return Status.OK_STATUS;
  }
}
