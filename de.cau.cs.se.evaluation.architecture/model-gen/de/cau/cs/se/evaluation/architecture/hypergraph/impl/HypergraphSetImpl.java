/**
 */
package de.cau.cs.se.evaluation.architecture.hypergraph.impl;

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage;
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphSet;
import de.cau.cs.se.evaluation.architecture.hypergraph.Node;

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
 * An implementation of the model object '<em><b>Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphSetImpl#getNodes <em>Nodes</em>}</li>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphSetImpl#getEdges <em>Edges</em>}</li>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.impl.HypergraphSetImpl#getGraphs <em>Graphs</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HypergraphSetImpl extends MinimalEObjectImpl.Container implements HypergraphSet {
	/**
	 * The cached value of the '{@link #getNodes() <em>Nodes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNodes()
	 * @generated
	 * @ordered
	 */
	protected EList<Node> nodes;

	/**
	 * The cached value of the '{@link #getEdges() <em>Edges</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEdges()
	 * @generated
	 * @ordered
	 */
	protected EList<Edge> edges;

	/**
	 * The cached value of the '{@link #getGraphs() <em>Graphs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGraphs()
	 * @generated
	 * @ordered
	 */
	protected EList<Hypergraph> graphs;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HypergraphSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return HypergraphPackage.Literals.HYPERGRAPH_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Node> getNodes() {
		if (nodes == null) {
			nodes = new EObjectContainmentEList<Node>(Node.class, this, HypergraphPackage.HYPERGRAPH_SET__NODES);
		}
		return nodes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Edge> getEdges() {
		if (edges == null) {
			edges = new EObjectContainmentEList<Edge>(Edge.class, this, HypergraphPackage.HYPERGRAPH_SET__EDGES);
		}
		return edges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Hypergraph> getGraphs() {
		if (graphs == null) {
			graphs = new EObjectContainmentEList<Hypergraph>(Hypergraph.class, this, HypergraphPackage.HYPERGRAPH_SET__GRAPHS);
		}
		return graphs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case HypergraphPackage.HYPERGRAPH_SET__NODES:
				return ((InternalEList<?>)getNodes()).basicRemove(otherEnd, msgs);
			case HypergraphPackage.HYPERGRAPH_SET__EDGES:
				return ((InternalEList<?>)getEdges()).basicRemove(otherEnd, msgs);
			case HypergraphPackage.HYPERGRAPH_SET__GRAPHS:
				return ((InternalEList<?>)getGraphs()).basicRemove(otherEnd, msgs);
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
			case HypergraphPackage.HYPERGRAPH_SET__NODES:
				return getNodes();
			case HypergraphPackage.HYPERGRAPH_SET__EDGES:
				return getEdges();
			case HypergraphPackage.HYPERGRAPH_SET__GRAPHS:
				return getGraphs();
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
			case HypergraphPackage.HYPERGRAPH_SET__NODES:
				getNodes().clear();
				getNodes().addAll((Collection<? extends Node>)newValue);
				return;
			case HypergraphPackage.HYPERGRAPH_SET__EDGES:
				getEdges().clear();
				getEdges().addAll((Collection<? extends Edge>)newValue);
				return;
			case HypergraphPackage.HYPERGRAPH_SET__GRAPHS:
				getGraphs().clear();
				getGraphs().addAll((Collection<? extends Hypergraph>)newValue);
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
			case HypergraphPackage.HYPERGRAPH_SET__NODES:
				getNodes().clear();
				return;
			case HypergraphPackage.HYPERGRAPH_SET__EDGES:
				getEdges().clear();
				return;
			case HypergraphPackage.HYPERGRAPH_SET__GRAPHS:
				getGraphs().clear();
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
			case HypergraphPackage.HYPERGRAPH_SET__NODES:
				return nodes != null && !nodes.isEmpty();
			case HypergraphPackage.HYPERGRAPH_SET__EDGES:
				return edges != null && !edges.isEmpty();
			case HypergraphPackage.HYPERGRAPH_SET__GRAPHS:
				return graphs != null && !graphs.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //HypergraphSetImpl
