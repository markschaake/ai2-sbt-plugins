package org.allenai.plugins

import sbt._
import sbt.Keys._

/** Declares common dependencies with specific versions to encourage
  * consistency accross projects
  */
trait CoreDependencies {
  val defaultScalaVersion = "2.11.5"

  object Logging {
    val slf4jVersion = "1.7.10"
    val logbackVersion = "1.1.2"
    // The logging API to use. This should be the only logging dependency of any API artifact
    // (anything that's going to be depended on outside of this SBT project).
    val slf4jApi = "org.slf4j" % "slf4j-api" % slf4jVersion
    val logbackCore = "ch.qos.logback" % "logback-core" % logbackVersion
    val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackVersion
  }

  // slf4j implementation (logback), and the log4j -> slf4j bridge.
  // This should be called on libraryDependencies like:
  // addLoggingDependencies(libraryDependencies)
  // TODO(markschaake&jkinkead): more comments about what is going on here
  def addLoggingDependencies(deps: SettingKey[Seq[ModuleID]]): Seq[Setting[Seq[ModuleID]]] = {
    import Logging._
    val cleanedDeps = deps ~= { seq =>
      seq map { module =>
        // Exclude the transitive dependencies that might mess things up for us.
        // slf4j replaces log4j.
        (module
          exclude ("log4j", "log4j")
          exclude ("commons-logging", "commons-logging")
          // We're using logback as the slf4j implementation, and we're providing it below.
          exclude ("org.slf4j", "slf4j-log4j12")
          exclude ("org.slf4j", "slf4j-jdk14")
          exclude ("org.slf4j", "slf4j-jcl")
          exclude ("org.slf4j", "slf4j-simple")
          // We'll explicitly provide the logback version; this avoids having to do an override.
          exclude ("ch.qos.logback", "logback-core")
          exclude ("ch.qos.logback", "logback-classic")
          // We add bridges explicitly as well
          exclude ("org.slf4j", "log4j-over-slf4j")
          exclude ("org.slf4j", "jcl-over-slf4j"))
      }
    }
    // Now, add the logging libraries.
    val logbackDeps = deps ++= Seq(
      slf4jApi,
      // Bridge log4j logging to slf4j.
      "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
      // Bridge jcl logging to slf4j.
      "org.slf4j" % "jcl-over-slf4j" % slf4jVersion,
      // Use logback for the implementation.
      logbackCore,
      logbackClassic
    )
    Seq(cleanedDeps, logbackDeps)
  }

  // AI2 common libraries
  private def common(name: String) = "org.allenai.common" %% s"common-$name" % "2015.04.01-0"
  val allenAiCommon = common("core")
  val allenAiTestkit = common("testkit")
  val allenAiWebapp = common("webapp")

  val scopt = "com.github.scopt" %% "scopt" % "3.3.0"
  val typesafeConfig = "com.typesafe" % "config" % "1.2.1"

  // Akka
  val akkaVersion = "2.3.9"
  def akkaModule(id: String): ModuleID = "com.typesafe.akka" %% s"akka-$id" % akkaVersion
  val akkaActor = akkaModule("actor") exclude ("com.typesafe", "config")
  val akkaLogging = akkaModule("slf4j")
  val akkaTestkit = akkaModule("testkit")

  // Spray
  val sprayVersion = "1.3.2"
  def sprayModule(id: String): ModuleID = "io.spray" %% s"spray-$id" % sprayVersion
  val sprayCan = sprayModule("can")
  val sprayRouting = sprayModule("routing")
  val sprayClient = sprayModule("client")
  val sprayTestkit = sprayModule("testkit")
  val sprayCaching = sprayModule("caching")

  // Spray json (separate from Spray toolkit)
  val sprayJson = "io.spray" %% "spray-json" % "1.3.1"

  val loggingDependencyOverrides = Set(
    Logging.slf4jApi,
    Logging.logbackCore,
    Logging.logbackClassic
  )
}

object CoreDependencies extends CoreDependencies
