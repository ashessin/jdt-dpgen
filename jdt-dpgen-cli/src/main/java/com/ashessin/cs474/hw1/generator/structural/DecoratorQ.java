package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "decorator", version = "jdt-dpgen 0.1",
		description = "Generates Decorator structural design pattern. " +
					  "Attach additional responsibilities to an object dynamically. Decorators " +
					  "provide a flexible alternative to sub-classing for extending functionality.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class DecoratorQ implements Callable<DpArrayList<DpSource>> {

	private static final Logger log = LoggerFactory.getLogger(DecoratorQ.class);
	private static final String PACKAGE_NAME = "com.gof.structural.decorator";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new DecoratorQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String decoratorName = InputGroup.decoratorName;
		List<String> concreteDecoratorNames = Arrays.stream(InputGroup.concreteDecoratorNames.split(","))
				.distinct().collect(Collectors.toList());
		String componentName = InputGroup.componentName;
		String concreteComponentName = InputGroup.concreteComponentName;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new DecoratorGen(packageName, decoratorName, concreteDecoratorNames, componentName, concreteComponentName).main();

	}

	static class InputGroup {

		private static final String DECORATOR_NAME = "Decorator";
		private static final String CONCRETE_DECORATOR_NAMES = "DecoratorA,DecoratorB";
		private static final String COMPONENT_NAME = "Component";
		private static final String CONCRETE_COMPONENT_NAME = "Concrete" + COMPONENT_NAME;
		@CommandLine.Parameters(index = "0", paramLabel = "DecoratorName",
				description = "The Decorator abstract class holds reference to the Component object.")
		static String decoratorName = DECORATOR_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteDecoratorNames",
				description = "The ConcreteDecorator class adds new features to the Component object.")
		static String concreteDecoratorNames = CONCRETE_DECORATOR_NAMES;
		@CommandLine.Parameters(index = "2", paramLabel = "ComponentName",
				description = "The Component defines interfaces for new features which will be added dynamically.")
		static String componentName = COMPONENT_NAME;
		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteComponentNames",
				description = "The ConcreteComponent class defines object where new features can be added.")
		static String concreteComponentName = CONCRETE_COMPONENT_NAME;

		private InputGroup() {
		}
	}
}
