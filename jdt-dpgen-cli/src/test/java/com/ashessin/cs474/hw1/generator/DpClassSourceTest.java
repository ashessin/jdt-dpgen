package com.ashessin.cs474.hw1.generator;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.parser.DpSourceParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

public class DpClassSourceTest {

	private static final Logger log = (Logger) LoggerFactory.getLogger(DpSourceParser.class);

	/**
	 * Test non abstract class with abstract methods.
	 * <p>
	 * It is a compile-time error if a normal class that is not abstract
	 * has an abstract method.
	 * <p>
	 * _JLS 8.1.1.1. abstract Classes_
	 */
	@Test
	public void classWithAbstractMethods() {
		Assertions.assertThrows(IllegalStateException.class, () ->
			DpClassSource.newBuilder("com.test", "TestClass")
				.addMethod(DpSourceMethod.newBuilder()
					.addModifier(DpSourceMethod.Modifier.ABSTRACT)
					.build())
				.build());
		Assertions.assertDoesNotThrow(() ->
			DpClassSource.newBuilder("com.test", "TestClass")
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addMethod(DpSourceMethod.newBuilder()
					.addModifier(DpSourceMethod.Modifier.ABSTRACT)
					.build())
				.build());
		Assertions.assertDoesNotThrow(() ->
			DpClassSource.newBuilder("com.test", "TestClass")
				.addMethod(DpSourceMethod.newBuilder()
					.build())
				.build());
	}

	/**
	 * Test class with duplicate fields.
	 * <p>
	 * It is a compile-time error for the body of a class declaration to
	 * declare two fields with the same name.
	 * <p>
	 * _JLS 8.3. Field Declarations_
	 */
	@Test
	public void classWithDuplicateFields() {
		Assertions.assertThrows(IllegalStateException.class, () ->
			DpClassSource.newBuilder("com.test", "TestClass")
				.addField(DpSourceField.newBuilder("field", "Object")
					.build())
				.addField(DpSourceField.newBuilder("field", "Object")
					.build())
				.addField(DpSourceField.newBuilder("field3", "Object")
					.build())
				.build());
		Assertions.assertDoesNotThrow(() ->
			DpClassSource.newBuilder("com.test", "TestClass")
				.addField(DpSourceField.newBuilder("field1", "Object")
					.build())
				.addField(DpSourceField.newBuilder("field2", "Object")
					.build())
				.addField(DpSourceField.newBuilder("field3", "Object")
					.build())
				.build());
	}
}