package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import java.security.MessageDigest
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

/* This controller processes the Login action */

@Singleton
class matriculasController @Inject()(turmasDAO : turmasDAO, materiasdao : materiasDAO, cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {


  def matricula() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => 
      Ok(views.html.matricula(turmasDAO.getSolicitacoes(user)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }

  def matricular = Action { implicit request =>
    request.session.get("connected").map { user => 
      matriculaForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest("Há um erro")
        },
        requisicao => {
          val turma = turmasDAO.getTurmaData(requisicao.id)
          if(turma.length == 1){
            val r = turmasDAO.addMatricula(turma(0), user, materiasdao.getPrerequisitos(turma(0).ID_MATERIA))
            r match {
              case Some(a) => Ok("Solicitação efetuada");
              case None => BadRequest("Erro ao inserir")
            }
          }
          else Unauthorized("Turma inválida")
        }
      )
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
  
  def retirarMatricula(idturma: Int) = Action { implicit request =>
    request.session.get("connected").map { user => 
      turmasDAO.retirarMatricula(user, idturma)
      Ok(views.html.matricula(turmasDAO.getSolicitacoes(user)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }

  def matriculaAtualizar = Action { implicit request =>
    request.session.get("connected").map { user => 
      matriculaUpdateForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest("Há um erro")
        },
        requisicao => {
          turmasDAO.atualizarMatricula(user,requisicao.idturma,requisicao.prioridade)
          Ok("Solicitação efetuada")
        }
      )
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
      
  val matriculaForm = Form(
    mapping(
      "id" -> number
    )(matriculaVO.apply)(matriculaVO.unapply)
  )

  val matriculaUpdateForm = Form(
    mapping(
      "idturma" -> number,
      "prioridade" -> number
    )(matriculaUpdateVO.apply)(matriculaUpdateVO.unapply)
  )
}
case class matriculaVO(id: Int)
case class matriculaUpdateVO(idturma: Int, prioridade: Int)
