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
case class TurmaData(ID: Int, letra: String, professor: String, vagas: Int, ID_MATERIA: Int)
case class Vagas(vagasocupadas: Int)
case class Result(ID: Int, ID_TURMA: Int, nome: String, letra: String, status: String, prioridade: Int, vagas: Int)
case class Conquista(ID_PREREQUISITO: Int)
case class counter(n: Int)
case class Matricula(ID_MATERIA: Int, ID_TURMA: Int, nome: String, letra: String, posicao: Int, prioridade: Int, vagas: Int)
case class orderList(matricula: String, tipo: Int)

case class trancamentoMateria(ID: Int, letra: String, nome: String ,ID_MATERIA: Int, prof: String)
case class retiraMateria(ID: int, nome: String, ID_MATERIA: Int)
case class coordenadorRetiraMateria(ID: Int, letra: String, nome: String ,ID_MATERIA: Int, prof: String)
  

class turmasDAO @Inject() (database: Database) {
  val parserH : RowParser[HorarioData] = Macro.namedParser[HorarioData]
  val parser : RowParser[TurmaData] = Macro.namedParser[TurmaData]
  val parserV : RowParser[Vagas] = Macro.namedParser[Vagas]
  val parserR : RowParser[Result] = Macro.namedParser[Result]
  val parserConquista : RowParser[Conquista] = Macro.namedParser[Conquista]
  val parsercounter : RowParser[counter] = Macro.namedParser[counter]
  val parserorderList : RowParser[orderList] = Macro.namedParser[orderList]
  val parserPR : RowParser[PreRequisito] = Macro.namedParser[PreRequisito]
 
  val parserTrancamentoMateria : RowParser[trancamentoMateria] = Macro.namedParser[trancamentoMateria]
  val parserRetiraMatricula : RowParser[retiraMatricula] = Macro.namedParser[retiraMatricula]
  val parserCoordenadorRetiraMateria : RowParser[retiraMateria] = Macro.namedParser[retiraMateria]

  val diasDaSemana = new ListBuffer[String]
  diasDaSemana += ("Domingo","Segunda","Terça","Quarta","Quinta","Sexta","Sábado")
  
  def getCoordenadorRetiraMateria(user: String) = database.withConnection { implicit connection =>
    SQL("SELECT tbl_turmas.ID AS ID, tbl_turmas.ID_MATERIA AS ID_MATERIA, tbl_turmas.letra AS letra, tbl_materias.nome AS nome, tbl_turmas.professor AS prof FROM tbl_turmas_matricula LEFT JOIN tbl_materias ON tbl_materias.ID = tbl_turmas.ID_MATERIA LEFT JOIN tbl_turmas on tbl_turmas_matricula.ID_TURMA = tbl_turmas.ID WHERE tbl_turmas_matricula.Matricula = user = {user}").on("user" -> user).as(parserCoordenadorRetiraMateria.*)
  }

  def coordenadorRetiraMateria(user: String) = database.withConnection { implicit connection =>
    SQL("DELETE tbl_turmas_horarios, tbl_turmas_matricula FROM  WHERE ").on("user" -> user).as(parserCoordenadorRetiraMateria.*)
  }

  def getRetirarMatricula(user: String) = database.withConnection { implicit connection =>
    SQL("SELECT tbl_turmas.ID AS ID, tbl_turmas.ID_MATERIA AS ID_MATERIA, tbl_materias.nome AS nome FROM tbl_turmas_matricula LEFT JOIN tbl_materias ON tbl_materias.ID = tbl_turmas.ID_MATERIA LEFT JOIN tbl_turmas on tbl_turmas_matricula.ID_TURMA = tbl_turmas.ID WHERE tbl_turmas_matricula.Matricula = user = {user} WHERE status = 'matriculado'").on("user" -> user).as(parserRetiraMatricula.*)
  }

  def retiraMatricula(user: String) = database.withConnection { implicit connection => 
    SQL("UPDATE tbl_turmas_matricula SET status = 'retirado' WHERE tbl_turmas_matricula.MATRICULA = '"+user+"'").on("user" -> user).as(parserRetiraMatricula.*)
  }

  def getTrancamentoMateria(user: String) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_turmas.ID AS ID, tbl_turmas.ID_MATERIA AS ID_MATERIA, tbl_turmas.letra AS letra, tbl_materias.nome AS nome, tbl_turmas.professor AS prof FROM tbl_turmas_matricula LEFT JOIN tbl_materias ON tbl_materias.ID = tbl_turmas.ID_MATERIA LEFT JOIN tbl_turmas on tbl_turmas_matricula.ID_TURMA = tbl_turmas.ID WHERE tbl_turmas_matricula.Matricula = user = {user} WHERE status = 'matriculado'").on("user" -> user).as(parserTrancamentoMateria.*)
  }

  def trancarTurma(user: String) = database.withConnection { implicit connection => 
    SQL("UPDATE tbl_users, tbl_historico, tbl_turmas_matricula SET trancamento = 1, mençao = 6, status = 'trancado' WHERE tbl_turmas_matricula.MATRICULA = '"+user+"'").on("user" -> user).as(parserTrancamentoMateria.*)
  }
  
   /* Obtém as turmas de uma matéria dado ID da matéria */
  def getTrancamentoGeral(user: String) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_turmas.ID AS ID, tbl_turmas.ID_MATERIA AS ID_MATERIA, tbl_turmas.letra AS letra tbl_materias.nome AS nome, tbl_turmas.prof AS tbl_turmas_matricula LEFT JOIN tbl_turmas ON tbl_turmas.ID = tbl_turmas_matricula.ID_TURMA LEFT JOIN tbl_materias ON tbl_materias.ID = tbl_turmas.ID_MATERIA WHERE status = 'matriculado' AND MATRICULA = {matricula}").on("user" -> user).as(parserTrancamentoMateria.*)
  }

  /* Obtém as turmas de uma matéria dado ID da matéria */
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
  
  /* Busca por turmas dado ID da matéria */
  def getTurmasData(idmateria: Int) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_turmas.ID, tbl_turmas.letra, tbl_turmas.vagas, tbl_users.nome as professor, tbl_turmas.ID_MATERIA as ID_MATERIA FROM tbl_turmas LEFT JOIN tbl_users ON tbl_users.matricula=tbl_turmas.professor WHERE tbl_turmas.ID_MATERIA={idmateria}").on("idmateria" -> idmateria).as(parser.*)
  }
  
  /* Busca por dados de uma turma em particular dado ID da turma */
  def getTurmaData(idturma: Int) = database.withConnection { implicit connection => 
    SQL("SELECT tbl_turmas.ID, tbl_turmas.letra, tbl_turmas.vagas, tbl_users.nome as professor, tbl_turmas.ID_MATERIA as ID_MATERIA FROM tbl_turmas LEFT JOIN tbl_users ON tbl_users.matricula=tbl_turmas.professor WHERE tbl_turmas.ID={idturma}").on("idturma" -> idturma).as(parser.*)
  }
  
  /* Obtém os horários (bruto) de uma turma dado ID da turma */
  def getHorarios(idturma: Int) = database.withConnection { implicit connection =>
    SQL("SELECT tbl_turmas_horarios.dia,CONVERT(tbl_turmas_horarios.inicio, char(16)) as inicio,CONVERT(tbl_turmas_horarios.fim, char(16)) as fim FROM tbl_turmas_horarios WHERE tbl_turmas_horarios.ID_TURMA={idturma}").on("idturma" -> idturma).as(parserH.*)
  }
  
  /* Retorna o número de vagas ocupadas em uma turma dado ID */
  def getVagasOcupadas(idturma: Int) = database.withConnection { implicit connection =>
    SQL("SELECT COUNT(ID) as vagasocupadas FROM tbl_turmas_matricula WHERE ID_TURMA={idturma} AND status='matriculado'").on("idturma" -> idturma).as(parserV.*)
  }
  
  def getPreRequisitosObtidos(idmateria: Int, user: String) = database.withConnection { implicit connection =>
    SQL("SELECT tbl_historico.ID_MATERIA AS ID, tbl_materias.nome AS nome, tbl_materia_prerequisitos.N_GRUPO FROM tbl_historico INNER JOIN tbl_materias ON tbl_materias.ID=tbl_historico.ID_MATERIA INNER JOIN tbl_materia_prerequisitos ON tbl_materia_prerequisitos.ID_PREREQUISITO=tbl_historico.ID_MATERIA AND tbl_materia_prerequisitos.ID_MATERIA={materia} WHERE matricula={user} AND tbl_historico.ID_MATERIA IN (SELECT ID_PREREQUISITO FROM tbl_materia_prerequisitos WHERE tbl_materia_prerequisitos.ID_MATERIA={materia}) AND tbl_historico.mençao IN (5,4,3,-1)").on("user" -> user, "materia" -> idmateria).as(parserPR.*)
  }
  
  /*def getPreRequisitosConquistados(idmateria: Int, user: String) = database.withConnection { implicit connection =>
    SQL("select DISTINCT ID_PREREQUISITO from tbl_materia_prerequisitos inner join tbl_historico on tbl_materia_prerequisitos.ID_PREREQUISITO=tbl_historico.ID_MATERIA where tbl_materia_prerequisitos.ID_MATERIA={idmateria} AND tbl_historico.matricula={user} AND (tbl_historico.mençao = 5 OR tbl_historico.mençao = 4 OR tbl_historico.mençao = 3 OR tbl_historico.mençao = -1)").on("idmateria" -> idmateria, "user" -> user).as(parserConquista.*)
  }*/
  
  /*def getNPreRequisitos(idmateria: Int) = database.withConnection { implicit connection =>
    SQL("select COUNT(ID) as n from tbl_materia_prerequisitos where tbl_materia_prerequisitos.ID_MATERIA={idmateria}").on("idmateria" -> idmateria).as(parsercounter.*)
  }*/
  
  /* Retorna o número de solicitações de um usuário a uma turma */
  def getSolicitacoesN(idturma: Int, user: String) = database.withConnection { implicit connection =>
    SQL("select COUNT(ID) as n from tbl_turmas_matricula WHERE MATRICULA={user} AND ID_TURMA={idturma}").on("idturma" -> idturma, "user" -> user).as(parsercounter.*)
  }(0)
  
  def verificaPreRequisitos(preRequisitos: List[PreRequisito], historico: List[PreRequisito]): Boolean = {
    if(preRequisitos.length == 0) return true
    val historicoAgrupado = historico.groupBy(a=>a.N_GRUPO).toList
    for(grupoPrequisitos <- preRequisitos.groupBy(a=>a.N_GRUPO).toList){
      historicoAgrupado.find(_._1 == grupoPrequisitos._1) match {
        case Some(historicoTupla) => 
          if(historicoTupla._2 == grupoPrequisitos._2) return true
        case None => {}
      }
    }
    return false
  }
  
  /* Adiciona uma solicitação de matrícula */
  def addMatricula(turma: TurmaData, user: String, prerequisitos: List[PreRequisito]): Option[Long] = database.withConnection { implicit connection =>
  
    /* Verifica se há vagas */
    val vagasocupadas = getVagasOcupadas(turma.ID)(0).vagasocupadas
    
    /* Verifica se a turma existe e se já não solicitou antes a vaga*/    
    
    if(
      getSolicitacoesN(turma.ID, user).n == 0 /* Verifica se já não solicitou vaga */ &&
      verificaPreRequisitos(prerequisitos, getPreRequisitosObtidos(turma.ID_MATERIA, user)) /* Verifica se tem os pré-requisitos */
    ) {     
      
      if(turma.vagas-vagasocupadas > 0){
        val id: Option[Long] = SQL("insert into tbl_turmas_matricula values (0,{matricula},{idturma},'aguardando',1,'OBR')").on("idturma" -> turma.ID, "matricula" -> user).executeInsert()
        return id
      }
      else return None
    }
    else return None
  }
  
  /* Retorna o resultado */
  def getResult(user: String) = database.withConnection { implicit connection =>
    SQL("select tbl_materias.ID, tbl_turmas.ID as ID_TURMA, tbl_turmas_matricula.status, tbl_turmas.letra, tbl_materias.nome, tbl_turmas_matricula.prioridade as prioridade, tbl_turmas.vagas from tbl_turmas_matricula INNER JOIN tbl_turmas ON tbl_turmas.ID=tbl_turmas_matricula.ID_TURMA INNER JOIN tbl_materias ON tbl_materias.ID=tbl_turmas.ID_MATERIA where MATRICULA={matricula}").on("matricula" -> user).as(parserR.*)
  }
  
  /* Retorna o resultado */
  def getResultAguardando(user: String) = database.withConnection { implicit connection =>
    SQL("select tbl_materias.ID, tbl_turmas.ID as ID_TURMA, tbl_turmas_matricula.status, tbl_turmas.letra, tbl_materias.nome, tbl_turmas_matricula.prioridade as prioridade, tbl_turmas.vagas from tbl_turmas_matricula INNER JOIN tbl_turmas ON tbl_turmas.ID=tbl_turmas_matricula.ID_TURMA INNER JOIN tbl_materias ON tbl_materias.ID=tbl_turmas.ID_MATERIA where MATRICULA={matricula} AND status='aguardando'").on("matricula" -> user).as(parserR.*)
  }
  
  /* 
    Retorna a lista de classificação com matrícula e tipo de uma turma 
    (PROBLEMAS COM EMPATE) 
    !TODO Implementar desempate por posição no fluxo e por IRA
  */
  def getSolicitacoesOrder(idturma: Int) = database.withConnection { implicit connection =>
    SQL("SELECT tbl_cursos.nome,tbl_users.matricula, CASE (SELECT tbl_cursos_materias.type FROM tbl_cursos_materias WHERE tbl_cursos_materias.ID_MATERIA=tbl_turmas.ID_MATERIA AND tbl_cursos_materias.ID_CURSO=tbl_cursos.habilitacao) WHEN 'OBR' THEN 1 WHEN 'OPT' THEN 2 WHEN 'ML' THEN 3 ELSE 3 END AS tipo from tbl_turmas_matricula INNER JOIN tbl_turmas ON tbl_turmas.ID=tbl_turmas_matricula.ID_TURMA INNER JOIN tbl_materias ON tbl_materias.ID=tbl_turmas.ID_MATERIA INNER JOIN tbl_users ON tbl_users.matricula = tbl_turmas_matricula.MATRICULA LEFT JOIN tbl_cursos ON tbl_cursos.habilitacao=tbl_users.curso  WHERE tbl_turmas_matricula.ID_TURMA={idturma} AND tbl_turmas_matricula.status = 'aguardando' ORDER BY tipo,prioridade").on("idturma" -> idturma).as(parserorderList.*)
  }
  
  /*
    Retorna uma lista de Matricula que tem as matérias solicitadas e a posição parcial nas vagas
  */
  def getSolicitacoes(user: String): List[Matricula] = database.withConnection { implicit connection =>
    var turmasResult = getResultAguardando(user)
    return turmasResult.map(turma => {
      var posicao = getSolicitacoesOrder(turma.ID_TURMA).zipWithIndex.filter(i => i._1.matricula == user)
      new Matricula(turma.ID, turma.ID_TURMA, turma.nome, turma.letra, 
      {if(posicao.length == 1) posicao(0)._2+1 else 0}, 
      turma.prioridade, turma.vagas)
      }
    )
  }
  
  /* Retira uma matrícula */
  def retirarMatricula(user: String, idturma: Int) = database.withConnection { implicit connection =>
    SQL("DELETE FROM tbl_turmas_matricula WHERE MATRICULA={user} AND ID_TURMA={idturma}").on("user" -> user, "idturma" -> idturma).executeInsert()
  }
  
  /* Atualiza uma matrícula */
  def atualizarMatricula(user: String, idturma: Int, prioridade: Int) = database.withConnection { implicit connection =>
    SQL("UPDATE tbl_turmas_matricula SET prioridade={prioridade} WHERE MATRICULA={user} AND ID_TURMA={idturma}").on("user" -> user, "idturma" -> idturma, "prioridade" -> prioridade).executeInsert()
  }
  
}

