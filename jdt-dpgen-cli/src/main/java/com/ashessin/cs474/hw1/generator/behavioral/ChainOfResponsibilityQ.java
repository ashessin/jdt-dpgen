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

@CommandLine.Command(name = "chainofresponsibility", version = "jdt-dpgen 0.1",
		description = "Generates Chain Of Responsibility behavioral design pattern. " +
					  "Avoid coupling the sender of a request to its receiver by giving more than one object a " +
					  "chance to handle the request. Chain the receiving objects and pass the request along the " +
					  "chain until an object handles it.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class ChainOfResponsibilityQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(ChainOfResponsibilityQ.class);
	private static final String PACKAGE_NAME = "com.gof.behavioral.chainofresponsibility";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new ChainOfResponsibilityQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String handlerName = InputGroup.handlerName;
		String handlerFieldName = InputGroup.handlerFieldName;
		List<String> concreteHandlers = Arrays.stream(InputGroup.concreteHandlerNames.split(","))
				.distinct().collect(Collectors.toList());

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new ChainOfResponsibilityGen(packageName, handlerName, handlerFieldName, concreteHandlers).method();

	}

	static class InputGroup implements ArgGroup {

		private static final String HANDLER_NAME = "Handler";
		private static final String HANDLER_FIELD_NAME = "succesor";
		private static final String CONCRETE_HANDLER_NAMES = "HandlerA,HandlerB";

		@CommandLine.Parameters(index = "0", paramLabel = "HandlerName",
				description = "A Handler defines interface for request handling.",
				defaultValue = HANDLER_NAME)
		static String handlerName = HANDLER_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "HandlerFieldName",
				defaultValue = HANDLER_FIELD_NAME)
		static String handlerFieldName = HANDLER_FIELD_NAME;

		@CommandLine.Parameters(index = "2", paramLabel = "ConcreteHandlerNames",
				description = "The ConcreteHandler handles the request, can access the next object in the chain and " +
							  "forward the request if necessary.",
				defaultValue = CONCRETE_HANDLER_NAMES)
		static String concreteHandlerNames = CONCRETE_HANDLER_NAMES;

		private InputGroup() {
		}
	}
}
