/**
 */
package de.cau.cs.se.evaluation.architecture.hypergraph;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
 * @model kind="package"
 * @generated
 */
public interface HypergraphPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "hypergraph";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://evaluation.se.cs.cau.de/hypergraph";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "hypergraph";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	HypergraphPackage eINSTANCE = de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl.init();

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphImpl <em>Hypergraph</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getHypergraph()
	 * @generated
	 */
	int HYPERGRAPH = 0;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH__NODES = 0;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH__EDGES = 1;

	/**
	 * The number of structural features of the '<em>Hypergraph</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Hypergraph</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModularHypergraphImpl <em>Modular Hypergraph</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModularHypergraphImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getModularHypergraph()
	 * @generated
	 */
	int MODULAR_HYPERGRAPH = 1;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULAR_HYPERGRAPH__NODES = HYPERGRAPH__NODES;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULAR_HYPERGRAPH__EDGES = HYPERGRAPH__EDGES;

	/**
	 * The feature id for the '<em><b>Modules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULAR_HYPERGRAPH__MODULES = HYPERGRAPH_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Modular Hypergraph</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULAR_HYPERGRAPH_FEATURE_COUNT = HYPERGRAPH_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Modular Hypergraph</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULAR_HYPERGRAPH_OPERATION_COUNT = HYPERGRAPH_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleImpl <em>Module</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getModule()
	 * @generated
	 */
	int MODULE = 2;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NamedElementImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT__NAME = 0;

	/**
	 * The number of structural features of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NAMED_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__NODES = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Derived From</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__DERIVED_FROM = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Module</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Module</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__EDGES = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Derived From</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__DERIVED_FROM = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeImpl <em>Edge</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getEdge()
	 * @generated
	 */
	int EDGE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Derived From</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__DERIVED_FROM = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeReferenceImpl <em>Node Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeReferenceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNodeReference()
	 * @generated
	 */
	int NODE_REFERENCE = 9;

	/**
	 * The number of structural features of the '<em>Node Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_REFERENCE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Node Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_REFERENCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeTraceImpl <em>Node Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeTraceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNodeTrace()
	 * @generated
	 */
	int NODE_TRACE = 6;

	/**
	 * The feature id for the '<em><b>Node</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_TRACE__NODE = NODE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Node Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_TRACE_FEATURE_COUNT = NODE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Node Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_TRACE_OPERATION_COUNT = NODE_REFERENCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeReferenceImpl <em>Edge Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeReferenceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getEdgeReference()
	 * @generated
	 */
	int EDGE_REFERENCE = 10;

	/**
	 * The number of structural features of the '<em>Edge Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_REFERENCE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Edge Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_REFERENCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeTraceImpl <em>Edge Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeTraceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getEdgeTrace()
	 * @generated
	 */
	int EDGE_TRACE = 7;

	/**
	 * The feature id for the '<em><b>Edge</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_TRACE__EDGE = EDGE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Edge Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_TRACE_FEATURE_COUNT = EDGE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Edge Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_TRACE_OPERATION_COUNT = EDGE_REFERENCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.GenericTraceImpl <em>Generic Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.GenericTraceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getGenericTrace()
	 * @generated
	 */
	int GENERIC_TRACE = 8;

	/**
	 * The feature id for the '<em><b>Resource Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_TRACE__RESOURCE_ID = NODE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Generic Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_TRACE_FEATURE_COUNT = NODE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Generic Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GENERIC_TRACE_OPERATION_COUNT = NODE_REFERENCE_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleReferenceImpl <em>Module Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleReferenceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getModuleReference()
	 * @generated
	 */
	int MODULE_REFERENCE = 12;

	/**
	 * The number of structural features of the '<em>Module Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_REFERENCE_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Module Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_REFERENCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleTraceImpl <em>Module Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleTraceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getModuleTrace()
	 * @generated
	 */
	int MODULE_TRACE = 11;

	/**
	 * The feature id for the '<em><b>Module</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_TRACE__MODULE = MODULE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Module Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_TRACE_FEATURE_COUNT = MODULE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Module Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_TRACE_OPERATION_COUNT = MODULE_REFERENCE_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.TypeTraceImpl <em>Type Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.TypeTraceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getTypeTrace()
	 * @generated
	 */
	int TYPE_TRACE = 13;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_TRACE__TYPE = MODULE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Type Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_TRACE_FEATURE_COUNT = MODULE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Type Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_TRACE_OPERATION_COUNT = MODULE_REFERENCE_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.FieldTraceImpl <em>Field Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.FieldTraceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getFieldTrace()
	 * @generated
	 */
	int FIELD_TRACE = 14;

	/**
	 * The feature id for the '<em><b>Field</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TRACE__FIELD = EDGE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Field Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TRACE_FEATURE_COUNT = EDGE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Field Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIELD_TRACE_OPERATION_COUNT = EDGE_REFERENCE_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.MethodTraceImpl <em>Method Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.MethodTraceImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getMethodTrace()
	 * @generated
	 */
	int METHOD_TRACE = 15;

	/**
	 * The feature id for the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_TRACE__METHOD = NODE_REFERENCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Method Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_TRACE_FEATURE_COUNT = NODE_REFERENCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Method Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int METHOD_TRACE_OPERATION_COUNT = NODE_REFERENCE_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph <em>Hypergraph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Hypergraph</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
	 * @generated
	 */
	EClass getHypergraph();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nodes</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph#getNodes()
	 * @see #getHypergraph()
	 * @generated
	 */
	EReference getHypergraph_Nodes();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Edges</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph#getEdges()
	 * @see #getHypergraph()
	 * @generated
	 */
	EReference getHypergraph_Edges();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph <em>Modular Hypergraph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Modular Hypergraph</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
	 * @generated
	 */
	EClass getModularHypergraph();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph#getModules <em>Modules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modules</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph#getModules()
	 * @see #getModularHypergraph()
	 * @generated
	 */
	EReference getModularHypergraph_Modules();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Module <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Module</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Module
	 * @generated
	 */
	EClass getModule();

	/**
	 * Returns the meta object for the reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Module#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Nodes</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Module#getNodes()
	 * @see #getModule()
	 * @generated
	 */
	EReference getModule_Nodes();

	/**
	 * Returns the meta object for the containment reference '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Module#getDerivedFrom <em>Derived From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Derived From</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Module#getDerivedFrom()
	 * @see #getModule()
	 * @generated
	 */
	EReference getModule_DerivedFrom();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Node#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Edges</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Node#getEdges()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Edges();

	/**
	 * Returns the meta object for the containment reference '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Node#getDerivedFrom <em>Derived From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Derived From</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Node#getDerivedFrom()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_DerivedFrom();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Edge <em>Edge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Edge</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Edge
	 * @generated
	 */
	EClass getEdge();

	/**
	 * Returns the meta object for the containment reference '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Edge#getDerivedFrom <em>Derived From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Derived From</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Edge#getDerivedFrom()
	 * @see #getEdge()
	 * @generated
	 */
	EReference getEdge_DerivedFrom();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.evaluation.architecture.hypergraph.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace <em>Node Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node Trace</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace
	 * @generated
	 */
	EClass getNodeTrace();

	/**
	 * Returns the meta object for the reference '{@link de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace#getNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Node</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace#getNode()
	 * @see #getNodeTrace()
	 * @generated
	 */
	EReference getNodeTrace_Node();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace <em>Edge Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Edge Trace</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace
	 * @generated
	 */
	EClass getEdgeTrace();

	/**
	 * Returns the meta object for the reference '{@link de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace#getEdge <em>Edge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Edge</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace#getEdge()
	 * @see #getEdgeTrace()
	 * @generated
	 */
	EReference getEdgeTrace_Edge();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.GenericTrace <em>Generic Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Generic Trace</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.GenericTrace
	 * @generated
	 */
	EClass getGenericTrace();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.evaluation.architecture.hypergraph.GenericTrace#getResourceId <em>Resource Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Resource Id</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.GenericTrace#getResourceId()
	 * @see #getGenericTrace()
	 * @generated
	 */
	EAttribute getGenericTrace_ResourceId();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference <em>Node Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node Reference</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference
	 * @generated
	 */
	EClass getNodeReference();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference <em>Edge Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Edge Reference</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference
	 * @generated
	 */
	EClass getEdgeReference();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace <em>Module Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Module Trace</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace
	 * @generated
	 */
	EClass getModuleTrace();

	/**
	 * Returns the meta object for the reference '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace#getModule <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Module</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace#getModule()
	 * @see #getModuleTrace()
	 * @generated
	 */
	EReference getModuleTrace_Module();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference <em>Module Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Module Reference</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference
	 * @generated
	 */
	EClass getModuleReference();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace <em>Type Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type Trace</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
	 * @generated
	 */
	EClass getTypeTrace();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace#getType()
	 * @see #getTypeTrace()
	 * @generated
	 */
	EAttribute getTypeTrace_Type();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace <em>Field Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Field Trace</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace
	 * @generated
	 */
	EClass getFieldTrace();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace#getField <em>Field</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Field</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace#getField()
	 * @see #getFieldTrace()
	 * @generated
	 */
	EAttribute getFieldTrace_Field();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace <em>Method Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Method Trace</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
	 * @generated
	 */
	EClass getMethodTrace();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace#getMethod <em>Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace#getMethod()
	 * @see #getMethodTrace()
	 * @generated
	 */
	EAttribute getMethodTrace_Method();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	HypergraphFactory getHypergraphFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphImpl <em>Hypergraph</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getHypergraph()
		 * @generated
		 */
		EClass HYPERGRAPH = eINSTANCE.getHypergraph();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HYPERGRAPH__NODES = eINSTANCE.getHypergraph_Nodes();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HYPERGRAPH__EDGES = eINSTANCE.getHypergraph_Edges();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModularHypergraphImpl <em>Modular Hypergraph</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModularHypergraphImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getModularHypergraph()
		 * @generated
		 */
		EClass MODULAR_HYPERGRAPH = eINSTANCE.getModularHypergraph();

		/**
		 * The meta object literal for the '<em><b>Modules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULAR_HYPERGRAPH__MODULES = eINSTANCE.getModularHypergraph_Modules();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleImpl <em>Module</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getModule()
		 * @generated
		 */
		EClass MODULE = eINSTANCE.getModule();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE__NODES = eINSTANCE.getModule_Nodes();

		/**
		 * The meta object literal for the '<em><b>Derived From</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE__DERIVED_FROM = eINSTANCE.getModule_DerivedFrom();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__EDGES = eINSTANCE.getNode_Edges();

		/**
		 * The meta object literal for the '<em><b>Derived From</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__DERIVED_FROM = eINSTANCE.getNode_DerivedFrom();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeImpl <em>Edge</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getEdge()
		 * @generated
		 */
		EClass EDGE = eINSTANCE.getEdge();

		/**
		 * The meta object literal for the '<em><b>Derived From</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EDGE__DERIVED_FROM = eINSTANCE.getEdge_DerivedFrom();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NamedElementImpl <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NamedElementImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNamedElement()
		 * @generated
		 */
		EClass NAMED_ELEMENT = eINSTANCE.getNamedElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NAMED_ELEMENT__NAME = eINSTANCE.getNamedElement_Name();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeTraceImpl <em>Node Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeTraceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNodeTrace()
		 * @generated
		 */
		EClass NODE_TRACE = eINSTANCE.getNodeTrace();

		/**
		 * The meta object literal for the '<em><b>Node</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE_TRACE__NODE = eINSTANCE.getNodeTrace_Node();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeTraceImpl <em>Edge Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeTraceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getEdgeTrace()
		 * @generated
		 */
		EClass EDGE_TRACE = eINSTANCE.getEdgeTrace();

		/**
		 * The meta object literal for the '<em><b>Edge</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EDGE_TRACE__EDGE = eINSTANCE.getEdgeTrace_Edge();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.GenericTraceImpl <em>Generic Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.GenericTraceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getGenericTrace()
		 * @generated
		 */
		EClass GENERIC_TRACE = eINSTANCE.getGenericTrace();

		/**
		 * The meta object literal for the '<em><b>Resource Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GENERIC_TRACE__RESOURCE_ID = eINSTANCE.getGenericTrace_ResourceId();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeReferenceImpl <em>Node Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeReferenceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNodeReference()
		 * @generated
		 */
		EClass NODE_REFERENCE = eINSTANCE.getNodeReference();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeReferenceImpl <em>Edge Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeReferenceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getEdgeReference()
		 * @generated
		 */
		EClass EDGE_REFERENCE = eINSTANCE.getEdgeReference();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleTraceImpl <em>Module Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleTraceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getModuleTrace()
		 * @generated
		 */
		EClass MODULE_TRACE = eINSTANCE.getModuleTrace();

		/**
		 * The meta object literal for the '<em><b>Module</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE_TRACE__MODULE = eINSTANCE.getModuleTrace_Module();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleReferenceImpl <em>Module Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.ModuleReferenceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getModuleReference()
		 * @generated
		 */
		EClass MODULE_REFERENCE = eINSTANCE.getModuleReference();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.TypeTraceImpl <em>Type Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.TypeTraceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getTypeTrace()
		 * @generated
		 */
		EClass TYPE_TRACE = eINSTANCE.getTypeTrace();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TYPE_TRACE__TYPE = eINSTANCE.getTypeTrace_Type();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.FieldTraceImpl <em>Field Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.FieldTraceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getFieldTrace()
		 * @generated
		 */
		EClass FIELD_TRACE = eINSTANCE.getFieldTrace();

		/**
		 * The meta object literal for the '<em><b>Field</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIELD_TRACE__FIELD = eINSTANCE.getFieldTrace_Field();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.MethodTraceImpl <em>Method Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.MethodTraceImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getMethodTrace()
		 * @generated
		 */
		EClass METHOD_TRACE = eINSTANCE.getMethodTrace();

		/**
		 * The meta object literal for the '<em><b>Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute METHOD_TRACE__METHOD = eINSTANCE.getMethodTrace_Method();

	}

} //HypergraphPackage
