package controllers

import javax.inject.Inject
import play.api.mvc.Controller
import akka.actor.ActorSystem
import play.api.mvc.Action
import akka.pattern._
import actors.FinancialDataActor
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.duration._
import akka.util.Timeout
import akka.actor.Props
import play.api.libs.ws.WSClient

class Application @Inject() (ws:WSClient, system: ActorSystem) extends Controller {
  
  lazy val fdActor = system.actorSelection("akka://application/user/FinancialData")
//  lazy val fdActor = system.actorOf(Props(new FinancialDataActor(ws)),"localFD")
  implicit val timeout = Timeout(10.second)
  
  def fitStockPrices(symbol:String, startDay:Int, startMonth:Int, startYear:Int,
      endDay:Int, endMonth:Int, endYear:Int) = Action.async {
    println("controller")
    val fit = fdActor ? FinancialDataActor.RequestFits(symbol, startDay, startMonth, startYear,
        endDay, endMonth, endYear)
    fit.map {
      case res => Ok(res.toString)
    }
  }
  
  def pingActor = Action.async { implicit request =>
    println("check")
    val response = fdActor ? FinancialDataActor.Ping
    response.map(s => Ok(s.toString))
  }
}