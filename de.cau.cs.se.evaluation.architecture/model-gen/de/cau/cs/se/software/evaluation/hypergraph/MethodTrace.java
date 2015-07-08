/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Method Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.MethodTrace#getMethod <em>Method</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getMethodTrace()
 * @model
 * @generated
 */
public interface MethodTrace extends NodeReference {
	/**
	 * Returns the value of the '<em><b>Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Method</em>' attribute.
	 * @see #setMethod(Object)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getMethodTrace_Method()
	 * @model required="true" transient="true"
	 * @generated
	 */
	Object getMethod();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.MethodTrace#getMethod <em>Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Method</em>' attribute.
	 * @see #getMethod()
	 * @generated
	 */
	void setMethod(Object value);

} // MethodTrace
