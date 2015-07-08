/**
 */
package de.cau.cs.se.software.evaluation.state.impl;

import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;

import de.cau.cs.se.software.evaluation.state.RowPatternTable;
import de.cau.cs.se.software.evaluation.state.StatePackage;
import de.cau.cs.se.software.evaluation.state.SystemSetup;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>System Setup</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.state.impl.SystemSetupImpl#getRowPatternTable <em>Row Pattern Table</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.state.impl.SystemSetupImpl#getSystem <em>System</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.state.impl.SystemSetupImpl#getSystemGraph <em>System Graph</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SystemSetupImpl extends MinimalEObjectImpl.Container implements SystemSetup {
	/**
	 * The cached value of the '{@link #getRowPatternTable() <em>Row Pattern Table</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRowPatternTable()
	 * @generated
	 * @ordered
	 */
	protected RowPatternTable rowPatternTable;

	/**
	 * The cached value of the '{@link #getSystem() <em>System</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSystem()
	 * @generated
	 * @ordered
	 */
	protected Hypergraph system;

	/**
	 * The cached value of the '{@link #getSystemGraph() <em>System Graph</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSystemGraph()
	 * @generated
	 * @ordered
	 */
	protected Hypergraph systemGraph;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SystemSetupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return StatePackage.Literals.SYSTEM_SETUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RowPatternTable getRowPatternTable() {
		return rowPatternTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRowPatternTable(RowPatternTable newRowPatternTable, NotificationChain msgs) {
		RowPatternTable oldRowPatternTable = rowPatternTable;
		rowPatternTable = newRowPatternTable;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE, oldRowPatternTable, newRowPatternTable);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRowPatternTable(RowPatternTable newRowPatternTable) {
		if (newRowPatternTable != rowPatternTable) {
			NotificationChain msgs = null;
			if (rowPatternTable != null)
				msgs = ((InternalEObject)rowPatternTable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE, null, msgs);
			if (newRowPatternTable != null)
				msgs = ((InternalEObject)newRowPatternTable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE, null, msgs);
			msgs = basicSetRowPatternTable(newRowPatternTable, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE, newRowPatternTable, newRowPatternTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Hypergraph getSystem() {
		return system;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSystem(Hypergraph newSystem, NotificationChain msgs) {
		Hypergraph oldSystem = system;
		system = newSystem;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StatePackage.SYSTEM_SETUP__SYSTEM, oldSystem, newSystem);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSystem(Hypergraph newSystem) {
		if (newSystem != system) {
			NotificationChain msgs = null;
			if (system != null)
				msgs = ((InternalEObject)system).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StatePackage.SYSTEM_SETUP__SYSTEM, null, msgs);
			if (newSystem != null)
				msgs = ((InternalEObject)newSystem).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StatePackage.SYSTEM_SETUP__SYSTEM, null, msgs);
			msgs = basicSetSystem(newSystem, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, StatePackage.SYSTEM_SETUP__SYSTEM, newSystem, newSystem));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Hypergraph getSystemGraph() {
		return systemGraph;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSystemGraph(Hypergraph newSystemGraph, NotificationChain msgs) {
		Hypergraph oldSystemGraph = systemGraph;
		systemGraph = newSystemGraph;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH, oldSystemGraph, newSystemGraph);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSystemGraph(Hypergraph newSystemGraph) {
		if (newSystemGraph != systemGraph) {
			NotificationChain msgs = null;
			if (systemGraph != null)
				msgs = ((InternalEObject)systemGraph).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH, null, msgs);
			if (newSystemGraph != null)
				msgs = ((InternalEObject)newSystemGraph).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH, null, msgs);
			msgs = basicSetSystemGraph(newSystemGraph, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH, newSystemGraph, newSystemGraph));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE:
				return basicSetRowPatternTable(null, msgs);
			case StatePackage.SYSTEM_SETUP__SYSTEM:
				return basicSetSystem(null, msgs);
			case StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH:
				return basicSetSystemGraph(null, msgs);
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
			case StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE:
				return getRowPatternTable();
			case StatePackage.SYSTEM_SETUP__SYSTEM:
				return getSystem();
			case StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH:
				return getSystemGraph();
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
			case StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE:
				setRowPatternTable((RowPatternTable)newValue);
				return;
			case StatePackage.SYSTEM_SETUP__SYSTEM:
				setSystem((Hypergraph)newValue);
				return;
			case StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH:
				setSystemGraph((Hypergraph)newValue);
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
			case StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE:
				setRowPatternTable((RowPatternTable)null);
				return;
			case StatePackage.SYSTEM_SETUP__SYSTEM:
				setSystem((Hypergraph)null);
				return;
			case StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH:
				setSystemGraph((Hypergraph)null);
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
			case StatePackage.SYSTEM_SETUP__ROW_PATTERN_TABLE:
				return rowPatternTable != null;
			case StatePackage.SYSTEM_SETUP__SYSTEM:
				return system != null;
			case StatePackage.SYSTEM_SETUP__SYSTEM_GRAPH:
				return systemGraph != null;
		}
		return super.eIsSet(featureID);
	}

} //SystemSetupImpl
