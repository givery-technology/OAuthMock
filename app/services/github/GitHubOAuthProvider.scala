package services.github

import scala.concurrent.Promise
import scala.concurrent.Future
import java.util.UUID

import com.ning.http.client.{Response, AsyncHttpClient, RequestBuilder, AsyncCompletionHandler}
import com.ning.http.util.UTF8UrlEncoder
import org.json4s._
import org.json4s.jackson.JsonMethods

abstract class OAuthProvider(clientId: String, clientSecret: String, redirectUri: String) {
  private implicit val format: Formats = DefaultFormats

  protected val accessRequestUri: String
  protected val tokenRequestUri: String

  def requestAccessUri(scope: String*) = {
    val params = Map[String, String](
      "client_id" -> clientId,
      "redirect_uri" -> redirectUri,
      "scope" -> scope.mkString(","),
      "response_type" -> "token",
      "state" -> UUID.randomUUID.toString
    )
    val query: String = params.map { case (k, v) => k +"="+ UTF8UrlEncoder.encode(v) }.mkString("&")
    accessRequestUri +"?"+ query
  }

  def requestToken(code: String): Future[String] = {
    val params: Map[String, String] = Map(
      "client_id" -> clientId,
      "client_secret" -> clientSecret,
      "code" -> code,
      "redirect_uri" -> redirectUri
    )
    val client: AsyncHttpClient = new AsyncHttpClient
    val builder: RequestBuilder = new RequestBuilder("POST")
      .setHeader("Content-Type", "application/x-www-form-urlencoded")
      .setHeader("Accept", "application/json")
      .setFollowRedirects(true)
      .setUrl(tokenRequestUri)
    params.foreach { case (k, v) => builder.addFormParam(k, v) }

    val deferred = Promise[String]()
    client.prepareRequest(builder.build).execute(new AsyncCompletionHandler[Response]() {
      def onCompleted(res: Response) = {
        val json = JsonMethods.parse(res.getResponseBody("utf-8"))
        val token = (json \ "access_token").extract[String]
        deferred.success(token)
        res
      }
      override def onThrowable(t: Throwable) {
        deferred.failure(t)
        super.onThrowable(t)
      }
    })
    deferred.future
  }
}

class GitHubOAuthProvider(clientId: String, clientSecret: String, redirectUri: String) extends OAuthProvider(clientId, clientSecret, redirectUri) {
  protected val accessRequestUri = "https://github.com/login/oauth/authorize"
  protected val tokenRequestUri = "https://github.com/login/oauth/access_token"
}

object GitHubOAuthProvider {

  def apply(clientId: String, clientSecret: String, redirectUri: String) = new GitHubOAuthProvider(clientId, clientSecret, redirectUri)

}
