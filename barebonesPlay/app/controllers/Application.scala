package controllers

import play.mvc.Controller;
import play.api.mvc.Action
import play.api.mvc.Results._
import javax.inject.Inject
import play.twirl.api.Html

class Application @Inject() extends Controller {
  def index = Action {
	  Ok(views.html.index("Reactive Programming"))
  }
  
  def add(a: Int, b: Int) = Action {
    Ok(views.html.main("Adding")(Html(s"$a + $b = ${a+b}")))
  }
  
  def factorial(n: Int) = Action {
    Ok(views.html.index(s"$n! = ${(BigInt(1) to BigInt(n)).product}"))
  }
}
