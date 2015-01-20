/**
 */
package de.cau.cs.se.evaluation.architecture.hypergraph;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getNodes <em>Nodes</em>}</li>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getEdges <em>Edges</em>}</li>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet#getGraphs <em>Graphs</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getHypergraphSet()
 * @model
 * @generated
 */
public interface HypergraphSet extends EObject {
	/**
	 * Returns the value of the '<em><b>Nodes</b></em>' containment reference list.
	 * The list contents are of type {@link de.cau.cs.se.evaluation.architecture.hypergraph.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nodes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nodes</em>' containment reference list.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getHypergraphSet_Nodes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getNodes();

	/**
	 * Returns the value of the '<em><b>Edges</b></em>' containment reference list.
	 * The list contents are of type {@link de.cau.cs.se.evaluation.architecture.hypergraph.Edge}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Edges</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' containment reference list.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getHypergraphSet_Edges()
	 * @model containment="true"
	 * @generated
	 */
	EList<Edge> getEdges();

	/**
	 * Returns the value of the '<em><b>Graphs</b></em>' containment reference list.
	 * The list contents are of type {@link de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Graphs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Graphs</em>' containment reference list.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getHypergraphSet_Graphs()
	 * @model containment="true"
	 * @generated
	 */
	EList<Hypergraph> getGraphs();

} // HypergraphSet
