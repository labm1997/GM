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
class LoginController @Inject()(logindao: LoginDAO, dadospessoaisdao: dadosPessoaisDAO, cc: ControllerComponents) extends AbstractController(cc) {

  def md5Hash(text: String) : String = java.security.MessageDigest.getInstance("MD5").digest(text.getBytes()).map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
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
        if(logindao.verificarCredenciais(login.user, md5Hash(login.password)).length == 1){
          val info = dadospessoaisdao.getData(login.user)
          if(info.length == 1)
            Ok(views.html.home(info(0))).withSession("connected" -> login.user)
          else Unauthorized("Deu ruim")
	      }
        else Unauthorized("NÃO DEU NÃO")
      }
    )
    
  }
  def sessionEnd() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login()).withNewSession
  }

val loginForm = Form(
    mapping(
      "user"  -> nonEmptyText,
      "password" -> nonEmptyText,
    )(loginVO.apply)(loginVO.unapply)    
  )
case class loginVO(user: String, password: String)
}
