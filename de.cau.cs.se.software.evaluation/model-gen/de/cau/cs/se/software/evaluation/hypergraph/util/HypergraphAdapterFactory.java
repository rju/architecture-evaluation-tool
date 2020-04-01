/**
 */
package de.cau.cs.se.software.evaluation.hypergraph.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import de.cau.cs.se.software.evaluation.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.EdgeReference;
import de.cau.cs.se.software.evaluation.hypergraph.EdgeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.FieldTrace;
import de.cau.cs.se.software.evaluation.hypergraph.GenericTrace;
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage;
import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleReference;
import de.cau.cs.se.software.evaluation.hypergraph.ModuleTrace;
import de.cau.cs.se.software.evaluation.hypergraph.NamedElement;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.hypergraph.NodeTrace;
import de.cau.cs.se.software.evaluation.hypergraph.TypeTrace;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * 
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage
 * @generated
 */
public class HypergraphAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static HypergraphPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
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
	 * 
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(final Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected HypergraphSwitch<Adapter> modelSwitch = new HypergraphSwitch<Adapter>() {
		@Override
		public Adapter caseHypergraph(final Hypergraph object) {
			return HypergraphAdapterFactory.this.createHypergraphAdapter();
		}

		@Override
		public Adapter caseModularHypergraph(final ModularHypergraph object) {
			return HypergraphAdapterFactory.this.createModularHypergraphAdapter();
		}

		@Override
		public Adapter caseModule(final Module object) {
			return HypergraphAdapterFactory.this.createModuleAdapter();
		}

		@Override
		public Adapter caseNode(final Node object) {
			return HypergraphAdapterFactory.this.createNodeAdapter();
		}

		@Override
		public Adapter caseEdge(final Edge object) {
			return HypergraphAdapterFactory.this.createEdgeAdapter();
		}

		@Override
		public Adapter caseNamedElement(final NamedElement object) {
			return HypergraphAdapterFactory.this.createNamedElementAdapter();
		}

		@Override
		public Adapter caseNodeTrace(final NodeTrace object) {
			return HypergraphAdapterFactory.this.createNodeTraceAdapter();
		}

		@Override
		public Adapter caseEdgeTrace(final EdgeTrace object) {
			return HypergraphAdapterFactory.this.createEdgeTraceAdapter();
		}

		@Override
		public Adapter caseGenericTrace(final GenericTrace object) {
			return HypergraphAdapterFactory.this.createGenericTraceAdapter();
		}

		@Override
		public Adapter caseNodeReference(final NodeReference object) {
			return HypergraphAdapterFactory.this.createNodeReferenceAdapter();
		}

		@Override
		public Adapter caseEdgeReference(final EdgeReference object) {
			return HypergraphAdapterFactory.this.createEdgeReferenceAdapter();
		}

		@Override
		public Adapter caseModuleTrace(final ModuleTrace object) {
			return HypergraphAdapterFactory.this.createModuleTraceAdapter();
		}

		@Override
		public Adapter caseModuleReference(final ModuleReference object) {
			return HypergraphAdapterFactory.this.createModuleReferenceAdapter();
		}

		@Override
		public Adapter caseTypeTrace(final TypeTrace object) {
			return HypergraphAdapterFactory.this.createTypeTraceAdapter();
		}

		@Override
		public Adapter caseFieldTrace(final FieldTrace object) {
			return HypergraphAdapterFactory.this.createFieldTraceAdapter();
		}

		@Override
		public Adapter caseMethodTrace(final MethodTrace object) {
			return HypergraphAdapterFactory.this.createMethodTraceAdapter();
		}

		@Override
		public Adapter caseCallerCalleeTrace(final CallerCalleeTrace object) {
			return HypergraphAdapterFactory.this.createCallerCalleeTraceAdapter();
		}

		@Override
		public Adapter caseModelElementTrace(final ModelElementTrace object) {
			return HypergraphAdapterFactory.this.createModelElementTraceAdapter();
		}

		@Override
		public Adapter defaultCase(final EObject object) {
			return HypergraphAdapterFactory.this.createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(final Notifier target) {
		return this.modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.Hypergraph <em>Hypergraph</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.Hypergraph
	 * @generated
	 */
	public Adapter createHypergraphAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph <em>Modular Hypergraph</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph
	 * @generated
	 */
	public Adapter createModularHypergraphAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.Module <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.Module
	 * @generated
	 */
	public Adapter createModuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.Node
	 * @generated
	 */
	public Adapter createNodeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.Edge <em>Edge</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.Edge
	 * @generated
	 */
	public Adapter createEdgeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.NamedElement
	 * @generated
	 */
	public Adapter createNamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.NodeTrace <em>Node Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.NodeTrace
	 * @generated
	 */
	public Adapter createNodeTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.EdgeTrace <em>Edge Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.EdgeTrace
	 * @generated
	 */
	public Adapter createEdgeTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.GenericTrace <em>Generic Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.GenericTrace
	 * @generated
	 */
	public Adapter createGenericTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.NodeReference <em>Node Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.NodeReference
	 * @generated
	 */
	public Adapter createNodeReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.EdgeReference <em>Edge Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.EdgeReference
	 * @generated
	 */
	public Adapter createEdgeReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.ModuleTrace <em>Module Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.ModuleTrace
	 * @generated
	 */
	public Adapter createModuleTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.ModuleReference <em>Module Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.ModuleReference
	 * @generated
	 */
	public Adapter createModuleReferenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.TypeTrace <em>Type Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.TypeTrace
	 * @generated
	 */
	public Adapter createTypeTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.FieldTrace <em>Field Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.FieldTrace
	 * @generated
	 */
	public Adapter createFieldTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.MethodTrace <em>Method Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.MethodTrace
	 * @generated
	 */
	public Adapter createMethodTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.CallerCalleeTrace <em>Caller Callee Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.CallerCalleeTrace
	 * @generated
	 */
	public Adapter createCallerCalleeTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace <em>Model Element Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see de.cau.cs.se.software.evaluation.hypergraph.ModelElementTrace
	 * @generated
	 */
	public Adapter createModelElementTraceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // HypergraphAdapterFactory
