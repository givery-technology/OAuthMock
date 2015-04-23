package services.oauth2

import java.util.UUID

import com.ning.http.util.UTF8UrlEncoder

trait AccessRequest {

  val method: String
  val requestUri: String
  protected val optionalParams: Map[String, String]

  val state: String = UUID.randomUUID.toString
  val responseType: String = "code"

  def params(clientId: String, redirectUri: String, moreParams: Map[String, String] = Map.empty): Map[String, String] = Map(
    "client_id" -> clientId,
    "redirect_uri" -> redirectUri,
    "state" -> state,
    "response_type" -> responseType
  ) ++ optionalParams ++ moreParams

  def uri(clientId: String, redirectUri: String, scope: Seq[String]): String = {
    val p = params(clientId, redirectUri, Map("scope" -> scope.mkString(",")))
    val query = p map { case (k, v) => k +"="+ UTF8UrlEncoder.encode(v) } mkString("&")
    requestUri +"?"+ query
  }
}
