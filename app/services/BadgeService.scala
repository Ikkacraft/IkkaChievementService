package services

import models.Badge


class BadgeService {
  def getAll() = ???

  def getByCategory(category_id: Long) = ???

  def getByUser(user_id: String) = ???

  def create(account: Badge) = ???

  def unlock(user_id: Long, badge_id: Long) = ???

  def update(badge: Badge) = ???

  def delete(badge_id: Long) = ???
}
