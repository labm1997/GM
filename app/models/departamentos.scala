package models

import anorm.SQL
import anorm.SqlQuery
import anorm.RowParser
import anorm.Macro
import anorm.SqlStringInterpolation
import anorm.SqlParser
import play.api.Play.current
import javax.inject.Inject
import play.api.db.Database
import javax.inject.Singleton

case class departamentos(ID: Int, nome: String, sigla: String)

class departamentosDAO @Inject() (database: Database) {
  val parser : RowParser[departamentos] = Macro.namedParser[departamentos]
  def getDepartamentos() = database.withConnection { implicit connection => 
    SQL("SELECT ID, nome, sigla FROM tbl_departamentos").as(parser.*)
  }
}

