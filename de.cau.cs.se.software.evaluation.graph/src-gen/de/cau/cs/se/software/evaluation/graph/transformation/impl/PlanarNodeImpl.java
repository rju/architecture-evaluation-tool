/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation.impl;

import de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode;
import de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage;

import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Planar Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarNodeImpl#getEdges <em>Edges</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarNodeImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarNodeImpl#getContext <em>Context</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PlanarNodeImpl extends NamedElementImpl implements PlanarNode {
	/**
	 * The cached value of the '{@link #getEdges() <em>Edges</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEdges()
	 * @generated
	 * @ordered
	 */
	protected EList<PlanarEdge> edges;

	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final EModuleKind KIND_EDEFAULT = EModuleKind.SYSTEM;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected EModuleKind kind = KIND_EDEFAULT;

	/**
	 * The default value of the '{@link #getContext() <em>Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContext()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContext() <em>Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContext()
	 * @generated
	 * @ordered
	 */
	protected String context = CONTEXT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PlanarNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TransformationPackage.Literals.PLANAR_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PlanarEdge> getEdges() {
		if (edges == null) {
			edges = new EObjectResolvingEList<PlanarEdge>(PlanarEdge.class, this, TransformationPackage.PLANAR_NODE__EDGES);
		}
		return edges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EModuleKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setKind(EModuleKind newKind) {
		EModuleKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransformationPackage.PLANAR_NODE__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getContext() {
		return context;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContext(String newContext) {
		String oldContext = context;
		context = newContext;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TransformationPackage.PLANAR_NODE__CONTEXT, oldContext, context));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TransformationPackage.PLANAR_NODE__EDGES:
				return getEdges();
			case TransformationPackage.PLANAR_NODE__KIND:
				return getKind();
			case TransformationPackage.PLANAR_NODE__CONTEXT:
				return getContext();
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
			case TransformationPackage.PLANAR_NODE__EDGES:
				getEdges().clear();
				getEdges().addAll((Collection<? extends PlanarEdge>)newValue);
				return;
			case TransformationPackage.PLANAR_NODE__KIND:
				setKind((EModuleKind)newValue);
				return;
			case TransformationPackage.PLANAR_NODE__CONTEXT:
				setContext((String)newValue);
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
			case TransformationPackage.PLANAR_NODE__EDGES:
				getEdges().clear();
				return;
			case TransformationPackage.PLANAR_NODE__KIND:
				setKind(KIND_EDEFAULT);
				return;
			case TransformationPackage.PLANAR_NODE__CONTEXT:
				setContext(CONTEXT_EDEFAULT);
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
			case TransformationPackage.PLANAR_NODE__EDGES:
				return edges != null && !edges.isEmpty();
			case TransformationPackage.PLANAR_NODE__KIND:
				return kind != KIND_EDEFAULT;
			case TransformationPackage.PLANAR_NODE__CONTEXT:
				return CONTEXT_EDEFAULT == null ? context != null : !CONTEXT_EDEFAULT.equals(context);
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (kind: ");
		result.append(kind);
		result.append(", context: ");
		result.append(context);
		result.append(')');
		return result.toString();
	}

} //PlanarNodeImpl
