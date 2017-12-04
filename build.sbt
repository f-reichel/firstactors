name := "firstactors"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies := Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.7",
  "com.typesafe.akka" %% "akka-persistence" % "2.5.7",
  "com.typesafe.akka" %% "akka-http-core" % "10.0.11",
  "com.typesafe.akka" %% "akka-http" % "10.0.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",
  "org.fusesource.leveldbjni"  % "leveldbjni-all" % "1.8"
)