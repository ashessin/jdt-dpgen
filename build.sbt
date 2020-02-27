name := "jdt-dpgen"

version := "0.1"

scalaVersion := "2.13.1"

lazy val roasterVersion = "2.21.1.Final"
lazy val javaVersionRequired = "1.8"

javacOptions ++= Seq("-source", javaVersionRequired)

initialize := {
    val _ = initialize.value
    val javaVersion = sys.props("java.specification.version")
    if (javaVersion != javaVersionRequired)
        sys.error("Java " + javaVersionRequired +" is required for this project. Found " + javaVersion + " instead")
}

fork := true

//assemblyMergeStrategy in assembly := {
//    case PathList(ps @ _*) if ps.last endsWith ".Named" => MergeStrategy.first
//    case "META-INF/MANIFEST.MF"                         => MergeStrategy.discard
//    case x => MergeStrategy.last
//}
//assemblyOption in assembly := (assemblyOption in assembly).value.copy(cacheOutput = false)

libraryDependencies ++= Seq(
    // https://mvnrepository.com/artifact/org.jboss.forge.roaster/roaster-api
    "org.jboss.forge.roaster"   %   "roaster-api"                         % roasterVersion,
    // https://mvnrepository.com/artifact/org.jboss.forge.roaster/roaster-jdt
    "org.jboss.forge.roaster"   %   "roaster-jdt"                         % roasterVersion      % "runtime",
    // https://mvnrepository.com/artifact/org.apache.maven/maven-embedder
    "org.apache.maven.shared"   % "maven-invoker"                         % "3.0.1",

    // https://mvnrepository.com/artifact/info.picocli/picocli
    "info.picocli"              %   "picocli"                             % "4.2.0",
    // https://mvnrepository.com/artifact/com.typesafe/config
    "com.typesafe"              %   "config"                              % "1.4.0",
    // https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
    "ch.qos.logback"            %   "logback-classic"                     % "1.2.3",

    // test dependencies
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
    "org.junit.jupiter"         %   "junit-jupiter-engine"                % "5.6.0"             % "test")

mainClass in (Compile, run) := Some("com.ashessin.cs474.hw1.Main")
mainClass in (Compile, packageBin) := Some("com.ashessin.cs474.hw1.Main")