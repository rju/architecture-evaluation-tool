/**
 */
package de.cau.cs.se.evaluation.architecture.state;

import de.cau.cs.se.evaluation.architecture.hypergraph.Node;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Row Pattern</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.state.RowPattern#getNodes <em>Nodes</em>}</li>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.state.RowPattern#getPattern <em>Pattern</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getRowPattern()
 * @model
 * @generated
 */
public interface RowPattern extends EObject {
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
	 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getRowPattern_Nodes()
	 * @model required="true"
	 * @generated
	 */
	EList<Node> getNodes();

	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Boolean}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pattern</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pattern</em>' attribute list.
	 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getRowPattern_Pattern()
	 * @model unique="false"
	 * @generated
	 */
	EList<Boolean> getPattern();

} // RowPattern
