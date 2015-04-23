package services.facebook

import services.oauth2.{TokenRequest, AccessRequest, OAuth2Provider}


class FacebookAccessRequest(clientId: String, redirectUri: String) extends AccessRequest(clientId, redirectUri) {
  val method = "GET"
  val requestUri = "https://www.facebook.com/dialog/oauth"
  val optionalParams = Map(
    "display" -> "page",
    "scope" -> ""
  )
}
class FacebookTokenRequest(clientId: String, clientSecret: String, redirectUri: String) extends TokenRequest(clientId, clientSecret, redirectUri) {
  val method = "GET"
  val requestUri = "https://graph.facebook.com/v2.3/oauth/access_token"
  val optionalParams: Map[String, String] = Map.empty
}

class FacebookOAuthProvider(clientId: String, clientSecret: String, redirectUri: String) extends OAuth2Provider(clientId, clientSecret, redirectUri) {
  protected val accessRequest = new FacebookAccessRequest(clientId, redirectUri)
  protected val tokenRequest = new FacebookTokenRequest(clientId, clientSecret, redirectUri)
}

object FacebookOAuthProvider {
  def apply(clientId: String, clientSecret: String, redirectUri: String) = new FacebookOAuthProvider(clientId, clientSecret, redirectUri)
}
