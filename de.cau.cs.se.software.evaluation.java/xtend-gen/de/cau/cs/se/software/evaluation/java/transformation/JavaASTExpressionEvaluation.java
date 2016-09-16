package de.cau.cs.se.software.evaluation.java.transformation;

import de.cau.cs.se.software.evaluation.hypergraph.MethodTrace;
import de.cau.cs.se.software.evaluation.hypergraph.ModularHypergraph;
import de.cau.cs.se.software.evaluation.hypergraph.Node;
import de.cau.cs.se.software.evaluation.hypergraph.NodeReference;
import de.cau.cs.se.software.evaluation.java.transformation.JavaASTExpressionEvaluationHelper;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

@SuppressWarnings("all")
public class JavaASTExpressionEvaluation {
  /**
   * Expression evaluation tree walker.
   */
  public static void evaluate(final Expression expression, final Node sourceNode, final ModularHypergraph graph, final List<String> dataTypePatterns) {
    NodeReference _derivedFrom = sourceNode.getDerivedFrom();
    Object _method = ((MethodTrace) _derivedFrom).getMethod();
    final IMethodBinding sourceMethodBinding = ((IMethodBinding) _method);
    final ITypeBinding contextTypeBinding = sourceMethodBinding.getDeclaringClass();
    boolean _matched = false;
    if (expression instanceof Annotation) {
      _matched=true;
    }
    if (!_matched) {
      if (expression instanceof BooleanLiteral) {
        _matched=true;
      }
    }
    if (!_matched) {
      if (expression instanceof CharacterLiteral) {
        _matched=true;
      }
    }
    if (!_matched) {
      if (expression instanceof NullLiteral) {
        _matched=true;
      }
    }
    if (!_matched) {
      if (expression instanceof NumberLiteral) {
        _matched=true;
      }
    }
    if (!_matched) {
      if (expression instanceof StringLiteral) {
        _matched=true;
      }
    }
    if (!_matched) {
      if (expression instanceof ThisExpression) {
        _matched=true;
      }
    }
    if (!_matched) {
      if (expression instanceof TypeLiteral) {
        _matched=true;
      }
    }
    if (_matched) {
    }
    if (!_matched) {
      if (expression instanceof ArrayAccess) {
        _matched=true;
        Expression _array = ((ArrayAccess)expression).getArray();
        JavaASTExpressionEvaluation.evaluate(_array, sourceNode, graph, dataTypePatterns);
        Expression _index = ((ArrayAccess)expression).getIndex();
        JavaASTExpressionEvaluation.evaluate(_index, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof ArrayCreation) {
        _matched=true;
        List _dimensions = ((ArrayCreation)expression).dimensions();
        final Consumer<Object> _function = (Object it) -> {
          JavaASTExpressionEvaluation.evaluate(((Expression) it), sourceNode, graph, dataTypePatterns);
        };
        _dimensions.forEach(_function);
        ArrayInitializer _initializer = ((ArrayCreation)expression).getInitializer();
        if (_initializer!=null) {
          JavaASTExpressionEvaluation.evaluate(_initializer, sourceNode, graph, dataTypePatterns);
        }
      }
    }
    if (!_matched) {
      if (expression instanceof ArrayInitializer) {
        _matched=true;
        List _expressions = ((ArrayInitializer)expression).expressions();
        final Consumer<Object> _function = (Object it) -> {
          JavaASTExpressionEvaluation.evaluate(((Expression) it), sourceNode, graph, dataTypePatterns);
        };
        _expressions.forEach(_function);
      }
    }
    if (!_matched) {
      if (expression instanceof Assignment) {
        _matched=true;
        Expression _leftHandSide = ((Assignment)expression).getLeftHandSide();
        JavaASTExpressionEvaluation.evaluate(_leftHandSide, sourceNode, graph, dataTypePatterns);
        Expression _rightHandSide = ((Assignment)expression).getRightHandSide();
        JavaASTExpressionEvaluation.evaluate(_rightHandSide, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof CastExpression) {
        _matched=true;
        Expression _expression = ((CastExpression)expression).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof ClassInstanceCreation) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processClassInstanceCreation(((ClassInstanceCreation)expression), sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof ConditionalExpression) {
        _matched=true;
        Expression _expression = ((ConditionalExpression)expression).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
        Expression _thenExpression = ((ConditionalExpression)expression).getThenExpression();
        JavaASTExpressionEvaluation.evaluate(_thenExpression, sourceNode, graph, dataTypePatterns);
        Expression _elseExpression = ((ConditionalExpression)expression).getElseExpression();
        if (_elseExpression!=null) {
          JavaASTExpressionEvaluation.evaluate(_elseExpression, sourceNode, graph, dataTypePatterns);
        }
      }
    }
    if (!_matched) {
      if (expression instanceof ExpressionMethodReference) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processExpressionMethodReference(((ExpressionMethodReference)expression), sourceNode, contextTypeBinding, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof FieldAccess) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processFieldAccess(((FieldAccess)expression), sourceNode, contextTypeBinding, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof InfixExpression) {
        _matched=true;
        Expression _leftOperand = ((InfixExpression)expression).getLeftOperand();
        JavaASTExpressionEvaluation.evaluate(_leftOperand, sourceNode, graph, dataTypePatterns);
        Expression _rightOperand = ((InfixExpression)expression).getRightOperand();
        if (_rightOperand!=null) {
          JavaASTExpressionEvaluation.evaluate(_rightOperand, sourceNode, graph, dataTypePatterns);
        }
      }
    }
    if (!_matched) {
      if (expression instanceof InstanceofExpression) {
        _matched=true;
        Expression _leftOperand = ((InstanceofExpression)expression).getLeftOperand();
        JavaASTExpressionEvaluation.evaluate(_leftOperand, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof LambdaExpression) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processLambdaExpression(((LambdaExpression)expression), sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof MethodInvocation) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processMethodInvocation(((MethodInvocation)expression), sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof ParenthesizedExpression) {
        _matched=true;
        Expression _expression = ((ParenthesizedExpression)expression).getExpression();
        JavaASTExpressionEvaluation.evaluate(_expression, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof PostfixExpression) {
        _matched=true;
        Expression _operand = ((PostfixExpression)expression).getOperand();
        JavaASTExpressionEvaluation.evaluate(_operand, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof PrefixExpression) {
        _matched=true;
        Expression _operand = ((PrefixExpression)expression).getOperand();
        JavaASTExpressionEvaluation.evaluate(_operand, sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof QualifiedName) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processQualifiedName(((QualifiedName)expression), sourceNode, graph);
      }
    }
    if (!_matched) {
      if (expression instanceof SimpleName) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processSimpleName(((SimpleName)expression), sourceNode, graph);
      }
    }
    if (!_matched) {
      if (expression instanceof SuperFieldAccess) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processSuperFieldAccess(((SuperFieldAccess)expression), sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof SuperMethodInvocation) {
        _matched=true;
        JavaASTExpressionEvaluationHelper.processSuperMethodInvocation(((SuperMethodInvocation)expression), sourceNode, graph, dataTypePatterns);
      }
    }
    if (!_matched) {
      if (expression instanceof VariableDeclarationExpression) {
        _matched=true;
        List _fragments = ((VariableDeclarationExpression)expression).fragments();
        final Consumer<Object> _function = (Object it) -> {
          Expression _initializer = ((VariableDeclarationFragment) it).getInitializer();
          JavaASTExpressionEvaluation.evaluate(_initializer, sourceNode, graph, dataTypePatterns);
        };
        _fragments.forEach(_function);
      }
    }
    if (!_matched) {
      Class<? extends Expression> _class = expression.getClass();
      String _plus = ("Expressions of type " + _class);
      String _plus_1 = (_plus + " are not supported. [");
      String _plus_2 = (_plus_1 + expression);
      String _plus_3 = (_plus_2 + "]");
      throw new UnsupportedOperationException(_plus_3);
    }
  }
}
