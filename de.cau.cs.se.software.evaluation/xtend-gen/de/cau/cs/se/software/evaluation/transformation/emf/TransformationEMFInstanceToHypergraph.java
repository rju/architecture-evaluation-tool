package de.cau.cs.se.software.evaluation.transformation.emf;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleReference;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.HypergraphCreationHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

@SuppressWarnings("all")
public class TransformationEMFInstanceToHypergraph extends AbstractTransformation<EPackage, ModularHypergraph> {
  public TransformationEMFInstanceToHypergraph(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public ModularHypergraph generate(final EPackage input) {
    ModularHypergraph _createModularHypergraph = HypergraphFactory.eINSTANCE.createModularHypergraph();
    this.result = _createModularHypergraph;
    final ArrayList<Module> packages = new ArrayList<Module>();
    final HashMap<EClass, Node> nodeMap = new HashMap<EClass, Node>();
    this.resolvePackages(packages, input);
    final Consumer<Module> _function = (Module module) -> {
      ModuleReference _derivedFrom = module.getDerivedFrom();
      EObject _element = ((ModelElementTrace) _derivedFrom).getElement();
      final EList<EClassifier> classifiers = ((EPackage) _element).getEClassifiers();
      Iterable<EClass> _filter = Iterables.<EClass>filter(classifiers, EClass.class);
      final Consumer<EClass> _function_1 = (EClass it) -> {
        String _name = module.getName();
        String _plus = (_name + ".");
        String _name_1 = it.getName();
        String _plus_1 = (_plus + _name_1);
        Node _createNode = HypergraphCreationHelper.createNode(this.result, module, _plus_1, it);
        nodeMap.put(it, _createNode);
      };
      _filter.forEach(_function_1);
    };
    packages.forEach(_function);
    Collection<Node> _values = nodeMap.values();
    final Consumer<Node> _function_1 = (Node sourceNode) -> {
      NodeReference _derivedFrom = sourceNode.getDerivedFrom();
      EObject _element = ((ModelElementTrace) _derivedFrom).getElement();
      final EList<EReference> references = ((EClass) _element).getEReferences();
      final Consumer<EReference> _function_2 = (EReference reference) -> {
        EClass _eReferenceType = reference.getEReferenceType();
        final Node targetNode = nodeMap.get(_eReferenceType);
        boolean _notEquals = (!Objects.equal(targetNode, null));
        if (_notEquals) {
          String _name = reference.getName();
          HypergraphCreationHelper.createEdge(this.result, sourceNode, targetNode, _name, reference);
        }
      };
      references.forEach(_function_2);
    };
    _values.forEach(_function_1);
    return this.result;
  }
  
  /**
   * Find all packages recursively and create modules with fully qualified names of these packages.
   */
  private void resolvePackages(final List<Module> packages, final EPackage ePackage) {
    String _determineName = this.determineName(ePackage);
    Module _createModule = HypergraphCreationHelper.createModule(this.result, _determineName, ePackage);
    packages.add(_createModule);
    EList<EPackage> _eSubpackages = ePackage.getESubpackages();
    final Consumer<EPackage> _function = (EPackage it) -> {
      this.resolvePackages(packages, it);
    };
    _eSubpackages.forEach(_function);
  }
  
  /**
   * Create full qualified package name.
   */
  private String determineName(final EPackage ePackage) {
    String _xifexpression = null;
    EPackage _eSuperPackage = ePackage.getESuperPackage();
    boolean _notEquals = (!Objects.equal(_eSuperPackage, null));
    if (_notEquals) {
      EPackage _eSuperPackage_1 = ePackage.getESuperPackage();
      String _determineName = this.determineName(_eSuperPackage_1);
      String _plus = (_determineName + ".");
      String _name = ePackage.getName();
      _xifexpression = (_plus + _name);
    } else {
      _xifexpression = ePackage.getName();
    }
    return _xifexpression;
  }
  
  @Override
  public int workEstimate(final EPackage input) {
    return ((1 + 
      1) + 
      1);
  }
}
