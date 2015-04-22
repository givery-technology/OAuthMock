package controllers

import play.api._
import play.api.mvc._
import services.github.GitHubOAuthProvider
import scala.concurrent.ExecutionContext.Implicits.global

object Login extends Controller {
  val githubClientId: String = sys.env("GITHUB_CLIENT_ID")
  val githubClientSecret: String = sys.env("GITHUB_CLIENT_SECRET")
  val facebookClientId: String = sys.env("FACEBOOK_CLIENT_ID")
  val facebookClientSecret: String = sys.env("FACEBOOK_CLIENT_SECRET")

  private def callbackUrl[T](service: String)(implicit request: Request[T]) = {
    val protocol = if (request.secure) "https" else "http"
    s"${protocol}://${request.host}/login/${service}/callback"
  }

  def github = Action { implicit request =>
    val oauth = GitHubOAuthProvider(githubClientId, githubClientSecret, callbackUrl("github"))
    Redirect(oauth.requestAccessUri("user", "repo"))
  }
  def githubCallback = Action.async { implicit request =>
    val oauth = GitHubOAuthProvider(githubClientId, githubClientSecret, callbackUrl("github"))
    val code: String = request.getQueryString("code").getOrElse("")
    oauth.requestToken(code).map(token =>
      Ok(views.html.index(token))
    )
  }

  def githubConfirm = Action {
    Ok(views.html.index("confirmed!!"))
  }

  def facebook = Action { implicit request =>
    Redirect("http://url.to.redirect")
  }

  def facebookCallback = Action.async { implicit request =>
    Ok(views.html.index("callback"))
  }

}
