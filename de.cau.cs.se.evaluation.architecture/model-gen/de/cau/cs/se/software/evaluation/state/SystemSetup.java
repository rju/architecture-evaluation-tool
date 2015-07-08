/**
 */
package de.cau.cs.se.software.evaluation.state;

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>System Setup</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.state.SystemSetup#getRowPatternTable <em>Row Pattern Table</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.state.SystemSetup#getSystem <em>System</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.state.SystemSetup#getSystemGraph <em>System Graph</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.software.evaluation.state.StatePackage#getSystemSetup()
 * @model
 * @generated
 */
public interface SystemSetup extends EObject {
	/**
	 * Returns the value of the '<em><b>Row Pattern Table</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Row Pattern Table</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Row Pattern Table</em>' containment reference.
	 * @see #setRowPatternTable(RowPatternTable)
	 * @see de.cau.cs.se.software.evaluation.state.StatePackage#getSystemSetup_RowPatternTable()
	 * @model containment="true" required="true"
	 * @generated
	 */
	RowPatternTable getRowPatternTable();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.state.SystemSetup#getRowPatternTable <em>Row Pattern Table</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Row Pattern Table</em>' containment reference.
	 * @see #getRowPatternTable()
	 * @generated
	 */
	void setRowPatternTable(RowPatternTable value);

	/**
	 * Returns the value of the '<em><b>System</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>System</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>System</em>' containment reference.
	 * @see #setSystem(Hypergraph)
	 * @see de.cau.cs.se.software.evaluation.state.StatePackage#getSystemSetup_System()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Hypergraph getSystem();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.state.SystemSetup#getSystem <em>System</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>System</em>' containment reference.
	 * @see #getSystem()
	 * @generated
	 */
	void setSystem(Hypergraph value);

	/**
	 * Returns the value of the '<em><b>System Graph</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>System Graph</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>System Graph</em>' containment reference.
	 * @see #setSystemGraph(Hypergraph)
	 * @see de.cau.cs.se.software.evaluation.state.StatePackage#getSystemSetup_SystemGraph()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Hypergraph getSystemGraph();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.state.SystemSetup#getSystemGraph <em>System Graph</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>System Graph</em>' containment reference.
	 * @see #getSystemGraph()
	 * @generated
	 */
	void setSystemGraph(Hypergraph value);

} // SystemSetup
