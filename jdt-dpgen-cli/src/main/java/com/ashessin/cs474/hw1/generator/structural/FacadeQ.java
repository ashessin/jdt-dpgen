package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.ArgGroup;
import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;

@CommandLine.Command(name = "facade", version = "jdt-dpgen 0.1",
		description = "Generates Facade structural design pattern. " +
					  "Provide a unified interface to a set of interfaces in a subsystem. Facade defines a " +
					  "higher-level interface that makes the subsystem easier to use.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class FacadeQ extends DesignPatternQ {

	private static final Logger log = LoggerFactory.getLogger(FacadeQ.class);
	private static final String PACKAGE_NAME = "com.gof.structural.facaed";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new FacadeQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String facadeServiceName = InputGroup.facadeServiceName;
		String serviceName = InputGroup.serviceName;
		List<String> concreteServiceNames = Arrays.asList(InputGroup.concreteServiceNames.split(","));

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new FacadeGen(packageName, facadeServiceName, serviceName, concreteServiceNames).invoke();
	}

	static class InputGroup implements ArgGroup {

		private static final String FACADE_SERVICE_NAME = "FacadeService";
		private static final String SERVICE_NAME = "Service";
		private static final String CONCRETE_SERVICE_NAMES = "Service1,Service2,Service3";

		@CommandLine.Parameters(index = "0", paramLabel = "FacedName",
				description = "The FacadeService, unifies all of our other services.",
				defaultValue = FACADE_SERVICE_NAME)
		static String facadeServiceName = FACADE_SERVICE_NAME;

		@CommandLine.Parameters(index = "1", paramLabel = "ServieName",
				description = "The Service standard service abstracted class.",
				defaultValue = SERVICE_NAME)
		static String serviceName = SERVICE_NAME;

		@CommandLine.Parameters(index = "2", paramLabel = "ConcreteServiceNames",
				description = "The ConcreteServiceNames providing some kind kind of service/action.",
				defaultValue = CONCRETE_SERVICE_NAMES)
		static String concreteServiceNames = CONCRETE_SERVICE_NAMES;

		private InputGroup() {
		}
	}
}
