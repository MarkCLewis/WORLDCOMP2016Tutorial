package actors

import akka.actor.Actor

class DataLoader extends Actor {
  def receive = {
    case _ =>
  }
  // http://chart.finance.yahoo.com/table.csv?s=GOOG&a=5&b=24&c=2016&d=6&e=24&f=2016&g=d&ignore=.csv
}