package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.templates._
import models._
import views._
import play.api.mvc._
import play.api.mvc.Results._
import com.blendlabsinc.play20.auth._


object Application extends Controller with StatelessSecurity with LoginLogout {

  val loginForm = Form {
    mapping("email" -> text, "password" -> text)(User.authenticate)(_.map(u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  def home = withOptionalUser { implicit currentUser => implicit request =>
    Ok(html.home())
  }

  def clubhouse = withUser { implicit currentUser => implicit request =>
    Ok(html.clubhouse())
  }

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def logout = Action { implicit request =>
    logoutSucceeded.flashing(
      "success" -> "You've been logged out."
    )
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => loginSucceeded(user.get).flashing(
        "success" -> "You've successfully logged in."
      )
    )
  }

}

trait StatelessSecurity extends StatelessSecurityBase {
  type User = models.User

  def lookupUser(userId: String) = User.findByEmail(userId)
  def getUserId(user: User) = user.email

  def onUnauthorized(request: RequestHeader) = Redirect(routes.Application.login)
  def onLoginSucceeded(request: RequestHeader) = Redirect(routes.Application.home)
  def onLogoutSucceeded(request: RequestHeader) = Redirect(routes.Application.login)
}
