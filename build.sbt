ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
libraryDependencies += "org.scalatest" %% "scalatest-featurespec" % "3.2.15" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "TIP"
  )
