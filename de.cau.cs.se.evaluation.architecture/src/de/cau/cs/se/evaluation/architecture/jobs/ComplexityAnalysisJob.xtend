package de.cau.cs.se.evaluation.architecture.jobs

import de.cau.cs.se.evaluation.architecture.hypergraph.Edge
import de.cau.cs.se.evaluation.architecture.hypergraph.Hypergraph
import de.cau.cs.se.evaluation.architecture.hypergraph.HypergraphFactory
import de.cau.cs.se.evaluation.architecture.hypergraph.Node
import de.cau.cs.se.evaluation.architecture.transformation.java.TransformationJavaMethodsToModularHypergraph
import de.cau.cs.se.evaluation.architecture.transformation.metrics.NamedValue
import de.cau.cs.se.evaluation.architecture.transformation.metrics.ResultModelProvider
import de.cau.cs.se.evaluation.architecture.transformation.metrics.TransformationHypergraphMetrics
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationHyperedgesOnlyGraph
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationIntermoduleHyperedgesOnlyGraph
import de.cau.cs.se.evaluation.architecture.transformation.processing.TransformationMaximalInterconnectedGraph
import de.cau.cs.se.evaluation.architecture.views.AnalysisResultView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList
import java.util.Iterator
import java.util.List
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IProject
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.jobs.Job
import org.eclipse.emf.common.util.EList
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ITreeSelection
import org.eclipse.jface.viewers.TreeSelection
import org.eclipse.swt.widgets.Shell
import org.eclipse.ui.PartInitException
import org.eclipse.ui.PlatformUI

class ComplexityAnalysisJob extends Job {
	
	var types = new ArrayList<IType>()
	
	List<String> dataTypePatterns
	
	val Shell shell
	
	List<String> observedSystemPatterns
	
	ISelection selection
	
	// Used in the parallelized version of this
	var volatile Iterator<Node> globalHyperEdgesOnlyGraphNodes
	var volatile List<Hypergraph> resultConnectedNodeGraphs
	
	var volatile double complexity
	
	var volatile List<Hypergraph> globalMetricsSubGraphs
	
	var volatile int globalMetricsSubGraphCounter
	
	var volatile int globalMetricsSubGraphTotal
	
	/**
	 * The constructor scans the selection for files.
	 * Compare to http://stackoverflow.com/questions/6892294/eclipse-plugin-how-to-get-the-path-to-the-currently-selected-project
	 */
	public new (ISelection selection, Shell shell) {
		super("Analysis Complexity")
		this.shell = shell
		this.selection = selection
	}
	
	protected override IStatus run(IProgressMonitor monitor) {
		// determine class files.
		determineClassFiles(monitor)
		// set total number of work units
		val result = ResultModelProvider.INSTANCE
		/** construct analysis. */
		val projects = new ArrayList<IJavaProject>()
		types.forEach[type | if (!projects.contains(type.javaProject)) projects.add(type.javaProject)]
		monitor.beginTask("Processing project", 0)
		
		val projectName = projects.get(0).project.name
		
		monitor.subTask("Determining observed system classes")
		val javaToModularHypergraph = new TransformationJavaMethodsToModularHypergraph(projects.get(0), types, dataTypePatterns, observedSystemPatterns, monitor)
		result.values.add(new NamedValue(projectName + " size of observed system", javaToModularHypergraph.classList.size))
		updateView()
		
		monitor.subTask("Reading Project")
		javaToModularHypergraph.transform
		
		result.values.add(new NamedValue(projectName + " number of modules", javaToModularHypergraph.modularSystem.modules.size))
		result.values.add(new NamedValue(projectName + " number of nodes",javaToModularHypergraph.modularSystem.nodes.size))
		result.values.add(new NamedValue(projectName + " number of edges",javaToModularHypergraph.modularSystem.edges.size))
		updateView()
						
		/** Preparing different hypergraphs */
		
		// Transformation for MS^(n)
		val transformationMaximalInterconnectedGraph = new TransformationMaximalInterconnectedGraph(javaToModularHypergraph.modularSystem)
		// Transformation for MS^*
		val transformationIntermoduleHyperedgesOnlyGraph = new TransformationIntermoduleHyperedgesOnlyGraph(javaToModularHypergraph.modularSystem)
		
		monitor.subTask("Create maximal interconnected graph")
		transformationMaximalInterconnectedGraph.transform
		monitor.subTask("Create intermodule hyperedges only graph")
		transformationIntermoduleHyperedgesOnlyGraph.transform
		
		/** calculating result metrics */
		monitor.beginTask("Calculating metrics", 1+3*3)
		// Calculation for S
		val metrics = new TransformationHypergraphMetrics(monitor)
		metrics.setSystem(javaToModularHypergraph.modularSystem)
		monitor.subTask("System size")
		val systemSize = metrics.calculate
		monitor.worked(1)
		
		// Calculation for S -> S^#, S^#_i
		val complexity = calculateComplexity(javaToModularHypergraph.modularSystem, monitor, "Complexity")
		// Calculation for MS^(n) -> MS^(n)#, MS^(n)#_i
		val complexityMaximalInterconnected = calculateComplexity(transformationMaximalInterconnectedGraph.result, 
			monitor, "Maximal interconnected graph complexity"
		)
		// Calculation for MS^* -> MS^*#, MS^*#_i
		val complexityIntermodule = calculateComplexity(transformationIntermoduleHyperedgesOnlyGraph.result, monitor, "Intermodule complexity")	
		
		/** display results */
		
		// val megaModelGraph = createMegaModelAnalysis
		// metrics.system = megaModelGraph
		// val mmSize = metrics.calculate
		// val mmComplexity = calculateComplexity(megaModelGraph, monitor)
				
		// result.getValues.add(new NamedValue("Size", mmSize))
		// result.getValues.add(new NamedValue("Complexity", mmComplexity))

		result.getValues.add(new NamedValue(projectName + " Size", systemSize))
		result.getValues.add(new NamedValue(projectName + " Complexity", complexity))
		result.getValues.add(new NamedValue(projectName + " Cohesion", complexityIntermodule/complexityMaximalInterconnected))
		result.getValues.add(new NamedValue(projectName + " Coupling", complexityIntermodule))
				
		monitor.done()
		
		updateView()
							
		return Status.OK_STATUS
	}
	
	private def updateView() {
		PlatformUI.getWorkbench.display.syncExec(new Runnable() {
       		public override void run() {
	           try { 
					val part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(AnalysisResultView.ID)
					(part as AnalysisResultView).update(ResultModelProvider.INSTANCE)
	           } catch (PartInitException e) {
	                e.printStackTrace()
	           }
	    	}
     	})
	}
	
	/**
	 * Determine class files.
	 */
	private def determineClassFiles(IProgressMonitor monitor) {
		if (this.selection instanceof IStructuredSelection) {
			System.out.println("Got a structured selection");
			if (this.selection instanceof ITreeSelection) {
				val TreeSelection treeSelection = selection as TreeSelection
				
				treeSelection.iterator.forEach[element |
					if (element instanceof IProject)
						element.scanForClasses(monitor)
					else
						MessageDialog.openWarning(shell, "Wrong Selection", "Only project roots can be selected.")
				]
			}
		}
	}
	
	/**
	 * Create Megamodel graph
	 */
	def createMegaModelAnalysis() {
		val Hypergraph graph = HypergraphFactory.eINSTANCE.createHypergraph
		for(var int i=1;i<22;i++) {
			val node = HypergraphFactory.eINSTANCE.createNode
			node.name = "Node " + i
			graph.nodes.add(node)
		}
	
		for(var int i=1;i<26;i++) {
			val edge = HypergraphFactory.eINSTANCE.createEdge
			edge.name = "Edge " + i
			graph.edges.add(edge)
		}
		
		connectNode(graph.nodes, graph.edges, "Node 1", "Edge 3")
		connectNode(graph.nodes, graph.edges, "Node 1", "Edge 9")
		
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 3")
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 4")
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 5")
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 6")
		connectNode(graph.nodes, graph.edges, "Node 2", "Edge 2")
		
		connectNode(graph.nodes, graph.edges, "Node 3", "Edge 2")
		connectNode(graph.nodes, graph.edges, "Node 3", "Edge 1")
		
		connectNode(graph.nodes, graph.edges, "Node 4", "Edge 9")
		connectNode(graph.nodes, graph.edges, "Node 4", "Edge 4")
		connectNode(graph.nodes, graph.edges, "Node 4", "Edge 25")
		connectNode(graph.nodes, graph.edges, "Node 4", "Edge 7")
		
		connectNode(graph.nodes, graph.edges, "Node 5", "Edge 7")
		connectNode(graph.nodes, graph.edges, "Node 5", "Edge 1")
		
		connectNode(graph.nodes, graph.edges, "Node 6", "Edge 5")
		connectNode(graph.nodes, graph.edges, "Node 6", "Edge 22")
		connectNode(graph.nodes, graph.edges, "Node 6", "Edge 24")
		
		connectNode(graph.nodes, graph.edges, "Node 7", "Edge 6")
		connectNode(graph.nodes, graph.edges, "Node 7", "Edge 24")
		connectNode(graph.nodes, graph.edges, "Node 7", "Edge 13")
		connectNode(graph.nodes, graph.edges, "Node 7", "Edge 23")
		
		connectNode(graph.nodes, graph.edges, "Node 8", "Edge 22")
		connectNode(graph.nodes, graph.edges, "Node 8", "Edge 1")
		
		connectNode(graph.nodes, graph.edges, "Node 9", "Edge 13")
		connectNode(graph.nodes, graph.edges, "Node 9", "Edge 12")
		
		connectNode(graph.nodes, graph.edges, "Node 10", "Edge 9")
		connectNode(graph.nodes, graph.edges, "Node 10", "Edge 10")
		
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 10")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 25")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 24")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 23")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 11")
		connectNode(graph.nodes, graph.edges, "Node 11", "Edge 16")
		
		connectNode(graph.nodes, graph.edges, "Node 12", "Edge 10")
		connectNode(graph.nodes, graph.edges, "Node 12", "Edge 8")
		
		connectNode(graph.nodes, graph.edges, "Node 13", "Edge 10")
		connectNode(graph.nodes, graph.edges, "Node 13", "Edge 21")
		
		connectNode(graph.nodes, graph.edges, "Node 14", "Edge 8")
		connectNode(graph.nodes, graph.edges, "Node 14", "Edge 21")
		
		connectNode(graph.nodes, graph.edges, "Node 15", "Edge 21")
		connectNode(graph.nodes, graph.edges, "Node 15", "Edge 20")
		connectNode(graph.nodes, graph.edges, "Node 15", "Edge 17")
		
		connectNode(graph.nodes, graph.edges, "Node 16", "Edge 20")
		connectNode(graph.nodes, graph.edges, "Node 16", "Edge 1")
		connectNode(graph.nodes, graph.edges, "Node 16", "Edge 19")
		
		connectNode(graph.nodes, graph.edges, "Node 17", "Edge 19")
		connectNode(graph.nodes, graph.edges, "Node 17", "Edge 18")
		
		connectNode(graph.nodes, graph.edges, "Node 18", "Edge 8")
		connectNode(graph.nodes, graph.edges, "Node 18", "Edge 16")
		connectNode(graph.nodes, graph.edges, "Node 18", "Edge 17")
		connectNode(graph.nodes, graph.edges, "Node 18", "Edge 18")
		
		connectNode(graph.nodes, graph.edges, "Node 19", "Edge 18")
		connectNode(graph.nodes, graph.edges, "Node 19", "Edge 11")
			
		connectNode(graph.nodes, graph.edges, "Node 20", "Edge 11")	
		connectNode(graph.nodes, graph.edges, "Node 20", "Edge 12")	
		connectNode(graph.nodes, graph.edges, "Node 20", "Edge 18")	
		
		connectNode(graph.nodes, graph.edges, "Node 21", "Edge 12")	
		connectNode(graph.nodes, graph.edges, "Node 21", "Edge 14")	
		connectNode(graph.nodes, graph.edges, "Node 21", "Edge 1")	
		
		return graph
	}
	
	def connectNode(EList<Node> nodes, EList<Edge> edges, String nodeName, String edgeName) {
		val node = nodes.findFirst[node | node.name.equals(nodeName)]
		val edge = edges.findFirst[edge | edge.name.equals(edgeName)]
		node.edges.add(edge)
	}
	
	
	/**
	 * Calculate for a given modular hyper graph:
	 * - hyperedges only graph
	 * - hyperedges only graphs for each node in the graph which is connected to the i-th node
	 * - calculate the size of all graphs
	 * - calculate the complexity
	 * 
	 * @param input a modular system
	 */
	private def calculateComplexity(Hypergraph input, IProgressMonitor monitor, String message) {
		monitor?.subTask(message + " - S^#")
		// S^#
		val transformationHyperedgesOnlyGraph = new TransformationHyperedgesOnlyGraph(input)
		transformationHyperedgesOnlyGraph.transform
		monitor?.worked(1)
		
		// S^#_i
		//val transformationConnectedNodeHyperedgesOnlyGraph = 
		//	new TransformationConnectedNodeHyperedgesOnlyGraph(transformationHyperedgesOnlyGraph.result)
		
		//val resultConnectedNodeGraphs = new ArrayList<Hypergraph>() 
		
		// var int i = 0
	
		var List<Job> jobs = new ArrayList<Job>()
		
		resultConnectedNodeGraphs = new ArrayList<Hypergraph>() 
		globalHyperEdgesOnlyGraphNodes = transformationHyperedgesOnlyGraph.result.nodes.iterator
				
		for (var int j=0;j<8;j++) {
			val job = new CalculationSubJob("S^#_i " + j, this, transformationHyperedgesOnlyGraph.result)
			jobs.add(job)
			job.schedule
		}
		
		jobs.forEach[it.join]
				
		//for (Node node : transformationHyperedgesOnlyGraph.result.nodes) {
		//	monitor?.subTask(message + " - S^#_" + i)
			
		//	transformationConnectedNodeHyperedgesOnlyGraph.node = node
		//	transformationConnectedNodeHyperedgesOnlyGraph.transform
		//	resultConnectedNodeGraphs.add(transformationConnectedNodeHyperedgesOnlyGraph.result)
		//	i++			
		//}
		monitor?.worked(1)
		
		val metrics = new TransformationHypergraphMetrics(monitor)
		
		metrics.setSystem(transformationHyperedgesOnlyGraph.result)
		monitor?.subTask(message + " - calculate")
		val complexity = calculateComplexity(transformationHyperedgesOnlyGraph.result, resultConnectedNodeGraphs, monitor)
		monitor?.worked(1)
		
		return complexity
	}
	
	/**
	 * Used for the parallelization. Return the next task
	 */
	synchronized def getNextConnectedNodeTask() {
		if (globalHyperEdgesOnlyGraphNodes.hasNext)
			globalHyperEdgesOnlyGraphNodes.next
		else
			null
	}
	
	/**
	 * Used for the parallelization. Deliver the result
	 */
	synchronized def deliverResult(Hypergraph hypergraph) {
		resultConnectedNodeGraphs.add(hypergraph)
	}
	
	
	/**
	 * Calculate complexity.
	 */
	private def double calculateComplexity(Hypergraph hypergraph, List<Hypergraph> subgraphs, IProgressMonitor monitor) {
		//val metrics = new TransformationHypergraphMetrics(monitor)
		//var double complexity = 0
		complexity = 0
		///** Can start at zero, as we ignore environment node by using system and not system graph. */
		//for (var int i=0;i < hypergraph.nodes.size;i++) {
		//	metrics.setSystem(subgraphs.get(i))					
		//	complexity += metrics.calculate
		//}
		
		globalMetricsSubGraphs = subgraphs
		globalMetricsSubGraphCounter = 0
		globalMetricsSubGraphTotal = hypergraph.nodes.size
		
		var List<Job> jobs = new ArrayList<Job>()
		for (var int j=0;j<8;j++) {
			val job = new MetricsSubJob("Metrics " + j, this)
			jobs.add(job)
			job.schedule
		}
		
		jobs.forEach[it.join]
		
		val metrics = new TransformationHypergraphMetrics(monitor)
		/** the rest stays in both versions */
		metrics.setSystem(hypergraph)
		complexity -= metrics.calculate
					
		return complexity
	}
		
	synchronized def getNextSubgraph() {
		if (globalMetricsSubGraphCounter<globalMetricsSubGraphTotal) {
			val result = globalMetricsSubGraphs.get(globalMetricsSubGraphCounter)
			globalMetricsSubGraphCounter++
			return result
		} else
			return null
	}
	
	synchronized def deliverMetricsResult(double d) {
		complexity += d
	}
	
	
	private def scanForClasses(IProject object, IProgressMonitor monitor) {
		System.out.println("  IProject " + object.toString)
		monitor.beginTask("Scanning project " + object.name,0)
		val dataTypePatternsFile = object.findMember("data-type-pattern.cfg") as IFile
		if (dataTypePatternsFile.isSynchronized(1)) {
			dataTypePatterns = readPattern(dataTypePatternsFile)
			val observedSystemPatternsFile = object.findMember("observed-system.cfg") as IFile
			if (observedSystemPatternsFile.isSynchronized(1)) {
				observedSystemPatterns  = readPattern(observedSystemPatternsFile)
				if (object.hasNature(JavaCore.NATURE_ID)) {
					val IJavaProject project = JavaCore.create(object);
					project.allPackageFragmentRoots.forEach[root | root.checkForTypes(monitor)]
				}
			} else {
				MessageDialog.openError(shell, "Configuration Error", "Observed system file (observed-system.cfg) listing patterns for classes of the observed system is missing.")
			}	
		} else {
			MessageDialog.openError(shell, "Configuration Error", "Data type pattern file (data-type-pattern.cfg) listing patterns for data type classes is missing.")
		}
	}
			
	/**
	 * Read full qualified class name patterns form an IFile.
	 * 
	 * @param file the file to read.
	 */
	private def readPattern(IFile file) {
		val List<String> patterns = new ArrayList<String>()
		val reader = new BufferedReader(new InputStreamReader(file.contents))
		var String line
		while ((line = reader.readLine()) != null) {
			patterns.add(line.replaceAll("\\.","\\."))
		}
		
		return patterns
	}
	
	/** check for types in the project hierarchy */
	
	/**
	 * in fragment roots 
	 */
	private def void checkForTypes(IPackageFragmentRoot root, IProgressMonitor monitor) {
		monitor.subTask(root.elementName)
		root.children.forEach[ element |
			if (element instanceof IPackageFragment) {
				(element as IPackageFragment).checkForTypes(monitor)
			}
		]
	}
	
	/**
	 * in fragments
	 */
	private def void checkForTypes(IPackageFragment fragment, IProgressMonitor monitor) {
		fragment.children.forEach[ element |
			if (element instanceof IPackageFragment) {
				(element as IPackageFragment).checkForTypes(monitor)
			} else if (element instanceof ICompilationUnit) {
				(element as ICompilationUnit).checkForTypes(monitor)
			}
		]
	}
	
	/**
	 * in compilation units
	 */
	private def void checkForTypes(ICompilationUnit unit, IProgressMonitor monitor) {
		unit.allTypes.forEach[type |
			if (type instanceof IType) {
				if (!(type as IType).binary) types.add(type)
			}
		]
	}
	
		
	
}