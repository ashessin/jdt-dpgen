package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ChainOfResponsibilityGen extends DesignPatternGen {

	private static final Logger log = LoggerFactory.getLogger(ChainOfResponsibilityGen.class);
	private static final String HANDLE_REQUEST = "handleRequest";
	private static final String INVOKED = "Invoked";
	private String packageName;
	private String handlerName;
	private String handlerFieldName;
	private List<String> concreteHandlerNames;

	public ChainOfResponsibilityGen(String packageName,
									String handlerName, String handlerFieldName, List<String> concreteHandlerNames) {
		this.packageName = packageName;
		this.handlerName = handlerName;
		this.handlerFieldName = handlerFieldName;
		this.concreteHandlerNames = concreteHandlerNames;
	}

	public DpArrayList<DpSource> main() {
		DpClassSource handler = DpClassSource.newBuilder(packageName, handlerName)
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addField(DpSourceField.newBuilder(handlerFieldName, handlerName)
						.setAccessModifier(DpSourceField.AccessModifier.PROTECTED)
						.setSetter(Boolean.TRUE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(HANDLE_REQUEST)
						.addModifier(DpSourceMethod.Modifier.ABSTRACT)
						.build())
				.build();

		dpSources.add(handler, this.getClass());

		List<DpClassSource> concreteHandlers = concreteHandlerNames.stream()
				.map(concreteHandlerName -> DpClassSource.newBuilder(packageName, concreteHandlerName)
						.setExtendsClass(handlerName)
						.addField(DpSourceField.newBuilder(HANDLE_REQUEST + INVOKED, "boolean", "false")
								.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
								.build())
						.addMethod(DpSourceMethod.newBuilder()
								.setReturnType("boolean")
								.setName("is" + HANDLE_REQUEST.substring(0, 1).toUpperCase()
										 + HANDLE_REQUEST.substring(1))
								.setBody(String.format("return %s;", HANDLE_REQUEST + INVOKED))
								.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
								.build())
						.addMethod(DpSourceMethod.newBuilder(handler.getMethods().get(0))
								.setBody(String.format(
										"%s = true;\n" +
										"\n" +
										"if (%s) {\n" +
										"	%s.%s();\n" +
										"}",
										HANDLE_REQUEST + INVOKED,
										HANDLE_REQUEST + INVOKED,
										handlerFieldName, HANDLE_REQUEST))
								.build())
						.build()).collect(Collectors.toList());

		dpSources.add(concreteHandlers, this.getClass());

		return dpSources;
	}
}