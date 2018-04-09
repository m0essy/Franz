name := "MultiTacking"

version := "0.1"

scalaVersion := "2.12.5"
val circeVersion = "0.9.3"

//resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + "/.m2/repository"

libraryDependencies += "MultiChainJavaAPI" % "MultiChainJavaAPI" % "0.4.8-SNAPSHOT"

libraryDependencies ++= Seq(
  "com.softwaremill.sttp" %% "akka-http-backend" % "1.1.12",
  "com.typesafe.akka" %% "akka-stream" % "2.5.11",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.11" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.0",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.0" % Test
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
