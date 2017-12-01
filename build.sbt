name := "firstactors"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies := Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.6",
  "com.typesafe.akka" %% "akka-persistence" % "2.5.6",
  "org.fusesource.leveldbjni"  % "leveldbjni-all"   % "1.8"
)