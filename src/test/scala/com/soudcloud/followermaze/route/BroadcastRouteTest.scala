package com.soudcloud.followermaze.route

import org.specs2.mutable.Specification
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.transport.route.BroadcastRoute
import com.soudcloud.followermaze.util.Events
import com.soundcloud.followermaze.event.EventType.BROADCAST

object BroadcastRouteTest extends Specification {

  val usersConnected = Set(1)
  val broadcast = Events.event(BROADCAST)

  val broadcastRoute = new BroadcastRoute()

  "Broadcast Route" should {
    "accept BROADCAST event" in {
      broadcastRoute.accept(broadcast) must beTrue
    }

    Events.except(BROADCAST).foreach(event => {
      s"not accept ${event.eventType} event" in {
        broadcastRoute.accept(event) must beFalse  
      }
    })
    
    "notify all users connected" in {
      broadcastRoute.process(broadcast, usersConnected) must beEqualTo(usersConnected)
    }
  }
}