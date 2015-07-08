/**
 */
package de.cau.cs.se.software.evaluation.state.impl;

import de.cau.cs.se.software.evaluation.state.StateModel;
import de.cau.cs.se.software.evaluation.state.StatePackage;
import de.cau.cs.se.software.evaluation.state.SystemSetup;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.state.impl.StateModelImpl#getSubsystems <em>Subsystems</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.state.impl.StateModelImpl#getMainsystem <em>Mainsystem</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StateModelImpl extends MinimalEObjectImpl.Container implements StateModel {
	/**
	 * The cached value of the '{@link #getSubsystems() <em>Subsystems</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubsystems()
	 * @generated
	 * @ordered
	 */
	protected EList<SystemSetup> subsystems;

	/**
	 * The cached value of the '{@link #getMainsystem() <em>Mainsystem</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMainsystem()
	 * @generated
	 * @ordered
	 */
	protected SystemSetup mainsystem;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StateModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return StatePackage.Literals.STATE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SystemSetup> getSubsystems() {
		if (subsystems == null) {
			subsystems = new EObjectContainmentEList<SystemSetup>(SystemSetup.class, this, StatePackage.STATE_MODEL__SUBSYSTEMS);
		}
		return subsystems;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SystemSetup getMainsystem() {
		return mainsystem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMainsystem(SystemSetup newMainsystem, NotificationChain msgs) {
		SystemSetup oldMainsystem = mainsystem;
		mainsystem = newMainsystem;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StatePackage.STATE_MODEL__MAINSYSTEM, oldMainsystem, newMainsystem);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMainsystem(SystemSetup newMainsystem) {
		if (newMainsystem != mainsystem) {
			NotificationChain msgs = null;
			if (mainsystem != null)
				msgs = ((InternalEObject)mainsystem).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StatePackage.STATE_MODEL__MAINSYSTEM, null, msgs);
			if (newMainsystem != null)
				msgs = ((InternalEObject)newMainsystem).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StatePackage.STATE_MODEL__MAINSYSTEM, null, msgs);
			msgs = basicSetMainsystem(newMainsystem, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, StatePackage.STATE_MODEL__MAINSYSTEM, newMainsystem, newMainsystem));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case StatePackage.STATE_MODEL__SUBSYSTEMS:
				return ((InternalEList<?>)getSubsystems()).basicRemove(otherEnd, msgs);
			case StatePackage.STATE_MODEL__MAINSYSTEM:
				return basicSetMainsystem(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case StatePackage.STATE_MODEL__SUBSYSTEMS:
				return getSubsystems();
			case StatePackage.STATE_MODEL__MAINSYSTEM:
				return getMainsystem();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case StatePackage.STATE_MODEL__SUBSYSTEMS:
				getSubsystems().clear();
				getSubsystems().addAll((Collection<? extends SystemSetup>)newValue);
				return;
			case StatePackage.STATE_MODEL__MAINSYSTEM:
				setMainsystem((SystemSetup)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case StatePackage.STATE_MODEL__SUBSYSTEMS:
				getSubsystems().clear();
				return;
			case StatePackage.STATE_MODEL__MAINSYSTEM:
				setMainsystem((SystemSetup)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case StatePackage.STATE_MODEL__SUBSYSTEMS:
				return subsystems != null && !subsystems.isEmpty();
			case StatePackage.STATE_MODEL__MAINSYSTEM:
				return mainsystem != null;
		}
		return super.eIsSet(featureID);
	}

} //StateModelImpl
