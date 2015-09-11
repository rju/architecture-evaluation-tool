/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Element Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace#getElement <em>Element</em>}</li>
 * </ul>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getModelElementTrace()
 * @model
 * @generated
 */
public interface ModelElementTrace extends EdgeReference, ModuleReference, NodeReference {
	/**
	 * Returns the value of the '<em><b>Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Element</em>' reference.
	 * @see #setElement(EObject)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getModelElementTrace_Element()
	 * @model transient="true"
	 * @generated
	 */
	EObject getElement();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace#getElement <em>Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Element</em>' reference.
	 * @see #getElement()
	 * @generated
	 */
	void setElement(EObject value);

} // ModelElementTrace
