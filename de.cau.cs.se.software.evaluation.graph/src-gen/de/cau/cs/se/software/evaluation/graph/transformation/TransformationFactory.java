/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.cau.cs.se.software.evaluation.graph.transformation.TransformationPackage
 * @generated
 */
public interface TransformationFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TransformationFactory eINSTANCE = de.cau.cs.se.software.evaluation.graph.transformation.impl.TransformationFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Planar Visualization Graph</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Planar Visualization Graph</em>'.
	 * @generated
	 */
	PlanarVisualizationGraph createPlanarVisualizationGraph();

	/**
	 * Returns a new object of class '<em>Planar Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Planar Node</em>'.
	 * @generated
	 */
	PlanarNode createPlanarNode();

	/**
	 * Returns a new object of class '<em>Planar Edge</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Planar Edge</em>'.
	 * @generated
	 */
	PlanarEdge createPlanarEdge();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	TransformationPackage getTransformationPackage();

} //TransformationFactory
