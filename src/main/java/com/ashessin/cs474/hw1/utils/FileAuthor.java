package com.ashessin.cs474.hw1.utils;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Saves a source/test file (*.java) assuming a maven project structure.
 */
public class FileAuthor {

	private static final Logger log = (Logger) LoggerFactory.getLogger(FileAuthor.class);

	private FileAuthor() {
	}

	// saves a source file (in java) assuming a maven project structure
	public static void saveMain(final byte[] javaSource, final String canonicalName, final Path srcDirectory) {
		final Path javaFile = Paths.get(srcDirectory.toString(),
			"/src/main/java/", canonicalName.replace(".", "/") + ".java"
		);

		save(javaSource, javaFile);
	}

	// saves a test file (in java) assuming a maven project structure
	public static void saveTest(final byte[] javaSource, final String canonicalName, final Path srcDirectory) {
		final Path javaFile = Paths.get(srcDirectory.toString(),
			"/src/test/java/", canonicalName.replace(".", "/") + ".java"
		);

		save(javaSource, javaFile);
	}

	private static void save(final byte[] javaSource, final Path srcFile) {
		try {
			Files.createDirectories(srcFile.getParent());

			if (Files.exists(srcFile)) {
				log.warn("Target file {} already exists, overwriting.", srcFile);
			} else {
				log.info("Attempting to write new file: {}", srcFile);
			}

			Files.write(srcFile, javaSource);
			log.info("Wrote {} bytes to file.", javaSource.length);

		} catch (IOException e) {
			log.error("Failed to write .java file.", e.getCause());
		}
	}
}
