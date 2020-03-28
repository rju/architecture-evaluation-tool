/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation;

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
 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationFactory
 * @model kind="package"
 * @generated
 */
public interface TransformationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "transformation";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "de.cau.cs.se.software.evaluation.graph.transformation";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "transformation";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TransformationPackage eINSTANCE = de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl.init();

	/**
	 * The meta object id for the '{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.NamedElementImpl <em>Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.NamedElementImpl
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl#getNamedElement()
	 * @generated
	 */
	int NAMED_ELEMENT = 0;

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
	 * The meta object id for the '{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarVisualizationGraphImpl <em>Planar Visualization Graph</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarVisualizationGraphImpl
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl#getPlanarVisualizationGraph()
	 * @generated
	 */
	int PLANAR_VISUALIZATION_GRAPH = 1;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_VISUALIZATION_GRAPH__NODES = 0;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_VISUALIZATION_GRAPH__EDGES = 1;

	/**
	 * The number of structural features of the '<em>Planar Visualization Graph</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_VISUALIZATION_GRAPH_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Planar Visualization Graph</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_VISUALIZATION_GRAPH_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarNodeImpl <em>Planar Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarNodeImpl
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl#getPlanarNode()
	 * @generated
	 */
	int PLANAR_NODE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_NODE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_NODE__EDGES = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_NODE__KIND = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_NODE__CONTEXT = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Planar Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_NODE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Planar Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_NODE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarEdgeImpl <em>Planar Edge</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarEdgeImpl
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl#getPlanarEdge()
	 * @generated
	 */
	int PLANAR_EDGE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_EDGE__NAME = NAMED_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_EDGE__COUNT = NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Start</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_EDGE__START = NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>End</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_EDGE__END = NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Planar Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_EDGE_FEATURE_COUNT = NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Planar Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PLANAR_EDGE_OPERATION_COUNT = NAMED_ELEMENT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.software.evaluation.graph.transformation.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Named Element</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.NamedElement
	 * @generated
	 */
	EClass getNamedElement();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.software.evaluation.graph.transformation.NamedElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.NamedElement#getName()
	 * @see #getNamedElement()
	 * @generated
	 */
	EAttribute getNamedElement_Name();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph <em>Planar Visualization Graph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Planar Visualization Graph</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph
	 * @generated
	 */
	EClass getPlanarVisualizationGraph();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Nodes</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph#getNodes()
	 * @see #getPlanarVisualizationGraph()
	 * @generated
	 */
	EReference getPlanarVisualizationGraph_Nodes();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Edges</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph#getEdges()
	 * @see #getPlanarVisualizationGraph()
	 * @generated
	 */
	EReference getPlanarVisualizationGraph_Edges();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode <em>Planar Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Planar Node</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode
	 * @generated
	 */
	EClass getPlanarNode();

	/**
	 * Returns the meta object for the reference list '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Edges</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getEdges()
	 * @see #getPlanarNode()
	 * @generated
	 */
	EReference getPlanarNode_Edges();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getKind()
	 * @see #getPlanarNode()
	 * @generated
	 */
	EAttribute getPlanarNode_Kind();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Context</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getContext()
	 * @see #getPlanarNode()
	 * @generated
	 */
	EAttribute getPlanarNode_Context();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge <em>Planar Edge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Planar Edge</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge
	 * @generated
	 */
	EClass getPlanarEdge();

	/**
	 * Returns the meta object for the attribute '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getCount <em>Count</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Count</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getCount()
	 * @see #getPlanarEdge()
	 * @generated
	 */
	EAttribute getPlanarEdge_Count();

	/**
	 * Returns the meta object for the reference '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Start</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getStart()
	 * @see #getPlanarEdge()
	 * @generated
	 */
	EReference getPlanarEdge_Start();

	/**
	 * Returns the meta object for the reference '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getEnd <em>End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>End</em>'.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getEnd()
	 * @see #getPlanarEdge()
	 * @generated
	 */
	EReference getPlanarEdge_End();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TransformationFactory getTransformationFactory();

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
		 * The meta object literal for the '{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.NamedElementImpl <em>Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.NamedElementImpl
		 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl#getNamedElement()
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
		 * The meta object literal for the '{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarVisualizationGraphImpl <em>Planar Visualization Graph</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarVisualizationGraphImpl
		 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl#getPlanarVisualizationGraph()
		 * @generated
		 */
		EClass PLANAR_VISUALIZATION_GRAPH = eINSTANCE.getPlanarVisualizationGraph();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLANAR_VISUALIZATION_GRAPH__NODES = eINSTANCE.getPlanarVisualizationGraph_Nodes();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLANAR_VISUALIZATION_GRAPH__EDGES = eINSTANCE.getPlanarVisualizationGraph_Edges();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarNodeImpl <em>Planar Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarNodeImpl
		 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl#getPlanarNode()
		 * @generated
		 */
		EClass PLANAR_NODE = eINSTANCE.getPlanarNode();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLANAR_NODE__EDGES = eINSTANCE.getPlanarNode_Edges();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLANAR_NODE__KIND = eINSTANCE.getPlanarNode_Kind();

		/**
		 * The meta object literal for the '<em><b>Context</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLANAR_NODE__CONTEXT = eINSTANCE.getPlanarNode_Context();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarEdgeImpl <em>Planar Edge</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarEdgeImpl
		 * @see de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationPackageImpl#getPlanarEdge()
		 * @generated
		 */
		EClass PLANAR_EDGE = eINSTANCE.getPlanarEdge();

		/**
		 * The meta object literal for the '<em><b>Count</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PLANAR_EDGE__COUNT = eINSTANCE.getPlanarEdge_Count();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLANAR_EDGE__START = eINSTANCE.getPlanarEdge_Start();

		/**
		 * The meta object literal for the '<em><b>End</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PLANAR_EDGE__END = eINSTANCE.getPlanarEdge_End();

	}

} //TransformationPackage
