/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation.impl;

import de.cau.cs.se.software.evaluation.graph.transformation.NamedElement;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph;
import de.cau.cs.se.software.evaluation.graph.transformation.TransformationFactory;
import de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage;

import de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TransformationPackageImpl extends EPackageImpl implements TransformationPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass namedElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass planarVisualizationGraphEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass planarNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass planarEdgeEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TransformationPackageImpl() {
		super(eNS_URI, TransformationFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link TransformationPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TransformationPackage init() {
		if (isInited) return (TransformationPackage)EPackage.Registry.INSTANCE.getEPackage(TransformationPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredTransformationPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		TransformationPackageImpl theTransformationPackage = registeredTransformationPackage instanceof TransformationPackageImpl ? (TransformationPackageImpl)registeredTransformationPackage : new TransformationPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		EcorePackage.eINSTANCE.eClass();
		HypergraphPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theTransformationPackage.createPackageContents();

		// Initialize created meta-data
		theTransformationPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTransformationPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TransformationPackage.eNS_URI, theTransformationPackage);
		return theTransformationPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getNamedElement() {
		return namedElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getNamedElement_Name() {
		return (EAttribute)namedElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlanarVisualizationGraph() {
		return planarVisualizationGraphEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlanarVisualizationGraph_Nodes() {
		return (EReference)planarVisualizationGraphEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlanarVisualizationGraph_Edges() {
		return (EReference)planarVisualizationGraphEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlanarNode() {
		return planarNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlanarNode_Edges() {
		return (EReference)planarNodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlanarNode_Kind() {
		return (EAttribute)planarNodeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlanarNode_Context() {
		return (EAttribute)planarNodeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPlanarEdge() {
		return planarEdgeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPlanarEdge_Count() {
		return (EAttribute)planarEdgeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlanarEdge_Start() {
		return (EReference)planarEdgeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPlanarEdge_End() {
		return (EReference)planarEdgeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransformationFactory getTransformationFactory() {
		return (TransformationFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		namedElementEClass = createEClass(NAMED_ELEMENT);
		createEAttribute(namedElementEClass, NAMED_ELEMENT__NAME);

		planarVisualizationGraphEClass = createEClass(PLANAR_VISUALIZATION_GRAPH);
		createEReference(planarVisualizationGraphEClass, PLANAR_VISUALIZATION_GRAPH__NODES);
		createEReference(planarVisualizationGraphEClass, PLANAR_VISUALIZATION_GRAPH__EDGES);

		planarNodeEClass = createEClass(PLANAR_NODE);
		createEReference(planarNodeEClass, PLANAR_NODE__EDGES);
		createEAttribute(planarNodeEClass, PLANAR_NODE__KIND);
		createEAttribute(planarNodeEClass, PLANAR_NODE__CONTEXT);

		planarEdgeEClass = createEClass(PLANAR_EDGE);
		createEAttribute(planarEdgeEClass, PLANAR_EDGE__COUNT);
		createEReference(planarEdgeEClass, PLANAR_EDGE__START);
		createEReference(planarEdgeEClass, PLANAR_EDGE__END);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		HypergraphPackage theHypergraphPackage = (HypergraphPackage)EPackage.Registry.INSTANCE.getEPackage(HypergraphPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		planarNodeEClass.getESuperTypes().add(this.getNamedElement());
		planarEdgeEClass.getESuperTypes().add(this.getNamedElement());

		// Initialize classes, features, and operations; add parameters
		initEClass(namedElementEClass, NamedElement.class, "NamedElement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getNamedElement_Name(), theEcorePackage.getEString(), "name", null, 0, 1, NamedElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(planarVisualizationGraphEClass, PlanarVisualizationGraph.class, "PlanarVisualizationGraph", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPlanarVisualizationGraph_Nodes(), this.getPlanarNode(), null, "nodes", null, 0, -1, PlanarVisualizationGraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPlanarVisualizationGraph_Edges(), this.getPlanarEdge(), null, "edges", null, 0, -1, PlanarVisualizationGraph.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(planarNodeEClass, PlanarNode.class, "PlanarNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPlanarNode_Edges(), this.getPlanarEdge(), null, "edges", null, 0, -1, PlanarNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlanarNode_Kind(), theHypergraphPackage.getEModuleKind(), "kind", null, 0, 1, PlanarNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPlanarNode_Context(), theEcorePackage.getEString(), "context", null, 0, 1, PlanarNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(planarEdgeEClass, PlanarEdge.class, "PlanarEdge", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPlanarEdge_Count(), theEcorePackage.getEInt(), "count", null, 0, 1, PlanarEdge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPlanarEdge_Start(), this.getPlanarNode(), null, "start", null, 0, 1, PlanarEdge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPlanarEdge_End(), this.getPlanarNode(), null, "end", null, 0, 1, PlanarEdge.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //TransformationPackageImpl
