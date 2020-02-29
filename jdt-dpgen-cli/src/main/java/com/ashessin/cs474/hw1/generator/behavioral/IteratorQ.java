package com.ashessin.cs474.hw1.generator.behavioral;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "iterator", version = "jdt-dpgen 0.1",
		description = "Generates Iterator behavioral design pattern. " +
					  "Provide a way to access the elements of an aggregate object sequentially without exposing " +
					  "its underlying representation.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class IteratorQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(IteratorQ.class);
	private static final String PACKAGE_NAME = "com.gof.behavioral.iterator";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new IteratorQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String aggregateName = InputGroup.aggregateName;
		String concreteAggregateName = InputGroup.concreteAggregateName;
		String iteratorName = InputGroup.iteratorName;
		String concreteIteratorName = InputGroup.concreteIteratorName;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new IteratorGen(packageName, aggregateName, concreteAggregateName, iteratorName, concreteIteratorName).method();

	}

	static class InputGroup implements ArgGroup {

		private static final String AGGREGATE_NAME = "Aggregate";
		private static final String CONCRETE_AGGREGATE_NAME = "Concrete" + AGGREGATE_NAME;
		private static final String ITERATOR_NAME = "Iterator";
		private static final String CONCRETE_ITERATOR_NAME = "Concrete" + ITERATOR_NAME;


		@CommandLine.Parameters(index = "0", paramLabel = "AggregateName",
				description = "The Aggregate defines an interface for creating an Iterator object.",
				defaultValue = AGGREGATE_NAME)
		static String aggregateName = AGGREGATE_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "ConcreteAggregateName",
				description = "The ConcreteAgregate class implements the Iterator creation interface to return an " +
							  "instance of the proper ConcreteIterator.",
				defaultValue = CONCRETE_AGGREGATE_NAME)
		static String concreteAggregateName = CONCRETE_AGGREGATE_NAME;

		@CommandLine.Parameters(index = "2", paramLabel = "IteratorName",
				description = "The Iterator defines an interface for accessing and traversing elements.",
				defaultValue = ITERATOR_NAME)
		static String iteratorName = ITERATOR_NAME;

		@CommandLine.Parameters(index = "3", paramLabel = "ConcreteIteratorName",
				description = "The ConcreteIterator implements the Iterator interface, keeps track of the current " +
							  "position in the traversal of the aggregate.",
				defaultValue = CONCRETE_ITERATOR_NAME)
		static String concreteIteratorName = CONCRETE_ITERATOR_NAME;

		private InputGroup() {
		}
	}
}
