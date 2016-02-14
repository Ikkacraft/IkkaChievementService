package models


import anorm.SqlParser._
import anorm.{RowParser, ~}
import play.api.libs.json.{Json, Writes}


case class EnhancedBadge(badge_id: Long, var title: String, category_id: Long, var description: Option[String], var parameters: Option[String], var urlImage: Option[String], status: Option[String], remaining: Option[Long]) {

  def this(title: String, category_id: Long, description: Option[String], parameters: Option[String], urlImage: Option[String], status: Option[String], remaining: Option[Long]) = this(0, title, category_id, description, parameters, urlImage, status, remaining)

}

object EnhancedBadge {
  implicit val jsonWrites: Writes[EnhancedBadge] = Json.writes[EnhancedBadge]

  val parser: RowParser[EnhancedBadge] = {
    get[Long]("ID") ~
      get[String]("TITLE") ~
      get[Long]("ID_CATEGORY") ~
      get[Option[String]]("DESCRIPTION") ~
      get[Option[String]]("PARAMETERS") ~
      get[Option[String]]("IMAGE_URL") ~
      get[Option[String]]("STATUS") ~
      get[Option[Long]]("REMAINING") map { case badge_id ~ title ~ category_id ~ description ~ parameters ~ urlImage ~ status ~ remaining =>
      EnhancedBadge(badge_id, title, category_id, description, parameters, urlImage, status, remaining)
    }
  }
}
