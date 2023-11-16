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

  def userCount = System.getProperty("USER_COUNT", "2000").toInt
//  def userCountTo = sys.env.getOrElse("USER_COUNT_TO", "100").toInt
  def testTime = System.getProperty("TEST_TIME", "3").toInt
  def rampTime = System.getProperty("RAMP_TIME", "1").toInt
  def bearer = System.getProperty("BEARER")
  def requestCount = 1
  def batchCount = 2000



  def baseTime = {
    val now = Instant.now()
    val millis = (now.toEpochMilli / 1000L) * 1000L // truncate seconds
    Instant.ofEpochMilli(millis)
  }
    val getEntitlements = http("getEntitlements")
      .get(s"${config("commerceService.url")}/commerce/account/#{oat}/entitlements?timeZoneMinutes=-420")
      .header("Authorization", "#{bearer}")
      .header("Content-Type", "application/json")
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

    val scn = scenario("Get Entitlement")
      .exec(_.set("appId", "NbcuPeacock"))
      .feed(accounts)
      .exec(_.set("baseTime", baseTime))
      .exec(_.set("bearer", bearer))
      .repeat(requestCount, "n") {
        exec(getEntitlements)
      }
    setUp(scn.inject(atOnceUsers(1), rampUsers(userCount).during(testTime.minutes)))
  }

