package controllers

import javax.inject.Inject
import play.api.mvc.Controller
import play.api.mvc.Action
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import play.api.libs.concurrent.Execution.Implicits._

class Unemployment @Inject() (dbConfigProvider: DatabaseConfigProvider) extends Controller {
  
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  implicit val db = dbConfig.db
  
  def areaList(term: String) = Action.async { implicit request =>
    val areas = models.Queries.areasMatchingString(term)
    areas.map(a => Ok(views.html.areas(a)))
  }
  
  def dataList(terms: String) = Action.async { implicit request =>
    val data = models.Queries.dataMatchingMultipleSeriesStrings(terms.split(":"))
    data.map(d => Ok(views.html.dataView(d)))
  }
  
  // The following were used for setup.
  
  def codeGen = Action {
    models.SlickCodeGen.generateCode
    Ok("Code generated.")
  }
  
  def deleteTables = Action.async {
    val f = models.PopulateTables.deleteTables
    f.map(s => Ok(s))
  }

  def populateTables = Action.async {
    val f = models.PopulateTables.populateTables
    f.map(s => Ok(s))
  }
}