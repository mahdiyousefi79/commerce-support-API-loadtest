import Dependencies._

enablePlugins(GatlingPlugin)

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.comcast",
      scalaVersion := "2.13.8",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "commerce-getentitlement-loadtest",
    libraryDependencies ++= gatling
)

val awsVersion = "2.17.228"

libraryDependencies += "software.amazon.awssdk" % "secretsmanager" % awsVersion
libraryDependencies += "software.amazon.awssdk" % "dynamodb" % awsVersion
