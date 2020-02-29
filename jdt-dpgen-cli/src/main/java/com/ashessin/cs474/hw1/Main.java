package com.ashessin.cs474.hw1;

import com.ashessin.cs474.hw1.generator.DesignPatternQ;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import com.ashessin.cs474.hw1.generator.behavioral.*;
import com.ashessin.cs474.hw1.generator.creational.*;
import com.ashessin.cs474.hw1.generator.structural.*;
import com.ashessin.cs474.hw1.parser.DpSourceParser;
import com.ashessin.cs474.hw1.utils.FileAuthor;
import com.ashessin.cs474.hw1.utils.MavenInvoker;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static java.util.stream.Collectors.toList;

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

	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private static final String CONFIG = "reference.conf";

	private Map<String, List<byte[]>> processedResultMap = new HashMap<>(1);

	@CommandLine.Option(names = {"-l", "--outputLanguage"},
			description = "The output language for the implement.\n" +
						  "  Candidates: ${COMPLETION-CANDIDATES}",
			defaultValue = "java",
			required = true
	)
	private OutputLanguage outputLanguage = OutputLanguage.JAVA;

	@CommandLine.Option(names = {"-d", "--outputLocation"},
			description = "The folder/directory path for saving generated files.",
			defaultValue = "${sys:user.dir}",
			required = true
	)
	private Path outputLocation;

	@CommandLine.Option(names = {"-g", "--groupId"},
			description = "The id of the project's organisation or group.",
			defaultValue = "com.gof",
			required = true
	)
	private String groupId = "com.gof";

	@CommandLine.Option(names = {"-a", "--artifactId"},
			description = "The id of the artifact (project).",
			defaultValue = "dpgen-output",
			required = true
	)
	private String artifactId = "dpgen-output";

	@CommandLine.Option(names = {"-c", "--config"},
			description = "Path to input configuration file.",
			arity = "0..1",
			fallbackValue = CONFIG
	)
	private Path configLocation;

	// this example implements Callable, so parsing, error handling and handling user
	// requests for usage help or version help can be done with one line of code.
	public static void main(String... args) {
		System.setProperty("picocli.usage.width", "auto");
		Main main = new Main();
		main.processResultMap(main, args);
		if (!main.processedResultMap.isEmpty()) {
			new MavenProject(main).invoke();
		}
		System.exit(0);
	}

	public Map<String, List<byte[]>> processResultMap(Main main, String... args) {
		CommandLine cmd = new CommandLine(main);
		cmd.execute(args);
		if (args.length != 0) {
			new SubcmdProcessor(cmd).invoke();
		}
		return main.processedResultMap;
	}

	@Override
	public Integer call() throws Exception {
		//TODO: set proper exit status

		outputLocation = outputLocation.toAbsolutePath();
		if (configLocation != null) {
			configLocation = configLocation.toAbsolutePath();
			new ConfigProcessor(configLocation).invoke();
		}

		return 0;
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

	private static final class ConfigProcessor {

		private static final String PARAMETERS = "parameters";
		private static final String OPTION = "option";
		private Config config;

		private ConfigProcessor(Path configLocation) {
			File configFile = new File(configLocation.toString());
			try {
				log.info("Trying to load {} config file.", configLocation);
				if (!configFile.exists()) throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				log.warn("Could not find {} config file, loading fallback config.", configLocation);
				configFile = new File(String.valueOf(Thread.currentThread()
						.getContextClassLoader()
						.getResourceAsStream(CONFIG)));
			}

			config = ConfigFactory.load(configFile.getPath());
		}

		private void invoke() {
			StringBuilder sb = new StringBuilder();
			for (Config conf : config.getConfigList("jdt-dpgen")) {
				if (!conf.hasPath("command")) throw new AssertionError("Invalid config file.");

				// process command(s)
				sb.append(conf.getString("command")).append(" ");

				// process command Parameter(s)
				if (conf.hasPath(PARAMETERS) && !conf.getIsNull(PARAMETERS)) {
					sb.append(String.join(" ", conf.getStringList(PARAMETERS)));
				}

				// process command Option(s)
				if (conf.hasPath(OPTION) && !conf.getIsNull(OPTION)) {
					for (Map.Entry<String, ConfigValue> entry : conf.getObject(OPTION).entrySet()) {
						String key = entry.getKey();
						ConfigValue configValue = entry.getValue();
						String value = configValue.unwrapped().toString();
						sb.append(" -").append(key).append("=").append(value).append(" ");
					}
				}
			}

			log.info("Executing command: {}", sb);
			main(sb.toString().split(" "));
		}
	}

	private static final class SubcmdProcessor {

		private Main main;
		private List<CommandLine> cliList;

		private SubcmdProcessor(CommandLine cmd) {
			main = cmd.getCommand();
			cliList = cmd.getParseResult().asCommandLineList().stream()
					.filter(c -> c.getExecutionResult() != null &&
								 c.getCommand() instanceof DesignPatternQ)
					.collect(toList());
		}

		public void invoke() {
			if (!cliList.isEmpty()) {
				log.info("Processing {} sub-commands.", cliList.size());

				// initialize roaster backend
				final DpSourceParser dpSourceParser = DpSourceParser.getParser(DpSourceParser.Backend.ROASTER);

				// for each sub command, process returned results
				for (CommandLine commandLine : cliList) {
					log.info("Begin processing {} sub-command.", commandLine.getCommandName());
					if (!(commandLine.getExecutionResult() instanceof DpArrayList)) throw new AssertionError();
					final DpArrayList<DpSource> executionResults = commandLine.getExecutionResult();
					executionResults.parallelStream().forEach(dpSource -> {
						byte[] dpSourceBytes = dpSourceParser.serialize(dpSource);
						String canonicalName = dpSource.getCanonicalName();
						if (main.processedResultMap.containsKey(canonicalName)) {
							main.processedResultMap.get(canonicalName).add(dpSourceBytes);
						} else {
							main.processedResultMap.put(canonicalName, Collections.singletonList(dpSourceBytes));
						}
					});
					log.info("Cease processing {} sub-command.", commandLine.getCommandName());
				}

				log.info("Processed all sub-commands.");
			}
		}
	}

	private static final class MavenProject {

		private static final String POM_XML = "pom.xml";
		private Main main;

		private MavenProject(Main main) {
			this.main = main;
		}

		private void invoke() {
			Path srcDirectory = Paths.get(String.valueOf(main.outputLocation), main.artifactId);
			for (Map.Entry<String, List<byte[]>> entry : main.processedResultMap.entrySet()) {
				String canonicalName = entry.getKey();
				List<byte[]> bytesList = entry.getValue();
				bytesList.parallelStream()
						.forEach(bytes -> FileAuthor.saveMain(bytes, canonicalName, srcDirectory));
			}

			if (new File(srcDirectory + File.separator + POM_XML).exists()) {
				log.warn("File pom.xml already exists for project {}, not invoking maven.", main.artifactId);
			} else {
				log.info("Setting up {} as new Maven project.", main.artifactId);
				if (MavenInvoker.newJavaProject(main.outputLocation, main.groupId, main.artifactId)) {
					log.info("Successfully setup {} as maven project.", main.artifactId);
				}
				try {
					if (main.outputLanguage.toString().equalsIgnoreCase("scala")) {
						log.info("Beginning Java to Scala transformation.");
						if (MavenInvoker.scalagen(main.outputLocation, main.artifactId)) {
							log.info("Successfully converted {} source files to scala.", main.artifactId);
						}
					}
				} catch (IOException e) {
					log.error("Exception while generating Scala source: ", e);
				}
			}
			log.info("Generated design patterns are available in {} directory.", srcDirectory);
		}
	}
}