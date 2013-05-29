package controllers

import play.api._
import play.api.mvc._
import model.Gender
import scala.concurrent.Future, Future._

object Application extends Controller {
  private def guessr(fields: Map[String, String]): Future[Map[String, String]] = {
    import sys.process._
    successful { Map.empty }
  }

  def guess = Action(BodyParsers.parse.urlFormEncoded) { request ⇒
    Async {
      for {
        guess ← guessr(Map.empty)
      } yield Ok(views.html.guess(guess("age").toInt, Gender.withName(guess("gender"))))
    }

  }

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}