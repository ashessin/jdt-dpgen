package com.ashessin.cs474.hw1.parser;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.DpClassSource;
import com.ashessin.cs474.hw1.generator.DpInterfaceSource;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.LoggerFactory;

/**
 * Abstract factory for Java parsers.
 */
public abstract class DpSourceParser {

	private static final Logger log = (Logger) LoggerFactory.getLogger(DpSourceParser.class);

	public static DpSourceParser getParser(Backend parserBackend) {
		log.info("Initializing backend parser.");
		switch (parserBackend) {
			case JAVAPARSE:
				return new DpSourceParserJavaparser();
			case JAVAPOET:
				return new DpSourceParserJavapoet();
			default:
				return new DpSourceParserRoaster();
		}
	}

	/**
	 * Serializes custom top level, named <i>design pattern</i> reference type
	 * to a actual Java top level, named java reference type (class/interface).
	 *
	 * @param dpSource custom top level, named <i>design pattern</i> reference type
	 *
	 * @return top level, named java reference type (class/interface)
	 */
	public abstract byte[] serialize(final DpSource dpSource);

	/**
	 * Serializes custom top level, named <i>design pattern</i> class reference type
	 * to a actual Java top level, named java class reference type.
	 *
	 * @param dpClassSource top level, named <i>design pattern</i> class
	 *
	 * @return top level, named java class reference type
	 */
	protected abstract String makeSource(final DpClassSource dpClassSource);

	/**
	 * Serializes custom top level, named <i>design pattern</i> interface reference type
	 * to a actual Java top level, named java interface reference type.
	 *
	 * @param dpInterfaceSource top level, named <i>design pattern</i> interface
	 *
	 * @return top level, named java interface reference type
	 */
	protected abstract String makeSource(final DpInterfaceSource dpInterfaceSource);

	/**
	 * List of possible parsers supported by the factory
	 * <p>
	 * Note: As of now only {@link Backend#ROASTER} is  implemented.
	 */
	public enum Backend {
		JAVAPARSE, JAVAPOET, ROASTER
	}
}
