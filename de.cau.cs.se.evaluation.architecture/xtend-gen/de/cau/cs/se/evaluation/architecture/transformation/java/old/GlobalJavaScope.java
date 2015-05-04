package de.cau.cs.se.evaluation.architecture.transformation.java.old;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.Scope;
import java.util.List;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.xtext.xbase.lib.Exceptions;

/**
 * Provide the scope over different Java projects.
 */
@SuppressWarnings("all")
public class GlobalJavaScope extends Scope implements IScope {
  private final List<IJavaProject> projects;
  
  public GlobalJavaScope(final List<IJavaProject> projects, final IScope parent) {
    super(parent);
    this.projects = projects;
  }
  
  public IType getType(final String name) {
    try {
      IType type = null;
      for (final IJavaProject project : this.projects) {
        {
          IType _findType = project.findType(name);
          type = _findType;
          boolean _notEquals = (!Objects.equal(type, null));
          if (_notEquals) {
            return type;
          }
        }
      }
      IScope _parentScope = this.getParentScope();
      boolean _notEquals = (!Objects.equal(_parentScope, null));
      if (_notEquals) {
        IScope _parentScope_1 = this.getParentScope();
        return _parentScope_1.getType(name);
      } else {
        return null;
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
