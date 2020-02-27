package com.ashessin.cs474.hw1.generator.structural;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class BridgeGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(BridgeGen.class);
	private static final String CONCRETE = "Concrete";
	private static final String OPERATION = "operation";
	private static final String IMPLEMENTATION = "implementation";
	private String packageName;
	private String abstractionName;
	private String refinedAbstractionName;
	private String implementorName;
	private List<String> concreteImplementorNames;

	public BridgeGen(String packageName,
					 String abstractionName, String refinedAbstractionName,
					 String implementorName, List<String> concreteImplementorNames) {
		this.packageName = packageName;
		this.abstractionName = abstractionName;
		this.refinedAbstractionName = refinedAbstractionName;
		this.implementorName = implementorName;
		this.concreteImplementorNames = concreteImplementorNames;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpClassSource abstraction = DpClassSource.newBuilder(packageName, abstractionName)
			.setModifier(DpClassSource.Modifier.ABSTRACT)
			.addField(DpSourceField.newBuilder(implementorName.toLowerCase(), implementorName)
				.setAccessModifier(DpSourceField.AccessModifier.PROTECTED)
				.build())
			.addMethod(DpSourceMethod.newBuilder()
				.setIsConstructor(Boolean.TRUE)
				.addParameter(implementorName.toLowerCase(), implementorName)
				.build())
			.addMethod(DpSourceMethod.newBuilder()
				.addModifier(DpSourceMethod.Modifier.ABSTRACT)
				.setReturnType("String")
				.setName(OPERATION)
				.build())
			.build();

		dpSources.add(abstraction, this.getClass());

		DpInterfaceSource implementor = DpInterfaceSource.newBuilder(packageName, implementorName)
			.addMethod(DpSourceMethod.newBuilder()
				.setReturnType("String")
				.setName(IMPLEMENTATION)
				.build())
			.build();

		dpSources.add(implementor, this.getClass());

		List<DpClassSource> concreteImplementors = concreteImplementorNames.stream()
			.map(concreteImplementorName -> DpClassSource.newBuilder(packageName, concreteImplementorName)
				.addImplementsInterface(implementorName)
				.addMethod(DpSourceMethod.newBuilder(implementor.getMethods().get(0))
					.setBody("return this.getClass().getName();")
					.build())
				.build())
			.collect(toList());


		// concreteImplementors.forEach(RoasterParser::makeSource);

		DpClassSource refinedAbstraction = DpClassSource.newBuilder(packageName, refinedAbstractionName)
			.setExtendsClass(abstractionName)
			.addMethod(DpSourceMethod.newBuilder()
				.setIsConstructor(Boolean.TRUE)
				.addParameter(implementorName.toLowerCase(), implementorName)
				.setBody(String.format("super(%s);", implementorName.toLowerCase()))
				.build())
			.addMethod(DpSourceMethod.newBuilder()
				.setReturnType("String")
				.setName(OPERATION)
				.setIsInherited(Boolean.TRUE)
				.setBody(String.format("return this.%s.%s();",
					implementorName.toLowerCase(),
					IMPLEMENTATION))
				.build())
			.build();

		dpSources.add(refinedAbstraction, this.getClass());

		return dpSources;
	}
}