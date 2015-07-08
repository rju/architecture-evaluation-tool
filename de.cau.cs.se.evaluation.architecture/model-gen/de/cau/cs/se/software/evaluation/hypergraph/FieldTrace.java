/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Field Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.FieldTrace#getField <em>Field</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getFieldTrace()
 * @model
 * @generated
 */
public interface FieldTrace extends EdgeReference {
	/**
	 * Returns the value of the '<em><b>Field</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Field</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Field</em>' attribute.
	 * @see #setField(Object)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getFieldTrace_Field()
	 * @model required="true" transient="true"
	 * @generated
	 */
	Object getField();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.FieldTrace#getField <em>Field</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Field</em>' attribute.
	 * @see #getField()
	 * @generated
	 */
	void setField(Object value);

} // FieldTrace
