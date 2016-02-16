package controllers

import java.util.UUID
import javax.inject.Inject

import models.{EnhancedBadge, Badge}
import play.api.libs.json.Json
import play.api.mvc._
import services.BadgeService

class Badges @Inject()(badgeService: BadgeService) extends Controller {
  def getBadges() = Action { request =>
    request.accepts("application/json") || request.accepts("text/json") match {
      case true => {
        val badges: List[Badge] = badgeService.getAll()
        if (badges.isEmpty) NoContent
        else Ok(Json.toJson(badges))
      }
      case false => {
        val badges: List[Badge] = badgeService.getAll()
        if (badges.isEmpty) NoContent
        else Ok(<badges>
          {badges.map(a => a.toXml)}
        </badges>)
      }
    }
  }

  def getBadgesByCategory(category_id: Long) = Action { request =>
    request.accepts("application/json") || request.accepts("text/json") match {
      case true => {
        val badges: List[Badge] = badgeService.getBadgesByCategory(category_id)
        if (badges.isEmpty) NoContent
        else Ok(Json.toJson(badges))
      }
      case false => {
        val badges: List[Badge] = badgeService.getBadgesByCategory(category_id)
        if (badges.isEmpty) NoContent
        else Ok(<badges>
          {badgeService.getBadgesByCategory(category_id).map(a => a.toXml)}
        </badges>)
      }
    }
  }

  def getBadgesByUser(user_id: String) = Action { request =>
    request.accepts("application/json") || request.accepts("text/json") match {
      case true => {
        val badges: List[EnhancedBadge] = badgeService.getBadgesByUser(UUID.fromString(user_id))
        if (badges.isEmpty) NoContent
        else Ok(Json.toJson(badges))
      }
      case false => {
        val badges: List[EnhancedBadge] = badgeService.getBadgesByUser(UUID.fromString(user_id))
        if (badges.isEmpty) NoContent
        else Ok(<badges>{badges.map(a => a.toXml)}</badges>)
      }
    }
  }

  def createBadge() = Action(parse.json) { implicit request =>
    val title: String = (request.body \ "title").as[String]
    val category: Long = (request.body \ "category_id").as[Long]
    val description: Option[String] = (request.body \ "description").asOpt[String]
    val image_url: Option[String] = (request.body \ "urlImage").asOpt[String]
    val parameters: Option[String] = (request.body \ "parameters").asOpt[String]

    val badge = new Badge(title, category, description, parameters, image_url)
    badgeService.create(badge) match {
      case -1 => BadRequest("The badge could not be created")
      case _ => Created("The badge has been successfully created")
    }
  }

  def unlockBadge() = Action(parse.json) { implicit request =>
    val user_id: String = (request.body \ "user_id").as[String]
    val badge_id: Long = (request.body \ "badge_id").as[Long]
    val status: String = (request.body \ "status").as[String]
    val remaining: Long = (request.body \ "remaining").asOpt[Long].getOrElse(-1)

    val id = badgeService.unlock(UUID.fromString(user_id), badge_id, status, remaining)
    id match {
      case 0 => BadRequest("The badge could not be unlock")
      case _ => Ok("The badge has been successfully unlock")
    }
  }

  def updateBadge(badge_id: Long) = Action(parse.json) { implicit request =>
    val title: String = (request.body \ "title").as[String]
    val category: Long = (request.body \ "category_id").as[Long]
    val description: Option[String] = (request.body \ "description").asOpt[String]
    val parameters: Option[String] = (request.body \ "parameters").asOpt[String]
    val image_url: Option[String] = (request.body \ "urlImage").asOpt[String]


    val badge = new Badge(badge_id, title, category, description, parameters, image_url)
    val id = badgeService.update(badge)
    id match {
      case 0 => BadRequest("The badge " + badge_id + " could not be updated")
      case _ => Ok("The badge " + badge_id + " has been successfully updated")
    }
  }

  def deleteBadge(badge_id: Long) = Action {
    Ok(Json.toJson(badgeService.delete(badge_id)))
  }

  def getCategories() = Action { request =>
    request.accepts("application/json") || request.accepts("text/json") match {
      case true => Ok(Json.toJson(badgeService.getAllCategory()))
      case false => Ok(<categories>
        {badgeService.getAllCategory().map(a => a.toXml)}
      </categories>)
    }
  }

  def getCategory(category_id: Long) = Action { request =>
    request.accepts("application/json") || request.accepts("text/json") match {
      case true => {
        badgeService.getCategory(category_id) match {
          case Some(category) => Ok(Json.toJson(category))
          case None => NoContent
        }
      }
      case false => {
        badgeService.getCategory(category_id) match {
          case Some(category) => Ok(category.toXml)
          case None => NoContent
        }
      }
    }
  }

  def getBadgesFilteredByUser(user_id: String, status: String) = Action { request =>
    request.accepts("application/json") || request.accepts("text/json") match {
      case true => {
        val badges: List[Badge] = badgeService.getBadgesFilteredByUser(UUID.fromString(user_id), status)
        if (badges.isEmpty) NoContent
        else Ok(Json.toJson(badges))
      }
      case false => {
        val badges: List[Badge] = badgeService.getBadgesFilteredByUser(UUID.fromString(user_id), status)
        if (badges.isEmpty) NoContent
        else Ok(<categories>
          {badgeService.getBadgesFilteredByUser(UUID.fromString(user_id), status).map(a => a.toXml)}
        </categories>)
      }
    }
  }
}
