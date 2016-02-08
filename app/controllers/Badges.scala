package controllers

import com.google.inject.Inject
import models.Badge
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import services.BadgeService

class Badges @Inject()(badgeService: BadgeService) extends Controller{
  def getBadges() = Action {
    Ok(Json.toJson(badgeService.getAll()))
  }

  def getBadgesByCategory(category_id: Long) =  Action {
    Ok(Json.toJson(badgeService.getByCategory(category_id)))
  }

  def getBadgesByuser(user_id: String) =  Action {
    Ok(Json.toJson(badgeService.getByUser(user_id)))
  }

  def createBadge() = Action(parse.json) { implicit request =>
    val title: String = (request.body \ "title").as[String]
    val description: Option[String] = (request.body \ "description").asOpt[String]
    val badge = new Badge(title, description)
    val id = badgeService.create(badge)
    id match {
      case -1 => BadRequest("The badge could not be created")
      case _  => Created(Json.toJson(id))
    }
  }

  def unlockBadge() = Action(parse.json) { implicit request =>
    val user_id:Long = (request.body \ "user_id").as[Long]
    val badge_id:Long = (request.body \ "badge_id").as[Long]

    val id = badgeService.unlock(user_id, badge_id)
    id match {
      case -1 => BadRequest("The badge could not be unlock")
      case _  => Ok(Json.toJson(id))
    }

  }

  def updateBadge(badge_id: Long) = Action(parse.json) { implicit request =>
    val title: String = (request.body \ "title").as[String]
    val description: Option[String] = (request.body \ "description").asOpt[String]

    val badge = new Badge(title, description)
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