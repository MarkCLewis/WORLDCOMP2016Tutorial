package models

import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

object SlickCodeGen extends App {
  def generateCode = {
    // This is commented because Eclipse doesn't like it.
//    slick.codegen.SourceCodeGenerator.main(
//      Array("slick.driver.MySQLDriver", "com.mysql.jdbc.Driver", 
//          "jdbc:mysql://localhost/tutorial2016?user=mlewis&password=password", 
//          "/home/mlewis/workspaceWORLDCOMP2016/unemployment-slick/app/models", "models"))
  }
  
  generateCode
}