/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Planar Edge</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getCount <em>Count</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getStart <em>Start</em>}</li>
 *   <li>{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getEnd <em>End</em>}</li>
 * </ul>
 *
 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarEdge()
 * @model
 * @generated
 */
public interface PlanarEdge extends NamedElement {
	/**
	 * Returns the value of the '<em><b>Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Count</em>' attribute.
	 * @see #setCount(int)
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarEdge_Count()
	 * @model unique="false"
	 * @generated
	 */
	int getCount();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getCount <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Count</em>' attribute.
	 * @see #getCount()
	 * @generated
	 */
	void setCount(int value);

	/**
	 * Returns the value of the '<em><b>Start</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start</em>' reference.
	 * @see #setStart(PlanarNode)
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarEdge_Start()
	 * @model
	 * @generated
	 */
	PlanarNode getStart();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getStart <em>Start</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' reference.
	 * @see #getStart()
	 * @generated
	 */
	void setStart(PlanarNode value);

	/**
	 * Returns the value of the '<em><b>End</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End</em>' reference.
	 * @see #setEnd(PlanarNode)
	 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage#getPlanarEdge_End()
	 * @model
	 * @generated
	 */
	PlanarNode getEnd();

	/**
	 * Sets the value of the '{@link de.cau.cs.se.software.evaluation.graph.transformation.PlanarEdge#getEnd <em>End</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End</em>' reference.
	 * @see #getEnd()
	 * @generated
	 */
	void setEnd(PlanarNode value);

} // PlanarEdge
