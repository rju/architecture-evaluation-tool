package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.transformation.pcm.TransformationPCMDeploymentToHypergraph;
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
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("all")
public class PCMDeploymentAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final IFile file;
  
  private Shell shell;
  
  public PCMDeploymentAnalysisJob(final IProject project, final IFile file, final Shell shell) {
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
      final AnalysisResultModelProvider result = AnalysisResultModelProvider.INSTANCE;
      EList<EObject> _contents_1 = source.getContents();
      EObject _get = _contents_1.get(0);
      final org.palladiosimulator.pcm.system.System model = ((org.palladiosimulator.pcm.system.System) _get);
      final TransformationPCMDeploymentToHypergraph deploymentModel = new TransformationPCMDeploymentToHypergraph(monitor);
      deploymentModel.generate(model);
      String _name = this.project.getName();
      ModularHypergraph _result = deploymentModel.getResult();
      EList<Module> _modules = _result.getModules();
      int _size_1 = _modules.size();
      result.addResult(_name, "number of modules", _size_1);
      String _name_1 = this.project.getName();
      ModularHypergraph _result_1 = deploymentModel.getResult();
      EList<Node> _nodes = _result_1.getNodes();
      int _size_2 = _nodes.size();
      result.addResult(_name_1, "number of nodes", _size_2);
      String _name_2 = this.project.getName();
      ModularHypergraph _result_2 = deploymentModel.getResult();
      EList<Edge> _edges = _result_2.getEdges();
      int _size_3 = _edges.size();
      result.addResult(_name_2, "number of edges", _size_3);
      ModularHypergraph _result_3 = deploymentModel.getResult();
      this.calculateSize(_result_3, monitor, result);
      ModularHypergraph _result_4 = deploymentModel.getResult();
      this.calculateComplexity(_result_4, monitor, result);
      ModularHypergraph _result_5 = deploymentModel.getResult();
      this.calculateCoupling(_result_5, monitor, result);
      ModularHypergraph _result_6 = deploymentModel.getResult();
      this.calculateCohesion(_result_6, monitor, result);
    } else {
      MessageDialog.openError(this.shell, "Model empty", "The selected resource is empty.");
    }
    monitor.done();
    return Status.OK_STATUS;
  }
}
