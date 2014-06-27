import BuildSettings._
import sbtrelease.ReleasePlugin._

// Subprojects are aggregated to enforce publishing all to the same version.

lazy val root =
  project.in(file ("."))
    .settings(releaseSettings: _*)
    .settings(
      scalaVersion := "2.10.4",
      name := "sbt-plugins")
    .aggregate(sbtFormat, sbtVersionInjector, sbtTravisPublisher, sbtSharedUi, sbtDeploy, sbtNodeJs, sbtWebapp)

lazy val sbtFormat =
  project.in(file("sbt-format"))
    .settings(sbtPluginSettings: _*)
    .settings(
      name := "sbt-format"
    )

lazy val sbtVersionInjector =
  project.in(file("sbt-version-injector"))
    .settings(sbtPluginSettings: _*)
    .settings(
      name := "sbt-version-injector"
    )

lazy val sbtTravisPublisher =
  project.in(file("sbt-travis-publisher"))
    .settings(sbtPluginSettings: _*)
    .settings(
      name := "sbt-travis-publisher"
    )

lazy val sbtSharedUi =
  project.in(file("sbt-shared-ui"))
    .settings(sbtPluginSettings: _*)
    .settings(
      name := "sbt-shared-ui"
    )

lazy val sbtDeploy =
  project.in(file("sbt-deploy"))
    .settings(sbtPluginSettings: _*)
    .settings(
      name := "sbt-deploy"
    )

lazy val sbtNodeJs =
  project.in(file("sbt-node-js"))
    .settings(sbtPluginSettings: _*)
    .settings(publishToBintraySettings: _*)
    .settings(
      name := "sbt-node-js"
    )

lazy val sbtWebapp =
  project.in(file("sbt-webapp"))
    .settings(sbtPluginSettings: _*)
    .settings(
      name := "sbt-webapp"
    ).dependsOn(sbtDeploy, sbtNodeJs)
