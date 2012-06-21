package com.blendlabsinc.play20.auth

import play.api.mvc._
import org.apache.commons.codec.binary.Base64.decodeBase64

trait StatelessSecurityBase {

  type User

  def getUserIdFromRequest(request: RequestHeader): Option[String] =
    Seq(getUserIdFromCookie _, getUserIdFromHTTPBasicAuth _).flatMap(_.apply(request)).headOption

  def getUserIdFromCookie(request: RequestHeader): Option[String] =
    AuthData.decodeFromCookie(request.cookies.get(AuthData.COOKIE_NAME)).get(AuthData.UserIdKey)

  def getUserIdFromHTTPBasicAuth(request: RequestHeader): Option[String] =
    request.headers.get("Authorization").flatMap { authorization =>
      authorization.split(" ").drop(1).headOption.flatMap { encoded =>
        new String(decodeBase64(encoded.getBytes)).split(":").toList match {
          case userId :: password :: Nil => authenticateUserAndReturnUserId(userId, password)
          case _ => None
        }
      }
    }

  def lookupUser(userId: String): Option[User]
  def getUserId(user: User): String
  def authenticateUserAndReturnUserId(userId: String, password: String): Option[String]
  
  def onUnauthorized(request: RequestHeader): Result
  def onLoginSucceeded(request: RequestHeader): PlainResult
  def onLogoutSucceeded(request: RequestHeader): PlainResult

  def withAuth(f: => String => Request[AnyContent] => Result): Action[AnyContent] =
    withAuth(BodyParsers.parse.anyContent)(f)

  def withAuth[A](p: BodyParser[A])(f: => String => Request[A] => Result): Action[A] =
    Action(p)(request => getUserIdFromRequest(request) match {
      case Some(userId) => f(userId)(request)
      case None => onUnauthorized(request)
    })

  def withUser(f: User => Request[AnyContent] => Result): Action[AnyContent] =
    withUser(BodyParsers.parse.anyContent)(f)

  def withUser[A](p: BodyParser[A])(f: User => Request[A] => Result): Action[A] = withAuth(p) { userId => implicit request =>
    lookupUser(userId).map { user =>
      f(user)(request)
    }.getOrElse(onUnauthorized(request))
  }

  def withOptionalAuth(f: Option[String] => Request[AnyContent] => Result): Action[AnyContent] =
    withOptionalAuth(BodyParsers.parse.anyContent)(f)

  def withOptionalAuth[A](p: BodyParser[A])(f: Option[String] => Request[A] => Result): Action[A] =
    Action(p)(request => f(getUserIdFromRequest(request))(request))

  def withOptionalUser(f: Option[User] => Request[AnyContent] => Result): Action[AnyContent] =
    withOptionalUser(BodyParsers.parse.anyContent)(f)

  def withOptionalUser[A](p: BodyParser[A])(f: Option[User] => Request[A] => Result): Action[A] = withOptionalAuth(p) { userId => implicit request =>
    userId.map { userId =>
      lookupUser(userId).map { user =>
        f(Some(user))(request)
      }.getOrElse(f(None)(request))
    }.getOrElse(f(None)(request))
  }
}

