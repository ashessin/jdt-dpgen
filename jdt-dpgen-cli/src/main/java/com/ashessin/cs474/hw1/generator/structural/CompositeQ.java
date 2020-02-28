package com.ashessin.cs474.hw1.generator.structural;

import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "composite", version = "jdt-dpgen 0.1",
		description = "Generates Composite structural design pattern. " +
					  "Compose objects into tree structures to represent part-whole hierarchies. " +
					  "Composite lets clients treat individual objects and compositions of objects uniformly.",
		mixinStandardHelpOptions = true,
		showDefaultValues = true,
		sortOptions = false
)
public class CompositeQ implements Callable<DpArrayList<DpSource>> {

	private static final Logger log = LoggerFactory.getLogger(CompositeQ.class);
	private static final String PACKAGE_NAME = "com.gof.structural.composite";

	@CommandLine.ArgGroup(exclusive = false)
	InputGroup inputGroup;

	@CommandLine.Option(order = Integer.MIN_VALUE, required = true, arity = "1", names = {"-p", "--packageName"},
			defaultValue = PACKAGE_NAME)
	String packageName;

	public static void main(String[] args) {
		System.setProperty("picocli.usage.width", "auto");
		int exitCode = new CommandLine(new CompositeQ()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public DpArrayList<DpSource> call() throws Exception {
		String componentName = InputGroup.componentName;
		String compositeName = InputGroup.compositeName;
		String leafName = InputGroup.leafName;

		// TODO: Add input validations

		log.info("Generating representation for design pattern sources.");
		return new CompositeGen(packageName, componentName, compositeName, leafName).main();

	}

	static class InputGroup {

		private static final String COMPONENT_NAME = "Component";
		private static final String COMPOSITE_NAME = "Composite";
		private static final String LEAF_NAME = "Leaf";
		@CommandLine.Parameters(index = "0", paramLabel = "ComponentName",
				description = "The Component abstract class declares the interface for objects in the " +
							  "composition, implements default behavior for the interface common to all classes as " +
							  "appropriate, and declares an interface for accessing and managing its child components.")
		static String componentName = COMPONENT_NAME;
		@CommandLine.Parameters(index = "1", paramLabel = "CompositeName",
				description = "The Composite class defines behavior for components having children, " +
							  "stores the child components and implements the child-related operations in the " +
							  "Component interface.")
		static String compositeName = COMPOSITE_NAME;
		@CommandLine.Parameters(index = "2", paramLabel = "LeafName",
				description = "The Leaf class represents leaf objects in the composition.")
		static String leafName = LEAF_NAME;

		private InputGroup() {
		}
	}
}
