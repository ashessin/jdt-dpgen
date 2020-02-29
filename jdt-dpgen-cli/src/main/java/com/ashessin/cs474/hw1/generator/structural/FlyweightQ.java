package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "flyweight", version = "jdt-dpgen 0.1",
		description = "Generates Flyweight structural design pattern. " +
					  "Use sharing to support large numbers of fine-grained objects efficiently.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class FlyweightQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(FlyweightQ.class);
	private static final String PACKAGE_NAME = "com.gof.structural.flyweight";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new FlyweightQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String flyweightName = InputGroup.flyweightName;
		String concreteFlyweightName = InputGroup.concreteFlyweightName;
		String unsharedFlyweightName = InputGroup.unsharedFlyweightName;
		String flyweightFactoryName = InputGroup.flyweightFactoryName;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new FlyweightGen(packageName, flyweightName, concreteFlyweightName, unsharedFlyweightName,
				flyweightFactoryName).method();
	}

	static class InputGroup implements ArgGroup {

		private static final String FLYWEIGHT_NAME = "Flyweight";
		private static final String FLYWEIGHT_FACTORY_NAME = FLYWEIGHT_NAME + "Factory";
		private static final String CONCRETE_FLYWEIGHT_NAME = "ConcreteFlyweight";
		private static final String UNSHARED_FLYWEIGHT_NAME = "Unshared" + CONCRETE_FLYWEIGHT_NAME;
		@CommandLine.Parameters(index = "0", paramLabel = "FlyweightName",
				description = "The Flyweight interface defines interfaces through which flyweight can " +
							  "receive and act on extrinsic states.")
		static String flyweightName = FLYWEIGHT_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "FlyweightFactoryName",
				description = "The FlyweightFactory class creates and manages the flyweight objects.")
		static String flyweightFactoryName = FLYWEIGHT_FACTORY_NAME;
		@CommandLine.Parameters(index = "2", paramLabel = "ConcreteFlyweightName",
				description = "The ConcreteFlyweight class implements Flyweight and adds storage " +
							  "for intrinsic state (Character).")
		static String concreteFlyweightName = CONCRETE_FLYWEIGHT_NAME;
		@CommandLine.Parameters(index = "3", paramLabel = "UnsharedFlyweightName",
				description = "The UnsharedConcreteFlyweight class defines objects which are not shared.")
		static String unsharedFlyweightName = UNSHARED_FLYWEIGHT_NAME;

		private InputGroup() {
		}
	}
}
