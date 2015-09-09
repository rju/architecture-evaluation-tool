/**
 */
package de.cau.cs.se.software.evaluation.hypergraph.impl;

import de.cau.cs.se.software.evaluation.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Caller Callee Trace</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.impl.CallerCalleeTraceImpl#getCaller <em>Caller</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.impl.CallerCalleeTraceImpl#getCallee <em>Callee</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CallerCalleeTraceImpl extends EdgeReferenceImpl implements CallerCalleeTrace {
	/**
	 * The default value of the '{@link #getCaller() <em>Caller</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCaller()
	 * @generated
	 * @ordered
	 */
	protected static final Object CALLER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCaller() <em>Caller</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCaller()
	 * @generated
	 * @ordered
	 */
	protected Object caller = CALLER_EDEFAULT;

	/**
	 * The default value of the '{@link #getCallee() <em>Callee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallee()
	 * @generated
	 * @ordered
	 */
	protected static final Object CALLEE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCallee() <em>Callee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallee()
	 * @generated
	 * @ordered
	 */
	protected Object callee = CALLEE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CallerCalleeTraceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HypergraphPackage.Literals.CALLER_CALLEE_TRACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getCaller() {
		return caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCaller(Object newCaller) {
		Object oldCaller = caller;
		caller = newCaller;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HypergraphPackage.CALLER_CALLEE_TRACE__CALLER, oldCaller, caller));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getCallee() {
		return callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCallee(Object newCallee) {
		Object oldCallee = callee;
		callee = newCallee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HypergraphPackage.CALLER_CALLEE_TRACE__CALLEE, oldCallee, callee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case HypergraphPackage.CALLER_CALLEE_TRACE__CALLER:
				return getCaller();
			case HypergraphPackage.CALLER_CALLEE_TRACE__CALLEE:
				return getCallee();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case HypergraphPackage.CALLER_CALLEE_TRACE__CALLER:
				setCaller(newValue);
				return;
			case HypergraphPackage.CALLER_CALLEE_TRACE__CALLEE:
				setCallee(newValue);
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
			case HypergraphPackage.CALLER_CALLEE_TRACE__CALLER:
				setCaller(CALLER_EDEFAULT);
				return;
			case HypergraphPackage.CALLER_CALLEE_TRACE__CALLEE:
				setCallee(CALLEE_EDEFAULT);
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
			case HypergraphPackage.CALLER_CALLEE_TRACE__CALLER:
				return CALLER_EDEFAULT == null ? caller != null : !CALLER_EDEFAULT.equals(caller);
			case HypergraphPackage.CALLER_CALLEE_TRACE__CALLEE:
				return CALLEE_EDEFAULT == null ? callee != null : !CALLEE_EDEFAULT.equals(callee);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (caller: ");
		result.append(caller);
		result.append(", callee: ");
		result.append(callee);
		result.append(')');
		return result.toString();
	}

} //CallerCalleeTraceImpl
