package com.soudcloud.followermaze.event

import org.specs2.matcher.ParserMatchers
import org.specs2.mutable.Specification
import com.soundcloud.followermaze.event.Event._
import com.soundcloud.followermaze.event.{ Event, Broadcast, Follow, PrivateMessage, Unfollow, StatusEvent }

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
    "parser component" should {
      "recognize an event" in {
        val event = follow("100|F|1|2")
        event must beASuccess[Follow]
        event.get.sequence must beEqualTo(100)
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
      "be instance of Follow" in {
        Event("100|F|1|2") must beAnInstanceOf[Follow]
      }
    }
  }

  "Unfollow Event" >> {
    "parser component" should {
      "recognize an event" in {
        val event = unfollow("100|U|1|2")
        event must beASuccess[Unfollow]
        event.get.sequence must beEqualTo(100)
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
      "be instance of Unfollow" in {
        Event("100|U|1|2") must beAnInstanceOf[Unfollow]
      }
    }
  }

  "Broadcast Event" >> {
    "parser component" should {
      "recognize an event" in {
        val event = broadcast("100|B")
        event must beASuccess[Broadcast]
        event.get.sequence must beEqualTo(100)
      }

      "not create an invalid event" in {
        broadcast must failOn("")
        broadcast must failOn("100")
        broadcast must failOn("100|X")
      }
    }

    "payload set to flag 'B'" should {
      "be instance of Broadcast" in {
        Event("100|B") must beAnInstanceOf[Broadcast]
      }
    }
  }

  "Private Message Event" >> {
    "parser component" should {
      "recognize an event" in {
        val event = privateMessage("100|P|1|2")
        event must beASuccess[PrivateMessage]
        event.get.sequence must beEqualTo(100)
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
      "be instance of PrivateMessage" in {
        Event("100|P|1|2") must beAnInstanceOf[PrivateMessage]
      }
    }
  }

  "Status Event" >> {
    "parser component" should {
      "recognize an event" in {
        val event = statusEvent("100|S|1")
        event must beASuccess[StatusEvent]
        event.get.sequence must beEqualTo(100)
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
        Event("100|S|1") must beAnInstanceOf[StatusEvent]
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