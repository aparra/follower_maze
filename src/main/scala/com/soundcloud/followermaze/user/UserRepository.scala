package com.soundcloud.followermaze.user

import scala.collection.mutable.Map

class UserRepository {

  val dataset = Map[Int, User]()

  def get(userId: Int): User = dataset.get(userId) match {
    case Some(user) => user
    case None => save(userId)
  }
  
  def save(userId: Int): User = {
    val newUser = User(userId)
    dataset(userId) = newUser 
    newUser
  }
}