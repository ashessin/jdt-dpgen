package com.ashessin.cs474.hw1.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DpInterfaceSourceTest {

	/**
	 * Test for Interface with private/protected methods.
	 * <p>
	 * It is a compile-time error if a member type declaration in an
	 * interface has the modifier protected or private.
	 * <p>
	 * _JLS 9.5. Member Type Declarations_
	 */
	@Test
	public void interfaceWithPrivateMethod() {
		Assertions.assertThrows(IllegalStateException.class, () ->
				DpInterfaceSource.newBuilder("com.test", "TestInterface")
						.addMethod(DpSourceMethod.newBuilder()
								.setAccessModifier(DpSourceMethod.AccessModifier.PRIVATE)
								.build())
						.build());
		Assertions.assertThrows(IllegalStateException.class, () ->
				DpInterfaceSource.newBuilder("com.test", "TestInterface")
						.addMethod(DpSourceMethod.newBuilder()
								.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
								.build())
						.build());
	}
}