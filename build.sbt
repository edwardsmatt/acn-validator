lazy val commonSettings = Seq(
  organization := "com.github.edwardsmatt",
  version := "0.0.1",
  scalaVersion := "2.11.6"
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "acn-validator",
    libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4",
    libraryDependencies += "org.scalautils" % "scalautils_2.11" % "2.1.5",
    libraryDependencies += "com.google.guava" % "guava" % "18.0",
    libraryDependencies += "org.scalaz" % "scalaz-core_2.11" % "7.1.1",
    libraryDependencies += "com.google.code.findbugs" % "jsr305" % "3.0.0",

    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
  )