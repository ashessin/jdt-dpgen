package com.ashessin.cs474.hw1.generator.creational;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = "builder", version = "jdt-dpgen 0.1",
		description = "Generates Builder creational design pattern. " +
					  "Separate the construction of a complex object from its representation so that the same " +
					  "construction process can create different representations.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class BuilderQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(BuilderQ.class);
	private static final String PACKAGE_NAME = "com.gof.creational.builder";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new BuilderQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String abstractBuilderName = InputGroup.abstractBuilderName;
		String concreteBuilderName = InputGroup.concreteBuilderName;
		String concreteProductName = InputGroup.concreteProductName;
		List<String> properties = Arrays.stream(InputGroup.properties.split(";")).collect(Collectors.toList());

		// TODO: Add input validations

		LinkedHashMap<String, String> propertiesMap = new LinkedHashMap<>(2);
		properties.forEach(propertyString -> {
			String[] property = propertyString.split(",");
			String propertyName = property[1];
			String propertyType = property[0];
			propertiesMap.put(propertyName, propertyType);
		});

		log.info("Generating representation for design pattern sources.");
		return new BuilderGen(packageName, abstractBuilderName, concreteBuilderName, concreteProductName,
				propertiesMap).method();
	}

	static class InputGroup implements ArgGroup {

		private static final String CONCRETE_DIRECTOR_NAME = "Director";
		private static final String ABSTRACT_BUILDER_NAME = "Builder";
		private static final String CONCRETE_BUILDER_NAME = "ConcreteBuilder";
		private static final String CONCRETE_PRODUCT_NAME = "Product";
		private static final String PROPERTIES = "Object,property1;Object,property2";

		@CommandLine.Parameters(index = "0", paramLabel = "BuilderName",
				description = "The Builder specifies an abstract interface for creating parts of a Product object.",
				defaultValue = ABSTRACT_BUILDER_NAME)
		static String abstractBuilderName = ABSTRACT_BUILDER_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteBuilderName",
				description = "The ConcreteBuilder class constructs and assembles parts of the product, implementing" +
							  " " +
							  "the Builder interface.",
				defaultValue = CONCRETE_BUILDER_NAME)
		static String concreteBuilderName = CONCRETE_BUILDER_NAME;

		@CommandLine.Parameters(index = "2", paramLabel = "ConcreteProductName",
				description = "The Product class represents a complex object.",
				defaultValue = CONCRETE_PRODUCT_NAME)
		static String concreteProductName = CONCRETE_PRODUCT_NAME;

		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteProductProperties",
				defaultValue = PROPERTIES)
		static String properties = PROPERTIES;

		private InputGroup() {
		}
	}
}
