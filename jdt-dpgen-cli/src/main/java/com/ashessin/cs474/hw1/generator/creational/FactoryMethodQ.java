package com.ashessin.cs474.hw1.generator.creational;

import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "factorymethod", version = "jdt-dpgen 0.1",
		description = "Generates Factory Method creational design pattern. " +
					  "Define an interface for creating an object, but let subclasses decide which " +
					  "class to instantiate. Factory Method lets a class defer instantiation to subclass",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class FactoryMethodQ implements Callable<DpArrayList<DpSource>> {

	private static final Logger log = LoggerFactory.getLogger(FactoryMethodQ.class);
	private static final String PACKAGE_NAME = "com.gof.creational.factorymethod";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new FactoryMethodQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String abstractCreatorName = InputGroup.abstractCreatorName;
		List<String> concreteCreatorNames = Arrays.stream(InputGroup.concreteCreatorNames.split(","))
				.distinct().collect(Collectors.toList());
		String abstractProductName = InputGroup.abstractProductName;
		List<String> concreteProductNames = Arrays.stream(InputGroup.concreteProductNames.split("[;,]"))
				.distinct().collect(Collectors.toList());

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new FactoryMethodGen(packageName, abstractCreatorName, concreteCreatorNames,
				abstractProductName, concreteProductNames).main();
	}

	static class InputGroup {

		private static final String ABSTRACT_CREATOR_NAME = "Creator";
		private static final String CONCRETE_CREATOR_NAMES = "Creator1,Creator2";
		private static final String ABSTRACT_PRODUCT_NAME = "Product";
		private static final String CONCRETE_PRODUCT_NAMES = "Product1,Product2";
		@CommandLine.Parameters(index = "0", paramLabel = "AbstractCreatorName",
				description = "The Creator abstract class declares the factory method interface.")
		static String abstractCreatorName = ABSTRACT_CREATOR_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteCreatorNames",
				description = "The ConcreteCreator class implements the Creator's factory method " +
							  "and returns an instance of the ConcreteProduct.")
		static String concreteCreatorNames = CONCRETE_CREATOR_NAMES;
		@CommandLine.Parameters(index = "2", paramLabel = "AbstractProductName",
				description = "The Product interface defines the interface of objects the factory method creates.")
		static String abstractProductName = ABSTRACT_PRODUCT_NAME;
		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteProductNames",
				description = "The ConcreteProduct class implements the Product interface.")
		static String concreteProductNames = CONCRETE_PRODUCT_NAMES;

		private InputGroup() {
		}
	}
}
