package models

import anorm.SqlParser._
import anorm.{RowParser, ~}
import play.api.libs.json.{Json, Writes}

case class Category(category_id: Long, label:String) {
  def toXml = {
    <category>
      <category_id>{category_id}</category_id>
      <label>{label}</label>
    </category>
  }
}

object Category {
  implicit val jsonWrites: Writes[Category] = Json.writes[Category]

  val parser: RowParser[Category] = {
    get[Long]("ID") ~
      get[String]("LABEL") map { case category_id ~ label =>
      Category(category_id,label)
    }
  }
}


