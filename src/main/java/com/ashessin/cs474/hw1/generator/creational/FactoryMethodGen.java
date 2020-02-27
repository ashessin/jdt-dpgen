package com.ashessin.cs474.hw1.generator.creational;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FactoryMethodGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(FactoryMethodGen.class);
	private static final String CONCRETE = "Concrete";
	private static final String FACTORY = "factory";
	private static final String METHOD = "Method";
	private String packageName;
	private String abstractCreatorName;
	private List<String> concreteCreatorNames;
	private String abstractProductName;
	private List<String> concreteProductNames;

	public FactoryMethodGen(String packageName,
							String abstractCreatorName, List<String> concreteCreatorNames,
							String abstractProductName, List<String> concreteProductNames) {
		this.packageName = packageName;
		this.abstractCreatorName = abstractCreatorName;
		this.concreteCreatorNames = concreteCreatorNames;
		this.abstractProductName = abstractProductName;
		this.concreteProductNames = concreteProductNames;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpInterfaceSource product = DpInterfaceSource.newBuilder(packageName, abstractProductName)
			.build();

		dpSources.add(product, this.getClass());

		DpClassSource creator = DpClassSource.newBuilder(packageName, abstractCreatorName)
			.setModifier(DpClassSource.Modifier.ABSTRACT)
			.addMethod(DpSourceMethod.newBuilder()
				.setName(FACTORY + METHOD)
				.setReturnType(abstractProductName)
				.setAccessModifier(DpSourceMethod.AccessModifier.PROTECTED)
				.addModifier(DpSourceMethod.Modifier.ABSTRACT)
				.build())
			.addMethod(DpSourceMethod.newBuilder()
				.setName(FACTORY)
				.setReturnType(abstractProductName)
				.setBody(String.format("return %s();",
					FACTORY + METHOD))
				.build())
			.build();

		dpSources.add(creator, this.getClass());

		List<DpClassSource> concreteProducts = concreteProductNames.stream()
			.map(concreteProductName -> DpClassSource.newBuilder(packageName, CONCRETE + concreteProductName)
				.addImplementsInterface(abstractProductName)
				.build())
			.collect(Collectors.toList());


		dpSources.addAll(concreteProducts, this.getClass());

		List<DpClassSource> concreteCreators = IntStream.range(0, concreteCreatorNames.size())
			.mapToObj(i -> DpClassSource.newBuilder(packageName, concreteCreatorNames.get(i))
				.setExtendsClass(abstractCreatorName)
				.addMethod(DpSourceMethod.newBuilder(creator.getMethods().get(0))
					.setBody(String.format("return new %s();",
						CONCRETE + concreteProductNames.get(i)))
					.build())
				.build())
			.collect(Collectors.toList());


		dpSources.addAll(concreteCreators, this.getClass());

		return dpSources;
	}
}