/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Modular Hypergraph</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph#getModules <em>Modules</em>}</li>
 * </ul>
 *
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getModularHypergraph()
 * @model
 * @generated
 */
public interface ModularHypergraph extends Hypergraph {
	/**
	 * Returns the value of the '<em><b>Modules</b></em>' containment reference list.
	 * The list contents are of type {@link de.cau.cs.se.software.evaluation.hypergraph.Module}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modules</em>' containment reference list.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage#getModularHypergraph_Modules()
	 * @model containment="true"
	 * @generated
	 */
	EList<Module> getModules();

} // ModularHypergraph
