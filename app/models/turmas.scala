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
import java.sql.Timestamp
import scala.collection.mutable.ListBuffer

case class HorarioData(dia: Int, inicio: String, fim: String)
case class Horario(dia: String, inicio: String, fim: String)
case class Turma(ID: Int, letra: String, professor: String, vagastotal: Int, vagasrestantes: Int, horarios: List[Horario])
case class TurmaData(ID: Int, letra: String, professor: String, vagas: Int)
case class Vagas(vagasocupadas: Int)
case class Result(ID: Int, nome: String, letra: String, status: String)

class turmasDAO @Inject() (database: Database) {
  val parserH : RowParser[HorarioData] = Macro.namedParser[HorarioData]
  val parser : RowParser[TurmaData] = Macro.namedParser[TurmaData]
  val parserV : RowParser[Vagas] = Macro.namedParser[Vagas]
  val parserR : RowParser[Result] = Macro.namedParser[Result]
  val diasDaSemana = new ListBuffer[String]
  diasDaSemana += ("Domingo","Segunda","Terça","Quarta","Quinta","Sexta","Sábado")
  def getTurmas(idmateria: Int): List[Turma] = database.withConnection { implicit connection => 
    var turmasList = getTurmasData(idmateria)
    return turmasList.map(turma =>
      new Turma(turma.ID, turma.letra, turma.professor, turma.vagas, (turma.vagas-getVagasOcupadas(turma.ID)(0).vagasocupadas), 
        getHorarios(turma.ID).map(
          horario => new Horario(
            diasDaSemana(horario.dia.toInt-1), 
            horario.inicio.split(" ")(1).split(":")(0)+"h", 
            horario.fim.split(" ")(1).split(":")(0)+"h"
          )
        )
      )
    )
  }
  def getTurmasData(idmateria: Int) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_turmas.ID, tbl_turmas.letra, tbl_turmas.vagas, tbl_users.nome as professor FROM tbl_turmas LEFT JOIN tbl_users ON tbl_users.matricula=tbl_turmas.professor WHERE tbl_turmas.ID_MATERIA={idmateria}").on("idmateria" -> idmateria).as(parser.*)
  }
  def getHorarios(idturma: Int) = database.withConnection { implicit connection =>
    SQL("SELECT tbl_turmas_horarios.dia,CONVERT(tbl_turmas_horarios.inicio, char(16)) as inicio,CONVERT(tbl_turmas_horarios.fim, char(16)) as fim FROM tbl_turmas_horarios WHERE tbl_turmas_horarios.ID_TURMA={idturma}").on("idturma" -> idturma).as(parserH.*)
  }
  def getVagasOcupadas(idturma: Int) = database.withConnection { implicit connection =>
    SQL("SELECT COUNT(ID) as vagasocupadas FROM tbl_turmas_matricula WHERE ID_TURMA={idturma}").on("idturma" -> idturma).as(parserV.*)
  }
  def addMatricula(idturma: Int, user: String): Option[Long] = database.withConnection { implicit connection =>
    /* Verifica se há vagas */
    val vagasocupadas = getVagasOcupadas(idturma)(0).vagasocupadas
    val turma = getTurmasData(idturma)(0)
    
    /* É preciso verificar se o aluno tem os pré-requisitos TODO */
    
    if(turma.vagas-vagasocupadas > 0){
      val id: Option[Long] = SQL("insert into tbl_turmas_matricula values (0,{matricula},{idturma},'aguardando',1,'OBR')").on("idturma" -> idturma, "matricula" -> user).executeInsert()
      return id
    }
    else return None
  }
  def getResult(user: String) = database.withConnection { implicit connection =>
    SQL("select tbl_materias.ID, tbl_turmas_matricula.status, tbl_turmas.letra, tbl_materias.nome from tbl_turmas_matricula INNER JOIN tbl_turmas ON tbl_turmas.ID=tbl_turmas_matricula.ID_TURMA INNER JOIN tbl_materias ON tbl_materias.ID=tbl_turmas.ID_MATERIA where MATRICULA={matricula}").on("matricula" -> user).as(parserR.*)
  }
}

