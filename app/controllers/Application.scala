package controllers

import play.api._
import play.api.mvc._
import model.Gender
import scala.concurrent.Future, Future._
import java.io.ByteArrayInputStream
import scala.annotation.tailrec
import play.api.libs.ws.WS

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

    val fieldOrder = List (
      "gender",
      "age"
      )

    val cmd = Play.configuration.getString("command.guess").getOrElse("./guess.sh")
    Future {
      try {
        val input = new ByteArrayInputStream({
          fields.toSeq.sortBy {
            case (k, v) ⇒ fieldOrder.indexOf(k)
          } map {
            case (k, v) ⇒ v
          } mkString(",")
        }.getBytes)
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

  def index = Action { implicit request ⇒
    Ok(views.html.index(routes.Application.token().absoluteURL()))
  }

  def token = Action(BodyParsers.parse.urlFormEncoded) { implicit request ⇒
    Async {
      val token = request.body("token").head
      WS.url("https://rpxnow.com/api/v2/auth_info").post(Map(
        "apiKey" -> Seq("a73b1ebad94f0f650c48d9c7dbc341375d22c7b5"),
        "token" -> Seq(token)
      )) map { response =>
        println(response.json)
        Redirect("/").withCookies(Cookie("token", token))
      }
    }
  }

}