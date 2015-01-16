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
	 * The feature id for the '<em><b>Nodes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH__NODES = 0;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' reference list.
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
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NamedElementImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 3;

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
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.NodeImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 1;

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
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 1;

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
	int EDGE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;


	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphSetImpl <em>Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphSetImpl
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getHypergraphSet()
	 * @generated
	 */
	int HYPERGRAPH_SET = 4;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH_SET__NODES = 0;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH_SET__EDGES = 1;

	/**
	 * The feature id for the '<em><b>Graphs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH_SET__GRAPHS = 2;

	/**
	 * The number of structural features of the '<em>Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH_SET_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HYPERGRAPH_SET_OPERATION_COUNT = 0;


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
	 * Returns the meta object for the reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Nodes</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph#getNodes()
	 * @see #getHypergraph()
	 * @generated
	 */
	EReference getHypergraph_Nodes();

	/**
	 * Returns the meta object for the reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Edges</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph#getEdges()
	 * @see #getHypergraph()
	 * @generated
	 */
	EReference getHypergraph_Edges();

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
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Edge <em>Edge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Edge</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Edge
	 * @generated
	 */
	EClass getEdge();

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
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet <em>Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Set</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet
	 * @generated
	 */
	EClass getHypergraphSet();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nodes</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getNodes()
	 * @see #getHypergraphSet()
	 * @generated
	 */
	EReference getHypergraphSet_Nodes();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Edges</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getEdges()
	 * @see #getHypergraphSet()
	 * @generated
	 */
	EReference getHypergraphSet_Edges();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getGraphs <em>Graphs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Graphs</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getGraphs()
	 * @see #getHypergraphSet()
	 * @generated
	 */
	EReference getHypergraphSet_Graphs();

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
		 * The meta object literal for the '<em><b>Nodes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HYPERGRAPH__NODES = eINSTANCE.getHypergraph_Nodes();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HYPERGRAPH__EDGES = eINSTANCE.getHypergraph_Edges();

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
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeImpl <em>Edge</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.EdgeImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getEdge()
		 * @generated
		 */
		EClass EDGE = eINSTANCE.getEdge();

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
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphSetImpl <em>Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphSetImpl
		 * @see de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphPackageImpl#getHypergraphSet()
		 * @generated
		 */
		EClass HYPERGRAPH_SET = eINSTANCE.getHypergraphSet();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HYPERGRAPH_SET__NODES = eINSTANCE.getHypergraphSet_Nodes();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HYPERGRAPH_SET__EDGES = eINSTANCE.getHypergraphSet_Edges();

		/**
		 * The meta object literal for the '<em><b>Graphs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference HYPERGRAPH_SET__GRAPHS = eINSTANCE.getHypergraphSet_Graphs();

	}

} //HypergraphPackage
