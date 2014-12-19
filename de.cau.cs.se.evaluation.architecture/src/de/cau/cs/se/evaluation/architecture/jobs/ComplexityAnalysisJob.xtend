package de.cau.cs.se.evaluation.architecture.jobs

import org.eclipse.core.runtime.jobs.Job
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ITreeSelection
import org.eclipse.jface.viewers.TreeSelection
import org.eclipse.jface.viewers.TreePath
import org.eclipse.core.runtime.IAdaptable
import org.eclipse.core.resources.IFile

class ComplexityAnalysisJob extends Job {
	
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
				val TreePath[] treePaths = treeSelection.getPaths()
				val TreePath treePath = treePaths.get(0)

				System.out.println("Last " + treePath.lastSegment);
				val Object lastSegmentObj = treePath.lastSegment
				var Class currClass = lastSegmentObj.getClass()
				while(currClass != null) {
					System.out.println("  Class=" + currClass.getName())
					val Class[] interfaces = currClass.getInterfaces()
					for(Class interfacey : interfaces) {
						System.out.println("   I=" + interfacey.getName())
					}
					currClass = currClass.getSuperclass()
				}
				if(lastSegmentObj instanceof IAdaptable) {
					val IFile file = (lastSegmentObj as IAdaptable).getAdapter(IFile) as IFile
					if(file != null) {
						System.out.println("File=" + file.getName())
						val String path = file.getRawLocation().toOSString()
						System.out.println("path: " + path)
					}
				}
			}
		}
	}
	
	
	protected override IStatus run(IProgressMonitor monitor) {
		// set total number of work units
		monitor.beginTask("Determine complexity of inter class dependency", 100)

		return Status.OK_STATUS
	}
	
}