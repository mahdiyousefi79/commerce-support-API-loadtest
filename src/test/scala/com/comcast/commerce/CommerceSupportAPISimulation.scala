package com.comcast.commerce
import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.core.session.{Expression, Session}
import io.gatling.http.Predef._
import java.time.Instant
import scala.concurrent.duration.{DurationInt}

trait CommerceBase {

  implicit val config = ConfigFactory.load()
  val env = Option(System.getenv("env")).getOrElse("preprod")

  def config(key: String): String = config.getString(s"$env.$key")

  def userCount = System.getProperty("USER_COUNT").toInt
  def testTime = System.getProperty("TEST_TIME").toInt
  def rampTime = System.getProperty("RAMP_TIME").toInt
  def bearer = System.getProperty("BEARER")
  def requestCount = System.getProperty("rc")
 
  def baseTime = {
    val now = Instant.now()
    val millis = (now.toEpochMilli / 1000L) * 1000L // truncate seconds
    Instant.ofEpochMilli(millis)
  }
    val supportAPI = http("supportAPI")
      .get(s"${config("commerceService.url")}/commerce/subscriptions/support/sku/59b8293e-e699-434d-ae0a-2e314cd10589?timeZoneMinutes=-300")
      .header("Authorization", "#{bearer}")
      .header("oat", "#{oat}")
      .header("Content-Type", "application/json")
      .header("msopartner", "acomcast")
      .header("sat-client-id", "x1:commerce-stage:771263")
      .header("Accept", "application/json;vnd.commerce.v2")
      .check(status.is(200))

    def printVar(key: String): Expression[Session] = (session: Session) => {
      println(s"$key: ${session(key).as[String]}")
      session
    }

    def log(s: String): Expression[Session] = (session: Session) => {
      println(s)
      session
    }

    def logEntitlement: Expression[Session] = (session: Session) => {

      val appId = session("appId").as[String]
      val oat = session("oat").as[String]
      println(s" appId=$appId, oat=$oat")
      session
    }
  }


  class GetEntitlements extends Simulation with CommerceBase {

    val accounts = csv("data.csv").circular
    val totalTime = testTime + rampTime
    println(s"\nenv=$env")
    println(s"userCount=$userCount")
    println(s"requests=$requestCount")

    val scn = scenario("Subscribe Support API")
      .exec(_.set("appId", "NbcuPeacock"))
      .feed(accounts)
      .exec(_.set("baseTime", baseTime))
      .exec(_.set("bearer", bearer))
      .repeat(requestCount) {
        exec(supportAPI)
      }
    setUp(scn.inject(atOnceUsers(1), rampUsers(userCount).during(testTime.minutes)))
  }

