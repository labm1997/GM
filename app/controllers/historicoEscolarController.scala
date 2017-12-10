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

/* Esse controlador acessa o historico escolar do aluno que esta logado */

@Singleton
class historicoEscolarController @Inject()(historicodao : historicoDAO, cc: ControllerComponents) 
  extends AbstractController(cc) with I18nSupport {

  def historicoEscolar() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => 
      var historico = historicodao.getHistorico(user).groupBy(a=>a.semestre).toList sortBy (_._1)
      Ok(views.html.historicoescolar(historico))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
}
