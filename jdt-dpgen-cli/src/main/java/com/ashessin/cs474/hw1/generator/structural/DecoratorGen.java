package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.*;

import java.util.List;
import java.util.stream.Collectors;

public class DecoratorGen extends DesignPatternGen {

	private static final String OPERATION = "operation";

	private String packageName;
	private String decoratorName;
	private List<String> concreteDecoratorNames;
	private String componentName;
	private String concreteComponentName;

	public DecoratorGen(String packageName,
						String decoratorName, List<String> concreteDecoratorNames,
						String componentName, String concreteComponentName) {
		this.packageName = packageName;
		this.decoratorName = decoratorName;
		this.concreteDecoratorNames = concreteDecoratorNames;
		this.componentName = componentName;
		this.concreteComponentName = concreteComponentName;
	}

	public DpArrayList<DpSource> main() {

		DpInterfaceSource component = DpInterfaceSource.newBuilder(packageName, componentName)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(OPERATION)
						.build())
				.build();

		dpSources.add(component, this.getClass());

		DpClassSource concreteComponent = DpClassSource.newBuilder(packageName, concreteComponentName)
				.addImplementsInterface(componentName)
				.addMethod(DpSourceMethod.newBuilder(component.getMethods().get(0))
						.setBody(";")
						.build())
				.build();

		dpSources.add(concreteComponent, this.getClass());

		DpClassSource decorator = DpClassSource.newBuilder(packageName, decoratorName)
				.addImplementsInterface(componentName)
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addField(DpSourceField.newBuilder(componentName.toLowerCase(), componentName)
						.setAccessModifier(DpSourceField.AccessModifier.PROTECTED)
						.setSetter(Boolean.TRUE)
						.build())
				.addMethod(DpSourceMethod.newBuilder(component.getMethods().get(0))
						.addModifier(DpSourceMethod.Modifier.ABSTRACT)
						.build())
				.build();

		dpSources.add(decorator, this.getClass());

		List<DpClassSource> concreteDecoratos = concreteDecoratorNames.stream()
				.map(concreteDecoratorName -> DpClassSource.newBuilder(packageName, concreteDecoratorName)
						.setExtendsClass(decoratorName)
						.addField(DpSourceField.newBuilder("state", "boolean")
								.setGetter(Boolean.TRUE)
								.build())
						.addMethod(DpSourceMethod.newBuilder(decorator.getMethods().get(0))
								.setBody(String.format("state = true;\n" +
													   "this.%s.%s();", componentName.toLowerCase(), OPERATION))
								.build())
						.build())
				.collect(Collectors.toList());

		dpSources.add(concreteDecoratos, this.getClass());

		return dpSources;
	}
}