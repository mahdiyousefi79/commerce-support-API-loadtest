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
    val commerceSupportAPI = http("commerceSupportAPI")
      .post(s"${config("commerceService.url")}/commerce/subscriptions/support/sku/a85d8131-0086-4d5a-9df9-ee15f7909aec?timeZoneMinutes=-300")
      .header("Authorization", "#{bearer}")
      .header("oat", "#{oat}")
      .header("Content-Type", "application/json")
      .header("msopartner", "comcast")
      .header("sat-client-id", "x1:commerce-prod-test:e08fca")
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


  class supportAPI extends Simulation with CommerceBase {

    val accounts = csv("data.csv").circular
    val totalTime = testTime + rampTime
    println(s"\nenv=$env")
    println(s"userCount=$userCount")
    println(s"requests=$requestCount")

    val scn = scenario("supportAPI")
      .exec(_.set("appId", "NbcuPeacock"))
      .feed(accounts)
      .exec(_.set("baseTime", baseTime))
      .exec(_.set("bearer", bearer))
      .repeat(requestCount) {
        exec(commerceSupportAPI)
      }
    setUp(scn.inject(atOnceUsers(1), rampUsers(userCount).during(testTime.minutes)))
  }

