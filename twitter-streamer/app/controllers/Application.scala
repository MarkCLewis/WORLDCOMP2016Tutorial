package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import javax.inject.Inject
import play.api.libs.oauth.RequestToken
import play.api.libs.oauth.ConsumerKey

class Application @Inject() (environment: play.api.Environment, configuration: play.api.Configuration) extends Controller {
  val credentials = for {
    apiKey <- configuration.getString("twitter.apiKey")
    apiSecret <- configuration.getString("twitter.apiSecret")
    token <- configuration.getString("twitter.token")
    tokenSecret <- configuration.getString("twitter.tokenSecret")
  } yield {
    (ConsumerKey(apiKey, apiSecret), RequestToken(token, tokenSecret))
  }
  
  def index = Action {
    Ok("Your ad goes here. Soon there will be Twitter tweets.")
  }
}