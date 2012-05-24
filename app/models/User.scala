package models

import play.api.db._
import anorm._
import anorm.SqlParser._
import play.api.Play.current
import org.apache.commons.codec.digest.DigestUtils._

case class User(email: String, password: String)

object User {

  val simple = {
    get[String]("user.email") ~
    get[String]("user.password") map {
      case email~pass => User(email, pass)
    }
  }

  def authenticate(email: String, password: String): Option[User] = {
    findByEmail(email).filter { user => user.password == hash(password, user.email) }
  }

  private def hash(pass: String, salt: String): String = sha256Hex(salt.padTo('0', 256) + pass)

  def findByEmail(email: String): Option[User] = {
    DB.withConnection { implicit connection =>
      SQL("SELECT * FROM user WHERE email = {email}").on(
        'email -> email
      ).as(simple.singleOpt)
    }
  }

  def findAll: Seq[User] = {
    DB.withConnection { implicit connection =>
      SQL("select * from user").as(simple *)
    }
  }

  def create(user: User) {
    DB.withConnection { implicit connection =>
      SQL("INSERT INTO user VALUES ({email}, {pass})").on(
        'email -> user.email,
        'pass -> hash(user.password, user.email)
      ).executeUpdate()
    }
  }


}
