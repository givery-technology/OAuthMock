package controllers

import play.api._
import play.api.mvc._
import services.GitHub.GitHubOAuthProvider

object Login extends Controller {

  def github = Action { request =>
    val protocol = if (request.secure) "https" else "http"
    val params: Map[String, String] = Map(
      "client_id" -> "",
      "redirect_uri" -> "%s://%s/login/github/callback".format(protocol, request.host),
      "scope" -> Set("user", "repo").mkString(","),
      "state" -> scala.util.Random.alphanumeric.take(20).mkString
    )
    Redirect(GitHubOAuthProvider.requestAccessUri(params))
  }
  def githubCallback = Action { request =>
    val protocol = if (request.secure) "https" else "http"
    val code: String = request.getQueryString("code").getOrElse("")
    val params: Map[String, String] = Map(
      "client_id" -> "",
      "client_secret" -> "",
      "code" -> code,
      "redirect_uri" -> "%s://%s/login/github/callback".format(protocol, request.host)
    )
    //Ok(views.html.index(GitHubOAuthProvider.requestToken(params)))
    val token = GitHubOAuthProvider.requestToken(params)
    Redirect("%s://%s/login/github/confirm".format(protocol, request.host))
  }

  def githubConfirm = Action {
    Ok(views.html.index("confirmed!!"))
  }

}
