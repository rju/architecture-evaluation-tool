package de.cau.cs.se.evaluation.architecture.jobs;

import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph;
import de.cau.cs.se.evaluation.architecture.transformation.java.GlobalJavaScope;
import de.cau.cs.se.evaluation.architecture.transformation.java.TransformationJavaClassesToHypergraph;
import de.cau.cs.se.evaluation.architecture.transformation.java.TransformationJavaMethodsToModularHypergraph;
import de.cau.cs.se.evaluation.architecture.transformation.metrics.ResultModelProvider;
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphMetrics;
import de.cau.cs.se.evaluation.architecture.views.AnalysisResultView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class ComplexityAnalysisJob extends Job {
  private ArrayList<IType> types = new ArrayList<IType>();
  
  /**
   * The constructor scans the selection for files.
   * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
   */
  public ComplexityAnalysisJob(final ISelection selection) {
    super("Analysis Complexity");
    if ((selection instanceof IStructuredSelection)) {
      System.out.println("Got a structured selection");
      if ((selection instanceof ITreeSelection)) {
        final TreeSelection treeSelection = ((TreeSelection) selection);
        Iterator _iterator = treeSelection.iterator();
        final Procedure1<Object> _function = new Procedure1<Object>() {
          public void apply(final Object element) {
            ComplexityAnalysisJob.this.scanForClasses(element);
          }
        };
        IteratorExtensions.<Object>forEach(_iterator, _function);
      }
    }
  }
  
  protected IStatus run(final IProgressMonitor monitor) {
    final ArrayList<IJavaProject> projects = new ArrayList<IJavaProject>();
    final Procedure1<IType> _function = new Procedure1<IType>() {
      public void apply(final IType type) {
        IJavaProject _javaProject = type.getJavaProject();
        boolean _contains = projects.contains(_javaProject);
        boolean _not = (!_contains);
        if (_not) {
          IJavaProject _javaProject_1 = type.getJavaProject();
          projects.add(_javaProject_1);
        }
      }
    };
    IterableExtensions.<IType>forEach(this.types, _function);
    int _size = this.types.size();
    int _multiply = (_size * 2);
    int _plus = (1 + _multiply);
    int _plus_1 = (_plus + 
      3);
    int _size_1 = this.types.size();
    int _plus_2 = (_plus_1 + _size_1);
    monitor.beginTask("Determine complexity of inter class dependency", _plus_2);
    monitor.worked(1);
    GlobalJavaScope scopes = new GlobalJavaScope(projects, null);
    final TransformationJavaClassesToHypergraph javaToHypergraph = new TransformationJavaClassesToHypergraph(scopes, this.types, monitor);
    final TransformationJavaMethodsToModularHypergraph javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(scopes, this.types, monitor);
    final TransformationHypergraphMetrics hypergraphMetrics = new TransformationHypergraphMetrics(monitor);
    javaToModularHypergraph.transform();
    javaToHypergraph.transform();
    Hypergraph _system = javaToHypergraph.getSystem();
    hypergraphMetrics.setSystem(_system);
    final ResultModelProvider result = hypergraphMetrics.calculate();
    monitor.done();
    IWorkbench _workbench = PlatformUI.getWorkbench();
    Display _display = _workbench.getDisplay();
    _display.syncExec(new Runnable() {
      public void run() {
        try {
          IWorkbench _workbench = PlatformUI.getWorkbench();
          IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
          IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
          final IViewPart part = _activePage.showView(AnalysisResultView.ID);
          ((AnalysisResultView) part).update(result);
        } catch (final Throwable _t) {
          if (_t instanceof PartInitException) {
            final PartInitException e = (PartInitException)_t;
            e.printStackTrace();
          } else {
            throw Exceptions.sneakyThrow(_t);
          }
        }
      }
    });
    return Status.OK_STATUS;
  }
  
  private void _scanForClasses(final IProject object) {
    try {
      String _string = object.toString();
      String _plus = ("  IProject " + _string);
      System.out.println(_plus);
      boolean _hasNature = object.hasNature(JavaCore.NATURE_ID);
      if (_hasNature) {
        final IJavaProject project = JavaCore.create(object);
        IPackageFragmentRoot[] _allPackageFragmentRoots = project.getAllPackageFragmentRoots();
        final Procedure1<IPackageFragmentRoot> _function = new Procedure1<IPackageFragmentRoot>() {
          public void apply(final IPackageFragmentRoot root) {
            ComplexityAnalysisJob.this.checkForTypes(root);
          }
        };
        IterableExtensions.<IPackageFragmentRoot>forEach(((Iterable<IPackageFragmentRoot>)Conversions.doWrapArray(_allPackageFragmentRoots)), _function);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void _scanForClasses(final IJavaProject object) {
    try {
      String _elementName = object.getElementName();
      String _plus = ("  IJavaProject " + _elementName);
      System.out.println(_plus);
      IPackageFragmentRoot[] _allPackageFragmentRoots = object.getAllPackageFragmentRoots();
      final Procedure1<IPackageFragmentRoot> _function = new Procedure1<IPackageFragmentRoot>() {
        public void apply(final IPackageFragmentRoot root) {
          ComplexityAnalysisJob.this.checkForTypes(root);
        }
      };
      IterableExtensions.<IPackageFragmentRoot>forEach(((Iterable<IPackageFragmentRoot>)Conversions.doWrapArray(_allPackageFragmentRoots)), _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void _scanForClasses(final IPackageFragmentRoot object) {
    String _elementName = object.getElementName();
    String _plus = ("  IPackageFragmentRoot " + _elementName);
    System.out.println(_plus);
    this.checkForTypes(object);
  }
  
  private void _scanForClasses(final IPackageFragment object) {
    String _elementName = object.getElementName();
    String _plus = ("  IPackageFragment " + _elementName);
    System.out.println(_plus);
    this.checkForTypes(object);
  }
  
  private void _scanForClasses(final ICompilationUnit unit) {
    String _elementName = unit.getElementName();
    String _plus = ("  ICompilationUnit " + _elementName);
    System.out.println(_plus);
    this.checkForTypes(unit);
  }
  
  private void _scanForClasses(final Object object) {
    Class<?> _class = object.getClass();
    String _canonicalName = _class.getCanonicalName();
    String _plus = ("  Selection=" + _canonicalName);
    String _plus_1 = (_plus + " ");
    String _string = object.toString();
    String _plus_2 = (_plus_1 + _string);
    System.out.println(_plus_2);
  }
  
  /**
   * in fragment roots
   */
  private void checkForTypes(final IPackageFragmentRoot root) {
    try {
      IJavaElement[] _children = root.getChildren();
      final Procedure1<IJavaElement> _function = new Procedure1<IJavaElement>() {
        public void apply(final IJavaElement element) {
          if ((element instanceof IPackageFragment)) {
            ComplexityAnalysisJob.this.checkForTypes(((IPackageFragment) element));
          }
        }
      };
      IterableExtensions.<IJavaElement>forEach(((Iterable<IJavaElement>)Conversions.doWrapArray(_children)), _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * in fragments
   */
  private void checkForTypes(final IPackageFragment fragment) {
    try {
      IJavaElement[] _children = fragment.getChildren();
      final Procedure1<IJavaElement> _function = new Procedure1<IJavaElement>() {
        public void apply(final IJavaElement element) {
          if ((element instanceof IPackageFragment)) {
            ComplexityAnalysisJob.this.checkForTypes(((IPackageFragment) element));
          } else {
            if ((element instanceof ICompilationUnit)) {
              ComplexityAnalysisJob.this.checkForTypes(((ICompilationUnit) element));
            }
          }
        }
      };
      IterableExtensions.<IJavaElement>forEach(((Iterable<IJavaElement>)Conversions.doWrapArray(_children)), _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * in compilation units
   */
  private void checkForTypes(final ICompilationUnit unit) {
    try {
      IType[] _allTypes = unit.getAllTypes();
      final Procedure1<IType> _function = new Procedure1<IType>() {
        public void apply(final IType type) {
          try {
            if ((type instanceof IType)) {
              int _flags = type.getFlags();
              boolean _isAbstract = Flags.isAbstract(_flags);
              boolean _not = (!_isAbstract);
              if (_not) {
                ComplexityAnalysisJob.this.types.add(type);
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      IterableExtensions.<IType>forEach(((Iterable<IType>)Conversions.doWrapArray(_allTypes)), _function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void scanForClasses(final Object object) {
    if (object instanceof IProject) {
      _scanForClasses((IProject)object);
      return;
    } else if (object instanceof ICompilationUnit) {
      _scanForClasses((ICompilationUnit)object);
      return;
    } else if (object instanceof IJavaProject) {
      _scanForClasses((IJavaProject)object);
      return;
    } else if (object instanceof IPackageFragment) {
      _scanForClasses((IPackageFragment)object);
      return;
    } else if (object instanceof IPackageFragmentRoot) {
      _scanForClasses((IPackageFragmentRoot)object);
      return;
    } else if (object != null) {
      _scanForClasses(object);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(object).toString());
    }
  }
}
