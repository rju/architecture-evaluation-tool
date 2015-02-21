package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import de.cau.cs.se.evaluation.architecture.transformation.IScope;
import de.cau.cs.se.evaluation.architecture.transformation.Scope;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JavaLocalScope extends Scope implements IScope {
  private final Map<String, IType> importedClasses = new HashMap<String, IType>();
  
  public JavaLocalScope(final CompilationUnit unit, final IScope parent) {
    super(parent);
    List _imports = unit.imports();
    final Procedure1<Object> _function = new Procedure1<Object>() {
      public void apply(final Object ref) {
        final ImportDeclaration importDecl = ((ImportDeclaration) ref);
        Name _name = importDecl.getName();
        final String fqn = _name.getFullyQualifiedName();
        final IType type = parent.getType(fqn);
        String[] _split = fqn.split("\\.");
        String _last = IterableExtensions.<String>last(((Iterable<String>)Conversions.doWrapArray(_split)));
        JavaLocalScope.this.importedClasses.put(_last, type);
      }
    };
    IterableExtensions.<Object>forEach(_imports, _function);
  }
  
  public IType getType(final String name) {
    final IType result = this.importedClasses.get(name);
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
