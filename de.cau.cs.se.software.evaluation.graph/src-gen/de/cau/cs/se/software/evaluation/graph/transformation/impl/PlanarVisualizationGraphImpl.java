/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation.impl;

import de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode;
import de.cau.cs.se.software.evaluation.graph.transformation.PlanarVisualizationGraph;
import de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Planar Visualization Graph</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarVisualizationGraphImpl#getNodes <em>Nodes</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.impl.PlanarVisualizationGraphImpl#getEdges <em>Edges</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PlanarVisualizationGraphImpl extends MinimalEObjectImpl.Container implements PlanarVisualizationGraph {
	/**
	 * The cached value of the '{@link #getNodes() <em>Nodes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNodes()
	 * @generated
	 * @ordered
	 */
	protected EList<PlanarNode> nodes;

	/**
	 * The cached value of the '{@link #getEdges() <em>Edges</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEdges()
	 * @generated
	 * @ordered
	 */
	protected EList<PlanarEdge> edges;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PlanarVisualizationGraphImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TransformationPackage.Literals.PLANAR_VISUALIZATION_GRAPH;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PlanarNode> getNodes() {
		if (nodes == null) {
			nodes = new EObjectContainmentEList<PlanarNode>(PlanarNode.class, this, TransformationPackage.PLANAR_VISUALIZATION_GRAPH__NODES);
		}
		return nodes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PlanarEdge> getEdges() {
		if (edges == null) {
			edges = new EObjectContainmentEList<PlanarEdge>(PlanarEdge.class, this, TransformationPackage.PLANAR_VISUALIZATION_GRAPH__EDGES);
		}
		return edges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__NODES:
				return ((InternalEList<?>)getNodes()).basicRemove(otherEnd, msgs);
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__EDGES:
				return ((InternalEList<?>)getEdges()).basicRemove(otherEnd, msgs);
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
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__NODES:
				return getNodes();
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__EDGES:
				return getEdges();
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
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__NODES:
				getNodes().clear();
				getNodes().addAll((Collection<? extends PlanarNode>)newValue);
				return;
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__EDGES:
				getEdges().clear();
				getEdges().addAll((Collection<? extends PlanarEdge>)newValue);
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
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__NODES:
				getNodes().clear();
				return;
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__EDGES:
				getEdges().clear();
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
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__NODES:
				return nodes != null && !nodes.isEmpty();
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH__EDGES:
				return edges != null && !edges.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //PlanarVisualizationGraphImpl
