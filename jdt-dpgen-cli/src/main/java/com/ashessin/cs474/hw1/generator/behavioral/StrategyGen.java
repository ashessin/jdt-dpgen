package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.*;

import java.util.List;
import java.util.stream.Collectors;

public class StrategyGen extends DesignPatternGen {

	private static final String ALGORITHM = "algorithm";
	private static final String OPERATION = "operation";
	private static final String CONCRETE = "Concrete";
	private static final String TYPE = "Object";

	private String packageName;
	private String strategyName;
	private List<String> concreteStrategyNames;
	private String contextName;


	public StrategyGen(String packageName,
					   String strategyName, List<String> concreteStrategyNames,
					   String contextName) {
		this.packageName = packageName;
		this.strategyName = strategyName;
		this.concreteStrategyNames = concreteStrategyNames;
		this.contextName = contextName;
	}

	public DpArrayList<DpSource> main() {
		DpInterfaceSource strategy = DpInterfaceSource.newBuilder(packageName, strategyName)
				.addMethod(DpSourceMethod.newBuilder()
						.setName(ALGORITHM)
						.setReturnType(TYPE)
						.build())
				.build();

		dpSources.add(strategy, this.getClass());

		List<DpClassSource> concreteStrategies = concreteStrategyNames.stream()
				.map(concreteStrategyName -> DpClassSource.newBuilder(packageName, CONCRETE + concreteStrategyName)
						.addMethod(DpSourceMethod.newBuilder()
								.setName(ALGORITHM)
								.setReturnType(TYPE)
								.setBody(String.format("return \"%s\";", concreteStrategyName))
								.build())
						.build())
				.collect(Collectors.toList());

		dpSources.add(concreteStrategies, this.getClass());

		DpClassSource context = DpClassSource.newBuilder(packageName, contextName)
				.addField(DpSourceField.newBuilder(strategyName.toLowerCase(), strategyName)
						.setAccessModifier(DpSourceField.AccessModifier.PRIVATE)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setIsConstructor(Boolean.TRUE)
						.addParameter(strategyName.toLowerCase(), strategyName)
						.build())
				.addMethod(DpSourceMethod.newBuilder()
						.setName(OPERATION)
						.setReturnType(TYPE)
						.setBody(String.format("return this.%s.%s();",
								strategyName.toLowerCase(), ALGORITHM))
						.build())
				.build();

		dpSources.add(context, this.getClass());

		return dpSources;
	}
}

