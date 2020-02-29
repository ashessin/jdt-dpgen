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

@CommandLine.Command(name = "mediator", version = "jdt-dpgen 0.1",
		description = "Generates Mediator behavioral design pattern. " +
					  "Define an object that encapsulates how a set of objects interact. Mediator promotes loose " +
					  "coupling by keeping objects from referring to each other explicitly, and it lets you vary " +
					  "their interaction independently.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class MediatorQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(MediatorQ.class);
	private static final String PACKAGE_NAME = "com.gof.behavioral.mediator";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new MediatorQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String mediatorName = InputGroup.mediatorName;
		String concreteMediatorName = InputGroup.concreteMediatorName;
		String colleagueName = InputGroup.colleagueName;
		List<String> concreteColleagueNames = Arrays.stream(InputGroup.concreteColleagueNames.split(","))
				.distinct().collect(Collectors.toList());

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new MediatorGen(packageName, mediatorName, concreteMediatorName,
				colleagueName, concreteColleagueNames).method();
	}

	static class InputGroup implements ArgGroup {

		private static final String MEDIATOR_NAME = "Mediator";
		private static final String CONCRETE_MEDIATOR_NAME = "ConcreteMediator";
		private static final String COLLEAGUE_NAME = "Colleague";
		private static final String CONCRETE_COLLEAGUE_NAMES = "Colleague1,Colleague2";

		@CommandLine.Parameters(index = "0", paramLabel = "MediatorName",
				description = "The Mediator defines an interface for communicating with Colleague objects.",
				defaultValue = MEDIATOR_NAME)
		static String mediatorName = MEDIATOR_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteMediatorName",
				description = "The ConcreteMediator implements cooperative behavior by coordinating the Colleague " +
							  "objects.",
				defaultValue = CONCRETE_MEDIATOR_NAME)
		static String concreteMediatorName = CONCRETE_MEDIATOR_NAME;

		@CommandLine.Parameters(index = "2", paramLabel = "ColleagueName",
				description = "A Colleague defines an interface for communication with another Colleague via the " +
							  "Mediator.",
				defaultValue = COLLEAGUE_NAME)
		static String colleagueName = COLLEAGUE_NAME;

		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteColleagueNames",
				description = "For the ConcreteColleague class, each Colleague class knows its Mediator object, and " +
							  "each Colleague communicates with its mediator whenever it would have otherwise " +
							  "communicated with another colleague.",
				defaultValue = CONCRETE_COLLEAGUE_NAMES)
		static String concreteColleagueNames = CONCRETE_COLLEAGUE_NAMES;

		private InputGroup() {
		}
	}
}
