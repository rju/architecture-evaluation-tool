/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Planar Visualization Graph</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph#getNodes <em>Nodes</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph#getEdges <em>Edges</em>}</li>
 * </ul>
 *
 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarVisualizationGraph()
 * @model
 * @generated
 */
public interface PlanarVisualizationGraph extends EObject {
	/**
	 * Returns the value of the '<em><b>Nodes</b></em>' containment reference list.
	 * The list contents are of type {@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nodes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nodes</em>' containment reference list.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarVisualizationGraph_Nodes()
	 * @model containment="true"
	 * @generated
	 */
	EList<PlanarNode> getNodes();

	/**
	 * Returns the value of the '<em><b>Edges</b></em>' containment reference list.
	 * The list contents are of type {@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Edges</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' containment reference list.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarVisualizationGraph_Edges()
	 * @model containment="true"
	 * @generated
	 */
	EList<PlanarEdge> getEdges();

} // PlanarVisualizationGraph
