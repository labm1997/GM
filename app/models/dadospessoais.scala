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

case class dadosPessoais(nome: Option[String], nomepai: Option[String], nomemae: Option[String], nacionalidade: Option[String], rg: Option[String], sexo: Option[String], matricula: Option[String], cpf: Option[String], semestreinicio: Option[String], curso: Option[String], cursoID: Option[Int], typed: String, datanascimento: Option[String], nivel: Option[String], bloqueado: Option[String], pne: Option[String], endereco: Option[String], uf: Option[String], cidade: Option[String], cep: Option[String], email: Option[String], telefone: Option[String], celular: Option[String], racacor: Option[String])

class dadosPessoaisDAO @Inject() (database: Database) {
  val parser : RowParser[dadosPessoais] = Macro.namedParser[dadosPessoais]
  def getData(matricula: String) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_users.nome,tbl_users.nomepai,tbl_users.nomemae,tbl_users.nacionalidade,tbl_users.rg,tbl_users.sexo,tbl_users.matricula,tbl_users.cpf,semestre_inicio as semestreinicio, tbl_cursos.nome as curso,tbl_users.curso as cursoID, tbl_users.type as typed, tbl_users.data_nascimento as datanascimento, tbl_users.nivel, tbl_users.bloqueado, tbl_users.pne, tbl_users.endereco, tbl_users.uf, tbl_users.cidade, tbl_users.cep, tbl_users.email, tbl_users.telefone, tbl_users.celular, tbl_users.racacor FROM tbl_users LEFT JOIN tbl_cursos ON curso=ID WHERE matricula={matricula} LIMIT 1").on("matricula" -> matricula).as(parser.*)
  }
}

