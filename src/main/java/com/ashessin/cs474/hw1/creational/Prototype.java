package com.ashessin.cs474.hw1.creational;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

/**
 * Implements the Prototype design pattern.
 *
 * <p>
 * <b>GoF Definition</b><br>
 * Specify the kinds of objects to create using a prototypical instance,
 * and create new objects by copying this prototype.
 * </p>
 *
 * <p>
 * Use the Prototype pattern when
 *     <ul>
 *         <li>Classes to be instantiated are specified at run-time.</li>
 *         <li>Avoiding the creation of a factory hierarchy is needed.</li>
 *         <li>It is more convenient to copy an existing instance than to create
 *         a new one.</li>
 *     </ul>
 *
 * <p>
 *     Examples:
 *     <a target="_blank"
 *     href="http://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#clone--">
 *         {@code java.lang.Object#clone()}</a>
 */
public class Prototype {

    public static void main(String[] args) {
        final JavaInterfaceSource prototypeInterface = Roaster.create(JavaInterfaceSource.class);
        prototypeInterface.setPackage("com.gof.creational.prototype").setName("Prototype");
        prototypeInterface.addMethod().setName("copy").setReturnType(prototypeInterface.getName());

        final JavaClassSource prototypeClass = Roaster.create(JavaClassSource.class);
        prototypeClass.setPackage("com.gof.creational.prototype").setName("ConcretePrototype");
        prototypeClass.implementInterface(prototypeInterface);
        prototypeClass.removeImport(prototypeInterface);
        prototypeClass.addProperty(Object.class, "field").removeAccessor().removeMutator();
        prototypeClass.addMethod().setConstructor(Boolean.TRUE)
                .setPublic()
                .setBody("this.field = field;")
                .addParameter(prototypeClass.getField("field").getType().getName(), "field");
        prototypeClass.addMethod().setConstructor(Boolean.TRUE)
                .setProtected()
                .setBody("""
                        super();
                        this.field = orignal.field;""")
                .addParameter(prototypeClass.getName(), "orignal");
        prototypeClass.getMethod("copy")
                .setBody("return new " + prototypeClass.getName() + "(this);");

        JavaSourceWriter.save(prototypeInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(prototypeClass, "/home/ashesh/workspace/example/src/main/java/");
    }
}
