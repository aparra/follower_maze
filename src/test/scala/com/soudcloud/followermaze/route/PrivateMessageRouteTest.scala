package com.soudcloud.followermaze.route

import org.specs2.mutable.Specification
import com.soudcloud.followermaze.util.Events
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.event.EventType.PRIVATE_MESSAGE
import com.soundcloud.followermaze.transport.route.PrivateMessageRoute

class PrivateMessagetRouteTest extends Specification {

  val privateMessage = Event("1|P|1|2")

  val privateMessageRoute = new PrivateMessageRoute()

  "PrivateMessage Route" should {
    "accept PRIVATE_MESSAGE event" in {
      privateMessageRoute.accept(privateMessage) must beTrue
    }

    Events.except(PRIVATE_MESSAGE).foreach(event => {
      s"not accept ${event.eventType} event" in {
        privateMessageRoute.accept(event) must beFalse  
      }
    })
    
    "notify only user receiver" in {
      privateMessageRoute.process(privateMessage, usersConnected = Set(1)) must beEqualTo(Set(2))
    }
  }
}