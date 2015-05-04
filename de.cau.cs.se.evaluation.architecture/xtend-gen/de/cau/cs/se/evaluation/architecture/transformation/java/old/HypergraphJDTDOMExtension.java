package de.cau.cs.se.evaluation.architecture.transformation.java.old;

import java.util.Hashtable;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class HypergraphJDTDOMExtension {
  /**
   * Get compilation unit for JDT type.
   */
  public static CompilationUnit getUnitForType(final IType type, final IProgressMonitor monitor, final IJavaProject project) {
    try {
      final Hashtable options = JavaCore.getOptions();
      JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
      final ASTParser parser = ASTParser.newParser(AST.JLS8);
      parser.setProject(project);
      parser.setCompilerOptions(options);
      parser.setKind(ASTParser.K_COMPILATION_UNIT);
      ICompilationUnit _compilationUnit = type.getCompilationUnit();
      IBuffer _buffer = _compilationUnit.getBuffer();
      String _contents = _buffer.getContents();
      char[] _charArray = _contents.toCharArray();
      parser.setSource(_charArray);
      ICompilationUnit _compilationUnit_1 = type.getCompilationUnit();
      String _elementName = _compilationUnit_1.getElementName();
      parser.setUnitName(_elementName);
      parser.setResolveBindings(true);
      parser.setBindingsRecovery(true);
      parser.setStatementsRecovery(true);
      ASTNode _createAST = parser.createAST(null);
      return ((CompilationUnit) _createAST);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static AbstractTypeDeclaration getTypeDeclarationForType(final IType type, final IProgressMonitor monitor, final IJavaProject project) {
    CompilationUnit _unitForType = HypergraphJDTDOMExtension.getUnitForType(type, monitor, project);
    List _types = _unitForType.types();
    final Object object = _types.get(0);
    if ((object instanceof AbstractTypeDeclaration)) {
      return ((AbstractTypeDeclaration) object);
    } else {
      return null;
    }
  }
}
