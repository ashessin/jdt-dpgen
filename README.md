```text
This project implements a cli that generates several design patterns described in the GoF book.
Through the cli, users can make a selection of design pattern with several configurable options and 
parameters (including package name, class/interface names, datatype and variables names among 
others). Explanation for each pattern and the relationship among its reference types can be pulled 
on demand. The program itself makes use of some of these patterns (Builder, Factory Method, 
Prototype etc.), both directly and indirectly.


-----
INDEX
-----
1. About Design Patterns
2. Some Important Files
3. Application Design
    3.1 Gathering input from the user
    3.2 Custom representation for classes and interfaces
    3.3 The process of generating custom representation from input
    3.4 Serializing custom representations to java source code
4. Setup Instructions
5. Usage Details
    5.1 Available commands and usage help
    5.2 Passing parameters and options to the cli
    5.3 Generating multiple design patterns at once
    5.4 Running from configuration file
6. Demo


------------------------
1. About Design Patterns
------------------------

According to the GoF (Design Patterns: Elements of Reusable Object-Oriented Software) book, 
"a design pattern names, abstracts, and identifies the key aspects of a common design structure that
make it useful for creating a reusable object-oriented design. The design pattern identifies the 
participating classes and instances, their roles and collaborations, and the distribution of 
responsibilities. Each design pattern focuses on a particular object-oriented design problem or 
issue. It describes when it applies, whether it can be applied in view of other design constraints, 
and the consequences and trade-offs of its use".

The design patterns discussed can be categorized into groups (more details in "Usage" Section):

- Creational patterns
  Creational design patterns abstract the instantiation process.
  They help make a system independent of how its objects are created, composed, and represented.

- Structural patterns
  Structural patterns are concerned with how classes and objects are composed to form larger 
  structures. Structural class patterns use inheritance to compose interfaces or implementations.

- Behavioural patterns
  Behavioral patterns are concerned with algorithms and assignment of responsibilities between 
  objects. Behavioral patterns describe not just patterns of objects or classes but also the 
  patterns of communication between them.

It is important to note that these patterns when used correctly can result in cleaner more reusable
and maintainable code base, however they are not intended to be applied directly in all scenarios
without proper consideration of the problem at hand. Many times overusing design patterns can lead 
to abuse. Also, the same design pattern may be implemented in a number of ways, each having its 
merits and demerits. As per my understanding, there's a lot of debate on how some of these patterns
should be implemented and even that some qualify as an Anti-pattern. 

For example, the Singleton patter can be implemented in any one or combination of the below ways:
  Eager initialization, Static block initialization, Lazy Initialization, hread Safe Singleton,
  Bill Pugh Singleton Implementation, Using Reflection to destroy Singleton Pattern, Enum Singleton,
  Serialization and Singleton


-----------------------
2. Some Important Files
-----------------------

    src/main/java/com/ashessin/cs474/hw1
        
        /generator                      sources for represnting custom design pattern refernce types
            /behavioral                     cli and generator classes for behavioral design patterns
            /creational                     cli and generator classes for creational design patterns
            /structural                     cli and generator classes for structural design patterns
        
        /parser                         classes for parsing custom design pattern refernce types
        
        /utils                          helper classes
            CliExtension.java           post-process results from a command interpretation
            FileAuthor.java             saves file (*.java) assuming a maven project structure
            LoggingReflection.java      for logging fields in class files
            MavenInnvoker.java          for invoking mvn command to setup a java/scala project

    src/main/resources/reference.conf   the default configuration file for reference


---------------------
3. Application Design
---------------------

The design pattern generator is built as a command line application that accepts user input.

For illustration, I am using below arguments to generate the builder pattern for some Employee 
builder design pattern with complex representation (many properties):
Arguments are passed to `com.ashessin.cs474.hw1.Main` class.
(see usage section for details)

    -l=scala
    -d=src/main/resources
    builder
    Builder
    EmployeeBuilder
    Employee
    int,age;String,lastName;String,firstName;java.util.List<String>,responsibilities

------------------------------------------------------
3.1 Application Design - Gathering input from the user
------------------------------------------------------

For each design pattern there exists a pair of files within the package names 
`com.ashessin.cs474.hw1.generator.behavioral`, `com.ashessin.cs474.hw1.generator.creational`, 
`com.ashessin.cs474.hw1.generator.structural`.  
The first type of file ending in prefix 'Q' contains the source for parsing the command line input 
and also showing command usage instructions. All of these classes implement the 
`java.util.concurrent.Callable` interface and make use of annotations for making the cli (supported 
through the picocli library). These files are responsible for instantiating their
respective design pattern generator class.  
The second type of files ending in prefix 'Gen' take the parsed cli input and returns a custom 
representation of interface (`DpInterfaceSource`) and class (`DpClassSource`) types associated with 
the design pattern. The representations are specific to each design pattern and capture the 
relationships as per some rules set in the same class body.

13:55:22.880 |     main | INFO  | w1.generator.creational.BuilderQ | Generating representation for design pattern sources.
13:55:22.887 |     main | INFO  | .generator.creational.BuilderGen | packageName=com.gof.creational.builder
13:55:22.888 |     main | INFO  | .generator.creational.BuilderGen | abstractBuilderName=Builder
13:55:22.889 |     main | INFO  | .generator.creational.BuilderGen | concreteBuilderName=EmployeeBuilder
13:55:22.889 |     main | INFO  | .generator.creational.BuilderGen | concreteProductName=Employee
13:55:22.889 |     main | INFO  | .generator.creational.BuilderGen | propertiesMap={age=int, lastName=String, firstName=String, responsibilities=java.util.List<String>}

-------------------------------------------------------------------------
3.2 Application Design - Custom representation for classes and interfaces
-------------------------------------------------------------------------

The custom top-level named reference type representations `DpClassSource` and `DpInterfaceSource`, 
extend the `DpSource` interface and loosely represent a class and an interface along with their 
members. These members can be fields/properties (`DpSourceField`) and methods (`DpSourceMethod`) in 
the case of `DpClassSource` instances or just methods in case of `DpInterfaceSource`. Enums are used
for modeling possible modifier values. All four of these implement a basic inner static Builder 
pattern with mandatory parameters and lazy validation.  
Additionally, to avoid a lot of boilerplate code associated with generating a `DpSourceMethod` when 
representing an override, the `DpSourceMethod` declares a copying builder method (a variation on the
copy constructor, often discussed along with the Prototype design pattern).  
It is important to note that these custom representations are quite limiting when compared to what 
one can accomplish within an actual class or interface declaration. For ex: there's no support for 
annotations, parameterized class definitions, inner class etc., just to name a few of those. However
this is not an issue since the user is supposed to extend on these generated design pattern files 
manually later. Also, all of the design patterns can be implemented without the need for supporting 
such extended representations.

The process of generating custom representation from input  
Lastly, `DpArrayList` extends the `java.util.ArrayList` class and overrides the `add()` method while
also declaring a few other methods that are specific to the derived class. This class can be
instantiated to hold objects of the derived type `DpSource`. This is a convenient data structure to 
pass around since it can hold both the derived types (`DpInterfaceSource`, `DpClassSource`). The 
'Gen' prefix classes make use of `DpArrayList` to add design pattern reference types as they are 
built in their respective `main` method.

13:55:22.902 |     main | DEBUG | .generator.creational.BuilderGen | DpClassSource[packageName='com.gof.creational.builder', name='Employee', javadoc='null', accessModifier=PUBLIC, modifier=NONE, implementsInterfaces=[], extendsClass='Object', methods=[], fields=[DpSourceField[name='age', type='int', value='null', javadoc='null', getter=true, setter=true, modifiers=[], accessModifier=PUBLIC], DpSourceField[name='lastName', type='String', value='null', javadoc='null', getter=true, setter=true, modifiers=[], accessModifier=PUBLIC], DpSourceField[name='firstName', type='String', value='null', javadoc='null', getter=true, setter=true, modifiers=[], accessModifier=PUBLIC], DpSourceField[name='responsibilities', type='java.util.List<String>', value='null', javadoc='null', getter=true, setter=true, modifiers=[], accessModifier=PUBLIC]]]
13:55:22.908 |     main | DEBUG | .generator.creational.BuilderGen | DpClassSource[packageName='com.gof.creational.builder', name='Builder', javadoc='null', accessModifier=PUBLIC, modifier=ABSTRACT, implementsInterfaces=[], extendsClass='Object', methods=[DpSourceMethod[constructor=false, name='buildAge', javadoc='', accessModifier=PUBLIC, modifiers=[ABSTRACT], returnType='Builder', body='null', parameters={age=int}, inherited=false], DpSourceMethod[constructor=false, name='buildLastName', javadoc='', accessModifier=PUBLIC, modifiers=[ABSTRACT], returnType='Builder', body='null', parameters={lastName=String}, inherited=false], DpSourceMethod[constructor=false, name='buildFirstName', javadoc='', accessModifier=PUBLIC, modifiers=[ABSTRACT], returnType='Builder', body='null', parameters={firstName=String}, inherited=false], DpSourceMethod[constructor=false, name='buildResponsibilities', javadoc='', accessModifier=PUBLIC, modifiers=[ABSTRACT], returnType='Builder', body='null', parameters={responsibilities=java.util.List<String>}, inherited=false]], fields=[]]
13:55:22.913 |     main | DEBUG | .generator.creational.BuilderGen | DpClassSource[packageName='com.gof.creational.builder', name='EmployeeBuilder', javadoc='null', accessModifier=PUBLIC, modifier=NONE, implementsInterfaces=[], extendsClass='Builder', methods=[DpSourceMethod[constructor=false, name='buildAge', javadoc='', accessModifier=PUBLIC, modifiers=[NONE], returnType='Builder', body='employee.setAge(age);return this;', parameters={age=int}, inherited=false], DpSourceMethod[constructor=false, name='buildLastName', javadoc='', accessModifier=PUBLIC, modifiers=[NONE], returnType='Builder', body='employee.setLastName(lastName);return this;', parameters={lastName=String}, inherited=false], DpSourceMethod[constructor=false, name='buildFirstName', javadoc='', accessModifier=PUBLIC, modifiers=[NONE], returnType='Builder', body='employee.setFirstName(firstName);return this;', parameters={firstName=String}, inherited=false], DpSourceMethod[constructor=false, name='buildResponsibilities', javadoc='', accessModifier=PUBLIC, modifiers=[NONE], returnType='Builder', body='employee.setResponsibilities(responsibilities);return this;', parameters={responsibilities=java.util.List<String>}, inherited=false]], fields=[DpSourceField[name='employee', type='Employee', value='null', javadoc='null', getter=false, setter=false, modifiers=[], accessModifier=PUBLIC]]]

-------------------------------------------------------------------------------
3.3 Application Design - Serializing custom representations to java source code 
-------------------------------------------------------------------------------

Ultimately, the custom design pattern type representations need to be converted into actual Java 
source. For this we can use a parser like Javapoet (`com.squareup.javapoet`), JavaParser 
(`com.github.javaparser`), Roaster (`org.jboss.forge.roaster`) or Eclipse JDT (`org.eclipse.jdt`).
For the sake of flexibility, a Factory Method design pattern has been implemented which may be used
by the requester to serialize `DpSource` types into respective Java Source representations with any 
one of the four parsers mentioned above as backend. As for this project, only the Roaster 
implementation is developed and ready for use. Roaster uses Eclipse JDT as a shaded library but is
much easier to use in comparison and provides a fluent interface for building objects.

13:55:22.917 |     main | INFO  | sin.cs474.hw1.utils.CliExtension | Processing 1 sub-commands.
13:55:22.920 |     main | INFO  | .cs474.hw1.parser.DpSourceParser | Initializing backend parser.
13:55:22.923 |     main | INFO  | hw1.parser.DpSourceParserRoaster | Initialized new Roaster backend.
13:55:22.931 |     main | INFO  | sin.cs474.hw1.utils.CliExtension | Processing builder sub-command.
13:55:22.935 |     main | INFO  | hw1.parser.DpSourceParserRoaster | Creating new design pattern class: com.gof.creational.builder.Builder
13:55:22.935 | worker-2 | INFO  | hw1.parser.DpSourceParserRoaster | Creating new design pattern class: com.gof.creational.builder.EmployeeBuilder
13:55:22.935 | worker-1 | INFO  | hw1.parser.DpSourceParserRoaster | Creating new design pattern class: com.gof.creational.builder.Employee

For writing the java source files and processing cli results returned from the cli interpretations,
helper classes are employed from the `com.ashessin.cs474.hw1.utils` package. Once the serialization
has been performed, the files are written to the user defined location. The serialization and file 
write operations are done in parallel for multiple files. There's also an attempt to initialize the 
root folder of generated files as a maven project. The project may be further converted to scala 
language through the use of the `scalagen` plugin. Note that the scala conversion is automatic and 
might have some deficiencies in the implementation. The conversion might also fail at times since the 
upstream project has not been updated in years. However, for the most part the conversion goes
through without any issues.

13:55:24.161 |     main | INFO  | essin.cs474.hw1.utils.FileAuthor | Attempting to write new file: src/main/resources/dpgen-output/src/main/java/com/gof/creational/builder/Builder.java
13:55:24.164 | worker-1 | INFO  | essin.cs474.hw1.utils.FileAuthor | Attempting to write new file: src/main/resources/dpgen-output/src/main/java/com/gof/creational/builder/Employee.java
13:55:24.164 | worker-2 | INFO  | essin.cs474.hw1.utils.FileAuthor | Attempting to write new file: src/main/resources/dpgen-output/src/main/java/com/gof/creational/builder/EmployeeBuilder.java
13:55:24.171 | worker-2 | INFO  | essin.cs474.hw1.utils.FileAuthor | Wrote 564 bytes to file.
13:55:24.171 | worker-1 | INFO  | essin.cs474.hw1.utils.FileAuthor | Wrote 744 bytes to file.
13:55:24.172 |     main | INFO  | essin.cs474.hw1.utils.FileAuthor | Wrote 337 bytes to file.
13:55:24.173 |     main | INFO  | sin.cs474.hw1.utils.CliExtension | Setting up dpgen-output as new Maven project.
13:55:24.183 |     main | INFO  | sin.cs474.hw1.utils.MavenInvoker | Invoking mvn command: mvn -q archetype:generate \ -DgroupId=com.gof \ -DartifactId=dpgen-output \ -DarchetypeGroupId=pl.org.miki \ -DarchetypeArtifactId=java8-quickstart-archetype \ -DarchetypeVersion=1.0.0 \ -DinteractiveMode=false
13:55:31.855 |     main | INFO  | sin.cs474.hw1.utils.CliExtension | Successfully setup dpgen-output as maven project.
13:55:31.856 |     main | INFO  | sin.cs474.hw1.utils.CliExtension | Beginning Java to Scala transformation.
13:55:31.857 |     main | INFO  | sin.cs474.hw1.utils.MavenInvoker | Invoking mvn command: mvn -q scalagen:main
13:55:36.664 |     main | INFO  | sin.cs474.hw1.utils.CliExtension | Successfully converted dpgen-output source files to scala.
13:55:36.664 |     main | INFO  | sin.cs474.hw1.utils.CliExtension | Generated design patterns are available in src/main/resources/dpgen-output directory.


---------------------
4. Setup Instructions
---------------------

1. Clone this repository using `git clone https://asing80@bitbucket.org/asing80/cs474-hw1.git`
command.
    
2. Make sure you have maven installed and the `MAVEN_HOME`, `M2_HOME` environment variables point to
the correct maven install location. On most Linux systems, `/usr/share/maven`. This can be check by
using `echo $MAVEN_HOME` and `echo $M2_HOME` if running from a terminal.

3. Change directory to the locally cloned repository, `cd cs474-hw1`.

4. Run a sbt build along with test cases `sbt clean compile run`. For generating the jar file, use 
`sbt assembly`. (optional)


----------------
5. Usage Details
----------------

The user can invoke the design pattern generator by using appropriate options and parameters on the 
command line. Some example usage has been provided for reference towards the bottom of this section.

-----------------------------------------------------
5.1 Usage Details - Available commands and usage help
-----------------------------------------------------

To run the cli from jar file, use: `java -jar jdt-dpgen-assembly-0.1.jar -h` (shows below usage help)
  
Usage: jdt-dpgen [-hV] [-c[=<configFile>]] [-a=<artifactId>] [-d=<outputLocation>] [-g=<groupId>] 
                 [-l=<outputLanguage>] COMMAND COMMAND...

Generates GOF design patterns in Java and Scala language.

  -l, --outputLanguage=<outputLanguage>
                                          The output language for the implement.
                                            Candidates: java, scala
                                            Default: java
  -d, --outputLocation=<outputLocation>
                                          The folder/directory location for saving generated
                                            files.
                                            Default: ${sys:user.dir}
  -a, --artifactId=<artifactId>
                                          Unique base name of the primary artifact being
                                            generated.
                                            Default: dpgen-output
  -g, --groupId=<groupId>
                                          Unique identifier of the organization or group.
                                            Default: com.gof
  -c, --config[=<configFile>]
                                          Path to input configuration file.
  -h, --help                              Show this help message and exit.
  -V, --version                           Print version information and exit.

Commands:
  singleton...............................Generates Singleton creational design pattern. Ensure
                                            a class only has one instance, and provide a global
                                            point of access to it.
  prototype...............................Generates Prototype creational design pattern. Specify
                                            the kinds of objects to create using a prototypical
                                            instance, and create new objects by copying this
                                            prototype.
  builder.................................Generates Builder creational design pattern. Separate
                                            the construction of a complex object from its
                                            representation so that the same construction process
                                            can create different representations.
  factorymethod...........................Generates Factory Method creational design pattern.
                                            Define an interface for creating an object, but let
                                            subclasses decide which class to instantiate.
                                            Factory Method lets a class defer instantiation to
                                            subclass
  abstractfactory.........................Generates Abstract Factory creational design pattern.
                                            Provide an interface for creating families of
                                            related or dependent objects without specifying
                                            their concrete classes.
  adapter.................................Generates Adapter structural design pattern. Convert
                                            the interface of a class into another interface
                                            clients expect. Adapter lets classes work together
                                            that couldn't otherwise because of incompatible
                                            interfaces.
  bridge..................................Generates Bridge structural design pattern. Decouple
                                            an abstraction from its implementation so that the
                                            two can vary independently.
  composite...............................Generates Composite structural design pattern. Compose
                                            objects into tree structures to represent part-whole
                                            hierarchies. Composite lets clients treat individual
                                            objects and compositions of objects uniformly.
  decorator...............................Generates Decorator structural design pattern. Attach
                                            additional responsibilities to an object
                                            dynamically. Decorators provide a flexible
                                            alternative to sub-classing for extending
                                            functionality.
  facade..................................Generates Facade structural design pattern. Provide a
                                            unified interface to a set of interfaces in a
                                            subsystem. Facade defines a higher-level interface
                                            that makes the subsystem easier to use.
  flyweight...............................Generates Flyweight structural design pattern. Use
                                            sharing to support large numbers of fine-grained
                                            objects efficiently.
  proxy...................................Generates Proxy structural design pattern. Provide a
                                            surrogate or placeholder for another object to
                                            control access to it.
  chainofresponsibility...................Generates Chain Of Responsibility behavioral design
                                            pattern. Avoid coupling the sender of a request to
                                            its receiver by giving more than one object a chance
                                            to handle the request. Chain the receiving objects
                                            and pass the request along the chain until an object
                                            handles it.
  command.................................Generates Command behavioral design pattern.
                                            Encapsulate a request as an object, thereby letting
                                            you parameterize clients with different requests,
                                            queue or log requests, and support undoable
                                            operations.
  interpreter.............................Generates Interpreter behavioral design pattern. Given
                                            a language, define a representation for its grammar
                                            along with an interpreter that uses the
                                            representation to interpret sentences in the
                                            language.
  iterator................................Generates Iterator behavioral design pattern. Provide
                                            a way to access the elements of an aggregate object
                                            sequentially without exposing its underlying
                                            representation.
  mediator................................Generates Mediator behavioral design pattern. Define
                                            an object that encapsulates how a set of objects
                                            interact. Mediator promotes loose coupling by
                                            keeping objects from referring to each other
                                            explicitly, and it lets you vary their interaction
                                            independently.
  memento.................................Generates Memento behavioral design pattern. Without
                                            violating encapsulation, capture and externalize an
                                            object’s internal state so that the object can be
                                            restored to this state later.
  observer................................Generates Observer behavioral design pattern. Define a
                                            one-to-many dependency between objects so that when
                                            one object changes state, all its dependents are
                                            notified and updated automatically.
  state...................................Generates State behavioral design pattern. Allow an
                                            object to alter its behavior when its internal state
                                            changes. The object will appear to change its class
  strategy................................Generates Strategy behavioral design pattern. Define a
                                            family of algorithms, encapsulate each one, and make
                                            them interchangeable. Strategy lets the algorithm
                                            vary independently from clients that use it.
  templatemethod..........................Generates Template Method behavioral design pattern.
                                            Define the skeleton of an algorithm in an operation,
                                            deferring some steps to subclasses. Template Method
                                            lets subclasses redefine certain steps of an
                                            algorithm without changing the algorithm’s structure.
  visitor.................................Generates Visitor behavioral design pattern. Represent
                                            an operation to be performed on the elements of an
                                            object structure. Visitor lets you define a new
                                            operation without changing the classes of the
                                            elements on which it operates.

You can also get usage help for specific design pattern subcommand, say the Builder pattern to get
what options, paramters are avaialble.

`java -jar jdt-dpgen-assembly-0.1.jar builder -h` (shows usage help for builder design pattern)

Usage: dpgen builder [BuilderName ConcreteBuilderName ConcreteProductName ConcreteProductProperties]
                     [-hV] [-p=<packageName>]

Generates Builder creational design pattern. Separate the construction of a complex object from its 
representation so that the same construction process can create different representations.

      BuilderName                         The Builder specifies an abstract interface for
                                            creating parts of a Product object.
                                            Default: Builder
      ConcreteBuilderName                 The ConcreteBuilder class constructs and assembles
                                            parts of the product, implementing the Builder
                                            interface.
                                            Default: ConcreteBuilder
      ConcreteProductName                 The Product class represents a complex object.
                                            Default: Product
      ConcreteProductProperties
                                            Default: Object,property1;Object,property2
  -p, --packageName=<packageName>
                                            Default: com.gof.creational.builder
  -h, --help                                Show this help message and exit.
  -V, --version                             Print version information and exit.

-------------------------------------------------------------
5.2 Usage Details - Passing parameters and options to the cli
-------------------------------------------------------------

Notice that each command has default parameters and options already set and will use those in case 
the user provides none. An example run of builder pattern genrator with user provided input was 
already shown in the Application Design section. However, for non customized implementation, one 
could also use:

    -l=scala
    -d=src/main/resources
    builder

Above we do not provide the cli with BuilderName, ConcreteBuilderName, ConcreteProductName, 
ConcreteProductProperties paramters however, since the default values are avilable, they are used
instead.

---------------------------------------------------------------
5.3 Usage Details - Generating multiple design patterns at once
---------------------------------------------------------------

If more than one design patterns need to be generated within the same package, that can be done by 
passing them all at once to the cli.

    -l=scala
    -d=src/main/resources
    builder
    anstractfactory
    strategy

To generate all the design patterns from the jar file use:

dpgen \
    singleton prototype builder factorymethod abstractfactory \
    adapter bridge composite decorator facade flyweight proxy \
    chainofresponsibility command interpreter iterator mediator memento observer state strategy templatemethod visitor

where dpgen is an alias, something like:
    alias dpgen="/usr/lib/jvm/java-13/bin/java --enable-preview -jar jdt-dpgen-assembly-0.1.jar"

---------------------------------------------------
5.4 Usage Details - Running from configuration file
---------------------------------------------------

To run from configuration file, use the `-c` option followed by the path to the configuration file.
    `java -jar jdt-dpgen-assembly-0.1.jar -c /path/to/configuration-file.conf`

If no path is passes, the application tries to use the default internal configuration file.
    `java -jar jdt-dpgen-assembly-0.1.jar -c`


-------
6. Demo
-------

https://asing80.people.uic.edu/cs441/hw2

```