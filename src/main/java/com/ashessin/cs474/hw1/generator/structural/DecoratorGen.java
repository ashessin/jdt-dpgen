package com.ashessin.cs474.hw1.generator.structural;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DecoratorGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(DecoratorGen.class);
	private static final String OPERATION = "operation";
	private static final String SPECIAL = "Special";
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

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpInterfaceSource component = DpInterfaceSource.newBuilder(packageName, componentName)
			.addMethod(DpSourceMethod.newBuilder()
				.setName(OPERATION)
				.build())
			.build();

		dpSources.add(component, this.getClass());

		DpClassSource concreteComponent = DpClassSource.newBuilder(packageName, componentName)
			.addImplementsInterface(componentName)
			.addMethod(DpSourceMethod.newBuilder(component.getMethods().get(0))
				.setBody(";")
				.build())
			.build();

		dpSources.add(concreteComponent, this.getClass());

		DpClassSource decorator = DpClassSource.newBuilder(packageName, decoratorName)
			.addImplementsInterface(componentName)
			.setModifier(DpClassSource.Modifier.ABSTRACT)
			.addMethod(DpSourceMethod.newBuilder(component.getMethods().get(0))
				.addModifier(DpSourceMethod.Modifier.ABSTRACT)
				.build())
			.build();

		dpSources.add(decorator, this.getClass());

		return dpSources;
	}
}