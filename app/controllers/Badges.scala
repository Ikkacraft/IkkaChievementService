package controllers

import java.util.UUID
import javax.inject.Inject

import models.Badge
import play.api.libs.json.Json
import play.api.mvc._
import services.BadgeService

class Badges @Inject()(badgeService: BadgeService) extends Controller {
  def getBadges() = Action {
    Ok(Json.toJson(badgeService.getAll()))
  }

  def getBadgesByCategory(category_id: Long) =  Action {
    Ok(Json.toJson(badgeService.getBadgesByCategory(category_id)))
  }

  def getBadgesByuser(user_id: String) =  Action {
    Ok(Json.toJson(badgeService.getBadgesByUser(UUID.fromString(user_id))))
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
      case _  => Created("The badge has been successfully created")
    }
  }

  def unlockBadge() = Action(parse.json) { implicit request =>
    val user_id:String = (request.body \ "user_id").as[String]
    val badge_id:Long = (request.body \ "badge_id").as[Long]
    val status:String = (request.body \ "status").as[String]
    val remaining:Long = (request.body \ "remaining").asOpt[Long].getOrElse(-1)

    val id = badgeService.unlock(UUID.fromString(user_id), badge_id, status, remaining)
    id match {
      case 0 => BadRequest("The badge could not be unlock")
      case _  => Ok(Json.toJson(id))
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
      case _ => Ok("The badge " + Json.toJson(id) + " has been successfully updated")
    }
  }

  def deleteBadge(badge_id: Long) = Action {
    Ok(Json.toJson(badgeService.delete(badge_id)))
  }

  def getCategories() = Action {
    Ok(Json.toJson(badgeService.getAllCategory()))
  }

  def getCategory(category_id : Long) = Action {
    Ok(Json.toJson(badgeService.getCategory(category_id)))
  }

  def getBadgesFilteredByUser(user_id: String, status: String) = Action {
    Ok(Json.toJson(badgeService.getBadgesFilteredByUser(UUID.fromString(user_id), status)))
  }
}
