package com.ashessin.cs474.hw1.parser;

import com.ashessin.cs474.hw1.generator.DpClassSource;
import com.ashessin.cs474.hw1.generator.DpSourceMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DpSourceParserRoasterTest {

	/**
	 * Verify simple serialization output using Roaster backend.
	 */
	@Test
	public void serializeHelloWorld() {
		String helloWorld = "package com.app;\n" +
							"public class HelloWorld {\n" +
							"\n" +
							"\tpublic static void main(String[] args) {\n" +
							"\t\tSystem.out.println(\"Hello World!\");\n" +
							"\t}\n" +
							"}";

		DpClassSource dpHelloWorld = DpClassSource.newBuilder("com.app", "HelloWorld")
			.addMethod(DpSourceMethod.newBuilder()
				.setName("main")
				.setAccessModifier(DpSourceMethod.AccessModifier.PUBLIC)
				.addModifier(DpSourceMethod.Modifier.STATIC)
				.addParameter("args", "String[]")
				.setBody("System.out.println(\"Hello World!\");")
				.build())
			.build();

		String roasterHelloWorld = new String(new DpSourceParserRoaster().serialize(dpHelloWorld));

		Assertions.assertEquals(helloWorld, roasterHelloWorld);
	}

	/**
	 * Verify syntax error checking.
	 */
	@Test
	public void checkMissingSemicolon() {
		DpClassSource classWithSyntaxError = DpClassSource.newBuilder("com.app", "SyntaxError")
			.addMethod(DpSourceMethod.newBuilder()
				.setName("main")
				.setAccessModifier(DpSourceMethod.AccessModifier.PUBLIC)
				.addModifier(DpSourceMethod.Modifier.STATIC)
				.addParameter("args", "String[]")
				// missing semicolon
				.setBody("System.out.println(\"Hello World!\")")
				.build())
			.build();

		String serialized = new String(new DpSourceParserRoaster().serialize(classWithSyntaxError));

		Assertions.assertTrue(serialized.isEmpty());
	}
}