/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation;

import de.cau.cs.se.software.evaluation.hypergraph.EModuleKind;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Planar Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getEdges <em>Edges</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getKind <em>Kind</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getContext <em>Context</em>}</li>
 * </ul>
 *
 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarNode()
 * @model
 * @generated
 */
public interface PlanarNode extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Edges</b></em>' reference list.
	 * The list contents are of type {@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' reference list.
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarNode_Edges()
	 * @model
	 * @generated
	 */
	EList<PlanarEdge> getEdges();

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link de.cau.cs.se.software.evaluation.hypergraph.EModuleKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.EModuleKind
	 * @see #setKind(EModuleKind)
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarNode_Kind()
	 * @model unique="false"
	 * @generated
	 */
	EModuleKind getKind();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.EModuleKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(EModuleKind value);

	/**
	 * Returns the value of the '<em><b>Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' attribute.
	 * @see #setContext(String)
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarNode_Context()
	 * @model unique="false"
	 * @generated
	 */
	String getContext();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarNode#getContext <em>Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' attribute.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(String value);

} // PlanarNode
