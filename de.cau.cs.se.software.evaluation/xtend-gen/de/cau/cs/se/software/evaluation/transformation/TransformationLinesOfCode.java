package de.cau.cs.se.software.evaluation.transformation;

import com.google.common.base.Objects;
import de.cau.cs.se.software.evaluation.transformation.AbstractTransformation;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Javadoc;

@SuppressWarnings("all")
public class TransformationLinesOfCode extends AbstractTransformation<List<AbstractTypeDeclaration>, Long> {
  public TransformationLinesOfCode(final IProgressMonitor monitor) {
    super(monitor);
  }
  
  @Override
  public Long generate(final List<AbstractTypeDeclaration> input) {
    this.result = Long.valueOf(0L);
    final Consumer<AbstractTypeDeclaration> _function = (AbstractTypeDeclaration type) -> {
      ASTNode _parent = type.getParent();
      if ((_parent instanceof CompilationUnit)) {
        ASTNode _parent_1 = type.getParent();
        final CompilationUnit compilationUnit = ((CompilationUnit) _parent_1);
        int _calculateLOC = this.calculateLOC(compilationUnit, type);
        Javadoc _javadoc = type.getJavadoc();
        int _calculateLOC_1 = this.calculateLOC(compilationUnit, _javadoc);
        int loc = (_calculateLOC - _calculateLOC_1);
        Integer _valueOf = Integer.valueOf(loc);
        long _longValue = _valueOf.longValue();
        long _plus = ((this.result).longValue() + _longValue);
        this.result = Long.valueOf(_plus);
      }
      this.monitor.worked(1);
    };
    input.forEach(_function);
    return this.result;
  }
  
  private int calculateLOC(final CompilationUnit compilationUnit, final ASTNode node) {
    int _xifexpression = (int) 0;
    boolean _notEquals = (!Objects.equal(node, null));
    if (_notEquals) {
      int _length = node.getLength();
      int _startPosition = node.getStartPosition();
      int _plus = (_length + _startPosition);
      int _lineNumber = compilationUnit.getLineNumber(_plus);
      int _startPosition_1 = node.getStartPosition();
      int _lineNumber_1 = compilationUnit.getLineNumber(_startPosition_1);
      int _minus = (_lineNumber - _lineNumber_1);
      _xifexpression = (_minus + 1);
    } else {
      _xifexpression = 0;
    }
    return _xifexpression;
  }
  
  @Override
  public int workEstimate(final List<AbstractTypeDeclaration> input) {
    return input.size();
  }
}
