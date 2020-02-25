package com.ashessin.cs474.hw1.behavioral;

import com.ashessin.cs474.hw1.utils.JavaSourceWriter;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class TemplateMethod {

    public static void main(String[] args) {
        final JavaClassSource abstractBaseClass = Roaster.create(JavaClassSource.class);
        abstractBaseClass.setPackage("com.gof.behavioral.templatemethod").setName("AbsBaseClass");
        abstractBaseClass.setAbstract(Boolean.TRUE);
        abstractBaseClass.addMethod()
                .setProtected()
                .setAbstract(Boolean.TRUE)
                .setName("methodA")
                .setReturnTypeVoid();
        abstractBaseClass.addMethod()
                .setProtected()
                .setAbstract(Boolean.TRUE)
                .setName("methodB")
                .setReturnTypeVoid();
        abstractBaseClass.addMethod().setConstructor(Boolean.FALSE)
                .setFinal(Boolean.TRUE)
                .setReturnTypeVoid()
                .setBody("""
                        methodA();
                        methodB();""")
                .setName("runAlgorithm");

        final JavaClassSource concreteClassA = Roaster.create(JavaClassSource.class);
        concreteClassA.setPackage("com.gof.behavioral.templatemethod").setName("concreteClassA");
        concreteClassA.extendSuperType(abstractBaseClass);
        concreteClassA.removeImport(abstractBaseClass);

        final JavaClassSource concreteClassB = Roaster.create(JavaClassSource.class);
        concreteClassB.setPackage("com.gof.behavioral.templatemethod").setName("concreteClassB");
        concreteClassB.extendSuperType(abstractBaseClass);
        concreteClassB.removeImport(abstractBaseClass);

        JavaSourceWriter.save(abstractBaseClass, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteClassA, "/home/ashesh/workspace/example/src/main/java/");
        JavaSourceWriter.save(concreteClassB, "/home/ashesh/workspace/example/src/main/java/");
    }
}
