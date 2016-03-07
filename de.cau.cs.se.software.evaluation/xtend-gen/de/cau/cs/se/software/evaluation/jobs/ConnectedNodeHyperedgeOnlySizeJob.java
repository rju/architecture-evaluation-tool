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
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.jobs.CalculateComplexity;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationConnectedNodeHyperedgesOnlyGraph;
import de.cau.cs.se.software.evaluation.transformation.metric.TransformationHypergraphSize;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

/**
 * Determine a connected node hyperedges only graph and calculate
 * its size.
 */
@SuppressWarnings("all")
public class ConnectedNodeHyperedgeOnlySizeJob extends Job {
  private CalculateComplexity parent;
  
  private Hypergraph input;
  
  public ConnectedNodeHyperedgeOnlySizeJob(final String name, final CalculateComplexity parent, final Hypergraph input) {
    super(name);
    this.parent = parent;
    this.input = input;
  }
  
  @Override
  protected IStatus run(final IProgressMonitor monitor) {
    Node node = null;
    int i = 0;
    final TransformationConnectedNodeHyperedgesOnlyGraph connectedNodeHyperedgesOnlyGraph = new TransformationConnectedNodeHyperedgesOnlyGraph(monitor);
    final TransformationHypergraphSize hypergraphSize = new TransformationHypergraphSize(monitor);
    while ((!Objects.equal((node = this.parent.getNextConnectedNodeTask()), null))) {
      {
        monitor.beginTask(("Determine S^#_" + Integer.valueOf(i)), 0);
        connectedNodeHyperedgesOnlyGraph.setNode(node);
        hypergraphSize.setName((("Determine Size(S^#_" + Integer.valueOf(i)) + ")"));
        Hypergraph _generate = connectedNodeHyperedgesOnlyGraph.generate(this.input);
        Double _generate_1 = hypergraphSize.generate(_generate);
        this.parent.deliverConnectedNodeHyperedgesOnlySizeResult((_generate_1).doubleValue());
        i++;
      }
    }
    monitor.done();
    return Status.OK_STATUS;
  }
}
