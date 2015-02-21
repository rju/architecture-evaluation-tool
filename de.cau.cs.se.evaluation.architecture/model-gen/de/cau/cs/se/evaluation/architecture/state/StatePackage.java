/**
 */
package de.cau.cs.se.evaluation.architecture.state;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see de.cau.cs.se.evaluation.architecture.state.StateFactory
 * @model kind="package"
 * @generated
 */
public interface StatePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "state";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://evalutation.se.cs.cau.de/hypergraph/metrics/state";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "state";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	StatePackage eINSTANCE = de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl.init();

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.state.impl.StateModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.state.impl.StateModelImpl
	 * @see de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl#getStateModel()
	 * @generated
	 */
	int STATE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Subsystems</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_MODEL__SUBSYSTEMS = 0;

	/**
	 * The feature id for the '<em><b>Mainsystem</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_MODEL__MAINSYSTEM = 1;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_MODEL_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.state.impl.SystemSetupImpl <em>System Setup</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.state.impl.SystemSetupImpl
	 * @see de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl#getSystemSetup()
	 * @generated
	 */
	int SYSTEM_SETUP = 1;

	/**
	 * The feature id for the '<em><b>Row Pattern Table</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_SETUP__ROW_PATTERN_TABLE = 0;

	/**
	 * The feature id for the '<em><b>System</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_SETUP__SYSTEM = 1;

	/**
	 * The feature id for the '<em><b>System Graph</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_SETUP__SYSTEM_GRAPH = 2;

	/**
	 * The number of structural features of the '<em>System Setup</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_SETUP_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>System Setup</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SYSTEM_SETUP_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.state.impl.RowPatternTableImpl <em>Row Pattern Table</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.state.impl.RowPatternTableImpl
	 * @see de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl#getRowPatternTable()
	 * @generated
	 */
	int ROW_PATTERN_TABLE = 2;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_PATTERN_TABLE__EDGES = 0;

	/**
	 * The feature id for the '<em><b>Patterns</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_PATTERN_TABLE__PATTERNS = 1;

	/**
	 * The number of structural features of the '<em>Row Pattern Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_PATTERN_TABLE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Row Pattern Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_PATTERN_TABLE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link de.cau.cs.se.evaluation.architecture.state.impl.RowPatternImpl <em>Row Pattern</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.cau.cs.se.evaluation.architecture.state.impl.RowPatternImpl
	 * @see de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl#getRowPattern()
	 * @generated
	 */
	int ROW_PATTERN = 3;

	/**
	 * The feature id for the '<em><b>Nodes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_PATTERN__NODES = 0;

	/**
	 * The feature id for the '<em><b>Pattern</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_PATTERN__PATTERN = 1;

	/**
	 * The number of structural features of the '<em>Row Pattern</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_PATTERN_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Row Pattern</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROW_PATTERN_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.state.StateModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.StateModel
	 * @generated
	 */
	EClass getStateModel();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.evaluation.architecture.state.StateModel#getSubsystems <em>Subsystems</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Subsystems</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.StateModel#getSubsystems()
	 * @see #getStateModel()
	 * @generated
	 */
	EReference getStateModel_Subsystems();

	/**
	 * Returns the meta object for the containment reference '{@link de.cau.cs.se.evaluation.architecture.state.StateModel#getMainsystem <em>Mainsystem</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Mainsystem</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.StateModel#getMainsystem()
	 * @see #getStateModel()
	 * @generated
	 */
	EReference getStateModel_Mainsystem();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.state.SystemSetup <em>System Setup</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>System Setup</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.SystemSetup
	 * @generated
	 */
	EClass getSystemSetup();

	/**
	 * Returns the meta object for the containment reference '{@link de.cau.cs.se.evaluation.architecture.state.SystemSetup#getRowPatternTable <em>Row Pattern Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Row Pattern Table</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.SystemSetup#getRowPatternTable()
	 * @see #getSystemSetup()
	 * @generated
	 */
	EReference getSystemSetup_RowPatternTable();

	/**
	 * Returns the meta object for the containment reference '{@link de.cau.cs.se.evaluation.architecture.state.SystemSetup#getSystem <em>System</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>System</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.SystemSetup#getSystem()
	 * @see #getSystemSetup()
	 * @generated
	 */
	EReference getSystemSetup_System();

	/**
	 * Returns the meta object for the containment reference '{@link de.cau.cs.se.evaluation.architecture.state.SystemSetup#getSystemGraph <em>System Graph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>System Graph</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.SystemSetup#getSystemGraph()
	 * @see #getSystemSetup()
	 * @generated
	 */
	EReference getSystemSetup_SystemGraph();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.state.RowPatternTable <em>Row Pattern Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Row Pattern Table</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.RowPatternTable
	 * @generated
	 */
	EClass getRowPatternTable();

	/**
	 * Returns the meta object for the reference list '{@link de.cau.cs.se.evaluation.architecture.state.RowPatternTable#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Edges</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.RowPatternTable#getEdges()
	 * @see #getRowPatternTable()
	 * @generated
	 */
	EReference getRowPatternTable_Edges();

	/**
	 * Returns the meta object for the containment reference list '{@link de.cau.cs.se.evaluation.architecture.state.RowPatternTable#getPatterns <em>Patterns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Patterns</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.RowPatternTable#getPatterns()
	 * @see #getRowPatternTable()
	 * @generated
	 */
	EReference getRowPatternTable_Patterns();

	/**
	 * Returns the meta object for class '{@link de.cau.cs.se.evaluation.architecture.state.RowPattern <em>Row Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Row Pattern</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.RowPattern
	 * @generated
	 */
	EClass getRowPattern();

	/**
	 * Returns the meta object for the reference list '{@link de.cau.cs.se.evaluation.architecture.state.RowPattern#getNodes <em>Nodes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Nodes</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.RowPattern#getNodes()
	 * @see #getRowPattern()
	 * @generated
	 */
	EReference getRowPattern_Nodes();

	/**
	 * Returns the meta object for the attribute list '{@link de.cau.cs.se.evaluation.architecture.state.RowPattern#getPattern <em>Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Pattern</em>'.
	 * @see de.cau.cs.se.evaluation.architecture.state.RowPattern#getPattern()
	 * @see #getRowPattern()
	 * @generated
	 */
	EAttribute getRowPattern_Pattern();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	StateFactory getStateFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.state.impl.StateModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.state.impl.StateModelImpl
		 * @see de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl#getStateModel()
		 * @generated
		 */
		EClass STATE_MODEL = eINSTANCE.getStateModel();

		/**
		 * The meta object literal for the '<em><b>Subsystems</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATE_MODEL__SUBSYSTEMS = eINSTANCE.getStateModel_Subsystems();

		/**
		 * The meta object literal for the '<em><b>Mainsystem</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATE_MODEL__MAINSYSTEM = eINSTANCE.getStateModel_Mainsystem();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.state.impl.SystemSetupImpl <em>System Setup</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.state.impl.SystemSetupImpl
		 * @see de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl#getSystemSetup()
		 * @generated
		 */
		EClass SYSTEM_SETUP = eINSTANCE.getSystemSetup();

		/**
		 * The meta object literal for the '<em><b>Row Pattern Table</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SYSTEM_SETUP__ROW_PATTERN_TABLE = eINSTANCE.getSystemSetup_RowPatternTable();

		/**
		 * The meta object literal for the '<em><b>System</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SYSTEM_SETUP__SYSTEM = eINSTANCE.getSystemSetup_System();

		/**
		 * The meta object literal for the '<em><b>System Graph</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SYSTEM_SETUP__SYSTEM_GRAPH = eINSTANCE.getSystemSetup_SystemGraph();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.state.impl.RowPatternTableImpl <em>Row Pattern Table</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.state.impl.RowPatternTableImpl
		 * @see de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl#getRowPatternTable()
		 * @generated
		 */
		EClass ROW_PATTERN_TABLE = eINSTANCE.getRowPatternTable();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW_PATTERN_TABLE__EDGES = eINSTANCE.getRowPatternTable_Edges();

		/**
		 * The meta object literal for the '<em><b>Patterns</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW_PATTERN_TABLE__PATTERNS = eINSTANCE.getRowPatternTable_Patterns();

		/**
		 * The meta object literal for the '{@link de.cau.cs.se.evaluation.architecture.state.impl.RowPatternImpl <em>Row Pattern</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.cau.cs.se.evaluation.architecture.state.impl.RowPatternImpl
		 * @see de.cau.cs.se.evaluation.architecture.state.impl.StatePackageImpl#getRowPattern()
		 * @generated
		 */
		EClass ROW_PATTERN = eINSTANCE.getRowPattern();

		/**
		 * The meta object literal for the '<em><b>Nodes</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROW_PATTERN__NODES = eINSTANCE.getRowPattern_Nodes();

		/**
		 * The meta object literal for the '<em><b>Pattern</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROW_PATTERN__PATTERN = eINSTANCE.getRowPattern_Pattern();

	}

} //StatePackage
