package de.cau.cs.se.software.evaluation.transformation.geco;

import com.google.common.base.Objects;
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
public class TransformationGecoMegamodelToHypergraph extends AbstractTransformation<Model, Hypergraph> {
  public TransformationGecoMegamodelToHypergraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public Hypergraph transform(final Model input) {
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
    EList<Processor> _processors = input.getProcessors();
    final Consumer<Processor> _function_1 = (Processor p) -> {
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
                _xblockexpression = HypergraphCreationHelper.createNode(this.result, _plus_2, aspectModelType);
              }
              _xifexpression = _xblockexpression;
            } else {
              JvmType _reference_3 = generator.getReference();
              _xifexpression = HypergraphCreationHelper.createNode(this.result, ("anonymous model " + "type unknown"), _reference_3);
            }
            final Node aspectModelNode = _xifexpression;
            JvmType _reference_4 = generator.getReference();
            String _simpleName_1 = _reference_4.getSimpleName();
            AspectModel _aspectModel_2 = ((Weaver)p).getAspectModel();
            final Node generatorNode = HypergraphCreationHelper.createNode(this.result, _simpleName_1, _aspectModel_2);
            SourceModelNodeSelector _sourceModel = generator.getSourceModel();
            Metamodel _reference_5 = _sourceModel.getReference();
            final Node sourceModelNode = mmNodeMap.get(_reference_5);
            String _name_2 = generatorNode.getName();
            String _plus_2 = (_name_2 + "::");
            String _name_3 = sourceModelNode.getName();
            String _plus_3 = (_plus_2 + _name_3);
            HypergraphCreationHelper.createEdge(this.result, generatorNode, sourceModelNode, _plus_3, null);
            String _name_4 = generatorNode.getName();
            String _plus_4 = (_name_4 + "::");
            String _name_5 = aspectModelNode.getName();
            String _plus_5 = (_plus_4 + _name_5);
            HypergraphCreationHelper.createEdge(this.result, generatorNode, aspectModelNode, _plus_5, null);
            String _name_6 = weaverNode.getName();
            String _plus_6 = (_name_6 + "::");
            String _name_7 = aspectModelNode.getName();
            String _plus_7 = (_plus_6 + _name_7);
            HypergraphCreationHelper.createEdge(this.result, weaverNode, aspectModelNode, _plus_7, null);
          } else {
            AspectModel _aspectModel_3 = ((Weaver)p).getAspectModel();
            final Node aspectModelNode_1 = mmNodeMap.get(((Metamodel) _aspectModel_3));
            String _name_8 = weaverNode.getName();
            String _plus_8 = (_name_8 + "::");
            String _name_9 = aspectModelNode_1.getName();
            String _plus_9 = (_plus_8 + _name_9);
            HypergraphCreationHelper.createEdge(this.result, weaverNode, aspectModelNode_1, _plus_9, null);
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
    _processors.forEach(_function_1);
    return this.result;
  }
}
