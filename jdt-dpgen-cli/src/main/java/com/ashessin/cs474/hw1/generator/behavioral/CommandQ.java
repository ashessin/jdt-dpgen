package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "command", version = "jdt-dpgen 0.1",
		description = "Generates Command behavioral design pattern. " +
					  "Encapsulate a request as an object, thereby letting you parameterize clients " +
					  "with different requests, queue or log requests, and support undoable operations.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class CommandQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(CommandQ.class);
	private static final String PACKAGE_NAME = "com.gof.behavioral.command";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new CommandQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String commandName = InputGroup.commandName;
		String concreteCommandNames = InputGroup.concreteCommandNames;
		String receiverNames = InputGroup.receiverNames;
		String abstractInvokerName = InputGroup.abstractInvokerName;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new CommandGen(packageName, commandName, concreteCommandNames,
				receiverNames, abstractInvokerName).method();
	}

	static class InputGroup implements ArgGroup {

		private static final String COMMAND_NAME = "Command";
		private static final String CONCRETE_COMMAND_NAME = "Concrete" + COMMAND_NAME;
		private static final String RECEIVER_NAME = "Receiver";
		private static final String INVOKER_NAME = "Invoker";

		@CommandLine.Parameters(index = "0", paramLabel = "CommandName",
				description = "The Command declares an interface for executing an operation.")
		static String commandName = COMMAND_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteCommandName",
				description = "The ConcreteCommand class defines the " +
							  "binding between a Receiver object and an action.")
		static String concreteCommandNames = CONCRETE_COMMAND_NAME;
		@CommandLine.Parameters(index = "2", paramLabel = "ReceiverName",
				description = "The Receiver class knows how to perform the operations" +
							  " associated with carrying out a request.")
		static String receiverNames = RECEIVER_NAME;
		@CommandLine.Parameters(index = "3", paramLabel = "InvokerName",
				description = "The Invoker class sends the command to carry out a request.")
		static String abstractInvokerName = INVOKER_NAME;

		private InputGroup() {
		}
	}
}
