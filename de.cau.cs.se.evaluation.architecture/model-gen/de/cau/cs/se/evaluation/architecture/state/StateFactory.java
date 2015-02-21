/**
 */
package de.cau.cs.se.evaluation.architecture.state;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage
 * @generated
 */
public interface StateFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	StateFactory eINSTANCE = de.cau.cs.se.evaluation.architecture.state.impl.StateFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	StateModel createStateModel();

	/**
	 * Returns a new object of class '<em>System Setup</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>System Setup</em>'.
	 * @generated
	 */
	SystemSetup createSystemSetup();

	/**
	 * Returns a new object of class '<em>Row Pattern Table</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Row Pattern Table</em>'.
	 * @generated
	 */
	RowPatternTable createRowPatternTable();

	/**
	 * Returns a new object of class '<em>Row Pattern</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Row Pattern</em>'.
	 * @generated
	 */
	RowPattern createRowPattern();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	StatePackage getStatePackage();

} //StateFactory
