/**
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
 */
package de.cau.cs.se.evaluation.architecture.jobs;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.transformation.NameResolutionHelper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;

@SuppressWarnings("all")
public class CollectInputModelJob extends Job {
  private ISelection selection;
  
  private Shell shell;
  
  private List<String> dataTypePatterns;
  
  private List<String> observedSystemPatterns;
  
  private final List<AbstractTypeDeclaration> classes = new ArrayList<AbstractTypeDeclaration>();
  
  public CollectInputModelJob(final ISelection selection, final Shell shell) {
    super("Collect input model");
    this.selection = selection;
    this.shell = shell;
  }
  
  private IJavaProject project;
  
  /**
   * Execute the preparation
   */
  protected IStatus run(final IProgressMonitor monitor) {
    monitor.beginTask("Collect project information", 0);
    final List<IType> types = this.determineClassFiles(monitor);
    boolean _notEquals = (!Objects.equal(types, null));
    if (_notEquals) {
      final ArrayList<IJavaProject> projects = new ArrayList<IJavaProject>();
      final Consumer<IType> _function = new Consumer<IType>() {
        public void accept(final IType type) {
          IJavaProject _javaProject = type.getJavaProject();
          boolean _contains = projects.contains(_javaProject);
          boolean _not = (!_contains);
          if (_not) {
            IJavaProject _javaProject_1 = type.getJavaProject();
            projects.add(_javaProject_1);
          }
        }
      };
      types.forEach(_function);
      IJavaProject _get = projects.get(0);
      this.project = _get;
      this.collectAllSourceClasses(types, this.dataTypePatterns, this.observedSystemPatterns, monitor);
      return Status.OK_STATUS;
    } else {
      return Status.CANCEL_STATUS;
    }
  }
  
  public List<AbstractTypeDeclaration> getClasses() {
    return this.classes;
  }
  
  public IJavaProject getProject() {
    return this.project;
  }
  
  public List<String> getDataTypePatterns() {
    return this.dataTypePatterns;
  }
  
  public List<String> getObservedSystemPatterns() {
    return this.observedSystemPatterns;
  }
  
  /**
   * Determine class files.
   */
  private List<IType> determineClassFiles(final IProgressMonitor monitor) {
    if ((this.selection instanceof IStructuredSelection)) {
      if ((this.selection instanceof ITreeSelection)) {
        final TreeSelection treeSelection = ((TreeSelection) this.selection);
        Iterator _iterator = treeSelection.iterator();
        final Function1<Object, Boolean> _function = new Function1<Object, Boolean>() {
          public Boolean apply(final Object element) {
            boolean _or = false;
            if ((element instanceof IProject)) {
              _or = true;
            } else {
              _or = (element instanceof IJavaProject);
            }
            return Boolean.valueOf(_or);
          }
        };
        final Object selectedElement = IteratorExtensions.<Object>findFirst(_iterator, _function);
        boolean _notEquals = (!Objects.equal(selectedElement, null));
        if (_notEquals) {
          boolean _matched = false;
          if (!_matched) {
            if (selectedElement instanceof IProject) {
              _matched=true;
              IJavaProject _javaProject = this.getJavaProject(((IProject)selectedElement));
              List<IType> _scanForClasses = null;
              if (_javaProject!=null) {
                _scanForClasses=this.scanForClasses(_javaProject, monitor);
              }
              return _scanForClasses;
            }
          }
          if (!_matched) {
            if (selectedElement instanceof IJavaProject) {
              _matched=true;
              return this.scanForClasses(((IJavaProject)selectedElement), monitor);
            }
          }
        }
      }
    }
    return null;
  }
  
  private IJavaProject getJavaProject(final IProject project) {
    try {
      boolean _hasNature = project.hasNature(JavaCore.NATURE_ID);
      if (_hasNature) {
        return JavaCore.create(project);
      } else {
        return null;
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Scann for classes in project.
   */
  private List<IType> scanForClasses(final IJavaProject project, final IProgressMonitor monitor) {
    try {
      final ArrayList<IType> types = new ArrayList<IType>();
      IProject _project = project.getProject();
      String _name = _project.getName();
      String _plus = ("Scanning project " + _name);
      monitor.subTask(_plus);
      IProject _project_1 = project.getProject();
      IResource _findMember = _project_1.findMember("data-type-pattern.cfg");
      final IFile dataTypePatternsFile = ((IFile) _findMember);
      boolean _notEquals = (!Objects.equal(dataTypePatternsFile, null));
      if (_notEquals) {
        boolean _isSynchronized = dataTypePatternsFile.isSynchronized(1);
        if (_isSynchronized) {
          List<String> _readPattern = this.readPattern(dataTypePatternsFile);
          this.dataTypePatterns = _readPattern;
          IProject _project_2 = project.getProject();
          IResource _findMember_1 = _project_2.findMember("observed-system.cfg");
          final IFile observedSystemPatternsFile = ((IFile) _findMember_1);
          boolean _notEquals_1 = (!Objects.equal(observedSystemPatternsFile, null));
          if (_notEquals_1) {
            boolean _isSynchronized_1 = observedSystemPatternsFile.isSynchronized(1);
            if (_isSynchronized_1) {
              List<String> _readPattern_1 = this.readPattern(observedSystemPatternsFile);
              this.observedSystemPatterns = _readPattern_1;
              IPackageFragmentRoot[] _allPackageFragmentRoots = project.getAllPackageFragmentRoots();
              final Consumer<IPackageFragmentRoot> _function = new Consumer<IPackageFragmentRoot>() {
                public void accept(final IPackageFragmentRoot root) {
                  CollectInputModelJob.this.checkForTypes(types, root, monitor);
                }
              };
              ((List<IPackageFragmentRoot>)Conversions.doWrapArray(_allPackageFragmentRoots)).forEach(_function);
              return types;
            } else {
              this.showErrorMessage("Configuration Error", "Observed system file (observed-system.cfg) listing patterns for classes of the observed system is missing.");
            }
          } else {
            this.showErrorMessage("Configuration Error", "Observed system file (observed-system.cfg) listing patterns for classes of the observed system is missing.");
          }
        } else {
          this.showErrorMessage("Configuration Error", "Data type pattern file (data-type-pattern.cfg) listing patterns for data type classes is missing.");
        }
      } else {
        this.showErrorMessage("Configuration Error", "Data type pattern file (data-type-pattern.cfg) listing patterns for data type classes is missing.");
      }
      return null;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Find all classes in the IType list which belong to the observed system.
   * 
   * @param classes the classes of the observed system
   * @param types the types found by scanning the project
   * @param dataTypePatterns pattern list for data types to be excluded
   * @param observedSystemPatterns pattern list for classes to be included
   */
  private void collectAllSourceClasses(final List<IType> types, final List<String> dataTypePatterns, final List<String> observedSystemPatterns, final IProgressMonitor monitor) {
    final Consumer<IType> _function = new Consumer<IType>() {
      public void accept(final IType jdtType) {
        final CompilationUnit unit = CollectInputModelJob.this.getUnitForType(jdtType, monitor);
        boolean _notEquals = (!Objects.equal(unit, null));
        if (_notEquals) {
          List _types = unit.types();
          final Consumer<Object> _function = new Consumer<Object>() {
            public void accept(final Object unitType) {
              if ((unitType instanceof TypeDeclaration)) {
                final TypeDeclaration type = ((TypeDeclaration) unitType);
                final ITypeBinding typeBinding = type.resolveBinding();
                final String name = NameResolutionHelper.determineFullyQualifiedName(typeBinding);
                final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
                  public Boolean apply(final String it) {
                    return Boolean.valueOf(name.matches(it));
                  }
                };
                boolean _exists = IterableExtensions.<String>exists(observedSystemPatterns, _function);
                if (_exists) {
                  boolean _isClassDataType = CollectInputModelJob.this.isClassDataType(typeBinding, dataTypePatterns);
                  boolean _not = (!_isClassDataType);
                  if (_not) {
                    CollectInputModelJob.this.classes.add(type);
                  }
                }
              }
            }
          };
          _types.forEach(_function);
        }
      }
    };
    types.forEach(_function);
  }
  
  /**
   * Determine if the given type is a data type.
   * 
   * @param type the type to be evaluated
   * @param dataTypes a list of data types
   * 
   * @return returns true if the given type is a data type and not a behavior type.
   */
  private boolean isClassDataType(final ITypeBinding typeBinding, final List<String> dataTypePatterns) {
    final String name = NameResolutionHelper.determineFullyQualifiedName(typeBinding);
    final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
      public Boolean apply(final String pattern) {
        return Boolean.valueOf(name.matches(pattern));
      }
    };
    return IterableExtensions.<String>exists(dataTypePatterns, _function);
  }
  
  /**
   * Get compilation unit for JDT type.
   */
  private CompilationUnit getUnitForType(final IType type, final IProgressMonitor monitor) {
    try {
      IPackageFragment _packageFragment = type.getPackageFragment();
      String _elementName = _packageFragment.getElementName();
      String _plus = (_elementName + ".");
      String _elementName_1 = type.getElementName();
      final String outerTypeName = (_plus + _elementName_1);
      final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
        public Boolean apply(final String it) {
          return Boolean.valueOf(outerTypeName.matches(it));
        }
      };
      boolean _exists = IterableExtensions.<String>exists(this.observedSystemPatterns, _function);
      if (_exists) {
        monitor.subTask(("Parsing " + outerTypeName));
        final Hashtable options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        final ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setProject(this.project);
        parser.setCompilerOptions(options);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        ICompilationUnit _compilationUnit = type.getCompilationUnit();
        IBuffer _buffer = _compilationUnit.getBuffer();
        String _contents = _buffer.getContents();
        char[] _charArray = _contents.toCharArray();
        parser.setSource(_charArray);
        ICompilationUnit _compilationUnit_1 = type.getCompilationUnit();
        String _elementName_2 = _compilationUnit_1.getElementName();
        parser.setUnitName(_elementName_2);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setStatementsRecovery(true);
        ASTNode _createAST = parser.createAST(null);
        return ((CompilationUnit) _createAST);
      } else {
        return null;
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Read full qualified class name patterns form an IFile.
   * 
   * @param file the file to read.
   */
  private List<String> readPattern(final IFile file) {
    try {
      final List<String> patterns = new ArrayList<String>();
      InputStream _contents = file.getContents();
      InputStreamReader _inputStreamReader = new InputStreamReader(_contents);
      final BufferedReader reader = new BufferedReader(_inputStreamReader);
      String line = null;
      while ((!Objects.equal((line = reader.readLine()), null))) {
        String _replaceAll = line.replaceAll("\\.", "\\.");
        patterns.add(_replaceAll);
      }
      return patterns;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * in fragment roots
   */
  private void checkForTypes(final List<IType> types, final IPackageFragmentRoot root, final IProgressMonitor monitor) {
    try {
      String _elementName = root.getElementName();
      monitor.subTask(_elementName);
      IJavaElement[] _children = root.getChildren();
      final Consumer<IJavaElement> _function = new Consumer<IJavaElement>() {
        public void accept(final IJavaElement element) {
          if ((element instanceof IPackageFragment)) {
            CollectInputModelJob.this.checkForTypes(types, ((IPackageFragment) element));
          }
        }
      };
      ((List<IJavaElement>)Conversions.doWrapArray(_children)).forEach(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * in fragments
   */
  private void checkForTypes(final List<IType> types, final IPackageFragment fragment) {
    try {
      IJavaElement[] _children = fragment.getChildren();
      final Consumer<IJavaElement> _function = new Consumer<IJavaElement>() {
        public void accept(final IJavaElement element) {
          if ((element instanceof IPackageFragment)) {
            CollectInputModelJob.this.checkForTypes(types, ((IPackageFragment) element));
          } else {
            if ((element instanceof ICompilationUnit)) {
              CollectInputModelJob.this.checkForTypes(types, ((ICompilationUnit) element));
            }
          }
        }
      };
      ((List<IJavaElement>)Conversions.doWrapArray(_children)).forEach(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * in compilation units
   */
  private void checkForTypes(final List<IType> types, final ICompilationUnit unit) {
    try {
      IType[] _allTypes = unit.getAllTypes();
      final Consumer<IType> _function = new Consumer<IType>() {
        public void accept(final IType type) {
          if ((type instanceof IType)) {
            boolean _isBinary = ((IType) type).isBinary();
            boolean _not = (!_isBinary);
            if (_not) {
              types.add(type);
            }
          }
        }
      };
      ((List<IType>)Conversions.doWrapArray(_allTypes)).forEach(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private void showErrorMessage(final String title, final String message) {
    IWorkbench _workbench = PlatformUI.getWorkbench();
    Display _display = _workbench.getDisplay();
    _display.syncExec(new Runnable() {
      public void run() {
        try {
          MessageDialog.openError(CollectInputModelJob.this.shell, title, message);
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
  }
}
