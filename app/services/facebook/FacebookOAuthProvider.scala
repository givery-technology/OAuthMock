package services.facebook

import scala.concurrent.Promise
import scala.concurrent.Future
import java.util.UUID

import com.ning.http.client.{Response, AsyncHttpClient, RequestBuilder, AsyncCompletionHandler}
import com.ning.http.util.UTF8UrlEncoder
import org.json4s._
import org.json4s.jackson.JsonMethods

sealed abstract class OAuthProvider(clientId: String, clientSecret: String, redirectUri: String) {
  private implicit val format: Formats = DefaultFormats

  protected val accessRequestUri: String
  protected val tokenRequestUri: String

  def requestAccessUri(scope: String*) = {
    val params = Map[String, String](
      "client_id" -> clientId,
      "redirect_uri" -> redirectUri,
      "display" -> "page",
      "scope" -> scope.mkString(","),
      "state" -> UUID.randomUUID.toString,
      "response_type" -> "code"
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
    val builder: RequestBuilder = new RequestBuilder("GET")
      .setHeader("Accept", "application/json")
      .setFollowRedirects(true)
      .setUrl(tokenRequestUri)
    params.foreach { case (k, v) => builder.addQueryParam(k, v) }

    val deferred = Promise[String]()
    client.prepareRequest(builder.build).execute(new AsyncCompletionHandler[Response]() {
      def onCompleted(res: Response) = {
        val json = JsonMethods.parse(res.getResponseBody("utf-8"))
        val token = (json \ "access_token").extract[Option[String]].getOrElse("")
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

class FacebookOAuthProvider(clientId: String, clientSecret: String, redirectUri: String) extends OAuthProvider(clientId, clientSecret, redirectUri) {
  protected val accessRequestUri = "https://www.facebook.com/dialog/oauth"
  protected val tokenRequestUri = "https://graph.facebook.com/v2.3/oauth/access_token"
}

object FacebookOAuthProvider {
  def apply(clientId: String, clientSecret: String, redirectUri: String) = new FacebookOAuthProvider(clientId, clientSecret, redirectUri)
}
