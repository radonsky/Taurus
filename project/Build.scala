import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "Taurus"
    val appVersion      = "2.0-SNAPSHOT"

  val appDependencies = Seq(
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "com.typesafe.play.extras" % "iteratees-extras_2.10" % "1.0.1",
    jdbc,
    anorm
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
