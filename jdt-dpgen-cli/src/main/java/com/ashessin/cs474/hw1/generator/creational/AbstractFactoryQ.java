package com.ashessin.cs474.hw1.generator.creational;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = "abstractfactory", version = "jdt-dpgen 0.1",
		description = "Generates Abstract Factory creational design pattern. " +
					  "Provide an interface for creating families of related or dependent objects " +
					  "without specifying their concrete classes.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class AbstractFactoryQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(AbstractFactoryQ.class);
	private static final String PACKAGE_NAME = "com.gof.creational.abstractfactory";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new AbstractFactoryQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String abstractFactoryName = InputGroup.abstractFactoryName;
		List<String> concreteFactoryNames = Arrays.stream(InputGroup.concreteFactoryNames.split(","))
				.distinct().collect(Collectors.toList());
		List<String> abstractProductNames = Arrays.stream(InputGroup.abstractProductNames.split(","))
				.distinct().collect(Collectors.toList());
		List<String> concreteProductNames = Arrays.stream(InputGroup.concreteProductNames.split("[;,]"))
				.distinct().collect(Collectors.toList());

		int familySize = concreteProductNames.size() / abstractProductNames.size();

		// TODO: Add/Improve validation through exception handling
		assert (concreteFactoryNames.size() == InputGroup.concreteFactoryNames.split(",").length &&
				concreteFactoryNames.size() >= 2);
		assert (abstractProductNames.size() == InputGroup.abstractProductNames.split(",").length &&
				abstractProductNames.size() >= 2);
		assert (concreteProductNames.size() == InputGroup.concreteProductNames.split("[;,]").length &&
				concreteProductNames.size() >= 4);
		assert (concreteFactoryNames.size() == familySize);
		assert (concreteProductNames.size() == abstractProductNames.size() * concreteFactoryNames.size());
		assert (concreteProductNames.size() / concreteFactoryNames.size() % abstractProductNames.size() <= 1);

		LinkedHashMap<String, List<String>> products = new LinkedHashMap<>(2);
		List<List<String>> families = new ArrayList<>(2);
		for (int start = 0; start < concreteProductNames.size(); start += familySize) {
			int end = Math.min(start + familySize, concreteProductNames.size());
			families.add(concreteProductNames.subList(start, end));
		}
		for (int i = 0; i < abstractProductNames.size(); i++) {
			products.put(abstractProductNames.get(i), families.get(i));
		}

		log.info("Generating representation for design pattern sources.");
		return new AbstractFactoryGen(packageName, abstractFactoryName, concreteFactoryNames, products).method();
	}

	static class InputGroup implements ArgGroup {

		private static final String ABSTRACT_FACTORY_NAME = "Factory";
		private static final String CONCRETE_FACTORY_NAMES = "Factory1,Factory2";
		private static final String ABSTRACT_PRODUCT_NAMES = "ProductA,ProductB";
		private static final String CONCRETE_PRODUCT_NAMES = "ProductA1,ProductA2;ProductB1,ProductB2";
		@CommandLine.Parameters(index = "0", paramLabel = "AbstractFactoryName",
				description = "The AbstractFactory defines the interface for creation of the abstract " +
							  "product objects.")
		static String abstractFactoryName = ABSTRACT_FACTORY_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteFactoryNames",
				description = "The ConcreteFactory class implements factories based on AbstractFactory interfaces.")
		static String concreteFactoryNames = CONCRETE_FACTORY_NAMES;
		@CommandLine.Parameters(index = "2", paramLabel = "AbstractProductNames",
				description = "The AbstractProduct defines the interface for product objects.")
		static String abstractProductNames = ABSTRACT_PRODUCT_NAMES;
		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteProductNames",
				description = "The ConcreteProduct class implements products based on AbstractProduct interfaces.")
		static String concreteProductNames = CONCRETE_PRODUCT_NAMES;

		private InputGroup() {
		}
	}
}
