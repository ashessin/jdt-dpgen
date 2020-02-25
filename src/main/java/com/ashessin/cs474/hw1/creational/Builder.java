package com.ashessin.cs474.hw1.creational;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

/**
 * Implements the Builder design pattern.
 *
 * <p>
 * <b>GoF Definition</b><br>
 * Separate the construction of a complex object from its representation
 * so that the same construction process can create different representations.
 * </p>
 *
 * <p>
 * Use the Builder pattern when
 *     <ul>
 *         <li>The algorithm for creating a complex object should be independent
 *         of the parts that make up the object and how they're assembled.</li>
 *         <li>The construction process must allow different representations for
 *         the object that's constructed.</li>
 *     </ul>
 *
 * <p>
 *     Examples:
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/java/lang/StringBuilder.html#append-boolean-">
 *         {@code java.lang.StringBuilder#append()}</a>,
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/java/lang/StringBuffer.html#append-boolean-">
 *         {@code java.lang.StringBuffer#append()}</a>,
 *     <a target="_blank"
 *     href="https://docs.oracle.com/javase/9/docs/api/java/util/stream/Stream.Builder.html>
 *         {@code java.util.stream.Stream.Builder}</a>
 */
public class Builder {

    public static void main(String[] args) {
        JavaClassSource builder = Roaster.create(JavaClassSource.class);
        builder.setPackage("com.gof.creational.builder").setName("Builder");
        builder.setAbstract(Boolean.TRUE);
        builder.addMethod().setPublic().setAbstract(Boolean.TRUE)
                .setReturnType(builder.getName())
                .setName("createProduct");
        builder.addMethod().setPublic().setAbstract(Boolean.TRUE)
                .setReturnType(builder.getName())
                .setName("buildPart1").addParameter(Object.class.getSimpleName(), "part");
        builder.addMethod().setPublic().setAbstract(Boolean.TRUE)
                .setReturnType(builder.getName())
                .setName("buildPart2").addParameter(Object.class.getSimpleName(), "part");

        JavaClassSource directorClass = Roaster.create(JavaClassSource.class);
        directorClass.setPackage("com.gof.creational.builder").setName("Director");
        directorClass.addProperty(builder, "builder")
                .removeAccessor().removeMutator();
        directorClass.addMethod().setConstructor(Boolean.TRUE)
                .setBody("this.builder = builder;")
                .addParameter(builder, "builder");
        directorClass.addMethod()
                .setPublic()
                .setReturnTypeVoid()
                .setName("build")
                .setBody("""
                        builder.createProduct()
                            .buildPart1("part1")
                            .buildPart2("part2");""");

        JavaClassSource productClass = Roaster.create(JavaClassSource.class);
        productClass.setPackage("com.gof.creational.builder").setName("Product");
        productClass.addProperty(Object.class.getSimpleName(), "part1");
        productClass.addProperty(Object.class.getSimpleName(), "part2");

        JavaClassSource builderClass = Roaster.create(JavaClassSource.class);
        builderClass.setPackage("com.gof.creational.builder").setName("ConcreteBuilder");
        builderClass.extendSuperType(builder);
        builderClass.removeImport(builder);
        builderClass.addProperty(productClass, "product")
                .removeMutator();
        builderClass.getMethod("createProduct")
                .setBody("""
                        this.product = new Product();
                        return this;""");
        builderClass.getMethod("buildPart1", Object.class.getSimpleName())
                .setBody("""
                        product.setPart1(part);
                        return this;""");
        builderClass.getMethod("buildPart2", Object.class.getSimpleName())
                .setBody("""
                        product.setPart2(part);
                        return this;""");

        JavaSourceWriter.save(builder, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(builderClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(productClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(directorClass, "/home/ashesh/workspace/example/src/main/java/");


    }
}
