/**
 */
package de.cau.cs.se.evaluation.architecture.hypergraph;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.hypergraph.Module#getNodes <em>Nodes</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getModule()
 * @model
 * @generated
 */
public interface Module extends EObject {
	/**
	 * Returns the value of the '<em><b>Nodes</b></em>' reference list.
	 * The list contents are of type {@link de.cau.cs.se.evaluation.architecture.hypergraph.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nodes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nodes</em>' reference list.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage#getModule_Nodes()
	 * @model
	 * @generated
	 */
	EList<Node> getNodes();

} // Module
