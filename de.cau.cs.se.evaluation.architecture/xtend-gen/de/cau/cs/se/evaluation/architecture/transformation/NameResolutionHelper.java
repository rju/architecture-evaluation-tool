package de.cau.cs.se.evaluation.architecture.transformation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

@SuppressWarnings("all")
public class NameResolutionHelper {
  public static String determineFullQualifiedName(final AbstractTypeDeclaration clazz, final MethodDeclaration method) {
    String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(clazz);
    String _plus = (_determineFullQualifiedName + ".");
    SimpleName _name = method.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    return (_plus + _fullyQualifiedName);
  }
  
  public static String determineFullQualifiedName(final AbstractTypeDeclaration clazz, final VariableDeclarationFragment fragment) {
    String _determineFullQualifiedName = NameResolutionHelper.determineFullQualifiedName(clazz);
    String _plus = (_determineFullQualifiedName + ".");
    SimpleName _name = fragment.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    return (_plus + _fullyQualifiedName);
  }
  
  public static String determineFullQualifiedName(final AbstractTypeDeclaration clazz) {
    ASTNode _parent = clazz.getParent();
    PackageDeclaration _package = ((CompilationUnit) _parent).getPackage();
    Name _name = _package.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    String _plus = (_fullyQualifiedName + ".");
    SimpleName _name_1 = clazz.getName();
    String _fullyQualifiedName_1 = _name_1.getFullyQualifiedName();
    final String name = (_plus + _fullyQualifiedName_1);
    return name;
  }
}
