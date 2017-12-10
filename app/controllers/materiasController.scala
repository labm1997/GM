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
class materiasController @Inject()(materiasdao: materiasDAO, turmasDAO: turmasDAO, departamentosdao: departamentosDAO, cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def oferta() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.ofertadepartamento(departamentosdao.getDepartamentos()))
  }
  def materiasdepartamento(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.materiasdepartamento(materiasdao.getMateriasDepartamento(id)))
  }
  def turmas(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.turmas(turmasDAO.getTurmas(id), id, departamentosdao.getNomeDepartamento(id)))
  }
  def resultado() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => 
      Ok(views.html.resultado(turmasDAO.getResult(user)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
    
  }
  def listaEspera() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.lista_de_espera())
  }
  def materia(id: Int) = Action { implicit request: Request[AnyContent] =>
    var materias = materiasdao.getMateria(id)
    var preRequisitos = materiasdao.getPrerequisitos(id).groupBy(_.N_GRUPO).toList
    if(materias.length == 1){
      Ok(views.html.materia(materias(0), id, preRequisitos))
    }
    else Unauthorized("Iiiiish")
  }
}
