package models

import slick.driver.MySQLDriver.api._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object Queries {
  def areasMatchingString(term: String)(implicit db: Database, ec: ExecutionContext): Future[Seq[Tables.AreasRow]] = {
    db.run {
      (for {
        area <- Tables.Areas if area.areatext like s"%$term%"
      } yield area).result
    }
  }
  
  def dataMatchingAreaString(term: String)(implicit db: Database, ec: ExecutionContext): Future[Seq[Tables.DataRow]] = {
    db.run {
      (for {
        area <- Tables.Areas if area.areatext like s"%$term%"
        series <- Tables.Series if series.areacode === area.areacode
        data <- Tables.Data if data.seriesid === series.seriesid
      } yield data).result
    }
  }

  def dataMatchingMultipleSeriesStrings(terms: Seq[String])(implicit db: Database, ec: ExecutionContext): Future[Seq[Tables.DataRow]] = {
    db.run {
      (for {
        series <- Tables.Series if series.seriestitle like terms.mkString("%", "%", "%")
        data <- Tables.Data if data.seriesid === series.seriesid
      } yield data).result
    }
  }
}