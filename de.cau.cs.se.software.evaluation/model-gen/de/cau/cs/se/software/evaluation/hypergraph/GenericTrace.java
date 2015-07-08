/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Generic Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.GenericTrace#getResourceId <em>Resource Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getGenericTrace()
 * @model
 * @generated
 */
public interface GenericTrace extends NodeReference, EdgeReference, ModuleReference {
	/**
	 * Returns the value of the '<em><b>Resource Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Resource Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Resource Id</em>' attribute.
	 * @see #setResourceId(String)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getGenericTrace_ResourceId()
	 * @model required="true"
	 * @generated
	 */
	String getResourceId();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.GenericTrace#getResourceId <em>Resource Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Id</em>' attribute.
	 * @see #getResourceId()
	 * @generated
	 */
	void setResourceId(String value);

} // GenericTrace
