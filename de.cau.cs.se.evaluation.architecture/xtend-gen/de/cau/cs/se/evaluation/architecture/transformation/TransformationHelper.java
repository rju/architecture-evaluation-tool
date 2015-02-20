package de.cau.cs.se.evaluation.architecture.transformation;

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.Module;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference;
import de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;
import java.util.List;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class TransformationHelper {
  public static Node deriveNode(final Node node) {
    final Node resultNode = HypergraphFactory.eINSTANCE.createNode();
    String _name = node.getName();
    resultNode.setName(_name);
    final NodeTrace derivedFrom = HypergraphFactory.eINSTANCE.createNodeTrace();
    derivedFrom.setNode(node);
    resultNode.setDerivedFrom(derivedFrom);
    return resultNode;
  }
  
  public static Edge deriveEdge(final Edge edge) {
    final Edge resultEdge = HypergraphFactory.eINSTANCE.createEdge();
    String _name = edge.getName();
    resultEdge.setName(_name);
    final EdgeTrace derivedFrom = HypergraphFactory.eINSTANCE.createEdgeTrace();
    derivedFrom.setEdge(edge);
    resultEdge.setDerivedFrom(derivedFrom);
    return resultEdge;
  }
  
  public static Module deriveModule(final Module module) {
    final Module resultModule = HypergraphFactory.eINSTANCE.createModule();
    String _name = module.getName();
    resultModule.setName(_name);
    final ModuleTrace derivedFrom = HypergraphFactory.eINSTANCE.createModuleTrace();
    derivedFrom.setModule(module);
    resultModule.setDerivedFrom(derivedFrom);
    return resultModule;
  }
  
  public static Node findConstructorMethod(final ModularHypergraph graph, final Type type, final List arguments) {
    Name _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (type instanceof SimpleType) {
        _matched=true;
        _switchResult = ((SimpleType)type).getName();
      }
    }
    if (!_matched) {
      if (type instanceof NameQualifiedType) {
        _matched=true;
        _switchResult = ((NameQualifiedType)type).getName();
      }
    }
    if (!_matched) {
      return null;
    }
    final Name typeName = _switchResult;
    EList<Module> _modules = graph.getModules();
    final Function1<Module, Boolean> _function = new Function1<Module, Boolean>() {
      public Boolean apply(final Module module) {
        ModuleReference _derivedFrom = module.getDerivedFrom();
        Object _type = ((TypeTrace) _derivedFrom).getType();
        String _elementName = ((IType) _type).getElementName();
        String _fullyQualifiedName = typeName.getFullyQualifiedName();
        return Boolean.valueOf(_elementName.equals(_fullyQualifiedName));
      }
    };
    final Module module = IterableExtensions.<Module>findFirst(_modules, _function);
    ModuleReference _derivedFrom = module.getDerivedFrom();
    Object _type = ((TypeTrace) _derivedFrom).getType();
    final String moduleName = ((IType) _type).getElementName();
    EList<Node> _nodes = module.getNodes();
    final Function1<Node, Boolean> _function_1 = new Function1<Node, Boolean>() {
      public Boolean apply(final Node node) {
        NodeReference _derivedFrom = node.getDerivedFrom();
        Object _method = ((MethodTrace) _derivedFrom).getMethod();
        String _elementName = ((IMethod) _method).getElementName();
        return Boolean.valueOf(_elementName.equals(moduleName));
      }
    };
    final Iterable<Node> nodes = IterableExtensions.<Node>filter(_nodes, _function_1);
    return TransformationHelper.matchArguments(nodes, arguments);
  }
  
  public static Node matchArguments(final Iterable<Node> nodes, final List arguments) {
    final Function1<Node, Boolean> _function = new Function1<Node, Boolean>() {
      public Boolean apply(final Node node) {
        boolean _xblockexpression = false;
        {
          NodeReference _derivedFrom = node.getDerivedFrom();
          Object _method = ((MethodTrace) _derivedFrom).getMethod();
          final MethodDeclaration method = ((MethodDeclaration) _method);
          final List parameters = method.parameters();
          boolean _xifexpression = false;
          int _size = parameters.size();
          int _size_1 = arguments.size();
          boolean _equals = (_size == _size_1);
          if (_equals) {
            _xifexpression = TransformationHelper.compareArgAndParam(parameters, arguments);
          } else {
            _xifexpression = false;
          }
          _xblockexpression = _xifexpression;
        }
        return Boolean.valueOf(_xblockexpression);
      }
    };
    return IterableExtensions.<Node>findFirst(nodes, _function);
  }
  
  /**
   * returns true if the arguments match the parameters.
   */
  public static boolean compareArgAndParam(final List parameters, final List arguments) {
    for (int i = 0; (i < parameters.size()); i++) {
      {
        Object _get = parameters.get(i);
        Type _type = ((SingleVariableDeclaration) _get).getType();
        ITypeBinding _resolveBinding = _type.resolveBinding();
        final ITypeBinding pType = _resolveBinding.getErasure();
        Object _get_1 = arguments.get(i);
        ITypeBinding _resolveTypeBinding = ((Expression) _get_1).resolveTypeBinding();
        final ITypeBinding aType = _resolveTypeBinding.getErasure();
        boolean _isAssignmentCompatible = pType.isAssignmentCompatible(aType);
        boolean _not = (!_isAssignmentCompatible);
        if (_not) {
          return false;
        }
      }
    }
    return true;
  }
}
