name := """play-with-actors"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
	"com.typesafe.akka" %% "akka-actor" % "2.4.8",
	"com.typesafe.akka" %% "akka-slf4j" % "2.4.8",
	"org.apache.commons" % "commons-math3" % "3.6.1"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
