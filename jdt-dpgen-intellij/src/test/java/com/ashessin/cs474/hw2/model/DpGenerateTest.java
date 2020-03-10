package com.ashessin.cs474.hw2.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DpGenerateTest {

	@Test
	void checkInstance() {
		assertEquals(DpGenerate.INSTANCE.hashCode(), DpGenerate.INSTANCE.hashCode());
	}
}