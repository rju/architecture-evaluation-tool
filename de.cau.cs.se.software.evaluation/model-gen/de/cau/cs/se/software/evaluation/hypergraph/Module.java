/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.Module#getNodes <em>Nodes</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.Module#getDerivedFrom <em>Derived From</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.Module#getKind <em>Kind</em>}</li>
 * </ul>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getModule()
 * @model
 * @generated
 */
public interface Module extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Nodes</b></em>' reference list.
	 * The list contents are of type {@link de.cau.cs.se.software.evaluation.hypergraph.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nodes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nodes</em>' reference list.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getModule_Nodes()
	 * @model
	 * @generated
	 */
	EList<Node> getNodes();

	/**
	 * Returns the value of the '<em><b>Derived From</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Derived From</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Derived From</em>' containment reference.
	 * @see #setDerivedFrom(ModuleReference)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getModule_DerivedFrom()
	 * @model containment="true"
	 * @generated
	 */
	ModuleReference getDerivedFrom();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.Module#getDerivedFrom <em>Derived From</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Derived From</em>' containment reference.
	 * @see #getDerivedFrom()
	 * @generated
	 */
	void setDerivedFrom(ModuleReference value);

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link de.cau.cs.se.software.evaluation.hypergraph.EModuleKind}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kind</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.EModuleKind
	 * @see #setKind(EModuleKind)
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getModule_Kind()
	 * @model required="true"
	 * @generated
	 */
	EModuleKind getKind();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.hypergraph.Module#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.EModuleKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(EModuleKind value);

} // Module
