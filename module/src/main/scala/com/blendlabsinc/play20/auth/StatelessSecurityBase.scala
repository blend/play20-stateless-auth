package com.blendlabsinc.play20.auth

import play.api.mvc._

trait StatelessSecurityBase {

  type User

  def userId(request: RequestHeader): Option[String] =
    AuthData.decodeFromCookie(request.cookies.get(AuthData.COOKIE_NAME)).get(AuthData.UserIdKey)

  def lookupUser(userId: String): Option[User]
  def getUserId(user: User): String
  
  def onUnauthorized(request: RequestHeader): Result
  def onLoginSucceeded(request: RequestHeader): PlainResult
  def onLogoutSucceeded(request: RequestHeader): PlainResult

  def withAuth(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(userId, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }

  def withUser(f: User => Request[AnyContent] => Result) = withAuth { userId => implicit request =>
    lookupUser(userId).map { user =>
      f(user)(request)
    }.getOrElse(onUnauthorized(request))
  }

  def withOptionalAuth(f: Option[String] => Request[AnyContent] => Result) =
    Action(request => f(userId(request))(request))

  def withOptionalUser(f: Option[User] => Request[AnyContent] => Result) = withOptionalAuth { userId => implicit request =>
    userId.map { userId =>
      lookupUser(userId).map { user =>
        f(Some(user))(request)
      }.getOrElse(f(None)(request))
    }.getOrElse(f(None)(request))
  }
}

