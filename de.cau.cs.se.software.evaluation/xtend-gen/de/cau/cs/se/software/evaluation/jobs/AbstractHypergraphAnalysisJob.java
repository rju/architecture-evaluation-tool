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
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.jobs.CalculateComplexity;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationIntermoduleHyperedgesOnlyGraph;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationMaximalInterconnectedGraph;
import de.cau.cs.se.software.evaluation.views.AnalysisResultView;
import de.cau.cs.se.software.evaluation.views.NamedValue;
import de.cau.cs.se.software.evaluation.views.ResultModelProvider;
import java.util.List;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public abstract class AbstractHypergraphAnalysisJob extends Job {
  protected final IProject project;
  
  public AbstractHypergraphAnalysisJob(final IProject project) {
    super(("Analysis " + project.getName()));
    this.project = project;
  }
  
  /**
   * Calculate cohesion of a graph.
   */
  public double calculateCohesion(final ModularHypergraph inputHypergraph, final IProgressMonitor monitor, final ResultModelProvider result, final double coupling) {
    final TransformationMaximalInterconnectedGraph maximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(monitor);
    maximalInterconnectedGraph.setInput(inputHypergraph);
    maximalInterconnectedGraph.transform();
    final CalculateComplexity calculateComplexity = new CalculateComplexity(monitor);
    ModularHypergraph _result = maximalInterconnectedGraph.getResult();
    final double complexityMaximalInterconnected = calculateComplexity.calculate(_result, 
      "Calculate maximal interconnected graph complexity");
    final double cohesion = (coupling / complexityMaximalInterconnected);
    List<NamedValue> _values = result.getValues();
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    NamedValue _namedValue = new NamedValue(_name, "inter module cohesion", cohesion);
    _values.add(_namedValue);
    return cohesion;
  }
  
  /**
   * Calculation for MS^* -> MS^*#, MS^*#_i
   * Calculate coupling
   */
  public double calculateCoupling(final ModularHypergraph inputHypergraph, final IProgressMonitor monitor, final ResultModelProvider result) {
    final TransformationIntermoduleHyperedgesOnlyGraph intermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(monitor);
    intermoduleHyperedgesOnlyGraph.setInput(inputHypergraph);
    intermoduleHyperedgesOnlyGraph.transform();
    final CalculateComplexity calculateComplexity = new CalculateComplexity(monitor);
    ModularHypergraph _result = intermoduleHyperedgesOnlyGraph.getResult();
    final double complexityIntermodule = calculateComplexity.calculate(_result, "Calculate intermodule complexity");
    List<NamedValue> _values = result.getValues();
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    NamedValue _namedValue = new NamedValue(_name, "inter module coupling", complexityIntermodule);
    _values.add(_namedValue);
    this.updateView(inputHypergraph);
    return complexityIntermodule;
  }
  
  /**
   * Calculate graph complexity.
   */
  public double calculateComplexity(final ModularHypergraph inputHypergraph, final IProgressMonitor monitor, final ResultModelProvider result) {
    final CalculateComplexity calculateComplexity = new CalculateComplexity(monitor);
    final double complexity = calculateComplexity.calculate(inputHypergraph, "Calculate system\'s hypergraph complexity");
    List<NamedValue> _values = result.getValues();
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    NamedValue _namedValue = new NamedValue(_name, "hypergraph complexity", complexity);
    _values.add(_namedValue);
    this.updateView(inputHypergraph);
    return complexity;
  }
  
  /**
   * Calculating system size based on input modular hyper graph
   */
  public Double calculateSize(final ModularHypergraph inputHypergraph, final IProgressMonitor monitor, final ResultModelProvider result) {
    final TransformationHypergraphSize hypergraphSize = new TransformationHypergraphSize(monitor);
    hypergraphSize.setName("Calculate system size");
    hypergraphSize.setInput(inputHypergraph);
    hypergraphSize.transform();
    List<NamedValue> _values = result.getValues();
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    Double _result = hypergraphSize.getResult();
    NamedValue _namedValue = new NamedValue(_name, "hypergraph size", (_result).doubleValue());
    _values.add(_namedValue);
    this.updateView(inputHypergraph);
    return hypergraphSize.getResult();
  }
  
  /**
   * Update the analysis view after updating its content.
   */
  protected void updateView(final ModularHypergraph hypergraph) {
    IWorkbench _workbench = PlatformUI.getWorkbench();
    Display _display = _workbench.getDisplay();
    _display.syncExec(new Runnable() {
      @Override
      public void run() {
        try {
          IWorkbench _workbench = PlatformUI.getWorkbench();
          IWorkbenchWindow _activeWorkbenchWindow = _workbench.getActiveWorkbenchWindow();
          IWorkbenchPage _activePage = _activeWorkbenchWindow.getActivePage();
          final IViewPart part = _activePage.showView(AnalysisResultView.ID);
          boolean _notEquals = (!Objects.equal(hypergraph, null));
          if (_notEquals) {
            ((AnalysisResultView) part).setGraph(hypergraph);
          }
          ((AnalysisResultView) part).setProject(AbstractHypergraphAnalysisJob.this.project);
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
