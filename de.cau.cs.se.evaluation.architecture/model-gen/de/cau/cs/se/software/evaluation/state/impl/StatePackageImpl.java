/**
 */
package de.cau.cs.se.software.evaluation.state.impl;

import de.cau.cs.se.software.evaluation.hypergraph.HypergraphPackage;

import de.cau.cs.se.software.evaluation.state.RowPattern;
import de.cau.cs.se.software.evaluation.state.RowPatternTable;
import de.cau.cs.se.software.evaluation.state.StateFactory;
import de.cau.cs.se.software.evaluation.state.StateModel;
import de.cau.cs.se.software.evaluation.state.StatePackage;
import de.cau.cs.se.software.evaluation.state.SystemSetup;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StatePackageImpl extends EPackageImpl implements StatePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stateModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass systemSetupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rowPatternTableEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rowPatternEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see de.cau.cs.se.software.evaluation.state.StatePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private StatePackageImpl() {
		super(eNS_URI, StateFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link StatePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static StatePackage init() {
		if (isInited) return (StatePackage)EPackage.Registry.INSTANCE.getEPackage(StatePackage.eNS_URI);

		// Obtain or create and register package
		StatePackageImpl theStatePackage = (StatePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof StatePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new StatePackageImpl());

		isInited = true;

		// Initialize simple dependencies
		HypergraphPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theStatePackage.createPackageContents();

		// Initialize created meta-data
		theStatePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theStatePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(StatePackage.eNS_URI, theStatePackage);
		return theStatePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStateModel() {
		return stateModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStateModel_Subsystems() {
		return (EReference)stateModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStateModel_Mainsystem() {
		return (EReference)stateModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSystemSetup() {
		return systemSetupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSystemSetup_RowPatternTable() {
		return (EReference)systemSetupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSystemSetup_System() {
		return (EReference)systemSetupEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSystemSetup_SystemGraph() {
		return (EReference)systemSetupEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRowPatternTable() {
		return rowPatternTableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRowPatternTable_Edges() {
		return (EReference)rowPatternTableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRowPatternTable_Patterns() {
		return (EReference)rowPatternTableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRowPattern() {
		return rowPatternEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRowPattern_Nodes() {
		return (EReference)rowPatternEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRowPattern_Pattern() {
		return (EAttribute)rowPatternEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StateFactory getStateFactory() {
		return (StateFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		stateModelEClass = createEClass(STATE_MODEL);
		createEReference(stateModelEClass, STATE_MODEL__SUBSYSTEMS);
		createEReference(stateModelEClass, STATE_MODEL__MAINSYSTEM);

		systemSetupEClass = createEClass(SYSTEM_SETUP);
		createEReference(systemSetupEClass, SYSTEM_SETUP__ROW_PATTERN_TABLE);
		createEReference(systemSetupEClass, SYSTEM_SETUP__SYSTEM);
		createEReference(systemSetupEClass, SYSTEM_SETUP__SYSTEM_GRAPH);

		rowPatternTableEClass = createEClass(ROW_PATTERN_TABLE);
		createEReference(rowPatternTableEClass, ROW_PATTERN_TABLE__EDGES);
		createEReference(rowPatternTableEClass, ROW_PATTERN_TABLE__PATTERNS);

		rowPatternEClass = createEClass(ROW_PATTERN);
		createEReference(rowPatternEClass, ROW_PATTERN__NODES);
		createEAttribute(rowPatternEClass, ROW_PATTERN__PATTERN);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		HypergraphPackage theHypergraphPackage = (HypergraphPackage)EPackage.Registry.INSTANCE.getEPackage(HypergraphPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(stateModelEClass, StateModel.class, "StateModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStateModel_Subsystems(), this.getSystemSetup(), null, "subsystems", null, 0, -1, StateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStateModel_Mainsystem(), this.getSystemSetup(), null, "mainsystem", null, 1, 1, StateModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(systemSetupEClass, SystemSetup.class, "SystemSetup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSystemSetup_RowPatternTable(), this.getRowPatternTable(), null, "rowPatternTable", null, 1, 1, SystemSetup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSystemSetup_System(), theHypergraphPackage.getHypergraph(), null, "system", null, 1, 1, SystemSetup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSystemSetup_SystemGraph(), theHypergraphPackage.getHypergraph(), null, "systemGraph", null, 1, 1, SystemSetup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(rowPatternTableEClass, RowPatternTable.class, "RowPatternTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRowPatternTable_Edges(), theHypergraphPackage.getEdge(), null, "edges", null, 0, -1, RowPatternTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		getRowPatternTable_Edges().getEKeys().add(theHypergraphPackage.getNamedElement_Name());
		initEReference(getRowPatternTable_Patterns(), this.getRowPattern(), null, "patterns", null, 0, -1, RowPatternTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(rowPatternEClass, RowPattern.class, "RowPattern", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRowPattern_Nodes(), theHypergraphPackage.getNode(), null, "nodes", null, 1, -1, RowPattern.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRowPattern_Pattern(), ecorePackage.getEBoolean(), "pattern", null, 0, -1, RowPattern.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //StatePackageImpl
