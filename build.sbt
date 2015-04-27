lazy val commonSettings = Seq(
  organization := "com.github.edwardsmatt",
  version := "0.0.1",
  scalaVersion := "2.11.6"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "acn-validator",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
  )

wartremoverWarnings ++= Warts.all