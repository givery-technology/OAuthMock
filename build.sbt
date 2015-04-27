import play.PlayScala

name := """OAuthMock"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.1"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  ws,
  cache,
  "com.ning" % "async-http-client" % "1.9.+",
  "org.json4s" %% "json4s-jackson" % "3.2.+",
  "org.twitter4j" % "twitter4j-core" % "4.0.+"
)
