/**
 * Copyright 2015
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cau.cs.se.software.evaluation.transformation.java;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;

@SuppressWarnings("all")
public class NameResolutionHelper {
  /**
   * Fully qualified name of a property of a class
   */
  public static String determineFullyQualifiedName(final AbstractTypeDeclaration clazz, final VariableDeclarationFragment fragment) {
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(clazz);
    String _plus = (_determineFullyQualifiedName + ".");
    SimpleName _name = fragment.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    return (_plus + _fullyQualifiedName);
  }
  
  /**
   * Fully qualified name of a class specified by a class declaration
   */
  public static String determineFullyQualifiedName(final AbstractTypeDeclaration clazz) {
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
  
  /**
   * Fully qualified name of a method based on the method binding.
   */
  public static String determineFullyQualifiedName(final IMethodBinding binding) {
    if ((binding.getDeclaringClass().isAnonymous() && binding.isConstructor())) {
      String _xifexpression = null;
      ITypeBinding _declaringClass = binding.getDeclaringClass();
      ITypeBinding[] _interfaces = _declaringClass.getInterfaces();
      boolean _notEquals = (!Objects.equal(_interfaces, null));
      if (_notEquals) {
        String _xifexpression_1 = null;
        ITypeBinding _declaringClass_1 = binding.getDeclaringClass();
        ITypeBinding[] _interfaces_1 = _declaringClass_1.getInterfaces();
        int _size = ((List<ITypeBinding>)Conversions.doWrapArray(_interfaces_1)).size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          ITypeBinding _declaringClass_2 = binding.getDeclaringClass();
          ITypeBinding[] _interfaces_2 = _declaringClass_2.getInterfaces();
          ITypeBinding _get = _interfaces_2[0];
          String _name = _get.getName();
          _xifexpression_1 = ("." + _name);
        } else {
          _xifexpression_1 = "";
        }
        _xifexpression = _xifexpression_1;
      } else {
        String _xifexpression_2 = null;
        ITypeBinding _declaringClass_3 = binding.getDeclaringClass();
        ITypeBinding _superclass = _declaringClass_3.getSuperclass();
        boolean _notEquals_1 = (!Objects.equal(_superclass, null));
        if (_notEquals_1) {
          ITypeBinding _declaringClass_4 = binding.getDeclaringClass();
          ITypeBinding _superclass_1 = _declaringClass_4.getSuperclass();
          _xifexpression_2 = ("." + _superclass_1);
        } else {
          _xifexpression_2 = "";
        }
        _xifexpression = _xifexpression_2;
      }
      final String typeName = _xifexpression;
      ITypeBinding _declaringClass_5 = binding.getDeclaringClass();
      String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_declaringClass_5);
      String _plus = (_determineFullyQualifiedName + ".");
      return (_plus + typeName);
    } else {
      ITypeBinding _declaringClass_6 = binding.getDeclaringClass();
      String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(_declaringClass_6);
      String _plus_1 = (_determineFullyQualifiedName_1 + ".");
      String _name_1 = binding.getName();
      return (_plus_1 + _name_1);
    }
  }
  
  /**
   * Determine the name of a calling method.
   */
  public static String determineFullyQualifiedName(final AbstractTypeDeclaration clazz, final MethodDeclaration method) {
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(clazz);
    String _plus = (_determineFullyQualifiedName + ".");
    SimpleName _name = method.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    String _plus_1 = (_plus + _fullyQualifiedName);
    String _plus_2 = (_plus_1 + "(");
    List _parameters = method.parameters();
    final Function1<Object, String> _function = (Object it) -> {
      Type _type = ((SingleVariableDeclaration) it).getType();
      return NameResolutionHelper.determineFullyQualifiedName(_type);
    };
    List<String> _map = ListExtensions.<Object, String>map(_parameters, _function);
    String _join = IterableExtensions.join(_map, ", ");
    String _plus_3 = (_plus_2 + _join);
    String _plus_4 = (_plus_3 + ") : ");
    Type _returnType2 = method.getReturnType2();
    String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(_returnType2);
    final String result = (_plus_4 + _determineFullyQualifiedName_1);
    List _thrownExceptionTypes = method.thrownExceptionTypes();
    int _size = _thrownExceptionTypes.size();
    boolean _greaterThan = (_size > 0);
    if (_greaterThan) {
      List _thrownExceptionTypes_1 = method.thrownExceptionTypes();
      final Function1<Object, String> _function_1 = (Object it) -> {
        return NameResolutionHelper.determineFullyQualifiedName(((Type) it));
      };
      List<String> _map_1 = ListExtensions.<Object, String>map(_thrownExceptionTypes_1, _function_1);
      String _join_1 = IterableExtensions.join(_map_1, ", ");
      return ((result + " throw ") + _join_1);
    } else {
      return result;
    }
  }
  
  /**
   * Determine the name of a called method.
   */
  public static String determineFullyQualifiedName(final AbstractTypeDeclaration clazz, final MethodInvocation callee) {
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(clazz);
    String _plus = (_determineFullyQualifiedName + ".");
    SimpleName _name = callee.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    String _plus_1 = (_plus + _fullyQualifiedName);
    String _plus_2 = (_plus_1 + "(");
    IMethodBinding _resolveMethodBinding = callee.resolveMethodBinding();
    ITypeBinding[] _parameterTypes = _resolveMethodBinding.getParameterTypes();
    final Function1<ITypeBinding, String> _function = (ITypeBinding it) -> {
      return it.getQualifiedName();
    };
    List<String> _map = ListExtensions.<ITypeBinding, String>map(((List<ITypeBinding>)Conversions.doWrapArray(_parameterTypes)), _function);
    String _join = IterableExtensions.join(_map, ", ");
    String _plus_3 = (_plus_2 + _join);
    String _plus_4 = (_plus_3 + ") : ");
    IMethodBinding _resolveMethodBinding_1 = callee.resolveMethodBinding();
    ITypeBinding _returnType = _resolveMethodBinding_1.getReturnType();
    String _qualifiedName = _returnType.getQualifiedName();
    final String result = (_plus_4 + _qualifiedName);
    IMethodBinding _resolveMethodBinding_2 = callee.resolveMethodBinding();
    ITypeBinding[] _exceptionTypes = _resolveMethodBinding_2.getExceptionTypes();
    int _length = _exceptionTypes.length;
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      IMethodBinding _resolveMethodBinding_3 = callee.resolveMethodBinding();
      ITypeBinding[] _exceptionTypes_1 = _resolveMethodBinding_3.getExceptionTypes();
      final Function1<ITypeBinding, String> _function_1 = (ITypeBinding it) -> {
        return it.getQualifiedName();
      };
      List<String> _map_1 = ListExtensions.<ITypeBinding, String>map(((List<ITypeBinding>)Conversions.doWrapArray(_exceptionTypes_1)), _function_1);
      String _join_1 = IterableExtensions.join(_map_1, ", ");
      return ((result + " throw ") + _join_1);
    } else {
      return result;
    }
  }
  
  /**
   * Determine the name of a called constructor.
   */
  public static String determineFullyQualifiedName(final AbstractTypeDeclaration clazz, final ConstructorInvocation callee) {
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(clazz);
    String _plus = (_determineFullyQualifiedName + ".");
    SimpleName _name = clazz.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    String _plus_1 = (_plus + _fullyQualifiedName);
    String _plus_2 = (_plus_1 + "(");
    IMethodBinding _resolveConstructorBinding = callee.resolveConstructorBinding();
    ITypeBinding[] _parameterTypes = _resolveConstructorBinding.getParameterTypes();
    final Function1<ITypeBinding, String> _function = (ITypeBinding it) -> {
      return it.getQualifiedName();
    };
    List<String> _map = ListExtensions.<ITypeBinding, String>map(((List<ITypeBinding>)Conversions.doWrapArray(_parameterTypes)), _function);
    String _join = IterableExtensions.join(_map, ", ");
    String _plus_3 = (_plus_2 + _join);
    final String result = (_plus_3 + ")");
    IMethodBinding _resolveConstructorBinding_1 = callee.resolveConstructorBinding();
    ITypeBinding[] _exceptionTypes = _resolveConstructorBinding_1.getExceptionTypes();
    int _length = _exceptionTypes.length;
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      IMethodBinding _resolveConstructorBinding_2 = callee.resolveConstructorBinding();
      ITypeBinding[] _exceptionTypes_1 = _resolveConstructorBinding_2.getExceptionTypes();
      final Function1<ITypeBinding, String> _function_1 = (ITypeBinding it) -> {
        return it.getQualifiedName();
      };
      List<String> _map_1 = ListExtensions.<ITypeBinding, String>map(((List<ITypeBinding>)Conversions.doWrapArray(_exceptionTypes_1)), _function_1);
      String _join_1 = IterableExtensions.join(_map_1, ", ");
      return ((result + " throw ") + _join_1);
    } else {
      return result;
    }
  }
  
  /**
   * Determine the name of a called constructor.
   */
  public static String determineFullyQualifiedName(final AbstractTypeDeclaration clazz, final SuperConstructorInvocation callee) {
    String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(clazz);
    String _plus = (_determineFullyQualifiedName + ".");
    SimpleName _name = clazz.getName();
    String _fullyQualifiedName = _name.getFullyQualifiedName();
    String _plus_1 = (_plus + _fullyQualifiedName);
    String _plus_2 = (_plus_1 + "(");
    IMethodBinding _resolveConstructorBinding = callee.resolveConstructorBinding();
    ITypeBinding[] _parameterTypes = _resolveConstructorBinding.getParameterTypes();
    final Function1<ITypeBinding, String> _function = (ITypeBinding it) -> {
      return it.getQualifiedName();
    };
    List<String> _map = ListExtensions.<ITypeBinding, String>map(((List<ITypeBinding>)Conversions.doWrapArray(_parameterTypes)), _function);
    String _join = IterableExtensions.join(_map, ", ");
    String _plus_3 = (_plus_2 + _join);
    final String result = (_plus_3 + ")");
    IMethodBinding _resolveConstructorBinding_1 = callee.resolveConstructorBinding();
    ITypeBinding[] _exceptionTypes = _resolveConstructorBinding_1.getExceptionTypes();
    int _length = _exceptionTypes.length;
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      IMethodBinding _resolveConstructorBinding_2 = callee.resolveConstructorBinding();
      ITypeBinding[] _exceptionTypes_1 = _resolveConstructorBinding_2.getExceptionTypes();
      final Function1<ITypeBinding, String> _function_1 = (ITypeBinding it) -> {
        return it.getQualifiedName();
      };
      List<String> _map_1 = ListExtensions.<ITypeBinding, String>map(((List<ITypeBinding>)Conversions.doWrapArray(_exceptionTypes_1)), _function_1);
      String _join_1 = IterableExtensions.join(_map_1, ", ");
      String _plus_4 = ((result + " throw ") + _join_1);
      return (_plus_4 + ";");
    } else {
      return result;
    }
  }
  
  /**
   * Determine the fully qualified name for a variable binding.
   */
  public static String determineFullyQualifiedName(final IVariableBinding variableBinding) {
    String _xifexpression = null;
    IMethodBinding _declaringMethod = variableBinding.getDeclaringMethod();
    boolean _notEquals = (!Objects.equal(_declaringMethod, null));
    if (_notEquals) {
      IMethodBinding _declaringMethod_1 = variableBinding.getDeclaringMethod();
      String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_declaringMethod_1);
      String _plus = (_determineFullyQualifiedName + ".");
      String _name = variableBinding.getName();
      _xifexpression = (_plus + _name);
    } else {
      ITypeBinding _declaringClass = variableBinding.getDeclaringClass();
      String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(_declaringClass);
      String _plus_1 = (_determineFullyQualifiedName_1 + ".");
      String _name_1 = variableBinding.getName();
      _xifexpression = (_plus_1 + _name_1);
    }
    return _xifexpression;
  }
  
  /**
   * Fully qualified name of a type.
   */
  public static String determineFullyQualifiedName(final Type type) {
    boolean _matched = false;
    if (type instanceof ArrayType) {
      _matched=true;
      Type _elementType = ((ArrayType)type).getElementType();
      String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_elementType);
      return (_determineFullyQualifiedName + "[]");
    }
    if (!_matched) {
      if (type instanceof ParameterizedType) {
        _matched=true;
        Type _type = ((ParameterizedType)type).getType();
        String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_type);
        String _plus = (_determineFullyQualifiedName + "?G");
        List _typeArguments = ((ParameterizedType)type).typeArguments();
        final Function1<Object, String> _function = (Object it) -> {
          return NameResolutionHelper.determineFullyQualifiedName(((Type) it));
        };
        List<String> _map = ListExtensions.<Object, String>map(_typeArguments, _function);
        String _join = IterableExtensions.join(_map, ", ");
        String _plus_1 = (_plus + _join);
        return (_plus_1 + ";");
      }
    }
    if (!_matched) {
      if (type instanceof PrimitiveType) {
        _matched=true;
        String _switchResult_1 = null;
        PrimitiveType.Code _primitiveTypeCode = ((PrimitiveType)type).getPrimitiveTypeCode();
        boolean _matched_1 = false;
        if (Objects.equal(_primitiveTypeCode, PrimitiveType.BOOLEAN)) {
          _matched_1=true;
          _switchResult_1 = "boolean";
        }
        if (!_matched_1) {
          if (Objects.equal(_primitiveTypeCode, PrimitiveType.BYTE)) {
            _matched_1=true;
            _switchResult_1 = "byte";
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_primitiveTypeCode, PrimitiveType.CHAR)) {
            _matched_1=true;
            _switchResult_1 = "char";
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_primitiveTypeCode, PrimitiveType.DOUBLE)) {
            _matched_1=true;
            _switchResult_1 = "double";
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_primitiveTypeCode, PrimitiveType.FLOAT)) {
            _matched_1=true;
            _switchResult_1 = "float";
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_primitiveTypeCode, PrimitiveType.INT)) {
            _matched_1=true;
            _switchResult_1 = "int";
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_primitiveTypeCode, PrimitiveType.LONG)) {
            _matched_1=true;
            _switchResult_1 = "long";
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_primitiveTypeCode, PrimitiveType.SHORT)) {
            _matched_1=true;
            _switchResult_1 = "short";
          }
        }
        if (!_matched_1) {
          if (Objects.equal(_primitiveTypeCode, PrimitiveType.VOID)) {
            _matched_1=true;
            _switchResult_1 = "void";
          }
        }
        return _switchResult_1;
      }
    }
    if (!_matched) {
      if (type instanceof QualifiedType) {
        _matched=true;
        String _xifexpression = null;
        if (((!Objects.equal(((QualifiedType)type).getName(), null)) && (!Objects.equal(((QualifiedType)type).getQualifier(), null)))) {
          SimpleName _name = ((QualifiedType)type).getName();
          String _fullyQualifiedName = _name.getFullyQualifiedName();
          String _plus = ("L" + _fullyQualifiedName);
          String _plus_1 = (_plus + ".");
          Type _qualifier = ((QualifiedType)type).getQualifier();
          String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_qualifier);
          String _plus_2 = (_plus_1 + _determineFullyQualifiedName);
          _xifexpression = (_plus_2 + ";");
        } else {
          String _xifexpression_1 = null;
          SimpleName _name_1 = ((QualifiedType)type).getName();
          boolean _notEquals = (!Objects.equal(_name_1, null));
          if (_notEquals) {
            SimpleName _name_2 = ((QualifiedType)type).getName();
            _xifexpression_1 = _name_2.getFullyQualifiedName();
          } else {
            Type _qualifier_1 = ((QualifiedType)type).getQualifier();
            _xifexpression_1 = NameResolutionHelper.determineFullyQualifiedName(_qualifier_1);
          }
          _xifexpression = _xifexpression_1;
        }
        return _xifexpression;
      }
    }
    if (!_matched) {
      if (type instanceof SimpleType) {
        _matched=true;
        Name _name = ((SimpleType)type).getName();
        return _name.getFullyQualifiedName();
      }
    }
    if (!_matched) {
      if (type instanceof UnionType) {
        _matched=true;
        List _types = ((UnionType)type).types();
        final Function1<Object, String> _function = (Object it) -> {
          return NameResolutionHelper.determineFullyQualifiedName(((Type) it));
        };
        List<String> _map = ListExtensions.<Object, String>map(_types, _function);
        return IterableExtensions.join(_map, "+");
      }
    }
    if (!_matched) {
      if (type instanceof WildcardType) {
        _matched=true;
        boolean _isUpperBound = ((WildcardType)type).isUpperBound();
        if (_isUpperBound) {
          Type _bound = ((WildcardType)type).getBound();
          String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_bound);
          String _plus = ("extends " + _determineFullyQualifiedName);
          return (_plus + ";");
        } else {
          Type _bound_1 = ((WildcardType)type).getBound();
          String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(_bound_1);
          String _plus_1 = ("super " + _determineFullyQualifiedName_1);
          return (_plus_1 + ";");
        }
      }
    }
    return "ERROR";
  }
  
  /**
   * Fully qualified name of a class specified by a type binding
   */
  public static String determineFullyQualifiedName(final ITypeBinding clazz) {
    try {
      boolean _isAnonymous = clazz.isAnonymous();
      if (_isAnonymous) {
        String _xifexpression = null;
        ITypeBinding[] _interfaces = clazz.getInterfaces();
        boolean _notEquals = (!Objects.equal(_interfaces, null));
        if (_notEquals) {
          String _xifexpression_1 = null;
          ITypeBinding[] _interfaces_1 = clazz.getInterfaces();
          int _size = ((List<ITypeBinding>)Conversions.doWrapArray(_interfaces_1)).size();
          boolean _greaterThan = (_size > 0);
          if (_greaterThan) {
            ITypeBinding[] _interfaces_2 = clazz.getInterfaces();
            ITypeBinding _get = _interfaces_2[0];
            String _name = _get.getName();
            _xifexpression_1 = ("." + _name);
          } else {
            _xifexpression_1 = "";
          }
          _xifexpression = _xifexpression_1;
        } else {
          String _xifexpression_2 = null;
          ITypeBinding _superclass = clazz.getSuperclass();
          boolean _notEquals_1 = (!Objects.equal(_superclass, null));
          if (_notEquals_1) {
            ITypeBinding _superclass_1 = clazz.getSuperclass();
            _xifexpression_2 = ("." + _superclass_1);
          } else {
            _xifexpression_2 = "";
          }
          _xifexpression = _xifexpression_2;
        }
        final String typeName = _xifexpression;
        ITypeBinding _declaringClass = clazz.getDeclaringClass();
        String _determineFullyQualifiedName = NameResolutionHelper.determineFullyQualifiedName(_declaringClass);
        String _plus = (_determineFullyQualifiedName + typeName);
        String _plus_1 = (_plus + "$");
        String _key = clazz.getKey();
        return (_plus_1 + _key);
      } else {
        boolean _isPrimitive = clazz.isPrimitive();
        if (_isPrimitive) {
          return clazz.getBinaryName();
        } else {
          boolean _isArray = clazz.isArray();
          if (_isArray) {
            ITypeBinding _elementType = clazz.getElementType();
            String _determineFullyQualifiedName_1 = NameResolutionHelper.determineFullyQualifiedName(_elementType);
            return (_determineFullyQualifiedName_1 + "[]");
          } else {
            boolean _isWildcardType = clazz.isWildcardType();
            if (_isWildcardType) {
              ITypeBinding _wildcard = clazz.getWildcard();
              boolean _isUpperbound = _wildcard.isUpperbound();
              if (_isUpperbound) {
                ITypeBinding _wildcard_1 = clazz.getWildcard();
                String _determineFullyQualifiedName_2 = NameResolutionHelper.determineFullyQualifiedName(_wildcard_1);
                String _plus_2 = ("extends " + _determineFullyQualifiedName_2);
                return (_plus_2 + ";");
              } else {
                ITypeBinding _wildcard_2 = clazz.getWildcard();
                String _determineFullyQualifiedName_3 = NameResolutionHelper.determineFullyQualifiedName(_wildcard_2);
                String _plus_3 = ("super " + _determineFullyQualifiedName_3);
                return (_plus_3 + ";");
              }
            } else {
              boolean _isTypeVariable = clazz.isTypeVariable();
              if (_isTypeVariable) {
                String _name_1 = clazz.getName();
                String _plus_4 = ("extends " + _name_1);
                return (_plus_4 + ";");
              } else {
                boolean _isParameterizedType = clazz.isParameterizedType();
                if (_isParameterizedType) {
                  IPackageBinding _package = clazz.getPackage();
                  String _name_2 = _package.getName();
                  String _plus_5 = (_name_2 + ".");
                  String _name_3 = clazz.getName();
                  return (_plus_5 + _name_3);
                } else {
                  boolean _notEquals_2 = (!Objects.equal(clazz, null));
                  if (_notEquals_2) {
                    IPackageBinding _package_1 = clazz.getPackage();
                    boolean _notEquals_3 = (!Objects.equal(_package_1, null));
                    if (_notEquals_3) {
                      IPackageBinding _package_2 = clazz.getPackage();
                      String _name_4 = _package_2.getName();
                      String _plus_6 = (_name_4 + ".");
                      String _name_5 = clazz.getName();
                      return (_plus_6 + _name_5);
                    } else {
                      throw new Exception("y");
                    }
                  } else {
                    throw new Exception("x");
                  }
                }
              }
            }
          }
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
