import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {
	
  val appVersion = "0.1"

  lazy val module = Project("play20-stateless-auth", file("module"))

  val appDependencies = Seq(
    "commons-codec" % "commons-codec" % "1.5"
  )

  lazy val root = PlayProject("sample", appVersion, appDependencies, mainLang = SCALA).settings(
  ).dependsOn(module)

}
