package com.ashessin.cs474.hw1.parser;

import com.ashessin.cs474.hw1.generator.DpClassSource;
import com.ashessin.cs474.hw1.generator.DpInterfaceSource;
import com.ashessin.cs474.hw1.generator.DpSource;

/**
 * The JavaParser implementation for parsing custom DpSource classes.
 *
 * <a target="_blank" href="https://github.com/javaparser/javaparser">JavaParser</a>
 */
public class DpSourceParserJavaparser extends DpSourceParser {

	/**
	 * @param dpSource custom top level, named <i>design pattern</i> reference type
	 *
	 * @return
	 */
	@Override
	public byte[] serialize(final DpSource dpSource) {
		return new byte[0];
	}

	/**
	 * @param dpClassSource top level, named <i>design pattern</i> class
	 *
	 * @return
	 */
	@Override
	protected String makeSource(final DpClassSource dpClassSource) {
		return null;
	}

	/**
	 * @param dpInterfaceSource top level, named <i>design pattern</i> interface
	 *
	 * @return
	 */
	@Override
	protected String makeSource(final DpInterfaceSource dpInterfaceSource) {
		return null;
	}

}
