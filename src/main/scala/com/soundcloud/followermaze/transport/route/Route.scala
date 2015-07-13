package com.soundcloud.followermaze.transport.route

import scala.collection.Set
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.event.EventType._
import com.soundcloud.followermaze.user.UserRepository

trait Route {
  def process(event: Event, usersConnected: Set[Int]): Set[Int]
  def accept(event: Event): Boolean
}

class BroadcastRoute extends Route {

  override def process(event: Event, usersConnected: Set[Int]): Set[Int] = usersConnected
  override def accept(event: Event): Boolean = event.is(BROADCAST)
}

class PrivateMessageRoute extends Route {

  override def process(event: Event, usersConnected: Set[Int]): Set[Int] = Set(event.userTo)
  override def accept(event: Event): Boolean = event.is(PRIVATE_MESSAGE)
}

class FollowRoute(userRepository: UserRepository) extends Route {

  override def process(event: Event, usersConnected: Set[Int]): Set[Int] = {
    userRepository.get(event.userTo).addFollower(event.userFrom)
    Set(event.userTo)
  }

  override def accept(event: Event): Boolean = event.is(FOLLOW)
}

class UnfollowRoute(userRepository: UserRepository) extends Route {

  override def process(event: Event, usersConnected: Set[Int]): Set[Int] = {
    userRepository.get(event.userTo).removeFollower(event.userFrom)
    Set.empty
  }

  override def accept(event: Event): Boolean = event.is(UNFOLLOW)
}

class StatusEventRoute(userRepository: UserRepository) extends Route {

  override def process(event: Event, usersConnected: Set[Int]): Set[Int] = {
    Set(userRepository.get(event.userFrom).followers.toList: _*)
  }

  override def accept(event: Event): Boolean = event.is(STATUS_EVENT)
}

object Route {
  
  def buildAvailableRoutes(userRepository: UserRepository) = Set(
      new FollowRoute(userRepository), 
      new UnfollowRoute(userRepository),
      new BroadcastRoute(),
      new PrivateMessageRoute(),
      new StatusEventRoute(userRepository))
}
