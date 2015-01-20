package de.cau.cs.se.evaluation.architecture.transformation;

import de.cau.cs.se.evaluation.architecture.transformation.IScope;

@SuppressWarnings("all")
public abstract class Scope implements IScope {
  private final IScope parentScope;
  
  public Scope(final IScope parentScope) {
    this.parentScope = parentScope;
  }
  
  public IScope getParentScope() {
    return this.parentScope;
  }
}
