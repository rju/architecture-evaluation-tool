package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("all")
public class PCMDeploymentAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final IFile file;
  
  private Shell shell;
  
  public PCMDeploymentAnalysisJob(final IProject project, final IFile file, final Shell shell) {
    super(project);
    this.file = file;
    this.shell = shell;
  }
  
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    throw new Error("Unresolved compilation problems:"
      + "\nCannot cast from EObject to System");
  }
}
