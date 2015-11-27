package de.cau.cs.se.software.evaluation.transformation.geco;

import com.google.common.base.Objects;
import de.cau.cs.se.geco.architecture.architecture.AdviceModel;
import de.cau.cs.se.geco.architecture.architecture.AspectModel;
import de.cau.cs.se.geco.architecture.architecture.Fragment;
import de.cau.cs.se.geco.architecture.architecture.GecoModel;
import de.cau.cs.se.geco.architecture.architecture.Generator;
import de.cau.cs.se.geco.architecture.architecture.Metamodel;
import de.cau.cs.se.geco.architecture.architecture.MetamodelSequence;
import de.cau.cs.se.geco.architecture.architecture.SeparatePointcutAdviceModel;
import de.cau.cs.se.geco.architecture.architecture.SourceModelNodeSelector;
import de.cau.cs.se.geco.architecture.architecture.TargetModelNodeType;
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
    final Map<Metamodel, Node> mmNodeMap = new HashMap<Metamodel, Node>();
    EList<MetamodelSequence> _metamodels = input.getMetamodels();
    final Consumer<MetamodelSequence> _function = (MetamodelSequence mm) -> {
      EList<Metamodel> _metamodels_1 = mm.getMetamodels();
      final Consumer<Metamodel> _function_1 = (Metamodel it) -> {
        String _name = it.getName();
        Node _createNode = HypergraphCreationHelper.createNode(this.result, _name, it);
        mmNodeMap.put(it, _createNode);
      };
      _metamodels_1.forEach(_function_1);
    };
    _metamodels.forEach(_function);
    EList<Fragment> _fragments = input.getFragments();
    final Consumer<Fragment> _function_1 = (Fragment p) -> {
      boolean _matched = false;
      if (!_matched) {
        if (p instanceof Weaver) {
          _matched=true;
          JvmType _reference = ((Weaver)p).getReference();
          String _simpleName = _reference.getSimpleName();
          final Node weaverNode = HypergraphCreationHelper.createNode(this.result, _simpleName, p);
          SourceModelNodeSelector _resolveWeaverSourceModel = ArchitectureTyping.resolveWeaverSourceModel(((Weaver)p));
          Metamodel _reference_1 = _resolveWeaverSourceModel.getReference();
          final Node baseModelNode = mmNodeMap.get(_reference_1);
          String _name = weaverNode.getName();
          String _plus = (_name + "::");
          String _name_1 = baseModelNode.getName();
          String _plus_1 = (_plus + _name_1);
          HypergraphCreationHelper.createEdge(this.result, weaverNode, baseModelNode, _plus_1, null);
          AspectModel _aspectModel = ((Weaver)p).getAspectModel();
          boolean _matched_1 = false;
          if (!_matched_1) {
            if (_aspectModel instanceof AdviceModel) {
              _matched_1=true;
              AspectModel _aspectModel_1 = ((Weaver)p).getAspectModel();
              this.createWeaver(((AdviceModel) _aspectModel_1), mmNodeMap, weaverNode);
            }
          }
          if (!_matched_1) {
            if (_aspectModel instanceof SeparatePointcutAdviceModel) {
              _matched_1=true;
              AspectModel _aspectModel_1 = ((Weaver)p).getAspectModel();
              this.createSeparatePointcutWeaver(((SeparatePointcutAdviceModel) _aspectModel_1), mmNodeMap, weaverNode);
            }
          }
        }
      }
      if (!_matched) {
        if (p instanceof Generator) {
          _matched=true;
          JvmType _reference = ((Generator)p).getReference();
          String _simpleName = _reference.getSimpleName();
          final Node generatorNode = HypergraphCreationHelper.createNode(this.result, _simpleName, p);
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
            HypergraphCreationHelper.createEdge(this.result, generatorNode, sourceModelNode, _plus_1, null);
          }
          TargetModelNodeType _targetModel = ((Generator)p).getTargetModel();
          Metamodel _reference_3 = _targetModel.getReference();
          final Node targetModelNode = mmNodeMap.get(_reference_3);
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
  private Edge createSeparatePointcutWeaver(final SeparatePointcutAdviceModel model, final Map<Metamodel, Node> mmNodeMap, final Node weaverNode) {
    Edge _xblockexpression = null;
    {
      AdviceModel _advice = model.getAdvice();
      this.createWeaver(_advice, mmNodeMap, weaverNode);
      TargetModelNodeType _pointcut = model.getPointcut();
      Metamodel _reference = _pointcut.getReference();
      final Node pointcutModelNode = mmNodeMap.get(_reference);
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
  private Edge createWeaver(final AdviceModel adviceModel, final Map<Metamodel, Node> mmNodeMap, final Node weaverNode) {
    Edge _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
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
          SourceModelNodeSelector _sourceModel = ((Generator)adviceModel).getSourceModel();
          Metamodel _reference_3 = _sourceModel.getReference();
          final Node sourceModelNode = mmNodeMap.get(_reference_3);
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
    }
    if (!_matched) {
      if (adviceModel instanceof TargetModelNodeType) {
        _matched=true;
        Edge _xblockexpression = null;
        {
          final Node aspectModelNode = mmNodeMap.get(((Metamodel) adviceModel));
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
}
