/**
 */
package de.cau.cs.se.evaluation.architecture.hypergraph;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Caller Callee Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace#getCaller <em>Caller</em>}</li>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace#getCallee <em>Callee</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getCallerCalleeTrace()
 * @model
 * @generated
 */
public interface CallerCalleeTrace extends EdgeReference {
	/**
	 * Returns the value of the '<em><b>Caller</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Caller</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Caller</em>' attribute.
	 * @see #setCaller(Object)
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getCallerCalleeTrace_Caller()
	 * @model required="true"
	 * @generated
	 */
	Object getCaller();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace#getCaller <em>Caller</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Caller</em>' attribute.
	 * @see #getCaller()
	 * @generated
	 */
	void setCaller(Object value);

	/**
	 * Returns the value of the '<em><b>Callee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Callee</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Callee</em>' attribute.
	 * @see #setCallee(Object)
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getCallerCalleeTrace_Callee()
	 * @model required="true"
	 * @generated
	 */
	Object getCallee();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace#getCallee <em>Callee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Callee</em>' attribute.
	 * @see #getCallee()
	 * @generated
	 */
	void setCallee(Object value);

} // CallerCalleeTrace
