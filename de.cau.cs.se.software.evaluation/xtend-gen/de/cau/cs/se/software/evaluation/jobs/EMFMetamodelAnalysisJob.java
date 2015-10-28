package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.transformation.emf.TransformationEMFInstanceToHypergraph;
import de.cau.cs.se.software.evaluation.views.NamedValue;
import de.cau.cs.se.software.evaluation.views.ResultModelProvider;
import java.util.List;
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
      List<NamedValue> _values = result.getValues();
      String _name = this.project.getName();
      ModularHypergraph _result = emfMetaModel.getResult();
      EList<Module> _modules = _result.getModules();
      int _size_1 = _modules.size();
      NamedValue _namedValue = new NamedValue(_name, "number of modules", _size_1);
      _values.add(_namedValue);
      List<NamedValue> _values_1 = result.getValues();
      String _name_1 = this.project.getName();
      ModularHypergraph _result_1 = emfMetaModel.getResult();
      EList<Node> _nodes = _result_1.getNodes();
      int _size_2 = _nodes.size();
      NamedValue _namedValue_1 = new NamedValue(_name_1, "number of nodes", _size_2);
      _values_1.add(_namedValue_1);
      List<NamedValue> _values_2 = result.getValues();
      String _name_2 = this.project.getName();
      ModularHypergraph _result_2 = emfMetaModel.getResult();
      EList<Edge> _edges = _result_2.getEdges();
      int _size_3 = _edges.size();
      NamedValue _namedValue_2 = new NamedValue(_name_2, "number of edges", _size_3);
      _values_2.add(_namedValue_2);
      ModularHypergraph _result_3 = emfMetaModel.getResult();
      this.calculateSize(_result_3, monitor, result);
      ModularHypergraph _result_4 = emfMetaModel.getResult();
      this.calculateComplexity(_result_4, monitor, result);
      ModularHypergraph _result_5 = emfMetaModel.getResult();
      this.calculateCoupling(_result_5, monitor, result);
      ModularHypergraph _result_6 = emfMetaModel.getResult();
      this.calculateCohesion(_result_6, monitor, result);
    } else {
      MessageDialog.openError(this.shell, "Model empty", "The selected resource is empty.");
    }
    monitor.done();
    return Status.OK_STATUS;
  }
}
