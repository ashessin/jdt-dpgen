package com.ashessin.cs474.hw1.generator.creational;

import com.ashessin.cs474.hw1.generator.*;

public class SingletonGen extends DesignPatternGen {

	private String packageName;
	private String singletonName;
	private String singletonInstanceFieldName;
	private String singletonAccessorMethodName;

	public SingletonGen(String packageName,
						String singletonName, String singletonInstanceFieldName, String singletonAccessorMethodName) {
		this.packageName = packageName;
		this.singletonName = singletonName;
		this.singletonInstanceFieldName = singletonInstanceFieldName;
		this.singletonAccessorMethodName = singletonAccessorMethodName;
	}

	public DpArrayList<DpSource> main() {

		DpClassSource singleton = DpClassSource.newBuilder(packageName, singletonName)
				.setJavadoc("Singleton class implements singleton pattern. Only one object can be " +
							"instantiated.")
				.addField(DpSourceField.newBuilder(singletonInstanceFieldName, singletonName)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.addModifier(DpSourceField.Modifier.STATIC)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.setAccessModifier(DpSourceMethod.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(singletonAccessorMethodName)
						.addModifier(DpSourceMethod.Modifier.STATIC)
						.addModifier(DpSourceMethod.Modifier.SYNCHRONIZED)
						.setReturnType(singletonName)
						.setBody(String.format(
								"if (%s == null) {\n" +
								"	%s = new %s();\n" +
								"}\n" +
								"return %s;",
								singletonInstanceFieldName,
								singletonInstanceFieldName, singletonName,
								singletonInstanceFieldName))
						.build())
				.build();
		dpSources.add(singleton, this.getClass());

		return dpSources;
	}
}
