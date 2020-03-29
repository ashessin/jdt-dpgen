package com.ashessin.cs474.hw1.generator.behavioral;

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

@CommandLine.Command(name = "strategy", version = "jdt-dpgen 0.1",
		description = "Generates Strategy behavioral design pattern. " +
					  "Define a family of algorithms, encapsulate each one, and make them interchangeable. Strategy " +
					  "lets the algorithm vary independently from clients that use it.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class StrategyQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(StrategyQ.class);
	private static final String PACKAGE_NAME = "com.gof.behavioral.strategy";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new StrategyQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String strategyName = InputGroup.strategyName;
		List<String> concreteStrategyNames = Arrays.stream(InputGroup.concreteStrategyNames.split(","))
				.distinct().collect(Collectors.toList());
		String contextName = InputGroup.contextName;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new StrategyGen(packageName, strategyName, concreteStrategyNames, contextName).invoke();

	}

	static class InputGroup implements ArgGroup {

		private static final String STRATEGY_NAME = "Strategy";
		private static final String CONCRETE_STRATEGY_NAMES = "Strategy1,Strategy2,Strategy3";
		private static final String CONTEXT_NAME = "Context";


		@CommandLine.Parameters(index = "0", paramLabel = "StrategyName",
				description = "The Strategy declares an interface common to all supported algorithms.",
				defaultValue = STRATEGY_NAME)
		static String strategyName = STRATEGY_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteStrategyNames",
				description = "The ConcreteStrategy implements the algorithm using the Strategy interface.",
				defaultValue = CONCRETE_STRATEGY_NAMES)
		static String concreteStrategyNames = CONCRETE_STRATEGY_NAMES;

		@CommandLine.Parameters(index = "2", paramLabel = "ContextName",
				description = "The Context uses this interface to call the algorithm defined by a ConcreteStrategy.",
				defaultValue = CONTEXT_NAME)
		static String contextName = CONTEXT_NAME;

		private InputGroup() {
		}
	}
}