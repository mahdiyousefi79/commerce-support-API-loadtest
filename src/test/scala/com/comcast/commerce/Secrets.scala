package com.comcast.commerce

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging
import io.gatling.core.Predef._
import io.gatling.core.session.{Expression, Session}
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest

import scala.jdk.CollectionConverters.MapHasAsScala

object Secrets extends StrictLogging {

  def getAwsSecrets(secretsName: String): Map[String, String] = {
    logger.info(s"Loading AWS secrets from $secretsName...")
    var map: java.util.Map[String, String] = null
    val secretsClient = SecretsManagerClient.builder.region(Region.US_EAST_1).build
    try {
      val valueRequest = GetSecretValueRequest.builder.secretId(secretsName).build
      val valueResponse = secretsClient.getSecretValue(valueRequest)
      val json: String = valueResponse.secretString
      if (json != null) {
        val mapper = new ObjectMapper
        map = mapper.readValue(json, new TypeReference[java.util.Map[String, String]]() {})
        if (map == null) {
          logger.error("Unable to load AWS secrets for " + secretsName)
        }
      }
    } catch {
      case e: Exception =>
        logger.error("Unable to load AWS secrets for " + secretsName, e)
    } finally {
      if (secretsClient != null) secretsClient.close()
    }
    if (map == null) {
      System.exit(-1)
    }
    map.asScala.toMap
  }

}
