package com.ashessin.cs474.hw1.creational;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class Prototype {

    public static void main(String[] args) {
        final JavaInterfaceSource prototypeInterface = Roaster.create(JavaInterfaceSource.class);
        prototypeInterface.setPackage("com.gof.creational.prototype").setName("Prototype");
        prototypeInterface.addMethod().setName("copy").setReturnType(prototypeInterface.getName());

        final JavaClassSource concretePrototype = Roaster.create(JavaClassSource.class);
        concretePrototype.setPackage("com.gof.creational.prototype").setName("ConcretePrototype");
        concretePrototype.implementInterface(prototypeInterface);
        concretePrototype.removeImport(prototypeInterface);
        concretePrototype.addProperty(Object.class, "field").removeMutator();
        concretePrototype.addMethod().setConstructor(Boolean.TRUE)
                .setBody("""
                        super();
                        this.field = orignal.field;""")
                .addParameter(concretePrototype.getName(), "orignal");
        concretePrototype.getMethod("copy")
                .setBody("return new " + concretePrototype.getName() + "(this);");

        JavaSourceWriter.save(prototypeInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concretePrototype, "/home/ashesh/workspace/example/src/main/java/");

    }
}
