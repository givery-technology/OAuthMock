package services.GitHub

import com.ning.http.client.{Response, AsyncHttpClient, RequestBuilder}
import com.ning.http.util.UTF8UrlEncoder

object GitHubOAuthProvider {
  val accessRequestUri = "https://github.com/login/oauth/authorize"
  val tokenRequestUri = "https://github.com/login/oauth/access_token"

  def requestAccessUri(parameters: Map[String, String]): String = {
    val params: Map[String, String] = parameters + ("response_type" -> "token")
    val query: String = params.map { case (k, v) => k +"="+ UTF8UrlEncoder.encode(v) }.mkString("&")
    accessRequestUri +"?"+ query
  }

  def requestToken(parameters: Map[String, String]): String = {
    val client: AsyncHttpClient = new AsyncHttpClient
    val builder: RequestBuilder = new RequestBuilder("POST")
      .setHeader("Content-Type", "application/x-www-form-urlencoded")
      .setHeader("Accept", "application/json")
      .setFollowRedirects(true)
      .setUrl(tokenRequestUri)
    parameters.foreach { case (k, v) => builder.addFormParam(k, v) }
    val response: Response = client.prepareRequest(builder.build).execute.get
    response.getResponseBody("utf-8")
  }
}
