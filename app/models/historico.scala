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

case class HistoricoItem(ID_MATERIA: Option[Int], nome: Option[String], mençao: Option[String], semestre: Option[String], creditos: Option[Int], tipo: Option[String])

class historicoDAO @Inject() (database: Database) {
  val parser : RowParser[HistoricoItem] = Macro.namedParser[HistoricoItem]
  def getHistorico(id: String) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_historico.ID_MATERIA, tbl_materias.nome, tbl_mençoes.sigla AS mençao, tbl_historico.semestre, tbl_materias.creditos, (SELECT type FROM tbl_cursos_materias WHERE ID_CURSO=(SELECT curso FROM tbl_users WHERE matricula={id}) AND ID_MATERIA=tbl_historico.ID_MATERIA) as tipo FROM tbl_historico LEFT JOIN tbl_materias ON tbl_materias.ID=tbl_historico.ID_MATERIA LEFT JOIN tbl_mençoes ON tbl_mençoes.ID=tbl_historico.mençao WHERE tbl_historico.matricula={id}").on("id" -> id).as(parser.*)
  }
}

