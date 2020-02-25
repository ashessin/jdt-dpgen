package com.ashessin.cs474.hw1.generator;

/**
 * An interface that all top level, named <i>design pattern</i> reference types must implement.
 * <p>
 * Reference types include class types and interface types.
 * A top level type declaration declares a top level class type or a top level
 * interface type.
 */
public interface DpSource {

	/**
	 * @return fully qualified named package/subpackage name that the top level
	 * named <i>design pattern</i> reference type belongs to
	 */
	String getPackageName();

	/**
	 * @return identifier of the top level named <i>design pattern</i> reference
	 * type
	 */
	String getName();

	/**
	 * @return javadoc comments of the top level named <i>design pattern</i>
	 * reference type
	 */
	String getJavadoc();
}
