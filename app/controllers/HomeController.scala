package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import java.security.MessageDigest

/* Vinicius Conseguiu editar */
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(
  logindao: LoginDAO, 
  dadospessoaisdao: dadosPessoaisDAO, 
  cursosdao: cursosDAO,
  departamentosdao: departamentosDAO,
  cc: ControllerComponents) 
  extends AbstractController(cc) {
  
  def md5Hash(text: String) : String = java.security.MessageDigest.getInstance("MD5").digest(text.getBytes()).map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  def login() = Action { implicit request: Request[AnyContent] =>
    /* Verifica se já há sessão */
    request.session.get("connected").map { user => Redirect("home") }.getOrElse {
      Ok(views.html.login())
    }
  }
  def sessionStart() = Action { implicit request: Request[AnyContent] =>
    loginForm.bindFromRequest.fold(
      formWithErros => {
        BadRequest("Há algum erro no formulário")
      },
      login => {
        if(logindao.verificarCredenciais(login.user, md5Hash(login.password)).length == 1)
        Ok(views.html.home(login.user)).withSession("connected" -> login.user)
        else Unauthorized("NÃO DEU NÃO")
      }
    )
    
  }
  def sessionEnd() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login()).withNewSession
  }
  def home() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => Ok(views.html.home(user)) }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
  def dadosPessoais() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => 
      Ok(views.html.dadospessoais(dadospessoaisdao.getData(user)(0)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
  def gradeHoraria() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.gradehoraria())
  }
  def historicoEscolar() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.historicoescolar())
  }
  def cursos() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.cursos(cursosdao.getCursos()))
  }
  def oferta() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.ofertadepartamento(departamentosdao.getDepartamentos()))
  }
  def resultado() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.resultado())
  }
  
  val loginForm = Form(
    mapping(
      "user"  -> nonEmptyText,
      "password" -> nonEmptyText,
    )(loginVO.apply)(loginVO.unapply)    
  )
}

case class loginVO(user: String, password: String)
