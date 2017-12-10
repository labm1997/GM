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
class cursosController @Inject()(cursosdao: cursosDAO, cc: ControllerComponents) 
  extends AbstractController(cc) with I18nSupport {

  def cursos() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.cursos(cursosdao.getCursos()))
  }
  def habilitacoes(idcurso: Int) = Action { implicit request: Request[AnyContent] =>
    var habilitacoes = cursosdao.getHabilitacoes(idcurso)
    Ok(views.html.habilitacoes(habilitacoes))
  }
  def curso(idHabilitacao: Int) = Action { implicit request: Request[AnyContent] =>
    var cursoData = cursosdao.getCursoHabilitacaoData(idHabilitacao)
    
    if(cursoData.length == 1){
      cursoData(0).habilitacao match {
        case Some(i) => {
          var fluxoData = cursosdao.getFluxo(i).groupBy(a=>a.semestre).toList sortBy (_._1)
          Ok(views.html.curso(cursoData(0), fluxoData))
        }
        case None => {
          BadRequest("Eita, não consegui desculpa")
        }
      }
    }
    else BadRequest("Eita, não consegui desculpa")
  }
}
