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

/**
 * Definir a inserçao de um novo
 * aluno no banco de dados
 *
 */

case class AlunoPost(matricula: String, nome: String, senha:String, semestre_inicio: String)
case class Aluno(matricula: String, nome: String, semestre_inicio: Option[String])


class Alunodao @Inject() (database: Database){
  val parserAluno : RowParser[Aluno] = Macro.namedParser[Aluno]

  def salvar(aluno: AlunoPost, curso: Int) = database.withConnection { implicit connection =>
    val id: Option[Long] = 
      SQL("INSERT INTO tbl_users(matricula, type, nome, password, curso, semestre_inicio) values ({matricula}, 'aluno', {nome}, {senha}, {curso}, {semestre_inicio})")
      .on(
      "curso" -> curso,
      "matricula" -> aluno.matricula,
	    "nome" -> aluno.nome,
	    "semestre_inicio" -> aluno.semestre_inicio,
	    "senha" -> aluno.senha).executeInsert()
  }
  
  def mostrarAlunosCoordenador(matriculaCoordenador: String) = database.withConnection { implicit connection =>
    SQL("SELECT matricula,nome,semestre_inicio FROM tbl_users WHERE curso=(SELECT curso FROM tbl_users WHERE matricula={matricula} LIMIT 1) AND type='aluno'").on("matricula" -> matriculaCoordenador).as(parserAluno.*)
  }
}
