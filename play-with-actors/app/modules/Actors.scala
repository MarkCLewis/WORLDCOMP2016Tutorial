package modules

import com.google.inject.AbstractModule

import actors.FinancialDataActor
import akka.actor.ActorSystem
import akka.actor.Props
import javax.inject.Inject
import play.api.libs.ws.WSClient

class Actors @Inject() (actorSystem: ActorSystem, ws: WSClient) extends ApplicationActor {
  actorSystem.actorOf(Props[FinancialDataActor],"FinancialData")
}

trait ApplicationActor

class ActorsModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ApplicationActor]).to(classOf[Actors]).asEagerSingleton
  }
}
