/***************************************************************************
 * Copyright 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package de.cau.cs.se.evaluation.architecture.jobs

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.List
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
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
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ITreeSelection
import org.eclipse.jface.viewers.TreeSelection
import org.eclipse.swt.widgets.Shell
import org.eclipse.ui.PartInitException
import org.eclipse.ui.PlatformUI

import static extension de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper.*

class CollectInputModelJob extends Job {
	
	ISelection selection
	
	Shell shell
	
	List<String> dataTypePatterns
	
	List<String> observedSystemPatterns
	
	val List<AbstractTypeDeclaration> classes = new ArrayList<AbstractTypeDeclaration>
	
	new(ISelection selection, Shell shell) {
		super("Collect input model")
		this.selection = selection
		this.shell = shell
	}
	
	var IJavaProject project
	
	/**
	 * Execute the preparation
	 */
	override protected run(IProgressMonitor monitor) {
		monitor.beginTask("Collect project information",0)		
		val types = determineClassFiles(monitor)
		
		if (types != null) {
			val projects = new ArrayList<IJavaProject>()
			types.forEach[type | if (!projects.contains(type.javaProject)) projects.add(type.javaProject)]
			project = projects.get(0)

			types.collectAllSourceClasses(dataTypePatterns, observedSystemPatterns, monitor)
		
			return Status.OK_STATUS
		} else {
			return Status.CANCEL_STATUS
		}
	}
	
	public def List<AbstractTypeDeclaration> getClasses() {
		this.classes
	}
	
	public def IJavaProject getProject() {
		this.project
	}
	
	public def getDataTypePatterns() {
		this.dataTypePatterns
	}
	
	public def getObservedSystemPatterns() {
		this.observedSystemPatterns
	}
	
	/**
	 * Determine class files.
	 */
	private def determineClassFiles(IProgressMonitor monitor) {
		if (this.selection instanceof IStructuredSelection) {
			if (this.selection instanceof ITreeSelection) {
				val TreeSelection treeSelection = selection as TreeSelection
				
				val selectedElement = treeSelection.iterator.findFirst[element | (element instanceof IProject)]
				if (selectedElement != null)
					return (selectedElement as IProject).scanForClasses(monitor)
			}
		}
		return null
	}
	
	/**
	 * Scann for classes in project.
	 */
	private def List<IType> scanForClasses(IProject project, IProgressMonitor monitor) {
		val types = new ArrayList<IType>()
		monitor.subTask("Scanning project " + project.name)
		val dataTypePatternsFile = project.findMember("data-type-pattern.cfg") as IFile
		if (dataTypePatternsFile != null) {
			if (dataTypePatternsFile.isSynchronized(1)) {
				dataTypePatterns = readPattern(dataTypePatternsFile)
				val observedSystemPatternsFile = project.findMember("observed-system.cfg") as IFile
				if (observedSystemPatternsFile != null) {
					if (observedSystemPatternsFile.isSynchronized(1)) {
						observedSystemPatterns  = readPattern(observedSystemPatternsFile)
						if (project.hasNature(JavaCore.NATURE_ID)) {
							val IJavaProject javaProject = JavaCore.create(project);
							javaProject.allPackageFragmentRoots.forEach[root | types.checkForTypes(root, monitor)]
							return types
						} 
					} else {
						showErrorMessage("Configuration Error", "Observed system file (observed-system.cfg) listing patterns for classes of the observed system is missing.")
					}	
				} else {
					showErrorMessage("Configuration Error", "Observed system file (observed-system.cfg) listing patterns for classes of the observed system is missing.")
				}
			} else {
				showErrorMessage("Configuration Error", "Data type pattern file (data-type-pattern.cfg) listing patterns for data type classes is missing.")
			}
		} else {
			showErrorMessage("Configuration Error", "Data type pattern file (data-type-pattern.cfg) listing patterns for data type classes is missing.")
		}
		
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
	private def void collectAllSourceClasses(List<IType> types, 
		List<String> dataTypePatterns, List<String> observedSystemPatterns,
		IProgressMonitor monitor
	) {
		types.forEach[jdtType |
			val unit = jdtType.getUnitForType(monitor)
			if (unit != null) {
				unit.types.forEach[unitType |
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
		return dataTypePatterns.exists[pattern | name.matches(pattern)]
	}
	
	/**
	 * Get compilation unit for JDT type.
	 */
	private def CompilationUnit getUnitForType(IType type, IProgressMonitor monitor) {
		val outerTypeName = type.packageFragment.elementName + "." + type.elementName
		if (observedSystemPatterns.exists[outerTypeName.matches(it)]) {
			monitor.subTask("Parsing " + outerTypeName)
			val options = JavaCore.getOptions()
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options)
	 		
			val ASTParser parser = ASTParser.newParser(AST.JLS8)
			parser.setProject(project)	
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
	 * Read full qualified class name patterns form an IFile.
	 * 
	 * @param file the file to read.
	 */
	private def readPattern(IFile file) {
		val List<String> patterns = new ArrayList<String>()
		val reader = new BufferedReader(new InputStreamReader(file.contents))
		var String line
		while ((line = reader.readLine()) != null) {
			patterns.add(line.replaceAll("\\.","\\."))
		}
		
		return patterns
	}
	
	/** check for types in the project hierarchy */
	
	/**
	 * in fragment roots 
	 */
	private def void checkForTypes(List<IType> types, IPackageFragmentRoot root, IProgressMonitor monitor) {
		monitor.subTask(root.elementName)
		root.children.forEach[ element |
			if (element instanceof IPackageFragment) {
				types.checkForTypes(element as IPackageFragment)
			}
		]
	}
	
	/**
	 * in fragments
	 */
	private def void checkForTypes(List<IType> types, IPackageFragment fragment) {
		fragment.children.forEach[ element |
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
		unit.allTypes.forEach[type |
			if (type instanceof IType) {
				if (!(type as IType).binary) types.add(type)
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
	
}