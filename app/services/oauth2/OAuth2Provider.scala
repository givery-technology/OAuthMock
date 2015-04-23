package services.oauth2

import com.ning.http.client.{Response, AsyncCompletionHandler, RequestBuilder, AsyncHttpClient}
import org.json4s.{Formats, DefaultFormats}
import org.json4s.jackson.JsonMethods

import scala.concurrent.{Promise, Future}

abstract class OAuth2Provider(clientId: String, clientSecret: String, redirectUri: String) {
  private implicit val format: Formats = DefaultFormats

  protected val accessRequest: AccessRequest
  protected val tokenRequest: TokenRequest

  def requestAccessUri(scope: String*) = {
    accessRequest.uri(clientId, redirectUri, scope)
  }

  def requestToken(code: String): Future[String] = {
    val params: Map[String, String] = tokenRequest.params(clientId, clientSecret, redirectUri, Map("code" -> code))
    val client: AsyncHttpClient = new AsyncHttpClient
    val builder: RequestBuilder = new RequestBuilder(tokenRequest.method)
      .setHeader("Accept", "application/json")
      .setFollowRedirects(true)
      .setUrl(tokenRequest.requestUri)

    tokenRequest.method match {
      case "GET" => params.foreach { case (k, v) => builder.addQueryParam(k, v) }
      case "POST" => params.foreach { case (k, v) => builder.addFormParam(k, v) }
    }

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
