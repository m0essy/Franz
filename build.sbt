name := "FranzMultiChain"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.8"
val circeVersion = "0.10.0"

//resolvers += "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + "/.m2/repository"

libraryDependencies ++= Seq(
  "com.softwaremill.sttp" %% "akka-http-backend" % "1.1.12",
  "com.typesafe.akka" %% "akka-stream" % "2.5.22",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.22" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.0",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.0" % Test,
  "org.specs2" %% "specs2-core" % "4.0.3" % Test,
  "org.specs2" %% "specs2-matcher-extra" % "4.0.3" % Test
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)
