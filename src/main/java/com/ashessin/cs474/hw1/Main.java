package com.ashessin.cs474.hw1;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.generator.behavioral.*;
import com.ashessin.cs474.hw1.generator.creational.*;
import com.ashessin.cs474.hw1.generator.structural.*;
import com.ashessin.cs474.hw1.utils.CliExtension;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "dpgen", version = "jdt-dpgen 0.1",
	description = "Generates GOF design patterns in Java and Scala language.",
	mixinStandardHelpOptions = true,
	subcommands = {
		// Creational Design Patterns
		SingletonQ.class, PrototypeQ.class, BuilderQ.class, FactoryMethodQ.class, AbstractFactoryQ.class,
		// Structural Design Patterns
		AdapterQ.class, BridgeQ.class, CompositeQ.class, DecoratorQ.class, FacadeQ.class, FlyweightQ.class,
		ProxyQ.class,
		// Behavioral Design Patterns
		ChainOfResponsibilityQ.class, CommandQ.class, InterpreterQ.class, IteratorQ.class, MediatorQ.class,
		MementoQ.class, ObserverQ.class, StateQ.class, StrategyQ.class, TemplateMethodQ.class, VisitorQ.class
	},
	synopsisSubcommandLabel = "COMMAND COMMAND...",
	subcommandsRepeatable = true,
	showDefaultValues = true,
	sortOptions = false
)
public class Main implements Callable<Integer> {

	private static final Logger log = (Logger) LoggerFactory.getLogger(Main.class);

	@CommandLine.Option(names = {"-l", "--outputLanguage"},
		description = "The output language for the implement.\n" +
					  "  Candidates: ${COMPLETION-CANDIDATES}",
		defaultValue = "java",
		required = true
	)
	OutputLanguage outputLanguage = OutputLanguage.JAVA;

	@CommandLine.Option(names = {"-d", "--outputLocation"},
		description = "The folder/directory location for saving generated files.",
		defaultValue = "${sys:user.dir}",
		required = true
	)
	Path outputLocation;

	@CommandLine.Option(names = {"-a", "--artifactId"},
		description = "Unique base name of the primary artifact being generated.",
		defaultValue = "dpgen-output",
		required = true
	)
	String artifactId;

	@CommandLine.Option(names = {"-g", "--groupId"},
		description = "Unique identifier of the organization or group.",
		defaultValue = "com.gof",
		required = true
	)
	String groupId;

	@CommandLine.Option(names = {"-c", "--config"},
		description = "Path to input configuration file.",
		arity = "0..1",
		fallbackValue = "reference.conf"
	)
	Path configFile;

	// this example implements Callable, so parsing, error handling and handling user
	// requests for usage help or version help can be done with one line of code.
	public static void main(String... args) {
		System.setProperty("picocli.usage.width", "auto");
		CommandLine cmd = new CommandLine(new Main());
		int cmdExitCode = cmd.execute(args);
		if (args.length != 0) CliExtension.processResult(cmd);
		System.exit(cmdExitCode);
	}

	@Override
	public Integer call() throws Exception {
		//TODO: set proper exit status
		if (configFile != null) CliExtension.processConfig(configFile);
		return 0;
	}

	public OutputLanguage getOutputLanguage() {
		return outputLanguage;
	}

	public Path getOutputLocation() {
		return outputLocation;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public enum OutputLanguage {
		JAVA("java"), SCALA("scala");
		private String name;

		OutputLanguage(String name) {
			this.name = name.toLowerCase();
		}

		@Override
		public String toString() {
			return name;
		}
	}
}