package models

import anorm.SqlParser._
import anorm.{RowParser, ~}
import play.api.libs.json.{Json, Writes}

case class Badge(badge_id: Long, var title: String, category_id: Long, var description: Option[String], var parameters:Option[String],var urlImage: Option[String]) {

  def this(title: String, category_id: Long, description: Option[String], parameters: Option[String], urlImage: Option[String]) = this(0, title, category_id, description, parameters, urlImage)

  def setTitle(t: String) {
    title = t
  }

  def setDescription(d: String) {
    description = Option(d)
  }

  def setUrlImage(u: String) {
    urlImage = Option(u)
  }

  def setParameters(p: String) {
    parameters = Option(p)
  }

  def toXml = {
    <badge>
      <badge_id>{badge_id}</badge_id>
      <title>{title}</title>
      <category_id>{category_id}</category_id>
      {if (description.isDefined) <description>{description.get}</description> else <description />}
      {if (parameters.isDefined) <parameters>{parameters.get}</parameters> else <parameters />}
      {if (urlImage.isDefined) <url_image>{urlImage.get}</url_image> else <url_image />}
    </badge>
  }
}

object Badge {
  implicit val jsonWrites: Writes[Badge] = Json.writes[Badge]

  val parser: RowParser[Badge] = {
    get[Long]("ID") ~
      get[String]("TITLE") ~
      get[Long]("ID_CATEGORY") ~
      get[Option[String]]("DESCRIPTION") ~
      get[Option[String]]("PARAMETERS") ~
      get[Option[String]]("IMAGE_URL") map { case badge_id ~ title ~ category_id ~ description ~ parameters ~ urlImage=>
      Badge(badge_id, title, category_id, description, parameters, urlImage)
    }
  }
}
