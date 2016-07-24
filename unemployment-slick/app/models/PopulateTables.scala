package models

import slick.driver.MySQLDriver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext

object PopulateTables {
  def populateTables(implicit db: Database, ec: ExecutionContext) = {
//    val deletes = Seq(Tables.Areas.delete, Tables.Series.delete, Tables.Data.delete)
//    val deleteFutures = deletes.map(db.run)

    for {
//      deletes <- Future.sequence(deleteFutures)
      adds <- Future.sequence(Seq(
//          populateAreas
          populateSeries
//          populateData("sql/la.data.11.California")
//          populateData("sql/la.data.12.Colorado"), 
//          populateData("sql/la.data.35.Nevada")
//          populateData("sql/la.data.39.NewYork"), 
//          populateData("sql/la.data.51.Texas")
          ))
    } yield {
      println("Futures complete.")
//      s"The deletes removed ${deletes.mkString(", ")}. Then the adds gave $data1, $data2, $data3, $data4, $data5."
      "Good to go"
    }
  }

  def populateAreas(implicit db: Database, ec: ExecutionContext) = {
    val source = scala.io.Source.fromFile("sql/la.area")
    val lines = source.getLines.drop(1)
    val ret = db.run {
      Tables.Areas ++= lines.toSeq.map { line =>
        val parts = line.split("\t").map(_.trim)
        Tables.AreasRow(parts(1), parts(2))
      }
    }
    ret.foreach { _ =>
      println("Closing area source.");
      source.close
    }
    ret
  }

  def populateSeries(implicit db: Database, ec: ExecutionContext) = {
    val source = scala.io.Source.fromFile("sql/la.series")
    val lines = source.getLines.drop(1)
    val ret = db.run {
      Tables.Series ++= lines.toSeq.map { line =>
        val parts = line.split("\t").map(_.trim)
        Tables.SeriesRow(parts(0), parts(1)(0), parts(2), parts(7), parts(8).toInt, parts(9).drop(1).toInt, parts(10).toInt, parts(11).drop(1).toInt)
      }
    }
    ret.foreach { _ =>
      println("Closing series source.");
      source.close
    }
    ret
  }

  def populateData(file: String)(implicit db: Database, ec: ExecutionContext) = {
    val source = scala.io.Source.fromFile(file)
    val lines = source.getLines.drop(1)
    val ret = db.run {
      Tables.Data ++= lines.toSeq.map { line =>
        val parts = line.split("\t").map(_.trim)
        Tables.DataRow(0, parts(0), parts(1).toInt, parts(2).drop(1).toInt, parts(3).toDouble)
      }
    }
    ret.foreach { _ =>
      println(s"Closing $file source.");
      source.close
    }
    ret
  }
}