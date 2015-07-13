package com.soudcloud.followermaze.event

import org.specs2.matcher.ParserMatchers
import org.specs2.mutable.Specification
import com.soundcloud.followermaze.event.Event
import com.soundcloud.followermaze.event.Event._
import com.soundcloud.followermaze.event.EventType._

class EventTest extends Specification with ParserMatchers {
  val parsers = com.soundcloud.followermaze.event.Event

  "Parser components" >> {
    "event sequence" should {
      "recognize a valid number" in {
        sequence("1234") must beASuccess[Long]
      }

      "failed on invalid number" in {
        sequence must failOn("X")
      }
    }

    "user" should {
      "recognize a valid user_id" in {
        user("100") must beASuccess[Int]
      }

      "failed on invalid user_id" in {
        user must failOn("X")
      }
    }
  }

  "Follow Event" >> {
    val payload = "100|F|1|2"
    
    "parser component" should {
      "recognize an event" in {
        val event = follow(payload).get
        event.is(FOLLOW) must beTrue
        event.toString must beEqualTo(payload)
      }

      "not create an invalid event" in {
        follow must failOn("")
        follow must failOn("100")
        follow must failOn("100|F")
        follow must failOn("100|F|1")
        follow must failOn("100|X|1|2")
      }
    }

    "payload set to flag 'F'" should {
      "be a Follow event" in {
        Event(payload).toString() must beEqualTo(payload)
      }
    }
  }

  "Unfollow Event" >> {
    val payload = "100|U|1|2"
    
    "parser component" should {
      "recognize an event" in {
        val event = unfollow(payload).get
        event.is(UNFOLLOW) must beTrue
        event.toString must beEqualTo(payload)
      }

      "not create an invalid event" in {
        unfollow must failOn("")
        unfollow must failOn("100")
        unfollow must failOn("100|U")
        unfollow must failOn("100|U|1")
        unfollow must failOn("100|X|1|2")
      }
    }

    "payload set to flag 'U'" should {
      "be an Unfollow event" in {
        Event(payload).toString() must beEqualTo(payload)
      }
    }
  }

  "Broadcast Event" >> {
    val payload = "100|B"
    "parser component" should {
      "recognize an event" in {
        val event = broadcast(payload).get
        event.is(BROADCAST) must beTrue
        event.toString must beEqualTo(payload)
      }

      "not create an invalid event" in {
        broadcast must failOn("")
        broadcast must failOn("100")
        broadcast must failOn("100|X")
      }
    }

    "payload set to flag 'B'" should {
      "be a Broadcast event" in {
        Event(payload).toString() must beEqualTo(payload)
      }
    }
  }

  "Private Message Event" >> {
    val payload = "100|P|1|2"
    "parser component" should {
      "recognize an event" in {
        val event = privateMessage(payload).get
        event.is(PRIVATE_MESSAGE) must beTrue
        event.toString must beEqualTo(payload)
      }

      "not create an invalid event" in {
        privateMessage must failOn("")
        privateMessage must failOn("100")
        privateMessage must failOn("100|P")
        privateMessage must failOn("100|P|1")
        privateMessage must failOn("100|X|1|2")
      }
    }

    "payload set to flag 'P'" should {
      "be a PrivateMessage event" in {
        Event(payload).toString() must beEqualTo(payload)
      }
    }
  }

  "Status Event" >> {
    val payload = "100|S|1"
    
    "parser component" should {
      "recognize an event" in {
        val event = statusEvent(payload).get
        event.is(STATUS_EVENT) must beTrue
        event.toString must beEqualTo(payload)
      }

      "not create an invalid event" in {
        statusEvent must failOn("")
        statusEvent must failOn("100")
        statusEvent must failOn("100|S")
        statusEvent must failOn("100|X|1")
      }
    }

    "payload set to flag 'S'" should {
      "be instance of StatusEvent" in {
        Event(payload).toString() must beEqualTo(payload)
      }
    }
  }
  
  "Invalid payload" should {
    "throw an IllegalArgumentException" in {
      Event("") must throwAn[IllegalArgumentException]
      Event("100|X") must throwAn[IllegalArgumentException]
    }
  }
}