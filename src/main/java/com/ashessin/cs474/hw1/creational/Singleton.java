package com.ashessin.cs474.hw1.creational;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class Singleton {

    public static void main(String[] args) {
        final JavaClassSource singletonClass = Roaster.create(JavaClassSource.class);
        singletonClass.setPackage("com.gof.creational.singleton").setName("Singleton");
        singletonClass.addProperty(singletonClass.getName(), "instance").removeMutator();
        singletonClass.getProperty("instance").getField().setStatic(Boolean.TRUE);
        singletonClass.getProperty("instance")
                .getAccessor()
                .setStatic(Boolean.TRUE)
                .setSynchronized(Boolean.TRUE)
                .setBody("""
                        if(instance == null) {
                            instance = new Singleton();
                        }
                        return instance;""");
        singletonClass.addMethod().setConstructor(Boolean.TRUE).setPrivate().setBody("");

        JavaSourceWriter.save(singletonClass, "/home/ashesh/workspace/example/src/main/java/");
    }
}
