name := "jdt-dpgen"

version := "0.1"

scalaVersion := "2.13.1"

javacOptions in Compile += "--enable-preview"
javacOptions in Compile ++= Seq("-source", "13", "-target", "13", "-Xlint")
javaOptions += "--enable-preview"

initialize := {
    val _ = initialize.value
    val javaVersion = sys.props("java.specification.version")
    if (javaVersion != "13")
        sys.error("Java 13 is required for this project. Found " + javaVersion + " instead")
}

lazy val roasterVersion = "2.21.1.Final"

libraryDependencies ++= Seq(
    // https://mvnrepository.com/artifact/org.jboss.forge.roaster/roaster-api
    "org.jboss.forge.roaster"   % "roaster-api"                         % roasterVersion,
    // https://mvnrepository.com/artifact/org.jboss.forge.roaster/roaster-jdt
    "org.jboss.forge.roaster"   % "roaster-jdt"                         % roasterVersion,

    // https://mvnrepository.com/artifact/com.typesafe/config
    "com.typesafe"              % "config"                              % "1.4.0",
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    "ch.qos.logback"            % "logback-classic"                     % "1.3.0-alpha5",

    // test dependencies
    // https://mvnrepository.com/artifact/junit/junit
    "junit"                     % "junit"                               % "4.13"            % "test")

mainClass in (Compile, run) := Some("com.ashessin.cs474.hw1.Main")