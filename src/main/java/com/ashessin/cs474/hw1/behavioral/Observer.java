package com.ashessin.cs474.hw1.behavioral;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

import java.util.ArrayList;
import java.util.List;

public class Observer {

    public static void main(String[] args) {
        // Create Subject Interface
        final JavaInterfaceSource subjectInterface = Roaster.create(JavaInterfaceSource.class);
        subjectInterface.setPackage("com.gof.behavioral.observer").setName("Subject");

        // Create Observer Interface
        final JavaInterfaceSource observerInterface = Roaster.create(JavaInterfaceSource.class);
        observerInterface.setPackage("com.gof.behavioral.observer").setName("Observer");

        subjectInterface.addMethod()
                .setConstructor(Boolean.FALSE)
                .setName("attach")
                .setReturnType(void.class)
                .addParameter(observerInterface.getName(), "observer");
        subjectInterface.addMethod()
                .setConstructor(Boolean.FALSE)
                .setName("detach")
                .setReturnType(void.class)
                .addParameter(observerInterface.getName(), "observer");
        subjectInterface.addMethod()
                .setConstructor(Boolean.FALSE)
                .setName("notifyObservers")
                .setReturnType(void.class);

        // Create Concrete Subject which implements Subject Interface
        final JavaClassSource concreteSubjectClass = Roaster.create(JavaClassSource.class);
        concreteSubjectClass.setPackage("com.gof.behavioral.observer").setName("ConcreteSubject");
        concreteSubjectClass.addImport(List.class);
        concreteSubjectClass.addImport(ArrayList.class);
        concreteSubjectClass.implementInterface(subjectInterface);
        concreteSubjectClass.removeImport(subjectInterface);
        concreteSubjectClass.addProperty(String.class, "state");
        concreteSubjectClass.addField().setType("List<" + observerInterface.getName() + ">")
                .setPrivate()
                .setName("observers")
                .setLiteralInitializer("new ArrayList<>();");
        concreteSubjectClass.getMethod("attach", observerInterface.getName())
                .setBody("this.observers.add(observer);");
        concreteSubjectClass.getMethod("detach", observerInterface.getName())
                .setBody("this.observers.remove(observer);");
        concreteSubjectClass.getMethod("notifyObservers")
                .setBody("observers.forEach(Observer::update);");


        observerInterface.addMethod()
                .setConstructor(Boolean.FALSE)
                .setName("update")
                .setReturnType(void.class);

        // Create Concrete Observer A which implements Observer Interface
        final JavaClassSource concreteObserverClassA = Roaster.create(JavaClassSource.class);
        concreteObserverClassA.setPackage("com.gof.behavioral.observer").setName("ConcreteObserverA");
        concreteObserverClassA.implementInterface(observerInterface);
        concreteObserverClassA.removeImport(observerInterface);
        concreteObserverClassA.addProperty(String.class, "state");

        // Create Concrete Observer B which implements Observer Interface
        final JavaClassSource concreteObserverClassB = Roaster.create(JavaClassSource.class);
        concreteObserverClassB.setPackage("com.gof.behavioral.observer").setName("ConcreteObserverB");
        concreteObserverClassB.removeImport(observerInterface);
        concreteObserverClassB.implementInterface(observerInterface);
        concreteObserverClassB.addProperty(String.class, "state");


        JavaSourceWriter.save(subjectInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteSubjectClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(observerInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteObserverClassA, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteObserverClassB, "/home/ashesh/workspace/example/src/main/java/");
    }
}
