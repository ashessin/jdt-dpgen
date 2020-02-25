package com.ashessin.cs474.hw1.utils;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

	private static final Logger log = (Logger) LoggerFactory.getLogger(FileHelper.class);

	private FileHelper() {
	}

	public static void save(String javaSource, String canonicalName, Path srcDirectory) {
		try {
			Path javaFile = Paths.get(srcDirectory.toString(),
					"/src/main/java/", canonicalName.replace(".", "/") + ".java"
			);

			Files.createDirectories(javaFile.getParent());
			log.info("Attempting to write to {}", javaFile);
			if (Files.exists(javaFile)) {
				log.warn("Target file {} already exists, overwriting.", javaFile);
			}

			byte[] javaSourceBytes = javaSource.getBytes(StandardCharsets.UTF_8);
			Files.write(javaFile, javaSourceBytes);
			log.info("Wrote {} bytes to file.", javaSourceBytes.length);

		} catch (IOException e) {
			log.error("Failed to write .java file.", e.getCause());
		}
	}
}
