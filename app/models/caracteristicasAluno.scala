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

/**
 * Definir a inserçao das caracteristicas
 * de um aluno no banco de dados
 *
 */

case class Caracteristicas(cpf: String, nomepai: String, nomemae: String, nacionalidade: String,rg: String, sexo: String, data_nascimento: String, nivel: String, pne: String, endereco: String, uf: String, cidade: String, cep: String, email: String, telefone: String, celular: String, racacor: String)

class Caracteristicasdao @Inject() (database: Database){

  def salvarCar(caracteristicas: Caracteristicas) = database.withConnection { implicit connection =>
    val id: Option[Long] = SQL(
      """INSERT INTO tbl_users(cpf, nomepai, nomemae, nacionalidade, rg, sexo, data_nascimento, nivel, pne, endereco, uf, cidade, cep, email, telefone, celular, racacor)
	values ({cpf}, {nomepai}, {nomemae}, {nacionalidade}, {rg}, {sexo}, {data_nascimento}, {nivel}, {pne}, {endereco}, {uf}, {cidade}, {cep}, {email}, {telefone}, {celular}, {racacor})""")
      .on('cpf -> caracteristicas.cpf, 'nomepai -> caracteristicas.nomepai, 'nomemae -> caracteristicas.nomemae,
	  'nacionalidade -> caracteristicas.nacionalidade, 'rg -> caracteristicas.rg, 'sexo -> caracteristicas.sexo,
	  'data_nascimento -> caracteristicas.data_nascimento, 'nivel -> caracteristicas.nivel,
	  'pne -> caracteristicas.pne, 'endereco -> caracteristicas.endereco,
	  'uf -> caracteristicas.uf, 'cidade -> caracteristicas.cidade, 'cep -> caracteristicas.cep, 'email -> caracteristicas.email,
	  'telefone -> caracteristicas.telefone, 'celular -> caracteristicas.celular, 'racacor -> caracteristicas.racacor).executeInsert()
 }
}
