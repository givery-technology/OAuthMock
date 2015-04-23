package services.facebook

import services.oauth2.{TokenRequest, AccessRequest, OAuth2Provider}


class FacebookAccessRequest(val clientId: String, val redirectUri: String) extends AccessRequest {
  val method = "GET"
  val requestUri = "https://www.facebook.com/dialog/oauth"
  val optionalParams = Map(
    "display" -> "page",
    "scope" -> ""
  )
}
class FacebookTokenRequest(val clientId: String, val clientSecret: String, val redirectUri: String) extends TokenRequest {
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
