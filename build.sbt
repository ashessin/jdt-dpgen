name := "jdt-dpgen"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
    // https://mvnrepository.com/artifact/org.eclipse.jdt/org.eclipse.jdt.core
    "org.eclipse.jdt" % "org.eclipse.jdt.core"                              % "3.20.0",
    // https://mvnrepository.com/artifact/com.typesafe/config
    "com.typesafe" % "config"                                               % "1.4.0",
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    "ch.qos.logback" % "logback-classic"                                    % "1.3.0-alpha5",

    // test dependencies
    // https://mvnrepository.com/artifact/junit/junit
    "junit" % "junit"                                                       % "4.13"              % "test")

mainClass in (Compile, run) := Some("com.ashessin.cs474.hw1.Main")