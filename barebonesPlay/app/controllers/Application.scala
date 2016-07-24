package controllers

import play.mvc.Controller;
import play.api.mvc.Action
import play.api.mvc.Results._
import javax.inject.Inject

class Application @Inject() extends Controller {
  def index = Action {
	  Ok("This is the text for the page.")
  }
}
