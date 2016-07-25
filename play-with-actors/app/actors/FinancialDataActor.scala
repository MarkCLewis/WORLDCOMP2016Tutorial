package actors

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import play.api.libs.ws.WSClient
import javax.inject.Inject

class FinancialDataActor extends Actor {
  private var dataLoader: ActorRef = _
  private var csvParser: ActorRef = _
  private var functionFitter: ActorRef = _
  
  override def preStart(): Unit = {
	  functionFitter = context.actorOf(Props[FunctionFitter],"FunctionFitter")    
    csvParser = context.actorOf(Props(new CSVParser(functionFitter)),"CSVParser")    
 		dataLoader = context.actorOf(Props(new DataLoader(csvParser)),"DataLoader")    
    super.preStart()
  }
  
  import FinancialDataActor._
  def receive = {
    case RequestFits(s, sd, sm, sy, ed, em, ey) =>
        println("Got request")
        val url = s"http://chart.finance.yahoo.com/table.csv?s=$s&a=$sm&b=$sd&c=$sy&d=$em&e=$ed&f=$ey&g=d&ignore=.csv"
        dataLoader forward DataLoader.LoadURL(url)
    case Ping => dataLoader forward Ping
  }
}

object FinancialDataActor {
  case class RequestFits(symbol:String, startDay:Int, startMonth:Int, startYear:Int,
      endDay:Int, endMonth:Int, endYear:Int)
  case object Ping
}