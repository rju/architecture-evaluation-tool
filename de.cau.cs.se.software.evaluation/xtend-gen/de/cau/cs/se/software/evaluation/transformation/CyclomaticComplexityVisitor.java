package de.cau.cs.se.software.evaluation.transformation;

import com.google.common.base.Objects;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.WhileStatement;

@SuppressWarnings("all")
public class CyclomaticComplexityVisitor extends ASTVisitor {
  private int cyclomatic = 1;
  
  private final String source;
  
  public CyclomaticComplexityVisitor(final String source) {
    this.source = source;
  }
  
  public int getCyclomatic() {
    return this.cyclomatic;
  }
  
  @Override
  public boolean visit(final CatchClause node) {
    this.cyclomatic++;
    return true;
  }
  
  @Override
  public boolean visit(final ConditionalExpression node) {
    this.cyclomatic++;
    return true;
  }
  
  @Override
  public boolean visit(final InfixExpression node) {
    InfixExpression.Operator _operator = node.getOperator();
    boolean _matched = false;
    if (Objects.equal(_operator, InfixExpression.Operator.CONDITIONAL_AND)) {
      _matched=true;
      this.cyclomatic++;
    }
    if (!_matched) {
      if (Objects.equal(_operator, InfixExpression.Operator.CONDITIONAL_OR)) {
        _matched=true;
        this.cyclomatic++;
      }
    }
    return true;
  }
  
  @Override
  public boolean visit(final LambdaExpression node) {
    return true;
  }
  
  @Override
  public boolean visit(final DoStatement node) {
    this.cyclomatic++;
    return true;
  }
  
  @Override
  public boolean visit(final ForStatement node) {
    this.cyclomatic++;
    return true;
  }
  
  @Override
  public boolean visit(final IfStatement node) {
    this.cyclomatic++;
    return true;
  }
  
  @Override
  public boolean visit(final SwitchCase node) {
    this.cyclomatic++;
    return true;
  }
  
  @Override
  public boolean visit(final WhileStatement node) {
    this.cyclomatic++;
    return true;
  }
  
  @Override
  public boolean visit(final ExpressionStatement node) {
    return false;
  }
}
