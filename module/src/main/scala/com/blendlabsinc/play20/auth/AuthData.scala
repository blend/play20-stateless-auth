package com.blendlabsinc.play20.auth

import play.api._
import play.api.mvc._

case class AuthData(data: Map[String,String] = Map()) {
  def get(key: String) = data.get(key)
}

object AuthData extends CookieBaker[AuthData] {
  val UserIdKey = "UserId"
  val COOKIE_NAME = Play.maybeApplication.flatMap(_.configuration.getString("auth.cookieName")).getOrElse("PLAY_AUTH")
  val emptyCookie = new AuthData
  override val isSigned = true
  val secure = Play.maybeApplication.flatMap(_.configuration.getBoolean("auth.secure")).getOrElse(false)
  val maxAge = Play.maybeApplication.flatMap(_.configuration.getInt("auth.maxAge")).getOrElse(-1)
  val httpOnly = Play.maybeApplication.flatMap(_.configuration.getBoolean("auth.httpOnly")).getOrElse(true)

  def deserialize(data: Map[String,String]) = new AuthData(data)

  def serialize(authData: AuthData) = authData.data
}
