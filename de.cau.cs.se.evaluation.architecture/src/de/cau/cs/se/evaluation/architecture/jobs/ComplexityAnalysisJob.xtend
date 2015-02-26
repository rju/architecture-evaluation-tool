package de.cau.cs.se.evaluation.architecture.jobs

import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ITreeSelection
import org.eclipse.jface.viewers.TreeSelection
import org.eclipse.core.resources.IProject
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IType
import java.util.ArrayList
import org.eclipse.jdt.core.IJavaProject
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphMetrics
import de.cau.cs.se.evaluation.architecture.transformation.java.GlobalJavaScope
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.Flags
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.PartInitException
import de.cau.cs.se.evaluation.architecture.views.AnalysisResultView
import de.cau.cs.se.evaluation.architecture.transformation.java.TransformationJavaClassesToHypergraph
import de.cau.cs.se.evaluation.architecture.transformation.java.TransformationJavaMethodsToModularHypergraph

class ComplexityAnalysisJob extends Job {
	
	var types = new ArrayList<IType>()
	
	/**
	 * The constructor scans the selection for files.
	 * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
	 */
	public new (ISelection selection) {
		super("Analysis Complexity")
		if (selection instanceof IStructuredSelection) {
			System.out.println("Got a structured selection");
			if (selection instanceof ITreeSelection) {
				val TreeSelection treeSelection = selection as TreeSelection
				
				treeSelection.iterator.forEach[element | element.scanForClasses]
			}
		}
	}
	
	protected override IStatus run(IProgressMonitor monitor) {
		// set total number of work units
			
		/** construct analysis. */
		val projects = new ArrayList<IJavaProject>()
		types.forEach[type | if (!projects.contains(type.javaProject)) projects.add(type.javaProject)]
		monitor.beginTask("Determine complexity of inter class dependency", 
			1 + // initialization 
			types.size*2 + // java to graph
			3 + types.size // graph analysis
		)
		monitor.worked(1)
				
		var scopes = new GlobalJavaScope(projects, null)
		
		val javaToHypergraph = new TransformationJavaClassesToHypergraph(scopes, types, monitor)
		val javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(projects.get(0), scopes, types, monitor)
		val hypergraphMetrics = new TransformationHypergraphMetrics( monitor)
		
		javaToModularHypergraph.transform()
		
		for (module : javaToModularHypergraph.modularSystem.modules) {
			System.out.println("module " + module.name)
			for (node : module.nodes) {
				System.out.println("  node " + node.name)
			}
		}
		for (node : javaToModularHypergraph.modularSystem.nodes) {
			System.out.println("node " + node.name)
			for (edge : node.edges) {
				System.out.println("  edge " + edge.name)
			}
		}
		
		javaToHypergraph.transform()		
		hypergraphMetrics.system = javaToHypergraph.system
		// val result = hypergraphMetrics.calculate()
		
		monitor.done()
		
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
       		public override void run() {
	           try { 
					val part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AnalysisResultView.ID)
					// (part as AnalysisResultView).update(result)
	           } catch (PartInitException e) {
	                e.printStackTrace()
	           }
	    	}
     	})
							
		return Status.OK_STATUS
	}
	
	
	private def dispatch scanForClasses(IProject object) {
		System.out.println("  IProject " + object.toString)
		if (object.hasNature(JavaCore.NATURE_ID)) {
			val IJavaProject project = JavaCore.create(object);
			project.allPackageFragmentRoots.forEach[root | root.checkForTypes]
		}
	}
	
	private def dispatch scanForClasses(IJavaProject object) {
		System.out.println("  IJavaProject " + object.elementName)
		object.allPackageFragmentRoots.forEach[root | root.checkForTypes]
	}
		
	private def dispatch scanForClasses(IPackageFragmentRoot object) {
		System.out.println("  IPackageFragmentRoot " + object.elementName)
		object.checkForTypes
	}
	
	private def dispatch scanForClasses(IPackageFragment object) {
		System.out.println("  IPackageFragment " + object.elementName)
		object.checkForTypes
	}
	
	private def dispatch scanForClasses(ICompilationUnit unit) {
		System.out.println("  ICompilationUnit " + unit.elementName)
		unit.checkForTypes()
	}
	
	private def dispatch scanForClasses(Object object) {
		System.out.println("  Selection=" + object.class.canonicalName + " " + object.toString)
	}
	
	/** check for types in the project hierarchy */
	
	/**
	 * in fragment roots 
	 */
	private def void checkForTypes(IPackageFragmentRoot root) {
		root.children.forEach[ element |
			if (element instanceof IPackageFragment) {
				(element as IPackageFragment).checkForTypes
			}
		]
	}
	
	/**
	 * in fragments
	 */
	private def void checkForTypes(IPackageFragment fragment) {
		fragment.children.forEach[ element |
			if (element instanceof IPackageFragment) {
				(element as IPackageFragment).checkForTypes
			} else if (element instanceof ICompilationUnit) {
				(element as ICompilationUnit).checkForTypes
			}
		]
	}
	
	/**
	 * in compilation units
	 */
	private def void checkForTypes(ICompilationUnit unit) {
		unit.allTypes.forEach[type | if (type instanceof IType) if (!Flags.isAbstract(type.flags)) types.add(type)]
	}
	
	
}