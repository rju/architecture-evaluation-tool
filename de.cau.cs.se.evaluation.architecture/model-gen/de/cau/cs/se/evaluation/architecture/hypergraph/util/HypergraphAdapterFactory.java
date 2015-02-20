/**
 */
package de.cau.cs.se.evaluation.architecture.hypergraph.util;

import de.cau.cs.se.evaluation.architecture.hypergraph.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage
 * @generated
 */
public class HypergraphAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static HypergraphPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HypergraphAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = HypergraphPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HypergraphSwitch<Adapter> modelSwitch =
		new HypergraphSwitch<Adapter>() {
			@Override
			public Adapter caseHypergraph(Hypergraph object) {
				return createHypergraphAdapter();
			}
			@Override
			public Adapter caseModularHypergraph(ModularHypergraph object) {
				return createModularHypergraphAdapter();
			}
			@Override
			public Adapter caseModule(Module object) {
				return createModuleAdapter();
			}
			@Override
			public Adapter caseNode(Node object) {
				return createNodeAdapter();
			}
			@Override
			public Adapter caseEdge(Edge object) {
				return createEdgeAdapter();
			}
			@Override
			public Adapter caseNamedElement(NamedElement object) {
				return createNamedElementAdapter();
			}
			@Override
			public Adapter caseNodeTrace(NodeTrace object) {
				return createNodeTraceAdapter();
			}
			@Override
			public Adapter caseEdgeTrace(EdgeTrace object) {
				return createEdgeTraceAdapter();
			}
			@Override
			public Adapter caseGenericTrace(GenericTrace object) {
				return createGenericTraceAdapter();
			}
			@Override
			public Adapter caseNodeReference(NodeReference object) {
				return createNodeReferenceAdapter();
			}
			@Override
			public Adapter caseEdgeReference(EdgeReference object) {
				return createEdgeReferenceAdapter();
			}
			@Override
			public Adapter caseModuleTrace(ModuleTrace object) {
				return createModuleTraceAdapter();
			}
			@Override
			public Adapter caseModuleReference(ModuleReference object) {
				return createModuleReferenceAdapter();
			}
			@Override
			public Adapter caseTypeTrace(TypeTrace object) {
				return createTypeTraceAdapter();
			}
			@Override
			public Adapter caseFieldTrace(FieldTrace object) {
				return createFieldTraceAdapter();
			}
			@Override
			public Adapter caseMethodTrace(MethodTrace object) {
				return createMethodTraceAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph <em>Hypergraph</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
	 * @generated
	 */
	public Adapter createHypergraphAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph <em>Modular Hypergraph</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph
	 * @generated
	 */
	public Adapter createModularHypergraphAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Module <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Module
	 * @generated
	 */
	public Adapter createModuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Node
	 * @generated
	 */
	public Adapter createNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.Edge <em>Edge</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.Edge
	 * @generated
	 */
	public Adapter createEdgeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.NamedElement
	 * @generated
	 */
	public Adapter createNamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace <em>Node Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.NodeTrace
	 * @generated
	 */
	public Adapter createNodeTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace <em>Edge Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.EdgeTrace
	 * @generated
	 */
	public Adapter createEdgeTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.GenericTrace <em>Generic Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.GenericTrace
	 * @generated
	 */
	public Adapter createGenericTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference <em>Node Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.NodeReference
	 * @generated
	 */
	public Adapter createNodeReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference <em>Edge Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.EdgeReference
	 * @generated
	 */
	public Adapter createEdgeReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace <em>Module Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.ModuleTrace
	 * @generated
	 */
	public Adapter createModuleTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference <em>Module Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.ModuleReference
	 * @generated
	 */
	public Adapter createModuleReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace <em>Type Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace
	 * @generated
	 */
	public Adapter createTypeTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace <em>Field Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace
	 * @generated
	 */
	public Adapter createFieldTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace <em>Method Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace
	 * @generated
	 */
	public Adapter createMethodTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //HypergraphAdapterFactory
