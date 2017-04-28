name := "KmsEncryption4S"

version := "1.0"

scalaVersion := "2.12.2"

libraryDependencies ++= {
  Seq(

    // KMS SDK
    "com.amazonaws"              % "aws-java-sdk-kms"         % "1.11.122",
    "com.amazonaws"              % "aws-encryption-sdk-java"  % "0.0.1",

    // Dependencies
    "org.bouncycastle"           % "bcprov-ext-jdk15on"       % "1.54",

    // Logging
    "com.typesafe.scala-logging" % "scala-logging_2.11"       % "3.5.0",
    "ch.qos.logback"             % "logback-classic"          % "1.1.7",

    // Argument parser
    "commons-cli"                % "commons-cli"              % "1.4",

    // Testing
    "org.scalatest"             %% "scalatest"                % "3.0.0"       % "test"
  )
}

// Assembly
mainClass in Global := Some("onema.Main")
assemblyJarName in assembly := "encryption4s.jar"
