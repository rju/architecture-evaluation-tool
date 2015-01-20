package de.cau.cs.se.evaluation.architecture.transformation.java;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.jdt.core.IType;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class JavaTypeHelper {
  /**
   * Add a list of new types to a list of types if they do not already occur in the list.
   */
  public List<IType> addUnique(final List<IType> types, final List<IType> addTypes) {
    if (addTypes!=null) {
      final Procedure1<IType> _function = new Procedure1<IType>() {
        public void apply(final IType type) {
          JavaTypeHelper.this.addUnique(types, type);
        }
      };
      IterableExtensions.<IType>forEach(addTypes, _function);
    }
    return types;
  }
  
  /**
   * Check if a type is already part of the type list. If not add it.
   */
  public List<IType> addUnique(final List<IType> types, final IType type) {
    boolean _notEquals = (!Objects.equal(type, null));
    if (_notEquals) {
      final Function1<IType, Boolean> _function = new Function1<IType, Boolean>() {
        public Boolean apply(final IType it) {
          return Boolean.valueOf(it.equals(type));
        }
      };
      IType _findFirst = IterableExtensions.<IType>findFirst(types, _function);
      boolean _equals = Objects.equal(_findFirst, null);
      if (_equals) {
        types.add(type);
      }
    }
    return types;
  }
}
