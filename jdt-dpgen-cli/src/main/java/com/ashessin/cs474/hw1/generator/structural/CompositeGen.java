package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.*;

public class CompositeGen extends DesignPatternGen {

	private static final String OPERATION = "operation";
	private static final String CHILDREN = "children";
	private static final String CHILD = "Child";
	private static final String INDEX = "index";
	private static final String ADD = "add";
	private static final String REMOVE = "remove";
	private String packageName;
	private String componentName;
	private String compositeName;
	private String leafName;


	public CompositeGen(String packageName,
						String componentName, String compositeName, String leafName) {
		this.packageName = packageName;
		this.componentName = componentName;
		this.compositeName = compositeName;
		this.leafName = leafName;
	}

	public DpArrayList<DpSource> main() {

		DpClassSource component = DpClassSource.newBuilder(packageName, componentName)
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(OPERATION)
						.addModifier(DpSourceMethod.Modifier.ABSTRACT)
						.build())
				.build();

		dpSources.add(component, this.getClass());

		DpClassSource leaf = DpClassSource.newBuilder(packageName, leafName)
				.setExtendsClass(componentName)
				.addMethod(DpSourceMethod.newBuilder(component.getMethods().get(0))
						.setBody(";")
						.build())
				.build();

		dpSources.add(leaf, this.getClass());

		DpClassSource composite = DpClassSource.newBuilder(packageName, compositeName)
				.setExtendsClass(componentName)
				.addField(DpSourceField.newBuilder(CHILDREN,
						String.format("java.util.List<%s>", componentName),
						"new java.util.ArrayList<>()")
						.build())
				.addMethod(DpSourceMethod.newBuilder(component.getMethods().get(0))
						.setBody(String.format("%s.forEach(%s::%s);", CHILDREN, componentName, OPERATION))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(ADD)
						.addParameter(componentName.toLowerCase(), componentName)
						.setBody(String.format("%s.add(%s);",
								CHILDREN, componentName.toLowerCase()))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(REMOVE)
						.addParameter(componentName.toLowerCase(), componentName)
						.setBody(String.format("%s.remove(%s);",
								CHILDREN, componentName.toLowerCase()))
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName("get" + CHILD)
						.addParameter(INDEX, "int")
						.setReturnType(componentName)
						.setBody(String.format("return %s.get(%s);",
								CHILDREN, INDEX))
						.build())
				.build();

		dpSources.add(composite, this.getClass());

		return dpSources;
	}
}