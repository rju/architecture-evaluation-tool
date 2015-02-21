/**
 */
package de.cau.cs.se.evaluation.architecture.state;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.state.StateModel#getSubsystems <em>Subsystems</em>}</li>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.state.StateModel#getMainsystem <em>Mainsystem</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getStateModel()
 * @model
 * @generated
 */
public interface StateModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Subsystems</b></em>' containment reference list.
	 * The list contents are of type {@link de.cau.cs.se.evaluation.architecture.state.SystemSetup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Subsystems</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Subsystems</em>' containment reference list.
	 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getStateModel_Subsystems()
	 * @model containment="true"
	 * @generated
	 */
	EList<SystemSetup> getSubsystems();

	/**
	 * Returns the value of the '<em><b>Mainsystem</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mainsystem</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mainsystem</em>' containment reference.
	 * @see #setMainsystem(SystemSetup)
	 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getStateModel_Mainsystem()
	 * @model containment="true" required="true"
	 * @generated
	 */
	SystemSetup getMainsystem();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.evaluation.architecture.state.StateModel#getMainsystem <em>Mainsystem</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mainsystem</em>' containment reference.
	 * @see #getMainsystem()
	 * @generated
	 */
	void setMainsystem(SystemSetup value);

} // StateModel
