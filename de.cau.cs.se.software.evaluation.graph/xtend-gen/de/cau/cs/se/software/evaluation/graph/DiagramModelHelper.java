package de.cau.cs.se.software.evaluation.graph;

import de.cau.cs.se.software.evaluation.hypergraph.Edge;
import de.cau.cs.se.software.evaluation.hypergraph.Module;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class DiagramModelHelper {
  public static boolean addUnique(final List<Edge> edges, final Edge edge) {
    boolean _xifexpression = false;
    boolean _contains = edges.contains(edge);
    boolean _not = (!_contains);
    if (_not) {
      _xifexpression = edges.add(edge);
    }
    return _xifexpression;
  }
  
  public static boolean addUnique(final List<Module> modules, final Module module) {
    boolean _xifexpression = false;
    boolean _contains = modules.contains(module);
    boolean _not = (!_contains);
    if (_not) {
      _xifexpression = modules.add(module);
    }
    return _xifexpression;
  }
  
  public static void addAllUnique(final List<Module> modules, final List<Module> additionalModules) {
    final Consumer<Module> _function = (Module it) -> {
      DiagramModelHelper.addUnique(modules, it);
    };
    additionalModules.forEach(_function);
  }
}
