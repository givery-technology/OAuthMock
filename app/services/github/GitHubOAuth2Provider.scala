package services.github

import services.oauth2.{OAuth2Provider, TokenRequest, AccessRequest}

class GitHubAccessRequest(val clientId: String, val redirectUri: String) extends AccessRequest {
  val method = "GET"
  val requestUri = "https://github.com/login/oauth/authorize"
  val optionalParams = Map("scope" -> "")
}
class GitHubTokenRequest(val clientId: String, val clientSecret: String, val redirectUri: String) extends TokenRequest {
  val method = "POST"
  val requestUri = "https://github.com/login/oauth/access_token"
  val optionalParams: Map[String, String] = Map.empty
}

class GitHubOAuth2Provider(clientId: String, clientSecret: String, redirectUri: String) extends OAuth2Provider(clientId, clientSecret, redirectUri) {
  val accessRequest = new GitHubAccessRequest(clientId, redirectUri)
  val tokenRequest = new GitHubTokenRequest(clientId, clientSecret, redirectUri)
}

object GitHubOAuth2Provider {

  def apply(clientId: String, clientSecret: String, redirectUri: String) = new GitHubOAuth2Provider(clientId, clientSecret, redirectUri)

}
