/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.Node#getEdges <em>Edges</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.Node#getDerivedFrom <em>Derived From</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getNode()
 * @model
 * @generated
 */
public interface Node extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Edges</b></em>' reference list.
	 * The list contents are of type {@link de.cau.cs.se.software.evaluation.hypergraph.Edge}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Edges</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' reference list.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getNode_Edges()
	 * @model
	 * @generated
	 */
	EList<Edge> getEdges();

	/**
	 * Returns the value of the '<em><b>Derived From</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Derived From</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Derived From</em>' containment reference.
	 * @see #setDerivedFrom(NodeReference)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getNode_DerivedFrom()
	 * @model containment="true" required="true"
	 * @generated
	 */
	NodeReference getDerivedFrom();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.Node#getDerivedFrom <em>Derived From</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Derived From</em>' containment reference.
	 * @see #getDerivedFrom()
	 * @generated
	 */
	void setDerivedFrom(NodeReference value);

} // Node
