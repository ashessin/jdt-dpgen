package com.ashessin.cs474.hw1.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DpSourceMethodTest {

	/**
	 * Test method with duplicate modifiers.
	 * <p>
	 * It is a compile-time error if the same keyword appears more than
	 * once as a modifier for a method declaration.
	 * _JLS 8.3.1. Field Modifiers_
	 * _JLS 9.3. Field (Constant) Declarations_
	 */
	@Test
	public void fieldWithDuplicateModifiers() {
		Assertions.assertThrows(IllegalStateException.class, () ->
			DpSourceMethod.newBuilder()
				.setName("testMethod")
				.addModifier(DpSourceMethod.Modifier.FINAL)
				.addModifier(DpSourceMethod.Modifier.FINAL)
				.addModifier(DpSourceMethod.Modifier.STATIC)
				.build());
		Assertions.assertDoesNotThrow(() ->
			DpSourceMethod.newBuilder()
				.setName("testMethod")
				.addModifier(DpSourceMethod.Modifier.FINAL)
				.addModifier(DpSourceMethod.Modifier.NONE)
				.build());
		Assertions.assertDoesNotThrow(() ->
			DpSourceMethod.newBuilder()
				.setName("testMethod")
				.addModifier(DpSourceMethod.Modifier.STATIC)
				.addModifier(DpSourceMethod.Modifier.NONE)
				.build());
		Assertions.assertDoesNotThrow(() ->
			DpSourceMethod.newBuilder()
				.setName("testMethod")
				.addModifier(DpSourceMethod.Modifier.FINAL)
				.addModifier(DpSourceMethod.Modifier.STATIC)
				.build());
		Assertions.assertDoesNotThrow(() ->
			DpSourceMethod.newBuilder()
				.setName("testMethod")
				.addModifier(DpSourceMethod.Modifier.NONE)
				.build());
		Assertions.assertDoesNotThrow(() ->
			DpSourceMethod.newBuilder()
				.setName("testMethod")
				.build());
	}
}