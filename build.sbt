name := "follower_maze"

version := "0.0.1"

scalaVersion := "2.11.7"

javacOptions ++= Seq("-encoding", "UTF-8")

resolvers += "Local Maven Repository" at "file:///"+ Path.userHome +"/.m2/repository"

EclipseKeys.withSource := true
