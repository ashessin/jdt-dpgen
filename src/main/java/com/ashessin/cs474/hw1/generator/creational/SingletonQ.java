package com.ashessin.cs474.hw1.generator.creational;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "singleton", version = "jdt-dpgen 0.1",
	description = "Generates Singleton creational design pattern. " +
				  "Ensure a class only has one instance, and provide a global point of access to it.",
	mixinStandardHelpOptions = true,
	showDefaultValues = true,
	sortOptions = false
)
public class SingletonQ implements Callable<DpArrayList<DpSource>> {

	private static final Logger log = (Logger) LoggerFactory.getLogger(SingletonQ.class);
	private static final String PACKAGE_NAME = "com.gof.creational.singleton";

	@CommandLine.Spec
	CommandLine.Model.CommandSpec spec;

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

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
		return new SingletonGen(packageName, singletonName, singletonInstanceName, singletonAccessorName).main();
	}

	static class InputGroup {

		private static final String SINGLETON_NAME = "Singleton";
		private static final String SINGLETON_INSTANCE_NAME = "instance";
		private static final String SINGLETON_ACCESSOR_NAME = "get" + SINGLETON_INSTANCE_NAME;
		@CommandLine.Parameters(index = "0", paramLabel = "SingletonName",
			description = "The singleton class should have only one active instance at any time.")
		static String singletonName = SINGLETON_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "SingletonInstanceName")
		static String singletonInstanceName = SINGLETON_INSTANCE_NAME;
		@CommandLine.Parameters(index = "2", paramLabel = "SingletonAccessorName")
		static String singletonAccessorName = SINGLETON_ACCESSOR_NAME;

		private InputGroup() {
		}
	}
}
