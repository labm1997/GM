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
  historicodao : historicoDAO,
  gradeHorariadao : gradeDAO,
  
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
    request.session.get("connected").map { user => 
      Ok(views.html.gradehoraria(gradeHorariadao.getGrade(user)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
  def historicoEscolar() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => 
      var historico = historicodao.getHistorico(user).groupBy(a=>a.semestre).toList sortBy (_._1)
      Ok(views.html.historicoescolar(historico))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
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
  
  def novo_aluno() = Action { implicit request: Request[AnyContent] => 
    request.session.get("connected").map { user => 
      /* !TODO verificar se user é coordenador */
      Ok(views.html.novo_aluno(alunoForm, alunosdao.mostrarAlunosCoordenador(user)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }

  def novoAlunoSubmissao = Action { implicit request =>
    request.session.get("connected").map { user => 
      /* !TODO verificar se user é coordenador */
      alunoForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest("Há um erro de formulário")
        },
        aluno => {
          val novoAluno = AlunoPost(aluno.matricula, aluno.nome, md5Hash(aluno.senha), aluno.semestre_inicio)
          dadospessoaisdao.getData(user)(0).cursoID match {
            case Some(curso) => 
              alunosdao.salvar(novoAluno, curso)
              Created("Aluno inserido com sucesso")
            case None => Unauthorized("Há um erro")
          }
        }
      )
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }

  def matricular = Action { implicit request =>
    request.session.get("connected").map { user => 
      matriculaForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest("Há um erro")
        },
        requisicao => {
          val turma = turmasDAO.getTurmaData(requisicao.id)
          if(turma.length == 1){
            val r = turmasDAO.addMatricula(turma(0), user, materiasdao.getPrerequisitos(turma(0).ID_MATERIA))
            r match {
              case Some(a) => Ok("Solicitação efetuada");
              case None => BadRequest("Erro ao inserir")
            }
          }
          else Unauthorized("Turma inválida")
        }
      )
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }
  
  def retirarMatricula(idturma: Int) = Action { implicit request =>
    request.session.get("connected").map { user => 
      turmasDAO.retirarMatricula(user, idturma)
      Ok(views.html.matricula(turmasDAO.getSolicitacoes(user)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }

  def matriculaAtualizar = Action { implicit request =>
    request.session.get("connected").map { user => 
      matriculaUpdateForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest("Há um erro")
        },
        requisicao => {
          turmasDAO.atualizarMatricula(user,requisicao.idturma,requisicao.prioridade)
          Ok("Solicitação efetuada")
        }
      )
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
  }

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
	        val nome = caracteristicasdao.getMatricula(user)
	        val novaCar = Caracteristicas(user, caracteristica.cpf, caracteristica.nomepai, caracteristica.nomemae, caracteristica.nacionalidade, caracteristica.rg, caracteristica.sexo, caracteristica.data_nascimento, caracteristica.nivel, caracteristica.pne, caracteristica.endereco, caracteristica.uf, caracteristica.cidade, caracteristica.cep, caracteristica.email, caracteristica.telefone, caracteristica.celular, caracteristica.racacor)
	        caracteristicasdao.salvarCar(novaCar)
	        Created(views.html.caracteristicas(caractForm))
          //BadRequest("nao implementado")	
        }
      )
    }.getOrElse{
	    Unauthorized("Ishhh")
	  }
  }
  
  def matricula() = Action { implicit request: Request[AnyContent] =>
    request.session.get("connected").map { user => 
      Ok(views.html.matricula(turmasDAO.getSolicitacoes(user)))
    }.getOrElse {
      Unauthorized("Iiiiish")
    }
    
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
      "semestre_inicio" -> nonEmptyText
    )(AlunoVO.apply)(AlunoVO.unapply)
  )

  val matriculaForm = Form(
    mapping(
      "id" -> number
    )(matriculaVO.apply)(matriculaVO.unapply)
  )

  val matriculaUpdateForm = Form(
    mapping(
      "idturma" -> number,
      "prioridade" -> number
    )(matriculaUpdateVO.apply)(matriculaUpdateVO.unapply)
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
case class AlunoVO(matricula : String, nome : String, senha : String, semestre_inicio: String)
case class matriculaVO(id: Int)
case class matriculaUpdateVO(idturma: Int, prioridade: Int)
case class CaracteristicaVO(cpf: String, nomepai: String, nomemae: String, nacionalidade: String, rg: String, sexo: String, data_nascimento: String, nivel: String, pne: String, endereco: String, uf: String, cidade: String, cep: String, email: String, telefone: String, celular: String, racacor: String)
