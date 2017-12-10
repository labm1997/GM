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
class gradeHorariaController @Inject()(gradeHorariadao : gradeDAO, cc: ControllerComponents) 
  extends AbstractController(cc) with I18nSupport {

  def gradeHoraria() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => 
      Ok(views.html.gradehoraria(gradeHorariadao.getGrade(user)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
}
