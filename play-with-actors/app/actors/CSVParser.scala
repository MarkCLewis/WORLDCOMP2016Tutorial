package actors

import akka.actor.Actor
import akka.actor.ActorRef
import java.time.LocalDate

class CSVParser(fitter: ActorRef) extends Actor {
  import CSVParser._
  def receive = {
    case ParseString(csv) =>
      println("parsing")
      val stringData = csv.split("\n").drop(1).filter(_.nonEmpty).map(_.trim.split(",").map(_.trim))
      // parse dates to number String
      stringData.foreach(row => row(0) = LocalDate.parse(row(0)).toEpochDay().toString)
      // convert rest to double
      val data = stringData.map(_.map(_.toDouble))
      fitter forward FunctionFitter.FitData(data)
  }
}

object CSVParser {
  case class ParseString(csv: String)
}