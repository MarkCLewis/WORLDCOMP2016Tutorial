package actors

import akka.actor._
import play.api._
import play.api.libs.json._
import play.api.libs.oauth._
import play.api.libs.concurrent.Execution.Implicits._

import scala.collection.mutable.ArrayBuffer
import play.api.libs.iteratee.Iteratee
import play.api.libs.ws.WSClient
import play.api.libs.iteratee.Concurrent.Broadcaster
import play.api.libs.ws.StreamedResponse
import akka.stream.scaladsl.Sink
import akka.stream.ActorMaterializer
import scala.util.Try

/**
 * This code was taken from 
 */
class TwitterStreamer(out: ActorRef) extends Actor {
  def receive = {
    case _ =>
      Logger.info("Received subscription from a client")
      TwitterStreamer.subscribe(out)
  }
  
  override def preStart():Unit = {
    Logger.info("Making Twitter Streamer")
    super.preStart()
  }

  override def postStop():Unit = {
    Logger.info("Client unsubscribing from stream")
    TwitterStreamer.unsubscribe(out)
  }
}

object TwitterStreamer {

//  private var broadcastEnumerator: Option[Enumerator[JsObject]] = None
  private var stream: Option[StreamedResponse] = None

  private val subscribers = new ArrayBuffer[ActorRef]()

  def subscribe(out: ActorRef)(implicit context: ActorContext): Unit = {

    implicit val materializer = ActorMaterializer()
    // Connect to stream
    stream.foreach { source =>
      Logger.info(source.toString())
      source.body.
      scan("")((acc, curr) => if (acc.contains("\r\n")) curr.utf8String else acc + curr.utf8String)
						.filter(_.contains("\r\n"))
//						.map(_.trim)
//						.runWith(Sink.actorRef(out, "Done"))
						.runForeach { tweet =>
              Logger.info(tweet)
              subscribers.foreach(_ ! tweet.trim)
						}
    }
    
    Logger.info("Adding subscriber")
    subscribers += out
  }

  def unsubscribe(subscriber: ActorRef): Unit = {
      val index = subscribers.indexWhere(_ == subscriber)
      if (index > 0) {
        subscribers.remove(index)
        Logger.info("Unsubscribed client from stream")
      }
  }

  def init(implicit ws:WSClient, configuration: Configuration, system: ActorSystem): Unit = {

    credentials(configuration).map { case (consumerKey, requestToken) =>

      val maybeMasterNodeUrl = Option(System.getProperty("masterNodeUrl"))
      val url = maybeMasterNodeUrl.getOrElse {
        "https://stream.twitter.com/1.1/statuses/filter.json"
      }

      ws.url(url)
        .sign(OAuthCalculator(consumerKey, requestToken))
        .withQueryString("track" -> "vegas").stream().foreach { s => 
          stream = Some(s)
      }

    } getOrElse {
      Logger.error("Twitter credentials are not configured")
    }

  }

  private def credentials(configuration: Configuration) = for {
    apiKey <- configuration.getString("twitter.apiKey")
    apiSecret <- configuration.getString("twitter.apiSecret")
    token <- configuration.getString("twitter.token")
    tokenSecret <- configuration.getString("twitter.tokenSecret")
  } yield (ConsumerKey(apiKey, apiSecret), RequestToken(token, tokenSecret))


}
