package services.GitHub

import java.net.URLEncoder

object GitHubOAuthProvider {
  val accessRequestUri = "https://github.com/login/oauth/authorize"
  val tokenRequestUri = "https://github.com/login/oauth/access_token"

  def requestAccessUri(parameter: Map[String, String]): String = {
    val params: Map[String, String] = parameter + ("response_type" -> "token")
    val query: String = params.map { case (k, v) => k +"="+ URLEncoder.encode(v, "utf-8") }.mkString("&")
    accessRequestUri +"?"+ query
  }

}
