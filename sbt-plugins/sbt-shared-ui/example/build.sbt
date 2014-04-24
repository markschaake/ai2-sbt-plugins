import spray.revolver.RevolverPlugin._
import SharedUiPlugin.SharedUiKeys

// Spray server settings
val serverSettings = Revolver.settings ++ Seq(
  resolvers += "spray repo" at "http://repo.spray.io",
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  libraryDependencies += "io.spray" % "spray-routing" % "1.2.0",
  libraryDependencies += "io.spray" % "spray-can" % "1.2.0",
  libraryDependencies += "com.typesafe.akka" %% s"akka-actor" % "2.2.3"
)

// The `shared` sub-project that will generate web asset resources
// that can be used by dependent projects.
// You must add the SharedUiPlugin.uiSettings to a project
// to enable web asset management (javascript linting, LESS processing, etc.)
lazy val shared = project.in(file("shared"))
  .addPlugins(SbtWeb)
  .settings(SharedUiPlugin.uiSettings: _*)
  .settings(
    // Force LESS processing of a single main file.
    // The single main file can import other files, but we
    // only want to generate a single CSS file for this example.
    SharedUiKeys.lessFilter := Some("shared.less")
  )

// A UI project that uses the `shared` project's assets.
// Simply add the SharedUiPlugin.uses settings
// These settings force building of shared web assets as
// a dependency for the dependent project's build.
lazy val ui1 = project.in(file("ui1"))
  .addPlugins(SbtWeb)
  .dependsOn(shared) // required to enable watching assets in `shared`
  .settings(SharedUiPlugin.uiSettings: _*)
  .settings(SharedUiPlugin.uses(shared): _*)
  .settings(serverSettings: _*)

// A UI project that uses another project with a dependency.
// Illustrates transitive dependecny.
lazy val ui3 = project.in(file("ui3"))
  .addPlugins(SbtWeb)
  .dependsOn(ui1)
  .settings(SharedUiPlugin.uiSettings: _*)
  .settings(SharedUiPlugin.uses(ui1): _*)
  .settings(serverSettings: _*)

// A second UI project that uses the `shared` project's assets
lazy val ui2 = project.in(file("ui2"))
  .addPlugins(SbtWeb)
  .dependsOn(shared) // required to enable watching assets in `shared`
  .settings(SharedUiPlugin.uiSettings: _*)
  .settings(SharedUiPlugin.uses(shared): _*)
  .settings(serverSettings: _*)

lazy val root = project.in(file(".")).aggregate(ui1, ui2, ui3, shared).settings(
  name := "sbt-shared-ui-tester",
  description := "Proof of technology project for the sbt-shared-ui plugin"
)
