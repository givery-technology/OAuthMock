package controllers

import play.api._
import play.api.mvc._
import services.GitHub.GitHubOAuthProvider

object Login extends Controller {

  def github = Action {
    val params: Map[String, String] = Map(
      "client_id" -> "a90710980838f3677f40",
      "redirect_uri" -> "http://localhost:9000/login/github/callback",
      "scope" -> Set("user", "repo").mkString(","),
      "state" -> scala.util.Random.alphanumeric.take(20).mkString
    )
    Redirect(GitHubOAuthProvider.requestAccessUri(params))
  }
  def githubCallback = Action { request =>
    Ok(views.html.index(""))
  }

}
