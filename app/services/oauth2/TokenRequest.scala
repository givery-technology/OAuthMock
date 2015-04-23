package services.oauth2

import java.util.UUID

trait TokenRequest {
  val clientId: String
  val clientSecret: String
  val redirectUri: String

  val method: String
  val requestUri: String
  protected val optionalParams: Map[String, String]

  val state: String = UUID.randomUUID.toString
  val responseType: String = "code"
  protected val baseParams: Map[String, String] = Map(
    "client_id" -> clientId,
    "client_secret" -> clientSecret,
    "redirect_uri" -> redirectUri,
    "state" -> state,
    "response_type" -> responseType
  )

  def params(moreParams: Map[String, String] = Map.empty): Map[String, String] = baseParams ++ optionalParams ++ moreParams

}
