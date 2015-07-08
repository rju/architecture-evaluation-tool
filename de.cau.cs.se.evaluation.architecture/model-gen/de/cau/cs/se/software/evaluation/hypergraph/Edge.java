/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Edge</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.Edge#getDerivedFrom <em>Derived From</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getEdge()
 * @model
 * @generated
 */
public interface Edge extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Derived From</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Derived From</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Derived From</em>' containment reference.
	 * @see #setDerivedFrom(EdgeReference)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getEdge_DerivedFrom()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EdgeReference getDerivedFrom();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.Edge#getDerivedFrom <em>Derived From</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Derived From</em>' containment reference.
	 * @see #getDerivedFrom()
	 * @generated
	 */
	void setDerivedFrom(EdgeReference value);

} // Edge
