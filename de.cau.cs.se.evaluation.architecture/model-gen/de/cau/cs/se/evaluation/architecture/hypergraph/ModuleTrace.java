/**
 */
package de.cau.cs.se.evaluation.architecture.hypergraph;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace#getModule <em>Module</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getModuleTrace()
 * @model
 * @generated
 */
public interface ModuleTrace extends ModuleReference {
	/**
	 * Returns the value of the '<em><b>Module</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Module</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module</em>' reference.
	 * @see #setModule(Module)
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getModuleTrace_Module()
	 * @model required="true" transient="true"
	 * @generated
	 */
	Module getModule();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace#getModule <em>Module</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Module</em>' reference.
	 * @see #getModule()
	 * @generated
	 */
	void setModule(Module value);

} // ModuleTrace
