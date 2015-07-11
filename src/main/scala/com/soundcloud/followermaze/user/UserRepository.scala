package com.soundcloud.followermaze.user

import scala.collection.mutable.Map

class UserRepository {

  val dataset = Map[Int, User]()
 
  def findById(userId: Int): Option[User] = dataset.get(userId)
  
  def save(user: User) { dataset(user.id) = user }
  
}