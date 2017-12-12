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

@singleton
class HomeController @Inject()(dao: turmasDAO, cc: ControllerComponents) 
  extends AbstractController(cc) with I18nSupport {
	
	def mostrarTrancamento() = Action { implicit request: Request[AnyContent] =>
		request.session.get("connected").map { user =>
		Ok(views.html.trancamento(dao.getTrancamentoMateria(user)))

		}.getOrElse{
		Unauthorized("Iiiiiish")
		}
	}

  def trancaMateria = Action { implicit request =>
    request.session.get("connected").map { user =>
      trancaMateriaForm.bindFromRequest.fold(
        formWithErrors => {
	        //BadRequest(views.html.caracteristicas(formWithErrors))
          BadRequest("Formulário com erros")	
        },
	      trancaMateria => {
               dao.trancarTurma(trancaMateria.ID_TURMA,user)
           Ok("Calma, está tudo bem agora!")
        }
    }
  }  
  
  def trancaGeral = Action { implicit request =>
    request.session.get("connected").map { user =>
      trancamentoGeralForm.bindFromRequest.fold(
        formWithErrors => {
	        //BadRequest(views.html.caracteristicas(formWithErrors))
          BadRequest("Formulário com erros")	
        },
	      trancaGeral => {
               dao.trancarGeral(user)
           Ok("Calma, está tudo bem agora!")
        }
    }
  }   

  val trancaMateriaForm = Form(
    mapping(
      "ID_TURMA"  -> number
    )(materiasGETVO.apply)(materiasGETVO.unapply)    
  )
  val trancamentoGeralForm = Form(
    mapping(
    )(trancamentoGeralVO.apply)(trancamentoGeralVO.unapply)    
  )

case class materiasGETVO(ID_TURMA: Int)
case class trancamentoGeralVO()
}
