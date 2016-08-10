package de.cau.cs.se.software.evaluation.jobs;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.AbstractHypergraphAnalysisJob;
import de.cau.cs.se.software.evaluation.transformation.TransformationCyclomaticComplexity;
import de.cau.cs.se.software.evaluation.transformation.TransformationLinesOfCode;
import de.cau.cs.se.software.evaluation.transformation.java.NameResolutionHelper;
import de.cau.cs.se.software.evaluation.transformation.java.TransformationJavaMethodsToModularHypergraph;
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider;
import de.cau.cs.se.software.evaluation.views.LogModelProvider;
import de.cau.cs.se.software.evaluation.views.LogView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class JavaProjectAnalysisJob extends AbstractHypergraphAnalysisJob {
  private final IJavaProject javaProject;
  
  private final Shell shell;
  
  private String DATA_TYPE_PATTERN_FILE = "data-type-pattern.cfg";
  
  private String DATA_TYPE_PATTERN_TITLE = "data type pattern";
  
  private String OBSERVED_SYSTEM_PATTERN_FILE = "observed-system.cfg";
  
  private String OBSERVED_SYSTEM_TITLE = "observed system";
  
  public JavaProjectAnalysisJob(final IProject project, final Shell shell) {
    super(project);
    IJavaProject _javaProject = this.getJavaProject(project);
    this.javaProject = _javaProject;
    this.shell = shell;
  }
  
  /**
   * Execute all metrics as requested.
   */
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    try {
      monitor.beginTask("Collect project information", 0);
      IProject _project = this.project.getProject();
      final List<String> dataTypePatterns = this.getPatternFile(_project, this.DATA_TYPE_PATTERN_FILE, this.DATA_TYPE_PATTERN_TITLE);
      boolean _notEquals = (!Objects.equal(dataTypePatterns, null));
      if (_notEquals) {
        IProject _project_1 = this.project.getProject();
        final List<String> observedSystemPatterns = this.getPatternFile(_project_1, this.OBSERVED_SYSTEM_PATTERN_FILE, 
          this.OBSERVED_SYSTEM_TITLE);
        boolean _notEquals_1 = (!Objects.equal(observedSystemPatterns, null));
        if (_notEquals_1) {
          final ArrayList<IType> types = new ArrayList<IType>();
          IProject _project_2 = this.project.getProject();
          String _name = _project_2.getName();
          String _plus = ("Scanning project " + _name);
          monitor.subTask(_plus);
          IPackageFragmentRoot[] _allPackageFragmentRoots = this.javaProject.getAllPackageFragmentRoots();
          final Consumer<IPackageFragmentRoot> _function = (IPackageFragmentRoot root) -> {
            this.checkForTypes(types, root, monitor);
          };
          ((List<IPackageFragmentRoot>)Conversions.doWrapArray(_allPackageFragmentRoots)).forEach(_function);
          boolean _notEquals_2 = (!Objects.equal(types, null));
          if (_notEquals_2) {
            final AnalysisResultModelProvider result = AnalysisResultModelProvider.INSTANCE;
            String _name_1 = this.project.getName();
            LogModelProvider.INSTANCE.setProjectName(_name_1);
            final List<AbstractTypeDeclaration> classes = this.collectAllSourceClasses(types, dataTypePatterns, observedSystemPatterns, monitor);
            this.calculateCodeStatistics(classes, monitor, result);
            final ModularHypergraph inputHypergraph = this.createHypergraphForJavaProject(dataTypePatterns, observedSystemPatterns, classes, monitor, result);
            this.calculateSize(inputHypergraph, monitor, result);
            this.calculateComplexity(inputHypergraph, monitor, result);
            this.calculateCoupling(inputHypergraph, monitor, result);
            this.calculateCohesion(inputHypergraph, monitor, result);
            this.updateLogView();
            monitor.done();
            return Status.OK_STATUS;
          } else {
            this.showErrorMessage("Project Setup Error", "No classes found for analysis.");
          }
        }
      }
      this.updateLogView();
      return Status.CANCEL_STATUS;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  /**
   * Claculate code statistics.
   */
  private void calculateCodeStatistics(final List<AbstractTypeDeclaration> classes, final IProgressMonitor monitor, final AnalysisResultModelProvider result) {
    final TransformationLinesOfCode linesOfCodeMetric = new TransformationLinesOfCode(monitor);
    linesOfCodeMetric.generate(classes);
    final TransformationCyclomaticComplexity javaMethodComplexity = new TransformationCyclomaticComplexity(monitor);
    javaMethodComplexity.generate(classes);
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    int _size = classes.size();
    result.addResult(_name, "size of observed system", _size);
    IProject _project_1 = this.project.getProject();
    String _name_1 = _project_1.getName();
    Long _result = linesOfCodeMetric.getResult();
    result.addResult(_name_1, "lines of code (LOC)", (_result).longValue());
    for (int i = 1; (i < javaMethodComplexity.getResult().size()); i++) {
      IProject _project_2 = this.project.getProject();
      String _name_2 = _project_2.getName();
      List<Integer> _result_1 = javaMethodComplexity.getResult();
      Integer _get = _result_1.get(i);
      result.addResult(_name_2, ("cyclomatic complexity bucket " + Integer.valueOf(i)), (_get).intValue());
    }
    this.updateView(null);
  }
  
  /**
   * Construct hypergraph from java project
   */
  private ModularHypergraph createHypergraphForJavaProject(final List<String> dataTypePatterns, final List<String> observedSystemPatterns, final List<AbstractTypeDeclaration> classes, final IProgressMonitor monitor, final AnalysisResultModelProvider result) {
    final TransformationJavaMethodsToModularHypergraph javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(this.javaProject, dataTypePatterns, observedSystemPatterns, monitor);
    javaToModularHypergraph.generate(classes);
    String _name = this.project.getName();
    ModularHypergraph _result = javaToModularHypergraph.getResult();
    EList<Module> _modules = _result.getModules();
    int _size = _modules.size();
    result.addResult(_name, "number of modules", _size);
    String _name_1 = this.project.getName();
    ModularHypergraph _result_1 = javaToModularHypergraph.getResult();
    EList<Node> _nodes = _result_1.getNodes();
    int _size_1 = _nodes.size();
    result.addResult(_name_1, "number of nodes", _size_1);
    String _name_2 = this.project.getName();
    ModularHypergraph _result_2 = javaToModularHypergraph.getResult();
    EList<Edge> _edges = _result_2.getEdges();
    int _size_2 = _edges.size();
    result.addResult(_name_2, "number of edges", _size_2);
    ModularHypergraph _result_3 = javaToModularHypergraph.getResult();
    this.updateView(_result_3);
    this.updateLogView();
    return javaToModularHypergraph.getResult();
  }
  
  /**
   * Get Java project of a project.
   */
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
   * Find all classes in the IType list which belong to the observed system.
   * 
   * @param classes the classes of the observed system
   * @param types the types found by scanning the project
   * @param dataTypePatterns pattern list for data types to be excluded
   * @param observedSystemPatterns pattern list for classes to be included
   */
  private List<AbstractTypeDeclaration> collectAllSourceClasses(final List<IType> types, final List<String> dataTypePatterns, final List<String> observedSystemPatterns, final IProgressMonitor monitor) {
    final List<AbstractTypeDeclaration> classes = new ArrayList<AbstractTypeDeclaration>();
    final Consumer<IType> _function = (IType jdtType) -> {
      final CompilationUnit unit = this.getUnitForType(jdtType, observedSystemPatterns, monitor);
      boolean _notEquals = (!Objects.equal(unit, null));
      if (_notEquals) {
        List _types = unit.types();
        final Consumer<Object> _function_1 = (Object unitType) -> {
          if ((unitType instanceof TypeDeclaration)) {
            final TypeDeclaration type = ((TypeDeclaration) unitType);
            final ITypeBinding typeBinding = type.resolveBinding();
            final String name = NameResolutionHelper.determineFullyQualifiedName(typeBinding);
            final Function1<String, Boolean> _function_2 = (String it) -> {
              return Boolean.valueOf(name.matches(it));
            };
            boolean _exists = IterableExtensions.<String>exists(observedSystemPatterns, _function_2);
            if (_exists) {
              boolean _isClassDataType = this.isClassDataType(typeBinding, dataTypePatterns);
              boolean _not = (!_isClassDataType);
              if (_not) {
                classes.add(type);
              }
            }
          }
        };
        _types.forEach(_function_1);
      }
    };
    types.forEach(_function);
    return classes;
  }
  
  /**
   * Get compilation unit for JDT type.
   */
  private CompilationUnit getUnitForType(final IType type, final List<String> observedSystemPatterns, final IProgressMonitor monitor) {
    try {
      IPackageFragment _packageFragment = type.getPackageFragment();
      String _elementName = _packageFragment.getElementName();
      String _plus = (_elementName + ".");
      String _elementName_1 = type.getElementName();
      final String outerTypeName = (_plus + _elementName_1);
      final Function1<String, Boolean> _function = (String it) -> {
        return Boolean.valueOf(outerTypeName.matches(it));
      };
      boolean _exists = IterableExtensions.<String>exists(observedSystemPatterns, _function);
      if (_exists) {
        monitor.subTask(("Parsing " + outerTypeName));
        final Hashtable<String, String> options = JavaCore.getOptions();
        JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
        final ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setProject(this.javaProject);
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
   * Determine if the given type is a data type.
   * 
   * @param type the type to be evaluated
   * @param dataTypes a list of data types
   * 
   * @return returns true if the given type is a data type and not a behavior type.
   */
  private boolean isClassDataType(final ITypeBinding typeBinding, final List<String> dataTypePatterns) {
    final String name = NameResolutionHelper.determineFullyQualifiedName(typeBinding);
    final Function1<String, Boolean> _function = (String pattern) -> {
      return Boolean.valueOf(name.matches(pattern));
    };
    return IterableExtensions.<String>exists(dataTypePatterns, _function);
  }
  
  /**
   * Get data type patterns.
   * 
   * @param project the project containing the data-type-pattern.cfg
   */
  private List<String> getPatternFile(final IProject project, final String filename, final String title) {
    IResource _findMember = project.findMember(filename);
    final IFile patternFile = ((IFile) _findMember);
    boolean _notEquals = (!Objects.equal(patternFile, null));
    if (_notEquals) {
      boolean _isSynchronized = patternFile.isSynchronized(1);
      if (_isSynchronized) {
        return this.readPattern(patternFile);
      }
    }
    this.showErrorMessage(
      "Configuration Error", 
      (((("The " + title) + " file (") + filename) + ") containing class name patterns is missing."));
    return null;
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
      final Consumer<IJavaElement> _function = (IJavaElement element) -> {
        if ((element instanceof IPackageFragment)) {
          this.checkForTypes(types, ((IPackageFragment) element));
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
      final Consumer<IJavaElement> _function = (IJavaElement element) -> {
        if ((element instanceof IPackageFragment)) {
          this.checkForTypes(types, ((IPackageFragment) element));
        } else {
          if ((element instanceof ICompilationUnit)) {
            this.checkForTypes(types, ((ICompilationUnit) element));
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
      final Consumer<IType> _function = (IType type) -> {
        if ((type instanceof IType)) {
          boolean _isBinary = ((IType) type).isBinary();
          boolean _not = (!_isBinary);
          if (_not) {
            types.add(type);
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
      @Override
      public void run() {
        try {
          MessageDialog.openError(JavaProjectAnalysisJob.this.shell, title, message);
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
  
  private void updateLogView() {
    IWorkbench _workbench = PlatformUI.getWorkbench();
    Display _display = _workbench.getDisplay();
    _display.syncExec(new Runnable() {
      @Override
      public void run() {
        try {
          IWorkbench _workbench = PlatformUI.getWorkbench();
          IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
          IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
          final IViewPart part2 = _activePage.showView(LogView.ID);
          ((LogView) part2).update();
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
