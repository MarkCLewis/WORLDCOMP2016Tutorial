package actors

import akka.actor.Actor
import akka.actor.ActorRef
import javax.inject.Inject
import play.api.libs.ws._
import play.api.libs.concurrent.Execution.Implicits._
import akka.pattern._
import scala.concurrent.Future

class DataLoader(parser: ActorRef) extends Actor {
  import DataLoader._
  def receive = {
    case LoadURL(url) =>
      println("Loading")
      parser forward CSVParser.ParseString(scala.io.Source.fromURL(url).mkString)
      
    case FinancialDataActor.Ping =>
      sender ! "2nd layer good"
  }
}

object DataLoader {
  case class LoadURL(url: String)
}