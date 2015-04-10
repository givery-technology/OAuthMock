package controllers

import play.api._
import play.api.mvc._

object Login extends Controller {

  def github = Action {
    Ok()
  }
  def githubCallback = Action {
    Ok()
  }

}
