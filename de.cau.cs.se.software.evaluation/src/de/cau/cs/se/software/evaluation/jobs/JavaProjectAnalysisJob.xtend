package de.cau.cs.se.software.evaluation.jobs

import de.cau.cs.se.software.evaluation.transformation.TransformationCyclomaticComplexity
import de.cau.cs.se.software.evaluation.transformation.TransformationLinesOfCode
import de.cau.cs.se.software.evaluation.transformation.java.TransformationJavaMethodsToModularHypergraph
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider
import de.cau.cs.se.software.evaluation.views.LogView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.List
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.widgets.Shell
import org.eclipse.ui.PartInitException
import org.eclipse.ui.PlatformUI

import static extension de.cau.cs.se.software.evaluation.transformation.java.NameResolutionHelper.*
import de.cau.cs.se.software.evaluation.views.LogModelProvider

class JavaProjectAnalysisJob extends AbstractHypergraphAnalysisJob {

	val IJavaProject javaProject

	val Shell shell

	String DATA_TYPE_PATTERN_FILE = "data-type-pattern.cfg"

	String DATA_TYPE_PATTERN_TITLE = "data type pattern"

	String OBSERVED_SYSTEM_PATTERN_FILE = "observed-system.cfg"

	String OBSERVED_SYSTEM_TITLE = "observed system"

	public new(IProject project, Shell shell) {
		super(project)
		this.javaProject = project.getJavaProject
		this.shell = shell
	}

	/**
	 * Execute all metrics as requested.
	 */
	protected override IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Collect project information", 0)

		val dataTypePatterns = getPatternFile(project.project, DATA_TYPE_PATTERN_FILE, DATA_TYPE_PATTERN_TITLE)
		if (dataTypePatterns != null) {
			val observedSystemPatterns = getPatternFile(project.project, OBSERVED_SYSTEM_PATTERN_FILE,
				OBSERVED_SYSTEM_TITLE)
			if (observedSystemPatterns != null) {
				val types = new ArrayList<IType>()
				monitor.subTask("Scanning project " + project.project.name)
				javaProject.allPackageFragmentRoots.forEach[root|types.checkForTypes(root, monitor)]

				if (types != null) {
					val result = AnalysisResultModelProvider.INSTANCE
					LogModelProvider.INSTANCE.projectName = project.name
					
					val classes = types.collectAllSourceClasses(dataTypePatterns, observedSystemPatterns, monitor)

					calculateCodeStatistics(classes, monitor, result)

					val inputHypergraph = createHypergraphForJavaProject(dataTypePatterns, observedSystemPatterns,
						classes, monitor, result)

					calculateSize(inputHypergraph, monitor, result)

					calculateComplexity(inputHypergraph, monitor, result)

					calculateCoupling(inputHypergraph, monitor, result)

					calculateCohesion(inputHypergraph, monitor, result)
					
					updateLogView()
					
					monitor.done()

					return Status.OK_STATUS
				} else {
					showErrorMessage("Project Setup Error", "No classes found for analysis.")
				}
			}
		}
		
		updateLogView()
		
		return Status.CANCEL_STATUS
	}

	/**
	 * Claculate code statistics.
	 */
	private def calculateCodeStatistics(List<AbstractTypeDeclaration> classes, IProgressMonitor monitor,
		AnalysisResultModelProvider result) {
		val linesOfCodeMetric = new TransformationLinesOfCode(monitor)
		linesOfCodeMetric.generate(classes)
		val javaMethodComplexity = new TransformationCyclomaticComplexity(monitor)
		javaMethodComplexity.generate(classes)

		result.addResult(project.project.name, "size of observed system", classes.size)
		result.addResult(project.project.name, "lines of code (LOC)", linesOfCodeMetric.result)
		for (var i = 1; i < javaMethodComplexity.result.size; i++) {
			result.addResult(project.project.name, "cyclomatic complexity bucket " + i,
				javaMethodComplexity.result.get(i))
		}
		updateView(null)
	}

	/**
	 * Construct hypergraph from java project
	 */
	private def createHypergraphForJavaProject(
		List<String> dataTypePatterns,
		List<String> observedSystemPatterns,
		List<AbstractTypeDeclaration> classes,
		IProgressMonitor monitor,
		AnalysisResultModelProvider result
	) {
		/** construct analysis. */
		val javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(javaProject, dataTypePatterns,
			observedSystemPatterns, monitor)

		javaToModularHypergraph.generate(classes)

		result.addResult(project.name, "number of modules", javaToModularHypergraph.result.modules.size)
		result.addResult(project.name, "number of nodes", javaToModularHypergraph.result.nodes.size)
		result.addResult(project.name, "number of edges", javaToModularHypergraph.result.edges.size)

		updateView(javaToModularHypergraph.result)
		updateLogView()

		return javaToModularHypergraph.result
	}

	/**
	 * Get Java project of a project.
	 */
	private def IJavaProject getJavaProject(IProject project) {
		if (project.hasNature(JavaCore.NATURE_ID)) {
			return JavaCore.create(project)
		} else
			return null
	}

	/**
	 * Find all classes in the IType list which belong to the observed system.
	 * 
	 * @param classes the classes of the observed system
	 * @param types the types found by scanning the project
	 * @param dataTypePatterns pattern list for data types to be excluded
	 * @param observedSystemPatterns pattern list for classes to be included
	 */
	private def List<AbstractTypeDeclaration> collectAllSourceClasses(
		List<IType> types,
		List<String> dataTypePatterns,
		List<String> observedSystemPatterns,
		IProgressMonitor monitor
	) {
		val List<AbstractTypeDeclaration> classes = new ArrayList<AbstractTypeDeclaration>

		types.forEach [ jdtType |
			val unit = jdtType.getUnitForType(observedSystemPatterns, monitor)
			if (unit != null) {
				unit.types.forEach [ unitType |
					if (unitType instanceof TypeDeclaration) {
						val type = unitType as TypeDeclaration
						val typeBinding = type.resolveBinding
						val name = typeBinding.determineFullyQualifiedName
						if (observedSystemPatterns.exists[name.matches(it)])
							if (!isClassDataType(typeBinding, dataTypePatterns))
								classes.add(type)
					}
				]
			}
		]

		return classes
	}

	/**
	 * Get compilation unit for JDT type.
	 */
	private def CompilationUnit getUnitForType(
		IType type,
		List<String> observedSystemPatterns,
		IProgressMonitor monitor
	) {
		val outerTypeName = type.packageFragment.elementName + "." + type.elementName
		if (observedSystemPatterns.exists[outerTypeName.matches(it)]) {
			monitor.subTask("Parsing " + outerTypeName)
			val options = JavaCore.getOptions()
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options)

			val ASTParser parser = ASTParser.newParser(AST.JLS8)
			parser.setProject(this.javaProject)
			parser.setCompilerOptions(options)
			parser.kind = ASTParser.K_COMPILATION_UNIT
			parser.source = type.compilationUnit.buffer.contents.toCharArray()
			parser.unitName = type.compilationUnit.elementName
			parser.resolveBindings = true
			parser.bindingsRecovery = true
			parser.statementsRecovery = true

			return parser.createAST(null) as CompilationUnit
		} else
			return null
	}

	/**
	 * Determine if the given type is a data type.
	 * 
	 * @param type the type to be evaluated
	 * @param dataTypes a list of data types
	 * 
	 * @return returns true if the given type is a data type and not a behavior type.
	 */
	private def boolean isClassDataType(ITypeBinding typeBinding, List<String> dataTypePatterns) {
		// TODO this might be to simple.
		val name = typeBinding.determineFullyQualifiedName
		return dataTypePatterns.exists[pattern|name.matches(pattern)]
	}

	/**
	 * Get data type patterns.
	 * 
	 * @param project the project containing the data-type-pattern.cfg 
	 */
	private def List<String> getPatternFile(IProject project, String filename, String title) {
		val patternFile = project.findMember(filename) as IFile
		if (patternFile != null) {
			if (patternFile.isSynchronized(1)) {
				return readPattern(patternFile)
			}
		}

		showErrorMessage(
			"Configuration Error",
			"The " + title + " file (" + filename + ") containing class name patterns is missing."
		)

		return null
	}

	/**
	 * Read full qualified class name patterns form an IFile.
	 * 
	 * @param file the file to read.
	 */
	private def readPattern(IFile file) {
		val List<String> patterns = new ArrayList<String>()
		val reader = new BufferedReader(new InputStreamReader(file.contents))
		var String line
		while ((line = reader.readLine()) != null) {
			patterns.add(line.replaceAll("\\.", "\\."))
		}

		return patterns
	}

	/** check for types in the project hierarchy */
	/**
	 * in fragment roots 
	 */
	private def void checkForTypes(List<IType> types, IPackageFragmentRoot root, IProgressMonitor monitor) {
		monitor.subTask(root.elementName)
		root.children.forEach [ element |
			if (element instanceof IPackageFragment) {
				types.checkForTypes(element as IPackageFragment)
			}
		]
	}

	/**
	 * in fragments
	 */
	private def void checkForTypes(List<IType> types, IPackageFragment fragment) {
		fragment.children.forEach [ element |
			if (element instanceof IPackageFragment) {
				types.checkForTypes(element as IPackageFragment)
			} else if (element instanceof ICompilationUnit) {
				types.checkForTypes(element as ICompilationUnit)
			}
		]
	}

	/**
	 * in compilation units
	 */
	private def void checkForTypes(List<IType> types, ICompilationUnit unit) {
		unit.allTypes.forEach [ type |
			if (type instanceof IType) {
				if(!(type as IType).binary) types.add(type)
			}
		]
	}

	private def showErrorMessage(String title, String message) {
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
			public override void run() {
				try {
					MessageDialog.openError(shell, title, message)
				} catch (PartInitException e) {
					e.printStackTrace()
				}
			}
		})
	}
	
	private def updateLogView() {
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
			public override void run() {
				try {
					val part2 = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().
						showView(LogView.ID)
					(part2 as LogView).update()
				} catch (PartInitException e) {
					e.printStackTrace()
				}
			}
		})
	}

}
