package services

import java.util.UUID

import anorm._
import models.{Badge, Category, EnhancedBadge}
import play.api.Play.current
import play.api.db._

class BadgeService {
  def getBadgesFilteredByUser(user_id: UUID, status: String): List[Badge] = {
    val results: List[Badge] = DB.withConnection { implicit c =>
      SQL( """SELECT BADGE.ID, ID_CATEGORY, TITLE, DESCRIPTION, IMAGE_URL, PARAMETERS, UNBLOCK.STATUS, UNBLOCK.REMAINING FROM BADGE JOIN UNBLOCK ON UNBLOCK.ID = BADGE.ID WHERE UNBLOCK.UUID = {user_id} AND UNBLOCK.STATUS= {status} """)
        .on('user_id -> user_id, 'status -> status).as(Badge.parser.*)
    }
    results
  }

  def getAllCategory(): List[Category] = {
    val results: List[Category] = DB.withConnection { implicit c =>
      SQL( """SELECT * FROM CATEGORY""").as(Category.parser.*)
    }
    results
  }

  def getCategory(category_id: Long): Option[Category] = {
    val results: Option[Category] = DB.withConnection { implicit c =>
      SQL( """SELECT * FROM CATEGORY WHERE ID = {category_id}""").on('category_id -> category_id).as(Category.parser.singleOpt)
    }
    results
  }

  def getAll(): List[Badge] = {
    val results: List[Badge] = DB.withConnection { implicit c =>
      SQL( """SELECT * FROM BADGE""").as(Badge.parser.*)
    }
    results
  }

  def getBadgesByCategory(category_id: Long): List[Badge] = {
    val results: List[Badge] = DB.withConnection { implicit c =>
      SQL( """SELECT * FROM BADGE WHERE ID_CATEGORY = {category_id} """).on('category_id -> category_id).as(Badge.parser.*)
    }
    results
  }

  def getBadgesByUser(user_id: UUID) : List[EnhancedBadge] = {
    val results: List[EnhancedBadge] = DB.withConnection { implicit c =>
      SQL( """SELECT BADGE.ID, ID_CATEGORY, TITLE, DESCRIPTION, IMAGE_URL, PARAMETERS, UNBLOCK.STATUS, UNBLOCK.REMAINING FROM BADGE JOIN UNBLOCK ON UNBLOCK.ID = BADGE.ID WHERE UNBLOCK.UUID = {user_id}  ORDER BY STATUS DESC""")
        .on('user_id -> user_id).as(EnhancedBadge.parser.* )
    }
    results
  }

  def create(badge: Badge): Long = {
    val id: Option[Long] = DB.withConnection { implicit c =>
      SQL("INSERT INTO BADGE(ID_CATEGORY, TITLE, DESCRIPTION, IMAGE_URL, PARAMETERS) VALUES({id_category}, {title}, {description}, {image_url}, {parameters})")
        .on('id_category -> badge.category_id, 'title -> badge.title, 'description -> badge.description, 'image_url -> badge.urlImage, 'parameters -> badge.parameters).executeInsert()
    }
    id.getOrElse(-1)
  }

  def updateUserBadge(user_id: UUID, badge_id: Long, status: String, remaining: Long): Long = {
    val id: Long = DB.withConnection { implicit c =>
      SQL("REPLACE INTO UNBLOCK VALUES({badge_id}, {user_id}, {status}, {remaining})")
        .on('user_id -> user_id, 'badge_id -> badge_id, 'status -> status, 'remaining -> remaining).executeUpdate()
    }
    id
  }

  def update(badge: Badge) = {
    val id: Int = DB.withConnection { implicit c =>
      SQL("UPDATE BADGE SET ID_CATEGORY = {category_id}, TITLE = {title}, description = {description},  IMAGE_URL = {image_url}, PARAMETERS = {parameters} WHERE ID = {badge_id}")
        .on('badge_id -> badge.badge_id, 'category_id -> badge.category_id, 'title -> badge.title, 'description -> badge.description, 'image_url -> badge.urlImage, 'parameters -> badge.parameters).executeUpdate()
    }
    id
  }

  def delete(badge_id: Long) = {
    deleteUnlockBadges(badge_id)

    val result: Int = DB.withConnection { implicit c =>
      SQL("DELETE FROM BADGE WHERE ID = {badge_id}").on('badge_id -> badge_id).executeUpdate()
    }
    result
  }

  def deleteUnlockBadges(badge_id: Long): Int = {
    DB.withConnection { implicit c =>
      SQL("DELETE FROM UNBLOCK WHERE ID = {badge_id}").on('badge_id -> badge_id).executeUpdate()
    }
  }
}
