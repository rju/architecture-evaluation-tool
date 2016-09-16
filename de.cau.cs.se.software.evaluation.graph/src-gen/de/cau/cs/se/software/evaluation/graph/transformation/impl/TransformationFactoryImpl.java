/**
 */
package de.cau.cs.se.software.evaluation.graph.transformation.impl;

import de.cau.cs.se.software.evaluation.graph.transformation.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TransformationFactoryImpl extends EFactoryImpl implements TransformationFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TransformationFactory init() {
		try {
			TransformationFactory theTransformationFactory = (TransformationFactory)EPackage.Registry.INSTANCE.getEFactory(TransformationPackage.eNS_URI);
			if (theTransformationFactory != null) {
				return theTransformationFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TransformationFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransformationFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case TransformationPackage.PLANAR_VISUALIZATION_GRAPH: return createPlanarVisualizationGraph();
			case TransformationPackage.PLANAR_NODE: return createPlanarNode();
			case TransformationPackage.PLANAR_EDGE: return createPlanarEdge();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlanarVisualizationGraph createPlanarVisualizationGraph() {
		PlanarVisualizationGraphImpl planarVisualizationGraph = new PlanarVisualizationGraphImpl();
		return planarVisualizationGraph;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlanarNode createPlanarNode() {
		PlanarNodeImpl planarNode = new PlanarNodeImpl();
		return planarNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PlanarEdge createPlanarEdge() {
		PlanarEdgeImpl planarEdge = new PlanarEdgeImpl();
		return planarEdge;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransformationPackage getTransformationPackage() {
		return (TransformationPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TransformationPackage getPackage() {
		return TransformationPackage.eINSTANCE;
	}

} //TransformationFactoryImpl
