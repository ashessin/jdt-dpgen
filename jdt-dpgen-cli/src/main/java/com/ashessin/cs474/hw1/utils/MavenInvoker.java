package com.ashessin.cs474.hw1.utils;

import org.apache.maven.shared.invoker.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Invokes the mvn command to setup a java/scala project.
 * <p>
 * Note that Maven must be pre-installed and environment variables {@code MAVEN_HOME}, {@code M2_HOME}
 * must point to the installation location.
 */
public class MavenInvoker {

	private static final Logger log = LoggerFactory.getLogger(MavenInvoker.class);

	private MavenInvoker() {
	}

	/**
	 * Creates a new Java maven project.
	 *
	 * @param mavenProjectRoot parent directory path to an existing maven project directory
	 * @param groupId          unique identifier of the organization or group
	 * @param artifactId       unique base name of the primary artifact
	 *
	 * @return 0 on success; otherwise -1
	 */
	public static int newJavaProject(final Path mavenProjectRoot, final String groupId, final String artifactId) {
		String command = String.format("-q archetype:generate \\\n" +
									   "    -DgroupId=%s \\\n" +
									   "    -DartifactId=%s \\\n" +
									   " 	-DarchetypeGroupId=pl.org.miki \\\n" +
									   "    -DarchetypeArtifactId=java8-quickstart-archetype \\\n" +
									   "    -DarchetypeVersion=1.0.0 \\\n" +
									   "    -DinteractiveMode=false", groupId, artifactId);
		return run(command, mavenProjectRoot);
	}

	/**
	 * Creates a new Scala maven project.
	 *
	 * @param mavenProjectRoot parent directory path to an existing maven project directory
	 * @param groupId          unique identifier of the organization or group
	 * @param artifactId       unique base name of the primary artifact
	 *
	 * @return 0 on success; otherwise -1
	 */
	public static int newScalaProject(final Path mavenProjectRoot, final String groupId, final String artifactId) {
		String command = String.format("-q archetype:generate -B \\\n" +
									   "    -DarchetypeGroupId=net.alchim31.maven \\\n" +
									   "    -DarchetypeArtifactId=scala-archetype-simple \\\n" +
									   "    -DarchetypeVersion=1.7 \\\n" +
									   "    -DgroupId=%s \\\n" +
									   "    -DartifactId=%s \\\n" +
									   "    -DinteractiveMode=false", groupId, artifactId);
		return run(command, mavenProjectRoot);
	}

	/**
	 * Adds {@code com.mysema.scalagen.scalagen-maven-plugin} as a plugin in existing maven project and
	 * then invokes it to convert java to scala code.
	 *
	 * @param mavenProjectRoot parent directory path to an existing maven project directory
	 * @param artifactId       unique base name of the primary artifact
	 *
	 * @return 0 on success; otherwise -1
	 *
	 * @throws IOException might fail to write the *.pom.xml file
	 */
	public static int scalagen(final Path mavenProjectRoot, final String artifactId) throws IOException {
		final Path path = Paths.get(mavenProjectRoot.toString(), artifactId, "pom.xml");

		final String snippet = "<plugin>\n" +
							   "  <groupId>com.mysema.scalagen</groupId>\n" +
							   "  <artifactId>scalagen-maven-plugin</artifactId>\n" +
							   "  <version>0.2.2</version>\n" +
							   "</plugin>\n" +
							   "</plugins>";

		String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
		content = content.replace("</plugins>", snippet);
		Files.write(path, content.getBytes(StandardCharsets.UTF_8));
		return run("-q scalagen:main", path.getParent());
	}

	/**
	 * Complies a maven project.
	 *
	 * @param mavenProjectRoot parent directory path to an existing maven project directory
	 * @param artifactId       unique base name of the primary artifact
	 *
	 * @return 0 on success; otherwise -1
	 */
	public static int complile(final Path mavenProjectRoot, final String artifactId) {
		final Path path = Paths.get(mavenProjectRoot.toString(), artifactId, "pom.xml");
		final String command = "compile";
		return run(command, path.getParent());
	}

	private static int run(final String command, final Path mavenProjectRoot) {
		final InvocationRequest request = new DefaultInvocationRequest();
		request.setBatchMode(true);
		request.setPomFile(new File(mavenProjectRoot + "/pom.xml"));
		request.setGoals(Collections.singletonList(command));

		Invoker invoker = new DefaultInvoker();
		try {
			log.info("Invoking mvn command: mvn {}", command);
			invoker.execute(request);
			return 0;
		} catch (MavenInvocationException | IllegalStateException e) {
			log.error("Exception while executing mvn command: {}", e.getMessage());
			log.info("Above exception usually occurs when the location of maven command can't be determined. " +
					 "Please make sure `MAVEN_HOME` and `M2_HOME` environment variables " +
					 "are set and point to maven installation directory");
		}
		return -1;
	}
}
