package com.soudcloud.followermaze.route

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import com.soudcloud.followermaze.util.Events
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.event.EventType.UNFOLLOW
import com.soundcloud.followermaze.transport.route.UnfollowRoute
import com.soundcloud.followermaze.user.{ User, UserRepository }

class UnfollowRouteTest extends Specification with Mockito {

  val user = new User(22) { addFollower(3) }
  
  val unfollow = Event("1|U|3|22")
  val unfollowRoute = new UnfollowRoute(userRepository)
  
  "Unfollow Route" should {
    "accept UNFOLLOW event" in {
      unfollowRoute.accept(unfollow) must beTrue
    }
    
    Events.except(UNFOLLOW).foreach(event => {
      s"not accept ${event.eventType} event" in {
        unfollowRoute.accept(event) must beFalse  
      }
    })

    "remove follower and not notify an user" in {
      unfollowRoute.process(unfollow, usersConnected = Set(1, 2)) must beEmpty
      user.followers must not contain(3)
    }
  }
  
  def userRepository = {
    val userRepository = mock[UserRepository]
    userRepository.get(user.id) returns user
  }
}