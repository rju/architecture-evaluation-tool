/**
 */
package de.cau.cs.se.evaluation.architecture.state;

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Row Pattern Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.state.RowPatternTable#getEdges <em>Edges</em>}</li>
 *   <li>{@link de.cau.cs.se.evaluation.architecture.state.RowPatternTable#getPatterns <em>Patterns</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getRowPatternTable()
 * @model
 * @generated
 */
public interface RowPatternTable extends EObject {
	/**
	 * Returns the value of the '<em><b>Edges</b></em>' reference list.
	 * The list contents are of type {@link de.cau.cs.se.evaluation.architecture.hypergraph.Edge}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Edges</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' reference list.
	 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getRowPatternTable_Edges()
	 * @model keys="name"
	 * @generated
	 */
	EList<Edge> getEdges();

	/**
	 * Returns the value of the '<em><b>Patterns</b></em>' containment reference list.
	 * The list contents are of type {@link de.cau.cs.se.evaluation.architecture.state.RowPattern}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Patterns</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Patterns</em>' containment reference list.
	 * @see de.cau.cs.se.evaluation.architecture.state.StatePackage#getRowPatternTable_Patterns()
	 * @model containment="true"
	 * @generated
	 */
	EList<RowPattern> getPatterns();

} // RowPatternTable
