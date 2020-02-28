package com.ashessin.cs474.hw1.generator.creational;

import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuilderGen {

	private static final Logger log = LoggerFactory.getLogger(BuilderGen.class);
	private static final String BUILD = "build";
	private String packageName;
	private String abstractBuilderName;
	private String concreteBuilderName;
	private String concreteProductName;
	// propertyName, propertyType
	private Map<String, String> propertiesMap;

	public BuilderGen(String packageName,
					  String abstractBuilderName, String concreteBuilderName,
					  String concreteProductName,
					  Map<String, String> propertiesMap) {
		this.packageName = packageName;
		this.abstractBuilderName = abstractBuilderName;
		this.concreteBuilderName = concreteBuilderName;
		this.concreteProductName = concreteProductName;
		this.propertiesMap = propertiesMap;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		List<DpSourceField> concreteProductProperties = propertiesMap.entrySet().stream()
				.map(entry -> DpSourceField.newBuilder(entry.getKey(), entry.getValue())
						.setGetter(Boolean.TRUE)
						.setSetter(Boolean.TRUE)
						.build()).collect(Collectors.toList());
		DpClassSource dpConcreteProduct = DpClassSource.newBuilder(this.packageName, this.concreteProductName)
				.addFields(concreteProductProperties)
				.build();

		dpSources.add(dpConcreteProduct, this.getClass());

		List<DpSourceMethod> dpAbstractBuilderMethods = propertiesMap.entrySet().stream()
				.map(entry -> DpSourceMethod.newBuilder()
						.setName(BUILD + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1))
						.addModifier(DpSourceMethod.Modifier.ABSTRACT)
						.setReturnType(this.abstractBuilderName)
						.addParameter(entry.getKey(), entry.getValue())
						.build())
				.collect(Collectors.toList());
		DpClassSource dpAbstractBuilder = DpClassSource.newBuilder(this.packageName, this.abstractBuilderName)
				.setModifier(DpClassSource.Modifier.ABSTRACT)
				.addMethods(dpAbstractBuilderMethods)
				.build();

		dpSources.add(dpAbstractBuilder, this.getClass());


		List<DpSourceMethod> dpConcreteBuilderMethods = propertiesMap.entrySet().stream()
				.map(entry -> DpSourceMethod.newBuilder()
						.setName(BUILD + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1))
						.addModifier(DpSourceMethod.Modifier.NONE)
						.setReturnType(this.abstractBuilderName)
						.addParameter(entry.getKey(), entry.getValue())
						.setBody(String.format("%s.set%s(%s);return this;",
								this.concreteProductName.toLowerCase(),
								entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1),
								entry.getKey()))
						.build())
				.collect(Collectors.toList());
		DpClassSource dpConcreteBuilder = DpClassSource.newBuilder(this.packageName, this.concreteBuilderName)
				.setExtendsClass(this.abstractBuilderName)
				.addField(DpSourceField.newBuilder(this.concreteProductName.toLowerCase(), this.concreteProductName).build())
				.addMethods(dpConcreteBuilderMethods)
				.build();

		dpSources.add(dpConcreteBuilder, this.getClass());

		return dpSources;
	}
}
