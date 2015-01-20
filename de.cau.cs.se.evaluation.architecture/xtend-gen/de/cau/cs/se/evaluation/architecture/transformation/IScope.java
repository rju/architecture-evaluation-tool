package de.cau.cs.se.evaluation.architecture.transformation;

import org.eclipse.jdt.core.IType;

@SuppressWarnings("all")
public interface IScope {
  /**
   * Returns type if the type is present in the scope else null.
   */
  public abstract IType getType(final String name);
}
