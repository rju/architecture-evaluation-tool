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
import de.cau.cs.se.software.evaluation.hypergraph.Hypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.jobs.CalculateComplexity;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphToGraphMapping;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationIntermoduleHyperedgesOnlyGraph;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationIntraModuleGraph;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationMaximalInterconnectedGraph;
import de.cau.cs.se.software.evaluation.views.AnalysisResultModelProvider;
import de.cau.cs.se.software.evaluation.views.AnalysisResultView;
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

/**
 * Abstract class implementing the basic metrics of the Edward B. Allen entropy
 * based metrics based on a modular hypergraph formalism
 * 
 * @author Reiner Jung
 */
@SuppressWarnings("all")
public abstract class AbstractHypergraphAnalysisJob extends Job {
  protected final IProject project;
  
  public AbstractHypergraphAnalysisJob(final IProject project) {
    super(("Analysis " + project.getName()));
    this.project = project;
  }
  
  /**
   * Calculating system size based on input hypergraph.
   * 
   * @param inputHypergraph a hypergraph which will be interpreted as a hypergraph without modules.
   * @param monitor Eclipse progress monitor
   * @param result handler for the result model
   * 
   * @return the calculated information size of the hypergraph
   */
  protected Double calculateSize(final Hypergraph inputHypergraph, final IProgressMonitor monitor, final AnalysisResultModelProvider result) {
    final TransformationHypergraphSize hypergraphSize = new TransformationHypergraphSize(monitor);
    hypergraphSize.setName("Calculate system size");
    hypergraphSize.generate(inputHypergraph);
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    Double _result = hypergraphSize.getResult();
    result.addResult(_name, "hypergraph size", _result);
    this.updateView(inputHypergraph);
    return hypergraphSize.getResult();
  }
  
  /**
   * Calculate graph complexity.
   * 
   * @param inputHypergraph a hypergraph which will be interpreted as a hypergraph without modules.
   * @param monitor Eclipse progress monitor
   * @param result handler for the result model
   * 
   * @return the calculated complexity of the hypergraph
   */
  protected double calculateComplexity(final Hypergraph inputHypergraph, final IProgressMonitor monitor, final AnalysisResultModelProvider result) {
    final CalculateComplexity calculateComplexity = new CalculateComplexity(monitor);
    final double complexity = calculateComplexity.calculate(inputHypergraph, "Calculate system\'s hypergraph complexity");
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    result.addResult(_name, "hypergraph complexity", Double.valueOf(complexity));
    this.updateView(inputHypergraph);
    return complexity;
  }
  
  /**
   * Calculate coupling of a modular hypergraph with only inter module hyperedges.
   * Calculation for MS^* -> MS^*#, MS^*#_i
   * 
   * @param inputHypergraph a modular hypergraph
   * @param monitor Eclipse progress monitor
   * @param result handler for the result model
   * 
   * @return the coupling of the modular inter-module hyperedges only hypergraph
   */
  protected void calculateCoupling(final ModularHypergraph inputHypergraph, final IProgressMonitor monitor, final AnalysisResultModelProvider result) {
    final TransformationIntermoduleHyperedgesOnlyGraph intermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(monitor);
    intermoduleHyperedgesOnlyGraph.generate(inputHypergraph);
    final CalculateComplexity calculateComplexity = new CalculateComplexity(monitor);
    ModularHypergraph _result = intermoduleHyperedgesOnlyGraph.getResult();
    final double complexityIntermodule = calculateComplexity.calculate(_result, "Calculate intermodule complexity");
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    result.addResult(_name, "inter module coupling", Double.valueOf(complexityIntermodule));
    this.updateView(inputHypergraph);
  }
  
  /**
   * Calculate cohesion of a modular graph only containing inter module edges.
   * For this calculation the hypergraph must also be a graph.
   * 
   * @param inputHypergraph a modular hypergraph
   * @param monitor Eclipse progress monitor
   * @param result handler for the result model
   * @param coupling the coupling values for the inter module only edges modular hypergraph
   * 
   * @return the cohesion of the modular inter-module hyperedges only hypergraph
   */
  protected double calculateCohesion(final ModularHypergraph inputHypergraph, final IProgressMonitor monitor, final AnalysisResultModelProvider result) {
    final TransformationHypergraphToGraphMapping modularGraph = new TransformationHypergraphToGraphMapping(monitor);
    modularGraph.generate(inputHypergraph);
    final TransformationMaximalInterconnectedGraph maximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(monitor);
    ModularHypergraph _result = modularGraph.getResult();
    maximalInterconnectedGraph.generate(_result);
    final TransformationIntraModuleGraph intraModuleGraph = new TransformationIntraModuleGraph(monitor);
    ModularHypergraph _result_1 = modularGraph.getResult();
    intraModuleGraph.generate(_result_1);
    final CalculateComplexity calculateComplexity = new CalculateComplexity(monitor);
    ModularHypergraph _result_2 = maximalInterconnectedGraph.getResult();
    final double complexityMaximalInterconnected = calculateComplexity.calculate(_result_2, 
      "Calculate maximal interconnected graph complexity");
    ModularHypergraph _result_3 = intraModuleGraph.getResult();
    final double coupling = calculateComplexity.calculate(_result_3, 
      "Calculate maximal interconnected graph complexity");
    final double cohesion = (coupling / complexityMaximalInterconnected);
    IProject _project = this.project.getProject();
    String _name = _project.getName();
    result.addResult(_name, "graph cohesion", Double.valueOf(cohesion));
    this.updateView(inputHypergraph);
    return cohesion;
  }
  
  /**
   * Update the analysis view after updating its content.
   */
  protected void updateView(final Hypergraph hypergraph) {
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
            ((AnalysisResultView) part).setHypergraph(hypergraph);
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
