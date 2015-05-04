package de.cau.cs.se.evaluation.architecture.transformation

import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.ArrayType
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.ConstructorInvocation
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.ParameterizedType
import org.eclipse.jdt.core.dom.PrimitiveType
import org.eclipse.jdt.core.dom.QualifiedType
import org.eclipse.jdt.core.dom.SimpleType
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.jdt.core.dom.SuperConstructorInvocation
import org.eclipse.jdt.core.dom.Type
import org.eclipse.jdt.core.dom.UnionType
import org.eclipse.jdt.core.dom.VariableDeclarationFragment
import org.eclipse.jdt.core.dom.WildcardType

class NameResolutionHelper {
	/**
	 * Fully qualified name of a property of a class
	 */
	def static determineFullyQualifiedName(AbstractTypeDeclaration clazz, VariableDeclarationFragment fragment) {
		clazz.determineFullyQualifiedName + "." + fragment.name.fullyQualifiedName
	}
	
	/**
	 * Fully qualified name of a class specified by a class declaration
	 */
	// TODO make private again
	def static determineFullyQualifiedName(AbstractTypeDeclaration clazz) {
		val name = (clazz.parent as CompilationUnit).package.name.fullyQualifiedName + "." + clazz.name.fullyQualifiedName
		return name
	}
	
	/**
	 * Fully qualified name of a class specified by a type binding
	 */
	def static determineFullyQualifiedName(ITypeBinding clazz) {
		val name = clazz.package.name + "." + clazz.name
		return name
	}
	
	/**
	 * Fully qualified name of a method based on the method binding.
	 */
	def static determineFullyQualifiedName(IMethodBinding binding) {
		val name = binding.declaringClass.determineFullyQualifiedName + "." + binding.name
		return name
	}
	

	/**
	 * Determine the name of a calling method.
	 */
	def static determineFullyQualifiedName(AbstractTypeDeclaration clazz, MethodDeclaration method) {
		val result = clazz.determineFullyQualifiedName + "." + method.name.fullyQualifiedName + "(" + 
			method.parameters.map[(it as SingleVariableDeclaration).type.determineFullyQualifiedName].join(',') + ") : " +
			method.returnType2.determineFullyQualifiedName
		if (method.thrownExceptionTypes.size > 0) 
			return result + " throw " + method.thrownExceptionTypes.map[(it as Type).determineFullyQualifiedName].join(",")
		else
			return result
	}

	/**
	 * Determine the name of a called method.
	 */
	def static determineFullyQualifiedName(AbstractTypeDeclaration clazz, MethodInvocation callee) {
		val result = clazz.determineFullyQualifiedName + "." + callee.name.fullyQualifiedName + "(" + 
			callee.resolveMethodBinding.parameterTypes.map[it.qualifiedName].join(",") + ") : " +
			callee.resolveMethodBinding.returnType.qualifiedName
		if (callee.resolveMethodBinding.exceptionTypes.length > 0)
			return result + " throw " + callee.resolveMethodBinding.exceptionTypes.map[it.qualifiedName].join(",")
		else
			return result
	}
	
	/**
	 * Determine the name of a called constructor.
	 */
	def static determineFullyQualifiedName(AbstractTypeDeclaration clazz, ConstructorInvocation callee) {
		val result = clazz.determineFullyQualifiedName + "." + clazz.name.fullyQualifiedName + "(" + 
			callee.resolveConstructorBinding.parameterTypes.map[it.qualifiedName].join(",") + ") "
		if (callee.resolveConstructorBinding.exceptionTypes.length > 0)
			return result + " throw " + callee.resolveConstructorBinding.exceptionTypes.map[it.qualifiedName].join(",")
		else
			return result
	}
	
	/**
	 * Determine the name of a called constructor.
	 */
	def static determineFullyQualifiedName(AbstractTypeDeclaration clazz, SuperConstructorInvocation callee) {
		val result = clazz.determineFullyQualifiedName + "." + clazz.name.fullyQualifiedName + "(" + 
			callee.resolveConstructorBinding.parameterTypes.map[it.qualifiedName].join(",") + ") "
		if (callee.resolveConstructorBinding.exceptionTypes.length > 0)
			return result + " throw " + callee.resolveConstructorBinding.exceptionTypes.map[it.qualifiedName].join(",")
		else
			return result
	}
	
	/**
	 * Determine the fully qualified name for a variable binding.
	 */
	def static determineFullyQualifiedName(IVariableBinding variableBinding) {
		if (variableBinding.declaringMethod != null)
			variableBinding.declaringMethod.determineFullyQualifiedName + "." + variableBinding.name
		else
			variableBinding.declaringClass.determineFullyQualifiedName + "." + variableBinding.name
	}
	
	/**
	 * Fully qualified name of a type.
	 */
	def static String determineFullyQualifiedName(Type type) {
		switch (type) {
			ArrayType:
				return type.elementType.determineFullyQualifiedName() + "[]"
			ParameterizedType:
				return type.type.determineFullyQualifiedName() + "<" + type.typeArguments.map[(it as Type).determineFullyQualifiedName].join(",") + ">"
			PrimitiveType: /** primitive types are all data types */
				return switch (type.primitiveTypeCode) {
					case PrimitiveType.BOOLEAN: "boolean"
					case PrimitiveType.BYTE: "byte"
					case PrimitiveType.CHAR: "char"
					case PrimitiveType.DOUBLE: "double"
					case PrimitiveType.FLOAT: "float"
					case PrimitiveType.INT: "int"
					case PrimitiveType.LONG: "long"
					case PrimitiveType.SHORT: "short"
					case PrimitiveType.VOID: "void"
				}
			QualifiedType:
				return if (type.name != null && type.qualifier != null)
					type.name.fullyQualifiedName + "." + type.qualifier.determineFullyQualifiedName
				else if (type.name != null)
					type.name.fullyQualifiedName
				else
					type.qualifier.determineFullyQualifiedName
			SimpleType:
				return type.name.fullyQualifiedName
			UnionType:
				return type.types.map[(it as Type).determineFullyQualifiedName].join("+")
			WildcardType:
				if (type.upperBound)
					return "? extends " + type.bound.determineFullyQualifiedName
				else
					return "? super " + type.bound.determineFullyQualifiedName
			default:
				return "ERROR"
		}
	}
}