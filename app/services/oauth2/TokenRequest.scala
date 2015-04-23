package services.oauth2

import java.util.UUID

import com.ning.http.util.UTF8UrlEncoder

abstract class TokenRequest (clientId: String, clientSecret: String, redirectUri: String) {

  val method: String
  val requestUri: String
  protected val optionalParams: Map[String, String]

  val state: String = UUID.randomUUID.toString
  val reponseType: String = "code"
  protected val baseParams: Map[String, String] = Map(
    "client_id" -> clientId,
    "client_secret" -> clientSecret,
    "redirect_uri" -> redirectUri,
    "state" -> state,
    "response_type" -> "code"
  )

  def params(moreParams: Map[String, String] = Map.empty): Map[String, String] = baseParams ++ optionalParams ++ moreParams

}
