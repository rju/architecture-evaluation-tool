/**
 */
package de.cau.cs.se.software.evaluation.hypergraph;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage
 * @generated
 */
public interface HypergraphFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	HypergraphFactory eINSTANCE = de.cau.cs.se.software.evaluation.hypergraph.impl.HypergraphFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Hypergraph</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Hypergraph</em>'.
	 * @generated
	 */
	Hypergraph createHypergraph();

	/**
	 * Returns a new object of class '<em>Modular Hypergraph</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Modular Hypergraph</em>'.
	 * @generated
	 */
	ModularHypergraph createModularHypergraph();

	/**
	 * Returns a new object of class '<em>Module</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Module</em>'.
	 * @generated
	 */
	Module createModule();

	/**
	 * Returns a new object of class '<em>Node</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node</em>'.
	 * @generated
	 */
	Node createNode();

	/**
	 * Returns a new object of class '<em>Edge</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Edge</em>'.
	 * @generated
	 */
	Edge createEdge();

	/**
	 * Returns a new object of class '<em>Named Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Named Element</em>'.
	 * @generated
	 */
	NamedElement createNamedElement();

	/**
	 * Returns a new object of class '<em>Node Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Trace</em>'.
	 * @generated
	 */
	NodeTrace createNodeTrace();

	/**
	 * Returns a new object of class '<em>Edge Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Edge Trace</em>'.
	 * @generated
	 */
	EdgeTrace createEdgeTrace();

	/**
	 * Returns a new object of class '<em>Generic Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Generic Trace</em>'.
	 * @generated
	 */
	GenericTrace createGenericTrace();

	/**
	 * Returns a new object of class '<em>Node Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node Reference</em>'.
	 * @generated
	 */
	NodeReference createNodeReference();

	/**
	 * Returns a new object of class '<em>Edge Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Edge Reference</em>'.
	 * @generated
	 */
	EdgeReference createEdgeReference();

	/**
	 * Returns a new object of class '<em>Module Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Module Trace</em>'.
	 * @generated
	 */
	ModuleTrace createModuleTrace();

	/**
	 * Returns a new object of class '<em>Module Reference</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Module Reference</em>'.
	 * @generated
	 */
	ModuleReference createModuleReference();

	/**
	 * Returns a new object of class '<em>Type Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Type Trace</em>'.
	 * @generated
	 */
	TypeTrace createTypeTrace();

	/**
	 * Returns a new object of class '<em>Field Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Field Trace</em>'.
	 * @generated
	 */
	FieldTrace createFieldTrace();

	/**
	 * Returns a new object of class '<em>Method Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Method Trace</em>'.
	 * @generated
	 */
	MethodTrace createMethodTrace();

	/**
	 * Returns a new object of class '<em>Caller Callee Trace</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Caller Callee Trace</em>'.
	 * @generated
	 */
	CallerCalleeTrace createCallerCalleeTrace();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	HypergraphPackage getHypergraphPackage();

} //HypergraphFactory
