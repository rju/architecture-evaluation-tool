/**
 */
package de.cau.cs.se.software.evaluation.hypergraph.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

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
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * 
 * @see de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage
 * @generated
 */
public class HypergraphSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static HypergraphPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
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
	 * 
	 * @param ePackage
	 *            the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(final EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(final int classifierID, final EObject theEObject) {
		switch (classifierID) {
		case HypergraphPackage.HYPERGRAPH: {
			final Hypergraph hypergraph = (Hypergraph) theEObject;
			T result = this.caseHypergraph(hypergraph);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.MODULAR_HYPERGRAPH: {
			final ModularHypergraph modularHypergraph = (ModularHypergraph) theEObject;
			T result = this.caseModularHypergraph(modularHypergraph);
			if (result == null) {
				result = this.caseHypergraph(modularHypergraph);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.MODULE: {
			final Module module = (Module) theEObject;
			T result = this.caseModule(module);
			if (result == null) {
				result = this.caseNamedElement(module);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.NODE: {
			final Node node = (Node) theEObject;
			T result = this.caseNode(node);
			if (result == null) {
				result = this.caseNamedElement(node);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.EDGE: {
			final Edge edge = (Edge) theEObject;
			T result = this.caseEdge(edge);
			if (result == null) {
				result = this.caseNamedElement(edge);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.NAMED_ELEMENT: {
			final NamedElement namedElement = (NamedElement) theEObject;
			T result = this.caseNamedElement(namedElement);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.NODE_TRACE: {
			final NodeTrace nodeTrace = (NodeTrace) theEObject;
			T result = this.caseNodeTrace(nodeTrace);
			if (result == null) {
				result = this.caseNodeReference(nodeTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.EDGE_TRACE: {
			final EdgeTrace edgeTrace = (EdgeTrace) theEObject;
			T result = this.caseEdgeTrace(edgeTrace);
			if (result == null) {
				result = this.caseEdgeReference(edgeTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.GENERIC_TRACE: {
			final GenericTrace genericTrace = (GenericTrace) theEObject;
			T result = this.caseGenericTrace(genericTrace);
			if (result == null) {
				result = this.caseNodeReference(genericTrace);
			}
			if (result == null) {
				result = this.caseEdgeReference(genericTrace);
			}
			if (result == null) {
				result = this.caseModuleReference(genericTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.NODE_REFERENCE: {
			final NodeReference nodeReference = (NodeReference) theEObject;
			T result = this.caseNodeReference(nodeReference);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.EDGE_REFERENCE: {
			final EdgeReference edgeReference = (EdgeReference) theEObject;
			T result = this.caseEdgeReference(edgeReference);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.MODULE_TRACE: {
			final ModuleTrace moduleTrace = (ModuleTrace) theEObject;
			T result = this.caseModuleTrace(moduleTrace);
			if (result == null) {
				result = this.caseModuleReference(moduleTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.MODULE_REFERENCE: {
			final ModuleReference moduleReference = (ModuleReference) theEObject;
			T result = this.caseModuleReference(moduleReference);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.TYPE_TRACE: {
			final TypeTrace typeTrace = (TypeTrace) theEObject;
			T result = this.caseTypeTrace(typeTrace);
			if (result == null) {
				result = this.caseModuleReference(typeTrace);
			}
			if (result == null) {
				result = this.caseNodeReference(typeTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.FIELD_TRACE: {
			final FieldTrace fieldTrace = (FieldTrace) theEObject;
			T result = this.caseFieldTrace(fieldTrace);
			if (result == null) {
				result = this.caseEdgeReference(fieldTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.METHOD_TRACE: {
			final MethodTrace methodTrace = (MethodTrace) theEObject;
			T result = this.caseMethodTrace(methodTrace);
			if (result == null) {
				result = this.caseNodeReference(methodTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.CALLER_CALLEE_TRACE: {
			final CallerCalleeTrace callerCalleeTrace = (CallerCalleeTrace) theEObject;
			T result = this.caseCallerCalleeTrace(callerCalleeTrace);
			if (result == null) {
				result = this.caseEdgeReference(callerCalleeTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case HypergraphPackage.MODEL_ELEMENT_TRACE: {
			final ModelElementTrace modelElementTrace = (ModelElementTrace) theEObject;
			T result = this.caseModelElementTrace(modelElementTrace);
			if (result == null) {
				result = this.caseEdgeReference(modelElementTrace);
			}
			if (result == null) {
				result = this.caseModuleReference(modelElementTrace);
			}
			if (result == null) {
				result = this.caseNodeReference(modelElementTrace);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		default:
			return this.defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Hypergraph</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Hypergraph</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHypergraph(final Hypergraph object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Modular Hypergraph</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Modular Hypergraph</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModularHypergraph(final ModularHypergraph object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModule(final Module object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNode(final Node object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Edge</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Edge</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdge(final Edge object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Named Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Named Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNamedElement(final NamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeTrace(final NodeTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Edge Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Edge Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdgeTrace(final EdgeTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Generic Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Generic Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGenericTrace(final GenericTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Node Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Node Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseNodeReference(final NodeReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Edge Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Edge Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEdgeReference(final EdgeReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModuleTrace(final ModuleTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module Reference</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module Reference</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModuleReference(final ModuleReference object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Type Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Type Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTypeTrace(final TypeTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Field Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Field Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFieldTrace(final FieldTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Method Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Method Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMethodTrace(final MethodTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Caller Callee Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Caller Callee Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCallerCalleeTrace(final CallerCalleeTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Element Trace</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Element Trace</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelElementTrace(final ModelElementTrace object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(final EObject object) {
		return null;
	}

} // HypergraphSwitch
