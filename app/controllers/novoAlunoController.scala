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
class novoAlunoController @Inject()(alunosdao : Alunodao, dadospessoaisdao: dadosPessoaisDAO, cc: ControllerComponents) 
  extends AbstractController(cc) with I18nSupport {
  
  def md5Hash(text: String) : String = java.security.MessageDigest.getInstance("MD5").digest(text.getBytes()).map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  /* OBSERVACAO: aqui provavelmente poderia ser refatorado, visto que o método md5Hash é utilizado em mais de um controller */

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

  val alunoForm = Form(
    mapping(
      "matricula" -> nonEmptyText,
      "nome" -> nonEmptyText,
      "senha" -> nonEmptyText,
      "semestre_inicio" -> nonEmptyText
    )(AlunoVO.apply)(AlunoVO.unapply)
  )
}
case class AlunoVO(matricula : String, nome : String, senha : String, semestre_inicio: String)
