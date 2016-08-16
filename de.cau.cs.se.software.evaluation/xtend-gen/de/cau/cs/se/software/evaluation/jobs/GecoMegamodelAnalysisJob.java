package de.cau.cs.se.software.evaluation.jobs;

import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Shell;

/**
 * run job for the GECO megamodel evaluation.
 */
@SuppressWarnings("all")
public class GecoMegamodelAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final IFile file;
  
  /**
   * resource set for the compilation.
   */
  /* @Inject
   */private /* XtextResourceSet */Object resourceSet;
  
  private Shell shell;
  
  public GecoMegamodelAnalysisJob(final IProject project, final IFile file, final Shell shell) {
    super(project);
    this.file = file;
    this.shell = shell;
  }
  
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method createInjectorAndDoEMFRegistration() is undefined for the type ArchitectureStandaloneSetup"
      + "\nThe method or field XtextResource is undefined"
      + "\nThe field GecoMegamodelAnalysisJob.resourceSet refers to the missing type XtextResourceSet"
      + "\nThe field GecoMegamodelAnalysisJob.resourceSet refers to the missing type XtextResourceSet"
      + "\ninjectMembers cannot be resolved"
      + "\naddLoadOption cannot be resolved"
      + "\nOPTION_RESOLVE_ALL cannot be resolved"
      + "\ngetResource cannot be resolved");
  }
}
