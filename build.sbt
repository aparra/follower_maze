name := "follower_maze"

version := "0.0.1"

scalaVersion := "2.10.2"

javacOptions ++= Seq("-encoding", "UTF-8")

libraryDependencies ++= Seq(
    "org.specs2" %% "specs2" % "2.2.2" % "test"
)

EclipseKeys.withSource := true
