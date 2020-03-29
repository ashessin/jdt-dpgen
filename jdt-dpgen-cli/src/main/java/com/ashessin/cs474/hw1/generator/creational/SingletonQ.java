package com.ashessin.cs474.hw1.generator.creational;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "singleton", version = "jdt-dpgen 0.1",
		description = "Generates Singleton creational design pattern. " +
					  "Ensure a class only has one instance, and provide a global point of access to it.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class SingletonQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(SingletonQ.class);
	private static final String PACKAGE_NAME = "com.gof.creational.singleton";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputParameterGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		CommandLine cmd = new CommandLine(new SingletonQ());
		System.exit(cmd.execute(args));
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String singletonName = InputGroup.singletonName;
		String singletonInstanceName = InputGroup.singletonInstanceName;
		String singletonAccessorName = InputGroup.singletonAccessorName;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new SingletonGen(packageName, singletonName, singletonInstanceName, singletonAccessorName).invoke();
	}

	static class InputGroup implements ArgGroup {

		private static final String SINGLETON_NAME = "Singleton";
		private static final String SINGLETON_INSTANCE_NAME = "instance";
		private static final String SINGLETON_ACCESSOR_NAME = "get" + SINGLETON_INSTANCE_NAME;

		@CommandLine.Parameters(index = "0", paramLabel = "SingletonName",
				description = "The singleton class should have only one active instance at any time.",
				defaultValue = SINGLETON_NAME)
		static String singletonName = SINGLETON_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "SingletonInstanceName",
				defaultValue = SINGLETON_INSTANCE_NAME)
		static String singletonInstanceName = SINGLETON_INSTANCE_NAME;

		@CommandLine.Parameters(index = "2", paramLabel = "SingletonAccessorName",
				defaultValue = SINGLETON_ACCESSOR_NAME)
		static String singletonAccessorName = SINGLETON_ACCESSOR_NAME;

		private InputGroup() {
		}
	}
}
