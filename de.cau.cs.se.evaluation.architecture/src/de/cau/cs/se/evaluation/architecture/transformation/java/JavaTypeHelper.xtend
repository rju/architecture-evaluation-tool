package de.cau.cs.se.evaluation.architecture.transformation.java

import java.util.List
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.dom.Name

class JavaTypeHelper {
	
	/**
	 * Add a list of new types to a list of types if they do not already occur in the list.
	 */
	def List<IType> addUnique(List<IType> types, List<IType> addTypes) {
		addTypes?.forEach[type | types.addUnique(type)]
		
		return types
	}
	
	/**
	 * Check if a typ is already part of the type list. If not add it.
	 */
	def List<IType> addUnique(List<IType> types, IType type) {
		if (types.findFirst[it.equals(type)] == null) {
			types.add(type)
		}
		
		return types
	}
	
	def IType resolveType(List<IJavaScope> scopes, Name name) {
		val matches = scopes.filter[it.getType(name) != null]
		if (matches.size>0)
			return matches.get(0).getType(name)
		else
			return null
	}
}