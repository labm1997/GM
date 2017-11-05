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

case class Materias(ID: Int, nome: String, creditos: Int, ementa: String, departamento: String)

class materiasDAO @Inject() (database: Database) {
  val parser : RowParser[Materias] = Macro.namedParser[Materias]
  def getMateriasDepartamento(iddepartamento: Int) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_materias.ID, tbl_materias.nome, ementa, creditos, tbl_departamentos.nome as departamento FROM tbl_materias LEFT JOIN tbl_departamentos ON ID_DEPARTAMENTO=tbl_departamentos.ID WHERE ID_DEPARTAMENTO={iddepartamento}").on("iddepartamento" -> iddepartamento).as(parser.*)
  }
}

