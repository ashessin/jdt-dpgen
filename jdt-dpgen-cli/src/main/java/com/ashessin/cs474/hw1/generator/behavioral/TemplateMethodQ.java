package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = "templatemethod", version = "jdt-dpgen 0.1",
		description = "Generates Template Method behavioral design pattern. " +
					  "Define the skeleton of an algorithm in an operation, deferring some steps to subclasses. " +
					  "Template Method lets subclasses redefine certain steps of an algorithm without changing the " +
					  "algorithm’s structure.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class TemplateMethodQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(TemplateMethodQ.class);
	private static final String PACKAGE_NAME = "com.gof.behavioral.templatemethod";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1",
			names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new TemplateMethodQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String abstractClassName = InputGroup.abstractClassName;
		List<String> concreteClassNames = Arrays.stream(InputGroup.concreteClassName.split(",")).distinct()
				.collect(Collectors.toList());
		HashMap<String, String> primitiveMethods = new LinkedHashMap<>(2);
		Arrays.stream(InputGroup.primitiveMethods.split(";")).forEach(s -> {
			String methodReturnType = s.split(",")[0];
			String methodName = s.split(",")[1];
			primitiveMethods.put(methodName, methodReturnType);
		});

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new TemplateMethodGen(packageName, abstractClassName, concreteClassNames, primitiveMethods).method();

	}

	static class InputGroup implements ArgGroup {

		private static final String ABSTRACT_CLASS_NAME = "AbstractClass";
		private static final String CONCRETE_CLASS_NAME = "Class1,Class2";
		private static final String PRIMITIVE_METHODS = "Object,primitive1;Object,primitive2";


		@CommandLine.Parameters(index = "0", paramLabel = "AbstractClassName",
				description = "The AbstractClass defines abstract primitive operations that concrete subclasses " +
							  "should implement.",
				defaultValue = ABSTRACT_CLASS_NAME)
		static String abstractClassName = ABSTRACT_CLASS_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteClassNames",
				description = "The ConcreteClass implements the abstract primitive operations to carry out " +
							  "subclass-specific steps of the algorithm.",
				defaultValue = CONCRETE_CLASS_NAME)
		static String concreteClassName = CONCRETE_CLASS_NAME;

		@CommandLine.Parameters(index = "2", paramLabel = "TemplateMethods",
				defaultValue = PRIMITIVE_METHODS)
		static String primitiveMethods = PRIMITIVE_METHODS;

		private InputGroup() {
		}
	}
}