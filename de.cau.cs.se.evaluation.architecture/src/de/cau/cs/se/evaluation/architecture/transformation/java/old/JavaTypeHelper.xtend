package de.cau.cs.se.evaluation.architecture.transformation.java.old

import java.util.List
import org.eclipse.jdt.core.IType

class JavaTypeHelper {
	
	/**
	 * Add a list of new types to a list of types if they do not already occur in the list.
	 */
	def List<IType> addUnique(List<IType> types, List<IType> addTypes) {
		addTypes?.forEach[type | types.addUnique(type)]
		return types
	}
	
	/**
	 * Check if a type is already part of the type list. If not add it.
	 */
	def List<IType> addUnique(List<IType> types, IType type) {
		if (type != null) {
			if (types.findFirst[it.equals(type)] == null) {
				types.add(type)
			}
		}
		return types
	}

}