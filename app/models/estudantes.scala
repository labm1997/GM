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

case class Aluno(matricula: String, nome: String, senha:String)


class Alunodao @Inject() (database: Database){
/*  val parser : RowParser[models.estudantes] = Macro.namedParser[models.estudantes]*/

  def salvar(aluno: Aluno) = database.withConnection { implicit connection =>
    val id: Option[Long] = 
      SQL("INSERT INTO tbl_users(matricula, nome, password) values ({matricula}, {nome}, {senha})")
      .on(
      "matricula" -> aluno.matricula,
	    "nome" -> aluno.nome,
	    "senha" -> aluno.senha).executeInsert()
  }
}
