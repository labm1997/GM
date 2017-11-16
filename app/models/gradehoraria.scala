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

case class GradeHoraria(professor: Option[String], materia: Option[String], letra: Option[String], dia: Int, inicio: Int, fim: Option[Int], ID: Option[Int], status: String)

class gradeDAO @Inject() (database: Database) {
  val parser : RowParser[GradeHoraria] = Macro.namedParser[GradeHoraria]
  def getGrade(user: String) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_turmas_matricula.status as status, tbl_users.nome as professor, tbl_materias.ID as ID, tbl_materias.nome as materia,tbl_turmas.letra,dia,CONVERT(TIME_FORMAT(inicio,'%H'),INT) as inicio,CONVERT(TIME_FORMAT(fim,'%H'),INT) as fim FROM tbl_turmas_matricula INNER JOIN tbl_turmas ON tbl_turmas.ID=tbl_turmas_matricula.ID_TURMA INNER JOIN tbl_turmas_horarios ON tbl_turmas_horarios.ID_TURMA=tbl_turmas.ID INNER JOIN tbl_materias ON tbl_materias.ID=tbl_turmas.ID_MATERIA INNER JOIN tbl_users ON tbl_users.matricula=tbl_turmas.professor WHERE tbl_turmas_matricula.MATRICULA={user}").on("user" -> user).as(parser.*)
  }
}

