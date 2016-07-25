package modules

import actors.FinancialDataActor
import akka.actor.ActorSystem
import akka.actor.Props
import javax.inject.Inject
import com.google.inject.AbstractModule

class Actors @Inject() (actorSystem: ActorSystem) extends ApplicationActor {
  actorSystem.actorOf(Props[FinancialDataActor].withDispatcher("control-aware-dispatcher"),"FinancialData")
}

trait ApplicationActor

class ActorsModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ApplicationActor]).to(classOf[Actors]).asEagerSingleton
  }
}
