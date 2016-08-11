package de.cau.cs.se.software.evaluation.transformation.geco;

import com.google.common.base.Objects;
import de.cau.cs.se.geco.architecture.architecture.AspectModel;
import de.cau.cs.se.geco.architecture.architecture.CombinedModel;
import de.cau.cs.se.geco.architecture.architecture.Fragment;
import de.cau.cs.se.geco.architecture.architecture.GecoModel;
import de.cau.cs.se.geco.architecture.architecture.Generator;
import de.cau.cs.se.geco.architecture.architecture.Model;
import de.cau.cs.se.geco.architecture.architecture.ModelSequence;
import de.cau.cs.se.geco.architecture.architecture.SeparateModels;
import de.cau.cs.se.geco.architecture.architecture.SourceModelSelector;
import de.cau.cs.se.geco.architecture.architecture.TargetModel;
import de.cau.cs.se.geco.architecture.architecture.Weaver;
import de.cau.cs.se.geco.architecture.typing.ArchitectureTyping;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.common.types.JvmGenericType;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * Transform geco model to a hypergraph.
 */
@SuppressWarnings("all")
public class TransformationGecoMegamodelToHypergraph extends AbstractTransformation<GecoModel, Hypergraph> {
  public TransformationGecoMegamodelToHypergraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public Hypergraph generate(final GecoModel input) {
    Hypergraph _createHypergraph = HypergraphFactory.eINSTANCE.createHypergraph();
    this.result = _createHypergraph;
    final Map<Model, Node> modelNodeMap = new HashMap<Model, Node>();
    EList<ModelSequence> _models = input.getModels();
    final Consumer<ModelSequence> _function = (ModelSequence sequence) -> {
      EList<Model> _models_1 = sequence.getModels();
      final Consumer<Model> _function_1 = (Model it) -> {
        String _name = it.getName();
        Node _createNode = HypergraphCreationHelper.createNode(this.result, _name, it);
        modelNodeMap.put(it, _createNode);
      };
      _models_1.forEach(_function_1);
    };
    _models.forEach(_function);
    EList<Fragment> _fragments = input.getFragments();
    final Consumer<Fragment> _function_1 = (Fragment p) -> {
      boolean _matched = false;
      if (p instanceof Weaver) {
        _matched=true;
        JvmType _reference = ((Weaver)p).getReference();
        String _simpleName = _reference.getSimpleName();
        final Node weaverNode = HypergraphCreationHelper.createNode(this.result, _simpleName, p);
        SourceModelSelector _resolveWeaverSourceModel = ArchitectureTyping.resolveWeaverSourceModel(((Weaver)p));
        Model _reference_1 = _resolveWeaverSourceModel.getReference();
        final Node baseModelNode = modelNodeMap.get(_reference_1);
        String _name = weaverNode.getName();
        String _plus = (_name + "::");
        String _name_1 = baseModelNode.getName();
        String _plus_1 = (_plus + _name_1);
        HypergraphCreationHelper.createEdge(this.result, weaverNode, baseModelNode, _plus_1, null);
        AspectModel _aspectModel = ((Weaver)p).getAspectModel();
        boolean _matched_1 = false;
        if (_aspectModel instanceof CombinedModel) {
          _matched_1=true;
          AspectModel _aspectModel_1 = ((Weaver)p).getAspectModel();
          this.createWeaver(((CombinedModel) _aspectModel_1), modelNodeMap, weaverNode);
        }
        if (!_matched_1) {
          if (_aspectModel instanceof SeparateModels) {
            _matched_1=true;
            AspectModel _aspectModel_1 = ((Weaver)p).getAspectModel();
            this.createSeparatePointcutWeaver(((SeparateModels) _aspectModel_1), modelNodeMap, weaverNode);
          }
        }
      }
      if (!_matched) {
        if (p instanceof Generator) {
          _matched=true;
          JvmType _reference = ((Generator)p).getReference();
          String _simpleName = _reference.getSimpleName();
          final Node generatorNode = HypergraphCreationHelper.createNode(this.result, _simpleName, p);
          SourceModelSelector _sourceModel = ((Generator)p).getSourceModel();
          Model _reference_1 = _sourceModel.getReference();
          boolean _notEquals = (!Objects.equal(_reference_1, null));
          if (_notEquals) {
            SourceModelSelector _sourceModel_1 = ((Generator)p).getSourceModel();
            Model _reference_2 = _sourceModel_1.getReference();
            final Node sourceModelNode = modelNodeMap.get(_reference_2);
            String _name = generatorNode.getName();
            String _plus = (_name + "::");
            String _name_1 = sourceModelNode.getName();
            String _plus_1 = (_plus + _name_1);
            HypergraphCreationHelper.createEdge(this.result, generatorNode, sourceModelNode, _plus_1, null);
          }
          TargetModel _targetModel = ((Generator)p).getTargetModel();
          Model _reference_3 = _targetModel.getReference();
          final Node targetModelNode = modelNodeMap.get(_reference_3);
          String _name_2 = generatorNode.getName();
          String _plus_2 = (_name_2 + "::");
          String _name_3 = targetModelNode.getName();
          String _plus_3 = (_plus_2 + _name_3);
          HypergraphCreationHelper.createEdge(this.result, generatorNode, targetModelNode, _plus_3, null);
        }
      }
    };
    _fragments.forEach(_function_1);
    return this.result;
  }
  
  /**
   * create point cut reference for weaver.
   */
  private Edge createSeparatePointcutWeaver(final SeparateModels model, final Map<Model, Node> modelNodeMap, final Node weaverNode) {
    Edge _xblockexpression = null;
    {
      CombinedModel _advice = model.getAdvice();
      this.createWeaver(_advice, modelNodeMap, weaverNode);
      TargetModel _pointcut = model.getPointcut();
      Model _reference = _pointcut.getReference();
      final Node pointcutModelNode = modelNodeMap.get(_reference);
      String _name = weaverNode.getName();
      String _plus = (_name + "::");
      String _name_1 = pointcutModelNode.getName();
      String _plus_1 = (_plus + _name_1);
      _xblockexpression = HypergraphCreationHelper.createEdge(this.result, weaverNode, pointcutModelNode, _plus_1, null);
    }
    return _xblockexpression;
  }
  
  /**
   * create advice/aspect edge.
   */
  private Edge createWeaver(final CombinedModel adviceModel, final Map<Model, Node> modelNodeMap, final Node weaverNode) {
    Edge _switchResult = null;
    boolean _matched = false;
    if (adviceModel instanceof Generator) {
      _matched=true;
      Edge _xblockexpression = null;
      {
        Node _xifexpression = null;
        JvmType _reference = ((Generator)adviceModel).getReference();
        if ((_reference instanceof JvmGenericType)) {
          Node _xblockexpression_1 = null;
          {
            JvmType _reference_1 = ((Generator)adviceModel).getReference();
            final JvmTypeReference aspectModelType = ArchitectureTyping.determineGeneratorOutputType(((JvmGenericType) _reference_1));
            String _simpleName = aspectModelType.getSimpleName();
            String _plus = ("anonymous model " + _simpleName);
            _xblockexpression_1 = HypergraphCreationHelper.createNode(this.result, _plus, aspectModelType);
          }
          _xifexpression = _xblockexpression_1;
        } else {
          JvmType _reference_1 = ((Generator)adviceModel).getReference();
          _xifexpression = HypergraphCreationHelper.createNode(this.result, ("anonymous model " + "type unknown"), _reference_1);
        }
        final Node aspectModelNode = _xifexpression;
        JvmType _reference_2 = ((Generator)adviceModel).getReference();
        String _simpleName = _reference_2.getSimpleName();
        final Node generatorNode = HypergraphCreationHelper.createNode(this.result, _simpleName, adviceModel);
        SourceModelSelector _sourceModel = ((Generator)adviceModel).getSourceModel();
        Model _reference_3 = _sourceModel.getReference();
        final Node sourceModelNode = modelNodeMap.get(_reference_3);
        String _name = generatorNode.getName();
        String _plus = (_name + "::");
        String _name_1 = sourceModelNode.getName();
        String _plus_1 = (_plus + _name_1);
        HypergraphCreationHelper.createEdge(this.result, generatorNode, sourceModelNode, _plus_1, null);
        String _name_2 = generatorNode.getName();
        String _plus_2 = (_name_2 + "::");
        String _name_3 = aspectModelNode.getName();
        String _plus_3 = (_plus_2 + _name_3);
        HypergraphCreationHelper.createEdge(this.result, generatorNode, aspectModelNode, _plus_3, null);
        String _name_4 = weaverNode.getName();
        String _plus_4 = (_name_4 + "::");
        String _name_5 = aspectModelNode.getName();
        String _plus_5 = (_plus_4 + _name_5);
        _xblockexpression = HypergraphCreationHelper.createEdge(this.result, weaverNode, aspectModelNode, _plus_5, null);
      }
      _switchResult = _xblockexpression;
    }
    if (!_matched) {
      if (adviceModel instanceof TargetModel) {
        _matched=true;
        Edge _xblockexpression = null;
        {
          final Node aspectModelNode = modelNodeMap.get(((Model) adviceModel));
          String _name = weaverNode.getName();
          String _plus = (_name + "::");
          String _name_1 = aspectModelNode.getName();
          String _plus_1 = (_plus + _name_1);
          _xblockexpression = HypergraphCreationHelper.createEdge(this.result, weaverNode, aspectModelNode, _plus_1, null);
        }
        _switchResult = _xblockexpression;
      }
    }
    return _switchResult;
  }
  
  @Override
  public int workEstimate(final GecoModel input) {
    EList<ModelSequence> _models = input.getModels();
    int _size = _models.size();
    EList<Fragment> _fragments = input.getFragments();
    int _size_1 = _fragments.size();
    return (_size + _size_1);
  }
}
