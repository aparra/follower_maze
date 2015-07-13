package com.soundcloud.followermaze.user

import scala.collection.mutable.Set

case class User(id: Int) {
  val followers = Set[Int]()

  def addFollower(userId: Int) = followers.add(userId)
  
  def removeFollower(userId: Int) = followers.remove(userId)
}

object User {
  val Unknown: Int = -1
}