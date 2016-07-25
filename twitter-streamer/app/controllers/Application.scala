package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.mvc.WebSocket
import javax.inject.Inject
import play.api.libs.oauth.RequestToken
import play.api.libs.oauth.ConsumerKey
import play.api.libs.json.JsValue
import actors.TwitterStreamer
import play.api.libs.json._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.Materializer
import play.api.libs.streams._
import akka.actor.Props
import play.api.libs.ws.WSClient

class Application @Inject() (
    implicit environment: play.api.Environment, 
    configuration: play.api.Configuration, 
    system: ActorSystem, 
    materializer: Materializer, 
    ws: WSClient) extends Controller {
  
  TwitterStreamer.init
  
  // The following three methods were taken from the chapter 2 example in Reactive Web Applications
  def index = Action { implicit request =>
    Ok(views.html.index("Tweets"))
  }

  def tweets = WebSocket.accept[String, String] { request =>
    println("Accepting WebSocket")
    ActorFlow.actorRef(out => Props(new TwitterStreamer(out)))
  }

}