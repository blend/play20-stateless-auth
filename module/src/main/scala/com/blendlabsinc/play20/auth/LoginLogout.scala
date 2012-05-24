package com.blendlabsinc.play20.auth

import play.api.mvc.{Request, PlainResult, Controller, Cookie}

trait LoginLogout {
  self: Controller with StatelessSecurityBase =>

  def loginSucceeded[A](user: User)(implicit request: Request[A]): PlainResult = {
    onLoginSucceeded(request).withCookies(
      AuthData.encodeAsCookie(
        AuthData(Map(AuthData.UserIdKey -> getUserId(user)))
      )
    )
  }

  def logoutSucceeded[A](implicit request: Request[A]): PlainResult = {
    onLogoutSucceeded(request).discardingCookies(AuthData.COOKIE_NAME)
  }
}
