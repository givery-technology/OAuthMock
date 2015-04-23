package services.oauth2

import java.util.UUID

import com.ning.http.util.UTF8UrlEncoder

trait AccessRequest {

  val clientId: String
  val redirectUri: String

  val method: String
  val requestUri: String
  protected val optionalParams: Map[String, String]

  val state: String = UUID.randomUUID.toString
  val responseType: String = "code"
  protected val baseParams: Map[String, String] = Map(
    "client_id" -> clientId,
    "redirect_uri" -> redirectUri,
    "state" -> state,
    "response_type" -> responseType
  )

  def params(moreParams: Map[String, String] = Map.empty): Map[String, String] = baseParams ++ optionalParams ++ moreParams

  def uri(scope: Seq[String]): String = {
    val p = params(Map("scope" -> scope.mkString(",")))
    val query = p map { case (k, v) => k +"="+ UTF8UrlEncoder.encode(v) } mkString("&")
    requestUri +"?"+ query
  }
}
