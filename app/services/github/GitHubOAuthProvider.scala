package services.github

import services.oauth2.{OAuth2Provider, TokenRequest, AccessRequest}

class GitHubAccessRequest(clientId: String, redirectUri: String) extends AccessRequest(clientId, redirectUri) {
  val method = "GET"
  val requestUri = "https://github.com/login/oauth/authorize"
  val optionalParams = Map("scope" -> "")
}
class GitHubTokenRequest(clientId: String, clientSecret: String, redirectUri: String) extends TokenRequest {
  val method = "POST"
  val requestUri = "https://github.com/login/oauth/access_token"
  val optionalParams: Map[String, String] = Map.empty
}

class GitHubOAuthProvider(clientId: String, clientSecret: String, redirectUri: String) extends OAuth2Provider(clientId, clientSecret, redirectUri) {
  val accessRequest = new GitHubAccessRequest(clientId, redirectUri)
  val tokenRequest = new GitHubTokenRequest(clientId, clientSecret, redirectUri)
}

object GitHubOAuthProvider {

  def apply(clientId: String, clientSecret: String, redirectUri: String) = new GitHubOAuthProvider(clientId, clientSecret, redirectUri)

}
