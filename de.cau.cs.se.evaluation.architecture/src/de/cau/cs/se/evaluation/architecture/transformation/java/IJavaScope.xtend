package de.cau.cs.se.evaluation.architecture.transformation.java

import org.eclipse.jdt.core.dom.Name
import org.eclipse.jdt.core.IType

interface IJavaScope {
	/**
	 * Returns type if the type is present in the scope else null.
	 */
	public def IType getType(Name name)
}