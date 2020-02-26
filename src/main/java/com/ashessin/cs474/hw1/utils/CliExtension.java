package com.ashessin.cs474.hw1.utils;

import ch.qos.logback.classic.Logger;
import com.ashessin.cs474.hw1.Main;
import com.ashessin.cs474.hw1.generator.DpArrayList;
import com.ashessin.cs474.hw1.generator.DpSource;
import com.ashessin.cs474.hw1.parser.DpSourceParser;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

import static com.ashessin.cs474.hw1.utils.MavenInvoker.scalagen;
import static java.util.stream.Collectors.toList;

/**
 * Provides helper functions to post-process results from a command (including subcommands).
 */
public class CliExtension {

	private static final Logger log = (Logger) LoggerFactory.getLogger(CliExtension.class);
	private static final String PARAMETERS = "parameters";
	private static final String OPTION = "option";

	private CliExtension() {
	}

	/**
	 * Takes an entire command line interpretation and further evaluates each sub command.
	 *
	 * @param cmd some command line interpretation (obtained from cli arguments)
	 */
	public static void processResult(CommandLine cmd) {
		// find all sub commands ie. In "dpgen singleton factorymethod",
		// "singleton" and "factorymethod" are sub commands
		final List<CommandLine> subCmd = cmd.getParseResult().asCommandLineList().stream()
			.filter(c -> c.getExecutionResult() != null && !(c.getCommand() instanceof Main))
			.collect(toList());

		// if user directly invoked a subcommand, return without furtehr proessing
		// note that this will only genrate custom DpSource representations and
		// no files will be generated
		if (subCmd.isEmpty()) {
			return;
		} else {
			log.info("Processing {} sub-commands.", subCmd.size());
		}

		// parent command must be `jdt-dpgen`
		if (!(cmd.getCommand() instanceof Main)) throw new InputMismatchException("Please prefix jdt-dpgen.");
		final Main main = cmd.getCommand();
		final DpSourceParser dpSourceParser = DpSourceParser.getParser(DpSourceParser.Backend.ROASTER);
		final Path location = main.getOutputLocation();
		final String artifactId = main.getArtifactId();

		// for each sub command, process returned results
		subCmd.stream().parallel().forEach(commandLine -> {
			log.info("Processing {} sub-command.", commandLine.getCommandName());
			if (!(commandLine.getExecutionResult() instanceof DpArrayList)) throw new AssertionError();
			final DpArrayList<DpSource> executionResult = commandLine.getExecutionResult();

			Path srcDirectory = Paths.get(location.toString(), artifactId);
			parseDP(executionResult, srcDirectory, dpSourceParser);
		});

		final String language = main.getOutputLanguage().toString();
		final String groupId = main.getGroupId();

		if (new File(String.valueOf(location), artifactId + "/pom.xml").exists()) {
			log.warn("File pom.xml already exists for project {}, not invoking maven.", artifactId);
		} else {
			log.info("Setting up {} as new Maven project.", artifactId);
			try {
				MavenInvoker.newJavaProject(location, groupId, artifactId);
				log.info("Successfully setup {} as maven project.", artifactId);
				if (language.equalsIgnoreCase("scala")) {
					log.info("Beginning Java to Scala transformation.");
					scalagen(location, artifactId);
					log.info("Successfully converted {} source files to scala.", artifactId);
				}
			} catch (IOException e) {
				log.error("Exception while generating Scala source: ", e);
			}
		}

		log.info("Generated design patterns are available in {} directory.",
				new File(String.valueOf(location), artifactId));
	}

	private static void parseDP(final DpArrayList<DpSource> dpSources,
								final Path srcDirectory, final DpSourceParser dpSourceParser) {
		dpSources.stream().parallel().forEach(dpSource -> {
			String canonicalName = dpSource.getPackageName() + "." + dpSource.getName();
			FileAuthor.saveMain(dpSourceParser.serialize(dpSource), canonicalName, srcDirectory);
		});
	}

	private static int scalagen(final Path location, final String artifactId) throws IOException {
		return MavenInvoker.scalagen(location, artifactId);
	}

	/**
	 * Processes a configuration file as input when using the {@code -c} option
	 *
	 * @param configFile path to the config file
	 *
	 * @throws URISyntaxException for malformed URI
	 */
	public static void processConfig(Path configFile) {
		final Config config;
		File file = configFile.toAbsolutePath().toFile();
		log.info("Trying to load {} config file.", file);
		if (!file.exists()) {
			log.warn("Could not find {} config file, loading fallback config.", file);
			file = new File(Objects.requireNonNull(
				Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("reference.conf"))
				.toString());
		}
		config = ConfigFactory.load(file.getPath());
		StringBuilder c = new StringBuilder();
		config.getConfigList("jdt-dpgen").forEach(o -> {
			c.append(o.getString("command")).append(" ");
			if (o.hasPath(PARAMETERS) && !o.getIsNull(PARAMETERS))
				c.append(String.join(" ", o.getStringList(PARAMETERS)));
			if (o.hasPath(OPTION)) {
				o.getObject(OPTION).forEach((key, value) -> {
					c.append(" -").append(key).append("=").append(value.unwrapped().toString());
					c.append(" ");
				});
			}
		});
		log.info("Executing command: {}", c);
		Main.main(c.toString().split(" "));
	}
}
