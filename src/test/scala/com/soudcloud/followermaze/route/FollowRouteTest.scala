package com.soudcloud.followermaze.route

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import com.soudcloud.followermaze.util.Events
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.event.EventType.FOLLOW
import com.soundcloud.followermaze.transport.route.FollowRoute
import com.soundcloud.followermaze.user.{ User, UserRepository }

class FollowRouteTest extends Specification with Mockito {

  val user = new User(42)
  
  val follow = Event("1|F|1|42")
  val followRoute = new FollowRoute(userRepository)
  
  "Follow Route" should {
    "accept FOLLOW event" in {
      followRoute.accept(follow) must beTrue
    }

    Events.except(FOLLOW).foreach(event => {
      s"not accept ${event.eventType} event" in {
        followRoute.accept(event) must beFalse  
      }
    })
    
    "add follower and notify only the followed user" in {
      followRoute.process(follow, usersConnected = Set(1)) must beEqualTo(Set(42))
      user.followers must contain(1)
    }
  }
  
  def userRepository = {
    val userRepository = mock[UserRepository]
    userRepository.get(user.id) returns user
  }
}