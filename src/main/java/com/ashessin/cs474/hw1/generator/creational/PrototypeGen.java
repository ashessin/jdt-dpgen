package com.ashessin.cs474.hw1.generator.creational;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PrototypeGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(PrototypeGen.class);
	private static final String ABSTRACT = "Abstract";
	private static final String CONCRETE = "Concrete";
	private static final String ORIGINAL = "original";
	private static final String COPY = "copy";
	private String packageName;
	private String abstractPrototypeName;
	private String concretePrototypeName;
	// propertyName, propertyType
	private Map<String, String> propertiesMap;

	public PrototypeGen(String packageName,
						String abstractPrototypeName, String concretePrototypeName,
						Map<String, String> propertiesMap) {
		this.packageName = packageName;
		this.abstractPrototypeName = abstractPrototypeName;
		this.concretePrototypeName = concretePrototypeName;
		this.propertiesMap = propertiesMap;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpSourceMethod abstractCopyMethod = DpSourceMethod.newBuilder().setName(COPY).setReturnType(this.abstractPrototypeName).build();
		DpInterfaceSource abstractProptotype = DpInterfaceSource.newBuilder(this.packageName, this.abstractPrototypeName)
			.addMethod(abstractCopyMethod)
			.build();

		dpSources.add(abstractProptotype, this.getClass());


		List<DpSourceField> concretePrototypeProperties = propertiesMap.entrySet().stream()
			.map(entry -> DpSourceField.newBuilder(entry.getKey(), entry.getValue()).build()).collect(Collectors.toList());
		StringBuilder copyConstructorBody = new StringBuilder("super();");
		concretePrototypeProperties.forEach(
			dpField -> copyConstructorBody.append(
				String.format("this.%s = %s.%s;", dpField.getName(), ORIGINAL, dpField.getName()))
		);

		DpClassSource concretePrototype = DpClassSource.newBuilder(this.packageName, this.concretePrototypeName)
			.addImplementsInterface(this.abstractPrototypeName)
			.addFields(concretePrototypeProperties)
			.addMethod(DpSourceMethod.newBuilder().setIsConstructor(Boolean.TRUE)
				.addParameters(concretePrototypeProperties)
				.build())
			.addMethod(DpSourceMethod.newBuilder().setIsConstructor(Boolean.TRUE)
				.addParameter(ORIGINAL, this.concretePrototypeName)
				.setBody(copyConstructorBody.toString())
				.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
				.build())
			.addMethod(DpSourceMethod.newBuilder(abstractCopyMethod)
				.setBody(String.format("return new %s(this);", this.concretePrototypeName))
				.build())
			.build();

		dpSources.add(concretePrototype, this.getClass());

		return dpSources;
	}
}
