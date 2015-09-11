package de.cau.cs.se.software.evaluation.jobs;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.cau.cs.se.geco.architecture.ArchitectureStandaloneSetup;
import de.cau.cs.se.geco.architecture.architecture.AspectModel;
import de.cau.cs.se.geco.architecture.architecture.Generator;
import de.cau.cs.se.geco.architecture.architecture.Metamodel;
import de.cau.cs.se.geco.architecture.architecture.MetamodelSequence;
import de.cau.cs.se.geco.architecture.architecture.Model;
import de.cau.cs.se.geco.architecture.architecture.Processor;
import de.cau.cs.se.geco.architecture.architecture.SourceModelNodeSelector;
import de.cau.cs.se.geco.architecture.architecture.TargetModelNodeType;
import de.cau.cs.se.geco.architecture.architecture.Weaver;
import de.cau.cs.se.geco.architecture.typing.ArchitectureTyping;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.views.ResultModelProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

@SuppressWarnings("all")
public class GecoComplexityAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final IFile file;
  
  /**
   * resource set for the compilation.
   */
  @Inject
  private XtextResourceSet resourceSet;
  
  public GecoComplexityAnalysisJob(final IProject project, final IFile file, final Shell shell) {
    super(project);
    this.file = file;
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
      final Hypergraph inputHypergraph = this.createGecoModelHypergraph(model);
      this.calculateSize(inputHypergraph, monitor, result);
      this.calculateComplexity(inputHypergraph, monitor, result);
    } else {
    }
    monitor.done();
    return Status.OK_STATUS;
  }
  
  private Hypergraph createGecoModelHypergraph(final Model model) {
    final Hypergraph hypergraph = HypergraphFactory.eINSTANCE.createHypergraph();
    final Map<Metamodel, Node> mmNodeMap = new HashMap<Metamodel, Node>();
    EList<MetamodelSequence> _metamodels = model.getMetamodels();
    final Consumer<MetamodelSequence> _function = (MetamodelSequence mm) -> {
      EList<Metamodel> _metamodels_1 = mm.getMetamodels();
      final Consumer<Metamodel> _function_1 = (Metamodel it) -> {
        String _name = it.getName();
        Node _createNode = this.createNode(hypergraph, _name, it);
        mmNodeMap.put(it, _createNode);
      };
      _metamodels_1.forEach(_function_1);
    };
    _metamodels.forEach(_function);
    EList<Processor> _processors = model.getProcessors();
    final Consumer<Processor> _function_1 = (Processor p) -> {
      boolean _matched = false;
      if (!_matched) {
        if (p instanceof Weaver) {
          _matched=true;
          JvmType _reference = ((Weaver)p).getReference();
          String _simpleName = _reference.getSimpleName();
          final Node weaverNode = this.createNode(hypergraph, _simpleName, p);
          SourceModelNodeSelector _resolveWeaverSourceModel = ArchitectureTyping.resolveWeaverSourceModel(((Weaver)p));
          Metamodel _reference_1 = _resolveWeaverSourceModel.getReference();
          final Node baseModelNode = mmNodeMap.get(_reference_1);
          String _name = weaverNode.getName();
          String _plus = (_name + "::");
          String _name_1 = baseModelNode.getName();
          String _plus_1 = (_plus + _name_1);
          this.createEdge(hypergraph, weaverNode, baseModelNode, _plus_1);
          AspectModel _aspectModel = ((Weaver)p).getAspectModel();
          if ((_aspectModel instanceof Generator)) {
            AspectModel _aspectModel_1 = ((Weaver)p).getAspectModel();
            final Generator generator = ((Generator) _aspectModel_1);
            Node _xifexpression = null;
            JvmType _reference_2 = generator.getReference();
            if ((_reference_2 instanceof JvmGenericType)) {
              Node _xblockexpression = null;
              {
                JvmType _reference_3 = generator.getReference();
                final JvmTypeReference aspectModelType = ArchitectureTyping.determineGeneratorOutputType(((JvmGenericType) _reference_3));
                String _simpleName_1 = aspectModelType.getSimpleName();
                String _plus_2 = ("anonymous model " + _simpleName_1);
                _xblockexpression = this.createNode(hypergraph, _plus_2, aspectModelType);
              }
              _xifexpression = _xblockexpression;
            } else {
              JvmType _reference_3 = generator.getReference();
              _xifexpression = this.createNode(hypergraph, ("anonymous model " + "type unknown"), _reference_3);
            }
            final Node aspectModelNode = _xifexpression;
            JvmType _reference_4 = generator.getReference();
            String _simpleName_1 = _reference_4.getSimpleName();
            AspectModel _aspectModel_2 = ((Weaver)p).getAspectModel();
            final Node generatorNode = this.createNode(hypergraph, _simpleName_1, _aspectModel_2);
            SourceModelNodeSelector _sourceModel = generator.getSourceModel();
            Metamodel _reference_5 = _sourceModel.getReference();
            final Node sourceModelNode = mmNodeMap.get(_reference_5);
            String _name_2 = generatorNode.getName();
            String _plus_2 = (_name_2 + "::");
            String _name_3 = sourceModelNode.getName();
            String _plus_3 = (_plus_2 + _name_3);
            this.createEdge(hypergraph, generatorNode, sourceModelNode, _plus_3);
            String _name_4 = generatorNode.getName();
            String _plus_4 = (_name_4 + "::");
            String _name_5 = aspectModelNode.getName();
            String _plus_5 = (_plus_4 + _name_5);
            this.createEdge(hypergraph, generatorNode, aspectModelNode, _plus_5);
            String _name_6 = weaverNode.getName();
            String _plus_6 = (_name_6 + "::");
            String _name_7 = aspectModelNode.getName();
            String _plus_7 = (_plus_6 + _name_7);
            this.createEdge(hypergraph, weaverNode, aspectModelNode, _plus_7);
          } else {
            AspectModel _aspectModel_3 = ((Weaver)p).getAspectModel();
            final Node aspectModelNode_1 = mmNodeMap.get(((Metamodel) _aspectModel_3));
            String _name_8 = weaverNode.getName();
            String _plus_8 = (_name_8 + "::");
            String _name_9 = aspectModelNode_1.getName();
            String _plus_9 = (_plus_8 + _name_9);
            this.createEdge(hypergraph, weaverNode, aspectModelNode_1, _plus_9);
          }
        }
      }
      if (!_matched) {
        if (p instanceof Generator) {
          _matched=true;
          JvmType _reference = ((Generator)p).getReference();
          String _simpleName = _reference.getSimpleName();
          final Node generatorNode = this.createNode(hypergraph, _simpleName, p);
          SourceModelNodeSelector _sourceModel = ((Generator)p).getSourceModel();
          Metamodel _reference_1 = _sourceModel.getReference();
          boolean _notEquals = (!Objects.equal(_reference_1, null));
          if (_notEquals) {
            SourceModelNodeSelector _sourceModel_1 = ((Generator)p).getSourceModel();
            Metamodel _reference_2 = _sourceModel_1.getReference();
            final Node sourceModelNode = mmNodeMap.get(_reference_2);
            String _name = generatorNode.getName();
            String _plus = (_name + "::");
            String _name_1 = sourceModelNode.getName();
            String _plus_1 = (_plus + _name_1);
            this.createEdge(hypergraph, generatorNode, sourceModelNode, _plus_1);
          }
          TargetModelNodeType _targetModel = ((Generator)p).getTargetModel();
          Metamodel _reference_3 = _targetModel.getReference();
          final Node targetModelNode = mmNodeMap.get(_reference_3);
          String _name_2 = generatorNode.getName();
          String _plus_2 = (_name_2 + "::");
          String _name_3 = targetModelNode.getName();
          String _plus_3 = (_plus_2 + _name_3);
          this.createEdge(hypergraph, generatorNode, targetModelNode, _plus_3);
        }
      }
    };
    _processors.forEach(_function_1);
    return hypergraph;
  }
  
  private Node createNode(final Hypergraph hypergraph, final String name, final EObject element) {
    final ModelElementTrace reference = HypergraphFactory.eINSTANCE.createModelElementTrace();
    reference.setElement(element);
    final Node node = HypergraphFactory.eINSTANCE.createNode();
    node.setName(name);
    node.setDerivedFrom(reference);
    EList<Node> _nodes = hypergraph.getNodes();
    _nodes.add(node);
    return node;
  }
  
  private boolean createEdge(final Hypergraph hypergraph, final Node source, final Node target, final String name) {
    boolean _xblockexpression = false;
    {
      final Edge edge = HypergraphFactory.eINSTANCE.createEdge();
      edge.setName(name);
      EList<Edge> _edges = hypergraph.getEdges();
      _edges.add(edge);
      EList<Edge> _edges_1 = source.getEdges();
      _edges_1.add(edge);
      EList<Edge> _edges_2 = target.getEdges();
      _xblockexpression = _edges_2.add(edge);
    }
    return _xblockexpression;
  }
}
