package com.ashessin.cs474.hw1.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DpSourceFieldTest {

	/**
	 * Test field with duplicate modifiers.
	 * <p>
	 * It is a compile-time error if the same keyword appears more than
	 * once as a modifier for a field declaration.
	 * _JLS 8.4.3. Method Modifiers_
	 * _JLS 9.4. Method Declarations_
	 */
	@Test
	public void fieldWithDuplicateModifiers() {
		Assertions.assertThrows(IllegalStateException.class, () ->
				DpSourceField.newBuilder("testField", "Object")
						.addModifier(DpSourceField.Modifier.FINAL)
						.addModifier(DpSourceField.Modifier.FINAL)
						.addModifier(DpSourceField.Modifier.STATIC)
						.build());
		Assertions.assertDoesNotThrow(() ->
				DpSourceField.newBuilder("testField", "Object")
						.addModifier(DpSourceField.Modifier.FINAL)
						.addModifier(DpSourceField.Modifier.NONE)
						.build());
		Assertions.assertDoesNotThrow(() ->
				DpSourceField.newBuilder("testField", "Object")
						.addModifier(DpSourceField.Modifier.STATIC)
						.addModifier(DpSourceField.Modifier.NONE)
						.build());
		Assertions.assertDoesNotThrow(() ->
				DpSourceField.newBuilder("testField", "Object")
						.addModifier(DpSourceField.Modifier.FINAL)
						.addModifier(DpSourceField.Modifier.STATIC)
						.build());
		Assertions.assertDoesNotThrow(() ->
				DpSourceField.newBuilder("testField", "Object")
						.addModifier(DpSourceField.Modifier.NONE)
						.build());
		Assertions.assertDoesNotThrow(() ->
				DpSourceField.newBuilder("testField", "Object")
						.build());
	}
}