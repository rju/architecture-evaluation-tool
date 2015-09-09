/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.NodeTrace#getNode <em>Node</em>}</li>
 * </ul>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getNodeTrace()
 * @model
 * @generated
 */
public interface NodeTrace extends NodeReference {
	/**
	 * Returns the value of the '<em><b>Node</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Node</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Node</em>' reference.
	 * @see #setNode(Node)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getNodeTrace_Node()
	 * @model required="true" transient="true"
	 * @generated
	 */
	Node getNode();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.NodeTrace#getNode <em>Node</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Node</em>' reference.
	 * @see #getNode()
	 * @generated
	 */
	void setNode(Node value);

} // NodeTrace
