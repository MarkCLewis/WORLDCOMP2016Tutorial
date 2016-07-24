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
  
  def areaList = Action {
    Ok("Show areas here")
  }
  
  
  // The following were used for setup.
  
  def codeGen = Action {
    models.SlickCodeGen.generateCode
    Ok("Code generated.")
  }
  
  def populateTables = Action.async {
    implicit val db = dbConfig.db
    val f = models.PopulateTables.populateTables
    f.map(s => Ok(s))
  }
}