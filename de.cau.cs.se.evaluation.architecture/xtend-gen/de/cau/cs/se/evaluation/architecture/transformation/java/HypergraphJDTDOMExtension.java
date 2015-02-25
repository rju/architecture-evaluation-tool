package de.cau.cs.se.evaluation.architecture.transformation.java;

import java.util.Hashtable;
import java.util.List;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class HypergraphJDTDOMExtension {
  /**
   * Get compilation unit for JDT type.
   */
  public static CompilationUnit getUnitForType(final IType type) {
    try {
      final ASTParser parser = ASTParser.newParser(AST.JLS8);
      final Hashtable options = JavaCore.getOptions();
      JavaCore.setComplianceOptions(JavaCore.VERSION_1_6, options);
      parser.setCompilerOptions(options);
      ICompilationUnit _compilationUnit = type.getCompilationUnit();
      IBuffer _buffer = _compilationUnit.getBuffer();
      String _contents = _buffer.getContents();
      char[] _charArray = _contents.toCharArray();
      parser.setSource(_charArray);
      ASTNode _createAST = parser.createAST(null);
      return ((CompilationUnit) _createAST);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static TypeDeclaration getTypeDeclarationForType(final IType type) {
    CompilationUnit _unitForType = HypergraphJDTDOMExtension.getUnitForType(type);
    List _types = _unitForType.types();
    final Object object = _types.get(0);
    if ((object instanceof TypeDeclaration)) {
      return ((TypeDeclaration) object);
    } else {
      return null;
    }
  }
}
