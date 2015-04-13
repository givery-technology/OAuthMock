import play.PlayScala

name := """OAuthMock"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
  "com.ning" % "async-http-client" % "1.9.18",
  "com.fasterxml.jackson.core" % "jackson-core" % "2.5.2"
)
