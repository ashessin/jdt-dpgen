package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "memento", version = "jdt-dpgen 0.1",
		description = "Generates Memento behavioral design pattern. " +
					  "Without violating encapsulation, capture and externalize an object’s internal " +
					  "state so that the object can be restored to this state later.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class MementoQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(MementoQ.class);
	private static final String PACKAGE_NAME = "com.gof.behavioral.memento";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new MementoQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String momentoName = InputGroup.momentoName;
		String caretakerName = InputGroup.caretakerName;
		String originatorName = InputGroup.ORIGINATOR_NAME;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new MementoGen(packageName, momentoName, caretakerName, originatorName).method();

	}

	static class InputGroup implements ArgGroup {

		private static final String MOMENTO_NAME = "Momento";
		private static final String CARETAKER_NAME = "Caretaker";
		private static final String ORIGINATOR_NAME = "Originator";

		@CommandLine.Parameters(index = "0", paramLabel = "MomentoName",
				description = "The Memento stores the internal state of the Originator object and protects against " +
							  "access by objects other than the Originator.")
		static String momentoName = MOMENTO_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "CaretakerName",
				description = "The Caretaker class is responsible for Memento's safekeeping.")
		static String caretakerName = CARETAKER_NAME;
		@CommandLine.Parameters(index = "2", paramLabel = "OriginatorName",
				description = "The Originator creates a Memento containing a snapshot of its current internal " +
							  "state and uses the Memento to restore its previous internal state.")
		static String originatorName = ORIGINATOR_NAME;

		private InputGroup() {
		}
	}
}