package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateMethodGen extends DesignPatternGen {

	private static final String TEMPLATE_METHOD = "templateMethod";
	private String packageName;
	private String abstractClassName;
	private List<String> concreteClassNames;
	private Map<String, String> primitiveMethods;

	public TemplateMethodGen(String packageName,
							 String abstractClassName, List<String> concreteClassNames,
							 Map<String, String> primitiveMethods) {
		this.packageName = packageName;
		this.abstractClassName = abstractClassName;
		this.concreteClassNames = concreteClassNames;
		this.primitiveMethods = primitiveMethods;
	}

	public DpArrayList<DpSource> main() {

		String templateMethodyBody = "";
		primitiveMethods.forEach((key, value) -> templateMethodyBody.concat(String.format("%s();", key)));

		DpClassSource abstractClass = DpClassSource.newBuilder(packageName, abstractClassName)
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(TEMPLATE_METHOD)
						.addModifier(DpSourceMethod.Modifier.ABSTRACT)
						.setBody(templateMethodyBody)
						.build())
				.build();

		dpSources.add(abstractClass, this.getClass());

		List<DpClassSource> concreteClasses = concreteClassNames.stream()
				.map(concreteClass -> DpClassSource.newBuilder(packageName, concreteClass)
						.addMethods(primitiveMethods.entrySet().stream()
								.map(entry -> DpSourceMethod.newBuilder()
										.setName(entry.getKey())
										.setReturnType(entry.getValue())
										.setBody(String.format("return \"%s\";", entry.getKey()))
										.build())
								.collect(Collectors.toList()))
						.build())
				.collect(Collectors.toList());

		dpSources.add(concreteClasses, this.getClass());

		return dpSources;
	}
}