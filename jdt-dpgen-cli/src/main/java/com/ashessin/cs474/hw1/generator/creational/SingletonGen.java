package com.ashessin.cs474.hw1.generator.creational;

import com.ashessin.cs474.hw1.generator.*;

public class SingletonGen extends DesignPatternGen {

	private String packageName;
	private String singletonName;
	private String singletonInstanceName;
	private String singletonAccessorName;

	public SingletonGen(String packageName,
						String singletonName, String singletonInstanceName, String singletonAccessorName) {
		this.packageName = packageName;
		this.singletonName = singletonName;
		this.singletonInstanceName = singletonInstanceName;
		this.singletonAccessorName = singletonAccessorName;
	}

	public DpArrayList<DpSource> main() {

		DpClassSource singleton = DpClassSource.newBuilder(packageName, singletonName)
				.setJavadoc("Singleton class implements singleton pattern. Only one object can be " +
							"instantiated.")
				.addField(DpSourceField.newBuilder(singletonInstanceName, singletonName)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.addModifier(DpSourceField.Modifier.STATIC)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.setAccessModifier(DpSourceMethod.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(singletonAccessorName)
						.addModifier(DpSourceMethod.Modifier.STATIC)
						.addModifier(DpSourceMethod.Modifier.SYNCHRONIZED)
						.setReturnType(singletonName)
						.setBody(String.format(
								"if (%s == null) {\n" +
								"	%s = new %s();\n" +
								"}\n" +
								"return %s;",
								singletonInstanceName,
								singletonInstanceName, singletonName,
								singletonInstanceName))
						.build())
				.build();
		dpSources.add(singleton, this.getClass());

		return dpSources;
	}
}
