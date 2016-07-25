package actors

import akka.actor.Actor
import org.apache.commons.math3.fitting.WeightedObservedPoints
import org.apache.commons.math3.fitting.PolynomialCurveFitter

class FunctionFitter extends Actor {
  import FunctionFitter._
  def receive = {
    case FitData(data) =>
      println("Do fit")
      val pnts = new WeightedObservedPoints();
      for(row <- data) pnts.add(row(0), row(4))
      val fitter = PolynomialCurveFitter.create(1)
      val coeff = fitter.fit(pnts.toList())
      sender ! "Fit is "+coeff.mkString(" + ")
  }
}

object FunctionFitter {
  case class FitData(data: Seq[Array[Double]])
}