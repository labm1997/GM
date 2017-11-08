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
  materiasdao: materiasDAO,
  turmasDAO : turmasDAO,
  alunosdao : Alunodao,
  caracteristicasdao : Caracteristicasdao,
  
  cc: ControllerComponents) 
  extends AbstractController(cc) with I18nSupport {
  
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
  def home() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => Ok(views.html.home(dadospessoaisdao.getData(user)(0))) }.getOrElse {
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
    var preRequisitos = materiasdao.getPrerequisitos(id)
    if(materias.length == 1){
      Ok(views.html.materia(materias(0), id, preRequisitos))
    }
    else Unauthorized("Iiiiish")
  }
  
  def novo_aluno() = Action { implicit request: Request[AnyContent] => 
    Ok(views.html.novo_aluno(alunoForm))
  }

  def novoAlunoSubmissao = Action { implicit request =>
    alunoForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest("Há um erro")
      },
      aluno => {
        val novoAluno = Aluno(aluno.matricula, aluno.nome, aluno.senha)
        alunosdao.salvar(novoAluno)
        Created(views.html.novo_aluno(alunoForm))
      }
    )
   }

  def matricular = Action { implicit request =>
    request.session.get("connected").map { user => 
      matriculaForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest("Há um erro")
        },
        requisicao => {
          val r = turmasDAO.addMatricula(requisicao.id, user)
          r match {
            case Some(a) => Ok("Inseriu");
            case None => BadRequest("Erro ao inserir")
          }
        }
      )
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
}    
  def SubmeterCar = Action { implicit request =>
    caractForm.bindFromRequest.fold(
      formWithErrors => {
	//BadRequest(views.html.caracteristicas(formWithErrors))
        BadRequest("deu ruim")	
},
	caracteristica => {
	  //val novaCar = Caracteristicas(caracteristica.cpf, caracteristica.nomepai, caracteristica.nomemae, caracteristica.nacionalidade, caracteristica.rg, caracteristica.sexo, caracteristica.data_nascimento, caracteristica.nivel, caracteristica.pne, caracteristica.endereco, caracteristica.uf, caracteristica.cidade, caracteristica.cep, caracteristica.email, caracteristica.telefone, caracteristica.celular, caracteristica.racacor)
	  //caracteristicasdao.salvarCar(novaCar)
	  //Created(views.html.caracteristicas(caractForm))
          BadRequest("nao implementado")	
}
    )
  }


//  }/*esse é o da classe, só para nao se perder */
    
  val loginForm = Form(
    mapping(
      "user"  -> nonEmptyText,
      "password" -> nonEmptyText,
    )(loginVO.apply)(loginVO.unapply)    
  )
  
  val materiasGET = Form(
    mapping(
      "id"  -> number
    )(materiasGETVO.apply)(materiasGETVO.unapply)    
  )

  val alunoForm = Form(
    mapping(
      "matricula" -> nonEmptyText,
      "nome" -> nonEmptyText,
      "senha" -> nonEmptyText,
    )(AlunoVO.apply)(AlunoVO.unapply)
  )

  val matriculaForm = Form(
    mapping(
      "id" -> number
    )(matriculaVO.apply)(matriculaVO.unapply)
  )

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


case class loginVO(user: String, password: String)
case class materiasGETVO(id: Int)
case class AlunoVO(matricula : String, nome : String, senha : String)
case class matriculaVO(id: Int)
case class CaracteristicaVO(cpf: String, nomepai: String, nomemae: String, nacionalidade: String, rg: String, sexo: String, data_nascimento: String, nivel: String, pne: String, endereco: String, uf: String, cidade: String, cep: String, email: String, telefone: String, celular: String, racacor: String)
