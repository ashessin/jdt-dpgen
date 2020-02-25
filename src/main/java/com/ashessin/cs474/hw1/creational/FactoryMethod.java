package com.ashessin.cs474.hw1.creational;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

/**
 * Implements the Factory Method design pattern.
 *
 * <p>
 * <b>GoF Definition</b><br>
 * The Factory Method Patter defines an interface for creating an object,
 * but lets subclasses decide which class to instantiate. Factory Method lets a
 * class defer instantiation to subclasses.
 * </p>
 *
 * <p>
 * Use the Factory Method pattern when
 *     <ul>
 *         <li>A class can't anticipate the type of the objects it is supposed to create.</li>
 *         <li>A class wants its subclasses to specify the objects it creates.</li>
 *     </ul>
 *
 * <p>
 *     Examples:
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/java/util/Calendar.html#getInstance--">
 *         {@code java.util.Calendar#getInstance()}</a>,
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/java/util/ResourceBundle.html#getBundle-java.lang.String-">
 *         {@code java.util.ResourceBundle#getBundle()}</a>,
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/java/text/NumberFormat.html#getInstance--">
 *         {@code java.text.NumberFormat#getInstance()}</a>
 */
public class FactoryMethod {

    public static void main(String[] args) {

        final JavaInterfaceSource productInterface = Roaster.create(JavaInterfaceSource.class);
        productInterface.setPackage("com.gof.creational.factorymethod").setName("Product");
        productInterface.addMethod().setName("method").setReturnTypeVoid();
        final JavaClassSource productClassA = Roaster.create(JavaClassSource.class);
        productClassA.setPackage("com.gof.creational.factorymethod").setName("ProductClassA");
        productClassA.implementInterface(productInterface);
        productClassA.removeImport(productInterface);
        final JavaClassSource productClassB = Roaster.create(JavaClassSource.class);
        productClassB.setPackage("com.gof.creational.factorymethod").setName("ProductClassB");
        productClassB.implementInterface(productInterface);
        productClassB.removeImport(productInterface);

        final JavaClassSource absCreatorClass = Roaster.create(JavaClassSource.class)
                .setAbstract(Boolean.TRUE);
        absCreatorClass.setPackage("com.gof.creational.factorymethod").setName("Creator");
        absCreatorClass.addMethod()
                .setConstructor(Boolean.FALSE)
                .setPublic()
                .setBody("return " + "factoryMethod" + "();")
                .setReturnType(productInterface.getName())
                .setName("factory");
        absCreatorClass.addMethod()
                .setProtected()
                .setAbstract(Boolean.TRUE)
                .setName("factoryMethod")
                .setReturnType(productInterface.getName());
        final JavaClassSource creatorClassA = Roaster.create(JavaClassSource.class);
        creatorClassA.setPackage("com.gof.creational.factorymethod").setName("CreatorClassA");
        creatorClassA.extendSuperType(absCreatorClass);
        creatorClassA.removeImport(absCreatorClass);
        creatorClassA.getMethod("factoryMethod")
                .setBody("return new " + productClassA.getName() + "();");
        final JavaClassSource creatorClassB = Roaster.create(JavaClassSource.class);
        creatorClassB.setPackage("com.gof.creational.factorymethod").setName("CreatorClassB");
        creatorClassB.extendSuperType(absCreatorClass);
        creatorClassB.removeImport(absCreatorClass);
        creatorClassB.getMethod("factoryMethod")
                .setBody("return new " + productClassB.getName() + "();");

        JavaSourceWriter.save(productInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(productClassA, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(productClassB, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(absCreatorClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(creatorClassA, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(creatorClassB, "/home/ashesh/workspace/example/src/main/java/");
    }
}
