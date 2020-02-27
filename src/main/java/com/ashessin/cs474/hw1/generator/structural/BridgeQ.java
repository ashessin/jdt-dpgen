package com.ashessin.cs474.hw1.generator.structural;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "bridge", version = "jdt-dpgen 0.1",
	description = "Generates Bridge structural design pattern. " +
				  "Decouple an abstraction from its implementation so that the two can " +
				  "vary independently.",
	mixinStandardHelpOptions = true,
	showDefaultValues = true,
	sortOptions = false
)
public class BridgeQ implements Callable<DpArrayList<DpSource>> {

	private static final Logger log = (Logger) LoggerFactory.getLogger(BridgeQ.class);
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
			implementorName, concreteImplementorNames).main();
	}

	static class InputGroup {

		private static final String ABSTRACTION_NAME = "Abstraction";
		private static final String REFINED_ABSTRACTION_NAME = "Refined" + ABSTRACTION_NAME;
		private static final String IMPLEMENTOR_NAME = "Implementor";
		private static final String CONCRETE_IMPLEMENTOR_NAMES = "ImplementorA,ImplementorB";
		@CommandLine.Parameters(index = "0", paramLabel = "AbstractionName",
			description = "The Abstraction abstract class defines the abstraction interface, maintains a " +
						  "reference to an object of type Implementator, and the link between the " +
						  "abstraction and the implementer is called a Bridge.")
		static String abstractionName = ABSTRACTION_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "RefinedAbstractionName",
			description = "The RefinedAbstracion extends the interface defined by Abstraction.")
		static String refinedAbstractionName = REFINED_ABSTRACTION_NAME;
		@CommandLine.Parameters(index = "2", paramLabel = "ImplementorName",
			description = "The Implementator interface defines the interface for implementation " +
						  "classes (concrete implementers).")
		static String implementorName = IMPLEMENTOR_NAME;
		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteImplementorNames",
			description = "The ConcreteImplementator class implements the Implementator interface and defines " +
						  "its concrete implementation.")
		static String concreteImplementorNames = CONCRETE_IMPLEMENTOR_NAMES;

		private InputGroup() {
		}
	}
}
