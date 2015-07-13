package com.soudcloud.followermaze.route

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.transport.route.StatusEventRoute
import com.soudcloud.followermaze.util.Events
import com.soundcloud.followermaze.event.EventType.STATUS_EVENT
import com.soundcloud.followermaze.user.User
import com.soundcloud.followermaze.user.UserRepository

class StatusEventRouteTest extends Specification with Mockito {

  val statusEvent = Event("1|S|100")
  val statusEventRoute = new StatusEventRoute(userRepository)

  "StatusEvent Route" should {
    "accept BROADCAST event" in {
      statusEventRoute.accept(statusEvent) must beTrue
    }

    Events.except(STATUS_EVENT).foreach(event => {
      s"not accept ${event.eventType} event" in {
        statusEventRoute.accept(event) must beFalse  
      }
    })
    
    "notify all follow users" in {
      statusEventRoute.process(statusEvent, usersConnected = Set(1)) must beEqualTo(Set(42))
    }
  }
  
  def userRepository = {
    val user = new User(100) { addFollower(42) }
    val userRepository = mock[UserRepository]
    userRepository.get(user.id) returns user
  }
}