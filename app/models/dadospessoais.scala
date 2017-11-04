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

case class dadosPessoais(nome: String, nomepai: String, nomemae: String, nacionalidade: String, rg: String, sexo: String, matricula: String, cpf: String, semestreinicio: String, curso: String, typed: String, datanascimento: String, nivel: String, bloqueado: String, pne: String, endereco: String, uf: String, cidade: String, cep: String, email: String, telefone: String, celular: String, racacor: String)

class dadosPessoaisDAO @Inject() (database: Database) {
  val parser : RowParser[dadosPessoais] = Macro.namedParser[dadosPessoais]
  def getData(matricula: String) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_users.nome,nomepai,nomemae,nacionalidade,rg,sexo,matricula,cpf,semestre_inicio as semestreinicio, tbl_cursos.nome as curso,type as typed, data_nascimento as datanascimento, nivel, bloqueado, pne, endereco, uf, cidade, cep, email, telefone, celular, racacor FROM tbl_users INNER JOIN tbl_cursos ON curso=ID WHERE matricula={matricula} LIMIT 1").on("matricula" -> matricula).as(parser.*)
  }
}

