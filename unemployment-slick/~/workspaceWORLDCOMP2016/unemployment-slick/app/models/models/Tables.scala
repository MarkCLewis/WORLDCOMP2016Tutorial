package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Areas.schema ++ Data.schema ++ Series.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Areas
   *  @param areacode Database column areaCode SqlType(CHAR), PrimaryKey, Length(15,false)
   *  @param areatext Database column areaText SqlType(VARCHAR), Length(200,true) */
  case class AreasRow(areacode: String, areatext: String)
  /** GetResult implicit for fetching AreasRow objects using plain SQL queries */
  implicit def GetResultAreasRow(implicit e0: GR[String]): GR[AreasRow] = GR{
    prs => import prs._
    AreasRow.tupled((<<[String], <<[String]))
  }
  /** Table description of table areas. Objects of this class serve as prototypes for rows in queries. */
  class Areas(_tableTag: Tag) extends Table[AreasRow](_tableTag, "areas") {
    def * = (areacode, areatext) <> (AreasRow.tupled, AreasRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(areacode), Rep.Some(areatext)).shaped.<>({r=>import r._; _1.map(_=> AreasRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column areaCode SqlType(CHAR), PrimaryKey, Length(15,false) */
    val areacode: Rep[String] = column[String]("areaCode", O.PrimaryKey, O.Length(15,varying=false))
    /** Database column areaText SqlType(VARCHAR), Length(200,true) */
    val areatext: Rep[String] = column[String]("areaText", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table Areas */
  lazy val Areas = new TableQuery(tag => new Areas(tag))

  /** Entity class storing rows of table Data
   *  @param dataid Database column dataid SqlType(INT), AutoInc, PrimaryKey
   *  @param seriesid Database column seriesid SqlType(CHAR), Length(20,false)
   *  @param year Database column year SqlType(INT)
   *  @param period Database column period SqlType(INT)
   *  @param value Database column value SqlType(DOUBLE) */
  case class DataRow(dataid: Int, seriesid: String, year: Int, period: Int, value: Double)
  /** GetResult implicit for fetching DataRow objects using plain SQL queries */
  implicit def GetResultDataRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Double]): GR[DataRow] = GR{
    prs => import prs._
    DataRow.tupled((<<[Int], <<[String], <<[Int], <<[Int], <<[Double]))
  }
  /** Table description of table data. Objects of this class serve as prototypes for rows in queries. */
  class Data(_tableTag: Tag) extends Table[DataRow](_tableTag, "data") {
    def * = (dataid, seriesid, year, period, value) <> (DataRow.tupled, DataRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(dataid), Rep.Some(seriesid), Rep.Some(year), Rep.Some(period), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> DataRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column dataid SqlType(INT), AutoInc, PrimaryKey */
    val dataid: Rep[Int] = column[Int]("dataid", O.AutoInc, O.PrimaryKey)
    /** Database column seriesid SqlType(CHAR), Length(20,false) */
    val seriesid: Rep[String] = column[String]("seriesid", O.Length(20,varying=false))
    /** Database column year SqlType(INT) */
    val year: Rep[Int] = column[Int]("year")
    /** Database column period SqlType(INT) */
    val period: Rep[Int] = column[Int]("period")
    /** Database column value SqlType(DOUBLE) */
    val value: Rep[Double] = column[Double]("value")

    /** Foreign key referencing Series (database name data_ibfk_1) */
    lazy val seriesFk = foreignKey("data_ibfk_1", seriesid, Series)(r => r.seriesid, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Data */
  lazy val Data = new TableQuery(tag => new Data(tag))

  /** Entity class storing rows of table Series
   *  @param seriesid Database column seriesid SqlType(CHAR), PrimaryKey, Length(20,false)
   *  @param areatypecode Database column areaTypeCode SqlType(CHAR)
   *  @param areacode Database column areaCode SqlType(CHAR), Length(15,false)
   *  @param seriestitle Database column seriesTitle SqlType(VARCHAR), Length(200,true)
   *  @param beginyear Database column beginYear SqlType(INT)
   *  @param beginperiod Database column beginPeriod SqlType(INT)
   *  @param endyear Database column endYear SqlType(INT)
   *  @param endperiod Database column endPeriod SqlType(INT) */
  case class SeriesRow(seriesid: String, areatypecode: Char, areacode: String, seriestitle: String, beginyear: Int, beginperiod: Int, endyear: Int, endperiod: Int)
  /** GetResult implicit for fetching SeriesRow objects using plain SQL queries */
  implicit def GetResultSeriesRow(implicit e0: GR[String], e1: GR[Char], e2: GR[Int]): GR[SeriesRow] = GR{
    prs => import prs._
    SeriesRow.tupled((<<[String], <<[Char], <<[String], <<[String], <<[Int], <<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table series. Objects of this class serve as prototypes for rows in queries. */
  class Series(_tableTag: Tag) extends Table[SeriesRow](_tableTag, "series") {
    def * = (seriesid, areatypecode, areacode, seriestitle, beginyear, beginperiod, endyear, endperiod) <> (SeriesRow.tupled, SeriesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(seriesid), Rep.Some(areatypecode), Rep.Some(areacode), Rep.Some(seriestitle), Rep.Some(beginyear), Rep.Some(beginperiod), Rep.Some(endyear), Rep.Some(endperiod)).shaped.<>({r=>import r._; _1.map(_=> SeriesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column seriesid SqlType(CHAR), PrimaryKey, Length(20,false) */
    val seriesid: Rep[String] = column[String]("seriesid", O.PrimaryKey, O.Length(20,varying=false))
    /** Database column areaTypeCode SqlType(CHAR) */
    val areatypecode: Rep[Char] = column[Char]("areaTypeCode")
    /** Database column areaCode SqlType(CHAR), Length(15,false) */
    val areacode: Rep[String] = column[String]("areaCode", O.Length(15,varying=false))
    /** Database column seriesTitle SqlType(VARCHAR), Length(200,true) */
    val seriestitle: Rep[String] = column[String]("seriesTitle", O.Length(200,varying=true))
    /** Database column beginYear SqlType(INT) */
    val beginyear: Rep[Int] = column[Int]("beginYear")
    /** Database column beginPeriod SqlType(INT) */
    val beginperiod: Rep[Int] = column[Int]("beginPeriod")
    /** Database column endYear SqlType(INT) */
    val endyear: Rep[Int] = column[Int]("endYear")
    /** Database column endPeriod SqlType(INT) */
    val endperiod: Rep[Int] = column[Int]("endPeriod")

    /** Foreign key referencing Areas (database name series_ibfk_1) */
    lazy val areasFk = foreignKey("series_ibfk_1", areacode, Areas)(r => r.areacode, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Series */
  lazy val Series = new TableQuery(tag => new Series(tag))
}
