package controllers

import java.util.UUID
import javax.inject.Inject

import models.Badge
import play.api.libs.json.Json
import play.api.mvc._
import services.BadgeService

import scala.concurrent.Future

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
    val category: Long = (request.body \ "category").as[Long]
    val description: Option[String] = (request.body \ "description").asOpt[String]
    val image_url: Option[String] = (request.body \ "image_url").asOpt[String]
    val parameters: Option[String] = (request.body \ "parameters").asOpt[String]

    val badge = new Badge(title, category, description, parameters, image_url)
    val id = badgeService.create(badge)
    id match {
      case -1 => BadRequest("The badge could not be created")
      case _  => Created(Json.toJson(id))
    }
  }

  def unlockBadge() = Action(parse.json) { implicit request =>
    val user_id:Long = (request.body \ "user_id").as[Long]
    val badge_id:Long = (request.body \ "badge_id").as[Long]
    val status:String = (request.body \ "status").as[String]

    val id:Future[Int] = badgeService.unlock(user_id, badge_id, status)
    id match {
      case -1 => BadRequest("The badge could not be unlock")
      case _  => Ok(Json.toJson(id))
    }

  }

  def updateBadge(badge_id: Long) = Action(parse.json) { implicit request =>
    val title: String = (request.body \ "title").as[String]
    val category: Long = (request.body \ "category").as[Long]
    val description: Option[String] = (request.body \ "description").asOpt[String]
    val parameters: Option[String] = (request.body \ "parameters").asOpt[String]
    val image_url: Option[String] = (request.body \ "image_url").asOpt[String]


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
}