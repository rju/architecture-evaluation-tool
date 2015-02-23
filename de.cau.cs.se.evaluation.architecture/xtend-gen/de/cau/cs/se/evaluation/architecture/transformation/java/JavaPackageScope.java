package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.Scope;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;

@SuppressWarnings("all")
public class JavaPackageScope extends Scope implements IScope {
  private final Map<String, IType> typesMap = new HashMap<String, IType>();
  
  public JavaPackageScope(final IPackageFragment packageFragment, final IScope parentScope) {
    super(parentScope);
    try {
      IJavaElement[] _children = packageFragment.getChildren();
      final Consumer<IJavaElement> _function = new Consumer<IJavaElement>() {
        public void accept(final IJavaElement it) {
          try {
            if ((it instanceof ICompilationUnit)) {
              final ICompilationUnit unit = ((ICompilationUnit) it);
              IType[] _allTypes = unit.getAllTypes();
              final IType type = _allTypes[0];
              boolean _notEquals = (!Objects.equal(type, null));
              if (_notEquals) {
                String _fullyQualifiedName = type.getFullyQualifiedName();
                final String[] fqn = _fullyQualifiedName.split("\\.");
                String _last = IterableExtensions.<String>last(((Iterable<String>)Conversions.doWrapArray(fqn)));
                JavaPackageScope.this.typesMap.put(_last, type);
              }
            }
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      ((List<IJavaElement>)Conversions.doWrapArray(_children)).forEach(_function);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public IType getType(final String name) {
    final IType result = this.typesMap.get(name);
    boolean _equals = Objects.equal(result, null);
    if (_equals) {
      IScope _parentScope = this.getParentScope();
      boolean _notEquals = (!Objects.equal(_parentScope, null));
      if (_notEquals) {
        IScope _parentScope_1 = this.getParentScope();
        return _parentScope_1.getType(name);
      } else {
        return null;
      }
    } else {
      return result;
    }
  }
}
