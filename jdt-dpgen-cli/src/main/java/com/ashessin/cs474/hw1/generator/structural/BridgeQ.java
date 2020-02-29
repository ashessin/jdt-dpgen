package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CommandLine.Command(name = "bridge", version = "jdt-dpgen 0.1",
		description = "Generates Bridge structural design pattern. " +
					  "Decouple an abstraction from its implementation so that the two can vary independently.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class BridgeQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(BridgeQ.class);
	private static final String PACKAGE_NAME = "com.gof.structural.bridge";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new BridgeQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String abstractionName = InputGroup.abstractionName;
		String refinedAbstractionName = InputGroup.refinedAbstractionName;
		String implementorName = InputGroup.implementorName;
		List<String> concreteImplementorNames = Arrays.stream(InputGroup.concreteImplementorNames.split(","))
				.distinct().collect(Collectors.toList());

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new BridgeGen(packageName, abstractionName, refinedAbstractionName,
				implementorName, concreteImplementorNames).method();
	}

	static class InputGroup implements ArgGroup {

		private static final String ABSTRACTION_NAME = "Abstraction";
		private static final String REFINED_ABSTRACTION_NAME = "Refined" + ABSTRACTION_NAME;
		private static final String IMPLEMENTOR_NAME = "Implementor";
		private static final String CONCRETE_IMPLEMENTOR_NAMES = "ImplementorA,ImplementorB";

		@CommandLine.Parameters(index = "0", paramLabel = "AbstractionName",
				description = "The Abstraction abstract class defines the abstraction interface, maintains a " +
							  "reference to an object of type Implementator, and the link between the abstraction " +
							  "and the implementer is called a Bridge.",
				defaultValue = ABSTRACTION_NAME)
		static String abstractionName = ABSTRACTION_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "RefinedAbstractionName",
				description = "The RefinedAbstracion extends the interface defined by Abstraction.",
				defaultValue = REFINED_ABSTRACTION_NAME)
		static String refinedAbstractionName = REFINED_ABSTRACTION_NAME;

		@CommandLine.Parameters(index = "2", paramLabel = "ImplementorName",
				description = "The Implementator interface defines the interface for implementation classes " +
							  "(concrete implementers).",
				defaultValue = IMPLEMENTOR_NAME)
		static String implementorName = IMPLEMENTOR_NAME;

		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteImplementorNames",
				description = "The ConcreteImplementator class implements the Implementator interface and defines " +
							  "its concrete implementation.",
				defaultValue = CONCRETE_IMPLEMENTOR_NAMES)
		static String concreteImplementorNames = CONCRETE_IMPLEMENTOR_NAMES;

		private InputGroup() {
		}
	}
}
