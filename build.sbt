import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.edvinprosystems"

lazy val root = (project in file("."))
  .settings(
    name := "Composearch",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += scalaMock % Test    
  )

