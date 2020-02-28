package com.ashessin.cs474.hw1.parser;

import com.ashessin.cs474.hw1.generator.DpClassSource;
import com.ashessin.cs474.hw1.generator.DpInterfaceSource;
import com.ashessin.cs474.hw1.generator.DpSource;

/**
 * The JavaPoet implementation for parsing custom DpSource classes.
 *
 * <a target="_blank" href="https://github.com/square/javapoet">JavaPoet</a>
 */
public class DpSourceParserJavapoet extends DpSourceParser {

	@Override
	public byte[] serialize(final DpSource dpSource) {
		return new byte[0];
	}

	@Override
	protected String makeSource(final DpClassSource dpClassSource) {
		return null;
	}

	@Override
	protected String makeSource(final DpInterfaceSource dpInterfaceSource) {
		return null;
	}
}
