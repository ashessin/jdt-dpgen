package com.ashessin.cs474.hw1.behavioral;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class Mediator {

    public static void main(String[] args) {
        // Create Component Interface
        final JavaInterfaceSource componentInterface = Roaster.create(JavaInterfaceSource.class);
        componentInterface.setPackage("com.gof.behavioral.mediator").setName("Component");

        // Create Mediator Interface
        final JavaInterfaceSource mediatorInterface = Roaster.create(JavaInterfaceSource.class);
        mediatorInterface.setPackage("com.gof.behavioral.mediator").setName("Mediator");

        componentInterface.addMethod()
                .setConstructor(Boolean.FALSE)
                .setName("setMediator")
                .setReturnType(void.class)
                .addParameter(mediatorInterface.getName(), "mediator");

        mediatorInterface.addMethod()
                .setConstructor(Boolean.FALSE)
                .setName("notify")
                .setReturnType(void.class)
                .addParameter(componentInterface.getName(), "component");


        // Create Concrete Component A which implements Component Interface
        final JavaClassSource concreteComponentClassA = Roaster.create(JavaClassSource.class);
        concreteComponentClassA.setPackage("com.gof.behavioral.mediator").setName("ConcreteComponentA");
        concreteComponentClassA.implementInterface(componentInterface);
        concreteComponentClassA.addField().setPrivate().setType(mediatorInterface.getName()).setName("mediator");
        concreteComponentClassA.removeImport(componentInterface);
        concreteComponentClassA.getMethod("setMediator", mediatorInterface.getName())
                .setBody("this.mediator = mediator;");
        concreteComponentClassA.addMethod().setPublic().setName("doOperationA").setBody("mediator.notify(this);");

        // Create Concrete Component B which implements Component Interface
        final JavaClassSource concreteComponentClassB = Roaster.create(JavaClassSource.class);
        concreteComponentClassB.setPackage("com.gof.behavioral.mediator").setName("ConcreteComponentB");
        concreteComponentClassB.implementInterface(componentInterface);
        concreteComponentClassB.addField().setPrivate().setType(mediatorInterface.getName()).setName("mediator");
        concreteComponentClassB.removeImport(componentInterface);
        concreteComponentClassB.getMethod("setMediator", mediatorInterface.getName())
                .setBody("this.mediator = mediator;");
        concreteComponentClassB.addMethod().setPublic().setName("doOperationB").setBody("mediator.notify(this);");

        // Create Concrete Mediator which implements Mediator Interface
        final JavaClassSource concreteMediatorClass = Roaster.create(JavaClassSource.class);
        concreteMediatorClass.setPackage("com.gof.behavioral.mediator").setName("ConcreteMediator");
        concreteMediatorClass.implementInterface(mediatorInterface);
        concreteMediatorClass.removeImport(mediatorInterface);

        concreteMediatorClass.addField().setPrivate().setType(concreteComponentClassA.getName()).setName("componentA");
        concreteMediatorClass.addMethod().setPublic().setName("operationA").setBody("");

        concreteMediatorClass.addField().setPrivate().setType(concreteComponentClassB.getName()).setName("componentB");
        concreteMediatorClass.addMethod().setPublic().setName("operationB").setBody("");

        JavaSourceWriter.save(componentInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(mediatorInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteComponentClassA, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteComponentClassB, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteMediatorClass, "/home/ashesh/workspace/example/src/main/java/");
    }
}
