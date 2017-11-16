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

case class cursos(ID: Option[Int], nome: Option[String], modalidade: Option[String], turno: Option[String], duracao: Option[String], habilitacao: Option[Int], hnome: Option[String], nivel: Option[String], reconhecido_mec: Option[String], vigente: Option[String], creditos_exigidos: Option[Int], creditos_por_periodo_max: Option[Int], creditos_por_periodo_min: Option[Int], limite_permanencia_max: Option[Int], limite_permanencia_min: Option[Int])

case class Fluxo(tipo: Option[String], sigla: Option[String], ID_MATERIA: Option[Int], nome: Option[String], semestre: Option[Int], creditos: Option[Int])

class cursosDAO @Inject() (database: Database) {
  val parser : RowParser[cursos] = Macro.namedParser[cursos]
  val parserFluxo : RowParser[Fluxo] = Macro.namedParser[Fluxo]
  def getCursos() = database.withConnection { implicit connection => 
    SQL("SELECT * FROM tbl_cursos GROUP BY ID ORDER BY nome").as(parser.*)
  }
  def getCursoData(id: Int) = database.withConnection { implicit connection => 
    SQL("SELECT * FROM tbl_cursos WHERE ID={id}").on("id" -> id).as(parser.*)
  }
  def getHabilitacoes(idCurso: Int) = database.withConnection { implicit connection => 
    SQL("SELECT * FROM tbl_cursos WHERE ID={id}").on("id" -> idCurso).as(parser.*)
  }
  def getCursoHabilitacaoData(idHabilitacao: Int) = database.withConnection { implicit connection => 
    SQL("SELECT * FROM tbl_cursos WHERE habilitacao={id}").on("id" -> idHabilitacao).as(parser.*)
  }
  def getFluxo(idHab: Int) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_cursos_materias.type as tipo, tbl_cursos_materias.semestre as semestre, tbl_cursos_materias.ID_MATERIA as ID_MATERIA, tbl_materias.nome as nome, tbl_departamentos.sigla as sigla, tbl_materias.creditos as creditos FROM tbl_cursos_materias LEFT JOIN tbl_materias ON tbl_materias.ID=tbl_cursos_materias.ID_MATERIA LEFT JOIN tbl_departamentos ON tbl_materias.ID_DEPARTAMENTO=tbl_departamentos.ID WHERE tbl_cursos_materias.ID_CURSO={id}").on("id"->idHab).as(parserFluxo.*)
  }
}

