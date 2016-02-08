package models

import anorm.RowParser
import anorm.SqlParser._
import anorm.~
import play.api.libs.json.Json
import play.api.libs.json.Writes

case class Badge(badge_id: Long, var title: String, var description: Option[String], var urlImage: Option[String], categorie_id: Option[Long]) {

  def this(title: String, description: Option[String]) = this(0, title, description)

  def setTitle(t: String) {
    title = t
  }

  def setDescription(d: String) {
    description = Option(d)
  }

  def setUrlPic(u: String) {
    urlImage = Option(u)
  }
}

object Badge {
  implicit val jsonWrites: Writes[Badge] = Json.writes[Badge]

  val parser: RowParser[Badge] = {
    get[Long]("ID") ~
      get[String]("TITRE") ~
      get[Option[String]]("DESCRIPTION") ~
      get[Option[String]]("URLPIC") ~
      get[Option[Long]]("ID_CATEGORIE") map { case badge_id ~ title ~ description ~ urlImage ~ categorie_id =>
      Badge(badge_id, title, description, urlImage, categorie_id)
    }
  }
}
