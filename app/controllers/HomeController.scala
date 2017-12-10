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


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(dadospessoaisdao: dadosPessoaisDAO, cc: ControllerComponents) 
  extends AbstractController(cc) with I18nSupport {
  
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  def home() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => Ok(views.html.home(dadospessoaisdao.getData(user)(0))) }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }

  
  val materiasGET = Form(
    mapping(
      "id"  -> number
    )(materiasGETVO.apply)(materiasGETVO.unapply)    
  )

case class materiasGETVO(id: Int)
}
