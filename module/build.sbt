name := "play20-stateless-auth"

version := "0.1-SNAPSHOT"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "play" %% "play" % "2.0.1"
)

organization := "com.blendlabsinc"
