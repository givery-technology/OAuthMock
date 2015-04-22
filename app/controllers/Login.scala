package controllers

import play.api._
import play.api.mvc._
import services.github.GitHubOAuthProvider
import scala.concurrent.ExecutionContext.Implicits.global

object Login extends Controller {
  val clientId: String = sys.env("GITHUB_CLIENT_ID")
  val clientSecret: String = sys.env("GITHUB_CLIENT_SECRET")

  private def callbackUrl[T](implicit request: Request[T]) = {
    val protocol = if (request.secure) "https" else "http"
    s"${protocol}://${request.host}/login/github/callback"
  }

  def github = Action { implicit request =>
    val oauth = GitHubOAuthProvider(clientId, clientSecret, callbackUrl)
    Redirect(oauth.requestAccessUri("user", "repo"))
  }
  def githubCallback = Action.async { implicit request =>
    val oauth = GitHubOAuthProvider(clientId, clientSecret, callbackUrl)
    val code: String = request.getQueryString("code").getOrElse("")
    oauth.requestToken(code).map(token =>
      Ok(views.html.index(token))
    )
  }

  def githubConfirm = Action {
    Ok(views.html.index("confirmed!!"))
  }

}
