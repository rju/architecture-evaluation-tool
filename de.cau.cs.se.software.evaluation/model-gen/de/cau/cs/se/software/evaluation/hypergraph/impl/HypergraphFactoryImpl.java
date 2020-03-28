/**
 */
package de.cau.cs.se.software.evaluation.hypergraph.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import de.cau.cs.se.software.evaluation.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.EdgeReference;
import de.cau.cs.se.software.evaluation.hypergraph.EdgeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.FieldTrace;
import de.cau.cs.se.software.evaluation.hypergraph.GenericTrace;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphFactory;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage;
import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleReference;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleTrace;
import de.cau.cs.se.software.evaluation.hypergraph.NamedElement;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.hypergraph.NodeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.TypeTrace;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class HypergraphFactoryImpl extends EFactoryImpl implements HypergraphFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static HypergraphFactory init() {
		try {
			final HypergraphFactory theHypergraphFactory = (HypergraphFactory) EPackage.Registry.INSTANCE.getEFactory(HypergraphPackage.eNS_URI);
			if (theHypergraphFactory != null) {
				return theHypergraphFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new HypergraphFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public HypergraphFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case HypergraphPackage.HYPERGRAPH:
			return this.createHypergraph();
		case HypergraphPackage.MODULAR_HYPERGRAPH:
			return this.createModularHypergraph();
		case HypergraphPackage.MODULE:
			return this.createModule();
		case HypergraphPackage.NODE:
			return this.createNode();
		case HypergraphPackage.EDGE:
			return this.createEdge();
		case HypergraphPackage.NAMED_ELEMENT:
			return this.createNamedElement();
		case HypergraphPackage.NODE_TRACE:
			return this.createNodeTrace();
		case HypergraphPackage.EDGE_TRACE:
			return this.createEdgeTrace();
		case HypergraphPackage.GENERIC_TRACE:
			return this.createGenericTrace();
		case HypergraphPackage.NODE_REFERENCE:
			return this.createNodeReference();
		case HypergraphPackage.EDGE_REFERENCE:
			return this.createEdgeReference();
		case HypergraphPackage.MODULE_TRACE:
			return this.createModuleTrace();
		case HypergraphPackage.MODULE_REFERENCE:
			return this.createModuleReference();
		case HypergraphPackage.TYPE_TRACE:
			return this.createTypeTrace();
		case HypergraphPackage.FIELD_TRACE:
			return this.createFieldTrace();
		case HypergraphPackage.METHOD_TRACE:
			return this.createMethodTrace();
		case HypergraphPackage.CALLER_CALLEE_TRACE:
			return this.createCallerCalleeTrace();
		case HypergraphPackage.MODEL_ELEMENT_TRACE:
			return this.createModelElementTrace();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(final EDataType eDataType, final String initialValue) {
		switch (eDataType.getClassifierID()) {
		case HypergraphPackage.EMODULE_KIND:
			return this.createEModuleKindFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(final EDataType eDataType, final Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case HypergraphPackage.EMODULE_KIND:
			return this.convertEModuleKindToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Hypergraph createHypergraph() {
		final HypergraphImpl hypergraph = new HypergraphImpl();
		return hypergraph;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModularHypergraph createModularHypergraph() {
		final ModularHypergraphImpl modularHypergraph = new ModularHypergraphImpl();
		return modularHypergraph;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Module createModule() {
		final ModuleImpl module = new ModuleImpl();
		return module;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Node createNode() {
		final NodeImpl node = new NodeImpl();
		return node;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Edge createEdge() {
		final EdgeImpl edge = new EdgeImpl();
		return edge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NamedElement createNamedElement() {
		final NamedElementImpl namedElement = new NamedElementImpl();
		return namedElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NodeTrace createNodeTrace() {
		final NodeTraceImpl nodeTrace = new NodeTraceImpl();
		return nodeTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EdgeTrace createEdgeTrace() {
		final EdgeTraceImpl edgeTrace = new EdgeTraceImpl();
		return edgeTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GenericTrace createGenericTrace() {
		final GenericTraceImpl genericTrace = new GenericTraceImpl();
		return genericTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NodeReference createNodeReference() {
		final NodeReferenceImpl nodeReference = new NodeReferenceImpl();
		return nodeReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EdgeReference createEdgeReference() {
		final EdgeReferenceImpl edgeReference = new EdgeReferenceImpl();
		return edgeReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModuleTrace createModuleTrace() {
		final ModuleTraceImpl moduleTrace = new ModuleTraceImpl();
		return moduleTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModuleReference createModuleReference() {
		final ModuleReferenceImpl moduleReference = new ModuleReferenceImpl();
		return moduleReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public TypeTrace createTypeTrace() {
		final TypeTraceImpl typeTrace = new TypeTraceImpl();
		return typeTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FieldTrace createFieldTrace() {
		final FieldTraceImpl fieldTrace = new FieldTraceImpl();
		return fieldTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MethodTrace createMethodTrace() {
		final MethodTraceImpl methodTrace = new MethodTraceImpl();
		return methodTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CallerCalleeTrace createCallerCalleeTrace() {
		final CallerCalleeTraceImpl callerCalleeTrace = new CallerCalleeTraceImpl();
		return callerCalleeTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelElementTrace createModelElementTrace() {
		final ModelElementTraceImpl modelElementTrace = new ModelElementTraceImpl();
		return modelElementTrace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EModuleKind createEModuleKindFromString(final EDataType eDataType, final String initialValue) {
		final EModuleKind result = EModuleKind.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertEModuleKindToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public HypergraphPackage getHypergraphPackage() {
		return (HypergraphPackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static HypergraphPackage getPackage() {
		return HypergraphPackage.eINSTANCE;
	}

} // HypergraphFactoryImpl
