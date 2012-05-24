import play.api._

import models._
import anorm._

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    if (User.findAll.isEmpty) {
      Seq(
        User("alice@example.com", "secret"),
        User("bob@example.com", "secret"),
        User("chris@example.com", "secret")
      ) foreach User.create
    }

  }

}
