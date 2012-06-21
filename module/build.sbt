name := "play20-stateless-auth"

version := "0.3-SNAPSHOT"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "play" %% "play" % "2.0.1",
  "commons-codec" % "commons-codec" % "1.2"
)

organization := "com.blendlabsinc"

publishMavenStyle := true

publishArtifact in Test := false

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) 
    Some("snapshots" at nexus + "content/repositories/snapshots") 
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>https://github.com/blendlabs/play20-stateless-auth</url>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>http://www.apache.org/licenses/</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:blendlabs/play20-stateless-auth.git</url>
    <connection>scm:git:git@github.com:blendlabs/play20-stateless-auth.git</connection>
  </scm>
  <developers>
    <developer>
      <id>sqs</id>
      <name>Quinn Slack</name>
      <url>http://qslack.com</url>
    </developer>
  </developers>)
