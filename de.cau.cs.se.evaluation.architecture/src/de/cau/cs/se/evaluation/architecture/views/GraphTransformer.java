package de.cau.cs.se.evaluation.architecture.views;

import de.cau.cs.se.evaluation.architecture.hypergraph.CallerCalleeTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.FieldTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.MethodTrace;
import de.cau.cs.se.evaluation.architecture.hypergraph.ModularHypergraph;
import de.cau.cs.se.evaluation.architecture.hypergraph.TypeTrace;

/**
 * Is used to transform a modular hypergraph into a serializable Form.
 *
 * @author Yannic Kropp
 *
 */
public class GraphTransformer {

	ModularHypergraph makeSerializable(final ModularHypergraph old){
		//final HypergraphFactory HgFactory = HypergraphFactory.eINSTANCE;
		final ModularHypergraph result = old;

		//fix Modules
		for (int i = 0; i<result.getModules().size(); i++){
			if(result.getModules().get(i).getDerivedFrom() instanceof TypeTrace){
				((TypeTrace) result.getModules().get(i).getDerivedFrom()).setType(null);
			}
		}

		//fix Nodes
		for (int i = 0; i<result.getNodes().size(); i++){
			if(result.getNodes().get(i).getDerivedFrom() instanceof TypeTrace){
				((TypeTrace) result.getNodes().get(i).getDerivedFrom()).setType(null);
			}
			if(result.getNodes().get(i).getDerivedFrom() instanceof MethodTrace){
				((MethodTrace) result.getNodes().get(i).getDerivedFrom()).setMethod(null);
			}
		}

		//fix Edges
		for (int i = 0; i<result.getEdges().size(); i++){
			if(result.getEdges().get(i).getDerivedFrom() instanceof FieldTrace){
				((FieldTrace) result.getEdges().get(i).getDerivedFrom()).setField(null);
			}
			if(result.getEdges().get(i).getDerivedFrom() instanceof CallerCalleeTrace){
				((CallerCalleeTrace) result.getEdges().get(i).getDerivedFrom()).setCaller(null);
				((CallerCalleeTrace) result.getEdges().get(i).getDerivedFrom()).setCallee(null);
			}
		}

		return result;

	}

}
