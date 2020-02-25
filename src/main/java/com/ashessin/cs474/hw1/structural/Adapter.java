package com.ashessin.cs474.hw1.structural;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class Adapter {

    public static void main(String[] args) {
        final JavaClassSource adapteeClass = Roaster.create(JavaClassSource.class);
        adapteeClass.setPackage("com.gof.structural.adapter").setName("Adaptee");

        final JavaClassSource targetClass = Roaster.create(JavaClassSource.class);
        targetClass.setPackage("com.gof.structural.adapter").setName("Target");

        final JavaInterfaceSource adapterInterface = Roaster.create(JavaInterfaceSource.class);
        adapterInterface.setPackage("com.gof.structural.adapter").setName("Adapter");
        adapterInterface.addMethod().setName("specialRequest").setReturnType(targetClass);

        final JavaClassSource concreteAdapter = Roaster.create(JavaClassSource.class);
        concreteAdapter.setPackage("com.gof.structural.adapter").setName("ConcreteAdapter");
        concreteAdapter.implementInterface(adapterInterface);
        concreteAdapter.removeImport(adapterInterface);
        concreteAdapter.addField()
                .setPrivate()
                .setName("adaptee")
                .setType(adapteeClass.getName())
                .setLiteralInitializer("new " + adapteeClass.getName() + "()");

        JavaSourceWriter.save(adapteeClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(targetClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(adapterInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteAdapter, "/home/ashesh/workspace/example/src/main/java/");
    }
}
