package com.ashessin.cs474.hw1.creational;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

/**
 * Implements the Singleton design pattern.
 *
 * <p>
 * <b>GoF Definition</b><br>
 * Ensure a class only has one instance, and provide a global point of access to it.
 * </p>
 *
 * <p>
 * Use the Singleton pattern when
 *     <ul>
 *         <li>There must be exactly one instance of a class, and it must be accessible
 *         to clients from a well-known access point.</li>
 *         <li>When the sole instance should be extensible by subclassing, and clients
 *         should be able to use an extended instance without modifying their code.</li>
 *     </ul>
 *
 * <p>
 *     Examples:
 *     <a target="_blank"
 *     href="https://docs.oracle.com/javase/8/docs/api/java/awt/Desktop.html#getDesktop--">
 *         {@code java.awt.Desktop#getDesktop()}</a>,
 *     <a target="_blank"
 *     href="https://docs.oracle.com/javase/8/docs/api/java/lang/Runtime.html#getRuntime--">
 *         {@code java.lang.Runtime#getRuntime()}</a>,
 *     <a target="_blank"
 *     href="https://docs.oracle.com/javase/8/docs/api/java/lang/System.html#getSecurityManager--">
 *         {@code java.lang.System#getSecurityManager()}</a>
 */
public class Singleton {

    public static void main(String[] args) {
        final JavaClassSource singletonClass = Roaster.create(JavaClassSource.class);

        singletonClass.setPackage("com.gof.creational.singleton").setName("Singleton")
                .getJavaDoc().setFullText("""
                The Singleton design pattern is a Creational Pattern.

                <p>
                Its main concern is allowing us to have only one instance of a specific
                object, useful for cases when the object is sharable. e.g., a database
                connection.
                </p>""");

        singletonClass.addProperty(singletonClass.getName(), "instance").removeMutator();
        singletonClass.getProperty("instance").getField()
                .setStatic(Boolean.TRUE)
                .getJavaDoc().setFullText("""
                The field for storing the singleton instance should be declared {@code static}.""");

        singletonClass.getProperty("instance").getAccessor()
                .setStatic(Boolean.TRUE)
                .setSynchronized(Boolean.TRUE)
                .setBody("""
                        if(instance == null) {
                            instance = new Singleton();
                        }
                        return instance;""")
                .getJavaDoc().setFullText("""
                The {@code static} method which controls access to the singleton instance.

                <p>
                This is a thread safe method using {@code synchronized} keyword.
                Additionally, lazy initialization is used and the singleton instance won't be
                created unless required via first call to this method.
                </p>

                @return the instance of class""");

        singletonClass.addMethod()
                .setConstructor(Boolean.TRUE)
                .setPrivate()
                .setBody("")
                .getJavaDoc().setFullText("""
                The constructor should be private to prevent use of {@code new} operator.""");

        JavaSourceWriter.save(singletonClass, "/home/ashesh/workspace/example/src/main/java/");
    }
}
