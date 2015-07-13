name := "follower_maze"

version := "0.0.1"

scalaVersion := "2.10.4"

javacOptions ++= Seq("-g", "-encoding", "UTF-8", "-source", "1.7", "-target", "1.7")

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.0.9",
  "org.specs2" %% "specs2" % "2.2.2" % "test",
  "org.mockito" % "mockito-core" % "1.9.5" % "test"
)

EclipseKeys.withSource := true
