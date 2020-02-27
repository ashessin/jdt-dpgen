package com.ashessin.cs474.hw1.generator.structural;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.*;
import com.ashessin.cs474.hw1.utils.LoggingReflection;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class FacadeGen {

	private static final Logger log = (Logger) LoggerFactory.getLogger(FacadeGen.class);
	private static final String ACTION = "Action";
	private static final String PERFORMED = "Performed";
	private static final String PERFORM = "Perform";
	private String packageName;
	private String facadeServiceName;
	private String serviceName;
	private List<String> concreteServiceNames;

	public FacadeGen(String packageName, String facadeServiceName, String serviceName,
					 List<String> concreteServiceNames) {
		this.packageName = packageName;
		this.facadeServiceName = facadeServiceName;
		this.serviceName = serviceName;
		this.concreteServiceNames = concreteServiceNames;
	}

	public DpArrayList<DpSource> main() {

		DpArrayList<DpSource> dpSources = new DpArrayList<>();

		if (log.isInfoEnabled()) {
			LoggingReflection.infoLogInstance(this);
		}

		DpClassSource service = DpClassSource.newBuilder(packageName, serviceName)
			.setModifier(DpClassSource.Modifier.ABSTRACT)
			.addField(DpSourceField.newBuilder(ACTION.toLowerCase() + PERFORMED, "boolean")
				.setAccessModifier(DpSourceField.AccessModifier.PROTECTED)
				.setGetter(Boolean.TRUE)
				.build())
			.addMethod(DpSourceMethod.newBuilder()
				.setAccessModifier(DpSourceMethod.AccessModifier.PUBLIC)
				.addModifier(DpSourceMethod.Modifier.ABSTRACT)
				.setName(PERFORM.toLowerCase() + serviceName)
				.build())
			.build();

		dpSources.add(service, this.getClass());

		List<DpClassSource> concreteServices = concreteServiceNames.stream()
			.map(concreteServiceName -> DpClassSource.newBuilder(packageName, concreteServiceName)
				.setExtendsClass(serviceName)
				.addMethod(DpSourceMethod.newBuilder(service.getMethods().get(0))
					.setBody(String.format("%s = true;", ACTION.toLowerCase() + PERFORMED))
					.build())
				.build())
			.collect(Collectors.toList());

		dpSources.addAll(concreteServices, this.getClass());

		String facedServiceConstructorBody = "";
		for (String concreteServiceName : concreteServiceNames) {
			facedServiceConstructorBody = facedServiceConstructorBody
				.concat(String.format("%s = new %s();%n",
					concreteServiceName.toLowerCase(), concreteServiceName));
		}
		DpClassSource facedService = DpClassSource.newBuilder(packageName, facadeServiceName)
			.addFields(concreteServiceNames.stream()
				.map(concreteServiceName ->
					DpSourceField.newBuilder(concreteServiceName.toLowerCase(), concreteServiceName)
						.addModifier(DpSourceField.Modifier.FINAL)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.collect(Collectors.toList()))
			.addMethod(DpSourceMethod.newBuilder()
				.setIsConstructor(Boolean.TRUE)
				.setBody(facedServiceConstructorBody)
				.build())
			.addMethods(concreteServiceNames.stream()
				.map(concreteServiceName ->
					DpSourceMethod.newBuilder()
						.setName("is" + concreteServiceName + PERFORMED)
						.setReturnType("boolean")
						.setBody(String.format("return %s.%s;",
							concreteServiceName.toLowerCase(), ACTION.toLowerCase() + PERFORMED))
						.build())
				.collect(Collectors.toList()))
			.build();

		dpSources.add(facedService, this.getClass());

		return dpSources;
	}
}