package com.ashessin.cs474.hw1.creational;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

/**
 * Implements the Abstract Factory design pattern.
 *
 * <p>
 * <b>GoF Definition</b><br>
 * Provide an interface for creating families of related or dependent objects
 * without specifying their concrete classes.
 * </p>
 *
 * <p>
 * Use the Abstract Factory pattern when
 *     <ul>
 *         <li>.</li>
 *         <li>.</li>
 *     </ul>
 *
 * <p>
 *     Examples:
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/javax/xml/parsers/DocumentBuilderFactory.html#newInstance--">
 *         {@code javax.xml.parsers.DocumentBuilderFactory#newInstance()}</a>,
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/javax/xml/transform/TransformerFactory.html#newInstance--">
 *         {@code javax.xml.transform.TransformerFactory#newInstance()}</a>,
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/javax/xml/xpath/XPathFactory.html#newInstance--">
 *         {@code javax.xml.xpath.XPathFactory#newInstance()}</a>
 */
public class AbstractFactory {

    public static void main(String[] args) {

        final JavaInterfaceSource productInterfaceA = Roaster.create(JavaInterfaceSource.class);
        productInterfaceA.setPackage("com.gof.creational.abstractfactory").setName("ProductA");
        productInterfaceA.addMethod().setName("method").setReturnTypeVoid();
        final JavaClassSource productClassA1 = Roaster.create(JavaClassSource.class);
        productClassA1.setPackage("com.gof.creational.abstractfactory").setName("ProductClassA1");
        productClassA1.implementInterface(productInterfaceA);
        productClassA1.removeImport(productInterfaceA);
        final JavaClassSource productClassA2 = Roaster.create(JavaClassSource.class);
        productClassA2.setPackage("com.gof.creational.abstractfactory").setName("ProductClassA2");
        productClassA2.implementInterface(productInterfaceA);
        productClassA2.removeImport(productInterfaceA);

        final JavaInterfaceSource productInterfaceB = Roaster.create(JavaInterfaceSource.class);
        productInterfaceB.setPackage("com.gof.creational.abstractfactory").setName("ProductB");
        productInterfaceB.addMethod().setName("method").setReturnTypeVoid();
        final JavaClassSource productClassB1 = Roaster.create(JavaClassSource.class);
        productClassB1.setPackage("com.gof.creational.abstractfactory").setName("ProductClassB1");
        productClassB1.implementInterface(productInterfaceB);
        productClassB1.removeImport(productInterfaceB);
        final JavaClassSource productClassB2 = Roaster.create(JavaClassSource.class);
        productClassB2.setPackage("com.gof.creational.abstractfactory").setName("ProductClassB2");
        productClassB2.implementInterface(productInterfaceB);
        productClassB2.removeImport(productInterfaceB);

        final JavaClassSource absCreatorClass = Roaster.create(JavaClassSource.class)
                .setAbstract(Boolean.TRUE);
        absCreatorClass.setPackage("com.gof.creational.abstractfactory").setName("Creator");
        absCreatorClass.addMethod()
                .setConstructor(Boolean.FALSE)
                .setPublic()
                .setBody("return " + "factoryMethod1" + "();")
                .setReturnType(productInterfaceA.getName())
                .setName("factory1");
        absCreatorClass.addMethod()
                .setConstructor(Boolean.FALSE)
                .setPublic()
                .setBody("return " + "factoryMethod2" + "();")
                .setReturnType(productInterfaceB.getName())
                .setName("factory2");
        absCreatorClass.addMethod()
                .setProtected()
                .setAbstract(Boolean.TRUE)
                .setName("factoryMethod1")
                .setReturnType(productInterfaceA.getName());
        absCreatorClass.addMethod()
                .setProtected()
                .setAbstract(Boolean.TRUE)
                .setName("factoryMethod2")
                .setReturnType(productInterfaceB.getName());
        final JavaClassSource creatorClass1 = Roaster.create(JavaClassSource.class);
        creatorClass1.setPackage("com.gof.creational.abstractfactory").setName("CreatorClass1");
        creatorClass1.extendSuperType(absCreatorClass);
        creatorClass1.removeImport(absCreatorClass);
        creatorClass1.getMethod("factoryMethod1")
                .setBody("return new " + productClassA1.getName() + "();");
        creatorClass1.getMethod("factoryMethod2")
                .setBody("return new " + productClassB1.getName() + "();");
        final JavaClassSource creatorClass2 = Roaster.create(JavaClassSource.class);
        creatorClass2.setPackage("com.gof.creational.abstractfactory").setName("CreatorClass2");
        creatorClass2.extendSuperType(absCreatorClass);
        creatorClass2.removeImport(absCreatorClass);
        creatorClass2.getMethod("factoryMethod1")
                .setBody("return new " + productClassA2.getName() + "();");
        creatorClass2.getMethod("factoryMethod2")
                .setBody("return new " + productClassB2.getName() + "();");

        JavaSourceWriter.save(productInterfaceA, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(productClassA1, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(productClassA2, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(productInterfaceB, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(productClassB1, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(productClassB2, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(absCreatorClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(creatorClass1, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(creatorClass2, "/home/ashesh/workspace/example/src/main/java/");
    }
}
