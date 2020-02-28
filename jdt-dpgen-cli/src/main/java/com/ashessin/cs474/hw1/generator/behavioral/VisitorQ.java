package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@CommandLine.Command(name = "visitor", version = "jdt-dpgen 0.1",
		description = "Generates Visitor behavioral design pattern. " +
					  "Represent an operation to be performed on the elements of an object structure. " +
					  "Visitor lets you define a new operation without changing the classes of the elements " +
					  "on which it operates.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class VisitorQ implements Callable<DpArrayList<DpSource>> {

	private static final Logger log = LoggerFactory.getLogger(VisitorQ.class);
	private static final String PACKAGE_NAME = "com.gof.behavioral.visitor";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new VisitorQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String visitorName = InputGroup.visitorName;
		List<String> concreteVisitorNames = Arrays.stream(InputGroup.concreteVisitorNames.split(","))
				.distinct().collect(Collectors.toList());
		String elementName = InputGroup.elementName;
		List<String> concreteElementNames = Arrays.stream(InputGroup.concreteElementNames.split(","))
				.distinct().collect(Collectors.toList());

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new VisitorGen(packageName, visitorName, concreteVisitorNames,
				elementName, concreteElementNames).main();
	}

	static class InputGroup {

		private static final String VISITOR_NAME = "Visitor";
		private static final String CONCRETE_VISITOR_NAMES = "Visitor1,Visitor2";
		private static final String ELEMENT_NAME = "Element";
		private static final String CONCRETE_ELEMENT_NAMES = "ElementA,ElementB";
		@CommandLine.Parameters(index = "0", paramLabel = "VisitorName",
				description = "The Visitor declares a Visit operation for each class " +
							  "of ConcreteElements in the object structure.")
		static String visitorName = VISITOR_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteVisitorNames",
				description = "The ConcreteVisitor implements each operation declared by the Visitor.")
		static String concreteVisitorNames = CONCRETE_VISITOR_NAMES;
		@CommandLine.Parameters(index = "2", paramLabel = "ElementName",
				description = "The Element defines an Accept operation that takes a visitor as an argument.")
		static String elementName = ELEMENT_NAME;
		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteElementNames",
				description = "The ConcreteElement implements an Accept operation that takes a visitor " +
							  "as an argument.")
		static String concreteElementNames = CONCRETE_ELEMENT_NAMES;

		private InputGroup() {
		}
	}
}
