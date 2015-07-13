package com.soundcloud.followermaze.event

import scala.collection.mutable.ListBuffer
import com.soundcloud.followermaze.event.EventType._
import scala.util.parsing.combinator.RegexParsers
import com.soundcloud.followermaze.user.User

case class Event(sequence: Long, eventType: EventType, userFrom: Int, userTo: Int) {

  def this(sequence: Long, eventType: EventType) = this(sequence, eventType, userFrom = User.Unknown, userTo = User.Unknown)

  def this(sequence: Long, eventType: EventType, userFrom: Int) = this(sequence, eventType, userFrom, userTo = User.Unknown)
  
  def is(eventType: EventType): Boolean = this.eventType == eventType
  
  override def toString: String = {
    val payload = ListBuffer(sequence, eventType.flag)
    if (userFrom != User.Unknown) payload += userFrom
    if (userTo != User.Unknown) payload += userTo
    payload.mkString("|")
  }
}

object Event extends RegexParsers {

  def sequence = """(\d+)""".r ^^ { _.toLong }

  def user = """(\d+)""".r ^^ { _.toInt }

  def follow: Parser[Event] = sequence ~ s"|${FOLLOW.flag}|" ~ user ~ "|" ~ user ^^ {
    case sequence ~ _ ~ userFrom ~ _ ~ userTo => Event(sequence, FOLLOW, userFrom, userTo)
  }

  def unfollow: Parser[Event] = sequence ~ s"|${UNFOLLOW.flag}|" ~ user ~ "|" ~ user ^^ {
    case sequence ~ _ ~ userFrom ~ _ ~ userTo => Event(sequence, UNFOLLOW, userFrom, userTo)
  }

  def broadcast: Parser[Event] = sequence ~ s"|${BROADCAST.flag}" ^^ {
    case sequence ~ _ => new Event(sequence, BROADCAST)
  }

  def privateMessage: Parser[Event] = sequence ~ s"|${PRIVATE_MESSAGE.flag}|" ~ user ~ "|" ~ user ^^ {
    case sequence ~ _ ~ userFrom ~ _ ~ userTo => Event(sequence, PRIVATE_MESSAGE, userFrom, userTo)
  }

  def statusEvent: Parser[Event] = sequence ~ s"|${STATUS_EVENT.flag}|" ~ user ^^ {
    case sequence ~ _ ~ userFrom => new Event(sequence, STATUS_EVENT, userFrom)
  }

  def event = follow | unfollow | broadcast | privateMessage | statusEvent

  def apply(payload: String): Event = parseAll(event, payload) match {
    case Success(event, _)  => event
    case error: NoSuccess => throw new IllegalArgumentException("unknown payload")
  }
}
