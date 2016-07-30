package actors

import akka.actor._
import play.api._
import play.api.libs.json._
import play.api.libs.oauth._
import play.api.libs.concurrent.Execution.Implicits._

import play.api.libs.iteratee.Iteratee
import play.api.libs.ws.WSClient
import play.api.libs.iteratee.Concurrent.Broadcaster
import play.api.libs.ws.StreamedResponse
import akka.stream.scaladsl.Sink
import akka.stream.ActorMaterializer
import scala.util.Try
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import akka.NotUsed
import akka.stream.scaladsl.Source

/**
 * This code was taken from
 */
class TwitterStreamer(out: ActorRef) extends Actor {
  def receive = {
    case _ =>
      Logger.info("Received subscription from a client")
      TwitterStreamer.subscribe(out)
  }

  override def preStart(): Unit = {
    Logger.info("Making Twitter Streamer")
    super.preStart()
  }

  override def postStop(): Unit = {
    Logger.info("Client unsubscribing from stream")
    TwitterStreamer.unsubscribe(out)
  }
}

object TwitterStreamer {

  //  private var broadcastEnumerator: Option[Enumerator[JsObject]] = None
  private var stream: Option[Source[String, _]] = None

  private var subscribers = List[ActorRef]()

  def subscribe(out: ActorRef)(implicit context: ActorContext): Unit = {
    Logger.info("Adding subscriber")
    subscribers ::= out

    if (subscribers.size == 1) {
      implicit val materializer = ActorMaterializer()
      // Connect to stream
      stream.foreach { source =>
        Logger.info(source.toString())
        source.runForeach { tweet =>
          Logger.info(tweet)
          subscribers.foreach(_ ! tweet.trim)
        }
      }
    }
  }

  def unsubscribe(subscriber: ActorRef): Unit = {
      subscribers = subscribers.filter(_ != subscriber)
      Logger.info("Unsubscribed client from stream")
  }

  def init(implicit ws: WSClient, configuration: Configuration, system: ActorSystem): Unit = {

    credentials(configuration).map {
      case (consumerKey, requestToken) =>

        val maybeMasterNodeUrl = Option(System.getProperty("masterNodeUrl"))
        val url = maybeMasterNodeUrl.getOrElse {
          "https://stream.twitter.com/1.1/statuses/filter.json"
        }

        ws.url(url)
          .sign(OAuthCalculator(consumerKey, requestToken))
          .withQueryString("track" -> "vegas").stream().foreach { s =>
            stream = Some(s.body.
              scan("")((acc, curr) => if (acc.contains("\r\n")) curr.utf8String else acc + curr.utf8String)
              .filter(_.contains("\r\n")))
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
