package models

import anorm.SQL
import anorm.SqlQuery
import anorm.RowParser
import anorm.Macro
import anorm.SqlStringInterpolation
import anorm.SqlParser
import play.api.Play.current
import javax.inject.Inject
import play.api.db.Database
import javax.inject.Singleton

case class Login(user: String, password: String)

/**
 * Um DAO para a classe de entidade Login. 
 */
class LoginDAO @Inject() (database: Database) {
  def verificarCredenciais(user: String, password: String): Login = database.withConnection { implicit connection =>
    val parser: RowParser[Login] = Macro.namedParser[Login]
    return SQL("SELECT matricula as user,password FROM tbl_users WHERE matricula={matricula} LIMIT 1")
    .on("matricula" -> user)
    .as(parser.single)
  }  
}

/*sugestao

class blabla ... ()
  val result : Boolean = SQL("SELECT matricula,password FROM tbl_users WHERE matricula={matricula} LIMIT 1").execute()












*/
