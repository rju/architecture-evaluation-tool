package de.cau.cs.se.software.evaluation.transformation;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import de.cau.cs.se.software.evaluation.transformation.CyclomaticComplexityVisitor;
import de.cau.cs.se.software.evaluation.views.LogModelProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;

@SuppressWarnings("all")
public class TransformationCyclomaticComplexity extends AbstractTransformation<List<AbstractTypeDeclaration>, List<Integer>> {
  private final int START_BUCKET = 100;
  
  private final int MORE_BUCKET = 100;
  
  private int maxBucket = this.START_BUCKET;
  
  private int topBucket = 0;
  
  private int[] buckets = new int[this.maxBucket];
  
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
    int _size = input.size();
    this.monitor.worked(_size);
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
        while ((bucket >= this.maxBucket)) {
          {
            int _maxBucket = this.maxBucket;
            this.maxBucket = (_maxBucket + this.MORE_BUCKET);
            final int[] resizedBuckets = new int[this.maxBucket];
            final Procedure2<Integer, Integer> _function_1 = (Integer value, Integer i) -> {
              resizedBuckets[(i).intValue()] = (value).intValue();
            };
            IterableExtensions.<Integer>forEach(((Iterable<Integer>)Conversions.doWrapArray(this.buckets)), _function_1);
            this.buckets = resizedBuckets;
            SimpleName _name = method.getName();
            String _plus = ((("Resizing bucket array to " + Integer.valueOf(this.maxBucket)) + " for ") + _name);
            String _plus_1 = (_plus + " with a complexity of ");
            String _plus_2 = (_plus_1 + Integer.valueOf(bucket));
            LogModelProvider.INSTANCE.addMessage("Metric management", _plus_2);
          }
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
  
  @Override
  public int workEstimate(final List<AbstractTypeDeclaration> input) {
    return input.size();
  }
}
