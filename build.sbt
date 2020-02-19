name := "jdt-dpgen"

version := "0.1"

scalaVersion := "2.13.1"

lazy val javaVersionRequired = "13"

javacOptions ++= Seq("--enable-preview", "-source", javaVersionRequired)
javaOptions += "--enable-preview"

initialize := {
    val _ = initialize.value
    val javaVersion = sys.props("java.specification.version")
    if (javaVersion != javaVersionRequired)
        sys.error("Java " + javaVersionRequired +" is required for this project. Found " + javaVersion + " instead")
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