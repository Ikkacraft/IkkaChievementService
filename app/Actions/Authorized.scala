package Actions


import play.api.Play.current
import play.api.libs.ws._
import play.api.mvc.Results._
import play.api.mvc._
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}


class Authorized extends ActionBuilder[Request] with ActionFilter[Request] {

  private val http: WSClient = WS.client

  def filter[A](request: Request[A]): Future[Option[Result]] = {
    val result = request.headers.get("Authorization") map { authHeader =>
      val future: Future[Option[Result]] = http.url("http://localhost:9001/users/authorized").withRequestTimeout(15000).withHeaders("Authorization" -> authHeader).get().map {
        response => {
          response.status match {
            case 401 => Some(Unauthorized)
            case _ => None
          }
        }
      }
      Await.result(future, 5000 millis)

    } getOrElse Some(Unauthorized)
    Future.successful(result)
  }
}

object Authorized extends Authorized
