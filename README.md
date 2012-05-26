Play2.0 module for stateless authentication
===========================================================

This module adds stateless authentication features to Play 2.0 applications. 

Target
---------------------------------------

This module targets the __Scala__ version of __Play 2.0__.

This module has been tested on Play 2.0.1.

Motivation
---------------------------------------

We wanted an authentication module that was:

### Stateless (no server state)

Don't store any state on the server side. Store it on the client side and cryptographically sign it
to prevent tampering.

### Simple

Make it easy.

### Flexible

Allow multiple kinds of controller actions: login-required and login-optional.


Known Issues
---------------------------------------

1. Auth cookies have no expiration.

2. The contents of the auth cookie are visible to the client.


Installation
---------------------------------------

1. Add a dependency declaration into your `Build.scala` or `build.sbt` file.

    1. Snapshot

        "com.blendlabsinc" %% "play20-stateless-auth" % "0.2-SNAPSHOT"


Usage
---------------------------------------

The code below is taken from the sample application included in this git repository.

### Configure your application

Define a trait that extends StatelessSecurityBase and specifies authentication handlers:

```scala
import com.blendlabsinc.play20.auth._

trait StatelessSecurity extends StatelessSecurityBase {
  type User = models.User

  def lookupUser(userId: String) = User.findByEmail(userId)
  def getUserId(user: User) = user.email

  def onUnauthorized(request: RequestHeader) = Redirect(routes.Application.login)
  def onLoginSucceeded(request: RequestHeader) = Redirect(routes.Application.home)
  def onLogoutSucceeded(request: RequestHeader) = Redirect(routes.Application.login)
}

```

### Define a User model

```scala
package models

case class User(email: String, password: String)

object User {
  def findByEmail(email: String): Option[User] = { /* user lookup code */ }
}

```

### Add StatelessSecurity to your controllers and actions

```scala
import com.blendlabsinc.play20.auth._

object Application extends Controller with StatelessSecurity with LoginLogout {
  /* Login optional. currentUser is an Option[User]. */
  def home = withOptionalUser { implicit currentUser => implicit request =>
    Ok(html.home())
  }

  /* Login required. currentUser is a User. */
  def clubhouse = withUser { implicit currentUser => implicit request =>
    Ok(html.clubhouse())
  }
}
```

### Use the currentUser val in your views

#### `home.scala.html`

```
@()(implicit currentUser: Option[User], flash: Flash)

@template(currentUser)("Home") {


<h1>Home: 
  @currentUser.map { currentUser =>
    @currentUser.email
  }.getOrElse("not logged in")
</h1>

@flash.get("success").map { message =>
  <p class="success">@message</p>
}   

}
```

#### `clubhouse.scala.html`

```
@()(implicit currentUser: User, flash: Flash)

@template(Some(currentUser))("Clubhouse (login required)") {


<h1>Welcome to the clubhouse,
  @currentUser.email
</h1>

This page is only visible to logged-in users.

}
```


Authors
---------------------------------------

Play20-stateless-auth was developed by Blend Labs: http://blendlabsinc.com.

This module was originally based on play20-auth at https://github.com/t2v/play20-auth.

License
---------------------------------------

This library is released under the Apache Software License, version 2, 
which should be included with the source in a file named `LICENSE`.

