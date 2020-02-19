package com.ashessin.cs474.hw1.utils;

import org.jboss.forge.roaster.model.source.JavaSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JavaSourceWriter {

    public static void save(JavaSource<?> javaSource, String sourcePath) {
        try {
            Path p = Paths.get(sourcePath +
                               javaSource.getCanonicalName().replace(".", File.separator) + ".java");
            File f = new File(p.getParent().toString());
            f.mkdirs();
            Files.write(p, javaSource.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
