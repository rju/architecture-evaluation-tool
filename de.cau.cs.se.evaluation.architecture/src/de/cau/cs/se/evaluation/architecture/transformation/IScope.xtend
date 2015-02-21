package de.cau.cs.se.evaluation.architecture.transformation

import org.eclipse.jdt.core.IType

interface IScope {
	/**
	 * Returns type if the type is present in the scope else null.
	 */
	public def IType getType(String name)
}