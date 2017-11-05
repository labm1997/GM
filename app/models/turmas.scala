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

case class Turma(ID: Int, letra: String, professor: String, vagas: Int)

class turmasDAO @Inject() (database: Database) {
  val parser : RowParser[Turma] = Macro.namedParser[Turma]
  def getTurmas(idmateria: Int) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_turmas.ID, tbl_turmas.letra, tbl_turmas.vagas, tbl_users.nome as professor FROM tbl_turmas LEFT JOIN tbl_users ON tbl_users.matricula=tbl_turmas.professor WHERE tbl_turmas.ID_MATERIA={idmateria}").on("idmateria" -> idmateria).as(parser.*)
  }
}

