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

case class dadosPessoais(nome: Option[String], nomepai: Option[String], nomemae: Option[String], nacionalidade: Option[String], rg: Option[String], sexo: Option[String], matricula: Option[String], cpf: Option[String], semestreinicio: Option[String], curso: Option[String], typed: Option[String], datanascimento: Option[String], nivel: Option[String], bloqueado: Option[String], pne: Option[String], endereco: Option[String], uf: Option[String], cidade: Option[String], cep: Option[String], email: Option[String], telefone: Option[String], celular: Option[String], racacor: Option[String])

class dadosPessoaisDAO @Inject() (database: Database) {
  val parser : RowParser[dadosPessoais] = Macro.namedParser[dadosPessoais]
  def getData(matricula: String) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_users.nome,nomepai,nomemae,nacionalidade,rg,sexo,matricula,cpf,semestre_inicio as semestreinicio, tbl_cursos.nome as curso,type as typed, data_nascimento as datanascimento, nivel, bloqueado, pne, endereco, uf, cidade, cep, email, telefone, celular, racacor FROM tbl_users LEFT JOIN tbl_cursos ON curso=ID WHERE matricula={matricula} LIMIT 1").on("matricula" -> matricula).as(parser.*)
  }
}

