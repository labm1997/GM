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

case class Departamento(ID: Int, nome: String, sigla: String)

class departamentosDAO @Inject() (database: Database) {
  val parser : RowParser[Departamento] = Macro.namedParser[Departamento]
  def getDepartamentos() = database.withConnection { implicit connection => 
    SQL("SELECT ID, nome, sigla FROM tbl_departamentos").as(parser.*)
  }
  def getNomeDepartamentoSQL(id: Int) = database.withConnection { implicit connection => 
    SQL("SELECT ID,nome,sigla FROM tbl_departamentos WHERE ID={id} LIMIT 1").on("id" -> id).as(parser.*)
  }
  def getNomeDepartamento(id: Int): Option[String] = {
    val result = getNomeDepartamentoSQL(id)
    if(result.length == 1) return Some(result(0).nome)
    else None
  }
}

