package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
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
    var user = request.body.asFormUrlEncoded.get("login")(0)
    Ok(views.html.home(user)).withSession("connected" -> user)
  }
  def sessionEnd() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login()).withNewSession
  }
  def home() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => Ok(views.html.home(user)) }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
}
