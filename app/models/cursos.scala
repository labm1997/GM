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

case class cursos(ID: Int, nome: String, totalcreditos: Int, minimocreditos: Int, maximocreditos: Int, modalidade: String, turno: String)

class cursosDAO @Inject() (database: Database) {
  val parser : RowParser[cursos] = Macro.namedParser[cursos]
  def getCursos() = database.withConnection { implicit connection => 
    SQL("SELECT ID, nome, totalcreditos, minimocreditos, maximocreditos, modalidade, turno FROM tbl_cursos").as(parser.*)
  }
}

