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
 * Definir a inserçao das caracteristicas
 * de um aluno no banco de dados
 *
 */

case class Matriculinha(user : String)
case class Caracteristicas(user : String, cpf: String, nomepai: String, nomemae: String, nacionalidade: String,rg: String, sexo: String, data_nascimento: String, nivel: String, pne: String, endereco: String, uf: String, cidade: String, cep: String, email: String, telefone: String, celular: String, racacor: String)

class Caracteristicasdao @Inject() (database: Database){
  val parser : RowParser[Caracteristicas] = Macro.namedParser[Caracteristicas]
  def getMatricula(user: String) = database.withConnection { implicit connection =>
    SQL("SELECT matricula as user FROM tbl_users WHERE matricula={matricula} LIMIT 1")
    .on("matricula" -> user).as(parser.*)
  }
  
  /* Vulnerabilidade de SQL Injection, divirta-se */
  
  def salvarCar(caracteristicas: Caracteristicas) = database.withConnection { implicit connection =>
    val id: Option[Long] = SQL(
      "UPDATE tbl_users SET cpf = '"+caracteristicas.cpf+"', nomepai= '"+caracteristicas.nomepai+"', nomemae = '"+caracteristicas.nomemae+"', nacionalidade = '"+caracteristicas.nacionalidade+"', rg = '"+caracteristicas.rg+"', sexo = '"+caracteristicas.sexo+"', data_nascimento = '"+caracteristicas.data_nascimento+"', nivel = '"+caracteristicas.nivel+"', pne = '"+caracteristicas.pne+"', endereco = '"+caracteristicas.endereco+"', uf = '"+caracteristicas.uf+"', cidade = '"+caracteristicas.cidade+"', cep = '"+caracteristicas.cep+"', email = '"+caracteristicas.email+"', telefone = '"+caracteristicas.telefone+"', celular = '"+caracteristicas.celular+"', racacor = '"+caracteristicas.racacor+"' WHERE matricula={matricula}")
      .on("matricula" -> caracteristicas.user).executeInsert()
 }
}
