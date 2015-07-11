package com.soundcloud.followermaze.user

import scala.collection.mutable.Set

case class User(id: Int) {
  val followers = Set[Int]()
  
}
