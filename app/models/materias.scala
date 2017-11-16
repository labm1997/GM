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

case class Materias(ID: Int, nome: String, creditos: Int, ementa: String, departamento: Option[String])
case class PreRequisito(ID: Option[Int], nome: Option[String], N_GRUPO: Option[Int])


class materiasDAO @Inject() (database: Database) {
  val parser : RowParser[Materias] = Macro.namedParser[Materias]
  val parserPR : RowParser[PreRequisito] = Macro.namedParser[PreRequisito]
  def getMateriasDepartamento(iddepartamento: Int) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_materias.ID, tbl_materias.nome, ementa, creditos, tbl_departamentos.nome as departamento FROM tbl_materias LEFT JOIN tbl_departamentos ON ID_DEPARTAMENTO=tbl_departamentos.ID WHERE ID_DEPARTAMENTO={iddepartamento}").on("iddepartamento" -> iddepartamento).as(parser.*)
  }
  def getPrerequisitos(id: Int) = database.withConnection { implicit connection =>
    SQL("select tbl_materia_prerequisitos.ID_PREREQUISITO as ID,tbl_materias.nome as nome, tbl_materia_prerequisitos.N_GRUPO AS N_GRUPO from tbl_materia_prerequisitos LEFT JOIN tbl_materias ON tbl_materia_prerequisitos.ID_PREREQUISITO=tbl_materias.ID where tbl_materia_prerequisitos.ID_MATERIA={idmateria};").on("idmateria"->id).as(parserPR.*)
  }
  def getMateria(id: Int): List[Materias] = database.withConnection { implicit connection =>
    SQL("select tbl_materias.ID,tbl_materias.nome as nome,ementa,creditos,tbl_departamentos.nome as departamento from tbl_materias left join tbl_departamentos ON tbl_departamentos.ID=tbl_materias.ID_DEPARTAMENTO WHERE tbl_materias.ID={idmateria}").on("idmateria"->id).as(parser.*)
  }
}

