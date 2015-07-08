/**
 */
package de.cau.cs.se.software.evaluation.state.impl;

import de.cau.cs.se.software.evaluation.state.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StateFactoryImpl extends EFactoryImpl implements StateFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static StateFactory init() {
		try {
			StateFactory theStateFactory = (StateFactory)EPackage.Registry.INSTANCE.getEFactory(StatePackage.eNS_URI);
			if (theStateFactory != null) {
				return theStateFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new StateFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StateFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case StatePackage.STATE_MODEL: return createStateModel();
			case StatePackage.SYSTEM_SETUP: return createSystemSetup();
			case StatePackage.ROW_PATTERN_TABLE: return createRowPatternTable();
			case StatePackage.ROW_PATTERN: return createRowPattern();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StateModel createStateModel() {
		StateModelImpl stateModel = new StateModelImpl();
		return stateModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemSetup createSystemSetup() {
		SystemSetupImpl systemSetup = new SystemSetupImpl();
		return systemSetup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RowPatternTable createRowPatternTable() {
		RowPatternTableImpl rowPatternTable = new RowPatternTableImpl();
		return rowPatternTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RowPattern createRowPattern() {
		RowPatternImpl rowPattern = new RowPatternImpl();
		return rowPattern;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatePackage getStatePackage() {
		return (StatePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static StatePackage getPackage() {
		return StatePackage.eINSTANCE;
	}

} //StateFactoryImpl
