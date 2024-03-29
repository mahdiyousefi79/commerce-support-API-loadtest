import sbt._

object Dependencies {
  lazy val gatling = Seq(
    "io.gatling.highcharts" % "gatling-charts-highcharts",
    "io.gatling" % "gatling-test-framework"
  ).map(_ % "3.7.6" % Test)
}
