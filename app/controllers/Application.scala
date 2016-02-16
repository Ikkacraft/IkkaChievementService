package controllers

import Actions.Authorized
import play.api.mvc._

class Application extends Controller {

  def index = Authorized {
    Ok("Your new application is ready.")
  }

}