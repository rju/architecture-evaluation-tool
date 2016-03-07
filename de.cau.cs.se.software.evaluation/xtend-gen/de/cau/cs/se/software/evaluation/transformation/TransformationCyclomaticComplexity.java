package de.cau.cs.se.software.evaluation.transformation;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.CyclomaticComplexityVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Conversions;

@SuppressWarnings("all")
public class TransformationCyclomaticComplexity extends AbstractTransformation<List<AbstractTypeDeclaration>, List<Integer>> {
  private final int MAX_BUCKET = 100;
  
  private int topBucket = 0;
  
  private final int[] buckets = new int[this.MAX_BUCKET];
  
  public TransformationCyclomaticComplexity(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public List<Integer> generate(final List<AbstractTypeDeclaration> input) {
    ArrayList<Integer> _arrayList = new ArrayList<Integer>();
    this.result = _arrayList;
    Iterable<TypeDeclaration> _filter = Iterables.<TypeDeclaration>filter(input, TypeDeclaration.class);
    final Consumer<TypeDeclaration> _function = (TypeDeclaration type) -> {
      this.checkMethods(type);
    };
    _filter.forEach(_function);
    for (int i = 0; (i <= this.topBucket); i++) {
      int _get = this.buckets[i];
      this.result.add(Integer.valueOf(_get));
    }
    return this.result;
  }
  
  private void checkMethods(final TypeDeclaration declaration) {
    MethodDeclaration[] _methods = declaration.getMethods();
    final Consumer<MethodDeclaration> _function = (MethodDeclaration method) -> {
      Block _body = method.getBody();
      boolean _notEquals = (!Objects.equal(_body, null));
      if (_notEquals) {
        final CyclomaticComplexityVisitor visitor = new CyclomaticComplexityVisitor("");
        method.accept(visitor);
        int bucket = visitor.getCyclomatic();
        if ((bucket > this.MAX_BUCKET)) {
          bucket = this.MAX_BUCKET;
        }
        if ((bucket > this.topBucket)) {
          this.topBucket = bucket;
        }
        int _get = this.buckets[bucket];
        int _plus = (_get + 1);
        this.buckets[bucket] = _plus;
      }
    };
    ((List<MethodDeclaration>)Conversions.doWrapArray(_methods)).forEach(_function);
  }
}
