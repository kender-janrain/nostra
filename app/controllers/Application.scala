package controllers

import play.api._
import play.api.mvc._
import model.Gender
import scala.concurrent.Future, Future._
import java.io.ByteArrayInputStream
import scala.annotation.tailrec

object Application extends Controller {
  import play.api.libs.concurrent.Execution.Implicits._

  @tailrec
  private def messages(e: Throwable, prev: List[String] = Nil): List[String] = {
    val next = prev :+ e.getMessage
    if (e.getCause == null) next
    else messages(e.getCause, next)
  }

  private def guessr(fields: Map[String, String]): Future[Map[String, String]] = {
    import scala.sys.process._
    import play.api.Play.current

    val cmd = Play.configuration.getString("command.guess").getOrElse("./guess.sh")
    Future {
      try {
        val input = new ByteArrayInputStream(fields.map(p ⇒ s"${p._1}=${p._2}").mkString("\n").getBytes)
        val p = (cmd #< input)
        val output = (p !!) split("\n") map { line ⇒
          line.split("=") match {
            case Array(key, value) ⇒ key → value
          }
        }
        output.toMap
      }
    }
  }

  def guess = Action(BodyParsers.parse.urlFormEncoded) { request ⇒
    Async {
      (for {
        guess ← guessr(request.body.toMap.map {
          case (key, values) ⇒ key → values.head
        })
      } yield Ok(views.html.guess(guess("age").toInt, Gender.withName(guess("gender"))))) recover {
        case e: Exception ⇒ Ok(messages(e).mkString("\n"))
      }
    }

  }

  def index = Action {
    Ok(views.html.index())
  }
}