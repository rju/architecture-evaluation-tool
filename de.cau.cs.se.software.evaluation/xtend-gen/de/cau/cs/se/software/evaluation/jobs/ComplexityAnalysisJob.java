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
package de.cau.cs.se.software.evaluation.jobs;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.CalculateComplexity;
import de.cau.cs.se.software.evaluation.transformation.java.TransformationJavaMethodsToModularHypergraph;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationIntermoduleHyperedgesOnlyGraph;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationMaximalInterconnectedGraph;
import de.cau.cs.se.software.evaluation.views.AnalysisResultView;
import de.cau.cs.se.software.evaluation.views.NamedValue;
import de.cau.cs.se.software.evaluation.views.ResultModelProvider;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class ComplexityAnalysisJob extends Job {
  private final List<AbstractTypeDeclaration> classes;
  
  private final List<String> dataTypePatterns;
  
  private final List<String> observedSystemPatterns;
  
  private final IJavaProject project;
  
  /**
   * The constructor scans the selection for files.
   * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
   */
  public ComplexityAnalysisJob(final IJavaProject project, final List<AbstractTypeDeclaration> classes, final List<String> dataTypePatterns, final List<String> observedSystemPatterns) {
    super("Analysis Complexity");
    this.project = project;
    this.classes = classes;
    this.dataTypePatterns = dataTypePatterns;
    this.observedSystemPatterns = observedSystemPatterns;
  }
  
  protected IStatus run(final IProgressMonitor monitor) {
    final ResultModelProvider result = ResultModelProvider.INSTANCE;
    List<NamedValue> _values = result.getValues();
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    int _size = this.classes.size();
    NamedValue _namedValue = new NamedValue(_name, "size of observed system", _size);
    _values.add(_namedValue);
    this.updateView(null);
    final TransformationJavaMethodsToModularHypergraph javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(this.project, this.dataTypePatterns, this.observedSystemPatterns, monitor);
    javaToModularHypergraph.setInput(this.classes);
    javaToModularHypergraph.transform();
    List<NamedValue> _values_1 = result.getValues();
    IProject _project_1 = this.project.getProject();
    String _name_1 = _project_1.getName();
    ModularHypergraph _result = javaToModularHypergraph.getResult();
    EList<Module> _modules = _result.getModules();
    int _size_1 = _modules.size();
    NamedValue _namedValue_1 = new NamedValue(_name_1, "number of modules", _size_1);
    _values_1.add(_namedValue_1);
    List<NamedValue> _values_2 = result.getValues();
    IProject _project_2 = this.project.getProject();
    String _name_2 = _project_2.getName();
    ModularHypergraph _result_1 = javaToModularHypergraph.getResult();
    EList<Node> _nodes = _result_1.getNodes();
    int _size_2 = _nodes.size();
    NamedValue _namedValue_2 = new NamedValue(_name_2, "number of nodes", _size_2);
    _values_2.add(_namedValue_2);
    List<NamedValue> _values_3 = result.getValues();
    IProject _project_3 = this.project.getProject();
    String _name_3 = _project_3.getName();
    ModularHypergraph _result_2 = javaToModularHypergraph.getResult();
    EList<Edge> _edges = _result_2.getEdges();
    int _size_3 = _edges.size();
    NamedValue _namedValue_3 = new NamedValue(_name_3, "number of edges", _size_3);
    _values_3.add(_namedValue_3);
    this.updateView(javaToModularHypergraph);
    final TransformationMaximalInterconnectedGraph maximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(monitor);
    ModularHypergraph _result_3 = javaToModularHypergraph.getResult();
    maximalInterconnectedGraph.setInput(_result_3);
    maximalInterconnectedGraph.transform();
    final TransformationIntermoduleHyperedgesOnlyGraph intermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(monitor);
    ModularHypergraph _result_4 = javaToModularHypergraph.getResult();
    intermoduleHyperedgesOnlyGraph.setInput(_result_4);
    intermoduleHyperedgesOnlyGraph.transform();
    final TransformationHypergraphSize hypergraphSize = new TransformationHypergraphSize(monitor);
    hypergraphSize.setName("Calculate system size");
    ModularHypergraph _result_5 = javaToModularHypergraph.getResult();
    hypergraphSize.setInput(_result_5);
    hypergraphSize.transform();
    List<NamedValue> _values_4 = result.getValues();
    IProject _project_4 = this.project.getProject();
    String _name_4 = _project_4.getName();
    Double _result_6 = hypergraphSize.getResult();
    NamedValue _namedValue_4 = new NamedValue(_name_4, "hypergraph size", (_result_6).doubleValue());
    _values_4.add(_namedValue_4);
    this.updateView(javaToModularHypergraph);
    final CalculateComplexity calculateComplexity = new CalculateComplexity(monitor);
    ModularHypergraph _result_7 = javaToModularHypergraph.getResult();
    final double complexity = calculateComplexity.calculate(_result_7, "Calculate system\'s hypergraph complexity");
    List<NamedValue> _values_5 = result.getValues();
    IProject _project_5 = this.project.getProject();
    String _name_5 = _project_5.getName();
    NamedValue _namedValue_5 = new NamedValue(_name_5, "hypergraph complexity", complexity);
    _values_5.add(_namedValue_5);
    this.updateView(javaToModularHypergraph);
    ModularHypergraph _result_8 = maximalInterconnectedGraph.getResult();
    final double complexityMaximalInterconnected = calculateComplexity.calculate(_result_8, 
      "Calculate maximal interconnected graph complexity");
    ModularHypergraph _result_9 = intermoduleHyperedgesOnlyGraph.getResult();
    final double complexityIntermodule = calculateComplexity.calculate(_result_9, "Calculate intermodule complexity");
    List<NamedValue> _values_6 = result.getValues();
    IProject _project_6 = this.project.getProject();
    String _name_6 = _project_6.getName();
    NamedValue _namedValue_6 = new NamedValue(_name_6, "inter module cohesion", (complexityIntermodule / complexityMaximalInterconnected));
    _values_6.add(_namedValue_6);
    List<NamedValue> _values_7 = result.getValues();
    IProject _project_7 = this.project.getProject();
    String _name_7 = _project_7.getName();
    NamedValue _namedValue_7 = new NamedValue(_name_7, "inter module coupling", complexityIntermodule);
    _values_7.add(_namedValue_7);
    monitor.done();
    this.updateView(javaToModularHypergraph);
    return Status.OK_STATUS;
  }
  
  /**
   * Update the analysis view after updating its content.
   */
  private void updateView(final TransformationJavaMethodsToModularHypergraph javaToModularHypergraph) {
    IWorkbench _workbench = PlatformUI.getWorkbench();
    Display _display = _workbench.getDisplay();
    _display.syncExec(new Runnable() {
      public void run() {
        try {
          IWorkbench _workbench = PlatformUI.getWorkbench();
          IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
          IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
          final IViewPart part = _activePage.showView(AnalysisResultView.ID);
          boolean _notEquals = (!Objects.equal(javaToModularHypergraph, null));
          if (_notEquals) {
            ModularHypergraph _result = javaToModularHypergraph.getResult();
            ((AnalysisResultView) part).setGraph(_result);
          }
          ((AnalysisResultView) part).setProject(ComplexityAnalysisJob.this.project);
          ((AnalysisResultView) part).update();
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
