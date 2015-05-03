/**
 */
package de.cau.cs.se.evaluation.architecture.hypergraph.util;

import de.cau.cs.se.evaluation.architecture.hypergraph.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphPackage
 * @generated
 */
public class HypergraphSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static HypergraphPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HypergraphSwitch() {
		if (modelPackage == null) {
			modelPackage = HypergraphPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case HypergraphPackage.HYPERGRAPH: {
				Hypergraph hypergraph = (Hypergraph)theEObject;
				T result = caseHypergraph(hypergraph);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.MODULAR_HYPERGRAPH: {
				ModularHypergraph modularHypergraph = (ModularHypergraph)theEObject;
				T result = caseModularHypergraph(modularHypergraph);
				if (result == null) result = caseHypergraph(modularHypergraph);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.MODULE: {
				Module module = (Module)theEObject;
				T result = caseModule(module);
				if (result == null) result = caseNamedElement(module);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.NODE: {
				Node node = (Node)theEObject;
				T result = caseNode(node);
				if (result == null) result = caseNamedElement(node);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.EDGE: {
				Edge edge = (Edge)theEObject;
				T result = caseEdge(edge);
				if (result == null) result = caseNamedElement(edge);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.NAMED_ELEMENT: {
				NamedElement namedElement = (NamedElement)theEObject;
				T result = caseNamedElement(namedElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.NODE_TRACE: {
				NodeTrace nodeTrace = (NodeTrace)theEObject;
				T result = caseNodeTrace(nodeTrace);
				if (result == null) result = caseNodeReference(nodeTrace);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.EDGE_TRACE: {
				EdgeTrace edgeTrace = (EdgeTrace)theEObject;
				T result = caseEdgeTrace(edgeTrace);
				if (result == null) result = caseEdgeReference(edgeTrace);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.GENERIC_TRACE: {
				GenericTrace genericTrace = (GenericTrace)theEObject;
				T result = caseGenericTrace(genericTrace);
				if (result == null) result = caseNodeReference(genericTrace);
				if (result == null) result = caseEdgeReference(genericTrace);
				if (result == null) result = caseModuleReference(genericTrace);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.NODE_REFERENCE: {
				NodeReference nodeReference = (NodeReference)theEObject;
				T result = caseNodeReference(nodeReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.EDGE_REFERENCE: {
				EdgeReference edgeReference = (EdgeReference)theEObject;
				T result = caseEdgeReference(edgeReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.MODULE_TRACE: {
				ModuleTrace moduleTrace = (ModuleTrace)theEObject;
				T result = caseModuleTrace(moduleTrace);
				if (result == null) result = caseModuleReference(moduleTrace);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.MODULE_REFERENCE: {
				ModuleReference moduleReference = (ModuleReference)theEObject;
				T result = caseModuleReference(moduleReference);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.TYPE_TRACE: {
				TypeTrace typeTrace = (TypeTrace)theEObject;
				T result = caseTypeTrace(typeTrace);
				if (result == null) result = caseModuleReference(typeTrace);
				if (result == null) result = caseNodeReference(typeTrace);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.FIELD_TRACE: {
				FieldTrace fieldTrace = (FieldTrace)theEObject;
				T result = caseFieldTrace(fieldTrace);
				if (result == null) result = caseEdgeReference(fieldTrace);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.METHOD_TRACE: {
				MethodTrace methodTrace = (MethodTrace)theEObject;
				T result = caseMethodTrace(methodTrace);
				if (result == null) result = caseNodeReference(methodTrace);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case HypergraphPackage.CALLER_CALLEE_TRACE: {
				CallerCalleeTrace callerCalleeTrace = (CallerCalleeTrace)theEObject;
				T result = caseCallerCalleeTrace(callerCalleeTrace);
				if (result == null) result = caseEdgeReference(callerCalleeTrace);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Hypergraph</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Hypergraph</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHypergraph(Hypergraph object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Modular Hypergraph</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Modular Hypergraph</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModularHypergraph(ModularHypergraph object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModule(Module object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNode(Node object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Edge</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Edge</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdge(Edge object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedElement(NamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeTrace(NodeTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Edge Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Edge Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdgeTrace(EdgeTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGenericTrace(GenericTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeReference(NodeReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Edge Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Edge Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdgeReference(EdgeReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModuleTrace(ModuleTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModuleReference(ModuleReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Type Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Type Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTypeTrace(TypeTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Field Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Field Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFieldTrace(FieldTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Method Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Method Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMethodTrace(MethodTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Caller Callee Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Caller Callee Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCallerCalleeTrace(CallerCalleeTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //HypergraphSwitch
