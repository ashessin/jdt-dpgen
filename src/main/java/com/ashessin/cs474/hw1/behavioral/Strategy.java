package com.ashessin.cs474.hw1.behavioral;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class Strategy {

    public static void main(String[] args) {
        // Create Strategy Interface
        final JavaInterfaceSource strategyInterface = Roaster.create(JavaInterfaceSource.class);
        strategyInterface.setPackage("com.gof.behavioral.strategy").setName("Strategy");
        strategyInterface.addMethod()
                .setConstructor(false)
                .setPublic()
                .setName("algorithmInterface")
                .setReturnTypeVoid();

        // Create Concrete Strategy which implements Strategy Interface
        final JavaClassSource concreteStrategyClass = Roaster.create(JavaClassSource.class);
        concreteStrategyClass.setPackage("com.gof.behavioral.strategy").setName("ConcreteStrategy");
        concreteStrategyClass.implementInterface(strategyInterface);
        concreteStrategyClass.removeImport(strategyInterface);

        // Create Context
        final JavaClassSource contextClass = Roaster.create(JavaClassSource.class);
        contextClass.setPackage("com.gof.behavioral.strategy").setName("Context");
        contextClass.addProperty(strategyInterface.getName(), "strategy")
                .setAccessible(Boolean.FALSE);
        contextClass.addMethod()
                .setConstructor(true)
                .setPublic()
                .setBody("this.strategy = strategy;")
                .addParameter(strategyInterface.getName(), "strategy");
        contextClass.addMethod()
                .setConstructor(false)
                .setPublic()
                .setName("contextInterface")
                .setBody("strategy.algorithmInterface();");

        JavaSourceWriter.save(strategyInterface, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteStrategyClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(contextClass, "/home/ashesh/workspace/example/src/main/java/");
    }
}
