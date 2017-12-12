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
	
	def mostrarRetiraMateria() = Action { implicit request: Request[AnyContent] =>
		request.session.get("connected").map { user =>
		Ok(views.html.retira_matricula(dao.getRetirarMatricula(user)))

		}.getOrElse{
		Unauthorized("Iiiiiish")
		}
	}

  def retiraMatricula = Action { implicit request =>
    request.session.get("connected").map { user =>
      retiraMatriculaForm.bindFromRequest.fold(
        formWithErrors => {
	        //BadRequest(views.html.caracteristicas(formWithErrors))
          BadRequest("Formulário com erros")	
        },
	      trancaMatermatriculaia => {
               dao.retiraMatricula(user,retiraMatricula.ID_TURMA)
           Ok("Calma, está tudo bem agora!")
        }
    }
  }  
  
  val retiraMatriculaForm = Form(
    mapping(
      "ID_TURMA"  -> number
    )(materiasGETVO.apply)(materiasGETVO.unapply)    
  )

case class materiasGETVO(ID_TURMA: Int)
}
