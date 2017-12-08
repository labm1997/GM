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
class caracteristicasController @Inject()(caracteristicasdao: Caracteristicasdao, cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def nova_caracteristica() = Action { implicit request: Request[AnyContent] =>
     Ok(views.html.caracteristicas(caractForm))
  }

    
  def SubmeterCar = Action { implicit request =>
    request.session.get("connected").map { user =>
      caractForm.bindFromRequest.fold(
        formWithErrors => {
	        //BadRequest(views.html.caracteristicas(formWithErrors))
          BadRequest("Formulário com erros")	
        },
	      caracteristica => {
	        //val nome = caracteristicasdao.getMatricula(user)
	        val novaCar = Caracteristicas(user, caracteristica.cpf, caracteristica.nomepai, caracteristica.nomemae, caracteristica.nacionalidade, caracteristica.rg, caracteristica.sexo, caracteristica.data_nascimento, caracteristica.nivel, caracteristica.pne, caracteristica.endereco, caracteristica.uf, caracteristica.cidade, caracteristica.cep, caracteristica.email, caracteristica.telefone, caracteristica.celular, caracteristica.racacor)
	        caracteristicasdao.salvarCar(novaCar)
	        Ok("Tudo OCOrreu como deveria")
          //BadRequest("nao implementado")	
        }
      )
    }.getOrElse{
	    Unauthorized("Ishhh")
	  }
  }

  val caractForm = Form(
   mapping(
     "cpf" -> nonEmptyText,
     "nomepai" -> nonEmptyText,
     "nomemae" -> nonEmptyText,
     "nacionalidade" -> nonEmptyText,
     "rg" -> nonEmptyText,
     "sexo" -> nonEmptyText,
     "data_nascimento" -> nonEmptyText,
     "nivel" -> nonEmptyText,
     "pne" -> nonEmptyText,
     "endereco" -> nonEmptyText,
     "uf" -> nonEmptyText,
     "cidade" -> nonEmptyText,
     "cep" -> nonEmptyText,
     "email" -> nonEmptyText, 
     "telefone" -> nonEmptyText,
     "celular" -> nonEmptyText,
     "racacor" -> nonEmptyText
    )(CaracteristicaVO.apply)(CaracteristicaVO.unapply)
   )

}
case class CaracteristicaVO(cpf: String, nomepai: String, nomemae: String, nacionalidade: String, rg: String, sexo: String, data_nascimento: String, nivel: String, pne: String, endereco: String, uf: String, cidade: String, cep: String, email: String, telefone: String, celular: String, racacor: String)
